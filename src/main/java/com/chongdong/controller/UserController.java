package com.chongdong.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.chongdong.model.User;
import com.chongdong.service.RoleService;
import com.chongdong.service.UserService;
import com.chongdong.utils.Code;
import com.chongdong.utils.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    private RoleService roleService;

    /**
     * 添加用户
     * @param user 不需要用户id，传主要用户信息添加
     */
    @PreAuthorize("@ss.hasPermission('user.add')")
    @PostMapping
    public R add(@RequestBody User user) {
        user.setCreateTime(new Date());
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        boolean save = userService.save(user);
        return save ? R.ok() : R.error();
    }

    /**
     * 删除用户
     * @param id 根据Id来删除用户信息
     */
    @DeleteMapping("{id}")
    public R delete(@PathVariable("id") Long id) {
        boolean removeById = userService.removeById(id);
        return removeById ? R.ok() : R.error();
    }

    /**
     * 更新用户
     * @param user 用户信息，根据Id来更新信息
     */
    @PutMapping
    public R update(@RequestBody User user) {
        user.setUpdateTime(new Date());
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        boolean updateById = userService.updateById(user);
        return updateById ? R.ok() : R.error();
    }

    /**
     * 查询用户
     * @param id 用户主键id
     * @return 匹配该id的用户信息
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return user!=null ? R.ok().data("user",user) : R.error();
    }

    /**
     * 分页查询所有用户
     * @param pageNum   第几页 不传默认第1页
     * @param pageSize  每页显示条数  不传默认为每页4条数据
     * @return 分页查询所有用户信息
     */
    @PreAuthorize("@ss.hasPermission('user.list')")
    @GetMapping()
    public R findAll(@RequestParam(defaultValue = "1") long pageNum,
                     @RequestParam(defaultValue = "4") long pageSize,
                     User userQueryVo) {
        return userService.listUserByUsernameOrNickname(pageNum,pageSize,userQueryVo);
    }

    @GetMapping("/toAssign/{userId}")
    public R toAssign(@PathVariable Long userId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return R.ok().data(roleMap);
    }

    /*
    比如这个路径它不做拦截处理麽？
    你这个是干啥用的接口嘛， 就是授权，给某个用户id1 ,比如说，这个接口得权限字符是：do_assign
    那这个接口就得判断当前用户是否存在do_assign得这个权限！而不是判断它是不是又这个路径，在看一个东西，

    你刚说的，再说一遍
    比如下面是get请求doAssign,
    用户登录后，直接
    http:localhost:8088/doAssign
    不就直接走这个啦？
    不会啊，你在过滤器做了判断了啊，如果：当前用户不存在do_assign得这个权限，那就直接返回错误了，根本都还没到这来。
    chain.dofilter(),这个方法就是放行的！没有这句话，所有的请求都不会直接到controller里面来！那大概清楚了。
     */
    @PostMapping("/doAssign")
    public R doAssign(@RequestParam Long userId, @RequestParam Long[] roleId) {
        roleService.saveUserRoleRelationShip(userId,roleId);
        return R.ok();
    }

    /**
     * 修改当前登录用户密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 成功/原密码错误
     */
    @PostMapping("/changePassword")
    public R changePassword(@RequestParam String oldPassword, @RequestParam String newPassword){
        return userService.updatePasswordByLogonUser(oldPassword, newPassword);
    }

    /**
     * 调用第三方API接口发送验证码，用户获取验证码
     * @return 第三方是否发送验证码
     */
    @GetMapping("/verification")
    public R verification(String phoneNumber){
        return Code.getR(phoneNumber);
    }

    @PostMapping("/verification")
    public R verification(@RequestParam String userInputCode , @RequestParam String newPassword){
        return userService.updatePasswordByVerification(userInputCode,newPassword);
    }
}
