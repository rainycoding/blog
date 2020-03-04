package com.ktz.blog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by 曾泉明 on 2020/3/4 15:55
 */
public class FileUtils {

    public static String getFileName(String sourceName) {
        String targetName = null;
        String str = UUID.randomUUID().toString().replaceAll("-", "");
        targetName = str + sourceName;
        return targetName;
    }

    public static String uploadImg(MultipartFile file, String path, HttpServletRequest request) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空！");
        }
        String fileName = getFileName(file.getOriginalFilename());
        File targetFile = new File(path + fileName);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getServletContext() + "/img/" + fileName;
    }


    public static void main(String[] args) {
        String name = getFileName("hello");
        System.out.println(name);
    }
}
