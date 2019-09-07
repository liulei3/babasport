package com.ali.core.service.product;

import com.ali.core.pojo.product.Product;

import cn.itcast.common.page.Pagination;

public interface ProductService {

	public Pagination selectProductPageByQuerylist(Integer pageNo,
			String name,Long brandId,Boolean isShow );
	
	public void insert(Product product);
	
	public void isShow(Long[] ids);
}
