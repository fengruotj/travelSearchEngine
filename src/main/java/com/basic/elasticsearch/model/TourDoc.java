package com.basic.elasticsearch.model;

/**
 * locate com.basic.elasticsearch.model
 * Created by mastertj on 2018/3/24.
 * 旅游搜索文档模型类
 */
public class TourDoc {
    private Integer id;

    private String title;

    private String describe; //简单描述

    private String content;

    private String supplier; //供应商

    private Integer price; //价格

    private String tourLine;//游玩路线

    private Integer tourDays;//行程天数

    private String startCity;//出发城市

    private String startMonth;//出发日期

    private String costInclude; //费用包含

    private String visaRequirements;//签证须知

    private String publishDate;//发布时间

    public TourDoc() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTourLine() {
        return tourLine;
    }

    public void setTourLine(String tourLine) {
        this.tourLine = tourLine;
    }

    public Integer getTourDays() {
        return tourDays;
    }

    public void setTourDays(Integer tourDays) {
        this.tourDays = tourDays;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getCostInclude() {
        return costInclude;
    }

    public void setCostInclude(String costInclude) {
        this.costInclude = costInclude;
    }

    public String getVisaRequirements() {
        return visaRequirements;
    }

    public void setVisaRequirements(String visaRequirements) {
        this.visaRequirements = visaRequirements;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
