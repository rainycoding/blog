package com.ktz.blog.web.controller;

import com.ktz.blog.entity.Blog;
import com.ktz.blog.entity.Category;
import com.ktz.blog.entity.Comment;
import com.ktz.blog.entity.Tag;
import com.ktz.blog.service.BlogService;
import com.ktz.blog.service.CategoryService;
import com.ktz.blog.service.CommentService;
import com.ktz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by 曾泉明 on 2020/2/24 20:42
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                        Pageable pageable, Model model) {
        Page<Blog> page = blogService.listBlogs(pageable);
        List<Category> categories = categoryService.listCategoryTop(6);
        List<Tag> tags = tagService.listTagTop(10);
        List<Blog> blogs = blogService.listRecommendBlog(8);
        model.addAttribute("page", page);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        model.addAttribute("blogs", blogs);
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        Page<Blog> page = blogService.listBlogs(query, pageable);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable("id") Long id, Model model) {
        int i = blogService.updateViews(id);
        Blog blog = blogService.getBlogAndConvert(id);
        List<Comment> comments = commentService.listCommens(id);
        model.addAttribute("blog", blog);
        model.addAttribute("comments", comments);
        return "blog";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/category")
    public String categories(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             @RequestParam(defaultValue = "-1") Long id,  Model model) {
        Integer size = categoryService.getNum();
        List<Category> categories = categoryService.listCategoryTop(size);
        if (id == -1) {
            id = categories.get(0).getId();
        }
        Blog blog = new Blog();
        Category category = new Category();
        category.setId(id);
        blog.setCategory(category);
        Page<Blog> page = blogService.listBlogs(pageable, blog);
        model.addAttribute("categories", categories);
        model.addAttribute("page", page);
        model.addAttribute("activeCategoryId", id);
        return "category";
    }

    @GetMapping("/archives")
    public String archives(Model model) {
        int num = blogService.getNum();
        Map<String, List<Blog>> archiveMap = blogService.archiveBlog();
        model.addAttribute("num", num);
        model.addAttribute("archiveMap", archiveMap);
        return "archives";
    }

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(defaultValue = "-1") Long id, Model model) {
        Integer size = tagService.getNum();
        List<Tag> tags = tagService.listTagTop(size);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        Page<Blog> page = blogService.listBlogs(id, pageable);
        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
