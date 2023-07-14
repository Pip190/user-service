package com.chongdong.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    /**
     * 操作状态
     */
    private Boolean flag;
    /**
     * 提示消息
     */
    private String message;
    /**
     * 响应数据
     */
    private Map<String, Object> data = new HashMap<String, Object>();

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setFlag(true);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setFlag(false);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setFlag(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }
    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
