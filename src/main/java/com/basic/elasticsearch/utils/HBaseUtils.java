package com.basic.elasticsearch.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchujie on 16/3/22.
 * 用列值过滤数据的时候,扫描必须将要过滤的列名字加入进去,或者直接不加Column这样默认是显示所有数据
 */
public class HBaseUtils {
    private static final Logger log = LoggerFactory.getLogger(HBaseUtils.class);
    private static Configuration conf = null;
    private static Connection connection;
    protected static Admin admin;
    static {
        try {
            org.apache.commons.configuration.Configuration config = new PropertiesConfiguration("hbase.properties");
            String hbase_zookeeper_client_port = config.getString("hbase.zk.port");
            String hbase_zookeeper_quorum = config.getString("hbase.zk.host");
            String hbase_master = config.getString("hbase.master");
            log.debug("HBase config debug zookeeper client port: "+hbase_zookeeper_client_port);
            log.debug("HBase config debug zookeeper quorum: "+hbase_zookeeper_quorum);
            log.debug("HBase config debug hbase master: "+hbase_master);
            conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.property.clientPort", hbase_zookeeper_client_port);
            conf.set("hbase.zookeeper.quorum", hbase_zookeeper_quorum);
            conf.set("hbase.master", hbase_master);
            connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            log.error("init HBase Configuration Exception: "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("init HBase Configuration Exception: " + e.getMessage());
        }
    }

    /**
     * 创建 HBase 表
     * @param tableName
     * @param familys
     * @throws IOException
     */
    public static void creatTable(String tableName,String... familys) {
        try {
            if(admin.tableExists(TableName.valueOf(tableName))) {
                log.debug("table already exists");
            } else {
                HTableDescriptor hTableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
                for (String family : familys) {
                    hTableDescriptor.addFamily(new HColumnDescriptor(family));
                }
                admin.createTable(hTableDescriptor);
                log.debug("create table:" + tableName + "  success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ============================================================================================
     * pre-Creating Regions 创建Hbase Table的时候做预分区处理
     */
    public static boolean createTable(String tableName,HTableDescriptor hTableDescriptor,byte[][]splits) throws Exception {
        if(admin.tableExists(TableName.valueOf(tableName))) {
            log.debug("table already exists");
            return false;
        } else {
            admin.createTable(hTableDescriptor,splits);
            log.debug("create table:" + tableName + "  success");
        }
        return true;
    }

    /**
     * 得到Regions 分区 只能针对那种数字型的RowKey
     * @param startKeys
     * @param endKey
     * @param numRegions
     * @return
     */
    public static byte[][] getHexSplits(String startKeys,String endKey,int numRegions){
        //start:001 end:1000 numReginon10
        byte[][]splits=new byte[numRegions-1][];
        BigInteger lowestKey=new BigInteger(startKeys,16);
        BigInteger highestKey=new BigInteger(endKey,16);
        BigInteger range=highestKey.subtract(lowestKey);
        BigInteger regionIncrement=range.divide(BigInteger.valueOf(numRegions));
        lowestKey=lowestKey.add(regionIncrement);
        for(int i=0;i< numRegions-1;i++){
            BigInteger key=lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[]b =String.format("%016x",key).getBytes();
            splits[i]=b;
        }
        return splits;
    }
    /**
     * ============================================================================================
     */

    /**
     * 删除 Hbase 表
     * @param tableName
     * @throws IOException
     */
    public static void deleteTable(String tableName) throws IOException {
        TableName table_name = TableName.valueOf(tableName);
        admin.disableTable(table_name);
        admin.deleteTable(table_name);
        log.debug("delete table:" + tableName + "  success");
    }

    /**
     *  增加列族
     * @param tableName
     * @param FamilyName
     * @throws IOException
     */
    public static void addCoulumFamily(String tableName,String FamilyName) throws IOException {
        TableName tableName1=TableName.valueOf(tableName);
        if(admin.tableExists(tableName1)) {
            admin.disableTable(tableName1);
            HTableDescriptor tableDescriptor=admin.getTableDescriptor(tableName1);
            tableDescriptor.addFamily(new HColumnDescriptor(FamilyName));
            admin.modifyTable(tableName1,tableDescriptor);
            admin.enableTable(tableName1);
        }
    }


    /**
     * 查看已有表
     * @throws IOException
     */
    public static void listTables() throws IOException {
        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for(HTableDescriptor hTableDescriptor :hTableDescriptors){
            System.out.println(hTableDescriptor.getNameAsString());
        }
    }

    /**
     *  删除列族
     * @param tableName
     * @param FamilyName
     * @throws IOException
     */
    public static void deleteCoulumFamily(String tableName,String FamilyName) throws IOException {
        TableName tableName1=TableName.valueOf(tableName);
        if(admin.tableExists(tableName1)) {
            admin.disableTable(tableName1);
            HTableDescriptor tableDescriptor=admin.getTableDescriptor(tableName1);
            tableDescriptor.removeFamily(FamilyName.getBytes());
            admin.modifyTable(tableName1,tableDescriptor);
            admin.enableTable(tableName1);
        }
    }

    /**
     * Hbase 插入数据
     * @param tableName
     * @param rowKey
     * @param family
     * @param qualifier
     * @param value
     * @throws IOException
     */
    public static void addRecord (String tableName, String rowKey, String family,
                                  String qualifier, String value) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        table.put(put);
        log.debug("insert recored " + rowKey + " to table " + tableName + " success.");
        table.close();
    }

    /**
     * Hbase 删除数据
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public static void delRecord (String tableName, String rowKey) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Delete del = new Delete(rowKey.getBytes());
        table.delete(del);
        log.debug("del recored " + rowKey + " success.");
        table.close();
    }

    /**
     * 根据 RowKey 查找数据
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public static void getOneRecord (String tableName, String rowKey) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for(Cell cell : rs.rawCells()){
            System.out.println("列簇为：" + new String(CellUtil.cloneFamily(cell)));
            System.out.println("值为："+new String(CellUtil.cloneValue(cell)));
        }
        table.close();
    }

    /**
     * 查询表中所有数据
     * @param tableName
     * @throws IOException
     */
    public static List<Result> getAllRecord (String tableName) throws IOException {
        List resultList = new ArrayList();
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Scan s = new Scan();
        ResultScanner ss = table.getScanner(s);
        for(Result r : ss){
            log.debug("该表RowKey为：" + new String(r.getRow()));
            for(Cell cell : r.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为："+new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(r);
        }
        table.close();
        return resultList;
    }

    /**
     * 根据rwokey查询
     * @param tableName
     * @param rowKey
     * @return
     * @throws IOException
     */
    public static Result getRecordByRowKey(String tableName, String rowKey)
            throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);
        for(Cell cell : result.rawCells()){
            log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
            log.debug("列修饰符为："+new String(CellUtil.cloneQualifier(cell)));
            log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
        }
        table.close();
        return result;
    }

    /**
     * 获取某一列的所有数据
     * @param tableName
     * @param familyName
     * @param qualifierNameList
     * @return
     * @throws IOException
     */
    public static List<Result> getAllRecordByQualifier(String tableName, String familyName, String[] qualifierNameList)
            throws IOException {
        List resultList = new ArrayList();
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Scan s = new Scan();
        for(String qualifier : qualifierNameList){
            s.addColumn(familyName.getBytes(), qualifier.getBytes());
        }
        ResultScanner ss = table.getScanner(s);
        for (Result r : ss){
            log.debug("该表RowKey为：" + new String(r.getRow()));
            for(Cell cell : r.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(r);
        }
        table.close();
        ss.close();
        return resultList;
    }

    /**
     *  列值过滤器--SingleColumnValueFilter根据列值过滤器查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param value
     * @param fiedlist fiedlist为需要显示的字段 如果为null表示显示所有列
     * @return
     * @throws IOException
     */
    public static List<Result> getColumnResultbyFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String value,String[]fiedlist) throws IOException {
        List resultList = new ArrayList();
        HTable table =(HTable) connection.getTable(TableName.valueOf(tableName));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                familyName.getBytes(),
                qualifer.getBytes(),
                compareOp,
                new BinaryComparator(value.getBytes())
        );
        filterList.addFilter(filter);
        Scan s = new Scan();

        if(fiedlist!=null){
            for(String field:fiedlist)
            s.addColumn(familyName.getBytes(),field.getBytes());
        }
        s.setFilter(filterList);

        ResultScanner ss = table.getScanner(s);
        for (Result r : ss){
            log.debug("该表RowKey为：" + new String(r.getRow()));
            for(Cell cell : r.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(r);
        }
        table.close();
        ss.close();
        return resultList;
    }

    /**
     *  列值过滤器--SingleColumnValueFilter根据列值过滤器正则表达式查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param regx
     * @param fiedlist  fiedlist为需要显示的字段 如果为null表示显示所有列
     * @return
     * @throws IOException
     */
    public static List<Result> getColumnResultbyRegxFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String regx,String[]fiedlist) throws IOException {
        RegexStringComparator regexStringComparator=new RegexStringComparator(regx);
        return getColumnResultByComparable(tableName,familyName,qualifer,compareOp,regexStringComparator,fiedlist);
    }

    /**
     *  列值过滤器--SingleColumnValueFilter根据列值过滤器匹配完整字节数组查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param binary
     * @param fiedlist
     * @return
     * @throws IOException
     */
    public static List<Result> getColumnResultbyBinaryComparatorFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String binary,String[]fiedlist) throws IOException {
        BinaryComparator binaryComparator=new BinaryComparator(binary.getBytes());
        return getColumnResultByComparable(tableName,familyName,qualifer,compareOp,binaryComparator,fiedlist);
    }

    /**
     *   列值过滤器--SingleColumnValueFilter根据列值过滤器匹配匹配字节数组前缀查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param binaryPriex
     * @param fiedlist
     * @return
     * @throws IOException
     */
    public static List<Result> getColumnResultbyBinaryPrefixComparatorFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String binaryPriex,String[]fiedlist) throws IOException {
        BinaryPrefixComparator prefixComparator=new BinaryPrefixComparator(binaryPriex.getBytes());
        return getColumnResultByComparable(tableName,familyName,qualifer,compareOp,prefixComparator,fiedlist);
    }

