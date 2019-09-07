package com.ali.test;

import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ali.core.pojo.User;
import com.ali.core.service.UserService;

import redis.clients.jedis.Jedis;

/**
 * 测试类 注解方式
 * @author +u刘磊
 * @CreateDate 2016年5月5日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestUser {
	
	//注入service
	@Resource
	private UserService userService;
	
	@Test
	public void testUser(){
		User user = new User();
		user.setUsername("张三的歌3");
		user.setBirthday(new Date());
		userService.insertUser(user);
	}
	
	@Resource
	private Jedis jedis;
	
	@Test
	public void testJedis(){
		jedis.set("dd", "niu");
		jedis.close();
	}
}
