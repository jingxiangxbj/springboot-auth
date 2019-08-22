package com.xslde.configurer;

import com.xslde.model.mapped.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @Author xslde
 * @Description
 * @Date 2018/7/20 16:30
 */
public class ShiroRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String username = (String) token.getPrincipal();
        //开发中，这里都是去数据库查询
        //做demo，就不查询了
        if (!"xslde".equals(username)&&!"test".equals(username)&&!"xslde.com".equals(username)){
            throw new UnknownAccountException("用户不存在！");
        }
        User user =null;
        if ("xslde".equals(username)){
            user = new User();
            user.setUsername("xslde");
            user.setPassword("0caf568dbf30f5c33a13c56b869259fc");
            user.setSalt("abcd");
            user.setAvailable(1);
        }
        if ("test".equals(username)){
            user = new User();
            user.setUsername("test");
            user.setPassword("0caf568dbf30f5c33a13c56b869259fc");
            user.setSalt("abcd");
            user.setAvailable(0);
        }

        //这是模拟数据库里面拥有QQ第三方用户信息
        if ("xslde.com".equals(username)){
            user = new User();
            user.setUsername("xslde.com");
            user.setAvailable(1);
            user.setSalt("abcd");
            user.setPassword("6e20337c6b222fa0a8c3bbb9dd979374");//
        }
        if (user.getAvailable()!=1){
            throw  new LockedAccountException("账户已被锁定");
        }
        return  new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }

    //生成一个加盐密码
    public static void main(String[] args) {
        String hashAlgorithmName = "md5";//加密类型
        Integer iteration = 2;//迭代次数
        String password = "123456";
        String salt = "abcd";
        String s = new SimpleHash(hashAlgorithmName,password,salt,iteration).toHex();
        System.out.println(s);
        //加密后的密码
        //0caf568dbf30f5c33a13c56b869259fc

    }
}
