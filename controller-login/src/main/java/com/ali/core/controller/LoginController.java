package com.ali.core.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ali.common.utils.MD5Utils;
import com.ali.common.utils.RequestUtils;
import com.ali.core.pojo.user.Buyer;
import com.ali.core.service.BuyerService;
import com.ali.core.service.SessionService;

/**
 * 单点登录
 * @author +u刘磊
 * @CreateDate 2016年5月14日
 */
@Controller
public class LoginController {
	@Resource
	private BuyerService buyerService;
	@Resource
	private SessionService sessionService;
	
	// 到登录页面,通过枚举进行区别重载
	@RequestMapping(value="/login.aspx",method=RequestMethod.GET)
	public String login(){
		
		return "login";
	}
	
	// 判断用户是否登录
	@RequestMapping("/isLogin.aspx")
	public @ResponseBody
	MappingJacksonValue isLogin(String callback,HttpServletRequest request,HttpServletResponse response){
		Integer result = 0;
		String username = sessionService.getAttribute2Username(RequestUtils.getCSESSIONID(request, response));
		if(null != username){
			result = 1;
		}
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		mjv.setJsonpFunction(callback);
		return mjv;
	}
	
	
	
	
	@RequestMapping(value="/login.aspx",method=RequestMethod.POST)
	public String login(String username,String password,String ReturnUrl,Model model,HttpServletRequest request,HttpServletResponse response){
		// 用户名不能为空
		if(null != username){
			// 密码不能为空
			if(null != password){
				Buyer buyer = buyerService.selectBuyerByName(username);
				// 判断用户名
				if(null != buyer){
					String oldPwd = buyer.getPassword();
					// 给输入密码进行加密
					String encodePassword = MD5Utils.encodePassword(password);
					// 判断密码
					if(encodePassword.equals(oldPwd)){
						// 登录成功,向远程session中加入值.
						String csessionid = RequestUtils.getCSESSIONID(request, response);
						sessionService.setAttribute2Username(csessionid,buyer.getUsername());
						if(null != ReturnUrl){
							// 验证完毕,进行登录
							return "redirect:"+ReturnUrl;
						}else{
							// 如果没有反回路径,就直接回首页
							return "redirect:http://localhost:8082";
						}
					}else{
						model.addAttribute("error", "密码不正确");
					}
				}else{
					model.addAttribute("error", "用户名或密码不正确");
				}
			}else{
				model.addAttribute("error", "密码不能为空");
			}
		}else{
			model.addAttribute("error", "用户名不能为空");
		}
		return "login";
	}
}
