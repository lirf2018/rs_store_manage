package com.yufan.other.help;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbRegion;

/**
 * @功能名称 保存地区数据
 * http://cnis.7east.com/
 * http://www.zxinc.org/gb2260-latest.htm
 * @作者 lirongfan
 * @时间 2016年1月29日 下午4:59:19
 */
public class SaveExcel {

    public static void main(String[] args) {
        System.out.println("---->处理开始");
        saveTxt1();
        System.out.println("---->处理结束");
    }

    /**
     * 解释excel并保存
     * //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县
     */
    public static void saveTxt() {

        File inFile = new File("C:\\Users\\usersLi\\Desktop\\ali云\\a.txt");
        try {
            int indexOrder = 5000;
            Scanner scanner = new Scanner(inFile);//UTF-8
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                if (StringUtils.isNotEmpty(line)) {
                    String strs[] = line.split("#");
                    String code = strs[0].trim();
                    String name = strs[1].trim();

                    String code1 = code.substring(0, 2);//省/自治区/直辖市/特别行政区
                    String code2 = code.substring(2, 4);//市/省(自治区)直辖县/省直辖区/自治州
                    String code3 = code.substring(4, 6);//市辖区/县/自治县

                    TbRegion region = new TbRegion();
                    region.setRegionCode(code);
                    region.setRegionName(name);
                    region.setRegionOrder(indexOrder--);
                    region.setCreatetime(new Timestamp(new Date().getTime()));
                    region.setStatus(1);
                    region.setRegionType(0);
                    //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
                    if ("0000".equals(code.substring(2, code.length()))) {
                        //省/自治区/直辖市/特别行政区
                        region.setRegionLevel(1);
                        region.setParentId("86");
                        //区域分类(0:其它1:省2:自治区3:直辖市4:特别行政区
                        if (name.indexOf("省") > -1) {
                            region.setRegionType(1);
                        } else if (name.indexOf("自治区") > -1) {
                            region.setRegionType(2);
                        } else if (name.indexOf("市") > -1) {
                            region.setRegionType(3);
                        } else if (name.indexOf("行政区") > -1) {
                            region.setRegionType(4);
                        } else {
                            region.setRegionType(0);
                        }
                    } else if ("00".equals(code3)) {
                        //市/省(自治区)直辖县/省直辖区/自治州
                        region.setRegionLevel(2);
                        region.setParentId(code1 + "0000");
                    } else {
                        //市辖区/县/自治县
                        region.setRegionLevel(3);
                        region.setParentId(code1 + code2 + "00");
                    }
                    saveDataDB(region);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 解释excel并保存
     * //层级4:乡/镇/街道5:村
     */
    public static void saveTxt1() {

        File inFile = new File("C:\\Users\\usersLi\\Desktop\\ali云\\补充.txt");
        try {
            int indexOrder = 5000;
            Scanner scanner = new Scanner(inFile);//UTF-8
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                if (StringUtils.isNotEmpty(line)) {
                    String strs[] = line.split("#");
                    String code = strs[0].trim();
                    String name = strs[1].trim();
                    String code1 = code.substring(0, 6);//市辖区/县/自治县
                    TbRegion region = new TbRegion();
                    region.setRegionCode(code);
                    name = name.replace("办事处", "");
                    region.setRegionName(name);
                    region.setRegionOrder(0);
                    region.setCreatetime(new Timestamp(new Date().getTime()));
                    region.setStatus(1);
                    region.setRegionType(0);
                    region.setRegionLevel(4);
                    region.setParentId(code1 + "000000");
                    //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
                    saveDataDB(region);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void saveTbRegion(String key, String name, int level, String parentId) {
        TbRegion region = new TbRegion();
        region.setRegionCode(key);
        region.setRegionName(name);
        region.setParentId("86");
        region.setRegionLevel(level);
        region.setRegionOrder(0);
        //区域分类(0:其它1:省2:自治区3:直辖市4:特别行政区
        if (name.indexOf("省") > -1) {
            region.setRegionType(1);
        } else if (name.indexOf("自治区") > -1) {
            region.setRegionType(2);
        } else if (name.indexOf("市") > -1) {
            region.setRegionType(3);
        } else if (name.indexOf("行政区") > -1) {
            region.setRegionType(4);
        } else {
            region.setRegionType(0);
        }
        region.setCreatetime(new Timestamp(new Date().getTime()));
        region.setStatus(1);
//            saveDataDB(region);
    }

    //第一步 分离省 1:省2:自治区3:直辖市4:特别行政区
    private static void provice(String key, String name) {
        if ("0000".equals(key.substring(2, key.length()))) {
            TbRegion region = new TbRegion();
            region.setRegionCode(key);
            region.setRegionName(name);
            region.setParentId("86");
            region.setRegionLevel(1);
            region.setRegionOrder(0);
            //区域分类(0:其它1:省2:自治区3:直辖市4:特别行政区
            if (name.indexOf("省") > -1) {
                region.setRegionType(1);
            } else if (name.indexOf("自治区") > -1) {
                region.setRegionType(2);
            } else if (name.indexOf("市") > -1) {
                region.setRegionType(3);
            } else if (name.indexOf("行政区") > -1) {
                region.setRegionType(4);
            } else {
                region.setRegionType(0);
            }
            region.setCreatetime(new Timestamp(new Date().getTime()));
            region.setStatus(1);
//            saveDataDB(region);
            System.out.println(key);
        }
    }

    public static void saveTxt2() {

        File inFile = new File("C:\\Users\\usersLi\\Desktop\\ali云\\b.txt");
        int i = 0;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(inFile);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            Map<String, String> map = new HashMap<String, String>();
            //一行一行读
            while (line != null) { //按行读数据
                line = bufferedReader.readLine();
                //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
                {
                    //分离省
                    if (StringUtils.isNotEmpty(line)) {
                        String strs[] = line.split("#");
                        String key = strs[0].trim();
                        String name = strs[1].trim();
                        if ("0000".equals(key.substring(2, key.length()))) {
                            map.put(key, name);
                            System.out.println(line);
                            i = i + 1;
                            TbRegion region = new TbRegion();
                            region.setRegionCode(key);
                            region.setRegionName(name);
                            region.setParentId("86");
                            region.setRegionLevel(1);
                            region.setRegionOrder(0);
                            //区域分类(0:其它1:省2:自治区3:直辖市4:特别行政区
                            if (name.indexOf("省") > -1) {
                                region.setRegionType(1);
                            } else if (name.indexOf("自治区") > -1) {
                                region.setRegionType(2);
                            } else if (name.indexOf("市") > -1) {
                                region.setRegionType(3);
                            } else if (name.indexOf("行政区") > -1) {
                                region.setRegionType(4);
                            } else {
                                region.setRegionType(0);
                            }
                            region.setCreatetime(new Timestamp(new Date().getTime()));
                            region.setStatus(1);
                            saveDataDB(region);
                        }
                    }
                }

            }
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 解释excel并保存
     */
    public static void saveExcel() {
        try {
            String excelPath = "d://out.xls";

            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));
            HSSFSheet sheet = workbook.getSheetAt(0);
            //得到行数
            int rows = sheet.getLastRowNum();
            for (int i = 1; i < rows; i++) {
                HSSFRow row = sheet.getRow(i);
                String value0 = row.getCell(0).getStringCellValue();//行政区划代码
                String value1 = row.getCell(1).getStringCellValue();//行政区划名称
                String value2 = row.getCell(2).getStringCellValue();//父行政区划
                String value3 = row.getCell(3).getStringCellValue();//层级
                String value4 = row.getCell(4).getStringCellValue();//行政区划英文名称
                String value5 = row.getCell(5).getStringCellValue();//行政区划简称
                String value6 = row.getCell(6).getStringCellValue();//区域分类
                TbRegion region = new TbRegion();
                //行政区划代码
                region.setRegionCode(value0);
                //行政区划名称
                region.setRegionName(value1);
                //行政区划短名称
                region.setRegionShortname("");
                //父行政区划
                region.setParentId(value2);
                //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村',
                region.setRegionLevel(Integer.parseInt(value3));
                //排序，用来调整顺序
                region.setRegionOrder(0);
                //行政区划英文名称
                region.setRegionNameEn(value4);
                //行政区划简称
                region.setRegionShortnameEn(value5);
                //区域分类(0:省/自治区1:直辖市2:特别行政区3:其它)',
//				region.setRegionType(Integer.parseInt(value6));
                //
                region.setStatus(1);
                //
                region.setRemark("");
                //
                region.setCreatetime(new Timestamp(System.currentTimeMillis()));
                //
//				region.setCreateman(1);
                //
//				region.setLastalterman("");
                //
                region.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                System.out.println("-------->" + JSONObject.toJSONString(region));
                saveDataDB(region);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void saveDataDB(Object obj) {

        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/resource/hibernate.cfg.xml";
        path = path.substring(1, path.length());
        Configuration conf = new Configuration().configure(new File(path));
        System.out.println();
        SessionFactory sf = conf.buildSessionFactory();
        Session sess = sf.openSession();
        Transaction tr = sess.beginTransaction();
        sess.save(obj);
        tr.commit();
        sess.close();
        sf.close();
    }


}
