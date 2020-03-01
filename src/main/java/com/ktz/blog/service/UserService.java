package com.ktz.blog.service;

import com.ktz.blog.entity.User;

/**
 * Created by 曾泉明 on 2020/2/25 16:13
 */
public interface UserService {

    public User checkUser(String username, String password);
}
