package com.shsxt.xmjf.api.utils;

import com.shsxt.xmjf.api.exception.BusiException;

public class AssertUtil {
    public static void isTrue(Boolean flag,String msg){
        if (flag){
            throw new BusiException(msg);
        }
    }
}
