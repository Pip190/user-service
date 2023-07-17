package com.chongdong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chongdong.model.Role;
import com.chongdong.model.UserRole;
import com.chongdong.service.RoleService;
import com.chongdong.mapper.RoleMapper;
import com.chongdong.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author wo
* @description 针对表【tcd_role(角色表)】的数据库操作Service实现
* @createDate 2023-07-05 11:00:59
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 根据用户id分配角色
     * @param userId    用户id
     * @param roleIds    角色id数组
     */
    @Override
    public void saveUserRoleRelationShip(Long userId, Long[] roleIds) {
        List<UserRole> userRoleList = new ArrayList<>();
        for(Long roleId : roleIds) {
            if(StringUtils.isEmpty(roleId)) continue;
            UserRole userRole = new UserRole();
            if(userRoleService.existUserRoleRelation(userId,roleId)){
                QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",userId).eq("role_id",roleId);
                userRole = userRoleService.getOne(queryWrapper);
                userRole.setUpdateTime(new Date());
                userRole.setIsDeleted(0);

            }else {
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(new Date());
            }
            userRoleList.add(userRole);
        }
        userRoleService.saveOrUpdateBatch(userRoleList);
        // 改删除状态
        Long[] roleIdByUserId = baseMapper.selectRoleIdByUserId(userId);
        List<Long> notInArray = getElementsNotInArray(roleIdByUserId, roleIds);
        for (Long removeRoleId:notInArray) {
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",userId).eq("role_id",removeRoleId);
            UserRole deleteUserRole = userRoleService.getOne(queryWrapper);
            deleteUserRole.setIsDeleted(1);
            userRoleService.update(deleteUserRole,new QueryWrapper<UserRole>().eq("role_id",removeRoleId).eq("user_id",userId));
        }
    }
    //根据用户获取角色数据
    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        //查询所有的角色
        List<Role> allRolesList =baseMapper.selectList(null);
        //根据用户id，查询用户拥有的角色id
        List<UserRole> existUserRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId).select("role_id"));
        List<Long> existRoleList = existUserRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());
        //对角色进行分类
        List<Role> assignRoles = new ArrayList<Role>();
        for (Role role : allRolesList) {
            //已分配
            if(existRoleList.contains(role.getId())) {
                assignRoles.add(role);
            }
        }
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoles", assignRoles);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    @Override
    public List<Role> selectRoleByUserId(Long id) {
        //根据用户id拥有的角色id
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", id).select("role_id"));
        List<Long> roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        List<Role> roleList = new ArrayList<>();
        if(roleIdList.size() > 0) {
            roleList = baseMapper.selectBatchIds(roleIdList);
        }
        return roleList;
    }


    public static List<Long> getElementsNotInArray(Long[] dataBaseArray, Long[] inputArray) {
        List<Long> result = new ArrayList<>();
        for (Long i : dataBaseArray) {
            boolean found = false;
            for (Long j : inputArray) {
                if (Objects.equals(i, j)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(i);
            }
        }
        return result;
    }
}