    /**
     *  .PageFilter 指定页面行数，返回对应行数的结果集。
     * @param tableName
     * @param startRows
     * @param pageNum   页面行数
     * @return
     * @throws IOException
     */
    public static List<Result> getPageResultByFilter(String tableName,String startRows,long pageNum) throws IOException {
        List resultList = new ArrayList();
        HTable table =(HTable) connection.getTable(TableName.valueOf(tableName));
        PageFilter pageFilter=new PageFilter(pageNum);
        Scan scan=new Scan();
        scan.setFilter(pageFilter);
        scan.setStartRow(startRows.getBytes());

        ResultScanner ss=table.getScanner(scan);
        for(Result result:ss){
            log.debug("该表RowKey为：" + new String(result.getRow()));
            for(Cell cell:result.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(result);
        }
        table.close();
        ss.close();
        return resultList;
    }

    /**
     *  列值过滤器
     *  得到数据从比较器中得到数据Result
     *  （2）比较器  ByteArrayComparable
     *      通过比较器可以实现多样化目标匹配效果，比较器有以下子类可以使用：
     *      BinaryComparator               匹配完整字节数组
     *      BinaryPrefixComparator     匹配字节数组前缀
     *      BitComparator
     *      NullComparator
     *      RegexStringComparator    正则表达式匹配
     *      SubstringComparator        子串匹配
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param comparable 比较器
     * @param fiedlist
     * @return
     * @throws IOException
     */
    public static List<Result> getColumnResultByComparable(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,ByteArrayComparable comparable,String[]fiedlist) throws IOException {
        List resultList = new ArrayList();
        HTable table =(HTable) connection.getTable(TableName.valueOf(tableName));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);

        SingleColumnValueFilter singleColumnValueFilter=new SingleColumnValueFilter(familyName.getBytes(),qualifer.getBytes(),
                compareOp,comparable
        );
        filterList.addFilter(singleColumnValueFilter);
        Scan scan=new Scan();
        if(fiedlist!=null){
            for(String field:fiedlist)
                scan.addColumn(familyName.getBytes(),field.getBytes());
        }
        scan.setFilter(filterList);

        ResultScanner ss=table.getScanner(scan);
        for(Result result:ss){
            log.debug("该表RowKey为：" + new String(result.getRow()));
            for(Cell cell:result.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(result);
        }
        table.close();
        ss.close();
        return resultList;
    }


    /*****************************************下面是为了统计一个Hbase表的个数的API*****************************/

    /**
     *  为一个Hbae表注册个Table注册该Coprocessor，用来统计Hbase中记录的个数
     * @param name
     * @throws IOException
     */
    public static void addCoprocessorToTable(String name) throws IOException {
        String coprocessClassName = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
        TableName tableName=TableName.valueOf(name);
        admin.disableTable(tableName);

        HTableDescriptor htd = admin.getTableDescriptor(tableName);

        htd.addCoprocessor(coprocessClassName);

        admin.modifyTable(tableName, htd);

        admin.enableTable(tableName);
        log.debug("=======================addCoprocessorToTable success!!========================== ");
    }

    /**
     *  得到Table的记录的个数
     * @param tableName
     * @return
     */
    public static long getTableCount(String tableName,Scan scan){
        //s.addColumn(Bytes.toBytes("debug"), Bytes.toBytes("c1"));

        AggregationClient ac = new AggregationClient(conf);

        try {
            long total=0;
            total=ac.rowCount(TableName.valueOf(tableName), new LongColumnInterpreter(), scan);
            log.debug("=====================tableName:个数 "+total);
            return total;

        } catch (Throwable e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 关闭连接
     */
    public static  void close(){
        try {
            if(null != admin)
                admin.close();
            if(null != connection)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
