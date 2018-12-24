package com.cr.mmall.service.impl;
// implement

import com.cr.mmall.common.Const;
import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.common.TokenCache;
import com.cr.mmall.mapper.UserMapper;
import com.cr.mmall.pojo.User;
import com.cr.mmall.service.IUserService;
import com.cr.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // 密码登录MD5,由于注册时密码已经被加密过，所以此处先加密再匹配数据库
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        // MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createByErrorMessage("注册成功");
    }

    /* 检验用户名和邮件的有效性 */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            // 开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("Email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        // 对于注册阶段，是希望没有重复的用户，显示校验成功，而这里则表明没有这个用户，所以要重新定义错误信息
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);  // 把question放到data返回
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /* 校验密保问题的答案是否正确 */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String passwordNew, String forgetToken) {
        // 检查传入的forgetToken是否为空白
        if (org.apache.commons.lang3.StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        // 检查用户名有效性
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        // 根据用户名查询本地缓存中的token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        // 比较本地的token和传入的forgetToken是否一致 更新密码
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 防止横向越权，校验用户旧密码的同时，一定要校验用户名，否则容易被人用密码字典撞库
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        // 如果旧密码与用户名匹配成功，则设置好新密码，更新到数据库
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        // username不更新，email更新时需要检查是否重复(除前用户的email外)
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");
        }
        // updateUser将除username之外的信息都更新了
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");

    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    /**
     * Backend
     */
    // 校验是不是管理员
    @Override
    public ServerResponse checkAdminRole(User user) {
        if ((user != null) || (user.getRole().intValue() == Const.Role.ROLE_ADMIN)) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
