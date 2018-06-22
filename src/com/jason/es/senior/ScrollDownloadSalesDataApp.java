package com.jason.es.senior;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.net.InetAddress;

/**
 * @Author : jasonzii @Author
 *                 scroll  批量下载
 * @Description : 现在要下载大批量的数据，从es,放到excel中,比如几十万条数据
 *    2条数据，做一个演示，每个批次下载一条销售记录，分2个批次给它下载完
 * @CreateDate : 18.6.9  15:14
 */
public class ScrollDownloadSalesDataApp {

    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"), 9300));

        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("sales")
                .setQuery(QueryBuilders.termQuery("brand.keyword", "宝马"))
                //时间窗口连接1分钟
                .setScroll(new TimeValue(60000))
                //每一批次，查询1条
                .setSize(1)
                .get();

        //计数器
        int batchCount = 0;

        do{
            for(SearchHit searchHit : searchResponse.getHits().getHits()){
                System.out.println("batch:" + ++batchCount);
                System.out.println(searchHit.getSourceAsString());

                //每次查询一批数据，比如1000行，然后写入本地的一个excel文件中

                //如果说你一下查询几十万条数据，jvm内存可能会爆掉

            }

            //通过searchResponse获得ScrollId，再来获取下一批次数据
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
        }while (searchResponse.getHits().getHits().length != 0);


        client.close();
    }



}
