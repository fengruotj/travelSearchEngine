package com.basic.elasticsearch.services.impl;

import com.basic.elasticsearch.services.HBaseService;
import com.basic.elasticsearch.utils.HBaseUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * locate com.basic.elasticsearch.services
 * Created by mastertj on 2018/3/25.
 */
@Service("hBaseService")
public class HBaseServiceImpl implements HBaseService {

	private String TABLE_NAME="tourDetailsTable";

	@Override
	public Map<String,String> getTourDocById(String rowkey) {
		Result rs=null;
		Map<String,String> stringMap=new HashMap<>();

		try {
			rs = HBaseUtils.getOneRecord(TABLE_NAME,rowkey);
			for(Cell cell : rs.rawCells()){
				stringMap.put(new String(CellUtil.cloneQualifier(cell)),new String(CellUtil.cloneValue(cell)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringMap;
	}
}
