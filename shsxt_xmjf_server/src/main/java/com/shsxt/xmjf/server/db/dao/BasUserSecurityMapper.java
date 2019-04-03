package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface BasUserSecurityMapper extends BaseMapper<BasUserSecurity>{

    //查询借款人
    public BasUserSecurity queryBasUserSecurityByUserId(@Param("userId") Integer userId);

    //查询身份证号码,保证一个身份证只能有一个人注册
    public BasUserSecurity queryBasUserSecurityByCardNum(@Param("cardNum")String cardNum);

}