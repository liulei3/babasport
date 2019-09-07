package com.ali.core.controller.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.core.pojo.product.Brand;
import com.ali.core.pojo.product.Color;
import com.ali.core.pojo.product.Product;
import com.ali.core.service.BrandService;
import com.ali.core.service.product.ColorService;
import com.ali.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;

/**
 * 接收页面发送的json格式数据,通过注解@RequestBody
 * 响应回去json格式数据,通过注解@ResponseBody
 * 当接收字段和页面字段不一致时,用@RequestParam设置接收
 * @author +u刘磊
 * @CreateDate 2016年5月9日
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	@Resource
	private ProductService productService;
	@Resource
	private BrandService brandService;
	
	@RequestMapping(value = "/list.do")
	public String list(Integer pageNo,String name,Long brandId,Boolean isShow,Model model){
		
		// 查询品牌 1 代表显示的品牌
		List<Brand> brands = brandService.selectBrandListByQuery(null, 1);
		model.addAttribute("brands", brands);
		
		// 获取分页对象
		Pagination pagination = productService.selectProductPageByQuerylist(pageNo, name, brandId, isShow);
		model.addAttribute("pagination", pagination);
		// 设置条件回显
		model.addAttribute("name", name);
		model.addAttribute("brandId",brandId );
		model.addAttribute("isShow", isShow);
		
		return "product/list";
	}
	
	@Resource
	private ColorService colorService;
	// 加载添加页面内容
	@RequestMapping(value = "/toAdd.do")
	public String toAdd(Model model){
		// 加载品牌结果集 1 代表显示的品牌
		List<Brand> brands = brandService.selectBrandListByQuery(null, 1);
		model.addAttribute("brands", brands);
		
		// 加载颜色结果集
		List<Color> colors = colorService.selectByExample();
		model.addAttribute("colors", colors);
		
		return "product/add";
	}
	
	/*
	 *  商品添加,包括两部分:
	 *  1 添加商品表数据信息
	 *  2 添加库存表数据信息
	 */
	@RequestMapping(value = "/add.do")
	public String add(Product product){
		productService.insert(product);
		
		return "redirect:/product/list.do";
	}
	
	/**
	 * 
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/isShow.do")
	public String isShow(Long[] ids){
		productService.isShow(ids);
		// 指定转发
		return "forward:/product/list.do";
	}
}
