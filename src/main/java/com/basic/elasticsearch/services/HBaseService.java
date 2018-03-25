package com.basic.elasticsearch.services;

import java.util.Map;

/**
 * locate com.basic.elasticsearch.services
 * Created by mastertj on 2018/3/25.
 */
public interface HBaseService {
	public Map<String,String> getTourDocById(String rowkey);
}
