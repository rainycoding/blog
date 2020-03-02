package com.ktz.blog.service;

import com.ktz.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by 曾泉明 on 2020/2/27 13:34
 */
public interface BlogService {

    public int getNum();

    public Blog saveBlog(Blog blog);

    public Blog getBlog(Long id);

    public Blog getBlogAndConvert(Long id);

    public Page<Blog> listBlogs(Pageable pageable, Blog blog);

    public Page<Blog> listBlogs(Pageable pageable);

    public Page<Blog> listBlogs(String query, Pageable pageable);

    public Page<Blog> listBlogs(Long tagId, Pageable pageable);

    public Page<Blog> listBlogsByCategoryId(Long categoryId, Pageable pageable);

    public List<Blog> listRecommendBlog(Integer size);

    public Map<String, List<Blog>> archiveBlog();

    public Blog updateBlog(Long id, Blog blog);

    public int updateViews(Long id);

    public void removeBlog(Long id);
}
