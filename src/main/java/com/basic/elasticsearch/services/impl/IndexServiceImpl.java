package com.basic.elasticsearch.services.impl;

import com.basic.elasticsearch.model.TourDoc;
import com.basic.elasticsearch.services.IndexService;
import com.basic.elasticsearch.utils.ElasticSearchUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * locate com.basic.elasticsearch.services
 * Created by mastertj on 2018/3/24.
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    private String TABLE_NAME="tourDetailsTable";
    private String FAMILYNAME="info";
    private String ID="id";
    private String TITLE="title";
    private String DESCRIBE="describe"; //简单描述
    private String CONTENT="content";
    private String SUPPLIER="supplier"; //供应商
    private String PRICE="price"; //价格
    private String TOURLINE="tourLine";//游玩路线
    private String TOURDAYS="tourDays";//行程天数
    private String STARTCITY="startCity";//出发城市
    private String STARTMONTH="startMonth";//出发日期
    private String COSTINCLUDE="costInclude"; //费用包含
    private String VISAREQUIREMENTS="visaRequirements";//签证须知
    private String PUBLISHDATE="publishDate";//发布时间
    @Override
    public void initIndexAndData(String filePath) {
        List<TourDoc> arrayList = new ArrayList<TourDoc>();
        File file = new File(filePath);
        List<String> list = null;
        try {
            list = FileUtils.readLines(file,"UTF8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String line : list){
            TourDoc Doc = new TourDoc();
			String[] split = line.split("\t");
            int id = Integer.parseInt(split[0].trim());
            String title= String.valueOf(split[1].trim());
            String describe= String.valueOf(split[2].trim());
            String content= String.valueOf(split[3].trim());
            String supplier= String.valueOf(split[4].trim());
            Integer price= Integer.valueOf(split[5].trim());
            String tourLine= String.valueOf(split[6].trim());
            Integer tourDays= Integer.valueOf(split[7].trim());
            String startCity= String.valueOf(split[8].trim());
            String startMonth= String.valueOf(split[9].trim());
            String cost_include= String.valueOf(split[10].trim());
            String visaRequirements= String.valueOf(split[11].trim());
            Doc.setId(id);
            Doc.setTitle(title);
            Doc.setDescribe(describe);
            Doc.setContent(content);
            Doc.setPrice(price);
            Doc.setSupplier(supplier);
            Doc.setTourDays(tourDays);
            Doc.setTourLine(tourLine);
            Doc.setStartCity(startCity);
            Doc.setStartMonth(startMonth);
            Doc.setCostInclude(cost_include);
            Doc.setVisaRequirements(visaRequirements);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Doc.setPublishDate(sdf.format(new Date()));
            arrayList.add(Doc);
        }

        for (TourDoc doc : arrayList) {
            try {
                //把数据插入hbase
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, TITLE, doc.getTitle());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, DESCRIBE, doc.getDescribe());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, CONTENT, doc.getContent());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, SUPPLIER, doc.getSupplier());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, PRICE, String.valueOf(doc.getPrice()));
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, TOURLINE, String.valueOf(doc.getTourDays()));
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, TOURDAYS, String.valueOf(doc.getTourDays()));
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, STARTCITY, doc.getStartCity());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, STARTMONTH, doc.getStartMonth());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, COSTINCLUDE, doc.getCostInclude());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, VISAREQUIREMENTS, doc.getVisaRequirements());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, PUBLISHDATE, doc.getPublishDate());
//                HBaseUtils.addRecord(TABLE_NAME, doc.getId()+"", FAMILYNAME, TITLE, doc.getTitle());
                //把数据插入es
                ElasticSearchUtils.addIndex("tourindex","tour",doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
