package com.czxy.controller;


import com.czxy.pojo.User;
import com.czxy.service.UserService;
import com.czxy.vo.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 接口方式 POST
     * 接口请求方法 regist
     *
     * 使用user实体接收页面数据
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<Object> regist(@RequestBody User user){

        //1 接收注册数据 保存到数据库
        userService.saveUser(user);
        //2 通过vo对象 返回注册信息
        BaseResult baseResult = new BaseResult(0,"注册成功");
        return ResponseEntity.ok(baseResult);
    }

}
