package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BusAccount;

import java.util.Map;

public interface IBusAccountService {

    public BusAccount queryBusAccountByUserId(Integer userId);

    public Map<String,Object> countAccountInfoByUserId(Integer userId);
}
