package com.ktz.blog.service;

import com.ktz.blog.entity.Category;
import com.ktz.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/25 20:47
 */
public interface CategoryService {

    public int getNum();

    public Category saveCategory(Category category);

    public Category getCategory(Long id);

    public Category getCategoryByName(String name);

    public List<Category> listAllCategory();

    public List<Category> listCategoryTop(Integer size);

    public Page<Category> listCategories(String name, Pageable pageable);

    public Category updateCateGory(Long id, Category category);

    public void removeCategory(Long id);
}
