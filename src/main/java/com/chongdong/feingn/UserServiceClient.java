/*
package com.chongdong.feingn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(value = "admin-service")
public interface UserServiceClient {
    @GetMapping(value = "/User/{id}")
    Map<String, Object> findById(@PathVariable("id") Long id);

    @PostMapping(value = "login")
    Map<String, Object> toLogin(String username,String password);
}
*/
