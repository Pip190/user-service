package com.chongdong.service;

import com.chongdong.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author wo
* @description 针对表【tcd_role(è§’è‰²è¡¨)】的数据库操作Service
* @createDate 2023-07-05 11:00:59
*/
public interface RoleService extends IService<Role> {

    /**
     * 给用户分配角色(含重新分配)
     * @param userId    用户id
     * @param roleId    角色id数组
     */
    void saveUserRoleRealtionShip(Long userId, Long[] roleId);

    List<Role> selectRoleByUserId(Long id);

    //根据用户获取角色数据
    Map<String, Object> findRoleByUserId(Long userId);
}
