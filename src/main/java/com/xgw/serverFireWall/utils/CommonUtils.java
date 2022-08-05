package com.xgw.serverFireWall.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class CommonUtils {
    public static BigDecimal baseUnit = new BigDecimal("1000000000000000000");

    /**
     * 币数量基础单位转换
     * @param amount
     * @return
     */
    public static Double dealCoinAmount(BigInteger amount){
        if(amount==null){
            amount = new BigInteger("0");
        }
        BigDecimal amountD = new BigDecimal(amount);

        return amountD.divide(baseUnit, 5, RoundingMode.HALF_UP).doubleValue();
    }
}
