package cn.oriki.elasticsearch;

import cn.oriki.elasticsearch.domain.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Iterator;

public class ElasticSearchTest {


    /**
     * 创建索引
     *
     * @throws Exception
     */
    @Test
    public void testCreateIndex() throws Exception {
        /**
         * 创建搜索服务器对象
         * 默认端口号9300
         */
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));

        //创建索引，参数为索引的名字
        client.admin().indices().prepareCreate("index_blog").get();

        client.close();
    }

    /**
     * 删除索引
     *
     * @throws Exception
     */
    @Test
    public void testDeleteIndex() throws Exception {
        /**
         * 创建搜索服务器对象
         * 默认端口号9300
         */
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));


        //删除索引
        client.admin().indices().prepareDelete("index_blog").get();

        //关闭连接
        client.close();

    }

    /**
     * 建立映射
     *
     * @throws Exception
     */
    @Test
    public void testCreateMapping() throws Exception {
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));

        //XContentBuilder工具类，用来组装json对象
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject().
                        startObject("properties").
                        startObject("id")
                .field("type", "integer").field("store", "yes")
                .endObject()
                .startObject("title")
                .field("type", "string").field("store", "yes")
                .endObject()
                .startObject("content")
                .field("type", "string").field("store", "yes")
                .endObject()
                .endObject()
                .endObject();

        //创建映射关系
        PutMappingRequest mappingRequest = Requests.putMappingRequest("index_blog")
                .type("article")
                .source(builder);
        //关闭连接
        client.close();
    }


    /**
     * 文档的创建，修改和删除
     *
     * @throws Exception
     */
    @Test
    public void testOperDocument() throws Exception {
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));

        Article article = new Article();
        article.setId(1001);
        article.setTitle("我是好人");
        article.setContent("我是一个善良的好人");

        ObjectMapper objectMapper = new ObjectMapper();

        //建立文档
        //参数1：索引表的名字
        //参数2：文档类型（通过映射分词条）
        //参数3：文档的主键，如果不指定，则默认生成随机索引主键，如果指定，则索引主键和业务主键一样。
        client.prepareIndex("index_blog", "article", article.getId().toString())
                .setSource(objectMapper.writeValueAsString(article))
                .get();
        //更新文档:底层先删除，再添加
//		client.prepareUpdate("idx_blog1", "article",article.getId().toString())
//		.setDoc(objectMapper.writeValueAsString(article))
//		.get();
        //删除文档
        client.prepareDelete("index_blog", "article", article.getId().toString())
                .get();

        //关闭连接
        client.close();
    }


    //批量插入
    @Test
    public void testAddDocumentBatch() throws Exception {
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));

        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 1; i <= 100; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("我是好人" + i);
            article.setContent("我是一个善良的好人" + i);
            //建立文档
            client.prepareIndex("index_blog", "article", article.getId().toString())
                    .setSource(objectMapper.writeValueAsString(article))
                    .get();
        }

        //关闭连接
        client.close();
    }

    //普通查询
    @Test
    public void testBaseQuery() throws Exception {
        //创建连接搜索服务器对象
        //默认的服务的端口是9300
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));

        //++++++++++++++++++++++++++++++++
        //搜索数据
        //获取搜索结果的响应对象
        //get()===（等价）execute().actionGet()
        SearchResponse searchResponse = client.prepareSearch("index_blog")//从哪个索引中检索数据
                .setTypes("article")//检索数据类别，如果不写，则是所有类别
                //设置查询策略！（比较复杂）
                .setQuery(QueryBuilders.matchAllQuery())//所有的数据，没条件
                .get();
        //++++++++++++++++++++++++++++++++

        //通过结果响应对象，来获取我们需要的信息
        //获取命中的数据信息
        SearchHits hits = searchResponse.getHits();
        //1）获取命中次数，查询有多少结果对象
        System.out.println("++++++++++查询的结果的总条数：" + hits.getTotalHits() + ",最高分：" + hits.getMaxScore());
        //2）获取命中的数据元素的集合
        Iterator<SearchHit> searchHitIterator = hits.iterator();
        while (searchHitIterator.hasNext()) {
            //依次检索每个命中对象
            SearchHit searchHit = searchHitIterator.next();
            System.out.println("分数：" + searchHit.getScore());

            //获取命中的对象关联的源文档的字符串格式内容:json字符串
            System.out.println("文档对象：" + searchHit.getSourceAsString());
            //获取命中对象的关联的源文档的某个属性的值
            System.out.println("文档的某个字段，如title：" + searchHit.getSource().get("title"));

        }

        //关闭连接
        client.close();
    }


    //分页查询
    @Test
    public void testPageQuery() throws Exception {
        //创建连接搜索服务器对象
        //默认的服务的端口是9300
        Client client = TransportClient
                .builder()
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("172.16.191.201"), 9300));
        //++++++++++++++++++++++++++++++++
        //搜索数据
        //获取搜索结果的响应对象
        //get()===（等价）execute().actionGet()
        SearchResponse searchResponse = client.prepareSearch("index_blog")//从哪个索引中检索数据
                .setTypes("article")//检索数据类别，如果不写，则是所有类别
                //设置查询策略！（比较复杂）
                .setQuery(QueryBuilders.matchAllQuery())//所有的数据，没条件
                //每页10条，第二页
                .setFrom(10)//起始的行号，默认是0
                .setSize(20)//最大记录数，默认是10
                .get();
        //++++++++++++++++++++++++++++++++

        //通过结果响应对象，来获取我们需要的信息
        //获取命中的数据信息
        SearchHits hits = searchResponse.getHits();
        //1）获取命中次数，查询有多少结果对象
        System.out.println("++++++++++查询的结果的总条数：" + hits.getTotalHits() + ",最高分：" + hits.getMaxScore());
        //2）获取命中的数据元素的集合
        Iterator<SearchHit> searchHitIterator = hits.iterator();
        while (searchHitIterator.hasNext()) {
            //依次检索每个命中对象
            SearchHit searchHit = searchHitIterator.next();
            System.out.println("分数：" + searchHit.getScore());

            //获取命中的对象关联的源文档的字符串格式内容:json字符串
            System.out.println("文档对象：" + searchHit.getSourceAsString());
            //获取命中对象的关联的源文档的某个属性的值
            System.out.println("文档的某个字段，如title：" + searchHit.getSource().get("title"));

        }

        //关闭连接
        client.close();
        System.out.println("ok.............");
    }

}
