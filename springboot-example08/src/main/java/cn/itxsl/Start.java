package cn.itxsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author itxsl
 * @Description
 * @Date 2018/10/8 11:03
 */
@Controller
@SpringBootApplication
public class Start {

    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }

    @GetMapping({"/index","/"})
    public String index(){
        return "index";
    }

}
