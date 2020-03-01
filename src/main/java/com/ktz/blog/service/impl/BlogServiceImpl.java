package com.ktz.blog.service.impl;

import com.ktz.blog.dao.BlogRepository;
import com.ktz.blog.entity.Blog;
import com.ktz.blog.entity.Category;
import com.ktz.blog.entity.Tag;
import com.ktz.blog.exception.NotFoundException;
import com.ktz.blog.service.BlogService;
import com.ktz.blog.utils.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 曾泉明 on 2020/2/27 13:38
 */
@Service
public class BlogServiceImpl implements BlogService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public int getNum() {
        return (int) blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(LocalDateTime.now());
            blog.setUpdateTime(LocalDateTime.now());
            blog.setViews(0);
            return blogRepository.save(blog);
        }else {
            Blog b = blogRepository.getOne(blog.getId());
            try {
                com.ktz.blog.utils.BeanUtils.copyPropertiesIgnoreNull(blog, b);
            } catch (IllegalAccessException e) {
                LOGGER.error("Exception : {}", e);
            }
            b.setUpdateTime(LocalDateTime.now());
            return blogRepository.save(b);
        }
    }

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getBlogAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String tempContent = MarkdownUtils.markdownToHtmlExtensions(b.getContent());
        b.setContent(tempContent);
        return b;
    }

    @Transactional
    @Override
    public Page<Blog> listBlogs(Pageable pageable, Blog blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (blog.getTitle() != null && !"".equals(blog.getTitle())) {
                    list.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getCategory() != null && blog.getCategory().getId() != null) {
                    list.add(criteriaBuilder.equal(root.<Category>get("category").get("id"), blog.getCategory().getId()));
                }
                if (blog.isRecommend()) {
                    list.add(criteriaBuilder.equal(root.get("recommend"), blog.isRecommend()));
                }
                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Page<Blog> listBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Page<Blog> listBlogs(String query, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                Predicate predicate = null;
                if (query != null && !"".equals(query)) {
                    list.add(criteriaBuilder.like(root.get("title"), "%" + query + "%"));
                    list.add(criteriaBuilder.like(root.get("content"), "%" + query + "%"));
                    predicate = criteriaQuery.where(criteriaBuilder.or(list.toArray(new Predicate[list.size()]))).getRestriction();
                }
                return predicate;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Page<Blog> listBlogs(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Transactional
    @Override
    public List<Blog> listRecommendBlog(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findYears();
        Map<String, List<Blog>> map = new HashMap<String, List<Blog>>();
        for (String year : years) {
            map.put(year, blogRepository.findBlogsByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new NotFoundException("编辑博客出错，不存在该博客！");
        }
        BeanUtils.copyProperties(blog, b);
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public int updateViews(Long id) {
        return blogRepository.updateViews(id);
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        Blog b = blogRepository.getOne(id);
        List<Tag> tags = b.getTags();
        for (Tag tag : tags) {
            tag.getBlogs().remove(b);
        }
        blogRepository.deleteById(id);
    }
}
