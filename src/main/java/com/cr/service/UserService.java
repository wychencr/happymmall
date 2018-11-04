package com.cr.service;

import com.cr.pojo.User;

public interface UserService {
    /**
     * 根据user信息检查数据库中是否存在该用户
     */
    User get(User user);
}
