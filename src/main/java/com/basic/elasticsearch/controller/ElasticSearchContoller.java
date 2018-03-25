package com.basic.elasticsearch.controller;

import com.basic.elasticsearch.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * locate com.basic.elasticsearch.controller
 * Created by mastertj on 2018/3/25.
 */
@Controller
public class ElasticSearchContoller extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ElasticSearchContoller.class);

	@RequestMapping("/search")
	public String serachArticle(Model model,
								@RequestParam(value="keyWords",required = false) String keyWords,
								@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
								@RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize){
		if(keyWords==null||keyWords.equals("")){
			return "search";
		}

		Map<String,Object> map = new HashMap<String, Object>();
		int count = 0;
		try {
			map = elasticSearchService.search(keyWords,"tourindex","tour",(pageNum-1)*pageSize, pageSize);
			count = Integer.parseInt(((Long) map.get("count")).toString());
		} catch (Exception e) {
			log.error("查询索引错误!{}",e);
			e.printStackTrace();
		}

		PageUtil<Map<String, Object>> page = new PageUtil<Map<String, Object>>(String.valueOf(pageNum),String.valueOf(pageSize),count);
		List<Map<String, Object>> articleList = (List<Map<String, Object>>)map.get("dataList");
		page.setList(articleList);
		log.info(page.getList().toString());
		model.addAttribute("total",count);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("data",page.getList());
		model.addAttribute("kw",keyWords);
		return "search";
	}

}
