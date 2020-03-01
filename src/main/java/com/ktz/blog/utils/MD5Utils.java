package com.ktz.blog.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 曾泉明 on 2020/2/25 17:36
 */
public class MD5Utils {

    /**
     * MD5加密
     * @param str 加密前的字符串
     * @return    加密后的字符串
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            int j;
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < b.length; i++) {
                j = b[i];
                if (j < 0) {
                    j += 256;
                }
                if (j < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(j));
            }
            //32位加密
            return sb.toString();
            //16位加密
            //return sb.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.code("123456"));
    }
}
