package com.ali.core.service.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.ali.core.pojo.product.Color;
import com.ali.core.pojo.product.Product;
import com.ali.core.pojo.product.Sku;
import com.ali.core.service.CmsService;
import com.ali.core.service.StaticPageService;

/**
 * 自定义监听类
 * 商品上架后,静态页面和商品详细页获取监听对象
 * @author +u刘磊
 * @CreateDate 2016年5月13日
 */
public class CustomMessageListense implements MessageListener{
	@Resource
	private CmsService cmsService;
	@Resource
	private StaticPageService staticPageService;
	
	// 获取上架后提交的信息
	public void onMessage(Message message) {
		
		ActiveMQTextMessage amm = (ActiveMQTextMessage)message;
		try {
			// 商品id
			String id = amm.getText();
			// 静态化
			Map<String, Object> root = new HashMap<String,Object>();
			Product product = cmsService.selectProductById(Long.parseLong(id));
			root.put("product", product);
			// 获取sku结果集 ,颜色
			List<Sku> skus = cmsService.selectSkuByProductId(Long.parseLong(id));
			root.put("skus", skus);
			
			// 比较颜色的id
			Set<Color> colors = new HashSet<Color>();
			for (Sku sku : skus) {
				colors.add(sku.getColor());
			}
			root.put("colors", colors);
			
			staticPageService.index(root, id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
