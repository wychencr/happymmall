package com.cr.mmall.controller;

import com.cr.mmall.common.Const;
import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.pojo.User;
import com.cr.mmall.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private IUserService iUserService;

    // 登录功能
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        /**
         * 功能描述:
         * @param: [username, password, session]
         * @return: java.lang.Object
         * @auther: Wychencr
         * @date: 2018/11/5 15:28
         */
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    // 登出功能
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    // 注册功能
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse <String> register(User user) {
        return iUserService.register(user);
    }


    // 检验用户名和邮件的有效性
    @RequestMapping(value = "/check_valid", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse <String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }
}
