package com.chongdong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chongdong.model.UserRole;
import com.chongdong.service.UserRoleService;
import com.chongdong.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author wo
* @description 针对表【tcd_user_role】的数据库操作Service实现
* @createDate 2023-07-05 11:00:59
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

    @Override
    public boolean existUserRoleRelation(Long userId, Long roleId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("role_id",roleId);
        UserRole one = this.getOne(queryWrapper);
        return one != null;
    }
}




