package com.ali.core.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.Cookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.common.utils.RequestUtils;
import com.ali.core.pojo.cart.BuyerItem;
import com.ali.core.pojo.cart.Cart;
import com.ali.core.pojo.product.Sku;
import com.ali.core.service.BuyerService;
import com.ali.core.service.SessionService;

/**
 * 购物车
 * @author +u刘磊
 * @CreateDate 2016年5月17日
 */
@Controller
public class CartController {
	@Resource
	private SessionService sessionService;
	@Resource
	private BuyerService buyerService;
	
	// 加入购物车
	@RequestMapping("/shopping/cart")
	public String cart(Long skuId,Integer amount,Model model,HttpServletResponse response,HttpServletRequest request) throws Exception{
		// 进行判断,如果没有登录,强制登录,这样可以减少后台判断,简化代码,提高访问效率
		String username = sessionService.getAttribute2Username(RequestUtils.getCSESSIONID(request, response));
		if(null != username){
			// 1 获取cookie
			// 2 判断cookie是否有购物车
			
			// 3 没有 创建购物车
			Cart cart = new Cart();
			// 4 追加当前商品
			if(null != skuId && null != amount){
				Sku sku = buyerService.selectSkuById(skuId);
				BuyerItem item = new BuyerItem();
				item.setSku(sku);
				item.setAmount(amount);
				// 加入购物车
				cart.addItem(item);
			}
			// 5 将购物车中所有的商品追加到redis中
			buyerService.insertCart2Redis(cart, username);
			// 6 清空cookie
			// 7 重定向	
			model.addAttribute("cart", cart);
			return "cart";
		}else{
			model.addAttribute("error", "请登录");
			// 问题 设置ReturnUrl
			return "redirect:http://localhost:8081/login.aspx";
		}
	}
	
	
	// 进入购物车
/*	@RequestMapping("/shopping/toCart")
	public String toCart(){
		// 1 获取cookie
		// 2 判断购物车
		// 3 没有 创建购物车
		Cart cart = new Cart();
		// 4 将购物车中所有的商品追加到redis中
		buyerService.
		// 5 获取redis中的购物车
		// 6 填充购物车数据
		// 7 数据回显
		// 8 页面跳转
	}*/
	
	

}
