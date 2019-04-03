package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.service.IMqService;
import com.shsxt.xmjf.api.service.ISmsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class MqServiceImpl implements IMqService {

    @Resource
    private ISmsService smsService;

    @Override
    public void doSendSms(Map<String, Object> params) {

        String phone = (String) params.get("phone");
        Integer type = (Integer) params.get("type");
        System.out.println("消费者收到消息 ===> "+phone+"==="+type);
        smsService.sendSms(phone, type);

    }
}
