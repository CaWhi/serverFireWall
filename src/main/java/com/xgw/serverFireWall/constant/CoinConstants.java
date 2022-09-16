package com.xgw.serverFireWall.constant;

import java.util.ArrayList;
import java.util.List;

public class CoinConstants {
    public static final String ETH = "ETH";

    public static final String ETC = "ETC";

    public static final String RVN = "RVN";

    public static final String ERGO = "ERGO";

    public static List<String> getAllCoins(){
        List<String> coins = new ArrayList<>();
//        coins.add(ETH);
        coins.add(ETC);
        coins.add(RVN);
        coins.add(ERGO);

        return coins;
    };
}
