package com.ali.core.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.core.pojo.User;
import com.ali.core.service.UserService;

/**
 * 后台管理中心
 * @author +u刘磊
 * @CreateDate 2016年5月5日
 */
@Controller
@RequestMapping("/control")
public class CenterController {

	/*@Resource
	private UserService userService;*/
	
	//进入后台入口
	@RequestMapping(value="/{id}")
	public String index(@PathVariable String id){
		return id;
	}

	//进入后台入口
	@RequestMapping(value="/{frame}/{method}")
	public String brand(@PathVariable String method){
		return "frame/"+method;
	}
	
}
