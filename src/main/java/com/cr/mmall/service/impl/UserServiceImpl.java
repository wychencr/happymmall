package com.cr.mmall.service.impl;

import com.cr.mmall.pojo.User;
import com.cr.mmall.service.UserService;
import com.cr.mmall.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
// @Service用于业务层 功能等同于@component
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User get(User user) {
        // 通过Mapper的select方法查询用户
        return userMapper.select(user);
    }
}
