package com.ktz.blog.service.impl;

import com.ktz.blog.dao.TagRepository;
import com.ktz.blog.entity.Blog;
import com.ktz.blog.entity.Tag;
import com.ktz.blog.exception.NotFoundException;
import com.ktz.blog.service.TagService;
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
 * Created by 曾泉明 on 2020/2/26 17:32
 */
@Service
public class TagServiceImpl implements TagService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TagRepository tagRepository;

    @Override
    public int getNum() {
        return (int) tagRepository.count();
    }

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.getByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTags(String name, Pageable pageable) {
        return tagRepository.findAll(new Specification<Tag>() {
            @Override
            public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
    public List<Tag> listAllTag() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public List<Tag> listTagTop() {
        return null;
    }

    @Transactional
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listAllTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("编辑标签出错，不存在该标签");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void removeTag(Long id) {
        Tag t = tagRepository.getOne(id);
        List<Blog> blogs = t.getBlogs();
        for (Blog blog : blogs) {
            blog.getTags().remove(t);
        }
        tagRepository.deleteById(id);
    }

    /**
     * 将字符串根据逗号转换为集合
     * @param ids
     * @return
     */
    private List<Long> convertToList(String ids) {
        List<Long> idList = new ArrayList<Long>();
        if (!"".equals(ids) && ids != null) {
            String[] str = ids.split(",");
            for (int i = 0; i < str.length; i++) {
                idList.add(Long.parseLong(str[i]));
            }
        }
        return idList;
    }
}
