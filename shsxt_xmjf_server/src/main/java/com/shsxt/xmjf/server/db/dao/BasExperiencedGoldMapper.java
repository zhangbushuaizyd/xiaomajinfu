package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasExperiencedGold;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface BasExperiencedGoldMapper extends BaseMapper<BasExperiencedGold>{
    //根据用户id查询体验金
    public  BasExperiencedGold queryBasExperiencedGoldByUserId(@Param("userId") Integer userId);
}