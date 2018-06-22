package com.jason.es.senior;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.concurrent.BlockingDeque;

/**
 * @Author : jasonzii @Author
 *                  bulk  批量上传
 * @Description : 希望能够在内存中缓存比如1000条销售记录，然后一次性批量上传到es中去
 * @CreateDate : 18.6.9  14:03
 */
public class BulkUploadSalesDateApp {

    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"),9300));

        //生成bulk,相当于一个集合，把增，删，改，加入进去。然后get()，直接批量执行
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("car_shop", "sales", "3")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                            .field("brand", "奔驰")
                            .field("name", "奔驰C200")
                            .field("price", 350000)
                            .field("produce_date", "2017-01-20")
                            .field("sale_price", 320000)
                            .field("sale_date", "2017-01-25")
                        .endObject());

        bulkRequestBuilder.add(indexRequestBuilder);

        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate("car_shop", "sales", "1")
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("sale_price", 290000)
                        .endObject());

        bulkRequestBuilder.add(updateRequestBuilder);

        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete("car_shop", "sales", "2");

        bulkRequestBuilder.add(deleteRequestBuilder);

        //批量执行
        BulkResponse bulkResponses = bulkRequestBuilder.get();

        for(BulkItemResponse bulkItemResponse : bulkResponses.getItems()){
            System.out.println(bulkItemResponse.getVersion());
        }

        client.close();

    }


}
