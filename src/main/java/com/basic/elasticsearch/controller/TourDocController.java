package com.basic.elasticsearch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * locate com.basic.elasticsearch.controller
 * Created by mastertj on 2018/3/25.
 */
@Controller
public class TourDocController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(TourDocController.class);

	/**
	 * 根据ID获取文档具体细节
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detailDocById/{id}")
	public ModelAndView detailDocById(@PathVariable("id") Integer id){
		ModelAndView model=new ModelAndView();
		Map<String, String> tourDocById = hBaseService.getTourDocById(String.valueOf(id));
		model.addObject("data",tourDocById);
		model.setViewName("detail");
		log.info(tourDocById.toString());
		return model;
	}
}
