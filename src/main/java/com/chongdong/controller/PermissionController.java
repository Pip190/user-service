package com.chongdong.controller;

import com.chongdong.model.Permission;
import com.chongdong.service.PermissionService;
import com.chongdong.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping("/permission")
@CrossOrigin
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 查询所有菜单树（权限）
     * @return 所有菜单及其子菜单
     */
    @GetMapping
    public R indexAllPermission() {
        List<Permission> list =  permissionService.queryAllMenu();
        return R.ok().data("children",list);
    }

    /**
     * 给角色id授权哪些权限id
     * @param roleId 角色id
     * @param permissionId 权限值id
     * @return 成功
     */
    @PostMapping("/doAssign")
    public R doAssign(Long roleId, Long[] permissionId) {
        permissionService.saveRolePermissionRealtionShipGuli(roleId,permissionId);
        return R.ok();
    }

    /**
     * 查询该角色可选择的[可选中]菜单树（权限）
     * @param roleId 角色id
     * @return 权限树
     */
    @GetMapping("toAssign/{roleId}")
    public R toAssign(@PathVariable Long roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return R.ok().data("children", list);
    }

}
