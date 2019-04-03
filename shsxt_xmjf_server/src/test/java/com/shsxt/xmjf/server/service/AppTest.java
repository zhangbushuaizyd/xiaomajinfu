package com.shsxt.xmjf.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shsxt.xmjf.server.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    /*/@Test
    //public void shouldAnswerWithTrue()
    //{
    //    String host = "http://idcard.market.alicloudapi.com";
    //    String path = "/lianzhuo/idcard";
    //    String method = "GET";
    //    String appcode = "218edb69789c4716adc39f9e260da292";
    //    Map<String, String> headers = new HashMap<String, String>();
    //    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
    //    headers.put("Authorization", "APPCODE " + appcode);
    //    Map<String, String> querys = new HashMap<String, String>();
    //    querys.put("cardno", "370703198111300338");
    //    querys.put("name", "郭德昌");
    //
    //
    //    try {
    //        /**
    //         * 重要提示如下:
    //         * HttpUtils请从
    //         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
    //         * 下载
    //         *
    //         * 相应的依赖请参照
    //         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
    //         */
    //        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
    //        System.out.println(response.toString());
    //        //获取response的body
    //        //System.out.println(EntityUtils.toString(response.getEntity()));
    //        String result = EntityUtils.toString(response.getEntity());
    //        JSONObject jsonObject = JSON.parseObject(result);
    //        System.out.println(jsonObject.get("status"));
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}

    //@Test
    //public void test02(){
    //        String host = "http://aliyunverifyidcard.haoservice.com";
    //        String path = "/idcard/VerifyIdcardv2";
    //        String method = "GET";
    //        String appcode = "218edb69789c4716adc39f9e260da292";
    //        Map<String, String> headers = new HashMap<String, String>();
    //        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
    //        headers.put("Authorization", "APPCODE " + appcode);
    //        Map<String, String> querys = new HashMap<String, String>();
    //        querys.put("cardNo", "330529199501020022");
    //        querys.put("realName", "张不帅");
    //
    //
    //        try {
    //            /**
    //             * 重要提示如下:
    //             * HttpUtils请从
    //             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
    //             * 下载
    //             *
    //             * 相应的依赖请参照
    //             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
    //             */
    //            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
    //            System.out.println(response.toString());
    //            //获取response的body
    //            //System.out.println(EntityUtils.toString(response.getEntity()));
    //            String result;
    //            result = EntityUtils.toString(response.getEntity());
    //            System.out.println(result);
    //            JSONObject jsonObject = JSON.parseObject(result);
    //            Integer code = (Integer) jsonObject.get("error_code");
    //            System.out.println(code);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
}
