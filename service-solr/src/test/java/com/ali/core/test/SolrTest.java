package com.ali.core.test;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ali.core.pojo.User;
import com.ali.core.service.UserService;

/**
 * 前台测试
 * @author +u刘磊
 * @CreateDate 2016年5月10日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class SolrTest {
		
	//注入service
	@Resource
	private SolrServer solrServer;
	
	@Test
	public void testSolr2() throws SolrServerException, IOException{
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", 64);
		document.setField("name", "流浪");
		solrServer.add(document);
		solrServer.commit();
	}
	
/*	未使用solr配置文件时的连接方式
 * @Test
	public void testSolr() throws SolrServerException, IOException{
		// 修改过名字,所以必须加上solr的名称????
		String baseURL = "http://192.168.200.128:8080/solr";
		
		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		// 保存
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", 2);
		document.setField("name", "22");
		solrServer.add(document);
		solrServer.commit();
	}*/
}
