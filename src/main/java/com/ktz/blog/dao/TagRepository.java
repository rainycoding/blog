package com.ktz.blog.dao;

import com.ktz.blog.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/26 17:28
 */
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    public Tag getByName(String name);

    @Query("select t from Tag t")
    public List<Tag> findTop(Pageable pageable);

}
