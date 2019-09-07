package com.ali.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.ali.core.dao.product.BrandDao;
import com.ali.core.dao.product.ProductDao;
import com.ali.core.dao.product.SkuDao;
import com.ali.core.pojo.product.Brand;
import com.ali.core.pojo.product.BrandQuery;
import com.ali.core.pojo.product.Product;
import com.ali.core.pojo.product.ProductQuery;
import com.ali.core.pojo.product.Sku;
import com.ali.core.pojo.product.SkuQuery;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("searchService")
public class SearchServiceImpl implements SearchService{
	@Resource
	private SolrServer solrServer;
	/**
	 * solr实现分页查询
	 *  注意:每次都要手动启动redis服务器
	 * @throws Exception 
	 */
	public Pagination selectPageByQuery(Integer pageNo, String keyword,Long brandId,String price) throws Exception {
		//查询Solr服务器
		ProductQuery productQuery = new ProductQuery();
		
		SolrQuery solrQuery = new SolrQuery();
		
		StringBuilder params = new StringBuilder();
		//关键词
		solrQuery.set("q", "name_ik:" + keyword);
		params.append("keyword=").append(keyword);
//		高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		// 设置高亮的前台标签<span color="red"></span>
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		
//		排序
		solrQuery.addSort("price", ORDER.asc);
		
//		过滤条件
		if(null != brandId){
			solrQuery.addFilterQuery("brandId:"+brandId);
			params.append("&brandId=").append(brandId);
		}
		if(null != price){
			String[] split = price.split("-");
			if(split.length == 2){
				solrQuery.addFilterQuery("price:["+split[0]+" TO "+split[1]+"]");
			}else{
				solrQuery.addFilterQuery("price:["+split[0]+" TO * ]");
			}
			params.append("&price=").append(price);
		}
		

//		分页（查询出结果集从Solr中）  返回总条数  当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(8);
		
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
//				分页对象
		//返回结果
		QueryResponse response = solrServer.query(solrQuery);
		//结果集
		SolrDocumentList docs = response.getResults();
		//总条数
		long numFound = docs.getNumFound();
		
		//创建商品结果集
		List<Product> products = new ArrayList<>();
		
		// 取出添加高亮后的查询结果(加不加高亮自定义)
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		for (SolrDocument doc : docs) {
			Product product = new Product();
			//商品ID
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			//商品名称
//			String name = (String) doc.get("name_ik");
			/*
			 *  通过key获取值
			 *  第一个key为商品id
			 *  第二个key为上面名称
			 */
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			product.setName(list.get(0));
			//价格
			product.setPrice((Float) doc.get("price"));
			//图片
			String url = (String) doc.get("url");
			product.setImgUrl(url);
			//品牌ID
			product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));
			products.add(product);
			
		}
		
		//当前页
		//每页数
		//总条数
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				(int)numFound
				);
		//设置结果集
		pagination.setList(products);
		//分页在页面展示
		String url = "/search";
		pagination.pageView(url, params.toString());
		return pagination;
	}
	
	@Resource
	private Jedis jedis;
	@Resource
	private BrandDao brandDao;
	/**
	 * 从redis中查询商品品牌
	 */
	public List<Brand> selectBrand4Redis(){
		Map<String, String> map = jedis.hgetAll("brand");
		if(null != map){
			// 获取map的键值对关系
			Set<Entry<String, String>> entrySet = map.entrySet();
			if(entrySet.size()>0){
				// 建立返回值集合
				List<Brand> brands = new ArrayList<Brand>();
				for (Entry<String, String> entry : entrySet) {
					Brand brand = new Brand();
					brand.setId(Long.parseLong(entry.getKey()));
					brand.setName(entry.getValue());
					brands.add(brand);
				}
				return brands;
			}
		}
		// 当redis索引库中没有时,从数据库中查找
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setIsDisplay(1);
		return brandDao.selectBrandListByQuery(brandQuery);
	}

	@Resource
	private ProductDao productDao;
	@Resource
	private SkuDao skuDao;
	
	/**
	 * 通过activeMQ获取商品上架后传递的信息
	 * 关键:从activeMQ信息的获取
	 * 操作:自定义监听处理类,当发生信息后,通过监听获取信息,监听类调用下面方法,实现数据同步
	 * @param id
	 */
	public void insertProductToSolr(Long id){
		/*
		 *  同步solr于数据库数据
		 *  solr索引库可检索的字段有:
		 *  id,name,imageUrl,price,brandId
		 */
		SolrInputDocument solrDocument = new SolrInputDocument();
		// 1 设置id
		solrDocument.setField("id", id);
		// 获取数据库上架商品的数据
		Product pro = productDao.selectByPrimaryKey(id);
		// 2 设置商品名
		solrDocument.setField("name_ik", pro.getName());
		// 3 设置图片路径
		solrDocument.setField("url", pro.getImgUrl());
		/*
		 * 设置商品价格,需要选取最低价 sql语句如下
		 * select price from bbs_sku where product_id = 442 order by price asc limit 0,1
		 */
		// 配置最低价查询语句
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);	//条件是product_id=id
		skuQuery.setFields("price"); 	//查询字段为price
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);	//最低价就是升序后,第一页第一个
		skuQuery.setPageSize(1);
		
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		// 4 设置商品最低价
		solrDocument.setField("price", skus.get(0).getPrice());
		// 5 设置商品品牌
		solrDocument.setField("brandId", pro.getBrandId());
		try {
			solrServer.add(solrDocument);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
