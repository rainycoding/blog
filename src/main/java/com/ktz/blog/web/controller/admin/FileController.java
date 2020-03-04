package com.ktz.blog.web.controller.admin;

import com.ktz.blog.entity.ImgRespones;
import com.ktz.blog.service.FileService;
import com.ktz.blog.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 曾泉明 on 2020/3/3 15:23
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/upload")
    public ImgRespones uploadImg(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file) {
        return fileService.upload(file);
    }
}
