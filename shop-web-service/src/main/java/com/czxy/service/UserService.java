package com.czxy.service;

import com.czxy.dao.UserMapper;
import com.czxy.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //用户注册成功
    public void saveUser(User user) {
        userMapper.insert(user);
    }

    //查询用户是否已经注册
    public User findUserByMobile(String mobile) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile",mobile);
        List<User> users = userMapper.selectByExample(example);

        //users 的长度是否大于0?大于获取0号位对象返回,不大于返回null
        return users.size()>0?users.get(0):null;
    }
}
