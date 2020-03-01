package com.ktz.blog.web.controller;

import com.ktz.blog.entity.Blog;
import com.ktz.blog.entity.Comment;
import com.ktz.blog.entity.User;
import com.ktz.blog.service.BlogService;
import com.ktz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/29 15:53
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comment(@PathVariable("blogId") Long blogId, Model model) {
        List<Comment> comments = commentService.listCommens(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdmin(true);
        }else {
            comment.setAvatar(avatar);
        }
        Comment savedComment = commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
