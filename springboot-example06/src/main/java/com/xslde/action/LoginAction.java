package com.xslde.action;

import com.google.gson.Gson;
import com.xslde.model.dto.QQDTO;
import com.xslde.model.dto.QQOpenidDTO;
import com.xslde.properties.OAuthProperties;
import com.xslde.utils.HttpsUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xslde on 2018/7/21
 */
@Controller
public class LoginAction {


    private Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/login")
    public String login(String username, String password, boolean rememberMe, Model model) {
        //判断用户名和密码为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            model.addAttribute("msg", "用户名和密码不能为空！");
            return "login.html";
        }

        //开始登录
        //实际开发中，用户名和密码错误，不给出明确提示
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, rememberMe));
        } catch (UnknownAccountException e) {//未知用户异常
            model.addAttribute("msg", "用户名错误！");
            return "login.html";
        } catch (LockedAccountException e) {//账户锁定
            model.addAttribute("msg", "账户被锁定！");
            return "login.html";
        } catch (CredentialsException e) {//用户密码错误异常
            model.addAttribute("msg", "用户密码错误！");
            return "login.html";
        } catch (Exception e) {
            model.addAttribute("msg", "其他异常！");
            return "login.html";
        }
        return "redirect:/index";
    }


    @Autowired
    private OAuthProperties oauth;


    //QQ登陆对外接口，只需将该接口放置html的a标签href中即可
    @GetMapping("/login/qq")
    public void loginQQ(HttpServletResponse response) {
        try {
            response.sendRedirect(oauth.getQQ().getCode_callback_uri() + //获取code码地址
                    "?client_id=" + oauth.getQQ().getClient_id()//appid
                    + "&state=" + UUID.randomUUID() + //这个说是防攻击的，就给个随机uuid吧
                    "&redirect_uri=" + oauth.getQQ().getRedirect_uri() +//这个很重要，这个是回调地址，即就收腾讯返回的code码
                    "&response_type=code");//授权模式，授权码模式
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //接收回调地址带过来的code码
    @GetMapping("/authorize/qq")
    public String authorizeQQ(Map<String, String> msg, String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", oauth.getQQ().getRedirect_uri());
        params.put("client_id", oauth.getQQ().getClient_id());
        params.put("client_secret", oauth.getQQ().getClient_secret());
        //获取access_token如：access_token=9724892714FDF1E3ED5A4C6D074AF9CB&expires_in=7776000&refresh_token=9E0DE422742ACCAB629A54B3BFEC61FF
        String result = HttpsUtils.doGet(oauth.getQQ().getAccess_token_callback_uri(), params);
        //对拿到的数据进行切割字符串
        String[] strings = result.split("&");
        //切割好后放进map
        Map<String, String> reulsts = new HashMap<>();
        for (String str : strings) {
            String[] split = str.split("=");
            if (split.length > 1) {
                reulsts.put(split[0], split[1]);
            }
        }
        //到这里access_token已经处理好了
        //下一步获取openid，只有拿到openid才能拿到用户信息
        String openidContent = HttpsUtils.doGet(oauth.getQQ().getOpenid_callback_uri() + "?access_token=" + reulsts.get("access_token"));
        //接下来对openid进行处理
        //截取需要的那部分json字符串
        String openid = openidContent.substring(openidContent.indexOf("{"), openidContent.indexOf("}") + 1);
        Gson gson = new Gson();
        //将返回的openid转换成DTO
        QQOpenidDTO qqOpenidDTO = gson.fromJson(openid, QQOpenidDTO.class);

        //接下来说说获取用户信息部分
        //登陆的时候去数据库查询用户数据对于openid是存在，如果存在的话，就不用拿openid获取用户信息了，而是直接从数据库拿用户数据直接认证用户，
        // 否则就拿openid去腾讯服务器获取用户信息，并存入数据库，再去认证用户
        //下面关于怎么获取用户信息，并登陆
        params.clear();
        params.put("access_token", reulsts.get("access_token"));//设置access_token
        params.put("openid", qqOpenidDTO.getOpenid());//设置openid
        params.put("oauth_consumer_key", qqOpenidDTO.getClient_id());//设置appid
        String userInfo = HttpsUtils.doGet(oauth.getQQ().getUser_info_callback_uri(), params);
        QQDTO qqDTO = gson.fromJson(userInfo, QQDTO.class);
        //这里拿用户昵称，作为用户名，openid作为密码（正常情况下，在开发时候用openid作为用户名，再自己定义个密码就可以了）
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(qqDTO.getNickname(), qqOpenidDTO.getOpenid()));
        } catch (Exception e) {
            msg.put("msg", "第三方登陆失败,请联系管理！");
            logger.error(e.getMessage());
            return "login.html";
        }
        return "redirect:/index";
    }


}
