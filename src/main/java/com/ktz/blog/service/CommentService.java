package com.ktz.blog.service;

import com.ktz.blog.entity.Comment;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/29 16:28
 */
public interface CommentService {

    public List<Comment> listCommens(Long blogId);

    public Comment saveComment(Comment comment);
}
