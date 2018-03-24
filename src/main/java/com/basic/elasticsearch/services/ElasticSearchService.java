package com.basic.elasticsearch.services;

import java.util.Map;

/**
 * locate com.basic.elasticsearch.services
 * Created by mastertj on 2018/3/24.
 */
public interface ElasticSearchService {
    public Map<String, Object> search(String key, String index, String type, int start, int row);
}
