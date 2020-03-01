package com.ktz.blog.web.controller.admin;

import com.ktz.blog.entity.Tag;
import com.ktz.blog.exception.NotFoundException;
import com.ktz.blog.service.TagService;
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
 * Created by 曾泉明 on 2020/2/26 17:27
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TagService tagService;

    @PostMapping("/tag")
    public String saveTag(Tag tag, RedirectAttributes attributes) {
        if (tagService.getTagByName(tag.getName()) != null) {
            attributes.addFlashAttribute("message", "新增标签失败，不能新增重复标签！");
        }else {
            Tag savedTag = tagService.saveTag(tag);
            attributes.addFlashAttribute("message", "新增标签成功！");
        }
        return "redirect:/admin/tags";
    }

    @ResponseBody
    @GetMapping("/tag/{id}")
    public Tag listTag(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }

    @GetMapping("/tags")
    public String listTags(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                           Pageable pageable, String tagName, Model model) {
        Page<Tag> page = tagService.listTags(tagName, pageable);
        model.addAttribute("page", page);
        return "admin-ui/tags";
    }

    @PostMapping("/tags")
    public String search(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, String tagName, Model model) {
        Page<Tag> page = tagService.listTags(tagName, pageable);
        model.addAttribute("page", page);
        return "admin-ui/tags :: tagList";
    }

    @PutMapping("/tag")
    public String updateTag(Tag tag, RedirectAttributes attributes) {
        if (tagService.getTagByName(tag.getName()) != null) {
            attributes.addFlashAttribute("message", "编辑标签失败，不能出现重复标签！");
        }else {
            Tag updatedTag = null;
            try {
                updatedTag = tagService.updateTag(tag.getId(), tag);
                attributes.addFlashAttribute("message", "编辑标签成功！");
            } catch (NotFoundException e) {
                LOGGER.error("Exception : {}", e);
                attributes.addFlashAttribute("message", "不存在该标签！");
            }
        }
        return "redirect:/admin/tags";
    }

    @DeleteMapping("/tag/{id}")
    public String removeTag(@PathVariable("id") Long id) {
        tagService.removeTag(id);
        return "redirect:/admin/tags";
    }
}
