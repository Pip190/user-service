package com.chongdong.mapper;

import com.chongdong.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author wo
* @description 针对表【tcd_role(è§’è‰²è¡¨)】的数据库操作Mapper
* @createDate 2023-07-05 11:00:59
* @Entity com.chongdong.model.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select role_id from tcd_user_role where user_id = #{userId}")
    Long[] selectRoleIdByUserId(Long userId);
}




