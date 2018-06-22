package com.jason.es.test;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author : jasonzii @Author
 * @Description : 员工聚合分析应用程序
 * 1.首先按照country国家来进行分组
 * 2.然后在每个country分组内，再按照入职年限进行分组
 * 3.最后计算每个分组内的平均薪资
 * @CreateDate : 18.6.4  22:25
 */
public class EmployeeAggrApp {

    public static void main(String[] args) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.25.128"),9300));

        SearchResponse searchResponse = client.prepareSearch("company")
                .addAggregation(AggregationBuilders.terms("group_by_country").field("country")
                    .subAggregation(AggregationBuilders
                        .dateHistogram("group_by_join_date")
                        .field("join_date")
                        .dateHistogramInterval(DateHistogramInterval.YEAR)
                        .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"))))
                .execute().actionGet();

        Map<String, Aggregation> apprMap = searchResponse.getAggregations().asMap();

        StringTerms groupByCountry = (StringTerms) apprMap.get("group_by_country");

        Iterator<StringTerms.Bucket> iterator = groupByCountry.getBuckets().iterator();

        while(iterator.hasNext()){
            StringTerms.Bucket next = iterator.next();
            System.out.println(next.getKey()+":"+next.getDocCount());

            Histogram groupByJoinDate = (Histogram) next.getAggregations().asMap().get("group_by_join_date");
            Iterator<? extends Histogram.Bucket> iterator1 = groupByJoinDate.getBuckets().iterator();

            while (iterator1.hasNext()){
                Histogram.Bucket next1 = iterator1.next();

                System.out.println(next1.getKey()+":"+next1.getDocCount());

//                没有完成，之后继续
            }


        }




        client.close();

    }






}
