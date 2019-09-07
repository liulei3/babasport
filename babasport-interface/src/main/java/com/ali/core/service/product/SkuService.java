package com.ali.core.service.product;

import java.util.List;

import com.ali.core.pojo.product.Sku;

public interface SkuService {

	public void insert(Sku sku);
	
	public List<Sku> selectByExample(Long productId);
	
	public void updateByPrimaryKeySelective(Sku sku);
}
