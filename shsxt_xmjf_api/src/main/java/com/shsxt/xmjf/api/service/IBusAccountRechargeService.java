package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;

import java.math.BigDecimal;
import java.util.Map;

public interface IBusAccountRechargeService {
    //添加充值用户
    public ResultInfo<String> addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPwd);

    //充值成功表操作
    public void updateBusAccountRechargeInfo(String orderNo, BigDecimal amount, String appId, String sellerId, String tradeNo);

    //充值记录操作
    public PageInfo<Map<String,Object>> queryBusAccountRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery);

}
