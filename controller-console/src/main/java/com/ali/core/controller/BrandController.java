package com.ali.core.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.core.pojo.product.Brand;
import com.ali.core.service.BrandService;

import cn.itcast.common.page.Pagination;

/**
 * 后台管理中心
 * @author +u刘磊
 * @CreateDate 2016年5月5日
 */
@Controller
public class BrandController {

	@Resource
	private BrandService brandService;

	//进入后台入口
	@RequestMapping(value="/brand/list.do")
	public String list(Integer pageNo,String name,Integer isDisplay,Model model){
		Pagination pagination = brandService.selectBrandPageByQuery(pageNo,name, isDisplay);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		
		return "brand/list";
	}
	
	//进入编辑页面
	@RequestMapping(value="/brand/toEdit.do")
	public String toEdit(Integer id,Model model){
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		
		return "brand/edit";
	}
		
	//保存编辑数据
	@RequestMapping(value="/brand/edit.do")
	public String edit(Brand brand){
		brandService.updateBrandById(brand);
		
		return "redirect:/brand/list.do";
	}
	
	// 批量删除 
	@RequestMapping(value="/brand/deletes.do")
	public String deleteByIds(Long[] ids){
		brandService.deleteByIds(ids);
		
		//return "redirect:/brand/list.do";
		// 由于是get请求,所以要使用forward
		return "forward:/brand/list.do";
	}	
}
