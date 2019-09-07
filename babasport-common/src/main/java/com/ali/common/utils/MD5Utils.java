package com.ali.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * md5加密工具类 
 * 这里进行了两次加密
 * 1 md5	2 hex
 */
public class MD5Utils {
	
	public static String encodePassword(String password){
		String algorithm = "MD5";
		// 获取md5实例
		MessageDigest instance;
		char[] encode = null;
		try {
			instance = MessageDigest.getInstance(algorithm);
			//	加密
			byte[] digest = instance.digest(password.getBytes());
			// 进行hex加密
			encode = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(encode);
	}
}
