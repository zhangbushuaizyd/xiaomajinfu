package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUser;
import com.shsxt.xmjf.api.po.User;

public interface IUserService {
    public User queryUserById(Integer userId);

    public BasUser queryBasUserByPhone(String phone);

     //添加用户方法
    public void saveUser(String phone,String password,String code);

    //注册
    public UserModel login(String phone,String password);

}
