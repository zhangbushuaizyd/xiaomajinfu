package com.shsxt.xmjf.api.querys;

import java.io.Serializable;

public class BasItemQuery implements Serializable{
    private static final long serialVersionUID = -7826088859401808602L;
    //类型
    private Integer itemType;
    //时间
    private Integer itemCycle;  //1:0到30 2: 30到90 3: 90
    //历史项目和可投项目
    private Integer isHistory; //是否为历史项目 1-历史项目 0-可投项目

    private Integer pageNum=1;

    private Integer pageSize=10;

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItemCycle() {
        return itemCycle;
    }

    public void setItemCycle(Integer itemCycle) {
        this.itemCycle = itemCycle;
    }

    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
