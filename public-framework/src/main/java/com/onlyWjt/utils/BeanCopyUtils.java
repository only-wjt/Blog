package com.onlyWjt.utils;

import com.onlyWjt.domain.entity.Article;
import com.onlyWjt.domain.view.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }
    public static <V> V copyBean(Object source,Class<V> clazz){
        V result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.
                stream().
                map(o -> BeanCopyUtils.copyBean(o, clazz)).
                collect(Collectors.toList());
    }
}
