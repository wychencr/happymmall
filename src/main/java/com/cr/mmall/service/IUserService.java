package com.cr.mmall.service;

import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.pojo.User;

public interface IUserService {
    ServerResponse <User> login(String username, String password);

    ServerResponse register(User user);

    ServerResponse <String> checkValid(String str, String type);
}
