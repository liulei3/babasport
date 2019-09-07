package com.ali.core.service;

import java.util.List;

import com.ali.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface BrandService {
	
	public List<Brand> selectBrandListByQuery(String name,Integer isDisplay);
	
	public Pagination selectBrandPageByQuery(Integer pageNo,String name,Integer isDisplay);
	
	public Brand selectBrandById(Integer id);
	
	public void updateBrandById(Brand brand);
	
	public void deleteByIds(Long[] ids);
}
