package com.jason.es.util;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * ESClient 客户端初始化，连接，关闭，获取客户端信息等
 */
public class ESClient {
	
	private static final Logger logger = Logger.getLogger(ESClient.class);
	private static TransportClient client;
	 
	@SuppressWarnings("resource")
	public static void init(){
		String clusterName = PropertiesUtil.getClusterName();
		String elasticIp = PropertiesUtil.getElasticIp();
		Settings settings = Settings.builder()
				.put("cluster.name", clusterName)
				.build();

		try {
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticIp), 9300));
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
			
		} 

    }
 
    public static void close() {
    	if (client != null){
    		 client.close();
    	}
    }
    
    public static TransportClient getClient(){
    	if(client == null){
    		init();
    	}
		return client;
    	
    }

    public static void main(String[] args) throws Exception {
        ESClient.init();
        ESClient.close();
        getClient();
    }
}
