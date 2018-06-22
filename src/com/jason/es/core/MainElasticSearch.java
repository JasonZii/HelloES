package com.jason.es.core;


import com.jason.es.util.ESClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

/**
 * @Author : jasonzii @Author
 * @Description :
 * @CreateDate : 18.5.30  17:15
 */
public class MainElasticSearch {

    private static final Logger logger = Logger.getLogger(MainElasticSearch.class);


    /**
     * ElasticSearch 入口
     */
    public static void main(String[] args) throws Exception {

        LogManager.resetConfiguration();
        //加载配置文件
        Properties props = new Properties();
        props.load(MainElasticSearch.class.getClassLoader().getResourceAsStream("log4j.properties"));
        PropertyConfigurator.configure(props);

        //初始化es
        ESClient.init();
        logger.info("es初始化完毕");


    }




}
