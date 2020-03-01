package com.ktz.blog.dao;

import com.ktz.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 曾泉明 on 2020/2/25 16:11
 */
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsernameAndPassword(String username, String password);
}
