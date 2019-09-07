package com.ali.core.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * 前台测试
 * @author +u刘磊
 * @CreateDate 2016年5月10日
 */
public class PortalTest {

	@Test
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
	}
}
