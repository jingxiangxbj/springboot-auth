package com.xslde.action;

import com.xslde.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("beetl","欢迎您："+user.getUsername());
        return "index.html";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @PostMapping("/login")
    public String login(String username,String password,boolean rememberMe,Model model){
        //判断用户名和密码为空
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            model.addAttribute("msg","用户名和密码不能为空！");
            return "login.html";
        }

        //开始登录
        //实际开发中，用户名和密码错误，不给出明确提示
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username,password,rememberMe));
        }catch (UnknownAccountException e){//未知用户异常
            model.addAttribute("msg","用户名错误！");
            return "login.html";
        }catch (LockedAccountException e){//账户锁定
            model.addAttribute("msg","账户被锁定！");
            return "login.html";
        }catch (CredentialsException e){//用户密码错误异常
            model.addAttribute("msg","用户密码错误！");
            return "login.html";
        }catch (Exception e){
            model.addAttribute("msg","其他异常！");
            return "login.html";
        }
        return "redirect:/index";
    }

}
