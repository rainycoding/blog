package com.ktz.blog.web.controller.admin;

import com.ktz.blog.entity.Category;
import com.ktz.blog.entity.User;
import com.ktz.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 曾泉明
 * @date 2020/2/24 16:12
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private String prefix = "admin-ui/";

    @Autowired
    private UserService userService;

    /* 处理页面跳转请求 */
    @GetMapping
    public String loginPage() {
        return prefix + "login";
    }

    @GetMapping("/index")
    public String index() {
        return prefix + "index";
    }

    /* 处理其他请求 */
    /**
     * 登录请求
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "redirect:index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误！");
            return "redirect:/admin";
        }
    }

    /**
     * 管理员注销
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
