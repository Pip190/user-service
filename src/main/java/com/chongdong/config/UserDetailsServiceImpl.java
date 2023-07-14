package com.chongdong.config;

import com.chongdong.model.User;
import com.chongdong.service.PermissionService;
import com.chongdong.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private PermissionService permissionService;
    public UserDetailsServiceImpl(UserService userService,PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库
        User user = userService.selectByUsername(username);
        // 判断数据库是否存在
        if (user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        User curUser = new User();
        BeanUtils.copyProperties(user,curUser);
        // 根据用户查询用户权限列表
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser =new SecurityUser();
        //
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(permissionValueList);
        return securityUser;
    }
}
