package com.xslde.model;

import java.io.Serializable;

/**
 * @Author xslde
 * @Description
 * @Date 2018/7/20 16:23
 */
public class User implements Serializable {


    //用户名称
    private String username;

    //用户密码
    private String password;

    //密码加盐
    private String salt;

    //用户是否可用
    private Integer available;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}
