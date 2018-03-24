package com.basic.elasticsearch.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dell-pc on 2016/4/21.
 */
@Controller
public class BaseController {

    //后台项目基础url
    protected String mainPath="/";

    @ModelAttribute("BasePath")
    public String getBasePath(HttpServletRequest httpServletRequest){
        log.info(httpServletRequest.getServletContext().getContextPath());
        return httpServletRequest.getServletContext().getContextPath();
    }

    protected Gson gson = new Gson();

    protected static final Logger log = LoggerFactory.getLogger(BaseController.class);

}
