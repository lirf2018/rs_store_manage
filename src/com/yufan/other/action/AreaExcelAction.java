package com.yufan.other.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.mapping.Array;

/**
 * @功能名称 自动处理地区Excel数据 用于处理匹配地区划分关系
 * @作者 lirongfan
 * @时间 2016年1月21日 上午11:32:51
 */
public class AreaExcelAction {
    /**
     * 说明:处理原则:得到数据后 先手动处理数据 不包括大陆之外的数据
     * 1.数据分3类  0:省/自治区1:直辖市2:特别行政区
     * 2.省下面单位分为市/省(自治区)直辖县/省直辖区
     * 3.市下单位分市辖区和县
     * 4.县下单位为乡/镇
     * 2.市辖区和县去掉
     * 官方:
     * （一）全国分为省、自治区、直辖市；
     * （二）省、自治区分为自治州、县、自治县、市；
     * （三）县、自治县分为乡、民族乡、镇。直辖市和较大的市分为区、县。自治州分为县、自治县、市。自治区、自治州、自治县都是民族自治地方。
     * 23个省(包括台湾省)、5个自治区、4个直辖市、2个特别行政区
     */
    public static void main(String[] args) {
        AreaExcelAction.handleExcelData();
//		String str="县";
//		System.out.println(str.length());
    }

