package com.ali.common.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取sessionid
 * cookie中有,直接获取;没有直接创建一个
 * @author +u刘磊
 * @CreateDate 2016年5月14日
 */
public class RequestUtils {

	public static String getCSESSIONID(HttpServletRequest request,HttpServletResponse response){
		
//		1、	获取Cookie
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length > 0){
			for (Cookie cookie : cookies) {
//		2、	获取Cookie中的CSESSIONID
				if("CSESSIONID".equals(cookie.getName())){
//		3、	有 直接使用
					return cookie.getValue();
				}
			}
			
		}
//		4、	没有 创建一个 返回给浏览器（Cookie放里）
		String csesssionid = UUID.randomUUID().toString().replaceAll("-", "");
		Cookie cookie = new Cookie("CSESSIONID",csesssionid);
		//设置存活时间
		cookie.setMaxAge(-1);
		//设置路径
		cookie.setPath("/");
		response.addCookie(cookie);
//		5、	直接使用
		return csesssionid;
	}
}