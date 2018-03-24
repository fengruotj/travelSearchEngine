package com.basic.elasticsearch.utils;

import com.basic.elasticsearch.model.TourDoc;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
		if(client==null)
			getClient();

		JsonObject jsonObject=new JsonObject();
		jsonObject.addProperty("id",doc.getId());
		jsonObject.addProperty("title",doc.getTitle());
		jsonObject.addProperty("supplier",doc.getSupplier());
		jsonObject.addProperty("price",doc.getPrice());
		jsonObject.addProperty("describe",doc.getDescribe());
		jsonObject.addProperty("tourLine",doc.getTourLine());
		jsonObject.addProperty("tourDays",doc.getTourDays());
		jsonObject.addProperty("startCity",doc.getStartCity());
		jsonObject.addProperty("startMonth",doc.getStartMonth());
		jsonObject.addProperty("publishDate",doc.getPublishDate());

		IndexResponse indexResponse = client.prepareIndex(index, type)
				.setSource(jsonObject.toString(), XContentType.JSON).get();
		LOG.info("索引名称:"+indexResponse.getIndex());
		LOG.info("索引类型:"+indexResponse.getType());
		LOG.info("索引Id:"+indexResponse.getId());
		LOG.info("当前实例状态："+indexResponse.status());
		return indexResponse.getId();
	}

//	public static void main(String[] args) {
//		Map<String, Object> search = Esutil.search("hbase", "bjsxt", "doc", 0, 10);
//		List<Map<String, Object>> list = (List<Map<String, Object>>) search.get("dataList");
//	}
}
