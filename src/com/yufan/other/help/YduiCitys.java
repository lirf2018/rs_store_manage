package com.yufan.other.help;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2018/11/20 10:18
 * 功能介绍: ydui.citys.js  生成收货地址静态文件(前端)
 */
public class YduiCitys {

    public static void main(String[] args) {
        System.out.println("-----start-----");
        long st = System.currentTimeMillis();
        //保存数据
        List<Map<String, String>> list1 = new ArrayList<>();//省
        List<Map<String, String>> list2 = new ArrayList<>();//市
        List<Map<String, String>> list3 = new ArrayList<>();//县
        List<Map<String, String>> list4 = new ArrayList<>();//镇


        File inFile = new File("C:\\Users\\usersLi\\Desktop\\垃圾\\tb_region.txt");
        int count = 0;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(inFile);
            bufferedReader = new BufferedReader(fileReader);
            String line = "aa";
            //一行一行读
            while (line != null) { //按行读数据
                line = bufferedReader.readLine();
                //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
                //分离省
                if (StringUtils.isNotEmpty(line)) {
                    String lines[] = line.split(";");
                    String region_code = lines[0];
                    String region_name = lines[1];
                    String parent_id = lines[2];
                    Map<String, String> map = new HashMap<>();
                    map.put("region_code", region_code);
                    map.put("region_name", region_name);
                    map.put("parent_id", parent_id);
                    int region_level = Integer.parseInt(lines[3]);//region_level
                    if (region_level == 1) {
                        list1.add(map);
                    } else if (region_level == 2) {
                        list2.add(map);
                    } else if (region_level == 3) {
                        list3.add(map);
                    } else if (region_level == 4) {
                        list4.add(map);
                    }
                    count++;
                }
            }

            //处理数据
            JSONArray result = new JSONArray();//最终结果
            for (int i = 0; i < list1.size(); i++) {
                String region_code1 = list1.get(i).get("region_code");
                String region_name1 = list1.get(i).get("region_name");
                JSONObject leve1 = new JSONObject();//省
                leve1.put("r_c", region_code1);
                leve1.put("r_n", region_name1);

                JSONArray r_c_l = new JSONArray();//市列表
                for (int j = 0; j < list2.size(); j++) {
                    String region_code2 = list2.get(j).get("region_code");
                    String region_name2 = list2.get(j).get("region_name");
                    String parent_id2 = list2.get(j).get("parent_id");
                    if (!parent_id2.equals(region_code1)) {
                        continue;
                    }
                    JSONObject leve2 = new JSONObject();//市
                    leve2.put("r_c", region_code2);
                    leve2.put("r_n", region_name2);

                    JSONArray r_co_l = new JSONArray();//县列表
                    for (int k = 0; k < list3.size(); k++) {
                        String region_code3 = list3.get(k).get("region_code");
                        String region_name3 = list3.get(k).get("region_name");
                        String parent_id3 = list3.get(k).get("parent_id");
                        if (!parent_id3.equals(region_code2)) {
                            continue;
                        }
                        JSONObject leve3 = new JSONObject();//县
                        leve3.put("r_c", region_code3);
                        leve3.put("r_n", region_name3);

//                        JSONArray r_t_l = new JSONArray();//镇列表
//                        for (int l = 0; l < list4.size(); l++) {
//                            String region_code4 = list4.get(l).get("region_code");
//                            String region_name4 = list4.get(l).get("region_name");
//                            String parent_id4 = list4.get(l).get("parent_id");
//                            if (!parent_id4.equals(region_code3)) {
//                                continue;
//                            }
//                            JSONObject leve4 = new JSONObject();//县
//                            leve4.put("r_c", region_code4);
//                            leve4.put("r_n", region_name4);
//                            r_t_l.add(leve4);
//                        }
//                        leve3.put("r_t_l", r_t_l);//县放镇列表

                        r_co_l.add(leve3);
                    }
                    leve2.put("r_co_l", r_co_l);//市放县列表
                    r_c_l.add(leve2);
                }
                leve1.put("r_c_l", r_c_l);//省放市列表
                result.add(leve1);
            }

            System.out.println("==========开始写数据==================");
            String str = "//省市县镇\n" +
                    "!function () {\n" +
                    "var citys =" + JSONObject.toJSONString(result);
            str = str + "\nif (typeof define === \"function\") {\n" +
                    "define(citys)\n" +
                    "} else {\n" +
                    "window.YDUI_CITYS = citys\n" +
                    "}\n" +
                    "}();";
            writeFile(str);
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
//        System.out.println(count);
//        System.out.println("list1="+list1.size());
//        System.out.println("list2="+list2.size());
//        System.out.println("list3="+list3.size());
//        System.out.println("list4="+list4.size());
//        System.out.println("size1="+list1.size() + (list1.size() + list2.size() + list3.size() + list4.size()));
        long et = System.currentTimeMillis();
        System.out.println("-----end-----" + (et - st));
    }


    public static void writeFile(String str) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\usersLi\\Desktop\\上线\\ydui.citys.my.min.js"));
            BufferedOutputStream buf = new BufferedOutputStream(outputStream);
            buf.write(str.getBytes());
            buf.flush();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
