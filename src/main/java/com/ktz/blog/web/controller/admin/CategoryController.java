package com.ktz.blog.web.controller.admin;

import com.ktz.blog.entity.Category;
import com.ktz.blog.exception.NotFoundException;
import com.ktz.blog.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by 曾泉明 on 2020/2/25 20:55
 */
@Controller
@RequestMapping("/admin")
public class CategoryController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    public String saveCategory(Category category, RedirectAttributes attributes) {
        if (categoryService.getCategoryByName(category.getName()) != null) {
            attributes.addFlashAttribute("message", "新增分类失败，不能新增重复的分类！");
        }else {
            Category savedCategory = categoryService.saveCategory(category);
            attributes.addFlashAttribute("message", "新增分类成功！");
        }
        return "redirect:/admin/categories";
    }

    @ResponseBody
    @GetMapping("/category/{id}")
    public Category listCategory(@PathVariable("id") Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/categories")
    public String listCategories(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                         Pageable pageable, String categoryName, Model model) {
        Page<Category> page = categoryService.listCategories(categoryName, pageable);
        model.addAttribute("page", page);
        return "admin-ui/category";
    }

    @PostMapping("/categories")
    public String search(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                         Pageable pageable, String categoryName, Model model) {
        Page<Category> page = categoryService.listCategories(categoryName, pageable);
        model.addAttribute("page", page);
        return "admin-ui/category :: categoryList";
    }

    @PutMapping("/category")
    public String updateCategory(Category category, RedirectAttributes attributes) {
        if (categoryService.getCategoryByName(category.getName()) != null) {
            attributes.addFlashAttribute("message", "编辑分类失败，不能出现重复分类！");
        }else {
            Category updatedCateGory = null;
            try {
                updatedCateGory = categoryService.updateCateGory(category.getId(), category);
                attributes.addFlashAttribute("message", "编辑分类成功！");
            } catch (NotFoundException e) {
                LOGGER.error("Exception : {}", e);
                attributes.addFlashAttribute("message", "不存在该分类！");
            }
        }
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/category/{id}")
    public String removeCategory(@PathVariable("id") Long id) {
        categoryService.removeCategory(id);
        return "redirect:/admin/categories";
    }
}
