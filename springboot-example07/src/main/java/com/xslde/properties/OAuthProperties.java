package com.xslde.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by xslde on 2018/7/21
 */
@Component//注入到spring容器，方便后面使用
@ConfigurationProperties(prefix = "oauth")//对应application.yml中，oauth下参数
public class OAuthProperties {

    //获取applicaiton.yml下qq下所有的参数
    private QQProperties qq = new QQProperties();

    public QQProperties getQQ() {
        return qq;
    }

    public void setQQ(QQProperties qq) {
        this.qq = qq;
    }
}
