package com.ktz.blog.dao;

import com.ktz.blog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/29 16:34
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public List<Comment> getAllByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
