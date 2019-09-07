package com.ali.core.service;

import java.util.List;

import com.ali.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface SearchService {
	/*
	 * 插入用户
	 */
	public Pagination selectPageByQuery(Integer pageNo,String keyword,Long brandId,String price) throws Exception;
	
	/*
	 * 获取页面品牌集
	 * @return
	 */
	public List<Brand> selectBrand4Redis();
	
	/*
	 * 向solr中添加商品信息
	 * @param id
	 */
	public void insertProductToSolr(Long id);
}
