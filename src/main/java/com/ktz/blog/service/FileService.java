package com.ktz.blog.service;

import com.ktz.blog.entity.ImgRespones;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 曾泉明 on 2020/3/4 16:33
 */
public interface FileService {
    public ImgRespones upload(MultipartFile file);
}
