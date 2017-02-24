package com.tomash.contactgetter.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrew on 2/24/17.
 */

public class Util {
    public static Map<Integer, String> getTypeLabelMap(boolean isMobile) {
        //home -1 same for all
        //mobile -phone -2
        //work -  phone-3 , email,address - 2
        //other - phone - 7 , email,address -3
        final String HOME = "home";
        final String MOBILE = "mobile";
        final String WORK = "work";
        final String OTHER = "other";

        Map<Integer, String> typeLabelMap = new HashMap<>();
        typeLabelMap.put(1, HOME);
        if (isMobile) {
            typeLabelMap.put(2, MOBILE);
            typeLabelMap.put(3, WORK);
            typeLabelMap.put(7, OTHER);
        } else {
            typeLabelMap.put(2, WORK);
            typeLabelMap.put(3, OTHER);
        }
        return typeLabelMap;
    }

    public static Map<String, Integer> getLabelTypeMap(boolean isMobile) {
        Map<Integer, String> typeLabelMap = getTypeLabelMap(isMobile);
        Map<String, Integer> resultMap = new HashMap<>();
        for (Map.Entry<Integer, String> integerStringEntry : typeLabelMap.entrySet()) {
            resultMap.put(integerStringEntry.getValue(), integerStringEntry.getKey());
        }
        return resultMap;
    }
}
