package com.cr.service.impl;

import com.cr.mapper.UserMapper;
import com.cr.pojo.User;
import com.cr.service.UserService;
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
