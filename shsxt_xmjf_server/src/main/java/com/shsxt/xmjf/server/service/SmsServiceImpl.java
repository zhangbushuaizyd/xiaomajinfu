package com.shsxt.xmjf.server.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.PhoneUtil;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements ISmsService {
    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name="redisTemplate")
    private ValueOperations<String,Object> valueOperations;

    @Override
    public void sendSms(String phone, Integer type) {
        checkParams(phone,type);

        //产生随机码
        String code= RandomCodesUtils.createRandom(true,4);

        //登录模板或者是注册模板
        if (type == XmjfConstant.SMS_LOGIN_TYPE){
            doSendSms( phone,type,code, XmjfConstant.SMS_LOGIN_TEMPLATE_CODE);
        }else if (type == XmjfConstant.SMS_REGISTER_TYPE){

            //注册模板,防止手机号重复注册
            AssertUtil.isTrue(null !=(userService.queryBasUserByPhone(phone)),"手机号已经被注册");
            doSendSms(phone,type,code,XmjfConstant.SMS_REGISTER_TEMPLATE_CODE);
            //设置key 并设置过期时间为180s
            valueOperations.set("phone::"+ phone+"::templateCode::"+XmjfConstant.SMS_REGISTER_TEMPLATE_CODE,code,180, TimeUnit.SECONDS);
        }else if (type== XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE){
            doSendSms(phone,type,phone,XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TEMPLATE_CODE );
        }else if(type==XmjfConstant.SMS_RECHARGE_SUCCESS_NOTIFY_TYPE){
            doSendSms(phone,type,phone,XmjfConstant.SMS_RECHARGE_SUCCESS_NOTIFY_TEMPLATE_CODE);
        }else if(type==XmjfConstant.SMS_INVEST_SUCCESS_NOTIFY_TYPE){
            doSendSms(phone,type,phone,XmjfConstant.SMS_INVEST_SUCCESS_NOTIFY_TEMPLATE_CODE);
        }else {
            System.out.println("暂时未想到!");
        }
    }

    //参数校验
    private void checkParams(String phone, Integer type) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)),"手机号格式非法!");
        AssertUtil.isTrue(null==type,"短信类型不能为空!");
        AssertUtil.isTrue(!(type==XmjfConstant.SMS_LOGIN_TYPE||type==XmjfConstant.SMS_REGISTER_TYPE||type==XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE||type==XmjfConstant.SMS_INVEST_SUCCESS_NOTIFY_TYPE),"短信类型不合法!");
    }

    //短信发送的方法
    public void doSendSms(String phone, Integer type, String code,String templateCode){
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            final String product = XmjfConstant.SMS_PRODUCT;
            final String domain = XmjfConstant.SMS_DOMAIN;
            final String accessKeyId =XmjfConstant.SMS_AK;
            final String accessKeySecret = XmjfConstant.SMS_SK;
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(phone);
            request.setSignName(XmjfConstant.SMS_SIGN);
            request.setTemplateCode(templateCode);
            request.setTemplateParam("{\"code\":\""+code+"\"}");
            System.out.println("=============短信验证码:"+code+"==========================");
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                System.out.println("短信发送成功。。。");
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
