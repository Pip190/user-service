package com.chongdong.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chongdong.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    public TokenAuthFilter(AuthenticationManager authenticationManager,TokenManager tokenManager,RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
        // 判断如果获取权限信息，放到权限上下文中
        /*if(authRequest != null){
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }*/
        if(Objects.nonNull(authRequest)){//不为空，说明已登录
            SecurityContextHolder.getContext().setAuthentication(authRequest);
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(name);
            chain.doFilter(request,response);
        }else {
//            chain.doFilter(request,response);
            throw new RuntimeException("未登录！");
        }
    }

    @SuppressWarnings("unchecked")
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        // 从header获取token
        String token = request.getHeader("token");
        if (token != null){
            // 从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);
            // 从redis获取对应权限列表
            String authStr = (String) redisTemplate.opsForValue().get(username);
            if (StringUtils.isEmpty(authStr)){
                throw new RuntimeException("未获取到登录信息");
            }
            /*
            然后你不是其他接口要访问服务嘛，这里是个鉴权的拦截器，  就是这个只要登陆就有访问路径的权力，不是那种动态权限控制嘛？
            现在authority这个对象里面不是已经有了他的那些菜单权限了嘛，你再获取到当前是属于哪个操作。比如删除什么东西的权限是：user::delete,那你现在不是已经
            获取到当前这个用户的所有权限了嘛，你去，等下，想想
            我这个数据库存的是访问路径还是哪种形式，就这一列，是存user/list之类麽？

做个实验：现在是不是只有admin登录了？那我们把访问接口的token头的值稍微改一下

             */
            JSONObject object = JSONObject.parseObject(authStr, JSONObject.class);
            JSONArray authorities = object.getJSONArray("authorities");
            Collection<GrantedAuthority> authority = new ArrayList<>();
            authorities.forEach(info -> {
                JSONObject parsed = JSONObject.parseObject(JSONObject.toJSONString(info));
                authority.add(new SimpleGrantedAuthority(parsed.getString("authority")));
            });
            /*for(JSONObject permissionValue : authorities) {
                JSONObject.parseObject()

                authority.add(auth);
            }*/
            return new UsernamePasswordAuthenticationToken(username,token,authority);
        }
        return null;
    }

}
