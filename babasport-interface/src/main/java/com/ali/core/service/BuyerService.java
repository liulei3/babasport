package com.ali.core.service;

import com.ali.core.pojo.cart.Cart;
import com.ali.core.pojo.product.Sku;
import com.ali.core.pojo.user.Buyer;

public interface BuyerService {
	
	public Buyer selectBuyerByName(String name);
	
	public Sku selectSkuById(Long skuId);
	
	public void insertCart2Redis(Cart cart,String username);
	
	public Cart selectCart4Redis(String name);
}
