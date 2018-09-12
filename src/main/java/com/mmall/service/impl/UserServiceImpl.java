package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2018/9/11.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);
        if(resultCount==0){
            return ServerResponse.createByErrorMessages("用户名不存在");
        }
        //todo  密码登陆MD5
        String md5password=MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLogin(username, md5password);
        if(user==null){
            return ServerResponse.createByErrorMessages("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse=this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        int resultCount=userMapper.checkUsername(user.getUsername());
        if(resultCount>0){
            return ServerResponse.createByErrorMessages("用户名已存在");
        }
        int emailCount=userMapper.checkEmail(user.getEmail());
        if(emailCount>0){
            return ServerResponse.createByErrorMessages("邮件已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.createByErrorMessages("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 校验
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessages("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int emailCount=userMapper.checkEmail(str);
                if(emailCount>0){
                    return ServerResponse.createByErrorMessages("邮件已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessages("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 找回密码
     * @param username
     * @return
     */
    public ServerResponse selectQuestion(
            String username){
            ServerResponse validResponse=this.checkValid(username,Const.USERNAME);
           if(validResponse.isSuccess()){
               return ServerResponse.createByErrorMessages("用户不存在");
           }
        String question=userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessages("找回密码的问题是空的");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount=userMapper.checkAnswer(username, question, answer);
        if(resultCount>0){
            String forgetToken= UUID.randomUUID().toString();
            //有效期
        }
        return null;
    }


}
