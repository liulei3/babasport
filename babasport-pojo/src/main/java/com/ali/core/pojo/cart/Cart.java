package com.ali.core.pojo.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable{
	private static final long serialVersionUID = 9172874528085390378L;
	// 购物项
	List<BuyerItem> items = new ArrayList<>();

	// 加入订单项
	public void addItem(BuyerItem item){
		items.add(item);
	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}
	
	
}
