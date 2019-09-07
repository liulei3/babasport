package com.ali.core.service;

import java.util.List;

import com.ali.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface UploadService {
	
	public String uploadPic(byte[] pic,String name,Long size) throws Exception;
}
