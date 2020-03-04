package com.ktz.blog.service.impl;

import com.ktz.blog.entity.ImgRespones;
import com.ktz.blog.service.FileService;
import com.ktz.blog.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by 曾泉明 on 2020/3/4 16:34
 */
@Service
public class FileServiceImpl implements FileService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${file.savedPath}")
    private String savedPath;

    @Value("${file.urlPath}")
    private String urlPath;

    @Override
    public ImgRespones upload(MultipartFile file) {
        ImgRespones respones = new ImgRespones();
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空！");
        }
        String fileName = FileUtils.getFileName(file.getOriginalFilename());
        File dest = new File(savedPath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        String url = urlPath + fileName;
        try {
            file.transferTo(dest);
            respones.setSuccess(1);
            respones.setMessage("文件上传成功！");
            respones.setUrl(url);
        } catch (IOException e) {
            LOGGER.error("Exception : {}", e);
        }
        return respones;
    }
}
