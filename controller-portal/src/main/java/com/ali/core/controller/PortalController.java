package com.ali.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.core.pojo.product.Brand;
import com.ali.core.service.SearchService;

import cn.itcast.common.page.Pagination;

/**
 * 前台页面
 * @author +u刘磊
 * @CreateDate 2016年5月10日
 */
@Controller
public class PortalController {
	@Resource
	private SearchService searchService;
	@RequestMapping("/")
	public String index(){
		return "index";
	}
	
	
	@RequestMapping("/search")
	public String search(Integer pageNo, String keyword,Long brandId,String price,Model model) throws Exception{
		// 获取页面的商品集
		List<Brand> brands = searchService.selectBrand4Redis();
		model.addAttribute("brands", brands);
		
		// 分页
		Pagination pagination = searchService.selectPageByQuery(pageNo, keyword,brandId,price);
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		
		// 设置选择中条件的页面显示
		Map<String, String> map = new HashMap<String,String>();
		if(null != brandId){
			for (Brand brand : brands) {
				if(brandId.equals(brand.getId())){
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		if(null != price){
			if(price.contains("-")){
				map.put("价格", price);
			}else{
				map.put("价格", price+"及以上");
			}
			
		}
		model.addAttribute("map", map);
		
		return "search";
	}
}
