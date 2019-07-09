package com.yufan.util;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JSON工具类
 *
 * @author
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
        StdSerializerProvider sp = new StdSerializerProvider();
        sp.setNullValueSerializer(new NullSerializer());
        objectMapper = new ObjectMapper(null, sp, null);
        objectMapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 将java对象转成json字符串
     *
     * @param object 为java对象，可以使pojo类、数组、list和map等
     * @return 返回json格式的字符串
     */
    public static String objectToJson(Object object) {
        return objectToJson(object, false);
    }

    /**
     * 将java对象转成json字符串
     *
     * @param object   为java对象，可以使pojo类、数组、list和map等
     * @param isPretty 格式化json字符串
     * @return 返回json格式的字符串
     */
    public static String objectToJson(Object object, boolean isPretty) {
        try {
            String jsonString = "";
            if (isPretty) {
                jsonString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(object);
            } else {
                jsonString = objectMapper.writeValueAsString(object);
            }
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过json格式的字符串，逆转成java对象
     *
     * @param json      需要逆转的数据源
     * @param valueType 需要逆转java对象类型
     * @return 返回Object对象，需要强制转换
     */
    public static Object jsonToObject(String json, Class<?> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过json格式的字符串，逆转成java对象
     * @param json                需要逆转的数据源
     * @param valueType            需要逆转java对象类型
     * @return 返回传递过来的T类型对象
     */
/*	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String json, TypeReference<T> valueType) {
		try {
			return (T)objectMapper.readValue(json, valueType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/

    /**
     * 使用測試
     *
     * @param args
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {

        // 对象
//		String serialValue = JsonUtils.objectToJson(new Student("wang", 20));
//		System.out.println(serialValue);
//		Student student = (Student)JsonUtils.jsonToObject(serialValue, Student.class);
//		System.out.println(student.getName());

        // 数组
		/*		String[] arr1 = { "111", "222" };
		serialValue = JsonUtils.objectToJson(arr1);
		System.out.println(serialValue);
		String[] arr2 = (String[])JsonUtils.jsonToObject(serialValue, String[].class);
		System.out.println(arr2[0]);

		// 集合
		List<Student> list1 = new ArrayList<Student>();
		list1.add(new Student("wang", 20));
		list1.add(new Student("zhang", 21));
		serialValue = JsonUtils.objectToJson(list1);
		System.out.println(serialValue);
		List<Student> list2 = JsonUtils.jsonToObject(serialValue,
				new TypeReference<List<Student>>() {
				});
		System.out.println(list2.get(0).getName());

		// MAP
		Map<Integer, String> map1 = new HashMap<Integer, String>();
		map1.put(1, "11");
		map1.put(2, "22");
		serialValue = JsonUtils.objectToJson(map1);
		System.out.println(serialValue);
		Map<Integer, String> map2 = JsonUtils.jsonToObject(serialValue,
				new TypeReference<Map<Integer, String>>() {
				});
		System.out.println(map2.get(1));

		// 枚举
		Color color = Color.RED;
		serialValue = JsonUtils.objectToJson(color);
		System.out.println(serialValue);
		Color color2 = (Color)JsonUtils.jsonToObject(serialValue, Color.class);
		System.out.println(color2);

		// 注入测试
		serialValue = JsonUtils.objectToJson(new Student("\",{wang}[]:,\"",
				20));
		System.out.println(serialValue);
		Student student3 = (Student)JsonUtils.jsonToObject(serialValue, Student.class);
		System.out.println(student3.getName());

		serialValue = JsonUtils.objectToJson(new Grade());
		System.out.println(serialValue);
		Grade g = (Grade)JsonUtils.jsonToObject(serialValue, Grade.class);
		System.out.println(g.name);*/
        testSQLFilter();
    }

    public static void testSQLFilter() {
//		String str = "{\"Level1\":[\";\",\"=\",\"'\",\"*\",\"%\",\"+\",\",\",\"-\",\"and\",\"exec\",\"insert\",\"select\",\"delete\",\"update\",\"count\",\"chr\",\"mid\",\"master\",\"truncate\",\"char\",\"declare\",\"or\"],\"Level2\":[]}";
        String str = "{\"Level1\":[\";\",\"=\",\"'\",\"*\",\"%\",\"+\",\",\",\"-\",\"and\",\"exec\",\"insert\",\"select\",\"delete\",\"count\",\"truncate\",\"declare\",\"or\"],\"Level2\":[]}";
        str = "{\"request_message\":{\"req_type\":\"19\",\"message\":{\"latitude\":\"22.388103\",\"longitude\":\"114.080734\",\"width\":\"1000\",\"height\":\"1200\",\"count\":\"20\"}}}";
        System.out.println(str);
//		Map<String, Map<String, Object>> rs = JsonUtils.jsonToObject(str, new TypeReference<Map<String, Map<String, Object>>>(){});
//		List<String> list3 = rs.get("Level1");
//		for (int i = 0; i < list3.size(); i++) {
//			System.out.println("p1:" + list3.get(i));
//		}
//		System.out.println(rs.get("request_message"));
//		System.out.println(rs.get("request_message").get("req_type"));

//		String[][] filterString = new String[2][];
//		filterString[0] = new String[]{"where", "1=1", "and", "like", "select", "from", "delete"};
//		
//		System.out.println(JsonUtils.objectToJson(filterString));
//		List<String> list1 = new ArrayList<String>();
//		
//		list1.add("'.*?'");
//		
//		list1.add("exec[\\s]{1,}");
//		list1.add("insert[\\s]{1,}");
//		list1.add("insert[\\s]{1,}");
//		list1.add("delete[\\s]{1,}");
//		list1.add("truncate[\\s]{1,}");
//		list1.add("declare[\\s]{1,}");
//		
//		list1.add("[\\s]{1,}chr[\\s]*[(]{1,}.*?[)]{1,}");
//		list1.add("[\\s]{1,}char[\\s]*[(]{1,}.*?[)]{1,}");
//		list1.add("[\\s]{1,}count[\\s]*[(]{1,}.*?[)]{1,}");
//		
//		list1.add("[\\s]{1,}or[\\s]{1,}");
//		list1.add("[\\s]{1,}and[\\s]{1,}");
//		List<String> list2 = new ArrayList<String>();
//		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
//		map.put("Level1", list1);
//		map.put("Level2", list2);
//		System.out.println(JsonUtils.objectToJson(map));
    }
}

class Student {
    String name;
    int age;

    public Student() {
    }

    public Student(String n, int age) {
        name = n;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}

class NullSerializer extends JsonSerializer<Object> {
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString("");
    }
}

class CustomDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }
}