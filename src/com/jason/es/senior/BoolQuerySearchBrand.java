package com.jason.es.senior;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author : jasonzii @Author
 *                 boolQuery   多条件查询
 * @Description : 多种条件的组合查询
 * @CreateDate : 18.6.9  17:16
 */
public class BoolQuerySearchBrand {

    public static void main(String[] args) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"), 9300));

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                //必须包含
                .must(QueryBuilders.matchQuery("brand","宝马"))
                //不能包含
                .mustNot(QueryBuilders.termQuery("name.raw","宝马318"))
                //范围查询
                .should(QueryBuilders.rangeQuery("produce_date").gte("2017-01-01").lte("2017-01-31"))
                //范围查询
                .filter(QueryBuilders.rangeQuery("price").gte(280000).lte(350000));

        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(queryBuilder)
                .get();

        for (SearchHit searchHit : searchResponse.getHits().getHits()){
            System.out.println(searchHit.getSourceAsString());
        }

        client.close();
    }

}
