package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exception.BusiException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.web.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private IBasUserSecurityService basUserSecurityService;

    //请求类型是get
    @GetMapping("user/{userId}")
    @ResponseBody
    public User queryUserByUserId(@PathVariable Integer userId) {
        return userService.queryUserById(userId);
    }

    //① 用户注册
    @PostMapping("user/saveUser")
    @ResponseBody
    public ResultInfo saveUser(String phone, String password, String code) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.saveUser(phone, password, code);
            resultInfo.setMsg("注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
            if (e instanceof BusiException) {
                BusiException be = (BusiException) e;
                resultInfo.setCode(be.getCode());
                resultInfo.setMsg(be.getMsg());
            }
        }
        return resultInfo;
    }

    //② 用户登录
    @RequestMapping("user/userLogin")
    @ResponseBody
    public ResultInfo userLogin(String phone, String password, HttpSession session) {

        ResultInfo resultInfo = new ResultInfo();
        try {
            UserModel userModel = userService.login(phone, password);
            session.setAttribute(XmjfConstant.SESSION_USER_INFO, userModel);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
            if (e instanceof BusiException) {
                BusiException be = (BusiException) e;
                resultInfo.setCode(be.getCode());
                resultInfo.setMsg(be.getMsg());
            }
        }
        return resultInfo;
    }

    //② 用户登录
    @RequestMapping("user/userLogin02")
    @ResponseBody
    public ResultInfo userLogin02(String phone, String password, HttpSession session) {

        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.login(phone, password);
        session.setAttribute(XmjfConstant.SESSION_USER_INFO, userModel);
        return resultInfo;
    }

    //③查询真实姓名
    @RequestMapping("user/checkUserIsRealName")
    @RequireLogin
    @ResponseBody
    public ResultInfo checkUserIsRealName(HttpSession session) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER_INFO);

        basUserSecurityService.checkUserIsRealName(userModel.getUserId());
        resultInfo.setMsg("用户已认证!");
        return resultInfo;
    }

    //④用户实名认证
    @RequestMapping("user/doAuth")
    @RequireLogin
    @ResponseBody
    public ResultInfo doAuth(String realName, String cardNum, String busiPwd, String confirmBusiPwd, HttpSession session) {
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER_INFO);
        ResultInfo resultInfo = basUserSecurityService.updateBasUserSecurityInfo(realName, cardNum, busiPwd, confirmBusiPwd, userModel.getUserId());
        resultInfo.setMsg("用户信息实名认证成功!");
        return resultInfo;
    }
}
