package com.xslde.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author xslde
 * @Description
 * @Date 2018/7/20 15:22
 */
@Controller
public class BeetlAction {

    @GetMapping({"/","/index","/beetl"})
    public String beetl(Model model){
        model.addAttribute("beetl","测试一下通过模板引擎传递参数！");
        return "index.html";
    }

}
