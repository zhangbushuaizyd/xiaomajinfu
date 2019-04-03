package com.shsxt.xmjf.server.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.enums.OrderStatus;
import com.shsxt.xmjf.api.enums.RechargeType;
import com.shsxt.xmjf.api.exception.BusiException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.MD5;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import com.shsxt.xmjf.server.db.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class BusAccountRechargeServiceImpl implements IBusAccountRechargeService {
    @Resource
    private BusAccountRechargeMapper busAccountRechargeMapper;
    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BusAccountMapper busAccountMapper;

    @Resource
    private BusAccountLogMapper busAccountLogMapper;

    @Resource
    private BusUserStatMapper busUserStatMapper;

    @Resource
    private BusUserIntegralMapper busUserIntegralMapper;

    @Resource
    private BusIntegralLogMapper busIntegralLogMapper;

    @Resource
    private ISmsService smsService;

    @Resource
    private IUserService userService;

    @Override
    public ResultInfo<String> addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPwd) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            /**
             * 1.参数校验
             *     userId:必须实名
             *     amount:非空  >0
             *     busiPwd:非空 必须与数据库密码一致
             */
            checkParams(userId, amount, busiPwd);
            BusAccountRecharge busAccountRecharge = new BusAccountRecharge();
            busAccountRecharge.setStatus(OrderStatus.CHECKING.getType());
            busAccountRecharge.setAddtime(new Date());
            busAccountRecharge.setRemark("PC端用户充值");
            busAccountRecharge.setType(RechargeType.PC.getType());
            busAccountRecharge.setResource("PC端用户充值");
            busAccountRecharge.setRechargeAmount(amount);
            String orderNo = "XMJF_" + new SimpleDateFormat("YYYYMMddHHmmssS").format(new Date()) + RandomCodesUtils.createRandom(true, 20);
            busAccountRecharge.setOrderNo(orderNo);
            busAccountRecharge.setUserId(userId);
            AssertUtil.isTrue(busAccountRechargeMapper.insert(busAccountRecharge) < 1, XmjfConstant.OPS_FAILED_MSG);

            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
            //订单名称，必填
            String subject = "PC端用户充值";
            //商品描述，可空
            String body = "PC端用户充值";
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderNo + "\","
                    + "\"total_amount\":\"" + amount + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"body\":\"" + body + "\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            resultInfo.setResult(result);

        } catch (Exception e) {
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("充值请求发起失败!");
            if (e instanceof BusiException) {
                BusiException busiException = (BusiException) e;
                resultInfo.setCode(busiException.getCode());
                resultInfo.setMsg(busiException.getMsg());
            }
        }
        return resultInfo;
    }


    private void checkParams(Integer userId, BigDecimal amount, String busiPwd) {
        //1. 实名
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
        AssertUtil.isTrue(basUserSecurity.getRealnameStatus() != 1, "用户未实名,请先执行实名操作!");
        AssertUtil.isTrue(null == amount, "请输入充值金额!");
        AssertUtil.isTrue(amount.compareTo(BigDecimal.ZERO) <= 0, "充值金额必须大于0!");
        AssertUtil.isTrue(!(basUserSecurity.getPaymentPassword().equals(MD5.toMD5(busiPwd))), "交易密码不正确!");
    }

    @Override
    //充值成功回调参数处理
    public void updateBusAccountRechargeInfo(String orderNo, BigDecimal amount, String appId, String sellerId, String busiNo) {
        //1. orderNo 非空
        AssertUtil.isTrue(StringUtils.isBlank(orderNo), "订单支付异常,请联系客服!");
        //2. orderNo 存在
        BusAccountRecharge busAccountRecharge = busAccountRechargeMapper.queryBusAccountRechargeByOrderNo(orderNo);
        AssertUtil.isTrue(null == busAccountRecharge,XmjfConstant.PAY_FAILED_MSG );
        //3. 订单金额不能为空,且大于0 并且和订单金额相同
        AssertUtil.isTrue(null == amount || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(busAccountRecharge.getRechargeAmount()) != 0, XmjfConstant.PAY_FAILED_MSG);
        //4. appId：非空  与卖家应用id 一致 sellerId:非空  与卖家sellerId一致
        AssertUtil.isTrue(StringUtils.isBlank(appId) || StringUtils.isBlank(sellerId) || !(appId.equals(XmjfConstant.APP_ID)) || !(sellerId.equals(XmjfConstant.SELLER_ID)), XmjfConstant.PAY_FAILED_MSG);
        //5. 如果订单状态已经结束,结束方法
        if (busAccountRecharge.getStatus() == OrderStatus.SUCCESS.getType()) {
            return;
        }
        // bus_account_recharge 更新 订单状态更新   实际到账金额  审核时间
        busAccountRecharge.setStatus(OrderStatus.SUCCESS.getType());
        busAccountRecharge.setActualAmount(amount);
        busAccountRecharge.setAddtime(new Date());
        busAccountRecharge.setBusiNo(busiNo);
        AssertUtil.isTrue(busAccountRechargeMapper.update(busAccountRecharge)<1,XmjfConstant.PAY_FAILED_MSG);

        //bus_account   根据订单号  查找 用户id-->查找账户  总金额 可用金额 可提现金额
        int userId=busAccountRecharge.getUserId();
        BusAccount busAccount= busAccountMapper.queryBusAccountByUserId(userId);
        busAccount.setTotal(busAccount.getTotal().add(amount));
        busAccount.setCash(busAccount.getCash().add(amount));
        busAccount.setUsable(busAccount.getUsable().add(amount));
        AssertUtil.isTrue(busAccountMapper.update(busAccount)<1,XmjfConstant.PAY_FAILED_MSG);

        //bus_account_log   添加账户信息变动日志
        BusAccountLog busAccountLog=new BusAccountLog();
        busAccountLog.setUsable(busAccount.getUsable());
        busAccountLog.setUserId(userId);
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setOperType("用户充值");
        busAccountLog.setBudgetType(1);// 收入
        busAccountLog.setRepay(busAccount.getRepay());
        busAccountLog.setRemark("PC端用户充值");
        busAccountLog.setOperMoney(amount);
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setAddtime(new Date());
        busAccountLog.setCash(busAccount.getCash());
        busAccountLog.setWait(busAccount.getWait());
        AssertUtil.isTrue(busAccountLogMapper.insert(busAccountLog)<1,XmjfConstant.PAY_FAILED_MSG);

        //bus_user_stat 更新:充值次数 充值总金额
        BusUserStat busUserStat= busUserStatMapper.queryBusUserStatByUserId(userId);
        busUserStat.setRechargeCount(busUserStat.getRechargeCount()+1);
        busUserStat.setRechargeAmount(busUserStat.getRechargeAmount().add(amount));
        AssertUtil.isTrue(busUserStatMapper.update(busUserStat)<1,XmjfConstant.PAY_FAILED_MSG);

        //bus_user_integral   更新:总积分  可用积分

        BusUserIntegral busUserIntegral=busUserIntegralMapper.queryBusUserIntegralByUserId(userId);
        busUserIntegral.setTotal(busUserIntegral.getTotal()+200);// 赠送200积分
        busUserIntegral.setUsable(busUserIntegral.getUsable ()+200);
        AssertUtil.isTrue(busUserIntegralMapper.update(busUserIntegral)<1,XmjfConstant.PAY_FAILED_MSG);


        //bus_integral_log
        //添加积分变动日志
        BusIntegralLog busIntegralLog=new BusIntegralLog();
        busIntegralLog.setWay("用户充值");
        busIntegralLog.setUserId(userId);
        busIntegralLog.setStatus(0);// 收入
        busIntegralLog.setIntegral(200);
        busIntegralLog.setAddtime(new Date());
        AssertUtil.isTrue(busIntegralLogMapper.insert(busIntegralLog)<1,XmjfConstant.PAY_FAILED_MSG);

        User user=userService.queryUserById(userId);
        // 发送短信通知
        smsService.sendSms(user.getMobile(),XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE);
    }

    @Override
    public PageInfo<Map<String, Object>> queryBusAccountRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery) {
        PageHelper.startPage(busAccountRechargeQuery.getPageNum(),busAccountRechargeQuery.getPageSize());
        return new PageInfo<Map<String,Object>>(busAccountRechargeMapper.queryBusAccountRechargesByUserId(busAccountRechargeQuery));
    }

}
