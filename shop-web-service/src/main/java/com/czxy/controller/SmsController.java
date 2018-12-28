package com.czxy.controller;

import com.aliyuncs.exceptions.ClientException;
import com.czxy.common.GetRandomCodeUtil;
import com.czxy.common.SmsUtil;
import com.czxy.pojo.User;
import com.czxy.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
public class SmsController {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 接口
     * POST /web-service/sms
     * 接收 手机号码
     * @return
     */
    @PostMapping("/sms")
    public ResponseEntity<Object> sms(@RequestBody User user) {

        //1 获取手机号,并判断手机号是否为空
        String mobile = user.getMobile();
        if(!StringUtils.isBlank(mobile)){
            //1.3通过手机号查询redis中是否已经存在验证码
            String RedisCode = redisTemplate.opsForValue().get(mobile);

            if(!StringUtils.isBlank(RedisCode)){
                BaseResult baseResult = new BaseResult(1, "短信已经发送,请在30分钟内使用");
                return ResponseEntity.ok(baseResult);
            }

            //2 调用工具类生成验证码
            String code = GetRandomCodeUtil.randomCode();
            //3 调用工具类发送短信
            try {
                SmsUtil.sendSms(mobile,code);
            } catch (ClientException e) {
                e.printStackTrace();
            }
            //4 调用redis存储手机号 验证码,设置存活时间 1小时
            redisTemplate.opsForValue().set(mobile,code,1, TimeUnit.HOURS);
            BaseResult baseResult = new BaseResult(1, "验证码已发送");
            return ResponseEntity.ok(baseResult);
        }
        //1.2手机号存在问题,返回提示
        BaseResult baseResult = new BaseResult(1, "手机号码输入错误");
        return ResponseEntity.ok(baseResult);
    }
}
