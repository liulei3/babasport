package com.ali.core.controller.product;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.core.pojo.product.Brand;
import com.ali.core.pojo.product.Sku;
import com.ali.core.service.product.SkuService;

import cn.itcast.common.page.Pagination;

/**
 * 接收页面发送的json格式数据,通过注解@RequestBody
 * 响应回去json格式数据,通过注解@ResponseBody
 * 当接收字段和页面字段不一致时,用@RequestParam设置接收
 * @author +u刘磊
 * @CreateDate 2016年5月9日
 */
@Controller
@RequestMapping("/sku")
public class SkuController {

	@Resource
	private SkuService skuService;
	
	@RequestMapping(value = "/list.do")
	public String list(Long productId,Model model){
		
		// 查询指定商品库存
		List<Sku> skus = skuService.selectByExample(productId);
		
		model.addAttribute("skus", skus);
		
		return "sku/list";
	}
	
	// 加载添加页面内容
	@RequestMapping(value = "/addSku.do")
	public void addSku(@RequestBody Sku sku,HttpServletResponse response) {
		// 设置json信息返回值
		JSONObject jsonObject = new JSONObject();
		try {
			skuService.updateByPrimaryKeySelective(sku);
			jsonObject.put("message", "保存成功");
		} catch (Exception e) {
			jsonObject.put("message", "失败了,欧耶");
		}
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
