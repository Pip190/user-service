package com.chongdong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chongdong.model.Role;
import com.chongdong.service.RoleService;
import com.chongdong.utils.R;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
@CrossOrigin
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 添加角色
     * @param role 角色信息
     * @return 添加成功
     */
    @PostMapping()
    public R save(@RequestBody Role role) {
        role.setCreateTime(new Date());
        roleService.save(role);
        return R.ok();
    }

    /**
     * 修改角色
     * @param role 角色信息，根据id进行更新
     * @return 修改成功/失败
     */
    @PutMapping
    public R update(@RequestBody Role role) {
        role.setUpdateTime(new Date());
        boolean updateById = roleService.updateById(role);
        return updateById ? R.ok() : R.error();
    }

    /**
     * 删除角色
     * @param id 根据id进行删除
     * @return 删除成功
     */
    @DeleteMapping()
    public R delete(Long id) {
        return roleService.deleteById(id);
    }
    @GetMapping("/{id}")
    public R findById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("role",role);
        return role!=null ? R.ok().data(data) : R.error();
    }
    @GetMapping("list")
    public R findAll(@RequestParam(defaultValue = "1") long pageNum,
                     @RequestParam(defaultValue = "4") long pageSize,
                     Role roleQueryVo) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryLikeRoleName = new QueryWrapper<>();
        if(!StringUtils.isEmpty(roleQueryVo.getRoleName())) {
            queryLikeRoleName.like("role_name",roleQueryVo.getRoleName());
        }
        Page<Role> result = roleService.page(page, queryLikeRoleName);
        return result != null ? R.ok().data("role",result) : R.error();
    }
}
