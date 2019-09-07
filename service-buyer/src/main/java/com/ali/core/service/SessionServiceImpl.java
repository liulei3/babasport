package com.ali.core.service;

import javax.annotation.Resource;
import redis.clients.jedis.Jedis;

/**
 * 生成远程session
 * 不用注解自动生成SessionServiceImpl的原因:
 * exp变量需要设置,自定义bean方便传递参数
 * @author +u刘磊
 * @CreateDate 2016年5月14日
 */
public class SessionServiceImpl implements SessionService{
	@Resource
	private Jedis jedis;
	// 设置session存活时间
	private Integer exp;
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public void setAttribute2Username(String csessionid,String value){
		jedis.set(csessionid + ":USER_SESSION", value);
		jedis.expire(csessionid + ":USER_SESSION", 60*exp);
	}
	public String getAttribute2Username(String csessionid){
		String value = jedis.get(csessionid + ":USER_SESSION");
		// 每次获取都重置session
		if(null != value){
			jedis.expire(csessionid + ":USER_SESSION", 60*exp);
		}
		return value;
	}
}
