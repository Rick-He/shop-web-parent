package com.czxy.controller;


import com.czxy.pojo.User;
import com.czxy.service.UserService;
import com.czxy.vo.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 接口方式 POST
     * 接口请求方法 regist
     *
     * 使用user实体接收页面数据
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<Object> regist(@RequestBody User user){
        //需求 用户输入手机号，发送短信验证码，发送短信验证码的时候，需要确认手机号是否已经注册，如果注册过，直接提醒，已经注册，直接登录.
        //当用户提交信息的时候，需要判断短信验证码是否正确，如果正确，则允许注册，如果不正确，则不允许注册

        //1 接收到注册数据 用户名 密码 手机号 验证码,判断手机是否已经注册,注册过返回提示信息
        String code = redisTemplate.opsForValue().get(user.getMobile());
        //1.1 调用service 查询数据库 手机号是否存在
        User exist = userService.findUserByMobile(user.getMobile());


        //2 判断exist返回用户,表示该手机号已注册,返回提示信息
        if(exist!=null){
            BaseResult baseResult = new BaseResult(1, "该手机号已经注册,请直接登录");
            return ResponseEntity.ok(baseResult);
        }

        //3 判断验证码是否正确,并注册用户
        if(user.getCode().equals(code)){
            //3.1  验证码使用完后删除
            redisTemplate.delete(user.getMobile());
            userService.saveUser(user);
            
            BaseResult baseResult = new BaseResult(0, "注册成功");
            return ResponseEntity.ok(baseResult);
        }

        BaseResult baseResult = new BaseResult(1, "验证码输入错误");
        return ResponseEntity.ok(baseResult);
    }
}
