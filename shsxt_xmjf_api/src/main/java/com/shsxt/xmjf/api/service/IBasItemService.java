package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.querys.BasItemQuery;

import java.util.Map;

public interface IBasItemService {
    //查询操作 basItemQuery 查询参数
    public PageInfo<Map<String,Object>> queryBasItemsByParams(BasItemQuery basItemQuery);
    //详情查询
    public BasItem queryBasItemByItemId(Integer itemId);
}
