package com.chongdong.filter;

import com.alibaba.fastjson.JSONObject;
import com.chongdong.config.SecurityUser;
import com.chongdong.security.TokenManager;
import com.chongdong.utils.R;
import com.chongdong.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager,TokenManager tokenManager,RedisTemplate redisTemplate){
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        // 设置只允许POST请求
        this.setPostOnly(false);
        // 设置登录路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
    }

    // 1.获取表单提交的用户名和密码
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 获取表单提交的数据
        /*try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getParameter("username"),request.getParameter("password"),new ArrayList<>()));

    }
    // 2.认证成功调用的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        // 认证成功，得到认证成功之后用户信息
        SecurityUser user = (SecurityUser)authResult.getPrincipal();
        // 根据用户名生成token
        String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());
        // 把用户名称和用户权限列表放到redis
        redisTemplate.opsForValue().set(user.getCurrentUserInfo().getUsername(),user.getPermissionValueList());
        /*User newUser = user.getCurrentUserInfo().setToken(token);
        user.setCurrentUserInfo(newUser);
        这里就是登录，匹配密码成功之后，把获取到的token、用户信息，菜单权限放在redis，，redis的前缀就是：oauth::{用户名}
        */
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("user",user.getCurrentUserInfo().setToken(token));
        dataMap.put("authorities",user.getAuthorities());
//        redisTemplate.opsForValue().set(user.getCurrentUserInfo().getUsername(), JSONObject.toJSONString(dataMap),24, TimeUnit.HOURS);
        // 放回token10
        Map<String,Object> returnInfo = new HashMap<>();
        returnInfo.put("user",user.getUsername());
        returnInfo.put("token",token);
        returnInfo.put("authorities",user.getAuthorities());
        ResponseUtil.out(response, R.ok().data("info",returnInfo));
    }
    // 3.认证失败调用的方法
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        ResponseUtil.out(response, R.error().message("认证失败"));
    }
}
