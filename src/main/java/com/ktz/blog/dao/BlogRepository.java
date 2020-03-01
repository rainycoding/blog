package com.ktz.blog.dao;

import com.ktz.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/27 13:37
 */
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true")
    public List<Blog> findTop(Pageable pageable);

    @Modifying
    @Query("update Blog b set b.views = b.views + 1 where b.id = ?1")
    public int updateViews(Long id);

    public Page<Blog> findBlogsByCategoryId(Long categoryId, Pageable pageable);

    @Query("select function('date_format', b.updateTime, '%Y') as year from Blog b group by function('date_format', b.updateTime, '%Y') order by year desc")
    public List<String> findYears();

    @Query("select b from  Blog b where function('date_format', b.updateTime, '%Y') = ?1")
    public List<Blog> findBlogsByYear(String year);

//    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
//    public Page<Blog> findByQuery(String query,Pageable pageable);
}
