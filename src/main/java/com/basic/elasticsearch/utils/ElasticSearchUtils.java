package com.basic.elasticsearch.utils;

import com.basic.elasticsearch.model.TourDoc;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticSearchUtils {
	public static Client client = null;
	private static Logger LOG= LoggerFactory.getLogger(ElasticSearchUtils.class);

    private static String CLUSTER_NAME="bigdata";
    private static int port=9300;
    private static String Master="ubuntu2";
    private static String slave1="ubuntu1";
    private static String slave2="ubuntu4";

    private static Gson gson=new Gson();

    /**
		 * 获取客户端
		 * @return
		 */
		public static  Client getClient() {
			if(client!=null){
				return client;
			}
            Settings.Builder settings=Settings.builder()
                    .put("cluster.name",CLUSTER_NAME)
                    .put("client.transport.sniff", true)
                    .put("xpack.security.transport.ssl.enabled", false)
                    .put("xpack.security.user", "elastic:changeme");
			try {
                client = new PreBuiltXPackTransportClient(settings.build())
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Master),port))
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(slave1),port))
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(slave2),port));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return client;
		}

	public static String addIndex(String index,String type,TourDoc doc){

		IndexResponse indexResponse = client.prepareIndex(index, type)
				.setSource(gson.toJson(doc), XContentType.JSON).get();
		LOG.info("索引名称:"+indexResponse.getIndex());
		LOG.info("索引类型:"+indexResponse.getType());
		LOG.info("索引Id:"+indexResponse.getId());
		LOG.info("当前实例状态："+indexResponse.status());
		return indexResponse.getId();
	}


//	public static Map<String, Object> search(String key,String index,String type,int start,int row){
//		SearchRequestBuilder builder = getClient().prepareSearch(index);
//		builder.setTypes(type);
//		builder.setFrom(start);
//		builder.setSize(row);
//		//设置高亮字段名称
//		builder.addHighlightedField("title");
//		builder.addHighlightedField("describe");
//		//设置高亮前缀
//		builder.setHighlighterPreTags("<font color='red' >");
//		//设置高亮后缀
//		builder.setHighlighterPostTags("</font>");
//		builder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
//		if(StringUtils.isNotBlank(key)){
////			builder.setQuery(QueryBuilders.termQuery("title",key));
//			builder.setQuery(QueryBuilders.multiMatchQuery(key, "title","describe"));
//		}
//		builder.setExplain(true);
//		SearchResponse searchResponse = builder.get();
//
//		SearchHits hits = searchResponse.getHits();
//		long total = hits.getTotalHits();
//		Map<String, Object> map = new HashMap<String,Object>();
//		SearchHit[] hits2 = hits.getHits();
//		map.put("count", total);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (SearchHit searchHit : hits2) {
//			Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
//			HighlightField highlightField = highlightFields.get("title");
//			Map<String, Object> source = searchHit.getSource();
//			if(highlightField!=null){
//				Text[] fragments = highlightField.fragments();
//				String name = "";
//				for (Text text : fragments) {
//					name+=text;
//				}
//				source.put("title", name);
//			}
//			HighlightField highlightField2 = highlightFields.get("describe");
//			if(highlightField2!=null){
//				Text[] fragments = highlightField2.fragments();
//				String describe = "";
//				for (Text text : fragments) {
//					describe+=text;
//				}
//				source.put("describe", describe);
//			}
//			list.add(source);
//		}
//		map.put("dataList", list);
//		return map;
//	}

//	public static void main(String[] args) {
//		Map<String, Object> search = Esutil.search("hbase", "bjsxt", "doc", 0, 10);
//		List<Map<String, Object>> list = (List<Map<String, Object>>) search.get("dataList");
//	}
}
