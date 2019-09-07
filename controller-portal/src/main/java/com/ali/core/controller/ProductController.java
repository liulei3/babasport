package com.ali.core.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.common.utils.RequestUtils;
import com.ali.core.pojo.product.Color;
import com.ali.core.pojo.product.Product;
import com.ali.core.pojo.product.Sku;
import com.ali.core.service.CmsService;
import com.ali.core.service.SessionService;

/**
 * 前台页面
 * @author +u刘磊
 * @CreateDate 2016年5月10日
 */
@Controller
public class ProductController {
	@Resource
	private CmsService cmsService;

	
	@RequestMapping("/product/detail")
	public String detail(Long id,Model model) throws Exception{
			
		Product product = cmsService.selectProductById(id);
		List<Sku> skus = cmsService.selectSkuByProductId(id);
		model.addAttribute("product", product);
		model.addAttribute("skus", skus);
		// 需要对商品的颜色去重
		Set<Color> colors = new HashSet<Color>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		model.addAttribute("colors", colors);
		
		return "product";
		
	}

}
