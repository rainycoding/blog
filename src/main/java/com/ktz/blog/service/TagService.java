package com.ktz.blog.service;

import com.ktz.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/26 17:29
 */
public interface TagService {

    public int getNum();

    public Tag saveTag(Tag tag);

    public Tag getTag(Long id);

    public Tag getTagByName(String name);

    public Page<Tag> listTags(String name, Pageable pageable);

    public List<Tag> listAllTag();

    public List<Tag> listTagTop();

    public List<Tag> listTagTop(Integer size);

    public List<Tag> listAllTag(String ids);

    public Tag updateTag(Long id, Tag tag);

    public void removeTag(Long id);
}
