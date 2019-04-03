package com.shsxt.xmjf.api.constants;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092500590907";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCrdIH6/Ix0t8lsBxZwk/B74RDvHMjTVKIeU9gDXjiL3OzFrIapQ2inHWfSlgLchuquMOK9mkxIbjVDwGhpTRq61cKKChVpzrJjhmgnm5Q6LGaMo4GMz6UDqWqheLrJ1RFKzbUuNqqmKRMLS1DV3uQTFD+fgHwvOHxNzRedKH7X5DKeSY4t5e0RYQK7TV7dFtwrmbNIyYsoGlu/TjLNfWwHmepABBzG6d7II1/d6OFIC0gzyRnvRWvUywkPNroaqFUxFiBg0NYK/mMsm4MUMhloVV0Fde5h+vcICV6KnppwVNtAwjy68ZArfoSqGmFBWpyh1uSHPiWwYPLTgzX+ZIqPAgMBAAECggEBAIGH+8sKkXJ8hwe+Mw3KNpzgZFLBhFdkUTZoUYH55O6FcbvFo9asvpvCc3V9L5w7sYD4HdTwhUETQdp1umwTfGY6qEZxZ8wQBGV8XaPOviVU9gS6Yl1nYltp2s0y0Ks69V12G7NWSYZO8bxOojtxsQ1Y9tgh0bA7FG4SzAxHU8z1tAUP5FsfxqfxX8tBn65+puJsqnMRCBHGj1Za/r/d2pMIg8VWtvNATgf53rGjUb0nNsV5wOXhNvk+pLLqfajY42/B6xDZCJoWe1PRJB6jQqvcHirn7NZLo/YZTTjAvtJ/3fWjclCGtUptos/rLY3r9dsnw4uyAs5GvsXgGrsU94ECgYEA5ZckBRJb7ntCs5tcaYtyhjvnhVK9eiw2PCA2zuUg+iRG9CIAZ/e1STfDCXUCYsuEwcA0KRVdAZ0oesZpV8NDsyW+PfJTRmC+IrUC0nfhpAj1/wFRrvi2exF1QWXtrWZ6pmQje6fquCln7/OuZO+a17Y8VKJrksIayc7Wbue4bwkCgYEAvy1t35VgCt90wrZfrHoQGgk9jM8637SLuTiJ5mU2377TRx5VIRbkf1mnE4xBhKWtbZvooMXgfUX1isKp+U05okoF4xRBSWnPob4jy97UkUGYz5shBcz+MhQgXGtPOh+oZvdVdTvuHpwxutI6hxbBsZuo9p+Xo27y094rzXEtetcCgYAI/wsiR73icmICt6u/Vv990wsXZk6WYvUdFgYPxVqtco51Mn/hkws/ezfhPvyA8oiZLvbcRUIHtijH5s5p6fTVKD10H/KzZ0cMBRu/M4Nb8h3xiBOzT3uXBvAZR+0wRju4GWRqjAHehYDVHi9PNTRUD4f9qP+eGI0EK16rqdjIQQKBgFPrPS/IAkDDlh1isNylOCw1oi8NSGqqdir4BpB8HXpZ6aRuTDAaPHMqh4ytJJV1pItYxOTN950F+KLQkIw+ue3jUHI9kesH53y4Wj5DskuQ3+NB1V0Wfv68lvf6ouyAuQt5xqmPhRcquSmo3sgt1RiBm8mmhInZSf52qAOmirBBAoGAH5iQj7KsIrO9MRrvXmMzpvBMtEg8fUZZbXjqT5LH+4fPrsH0YlBaye7YP7GYhB+2kR/RY+b6q1DIkjx70fQGYIaAH2eZ/b2Fww+C81gK6IHcHz632H5rs7l0d9eAevsoDMicD9bD9cDaKufPv0bVcQiTYbDzIz5rmURtGav1jyE=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq3SB+vyMdLfJbAcWcJPwe+EQ7xzI01SiHlPYA144i9zsxayGqUNopx1n0pYC3IbqrjDivZpMSG41Q8BoaU0autXCigoVac6yY4ZoJ5uUOixmjKOBjM+lA6lqoXi6ydURSs21LjaqpikTC0tQ1d7kExQ/n4B8Lzh8Tc0XnSh+1+QynkmOLeXtEWECu01e3RbcK5mzSMmLKBpbv04yzX1sB5nqQAQcxuneyCNf3ejhSAtIM8kZ70Vr1MsJDza6GqhVMRYgYNDWCv5jLJuDFDIZaFVdBXXuYfr3CAleip6acFTbQMI8uvGQK36EqhphQVqcodbkhz4lsGDy04M1/mSKjwIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://fie56f.natappfree.cc/account/notifyCallBack";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://fie56f.natappfree.cc/account/returnCallBack";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

