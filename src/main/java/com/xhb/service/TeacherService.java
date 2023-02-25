package com.xhb.service;

import com.xhb.model.ClassInfo;
import com.xhb.model.Teacher;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: xhb
 * @date: 2023/2/6 17:53
 * @description: TODO
 * @version: 1.0
 */
@Service
public interface TeacherService {
    Map<String, Teacher> importTeachers(String teacherFileName, Map<String, ClassInfo> classInfoMap) throws Exception;
}
