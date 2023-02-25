package com.xhb.service;

import com.xhb.model.Lesson;

import java.util.List;
import java.util.Map;

/**
 * @author: xhb
 * @date: 2023/2/11 16:02
 * @description: TODO
 * @version: 1.0
 */
public interface LessonsService {
    Map<String, List<Lesson>> importLessons(String lesssonFilePath);
}
