package com.jason.es.test;

import com.jason.es.util.ESClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @Author : jasonzii @Author
 * @Description : 员工复杂搜索操作
 * @CreateDate : 18.6.4  21:39
 */
public class EmployeeSearchApp {

    public static void main(String[] args) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"),9300));

        prepareData(client);

        executeSearch(client);

        client.close();


    }


    /**
     * 执行搜索操作
     * 搜索index为company，type为employee。职位带有technique,年龄在30~40之间的，分页查询只查询一个
     */
    private static void executeSearch(TransportClient client){

        SearchResponse response = client.prepareSearch("company")
                .setTypes("employee")
                .setQuery(QueryBuilders.matchQuery("position", "technique"))
                .setPostFilter(QueryBuilders.rangeQuery("age").from(30).to(40))
                .setFrom(0).setSize(1)
                .get();

        SearchHit[] hits = response.getHits().getHits();

        for(int i=0 ; i < hits.length; i++){

            System.out.println(hits[i].getSource());
        }


    }


    /**
     * 准备数据
     */
    private static void prepareData(TransportClient client) throws IOException {

        client.prepareIndex("company","employee","1")
                .setSource(XContentFactory.jsonBuilder()
                    .startObject()
                        .field("name","jack")
                        .field("age","27")
                        .field("position","technique software")
                        .field("country","china")
                        .field("join_date","2017-01-01")
                        .field("salary",10000)
                    .endObject())
                .get();


        client.prepareIndex("company","employee","2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name","marry")
                        .field("age","35")
                        .field("position","technique manager")
                        .field("country","china")
                        .field("join_date","2017-01-01")
                        .field("salary",12000)
                        .endObject())
                .get();


        client.prepareIndex("company","employee","3")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name","tom")
                        .field("age","32")
                        .field("position","senior technique software")
                        .field("country","china")
                        .field("join_date","2016-01-01")
                        .field("salary",11000)
                        .endObject())
                .get();


        client.prepareIndex("company","employee","4")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name","jen")
                        .field("age","25")
                        .field("position","junior finance")
                        .field("country","usa")
                        .field("join_date","2016-01-01")
                        .field("salary",7000)
                        .endObject())
                .get();


        client.prepareIndex("company","employee","5")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name","mike")
                        .field("age","37")
                        .field("position","finance manager")
                        .field("country","usa")
                        .field("join_date","2015-01-01")
                        .field("salary",10000)
                        .endObject())
                .get();




    }



}
