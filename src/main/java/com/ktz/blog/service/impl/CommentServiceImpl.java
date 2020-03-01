package com.ktz.blog.service.impl;

import com.ktz.blog.dao.CommentRepository;
import com.ktz.blog.entity.Comment;
import com.ktz.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/29 16:30
 */
@Service
public class CommentServiceImpl implements CommentService {
    //存放迭代找出所有子代的集合
    private List<Comment> tempReplys = new ArrayList<Comment>();

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    @Override
    public List<Comment> listCommens(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.getAllByBlogIdAndParentCommentNull(blogId, sort);
        return eahComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶端的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eahComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<Comment>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replyComments = comment.getReplyComments();
            for (Comment replyComment : replyComments) {
                recursively(replyComment);
            }
            //修改定级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<Comment>();
        }
    }

    /**
     * 递归迭代
     * @param comment 被迭代额对象
     */
    private void recursively(Comment comment) {
        //顶节点添加到临时存放集合
        tempReplys.add(comment);
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replyComments = comment.getReplyComments();
            for (Comment replyComment : replyComments) {
                tempReplys.add(replyComment);
                if (replyComment.getReplyComments().size() > 0)
                    recursively(replyComment);
            }
        }
    }
}
