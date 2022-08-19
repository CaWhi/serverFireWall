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

    public static String getHashRate(Long hashRate){
        if(hashRate <= 0){
            return "0 MH/s";
        }
        int num = 0;
        BigDecimal rate = new BigDecimal(String.valueOf(hashRate));
        BigDecimal thus = new BigDecimal("1000");
        while(rate.compareTo(thus) >= 0){
            rate = rate.divide(thus);
            num++;
        }

        String unit = "";
        switch(num){
            case 0:
                unit = "KH/s";
                break;
            case 1:
                unit = "MH/s";
                break;
            case 2:
                unit = "GH/s";
                break;
            case 3:
                unit = "TH/s";
                break;
            case 4:
                unit = "PH/s";
                break;
            default:
                unit = "KH/s";
                break;
        }

        rate = rate.setScale(2, RoundingMode.HALF_UP);

        return rate.toString() + unit;
    }
}
