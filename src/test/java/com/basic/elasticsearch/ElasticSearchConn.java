package com.basic.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * locate com.basic.elasticSearch
 * Created by mastertj on 2018/3/21.
 * java连接elasticSearch集群
 */
public class ElasticSearchConn {
    private static String host="ubuntu2"; // 服务器地址
    private static int port=9300; // 端口
    private TransportClient client=null;

    public static final String CLUSTER_NAME="bigdata";

    public static Settings.Builder settings=Settings.builder()
            .put("cluster.name",CLUSTER_NAME)
            .put("client.transport.sniff", true)
            .put("xpack.security.transport.ssl.enabled", false)
            .put("xpack.security.user", "elastic:changeme");
    /**
     * 获取客户端
     * @throws Exception
     */
    @Before
    public void getClinet()throws Exception{
        client = new PreBuiltXPackTransportClient(settings.build())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
        System.out.println(client);
    }

    @Test
    public void test(){

    }

    /**
     * 关闭连接
     */
    @After
    public void close(){
        if(client!=null)
            client.close();
    }

}
