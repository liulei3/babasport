package com.ali.core.service.message;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.ali.core.service.SearchService;

/**
 * 自定义监听处理类
 * @author +u刘磊
 * @CreateDate 2016年5月13日
 */
public class CustomMessageListense implements MessageListener{

	@Resource
	private SearchService searchService;

	/*
	 * 监听信息,获取后,调用相应方法,实现数据同步
	 */
	public void onMessage(Message mes) {
		ActiveMQTextMessage amm = (ActiveMQTextMessage)mes;
		try {
			String id = amm.getText();
			searchService.insertProductToSolr(Long.parseLong(id));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
