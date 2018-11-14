package com.cr.mmall.service;

import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.pojo.User;

public interface IUserService {
    ServerResponse <User> login(String username, String password);

    ServerResponse register(User user);

    ServerResponse <String> checkValid(String str, String type);

    ServerResponse <String> selectQuestion(String username);

    ServerResponse <String> checkAnswer(String username, String question, String answer);

    ServerResponse <String> resetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse <String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse <User> updateInformation(User user);

    ServerResponse <User> getInformation(Integer userId);
}
