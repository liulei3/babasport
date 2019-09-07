package com.ali.core.pojo.cart;

import java.io.Serializable;

import com.ali.core.pojo.product.Sku;

/**
 * 购物项
 * @author +u刘磊
 * @CreateDate 2016年5月16日
 */
public class BuyerItem implements Serializable{
	private static final long serialVersionUID = 6368084211652500584L;
	
	// sku对象
	private Sku sku;
	// 是否有货 设置默认值为true
	private Boolean isHave = true;
	// 购买数量 默认为1
	private Integer amount = 1;
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}
	public Boolean getIsHave() {
		return isHave;
	}
	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
