package com.jason.es.test;

import com.jason.es.util.ESClient;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author : jasonzii @Author
 * @Description :
 * @CreateDate : 18.5.31  21:40
 */
public class IndexTest {

    public static void main(String[] args) throws Exception {

        //获取客户端
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(
                        InetAddress.getByName("192.168.25.128"), 9300));

        indexGet(client);
        //indexCreate(client);
        //indexDelete(client,"AVPmcmhdem-ZMWF0VMHm");

//        get(client);

        // 先构建client
        /*Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.25.128"),9300));
        createEmployee(client);
        client.close();*/


    }

    public static void indexGet(Client client){
        SearchResponse res = null;
        res = client.prepareSearch("accounts").setTypes("person").get();

        System.out.println(res);
        //on shutdown
        client.close();
    }

    public static void get(Client client){
        GetResponse response = client.prepareGet("accounts","person","1").get();
        String res = response.getSourceAsString();
        System.out.println(res);

    }


}
