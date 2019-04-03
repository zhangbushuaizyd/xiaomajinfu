package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.po.BusItemLoan;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("item")
public class BasItemController {
    @Resource
    private IBasItemService basItemService;
    @Resource
    private IBusAccountService busAccountService;
    @Resource
    private IBasUserSecurityService basUserSecurityService;
    @Resource
    private IBusItemLoanService busItemLoanService;

    @Resource
    private ISysPictureService sysPictureService;

    @RequestMapping("index")
    public String index(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "invest_list";
    }

    @RequestMapping("details/{itemId}")
    public String details(@PathVariable Integer itemId, HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        BasItem basItem = basItemService.queryBasItemByItemId(itemId);
        request.setAttribute("item", basItem);

        //获取用户SESSION信息
        UserModel userModel = (UserModel) request.getSession().getAttribute(XmjfConstant.SESSION_USER_INFO);
        if (null != userModel) {
            //用户登录状态
            BusAccount busAccount = busAccountService.queryBusAccountByUserId(userModel.getUserId());
            request.setAttribute("busAccount", busAccount);
        }

        //贷款用户基本信息
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(basItem.getItemUserId());
        //用户名脱敏操作
        basUserSecurity.setRealname(basUserSecurity.getRealname().substring(0, 1) + getReplaceStr(basUserSecurity.getRealname().substring(1)));
        //身份证信息脱敏操作
        basUserSecurity.setIdentifyCard(basUserSecurity.getIdentifyCard().substring(0, 4) + getReplaceStr(basUserSecurity.getIdentifyCard().substring(4, 14)) + basUserSecurity.getIdentifyCard().substring(14));
        //加入作用域
        request.setAttribute("loanUser", basUserSecurity);
        BusItemLoan busItemLoan = busItemLoanService.queryBusItemLoanByItemId(itemId);
        //车辆信息
        request.setAttribute("busItemLoan", busItemLoan);

        //安全审核图片信息
        List<Map<String, Object>> pics = sysPictureService.querySysPicturesByItemId(itemId);

        request.setAttribute("pics",pics);

        return "details";
    }


    //*脱敏操作
    private String getReplaceStr(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isNotBlank(str)) {
            for (int i = 0; i < str.length(); i++) {
                stringBuffer.append("*");
            }
        }
        return stringBuffer.toString();
    }

    @RequestMapping("list")
    @ResponseBody
    public PageInfo<Map<String, Object>> queryItemsByParams(BasItemQuery basItemQuery) {
        return basItemService.queryBasItemsByParams(basItemQuery);
    }
}
