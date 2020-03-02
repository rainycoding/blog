package com.ktz.blog.service.impl;

import com.ktz.blog.dao.CategoryRepository;
import com.ktz.blog.entity.Blog;
import com.ktz.blog.entity.Category;
import com.ktz.blog.exception.NotFoundException;
import com.ktz.blog.service.CategoryService;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/25 20:48
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public int getNum() {
        return (int) categoryRepository.count();
    }

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Transactional
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.getByName(name);
    }

    @Transactional
    @Override
    public List<Category> listAllCategory() {
        return categoryRepository.findAll();
    }

    @Transactional
    @Override
    public List<Category> listCategoryTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        List<Category> categoryTop = categoryRepository.findTop(pageable);
        List<Blog> temp = null;
        for (int i = 0; i < categoryTop.size(); i++) {
            temp = new ArrayList<Blog>();
            List<Blog> blogs = categoryTop.get(i).getBlogs();
            temp.addAll(blogs);
            for (Blog blog : blogs) {
                if (!blog.isPublished()) {
                    temp.remove(blog);
                }
            }
            categoryTop.get(i).setBlogs(temp);
        }
        return categoryTop;
    }

    @Transactional
    @Override
    public Page<Category> listCategories(String name, Pageable pageable) {
        return categoryRepository.findAll(new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (name != null && !"".equals(name)) {
                    predicate = criteriaBuilder.like(root.<String>get("name"), "%" + name + "%");
                }
                return predicate;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Category updateCateGory(Long id, Category category) {
        Category c = categoryRepository.getOne(id);
        if (c == null) {
            throw new NotFoundException("编辑分类出错，不存在该分类！");
        }
        BeanUtils.copyProperties(category, c);
        return categoryRepository.save(c);
    }

    @Transactional
    @Override
    public void removeCategory(Long id) {
        Category c = categoryRepository.getOne(id);
        List<Blog> blogs = c.getBlogs();
        for (Blog blog : blogs) {
            blog.setCategory(null);
        }
        categoryRepository.deleteById(id);
    }
}
