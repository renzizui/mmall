package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by Administrator on 2018/9/11.
 */
public interface IUserService {
    /**
     * 登陆
     * @param usename
     * @param password
     * @return
     */
    ServerResponse<User> login(String usename, String password);

    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 校验
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str,String type);

    /**
     * 找回密码
     * @param username
     * @return
     */
    ServerResponse selectQuestion(String username);

}
