package com.yufan.other.help;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2018/7/27 10:07
 * 功能介绍:  //读取网页
 */
public class ReadHtml {

    //String[] arrayLeve2 = {"01","03","04","05","06","07","08","09","23","25","26","28","29","31","33","34"};//云南省53
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","13"};//江苏32
    //String[] arrayLeve2 = {"01","02","03","05","06","07","08","09","10","11","12","13","28","90"};//湖北省42
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","31"};//湖南省43
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11"};//河北省13
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","12","13","14","15","16","17","18","19","20","51","52","53"};//广东44
    //String[] arrayLeve2 = {"01","02","03","04","05","24","25"};//西藏自治区54
    //String[] arrayLeve2 = {"01"};//北京市11
    //String[] arrayLeve2 = {"01"};//天津市12
    //String[] arrayLeve2 = {"01"};//上海市31
    //String[] arrayLeve2 = {"01","02" };//重庆市50
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11"};//浙江33
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10"};//陕西61
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11"};//山西省14
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","10","11","12","13","15","16","17","18"};//安徽省34
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14"};//广西壮族自治区45
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","29","30"};//甘肃省62
    //String[] arrayLeve2 = {"01","02","22","23","25","26","27","28"};//青海省63
    //String[] arrayLeve2 = {"01", "02", "03", "04", "90"};//海南省46
    //String[] arrayLeve2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09"};//福建省35
    //String[] arrayLeve2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "22", "25", "29"};//内蒙古自治区15
    //String[] arrayLeve2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14"};//辽宁省21
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11" };//江西省36
    //String[] arrayLeve2 = {"01","02","03","04","05" };//宁夏回族自治区64
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","24" };//吉林省22
    //String[] arrayLeve2 = {"01","02","03","04","05","06","23","26","27" };//贵州省52
    //String[] arrayLeve2 = {"01","03","04","05","06","07","08","09","10","11","13","14","15","16","17","18","19","20","32","33","34"};// 四川省51
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17" };//山东省37
    //String[] arrayLeve2 = {"01","02","04","05","23","27","28","29","30","31","32","40","42","43","90" };//新疆维吾尔自治区65
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","90" };//河南省41
    //String[] arrayLeve2 = {"01","02","03","04","05","06","07","08","09","10","11","12","27"};//黑龙江省23
    public static void main(String[] args) {
        System.out.println("---------------------------开始");
        //获取县级下一级数据
        String[] arrayLeve2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "27"};//黑龙江省23
        for (int j = 0; j < arrayLeve2.length; j++) {
            String l1 = "23";
            String l2 = arrayLeve2[j];
            for (int i = 0; i < 100; i++) {
                int page = 0;
                if (i < 10) {
                    page = Integer.parseInt(l1 + l2 + 0 + i);
                } else {
                    page = Integer.parseInt(l1 + l2 + i);
                }
                String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/" + l1 + "/" + l2 + "/" + page + ".html";
//            System.out.println(url);
                ReadAddrHtml(url);
            }
        }


//        for (int i = 0; i < 100; i++) {
//            String l1 = "32";
//            String l2 = "01";
//            int page = 0;
//            if (i < 10) {
//                page = Integer.parseInt(l1 + l2 + 0 + i);
//            } else {
//                page = Integer.parseInt(l1 + l2 + i);
//            }
//            String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/" + l1 + "/" + l2 + "/" + page + ".html";
////            System.out.println(url);
//            ReadAddrHtml(url);
//        }
        System.out.println("---------------------------结束");
    }


    public static void ReadAddrHtml(String url) {
        String content = CollectBase.getInstence().exeConnection(url, "gbk");
//        System.out.println(content);
        content = CollectBase.getInstence().collect(content, "(<table(.*?)</table>)");
        List<Object> contentList = CollectBase.getInstence().collectList(content, "(<tr(.*?)</tr>)");
        for (int i = 0; i < contentList.size(); i++) {
            content = String.valueOf(contentList.get(i));
            String[] contentArr = content.split("</a></td><td>");
            if (contentArr.length > 1) {
                String contend0 = contentArr[0];
                String contend1 = contentArr[1];
//                System.out.println(contend0.split(".html'>")[1]);
//                System.out.println(contentArr[1].replace("</a></td>","").split(">")[1]);
                content = contend0.split(".html'>")[1].trim() + "#" + contentArr[1].replace("</a></td>", "").split(">")[1].trim();
                System.out.println(content);
            }
        }
//        System.out.println("共" + contentList.size());
    }

}
