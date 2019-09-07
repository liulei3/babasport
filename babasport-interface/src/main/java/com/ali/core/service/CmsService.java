package com.ali.core.service;

import java.util.List;

import com.ali.core.pojo.product.Product;
import com.ali.core.pojo.product.Sku;

public interface CmsService {

	public Product selectProductById(Long id);
	
	public List<Sku> selectSkuByProductId(Long id);
}