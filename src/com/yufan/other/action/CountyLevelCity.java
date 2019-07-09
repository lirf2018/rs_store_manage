package com.yufan.other.action;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能名称: 县级市 开发人: lirf
 * 开发时间: 2016下午8:26:59
 * 其它说明：
 */
public class CountyLevelCity {

    public static void main(String[] args) {
        String str = "青铜峡市,灵武市";
        String strs[] = str.split(",");
        Map<String, Object> map = countyLevelCity();
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].equals(String.valueOf(map.get(strs[i])))) {
//				System.out.println(strs[i]);
            } else {
                System.out.println("==========>" + strs[i]);
//				System.out.println("map.put(\""+strs[i]+"\",\""+strs[i]+"\");");
            }
        }

    }


    /**
     * 存放县级市
     */
    public static Map<String, Object> countyLevelCity() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 河北省
        map.put("辛集市", "辛集市");
        map.put("晋州市", "晋州市");
        map.put("新乐市", "新乐市");
        map.put("遵化市", "遵化市");
        map.put("迁安市", "迁安市");
        map.put("武安市", "武安市");
        map.put("南宫市", "南宫市");
        map.put("沙河市", "沙河市");
        map.put("涿州市", "涿州市");
        map.put("定州市", "定州市");
        map.put("安国市", "安国市");
        map.put("高碑店市", "高碑店市");
        map.put("泊头市", "泊头市");
        map.put("任丘市", "任丘市");
        map.put("黄骅市", "黄骅市");
        map.put("河间市", "河间市");
        map.put("霸州市", "霸州市");
        map.put("三河市", "三河市");
        map.put("冀州市", "冀州市");
        map.put("深州市", "深州市");
        // 山西省
        map.put("古交市", "古交市");
        map.put("潞城市", "潞城市");
        map.put("高平市", "高平市");
        map.put("介休市", "介休市");
        map.put("永济市", "永济市");
        map.put("河津市", "河津市");
        map.put("原平市", "原平市");
        map.put("侯马市", "侯马市");
        map.put("霍州市", "霍州市");
        map.put("孝义市", "孝义市");
        map.put("汾阳市", "汾阳市");
        // 内蒙
        map.put("霍林郭勒市", "霍林郭勒市");
        map.put("满洲里市", "满洲里市");
        map.put("牙克石市", "牙克石市");
        map.put("扎兰屯市", "扎兰屯市");
        map.put("额尔古纳市", "额尔古纳市");
        map.put("根河市", "根河市");
        map.put("丰镇市", "丰镇市");
        // 辽宁省
        map.put("新民市", "新民市");
        map.put("瓦房店市", "瓦房店市");
        map.put("普兰店市", "普兰店市");
        map.put("庄河市", "庄河市");
        map.put("海城市", "海城市");
        map.put("东港市", "东港市");
        map.put("凤城市", "凤城市");
        map.put("凌海市", "凌海市");
        map.put("北镇市", "北镇市");
        map.put("盖州市", "盖州市");
        map.put("大石桥市", "大石桥市");
        map.put("灯塔市", "灯塔市");
        map.put("调兵山市", "调兵山市");
        map.put("开原市", "开原市");
        map.put("北票市", "北票市");
        map.put("凌源市", "凌源市");
        map.put("兴城市", "兴城市");
        // 吉林省
        map.put("榆树市", "榆树市");
        map.put("德惠市", "德惠市");
        map.put("蛟河市", "蛟河市");
        map.put("桦甸市", "桦甸市");
        map.put("舒兰市", "舒兰市");
        map.put("磐石市", "磐石市");
        map.put("公主岭市", "公主岭市");
        map.put("双辽市", "双辽市");
        map.put("梅河口市", "梅河口市");
        map.put("集安市", "集安市");
        map.put("临江市", "临江市");
        map.put("扶余市", "扶余市");
        map.put("洮南市", "洮南市");
        map.put("大安市", "大安市");
        // 黑龙江省
        map.put("双城市", "双城市");
        map.put("尚志市", "尚志市");
        map.put("五常市", "五常市");
        map.put("讷河市", "讷河市");
        map.put("虎林市", "虎林市");
        map.put("密山市", "密山市");
        map.put("铁力市", "铁力市");
        map.put("同江市", "同江市");
        map.put("富锦市", "富锦市");
        map.put("绥芬河市", "绥芬河市");
        map.put("海林市", "海林市");
        map.put("宁安市", "宁安市");
        map.put("穆棱市", "穆棱市");
        map.put("北安市", "北安市");
        map.put("五大连池市", "五大连池市");
        map.put("安达市", "安达市");
        map.put("肇东市", "肇东市");
        map.put("海伦市", "海伦市");
        //江苏省
        map.put("江阴市", "江阴市");
        map.put("宜兴市", "宜兴市");
        map.put("新沂市", "新沂市");
        map.put("邳州市", "邳州市");
        map.put("溧阳市", "溧阳市");
        map.put("金坛市", "金坛市");
        map.put("常熟市", "常熟市");
        map.put("张家港市", "张家港市");
        map.put("昆山市", "昆山市");
        map.put("太仓市", "太仓市");
        map.put("启东市", "启东市");
        map.put("如皋市", "如皋市");
        map.put("海门市", "海门市");
        map.put("东台市", "东台市");
        map.put("大丰市", "大丰市");
        map.put("仪征市", "仪征市");
        map.put("高邮市", "高邮市");
        map.put("丹阳市", "丹阳市");
        map.put("扬中市", "扬中市");
        map.put("句容市", "句容市");
        map.put("兴化市", "兴化市");
        map.put("靖江市", "靖江市");
        map.put("泰兴市", "泰兴市");
        //浙江省
        map.put("建德市", "建德市");
        map.put("富阳市", "富阳市");
        map.put("临安市", "临安市");
        map.put("余姚市", "余姚市");
        map.put("慈溪市", "慈溪市");
        map.put("奉化市", "奉化市");
        map.put("瑞安市", "瑞安市");
        map.put("乐清市", "乐清市");
        map.put("海宁市", "海宁市");
        map.put("平湖市", "平湖市");
        map.put("桐乡市", "桐乡市");
        map.put("诸暨市", "诸暨市");
        map.put("嵊州市", "嵊州市");
        map.put("兰溪市", "兰溪市");
        map.put("义乌市", "义乌市");
        map.put("东阳市", "东阳市");
        map.put("永康市", "永康市");
        map.put("江山市", "江山市");
        map.put("温岭市", "温岭市");
        map.put("临海市", "临海市");
        map.put("龙泉市", "龙泉市");
        //安徽省
        map.put("巢湖市", "巢湖市");
        map.put("桐城市", "桐城市");
        map.put("天长市", "天长市");
        map.put("明光市", "明光市");
        map.put("界首市", "界首市");
        map.put("宁国市", "宁国市");
        //福建省
        map.put("福清市", "福清市");
        map.put("长乐市", "长乐市");
        map.put("永安市", "永安市");
        map.put("石狮市", "石狮市");
        map.put("晋江市", "晋江市");
        map.put("南安市", "南安市");
        map.put("龙海市", "龙海市");
        map.put("邵武市", "邵武市");
        map.put("武夷山市", "武夷山市");
        map.put("建瓯市", "建瓯市");
        map.put("建阳市", "建阳市");
        map.put("漳平市", "漳平市");
        map.put("福安市", "福安市");
        map.put("福鼎市", "福鼎市");
        //江西省
        map.put("乐平市", "乐平市");
        map.put("瑞昌市", "瑞昌市");
        map.put("共青城市", "共青城市");
        map.put("贵溪市", "贵溪市");
        map.put("瑞金市", "瑞金市");
        map.put("井冈山市", "井冈山市");
        map.put("丰城市", "丰城市");
        map.put("樟树市", "樟树市");
        map.put("高安市", "高安市");
        map.put("德兴市", "德兴市");
        //山东省
        map.put("章丘市", "章丘市");
        map.put("胶州市", "胶州市");
        map.put("即墨市", "即墨市");
        map.put("平度市", "平度市");
        map.put("莱西市", "莱西市");
        map.put("滕州市", "滕州市");
        map.put("龙口市", "龙口市");
        map.put("莱阳市", "莱阳市");
        map.put("莱州市", "莱州市");
        map.put("蓬莱市", "蓬莱市");
        map.put("招远市", "招远市");
        map.put("栖霞市", "栖霞市");
        map.put("海阳市", "海阳市");
        map.put("青州市", "青州市");
        map.put("诸城市", "诸城市");
        map.put("寿光市", "寿光市");
        map.put("安丘市", "安丘市");
        map.put("高密市", "高密市");
        map.put("昌邑市", "昌邑市");
        map.put("曲阜市", "曲阜市");
        map.put("邹城市", "邹城市");
        map.put("新泰市", "新泰市");
        map.put("肥城市", "肥城市");
        map.put("荣成市", "荣成市");
        map.put("乳山市", "乳山市");
        map.put("乐陵市", "乐陵市");
        map.put("禹城市", "禹城市");
        map.put("临清市", "临清市");
        //河南省
        map.put("巩义市", "巩义市");
        map.put("荥阳市", "荥阳市");
        map.put("新密市", "新密市");
        map.put("新郑市", "新郑市");
        map.put("登封市", "登封市");
        map.put("偃师市", "偃师市");
        map.put("舞钢市", "舞钢市");
        map.put("汝州市", "汝州市");
        map.put("林州市", "林州市");
        map.put("卫辉市", "卫辉市");
        map.put("辉县市", "辉县市");
        map.put("沁阳市", "沁阳市");
        map.put("孟州市", "孟州市");
        map.put("禹州市", "禹州市");
        map.put("长葛市", "长葛市");
        map.put("义马市", "义马市");
        map.put("灵宝市", "灵宝市");
        map.put("邓州市", "邓州市");
        map.put("永城市", "永城市");
        map.put("项城市", "项城市");
        //湖北省
        map.put("大冶市", "大冶市");
        map.put("丹江口市", "丹江口市");
        map.put("宜都市", "宜都市");
        map.put("当阳市", "当阳市");
        map.put("枝江市", "枝江市");
        map.put("老河口市", "老河口市");
        map.put("枣阳市", "枣阳市");
        map.put("宜城市", "宜城市");
        map.put("钟祥市", "钟祥市");
        map.put("应城市", "应城市");
        map.put("安陆市", "安陆市");
        map.put("汉川市", "汉川市");
        map.put("石首市", "石首市");
        map.put("洪湖市", "洪湖市");
        map.put("松滋市", "松滋市");
        map.put("麻城市", "麻城市");
        map.put("武穴市", "武穴市");
        map.put("赤壁市", "赤壁市");
        map.put("广水市", "广水市");
        //湖南省
        map.put("浏阳市", "浏阳市");
        map.put("醴陵市", "醴陵市");
        map.put("湘乡市", "湘乡市");
        map.put("韶山市", "韶山市");
        map.put("耒阳市", "耒阳市");
        map.put("常宁市", "常宁市");
        map.put("武冈市", "武冈市");
        map.put("汨罗市", "汨罗市");
        map.put("临湘市", "临湘市");
        map.put("沅江市", "沅江市");
        map.put("资兴市", "资兴市");
        map.put("洪江市", "洪江市");
        map.put("冷水江市", "冷水江市");
        map.put("涟源市", "涟源市");
        map.put("津市市", "津市市");
        //广东省
        map.put("乐昌市", "乐昌市");
        map.put("南雄市", "南雄市");
        map.put("台山市", "台山市");
        map.put("开平市", "开平市");
        map.put("鹤山市", "鹤山市");
        map.put("恩平市", "恩平市");
        map.put("廉江市", "廉江市");
        map.put("雷州市", "雷州市");
        map.put("吴川市", "吴川市");
        map.put("高州市", "高州市");
        map.put("化州市", "化州市");
        map.put("信宜市", "信宜市");
        map.put("高要市", "高要市");
        map.put("四会市", "四会市");
        map.put("兴宁市", "兴宁市");
        map.put("陆丰市", "陆丰市");
        map.put("阳春市", "阳春市");
        map.put("英德市", "英德市");
        map.put("普宁市", "普宁市");
        map.put("连州市", "连州市");
        map.put("罗定市", "罗定市");
        //广西壮族自治区
        map.put("岑溪市", "岑溪市");
        map.put("东兴市", "东兴市");
        map.put("桂平市", "桂平市");
        map.put("北流市", "北流市");
        map.put("宜州市", "宜州市");
        map.put("合山市", "合山市");
        map.put("凭祥市", "凭祥市");
        //四川省
        map.put("都江堰市", "都江堰市");
        map.put("江油市", "江油市");
        map.put("简阳市", "简阳市");
        map.put("彭州市", "彭州市");
        map.put("华蓥市", "华蓥市");
        map.put("邛崃市", "邛崃市");
        map.put("峨眉山市", "峨眉山市");
        map.put("阆中市", "阆中市");
        map.put("万源市", "万源市");
        map.put("崇州市", "崇州市");
        map.put("广汉市", "广汉市");
        map.put("什邡市", "什邡市");
        map.put("绵竹市", "绵竹市");
        //陕西省
        map.put("兴平市", "兴平市");
        map.put("韩城市", "韩城市");
        map.put("华阴市", "华阴市");
        //甘肃省
        map.put("玉门市", "玉门市");
        map.put("敦煌市", "敦煌市");
        //宁夏回族自治区
        map.put("灵武市", "灵武市");
        map.put("青铜峡市", "青铜峡市");
        //贵州省
        map.put("清镇市", "清镇市");
        map.put("赤水市", "赤水市");
        map.put("仁怀市", "仁怀市");
        //云南省
        map.put("安宁市", "安宁市");
        map.put("宣威市", "宣威市");
        return map;
    }

}
