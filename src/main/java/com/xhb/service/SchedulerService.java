package com.xhb.service;

import com.xhb.model.ClassInfo;
import com.xhb.model.Lesson;
import com.xhb.model.Teacher;

import java.util.List;
import java.util.Map;

/**
 * @author: xhb
 * @date: 2023/2/19 16:01
 * @description: TODO
 * @version: 1.0
 */
public interface SchedulerService {
    void scheduling(Map<String, ClassInfo> classInfoMap, Map<String, Teacher> teachersMap, Map<String, List<Lesson>> classLessons);
}
