package com.ali.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ali.core.dao.product.BrandDao;
import com.ali.core.pojo.product.Brand;
import com.ali.core.pojo.product.BrandQuery;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Resource
	private BrandDao brandDao;
	
	/**
	 * 条件查询
	 */
	public List<Brand> selectBrandListByQuery(String name,Integer isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		if(null != name ){
			brandQuery.setName(name);
		}
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
		}else{
			brandQuery.setIsDisplay(1);
		}
				
		return brandDao.selectBrandListByQuery(brandQuery);
	}
	
	/**
	 * 分页查询:带查询条件进行页面跳转
	 * <a href="/product/list.do?&amp;isShow=0&amp;pageNo=4">4</a>
	 */
	public Pagination selectBrandPageByQuery(Integer pageNo,String name,Integer isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		// 拼接查询条件
		StringBuilder sBuilder = new StringBuilder();
		if(null != name){
			brandQuery.setName(name);
			sBuilder.append("&name=").append(name);
		}
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
			sBuilder.append("&isDisplay=").append(isDisplay);
		}else{
			brandQuery.setIsDisplay(1);
			sBuilder.append("&isDisplay=").append(1);
		}
		/*
		 * Pagination 已定义好的分页类,构造方法有以下参数
		 * pageNo, pageSize, totalCount
		 */
		Pagination pagination = new Pagination(Pagination.cpn(pageNo)	//pageNo为null时,值为1
				, 3, brandDao.selectBrandTotalNo(brandQuery));
		
		//设置查询条件信息
		brandQuery.setPageNo(pagination.getPageNo());
		brandQuery.setPageSize(pagination.getPageSize());
		
		//设置结果集
		pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
		
		//设置分页对象的页面显示内容
		String url = "/brand/list.do";
		pagination.pageView(url, sBuilder.toString());
				
		return pagination;
		
	}

	/**
	 * 根据id查询
	 */
	public Brand selectBrandById(Integer id) {
		return brandDao.selectBrandById(id);
	}
	
	@Resource
	private Jedis jedis;
	/**
	 * 根据id更新,因为前台有根据品牌检索,
	 * 所以添加品牌和更新品牌时将品牌放入redis索引库中
	 */
	public void updateBrandById(Brand brand) {
		// 前台实际需要的是商品名,通过商品id进行区别
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
		brandDao.updateBrandById(brand);
	}

	/**
	 * 根据id,批量删除对象
	 */
	public void deleteByIds(Long[] ids) {
		brandDao.deleteByIds(ids);
	}

}
