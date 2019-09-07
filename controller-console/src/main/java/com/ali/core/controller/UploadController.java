package com.ali.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.ali.common.constant.Constants;
import com.ali.core.service.UploadService;

/**
 * 后台管理中心
 * @author +u刘磊
 * @CreateDate 2016年5月5日
 */
@Controller
public class UploadController {
	
	@Resource
	private UploadService uploadService;

	/*
	 *  ajax进行图片上传 
	 *  图片上传有两种方式:
	 *  	第一种:通过tomcat响应ajax请求
	 *  	第二种:通过fastDFS图片服务器
	 */
	// 方式一 : tomcat响应 通过响应头进行数据传输,不用返回值
	/*@RequestMapping(value="/upload/uploadPic.do")
	public void upload(@RequestParam(required = false) MultipartFile pic,
			HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException{
		// 获取全路径
		String realPath = request.getSession().getServletContext().getRealPath("/upload")+"/";
		System.out.println(realPath+"----------------------------------");
		
		 *  获取扩展名
		 *  FilenameUtils.getExtension:通过内置工具类,获取扩展名
		 *  pic.getOriginalFilename():获取原始文件名
		 
		String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
		
		// 生成文件名
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String name = dateFormat.format(new Date());
		//外加随机的三位数,进一步保证唯一性
		Random random = new Random();
		for (int i = 0; i<3 ;i++) {
			name+=random.nextInt(10);
		}
		
		// 设置文件存储路径
		String url = realPath+name+"."+ext;
		
		// 传输文件到指定地点
		pic.transferTo(new File(url));
		
		//设置json格式路径返回值
		JSONObject jo = new JSONObject();
		jo.put("url", "/upload/"+name+"."+ext);
		jo.put("path", realPath);
		
		//返回值
		//设置编码
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(jo.toString());
		
		System.out.println(jo.toString());
	}*/
	
	/*
	 *  方式二 : fastDFS 图片服务器 
	 *  文件存储地址
	 *  http://192.168.200.128/group1/M00/00/01/*.jpg
	 */
	@RequestMapping(value="/upload/uploadPic.do")
	public void upload(@RequestParam(required = false) MultipartFile pic,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 获取图片存储相对路径 
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		
		//设置json格式返回值
		String url = Constants.IMG_URL + path;
		JSONObject jo = new JSONObject();
		jo.put("url", url);
		
		// 通过response响应
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(jo.toString());
	}
	
	/**
	 * 多图片上传
	 * @param pic
	 * @return 图片地址集合
	 * @throws Exception
	 */
	@RequestMapping(value="/upload/uploadPics.do")
	public @ResponseBody List<String> uploads(@RequestParam(required = false) MultipartFile[] pics) throws Exception{
		List<String> list = new ArrayList<String>();
		for (MultipartFile pic : pics) {
			System.out.println(pic);
			// 获取图片存储相对路径 
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			
			//设置json格式返回值
			String url = Constants.IMG_URL + path;
			list.add(url);
		}
		// 通过@ResponseBody转JSON
		return list;
	}

	/**
	 * kindEditor富文本编辑器 无敌接收,不用文件名,即可接收文件
	 * 作用: 可以接收单张,多张图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 获取文本参数 图片或文件
		MultipartRequest mRequest = (MultipartRequest) request;
		// 获取文件Map集合
		Map<String, MultipartFile> fileMap = mRequest.getFileMap();
		Set<Entry<String, MultipartFile>> keyset = fileMap.entrySet();
		// 获取集合中值
		for (Entry<String, MultipartFile> entry : keyset) {
			MultipartFile pic = entry.getValue();
			// 获取图片存储相对路径 
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			
			//设置json格式返回值
			String url = Constants.IMG_URL + path;
			JSONObject jo = new JSONObject();
			jo.put("url", url);
			
			// 设置响应信息,告诉前台接收成功
			jo.put("error", 0);
			
			// 通过response响应
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(jo.toString());
		}
	}
		
}
