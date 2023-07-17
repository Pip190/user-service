package com.chongdong.service;

import com.alibaba.fastjson.JSONObject;
import com.chongdong.model.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wo
* @description 针对表【tcd_permission(æƒé™)】的数据库操作Service
* @createDate 2023-07-05 11:00:59
*/
public interface PermissionService extends IService<Permission> {
    /**
     * 获取全部菜单
     * @return 所有菜单树
     */
    List<Permission> queryAllMenu();

    //根据角色获取菜单
    List<Permission> selectAllMenu(Long roleId);

    //给角色分配权限
    void saveRolePermissionRelationShip(Long roleId, Long[] permissionId);

    //递归删除菜单
    void removeChildById(Long id);

    //根据用户id获取用户菜单
    List<String> selectPermissionValueByUserId(Long id);

    List<JSONObject> selectPermissionByUserId(Long id);

    //获取全部菜单
    List<Permission> queryAllMenuGuli();

    //递归删除菜单
    void removeChildByIdGuli(Long id);

    // 给角色分配权限
    void saveRolePermissionRealtionShipGuli(Long roleId, Long[] permissionId);
}
