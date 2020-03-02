package com.ktz.blog.web.controller.admin;

import com.ktz.blog.entity.Blog;
import com.ktz.blog.entity.Category;
import com.ktz.blog.entity.Tag;
import com.ktz.blog.entity.User;
import com.ktz.blog.service.BlogService;
import com.ktz.blog.service.CategoryService;
import com.ktz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 曾泉明 on 2020/2/26 22:04
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public String listBlogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                            Pageable pageable, Blog blog, Model model) {
        Page<Blog> page = blogService.listBlogs(pageable, blog);
        List<Category> categories = categoryService.listAllCategory();
        model.addAttribute("page", page);
        model.addAttribute("categories", categories);
        return "admin-ui/blog";
    }

    @PostMapping("/blogs")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Blog blog, Model model) {
        Page<Blog> page = blogService.listBlogs(pageable, blog);
        List<Category> categories = categoryService.listAllCategory();
        model.addAttribute("page", page);
        model.addAttribute("categories", categories);
        return "admin-ui/blog :: blogList";
    }

    @GetMapping("/blog/release")
    public String release(Model model) {
        List<Tag> tags = tagService.listAllTag();
        List<Category> categories = categoryService.listAllCategory();
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("blog", new Blog());
        return "admin-ui/blog-release";
    }

    @GetMapping("/blog/{id}/release")
    public String edit(@PathVariable("id") Long id, Model model) {
        List<Tag> tags = tagService.listAllTag();
        List<Category> categories = categoryService.listAllCategory();
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("blog", blog);
        return "admin-ui/blog-release";
    }

    @PostMapping("/blog")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setCategory(categoryService.getCategory(blog.getCategory().getId()));
        blog.setTags(tagService.listAllTag(blog.getTagIds()));
        Blog b = blogService.saveBlog(blog);
        if (b != null) {
            attributes.addFlashAttribute("message", "操作成功！");
        }else {
            attributes.addFlashAttribute("message", "操作失败！");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blog/{id}/remove")
    public String remove(@PathVariable("id")Long id, RedirectAttributes attributes) {
        blogService.removeBlog(id);
        attributes.addFlashAttribute("message", "博客删除成功！");
        return "redirect:/admin/blogs";
    }
}
