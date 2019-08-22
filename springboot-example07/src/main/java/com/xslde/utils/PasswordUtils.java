package com.xslde.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by xslde on 2018/7/23
 */
public class PasswordUtils {

    /**
     *
     * @param salt 盐
     * @param password 明文密码
     * @return
     */
    public static String getPassword(String salt,String password){
        String hashAlgorithmName = "md5";//加密类型
        Integer iteration = 2;//迭代次数
        return new SimpleHash(hashAlgorithmName,password,salt,iteration).toHex();
    }

}
