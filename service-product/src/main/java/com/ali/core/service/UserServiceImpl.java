package com.ali.core.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ali.core.dao.UserDao;
import com.ali.core.pojo.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	
	//注入dao
	@Autowired
	private UserDao userDao;
	
	public void insertUser(User user) {
		userDao.insert(user);
		throw new RuntimeException();
	}

}
