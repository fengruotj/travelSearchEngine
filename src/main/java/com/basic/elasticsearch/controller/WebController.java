package com.basic.elasticsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dell-pc on 2016/4/22.
 */

@Controller
public class WebController extends BaseController{

    @RequestMapping(value = "/temp")
    public String temp(){
        return "temp";
    }

    @RequestMapping(value = "/uploadftl")
    public String upload(){
        log.info("abcdefg");
        return "upload";
    }

    /**
     * 页面转发控制器 两个参数
     * @param var1
     * @param var2
     * @return
     */
    @RequestMapping("/travel_{var1}_{var2}")
    public String sendFunc(@PathVariable("var1") String var1, @PathVariable("var2") String var2){
        return mainPath+var1+"/"+var2;
    }

}
