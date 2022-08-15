package com.xgw.serverFireWall.utils;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    public static <T> List<List<T>> pageList(List<T> data, Integer pageSize){
        if(CollectionUtils.isEmpty(data)){
            return null;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 50;
        }

        List<List<T>> result = new ArrayList<>();
        List<T> page = null;
        for(int i=0; i < data.size(); i++){
            if(i%pageSize == 0){
                if(!CollectionUtils.isEmpty(page)){
                    result.add(page);
                }
                page = new ArrayList<>();
            }
            page.add(data.get(i));
        }
        if(!CollectionUtils.isEmpty(page)){
            result.add(page);
        }

        return result;
    }
}
