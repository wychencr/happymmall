package com.cr.mmall.service;

import com.cr.mmall.pojo.User;

public interface UserService {
    /**
     * 根据user信息检查数据库中是否存在该用户
     */
    User get(User user);
}
