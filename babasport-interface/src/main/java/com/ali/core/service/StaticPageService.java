package com.ali.core.service;

import java.util.Map;

public interface StaticPageService {

	public String getPath(String name);
	
	public void index(Map<String,Object> root,String id);
}
