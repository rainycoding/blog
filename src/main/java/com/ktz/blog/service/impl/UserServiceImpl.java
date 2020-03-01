package com.ktz.blog.service.impl;

import com.ktz.blog.dao.UserRepository;
import com.ktz.blog.entity.User;
import com.ktz.blog.service.UserService;
import com.ktz.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 曾泉明 on 2020/2/25 16:14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public User checkUser(String username, String password) {
        password = MD5Utils.code(password);
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
