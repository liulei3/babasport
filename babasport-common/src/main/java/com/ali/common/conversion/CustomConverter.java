package com.ali.common.conversion;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义转换器
 * @author +u刘磊
 * @CreateDate 2016年5月8日
 */
public class CustomConverter implements Converter<String, String>{

	@Override
	public String convert(String source) {
		try {
			// 返回非空字符
			if(null != source){
				source = source.trim();
				if(!"".equals(source)){
					return source;
				}
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	
}
