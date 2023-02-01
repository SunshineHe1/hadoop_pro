package com.gao.hadoop.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 11526
 */
public class StringUtils {
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0 || "null".equals(str);
    }
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static Map<String, String> argsToMap(String[] args){
        HashMap<String,String> map = new HashMap<>(args.length/2);
        int i = 0;
        while (i < args.length){
            final String key = getKeyFromArgs(args, i);
            i += 1;
            if (i >= args.length){
                map.put(key, null);
            }else if(args[i].startsWith("--") || args[i].startsWith("-")){
                map.put(key, null);
            }else {
                map.put(key, args[i]);
                i += 1;
            }
        }
        return map;
    }

    private static String getKeyFromArgs(String[] args, int index) {
        String key;
        if (args[index].startsWith("--")){
            key = args[index].substring(2);
        }else if(args[index].startsWith("-")){
            key = args[index].substring(1);
        }else {
            throw new IllegalArgumentException(
                    String.format(
                    "Error parsing arguments '%s' on '%s', Please prefix keys with -- or -.",
                            Arrays.toString(args), args[index]
            ));
        }
        if (key.isEmpty()){
            throw new IllegalArgumentException(
                    "The input" + Arrays.toString(args) + "contains an empty argument."
            );
        }
        return key;
    }
}
