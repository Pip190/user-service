package com.chongdong.service;

import com.chongdong.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chongdong.utils.R;

/**
* @author wo
* @description 针对表【tcd_user(ç”¨æˆ·è¡¨)】的数据库操作Service
* @createDate 2023-07-05 11:00:59
*/
public interface UserService extends IService<User> {
    // 从数据库中取出用户数据
    User selectByUsername(String username);

    /**
     * 修改当前用户密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改成功/失败
     */
    R updatePasswordByLogonUser(String oldPassword, String newPassword);

    R updatePasswordByVerification(String userInputCode, String newPassword);

    R listUserByUsernameOrNickname(Long pageNum,Long pageSize,User userQueryVo);
}
