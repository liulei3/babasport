package com.ali.core.service;


import org.springframework.stereotype.Service;

import com.ali.common.utils.FastDFSUtils;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

	/*
	 * 图片上传
	 * 返回相对路径 group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
	 */
	public String uploadPic(byte[] pic, String name, Long size) throws Exception {
		return FastDFSUtils.uploadPic(pic, name, size);
	}

}
