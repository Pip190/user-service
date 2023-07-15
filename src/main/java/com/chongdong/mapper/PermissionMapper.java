package com.chongdong.mapper;

import com.chongdong.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author wo
* @description 针对表【tcd_permission(权限表)】的数据库操作Mapper
* @createDate 2023-07-05 11:00:59
* @Entity com.chongdong.model.Permission
*/
public interface PermissionMapper extends BaseMapper<Permission> {
    // 超级管理员权限
    List<String> selectPermissionValueByRoot();
    // 系统管理员权限
    List<String> selectPermissionValueByAdmin();
    // 通过用户id查询权限值
    List<String> selectPermissionValueByUserId(Long id);
    // 通过用户id查询权限表集合
    List<Permission> selectPermissionByUserId(Long userId);

}




