package com.chongdong.security;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultPasswordEncoder implements PasswordEncoder {
    public DefaultPasswordEncoder(){
        this(-1);
    }
    public DefaultPasswordEncoder(int strength){

    }
    // 进行MD5加密
    @Override
    public String encode(CharSequence rawPassword) {
        // 加密
        return DigestUtil.md5Hex(rawPassword.toString());
    }
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(DigestUtil.md5Hex(rawPassword.toString()));
    }
}
