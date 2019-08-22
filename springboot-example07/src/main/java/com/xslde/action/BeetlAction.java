package com.xslde.action;

import com.xslde.model.mapped.User;
import org.apache.shiro.SecurityUtils;
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
        //获取用户名
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        //通过模板引擎将用户信息传递到页面
        model.addAttribute("beetl","欢迎您："+user.getNickname());
        return "index.html";
    }


}
