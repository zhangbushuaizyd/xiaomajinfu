package com.shsxt.xmjf.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.service.IBusAccountService;
import com.shsxt.xmjf.web.annotations.RequireLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("account")
public class AccountController {


    @Resource
    private IBasUserSecurityService basUserSecurityService;
    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    @Resource
    private IBusAccountService busAccountService;

    @RequestMapping("setting")
    @RequireLogin
    public String setting(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        UserModel userModel = (UserModel) request.getSession().getAttribute(XmjfConstant.SESSION_USER_INFO);
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userModel.getUserId());
        request.setAttribute("security", basUserSecurity);
        return "setting";
    }

    @RequestMapping("index")
    @RequireLogin
    public String index(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "account";
    }

    //认证模块
    @RequestMapping("auth")
    @RequireLogin
    public String auth(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "auth";
    }

    @RequireLogin
    @RequestMapping("recharge")
    public String recharge(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "recharge";
    }

    /**
     * 用户充值
     */
    @RequireLogin
    @RequestMapping("doRecharge")
    public String doRecharge(BigDecimal amount,
                             String imageCode,
                             String busiPwd,
                             HttpServletRequest request,
                             HttpSession session) {
        request.setAttribute("ctx", request.getContextPath());
        if (StringUtils.isBlank(imageCode)) {
            request.setAttribute("msg", "图片验证码不能为空!");
            return "recharge";
        }
        String sessionImageCode = (String) session.getAttribute(XmjfConstant.IMAGE);
        if (StringUtils.isBlank(sessionImageCode)) {
            request.setAttribute("msg", "图片验证码已失效,请刷新页面!");
            return "recharge";
        }
        if (!(imageCode.equals(sessionImageCode))) {
            request.setAttribute("msg", "图片验证码不正确!");
            return "recharge";
        }
        session.removeAttribute(XmjfConstant.IMAGE);
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER_INFO);
        ResultInfo<String> resultInfo = busAccountRechargeService.addBusAccountRecharge(userModel.getUserId(), amount, busiPwd);
        if (resultInfo.getCode().equals(XmjfConstant.OPS_SUCCESS_CODE)) {
            request.setAttribute("result", resultInfo.getResult());
            return "pay";
        } else {
            request.setAttribute("msg", resultInfo.getMsg());
            return "recharge";
        }

    }


    /**
     * 同步通知
     */
    @RequestMapping("returnCallBack")
    public String returnCallBack(
            @RequestParam(name = "out_trade_no") String orderNo,
            @RequestParam(name = "total_amount") BigDecimal totalAmount,
            @RequestParam(name = "app_id") String appId,
            @RequestParam(name = "seller_id") String sellerId,
            @RequestParam(name = "trade_no") String tradeNo,
            HttpServletRequest request
    ) {
        System.out.println("同步通知....");
        try {
            if (checkSign(request)) {
                busAccountRechargeService.updateBusAccountRechargeInfo(orderNo, totalAmount, appId, sellerId, tradeNo);
            }
            request.setAttribute("result", "账户充值成功!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", XmjfConstant.PAY_FAILED_MSG);
        }
        request.setAttribute("ctx", request.getContextPath());
        return "pay_result";
    }

    //异步通知会多一个tradeStatus
    @RequestMapping("notifyCallBack")
    public String notifyCallBack(
            @RequestParam(name = "out_trade_no") String orderNo,
            @RequestParam(name = "total_amount") BigDecimal totalAmount,
            @RequestParam(name = "app_id") String appId,
            @RequestParam(name = "seller_id") String sellerId,
            @RequestParam(name = "trade_no") String tradeNo,
            @RequestParam(name = "trade_status") String tradeStatus,
            HttpServletRequest request) {
        System.out.println("异步通知...");
        try {
            //1. 验签操作
            if (!(checkSign(request))) {
                request.setAttribute("result", "fail");
                return "notify_result";
            }
            //2.检查订单状态
            if(!(tradeStatus.equals(XmjfConstant.TRADE_RESULT))){
                request.setAttribute("result","fail");
                return "notify_result";
            }
            //3.更新订单信息
            busAccountRechargeService.updateBusAccountRechargeInfo(orderNo,totalAmount,appId,sellerId,tradeNo);
            request.setAttribute("result","success");
            return "notify_result";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "fail");
            return "notify_result";
        }
    }

    public Boolean checkSign(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
    }

    @RequestMapping("countAccountInfoByUserId")
    @ResponseBody
    public Map<String,Object> countAccountInfoByUserId(HttpSession session){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER_INFO);
        return busAccountService.countAccountInfoByUserId(userModel.getUserId());
    }

}
