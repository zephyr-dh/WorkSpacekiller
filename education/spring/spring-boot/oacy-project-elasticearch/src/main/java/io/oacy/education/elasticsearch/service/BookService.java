package io.oacy.education.elasticsearch.service;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-21 4:24 PM
 */

@Service
public class BookService {

    @Autowired
    private TransportClient transportClient;

    /**
     * @param id
     * @return
     */
    public GetResponse get(String id) {
        GetResponse response = transportClient.prepareGet("book", "novel", id).get();
        return response;
    }

    /**
     * @param title
     * @param author
     * @param wordCount
     * @param publishDate
     * @return
     */
    public IndexResponse add(String title, String author, String wordCount, String publishDate) {
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", title)
                    .field("author", author)
                    .field("word_count", wordCount)
                    .field("publish_date", publishDate)
                    .endObject();
            IndexResponse response = transportClient.prepareIndex("book", "novel")
                    .setSource(contentBuilder)
                    .get();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param id
     * @return
     */
    public DeleteResponse delete(String id) {
        return transportClient.prepareDelete("book", "novel", id).get();
    }

    /**
     * @param id
     * @param title
     * @param author
     * @param wordCount
     * @param publishDate
     * @return
     */
    public UpdateResponse update(String id, String title, String author, Integer wordCount, String publishDate) {
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();
            if (null != title) contentBuilder.field("title", title);
            if (null != author) contentBuilder.field("author", author);
            if (null != wordCount) contentBuilder.field("word_count", wordCount);
            if (null != publishDate) contentBuilder.field("publish_date", publishDate);
            contentBuilder.endObject();

            UpdateRequest request = transportClient.prepareUpdate("book", "novel", id).request();
            request.doc(contentBuilder);
            return transportClient.update(request).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param title
     * @param author
     * @param gtWordCount
     * @param ltWordCount
     */
    public List<Map<String,Object>> query(String title, String author, Integer gtWordCount, Integer ltWordCount) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (null != author) boolQuery.must(QueryBuilders.matchQuery("author", author));
        if (null != title) boolQuery.must(QueryBuilders.matchQuery("title", title));

//        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word_count");
//        rangeQuery.gt(gtWordCount);
        RangeQueryBuilder rangeQuery=QueryBuilders.rangeQuery("word_count").from(gtWordCount);
        if (null != ltWordCount&&ltWordCount>0) rangeQuery.lt(ltWordCount);

        boolQuery.filter(rangeQuery);

        SearchRequestBuilder searchRequest=transportClient.prepareSearch("book")
                .setTypes("novel")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(10);

        System.out.println(searchRequest);

        SearchResponse response=searchRequest.get();

        List<Map<String,Object>> hits=new ArrayList<>();
        for (SearchHit searchHit:response.getHits()) {
            hits.add(searchHit.getSource());
        }
        return hits;

    }
}
