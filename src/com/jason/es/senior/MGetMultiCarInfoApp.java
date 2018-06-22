package com.jason.es.senior;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author : jasonzii @Author
 * @Description : multi  一次性将多个document的数据查询出来，放在一起显示
 * @CreateDate : 18.6.9  13:01
 */
public class MGetMultiCarInfoApp {

    public static void main(String[] args) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"),9300));

        MultiGetResponse multiGetResponses = client.prepareMultiGet()
                .add("car_shop", "cars", "1")
                .add("car_shop", "cars", "2")
                .get();

        for(MultiGetItemResponse multiGetItemResponses : multiGetResponses) {
            GetResponse getresponse = multiGetItemResponses.getResponse();
            if(getresponse.isExists()){
                System.out.println(getresponse.getSourceAsString());
            }

        }

        client.close();
    }


}
