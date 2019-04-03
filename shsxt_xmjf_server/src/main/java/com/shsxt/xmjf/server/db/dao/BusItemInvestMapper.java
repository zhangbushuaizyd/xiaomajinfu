package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusItemInvest;
import com.shsxt.xmjf.api.querys.BusItemInvestQuery;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BusItemInvestMapper extends BaseMapper<BusItemInvest>{

    public List<Map<String,Object>> queryInvestsByParams(BusItemInvestQuery busItemInvestQuery);
    //新手标项目
    public  int queryIsInvestNewItemByUserId(@Param("userId") Integer userId);

    public List<Map<String,Object>> countInvestIncomeInfoByUserId(@Param("userId")Integer userId);

}