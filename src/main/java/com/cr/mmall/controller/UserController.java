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
    @ResponseBody  // @ResponseBody注解实现将Controller类方法返回对象转换为json响应给客户端
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

    // 获取用户登录信息
    @RequestMapping(value = "/get_user_info", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse <User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
           return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    // 忘记密码 找回密保问题
    @RequestMapping(value = "/forget_get_question", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse <String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    // 忘记密码 校验密保问题答案是否正确
    @RequestMapping(value = "/forget_check_answer", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse <String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    // 忘记密码 重设密码
    @RequestMapping(value = "/forget_reset_password", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse <String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.resetPassword(username, passwordNew, forgetToken);
    }

}
