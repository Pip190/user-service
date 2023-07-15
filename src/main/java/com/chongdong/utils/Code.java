package com.chongdong.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 验证码
 * Author: wo
 * Created: 2023/7/13 17:36
 * Modified By: wo
 * Last Modified: 2023/7/13 17:36
 */
@Data
@AllArgsConstructor
public class Code {
    private static String verificationCode;

    public static String getVerificationCode() {
        return verificationCode;
    }

    public static R getR(String phoneNumber) {
        verificationCode = RandomUtil.randomNumbers(4);
        // 设置短信服务商API URL
        /*String apiUrl = "https://api.sms.com/send";
        // 设置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("apiKey", "your_api_key");
        params.put("phoneNumber", phoneNumber);
        params.put("message", "Your verification code is: " + verificationCode);

        // 发送HTTP POST请求
        String result = HttpUtil.post(apiUrl, params);

        JSONObject jsonResult = JSONUtil.parseObj(result);
        int status = jsonResult.getInt("status");

        // 根据返回结果进行处理
        if (status == 200) {
            // 发送成功
            return R.ok().message("验证码已发送").data("验证码：",verificationCode);
        } else {
            // 发送失败
            return R.error().message("电话号码错误，验证码发送失败");
        }*/
        return R.ok().message(verificationCode);
    }
}
