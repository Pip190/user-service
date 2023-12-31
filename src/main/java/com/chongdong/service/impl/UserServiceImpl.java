package com.chongdong.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chongdong.model.User;
import com.chongdong.service.UserService;
import com.chongdong.mapper.UserMapper;
import com.chongdong.utils.Code;
import com.chongdong.utils.R;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author wo
* @description 针对表【tcd_user(ç”¨æˆ·è¡¨)】的数据库操作Service实现
* @createDate 2023-07-05 11:00:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }

    @Override
    public R updatePasswordByLogonUser(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userServiceOne = this.getOne(new QueryWrapper<User>().eq("username", username));
        boolean modifySuccessfully = false;
        if(DigestUtil.md5Hex(oldPassword).equals(userServiceOne.getPassword())){
            userServiceOne.setPassword(DigestUtil.md5Hex(newPassword));
            modifySuccessfully = this.saveOrUpdate(userServiceOne);
            redisTemplate.delete(userServiceOne.getUsername());
        }
        return modifySuccessfully ? R.ok() : R.error();
    }

    @Override
    public R updatePasswordByVerification(String userInputCode, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByLogonUser = this.getOne(new QueryWrapper<User>().eq("username", username));
        boolean update = false;
        if (userInputCode.equals(Code.getVerificationCode())) {
            // 验证码正确
            userByLogonUser.setPassword(DigestUtil.md5Hex(newPassword));
            update = this.updateById(userByLogonUser);
            redisTemplate.delete(userByLogonUser.getUsername());
        }
        return update ? R.ok() : R.error();
    }

    @Override
    public R listUserByUsernameOrNickname(Long pageNum,Long pageSize,User userQueryVo) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userQueryVo.getUsername())){
            queryWrapper.like("username",userQueryVo.getUsername());
        }
        if (!StringUtils.isEmpty(userQueryVo.getNickName())){
            queryWrapper.like("nick_name",userQueryVo.getNickName());
        }
        Page<User> result = this.page(page, queryWrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("user",result);
        return result!=null ? R.ok().data(data) : R.error();
    }

    @Override
    public R deleteUserById(Long id) {
        User byId = this.getById(id);
        byId.setIsDeleted(1);
        byId.setUpdateTime(new Date());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        boolean update = this.update(byId, queryWrapper);
        return update ? R.ok() : R.error();
    }
}




