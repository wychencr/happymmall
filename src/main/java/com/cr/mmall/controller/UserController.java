package com.cr.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password, HttpSession session) {
        /**
         * 功能描述:
         * @param: [username, password, session]
         * @return: java.lang.Object
         * @auther: Wychencr
         * @date: 2018/11/5 15:28
         */
        return null;
    }

}