    public static void handleExcelData() {
        //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
        //区域分类(0:省/自治区1:直辖市2:特别行政区3:其它)
        try {
            String excelPath = "d://行政数据.xls";
            InputStream is = new FileInputStream(new File(excelPath));
            //读取工作表格
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            //读取第一个工作表单
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
//			 System.out.println("总共行数="+(sheet.getLastRowNum()+1));

//			 //得到第一行
//			 HSSFRow row=sheet.getRow(0);
//			//得到第一列
//			 HSSFCell cell=row.getCell(0);
//			 //得到内容
//			 String conment=cell.getStringCellValue();
//			 System.out.println(conment);
            int count_p = 0;//处理省数量
            int count_zz = 0;//处理自治区数量
            int count_zx = 0;//处理直辖市数量
            int count_t = 0;//特别行政区数量
            boolean flag = false;//进入处理方法
            int hf = 0;//记录省、自治区、直辖市划分
            //用于记录特殊参数
            Map<String, Object> needParam = new HashMap<String, Object>();
//			 needParam.put("省直辖县级行政区划","省直辖县级行政区划" );
            needParam.put("上海市", "上海市");
            needParam.put("北京市", "北京市");
            needParam.put("天津市", "天津市");
            needParam.put("重庆市", "重庆市");
//			 needParam.put("台湾省","台湾省" );
            //1省/2自治区/3直辖市 数据集合
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            int index = 0;//处理数据下标
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                //得到第i行
                HSSFRow row = sheet.getRow(i);
                //得到第一列
//					 HSSFCell cell=row.getCell(j);
                //得到内容
//					 String conment=cell.getStringCellValue();
                //行政区划代码	行政区划名称	父行政区划	层级	行政区划英文名称	行政区划简称	区域分类
                String region_code = row.getCell(0).getStringCellValue() == null ? "" : row.getCell(0).getStringCellValue();
                region_code.trim();
                map.put("region_code", region_code);//行政区划代码
                String region_name = row.getCell(1).getStringCellValue() == null ? "" : row.getCell(1).getStringCellValue();
                region_name = region_name.trim();
                map.put("region_name", region_name);//行政区划名称
//					 System.out.println("------>region_name="+region_name);
                if (!"台湾省".equals(region_name) && !"香港特别行政区".equals(region_name) && !"澳门特别行政区".equals(region_name) && region_name.length() > 1) {
                    list.add(map);
                }
                //判断是否有下一行
                if (null != sheet.getRow(i + 1)) {
                    //得到下一行的名称
                    String region_name_next = sheet.getRow(i + 1).getCell(1).getStringCellValue() == null ? "" : sheet.getRow(i + 1).getCell(1).getStringCellValue();
                    region_name_next = region_name_next.trim();
                    if (region_name_next.length() > 1) {
//							 System.out.println("substring------>region_name_next="+region_name_next.substring(region_name_next.length()-2, region_name_next.length()-1));
                        if ("省".equals(region_name_next.substring(region_name_next.length() - 1, region_name_next.length())) && !"台湾省".equals(region_name_next)) {//省
                            flag = true;
                        } else if (region_name_next.contains("自治区") && !"自治区直辖县级行政区划".equals(region_name_next)) {//自治区
                            flag = true;
                        } else if (region_name_next.equals(String.valueOf(needParam.get(region_name_next)))) {//直辖市
                            flag = true;
                        }
                    } else {
                        flag = false;
                    }
                    if (flag) {
                        region_name = String.valueOf(list.get(0).get("region_name"));
                        region_name = region_name.trim();
                        if (region_name.length() > 1) {
//								 System.out.println("substring------>region_name="+region_name.substring(region_name.length()-2, region_name.length()-1));
                            if ("省".equals(region_name.substring(region_name.length() - 1, region_name.length())) && !"台湾省".equals(region_name)) {//省
                                index = 1;
                                flag = true;
                            } else if (region_name.contains("自治区") && !"自治区直辖县级行政区划".equals(region_name)) {//自治区
                                index = 2;
                                flag = true;
                            } else if (region_name.equals(String.valueOf(needParam.get(region_name)))) {//直辖市
                                index = 3;
                                flag = true;
                            }
                        }
                    }
                } else {
                    flag = true;
                    region_name = String.valueOf(list.get(0).get("region_name"));
                    region_name = region_name.trim();
                    if (flag) {
                        if (region_name.length() > 1) {
//								 System.out.println("substring------>region_name="+region_name.substring(region_name.length()-2, region_name.length()-1));
                            if ("省".equals(region_name.substring(region_name.length() - 1, region_name.length())) && !"台湾省".equals(region_name)) {//省
                                index = 1;
                            } else if (region_name.contains("自治区") && !"自治区直辖县级行政区划".equals(region_name)) {//自治区
                                index = 2;
                            } else if (region_name.equals(String.valueOf(needParam.get(region_name)))) {//直辖市
                                index = 3;
                            }
                        }
                    }
                }
                if (flag) {
//						System.out.println(index);
                    switch (index) {
                        case 1://省
//							System.out.println("------------->省数据<-------------region_name="+region_name);
                            handleExcelData1(list);
                            count_p = count_p + 1;
                            flag = false;
                            break;
                        case 2://自治区
//							System.out.println("------------->自治区数据<-------------region_name="+region_name);
                            handleExcelData2(list);
                            count_zz = count_zz + 1;
                            flag = false;
                            break;
                        case 3://直辖市
//							System.out.println("------------->直辖市数据<-------------region_name="+region_name);
                            handleExcelData3(list);
                            count_zx = count_zx + 1;
                            flag = false;
                            break;
                        default:
//							System.out.println("------------->其他<-------------region_name="+region_name);
                            count_t = count_t + 1;
                            flag = false;
                            break;
                    }
                    index = 0;
                    list.clear();
                }
            }
            System.out.println("官方------->23个省(台湾省)、5个自治区、4个直辖市、2个特别行政区");
            System.out.println("处理省数据数量=" + count_p + "个省(不包括台湾省)");
            System.out.println("处理自治区数量=" + count_zz + "个自治区");
            System.out.println("处理直辖市数量=" + count_zx + "个直辖市");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 处理省会数据
     * 官方:
     * （一）全国分为省、自治区、直辖市；
     * （二）省、自治区分为自治州、县、自治县、市；
     * （三）县、自治县分为乡、民族乡、镇。直辖市和较大的市分为区、县。自治州分为县、自治县、市。自治区、自治州、自治县都是民族自治地方。
     */
    public static void handleExcelData1(List<Map<String, Object>> list) {
//		System.out.println("记录数:"+list.size()+"-------->list="+JSONObject.toJSONString(list));
        if (null != list && list.size() > 0) {
            handleExcelData2(list);
        }
    }

    /**
     * 处理自治区数据
     * 官方:
     * （一）全国分为省、自治区、直辖市；
     * （二）省、自治区分为自治州、县、自治县、市；
     * （三）县、自治县分为乡、民族乡、镇。直辖市和较大的市分为区、县。自治州分为县、自治县、市。自治区、自治州、自治县都是民族自治地方。
     */
    public static void handleExcelData2(List<Map<String, Object>> list) {
//		System.out.println("记录数:"+list.size()+"-------->list="+JSONObject.toJSONString(list));
        if (null != list && list.size() > 0) {
            //用于记录不用的参数
            Map<String, Object> delParam = new HashMap<String, Object>();
            delParam.put("市辖区", "市辖区");
            delParam.put("县", "县");

            //县级市
            Map<String, Object> countyLevelCity = CountyLevelCity.countyLevelCity();


            //自治州/市/省直辖县级行政区划/自治区直辖县级行政区划 同一级
            boolean flag = true;
            //取出第一行list
            List<Map<String, Object>> a_list = new ArrayList<Map<String, Object>>();
            //保存处理后的数据直辖市名称
            //行政区划代码	行政区划名称	父行政区划	层级	行政区划英文名称	行政区划简称	区域分类
            //list 第0条记录为map0=省/自治区
            Map<String, Object> map0 = new HashMap<String, Object>();
            String region_name0 = String.valueOf(list.get(0).get("region_name"));
            String region_code0 = String.valueOf(list.get(0).get("region_code"));
            map0.put("region_code", region_code0);//行政区划代码
            map0.put("region_name", region_name0);//行政区划名称
            map0.put("parent_id", "86");//父行政区划
            map0.put("region_level", "1");//层级
            map0.put("region_name_en", "");//行政区划英文名称
            map0.put("region_shortname_en", "");//行政区划简称
            if (region_name0.contains("自治区")) {
                map0.put("region_type", "2");//区域分类
            } else {//省
                map0.put("region_type", "1");//区域分类
            }
            a_list.add(map0);
            for (int i = 1; i < list.size(); i++) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                //取第一行数据map1=自治州/市/省直辖县级行政区划/自治区直辖县级行政区划/盟 /地区
                String region_name1 = String.valueOf(list.get(i).get("region_name"));
                String region_code1 = String.valueOf(list.get(i).get("region_code"));
                map1.put("region_code", region_code1);//行政区划代码
                map1.put("region_name", region_name1);//行政区划名称
                map1.put("parent_id", region_code0);//父行政区划
                map1.put("region_level", "2");//层级
                map1.put("region_name_en", "");//行政区划英文名称
                map1.put("region_shortname_en", "");//行政区划简称
                map1.put("region_type", "0");//区域分类
                if (!"省直辖县级行政区划".equals(region_name1) && !"自治区直辖县级行政区划".equals(region_name1) && !"市辖区".equals(region_name1)) {
                    a_list.add(map1);
                }
                //判断这行是否为市
                if ("市".equals(region_name1.subSequence(region_name1.length() - 1, region_name1.length())) && flag && !"市辖区".equals(region_name1)) {
                    for (int j = i + 1; j < list.size(); j++) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        String region_name2 = String.valueOf(list.get(j).get("region_name"));
                        map2.put("region_code", list.get(j).get("region_code"));//行政区划代码
                        map2.put("region_name", region_name2);//行政区划名称
                        map2.put("parent_id", region_code1);//父行政区划
                        map2.put("region_level", "3");//层级
                        map2.put("region_name_en", "");//行政区划英文名称
                        map2.put("region_shortname_en", "");//行政区划简称
                        map2.put("region_type", "0");//区域分类
                        if ("自治州".contains(region_name2) || "省直辖县级行政区划".equals(region_name2) || "自治区直辖县级行政区划".equals(region_name2)) {
                            i = j - 1;
                            break;
                        } else if ("市".equals(region_name2.subSequence(region_name2.length() - 1, region_name2.length())) && flag && !"市辖区".equals(region_name2) && !region_name2.equals(String.valueOf(countyLevelCity.get(region_name2)))) {//县级市
                            i = j - 1;
                            break;
                        }
                        if (!"市辖区".equals(region_name2)) {
                            a_list.add(map2);
                        }
                    }
                } else if ("自治州".contains(region_name1) || "盟".equals(region_name1.substring(region_name1.length() - 1, region_name1.length())) || "地区".equals(region_name1.substring(region_name1.length() - 2, region_name1.length()))) {//判断这行是否为
                    flag = false;
                    for (int j = i + 1; j < list.size(); j++) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        String region_name2 = String.valueOf(list.get(j).get("region_name"));
                        map2.put("region_code", list.get(j).get("region_code"));//行政区划代码
                        map2.put("region_name", region_name2);//行政区划名称
                        map2.put("parent_id", region_code1);//父行政区划
                        map2.put("region_level", "3");//层级
                        map2.put("region_name_en", "");//行政区划英文名称
                        map2.put("region_shortname_en", "");//行政区划简称
                        map2.put("region_type", "0");//区域分类
                        if ("自治州".contains(region_name2) || "省直辖县级行政区划".equals(region_name2) || "自治区直辖县级行政区划".equals(region_name2)) {
                            i = j - 1;
                            break;
                        }
                        if (!"市辖区".equals(region_name2)) {
                            a_list.add(map2);
                        }
                    }
                } else if ("省直辖县级行政区划".equals(region_name1) || "自治区直辖县级行政区划".equals(region_name1)) {//判断这行是否为
                    flag = false;
                    for (int j = i + 1; j < list.size(); j++) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        String region_name2 = String.valueOf(list.get(j).get("region_name"));
                        map2.put("region_code", list.get(j).get("region_code"));//行政区划代码
                        map2.put("region_name", region_name2);//行政区划名称
                        map2.put("parent_id", region_code0);//父行政区划
                        map2.put("region_level", "2");//层级
                        map2.put("region_name_en", "");//行政区划英文名称
                        map2.put("region_shortname_en", "");//行政区划简称
                        map2.put("region_type", "0");//区域分类
                        if ("自治州".contains(region_name2) || "省直辖县级行政区划".equals(region_name2) || "自治区直辖县级行政区划".equals(region_name2)) {
                            i = j - 1;
                            break;
                        }
                        if (!"市辖区".equals(region_name2)) {
                            a_list.add(map2);
                        }
                    }
                }
            }
            //写入excel或者数据库
            writeData(a_list);
        }
    }

    /**
     * 处理直辖市数据
     * 官方:
     * （一）全国分为省、自治区、直辖市；
     * （二）省、自治区分为自治州、县、自治县、市；
     * （三）县、自治县分为乡、民族乡、镇。直辖市和较大的市分为区、县。自治州分为县、自治县、市。自治区、自治州、自治县都是民族自治地方。
     */
    public static void handleExcelData3(List<Map<String, Object>> list) {
        //用于记录不用的参数
        Map<String, Object> delParam = new HashMap<String, Object>();
        delParam.put("市辖区", "市辖区");
        delParam.put("县", "县");
//		System.out.println("记录数:"+list.size()+"-------->list="+JSONObject.toJSONString(list));
        if (null != list && list.size() > 0) {
            List<Map<String, Object>> a_list = new ArrayList<Map<String, Object>>();
            //保存处理后的数据直辖市名称
            //行政区划代码	行政区划名称	父行政区划	层级	行政区划英文名称	行政区划简称	区域分类
            //list 第0条记录为 map0=直辖市
            Map<String, Object> map0 = new HashMap<String, Object>();
            map0.put("region_code", list.get(0).get("region_code"));//行政区划代码
            map0.put("region_name", list.get(0).get("region_name"));//行政区划名称
            map0.put("parent_id", "86");//父行政区划
            map0.put("region_level", "1");//层级
            map0.put("region_name_en", "");//行政区划英文名称
            map0.put("region_shortname_en", "");//行政区划简称
            map0.put("region_type", "3");//区域分类
            a_list.add(map0);
            for (int i = 1; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                String region_code = list.get(i).get("region_code").toString();
                String region_name = list.get(i).get("region_name").toString();
                map.put("region_code", region_code);//行政区划代码
                map.put("region_name", region_name);//行政区划名称
                map.put("parent_id", list.get(0).get("region_code"));//父行政区划
                map.put("region_level", "3");//层级
                map.put("region_name_en", "");//行政区划英文名称
                map.put("region_shortname_en", "");//行政区划简称
                map.put("region_type", "0");//区域分类
                if (!region_name.equals(String.valueOf(delParam.get(region_name)))) {
                    a_list.add(map);
                }
            }
            //写入excel或者数据库
            writeData(a_list);
        }
    }

    /**
     * 写入数据
     */
    public static void writeData(List<Map<String, Object>> list) {

        try {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("0", "region_code");//行政区划代码
            map.put("1", "region_name");//行政区划名称
            map.put("2", "parent_id");//父行政区划
            map.put("3", "region_level");//层级
            map.put("4", "region_name_en");//行政区划英文名称
            map.put("5", "region_shortname_en");//行政区划简称
            map.put("6", "region_type");//区域分类

            //输出路径
            String outexcelpath = "d://out.xls";

            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(outexcelpath));
            HSSFSheet sheet = workbook.getSheetAt(0);
            //得到行数
            int rows = sheet.getLastRowNum();
            if (null != sheet.getRow(rows) && !"".equals(sheet.getRow(rows).getCell(0).getStringCellValue())) {
                rows = rows + 1;
            }
            System.out.println("从第" + rows + "行开始写数据");
            //创建行对象
            HSSFRow row = null;
            //创建表格对象
            HSSFCell cell = null;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> mapv = list.get(i);
                row = sheet.createRow(rows);
                for (int j = 0; j < 7; j++) {
                    cell = row.createCell(j);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(String.valueOf(mapv.get(map.get(String.valueOf(j)))));
                }
                FileOutputStream fos = new FileOutputStream(outexcelpath);
                //写到磁盘上
                workbook.write(fos);
//			    fos.flush();
                fos.close();
                rows = rows + 1;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 写入03excel
     *
     * @param filePath
     */

    public static void write2003Excel() {

        try {
            //输出路径
            String filePath = "d://out.xls";
            List<String> list = new ArrayList<String>();
            list.add("出路径");
            list.add("fdsfs");
            list.add("adsf");
            list.add("写入数据");
            list.add("写入数据");
            list.add("写入数据");
            list.add("写入数据");

            //创建excel文件对象
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
            //创建一个张表
            HSSFSheet sheet = wb.getSheetAt(0);
            //创建行对象
            HSSFRow row = null;
            //创建表格对象
            HSSFCell cell = null;
            //循环行
            System.out.println("===" + sheet.getPhysicalNumberOfRows());
            for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {//判断是否为空
                    continue;
                }
                //循环列
                for (int j = 0; j < list.size(); j++) {

                    cell = row.createCell(j);//创建单元格

                    String m = list.get(j);

                    cell.setCellValue(m);//赋值
                }
            }

            FileOutputStream out = new FileOutputStream(filePath);

            wb.write(out);

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
