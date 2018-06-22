package com.jason.es.test;

import com.jason.es.util.ESClient;
import com.jason.es.util.PropertiesUtil;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.action.search.RestSuggestAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.omg.CORBA.portable.UnknownException;

import javax.management.Query;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author : jasonzii @Author
 * @Description :
 * @CreateDate : 18.5.31  16:11
 */

public class CreateIndex {

    //ES查询接口   prepareGet()
    public static void get() {

        ESClient.init();

        TransportClient client = ESClient.getClient();

        GetResponse result = client.prepareGet("book", "novel", "1")
                .get();

        String sourceAsString = result.getSourceAsString();

        System.out.println(sourceAsString);

    }

    //ES增加接口   prepareGet()
    public static void add() {

        ESClient.init();

        TransportClient client = ESClient.getClient();

        try {
            //构造数据，三种方式，XContentFactory ,json ,Map
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", "标题")
                    .field("author", "作者")
                    .field("word_count", "1000")
                    .field("publish_date", "2018-06-03 00:00:00")
                    .endObject();

            //不设置id，则自动生成
            IndexResponse result = client.prepareIndex("book", "novel")
                    .setSource(content)
                    .get();

            //看一下自动生成的id
            System.out.println(result.getId());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    //删除数据
    public static void delete(){

        ESClient.init();

        TransportClient client = ESClient.getClient();

        DeleteResponse result = client.prepareDelete("book", "novel", "1")
                .get();

        System.out.println(result.getResult().toString());

    }

    //修改数据
    private static void update(){

        ESClient.init();

        TransportClient client = ESClient.getClient();

        //指定修改的文档
        UpdateRequest updateRequest = new UpdateRequest("book","novel","2");

        XContentBuilder builder = null;

        try {

            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", "JAVA进阶")
                    .field("author", "jason")
                    .endObject();

            //把新数据加入到文档
            updateRequest.doc(builder);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            //更改数据
            UpdateResponse result = client.update(updateRequest).get();

            System.out.println(result.getResult().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //复合查询
    public static void query(){

        ESClient.init();

        TransportClient client = ESClient.getClient();

        //查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //match查询
        boolQuery.must(QueryBuilders.matchQuery("author","李四"));

        boolQuery.must(QueryBuilders.matchQuery("title","ES"));

        //范围查询
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word_count");

        //从几开始
        rangeQuery.from(0);

        //小于
        rangeQuery.to(4000);

        //过滤范围查询后，再match查询
        boolQuery.filter(rangeQuery);

        SearchRequestBuilder builder = client.prepareSearch("book")
                .setTypes("novel")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)    //当数据足够多时，用QUERY_THEN_FETCH
                .setQuery(boolQuery)
                //从第一个开始
                .setFrom(0)
                //读取10条    相当于mysql 中的limit
                .setSize(10);

        //可以把请求体直接打印出来    Request:表示请求的json数据。当使用get()方法后，获得Response:表示响应的数据
        System.out.println(builder);

        SearchResponse response = builder.get();

        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

        //response.getHits()   获得hits: 所有数据和详情
        for (SearchHit hit : response.getHits()) {

            //hit.getSource()  从hit中只取出数据
            result.add(hit.getSource());

            System.out.println(result);

        }


    }



    public static void main(String[] args) throws Exception {

        //查询数据
//        get();

        //增加数据
//        add();

        //删除数据
//        delete();

        //修改数据
//        update();

        //复合查询
//        query();

    }


}
