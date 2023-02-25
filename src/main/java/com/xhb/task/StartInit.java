package com.xhb.task;

import com.xhb.model.ClassInfo;
import com.xhb.model.Lesson;
import com.xhb.model.Teacher;
import com.xhb.service.ClassService;
import com.xhb.service.LessonsService;
import com.xhb.service.SchedulerService;
import com.xhb.service.TeacherService;
import com.xhb.utils.PoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xhb
 * @date: 2023/1/29 20:22
 * @description: TODO
 * @version: 1.0
 */
@Component
@Slf4j
public class StartInit {

    @Autowired
    ClassService classService;

    @Autowired
    LessonsService lessonsService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    SchedulerService schedulerService;

    @PostConstruct
    public void init() throws Exception {
        log.info("------------------------排课任务启动--------------------------");

        /**
         * 读取班级，教师，课程安装排信息
        */
        Map<String, Teacher> teachersMap = null;
        Map<String, ClassInfo> classInfoMap = new HashMap<>();

        String dirPath = "C:\\Users\\Administrator\\Desktop\\排课xlsx\\";
        String teacherFilePath = "C:\\Users\\Administrator\\Desktop\\排课xlsx\\高二任课一览表.xls";
        String lesssonFilePath = "C:\\Users\\Administrator\\Desktop\\排课xlsx\\高二会考后课时设置20230127.xlsx";
        String scheduleFilePath = "C:\\Users\\Administrator\\Desktop\\排课xlsx\\课表.xlsx";

        log.info("------------------------开始读取Excel数据--------------------------");
        // 导入班级信息
        // List<ClassInfo> classInfos = classService.importLessons();

        log.info("------------------------开始读取教师信息表："+teacherFilePath+"--------------------------");
        // 导入教师信息和班级信息
        teachersMap = teacherService.importTeachers(teacherFilePath,classInfoMap);
        log.info("------------------------读取"+teacherFilePath+"结束--------------------------");

        log.info("------------------------开始读取课程信息表："+lesssonFilePath+"--------------------------");
        //导入课程信息
        Map<String,List<Lesson>> classLessons = lessonsService.importLessons(lesssonFilePath);
        log.info("------------------------读取"+lesssonFilePath+"结束--------------------------");
        log.info("------------------------读取Excel数据结束--------------------------");


        log.info("------------------------开始进行排课--------------------------");
        schedulerService.scheduling(classInfoMap,teachersMap,classLessons);
        log.info("------------------------排课完成--------------------------");
        log.info("------------------------生成课程表文件--开始--------------------------");
        PoiUtils.createSchedule(classInfoMap,scheduleFilePath);
        log.info("------------------------生成课程表文件--完成--------------------------");

        log.info("------------------------排课任务结束--------------------------");



    }


}
