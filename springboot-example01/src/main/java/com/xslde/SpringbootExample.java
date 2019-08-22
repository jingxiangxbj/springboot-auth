package com.xslde;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xslde
 * @Description
 * @Date 2018/7/20 14:45
 */
@SpringBootApplication //表明这是一个springboot启动类
public class SpringbootExample {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootExample.class,args);
    }

    @RestController
    class TestRest{
        @GetMapping("/test")
        public String test(){
            return "测试一下！";
        }
    }

}
