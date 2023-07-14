package com.chongdong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chongdong.model.Role;
import com.chongdong.service.RoleService;
import com.chongdong.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
//@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("{page}/{limit}")
    public R index(@PathVariable Long page, @PathVariable Long limit, Role role) {
        Page<Role> pageParam = new Page<>(page, limit);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(role.getRoleName())) {
            wrapper.like("role_name",role.getRoleName());
        }
        roleService.page(pageParam,wrapper);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("save")
    public R save(@RequestBody Role role) {
        roleService.save(role);
        return R.ok();
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping
    public R update(@RequestBody Role role) {
        boolean updateById = roleService.updateById(role);
        return updateById ? R.ok() : R.error();
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public R delete(Long id) {
        roleService.removeById(id);
        return R.ok();
    }
    @GetMapping("/{id}")
    public R findById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("role",role);
        return role!=null ? R.ok().data("role",role) : R.error();
    }
    @GetMapping()
    public R findAll(@RequestParam(defaultValue = "1") long pageNum,
                     @RequestParam(defaultValue = "4") long pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        Page<Role> result = roleService.page(page, new QueryWrapper<>());
        return result != null ? R.ok().data("role",result) : R.error();
    }
}
