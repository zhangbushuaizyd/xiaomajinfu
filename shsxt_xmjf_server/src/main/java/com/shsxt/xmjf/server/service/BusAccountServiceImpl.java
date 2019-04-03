package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.service.IBusAccountService;
import com.shsxt.xmjf.server.db.dao.BusAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusAccountServiceImpl implements IBusAccountService{
    @Resource
    private BusAccountMapper busAccountMapper;
    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
        return busAccountMapper.queryBusAccountByUserId(userId);
    }

    //统计报表展示
    @Override
    public Map<String, Object> countAccountInfoByUserId(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data1 = new ArrayList<>();
        Map<String, Object> tempMap = busAccountMapper.countAccountInfoByUserId(userId);

        for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
            if (entry.getKey().equals("总资产")){
                result.put("data2",entry.getValue());
                continue;
            }
            Map<String,Object> temp = new HashMap<>();
            temp.put("name",entry.getKey());
            temp.put("y",entry.getValue());
            data1.add(temp);

        }
        result.put("data1",data1);
        return result;
    }
}
