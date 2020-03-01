package com.ktz.blog.utils;

import org.thymeleaf.expression.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by 曾泉明 on 2020/2/28 14:17
 */
public class BeanUtils {

    public static void copyPropertiesIgnoreNull(Object source, Object target, String... ignore) throws IllegalAccessException {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        List<String> ignorePropertiesList = new ArrayList<String>();
        if (ignore != null) {
            ignorePropertiesList.addAll(Arrays.asList(ignore));
        }
        Field[] fields = source.getClass().getDeclaredFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            //判断source的属性是否为privite
            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }
            Object fieldValue = field.get(source);
            if (fieldValue == null) {
                ignorePropertiesList.add(field.getName());
            }
            field.setAccessible(accessible);
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignorePropertiesList.toArray(new String[ignorePropertiesList.size()]));
    }
}
