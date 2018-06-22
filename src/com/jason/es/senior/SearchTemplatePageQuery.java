package com.jason.es.senior;

import io.netty.channel.ChannelHandler;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : jasonzii @Author
 * 首先要在es安装目录下的config/script/文件夹中放入模板 page_query_by_brand.mustache
 * @Description : 建立一个搜索模板，通过调用这个模板，来进行分页查询
 * @CreateDate : 18.6.9  16:11
 */
public class SearchTemplatePageQuery {

    @SuppressWarnings({"resource","unchecked"})
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"), 9300));

        Map<String, Object> scriptParams = new HashMap<String, Object>();
        //0表示取第一批
        scriptParams.put("from",0);
        //一批中取几个
        scriptParams.put("size",1);
        scriptParams.put("brand","宝马");

        SearchResponse searchResponse = new SearchTemplateRequestBuilder(client)
                //模板名
                .setScript("page_query_by_brand")
                //类型
                .setScriptType(ScriptType.FILE)
                .setScriptParams(scriptParams)
                .setRequest(new SearchRequest("car_shop").types("sales"))
                .get()
                .getResponse();

        for(SearchHit searchHit : searchResponse.getHits().getHits()){
            System.out.println(searchHit.getSourceAsString());
        }


        client.close();

    }

}
