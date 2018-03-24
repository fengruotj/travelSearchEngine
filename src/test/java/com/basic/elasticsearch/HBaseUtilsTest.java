package com.basic.elasticsearch;

import com.basic.elasticsearch.utils.HBaseUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * locate com.basic.hbase.util
 * Created by 79875 on 2017/5/24.
 * HBaseUtilsTest HBaseUtils测试类
 */
public class HBaseUtilsTest {
	private static final Logger log = LoggerFactory.getLogger(HBaseUtilsTest.class);

    private Logger logger= LoggerFactory.getLogger(HBaseUtilsTest.class);

    @Test
    public void creatTable() throws Exception {
        List<Result> psn = HBaseUtils.getAllRecord("psn");
        for(Result res :psn){
            logger.info(res.toString());
        }
    }

    @Test
    public void listTables() throws IOException {
        HBaseUtils.listTables();
    }

    @Test
    public void test(){
        String a="a b";
        String[] split = a.split("\t");
    }

    @Test
    public void createTable(){
        HBaseUtils.creatTable("tourDetailsTable","info");
    }

    @Test
    public void get() throws IOException {
		List<Result> tourDetailsTable = HBaseUtils.getAllRecord("tourDetailsTable");
		for(Result r : tourDetailsTable){
			log.info("该表RowKey为：" + new String(r.getRow()));
			for(Cell cell : r.rawCells()){
				log.info("列簇为：" + new String(CellUtil.cloneFamily(cell)));
				log.info("列修饰符为："+new String(CellUtil.cloneQualifier(cell)));
				log.info("值为：" + new String(CellUtil.cloneValue(cell)));
			}
		}
	}
}
