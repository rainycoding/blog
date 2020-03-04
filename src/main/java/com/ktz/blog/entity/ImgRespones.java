package com.ktz.blog.entity;

/**
 * Created by 曾泉明 on 2020/3/3 15:24
 */
public class ImgRespones {
    private Integer success;
    private String message;
    private String url;

    public ImgRespones() {
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImgRespones{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
