package com.ali.core.service.product;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.ali.core.dao.product.ProductDao;
import com.ali.core.dao.product.SkuDao;
import com.ali.core.pojo.product.Product;
import com.ali.core.pojo.product.ProductQuery;
import com.ali.core.pojo.product.ProductQuery.Criteria;
import com.ali.core.pojo.product.Sku;
import com.ali.core.pojo.product.SkuQuery;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("productService")
public class ProductServiceImpl implements ProductService{

	@Resource
	private ProductDao productDao;
	
	/**
	 * 分页查询:带查询条件进行页面跳转
	 * <a href="/product/list.do?&amp;isShow=0&amp;pageNo=4">4</a>
	 */
	public Pagination selectProductPageByQuerylist(Integer pageNo,
						String name,Long brandId,Boolean isShow ) {
		ProductQuery productQuery = new ProductQuery();
		// 拼接查询条件
		StringBuilder sBuilder = new StringBuilder();
		
		// 拼接sql条件的对象
		Criteria criteria = productQuery.createCriteria();
		if(null != name){
			criteria.andNameLike("%"+name+"%");
			sBuilder.append("&name=").append(name);
		}
		if(null != brandId){
			criteria.andBrandIdEqualTo(brandId);
			sBuilder.append("&brandId=").append(brandId);
		}
		if(null != isShow){
			/*criteria.andIsShowEqualTo(isShow);
			sBuilder.append("&brandId=").append(brandId);*/
			criteria.andIsShowEqualTo(isShow);
			sBuilder.append("&isShow=").append(isShow);
		}else{
			// isShow的默认值为false,即0;
			criteria.andIsShowEqualTo(false);
			sBuilder.append("&isShow=").append(false);
		}
		/*
		 * Pagination 已定义好的分页类,构造方法有以下参数
		 * pageNo, pageSize, totalCount
		 * ???? productQuery中怎么获取criteria的
		 */
		
		/*
		 * 问题描述:当直接从分页中点击最后一页时,会报错.
		 * 原因:可能由于总记录数不能整除pageSize.造成页码较少
		 *  解决思路:获取总记录数 ,但是未成功  .存在bug
		 *  
		 */
	/*	int totalNo = productDao.countByExample(productQuery);
		if(totalNo%10!=0){
			totalNo+=1;
		}*/
		
		Pagination pagination = new Pagination(Pagination.cpn(pageNo)	//pageNo为null时,值为1
				, 10, productDao.countByExample(productQuery));
		
		//设置查询条件信息
		productQuery.setPageNo(pagination.getPageNo());
		productQuery.setPageSize(pagination.getPageSize());
		
		//设置结果集
		pagination.setList(productDao.selectByExample(productQuery));
		
		//设置分页对象的页面显示内容
		String url = "/product/list.do";
		pagination.pageView(url, sBuilder.toString());
				
		return pagination;
		
	}
	
	@Resource
	private SkuDao skuDao;
	@Resource
	private Jedis jedis;
	/**
	 * 新增商品
	 *  添加商品,添加库存
	 */
	public void insert(Product product) {
		// 1 添加商品
		// 通过jedis生成全服务器唯一id
		Long id = jedis.incr("pno");
		product.setId(id);
		
		// 默认为不上架
		product.setIsShow(false);
		// true(1)表示不删,true(0)删
		product.setIsDel(true);
		product.setCreateTime(new Date());
		productDao.insertSelective(product);
		
		//2 添加库存 库存和商品是一对多的关系
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		for (String color : colors) {
			for(String size : sizes){
				Sku sku = new Sku();
				sku.setColorId(Long.parseLong(color));
				sku.setProductId(product.getId());
				sku.setSize(size);
				sku.setPrice(80f);
				sku.setCreateTime(new Date());
				sku.getDeliveFee();
				sku.setMarketPrice(100f);
				sku.setUpperLimit(100);
				sku.setStock(20);
				skuDao.insertSelective(sku);
			}
		}
	}

	@Resource
	private SolrServer solrServer;
	@Resource
	private JmsTemplate jmsTemplate;
	/**
	 * 设置商品上架
	 * 通过solr索引库提高页面检索效率,需要把数据库的信息在上架时,
	 * 保存到索引库中,这样实现solr的数据源
	 * 上架后,通过activeMQ进行信息传递,solr接收传递信息,同步保存商品信息
	 */
	public void isShow(Long[] ids) {
		Product product = new Product();
		// 设置上架
		product.setIsShow(true);
		
		for (final Long id : ids) {
			// 根据id修改商品上架属性
			product.setId(id);
			productDao.updateByPrimaryKeySelective(product);
			
			/*
			 *  通过匿名内部类,调用发生信息方法
			 *  内部类定义在类中的局部位置上时，只能访问该局部被final修饰的局部变量
			 */
			jmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(String.valueOf(id));
				}
			});
			
		}
	}
}
