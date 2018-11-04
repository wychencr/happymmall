package com.cr.mmall.controller;

import com.cr.mmall.pojo.User;
import com.cr.mmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
// spring-mybatis.xml和springmvc-servlet.xml都配置了扫描控制层
@Controller
public class UserController {
    // 注入UserService
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login")
    public String login(User user) {
        ModelAndView mv = new ModelAndView();
        System.out.println("开始查询---");
        user = userService.get(user);

        if (user != null) {
            System.out.println("查到的User: " + user.getUsername());
            mv.addObject("user", user);
            // 转到user.jsp用户界面
            return "user";
        } else {
            System.out.println("未查到此用户");
            // 查不到用户信息，则重定向回登录界面
            System.out.println("重定向回登录界面---");
            return "login";
        }
    }
}
