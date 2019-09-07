package com.ali.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 用于展示商品详情
 * 包括三部分:获取指定商品,获取颜色,库存,价格信息
 * @author +u刘磊
 * @CreateDate 2016年5月13日
 */
@Service("staticPageService")
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {
	// 声明,以获取模板对象
	private Configuration conf;
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer){
		this.conf= freeMarkerConfigurer.getConfiguration();
	}
	// 获取context,以获取文件的绝对路径
	private ServletContext servletContext;
	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
	}
	// 获取保存地址的绝对路径
	public String getPath(String name){
		return servletContext.getRealPath(name);
	}
	
	// 读取和输出静态化页面
	public void index(Map<String,Object> root,String id) {
		// 获取接收文件
		String path = getPath("/html/product/"+id+".html");
		File file = new File(path);
		// 创建文件夹
		File parentFile = file.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		
		// 获取模板
		Writer writer = null;
		try {
			Template template = conf.getTemplate("product.html");
			// 输出
			writer = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
			// 放行
			template.process(root, writer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if(null != writer){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
