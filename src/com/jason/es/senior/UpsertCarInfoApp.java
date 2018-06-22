package com.jason.es.senior;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author : jasonzii @Author
 * @Description : 保存(汽车)信息，更新信息
 * @CreateDate : 18.6.7  21:12
 */
public class UpsertCarInfoApp {

    @SuppressWarnings({"unchecked","resource"})
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                //嗅探整个集群的状态
                .put("client.transport.sniff",true)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings).
                addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"),9300));

        IndexRequest indexRequest = new IndexRequest("car_shop","cars","1")
                .source(XContentFactory.jsonBuilder()
                    .startObject()
                        .field("brand","宝马")
                        .field("name","宝马320")
                        .field("price","310000")
                        .field("produce_date","2017-01-01")
                    .endObject());

        UpdateRequest updateRequest = new UpdateRequest("car_shop","cars","1")
                .doc(XContentFactory.jsonBuilder()
                    .startObject()
                        .field("price",300000)
                    .endObject())
                //插入更新
                .upsert(indexRequest);

        UpdateResponse updateResponse = client.update(updateRequest).get();

        System.out.println(updateResponse.getVersion());

    }


}
