package com.ktz.blog.dao;

import com.ktz.blog.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/25 20:47
 */
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    public Category getByName(String name);

    @Query("select c from Category  c")
    public List<Category> findTop(Pageable pageable);
}
