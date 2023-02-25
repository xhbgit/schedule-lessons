package com.xhb.utils;

import com.xhb.model.Lesson;
import com.xhb.model.Schedule;
import com.xhb.model.Teacher;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author: xhb
 * @date: 2023/2/23 18:14
 * @description: TODO
 * @version: 1.0
 */
public class ScheduleUtils {

    /**
     * 课程坐标连接符
    */
    public final static String splitStr = ",";

    /**
     * 班级课表key连接符
    */
    public final static String classLessonsKeySplitStr = ",";


    /**
     * @author xhb
     * @date 2023/2/23 18:39
     * @description TODO
     * @param scheduleType: 课表类型
    	 * @param coordinateStr: 课程坐标字符串
    	 * @param teacher:
     * @return boolean
    */
    public static boolean checkTeacherSchedule(String scheduleType, String coordinateStr, Teacher teacher) {

        String[] split = coordinateStr.split(splitStr);
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        if (teacher.getSchedule() == null) {
            Schedule schedule = new Schedule();
            // 新建空白课表坐标属性
            Map<String, Lesson[][]> map = new HashMap<>();
            // 指定坐标范围
            Lesson[][] lessonsCoordinate = new Lesson[10][5];
            map.put(scheduleType,lessonsCoordinate);
            teacher.setSchedule(new Schedule(map));
        }
        if (teacher.getSchedule().getCoordinate().get(scheduleType)[x][y] != null)
            return true;
        return false;
    }

    /**
     * @author xhb
     * @date 2023/2/23 18:41
     * @description TODO
     * @param LsNum:
    	 * @param days:
     * @return Set<String>
    */
    public static Set<String> createTempSchedules(int LsNum,int days ) {
        /**
         * 注意！！！！！注意！！！！！！！！！！多个班级指向一个临时课程坐标，会多个对象修改一个对象
         */
        // 创建临时课程坐标
        Set<String> schedules = new HashSet<>();
        for (int i = 0; i < LsNum; i++) {
            for (int j = 0; j < days; j++) {
                schedules.add(i+splitStr+j);
            }
        }

        return schedules;
    }


    /**
     * @author xhb
     * @date 2023/2/23 19:27
     * @description TODO
     * @param coordinateStr:
     * @return int
    */
    public static int[] getCoordinate(String coordinateStr) {
        String[] split = coordinateStr.split(splitStr);
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        return new int[]{x,y};
    }

    public static String getclassLessonsKey(String classType, String scheduleType) {
        return classType+"-"+scheduleType;
    }
}
