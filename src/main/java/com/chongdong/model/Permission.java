package com.chongdong.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 权限管理
 * @TableName tcd_permission
 */
@TableName(value ="tcd_permission")
@Data
public class Permission implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属上级
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 权限名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类型（1：菜单，2：按钮）
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 权限值
     */
    @TableField(value = "permission_value")
    private String permissionValue;

    /**
     * 访问路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @TableField(value = "component")
    private String component;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 状态（0：禁止，1：正常）
     */
    @TableField(value = "status")
    private Integer status;
    @TableField(exist = false)
    private Integer level;
    @TableField(exist = false)
    private List<Permission> children;
    /**
     * 逻辑选择 1（true）可选择， 0（false）不可选
     */
    @TableField(exist = false)
    private boolean isSelect;

    /**
     * 逻辑删除（1（true）已删除，0（false）未删除）
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}