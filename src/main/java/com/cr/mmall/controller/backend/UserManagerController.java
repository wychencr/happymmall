package com.cr.mmall.controller.backend;


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
@RequestMapping(value = "/manage/user")
public class UserManagerController {
    @Resource
    private IUserService iUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse <User> login(String username, String password, HttpSession session) {
        ServerResponse <User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // 说明是管理员
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }
        }
        return response;
    }
}
