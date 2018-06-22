package com.jason.es.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件工具类
 */
public class PropertiesUtil {
	private static Properties p = null;
	private static final String INIT_FILE = "system.properties";
	private static String REDIS_IP = "redis_ip";
	private static String REDIS_PORT = "redis_port";	
	private static String REDIS_PWD = "redis_pwd";	
	
	private static String NGINX_IP = "nginx_ip";
	private static String NGINX_PORT = "nginx_port";
	
	
	private static String KAFKA_IP = "kafka_ip";
	private static String KAFKA_PORT = "kafka_port";
	private static String ZOOKEEPER_KAFKA_PORT = "zookeeper_kafka_port";
	
	private static String CLUSTER_NAME = "cluster.name";
	private static String ELASTIC_IP = "elastic.ip";
	
	private static void init(){
		try {
			p = new Properties();
			p.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(INIT_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static String getRedisIp(){
		if (p==null){
			init();
		}
		return p.getProperty(REDIS_IP);
	}
	
	public static Integer getRedisPort(){
		if (p==null){
			init();
		}
		return Integer.valueOf(p.getProperty(REDIS_PORT));
	}
	public static String getRedisPwd(){
		if (p==null){
			init();
		}
		return p.getProperty(REDIS_PWD);
	}	
	
	public static String getAdurl(){
		if (p==null){
			init();
		}
		String ip = p.getProperty(NGINX_IP);
		String port = p.getProperty(NGINX_PORT);
		String preUrl = "http://"+ip+":"+port+"/ad";
		return preUrl;
	}
	
	public static String getKafkaIp(){
		if (p==null){
			init();
		}
		String ip = p.getProperty(KAFKA_IP);
		return ip;
	}
	
	public static String getKafkaPort(){
		if (p==null){
			init();
		}
		String port = p.getProperty(KAFKA_PORT);
		return port;
	}
	
	public static String getZookeeperKafkaPort(){
		if (p==null){
			init();
		}
		String port = p.getProperty(ZOOKEEPER_KAFKA_PORT);
		return port;
	}
	public static String getClusterName(){
		if (p==null){
			init();
		}
		return p.getProperty(CLUSTER_NAME);
	}
	public static String getElasticIp(){
		if (p==null){
			init();
		}
		return p.getProperty(ELASTIC_IP);
	}
}
