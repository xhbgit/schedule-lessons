package com.xhb.service.impl;

import com.xhb.model.ClassInfo;
import com.xhb.model.Lesson;
import com.xhb.model.Schedule;
import com.xhb.model.Teacher;
import com.xhb.service.SchedulerService;
import com.xhb.utils.ScheduleUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: xhb
 * @date: 2023/2/19 16:02
 * @description: TODO
 * @version: 1.0
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

    /**
     * @author xhb
     * @date 2023/2/19 16:24
     * @description TODO 排课逻辑实现
     *          * 1.初始化班级空白课表，空闲课表坐标，填充固定课程
     *          * 2.遍历教师对象，安排课程
     *          *      （1）获取当前班级空闲课表坐标（临时坐标内为未安排坐标），判断与该教师个人课表是否冲突
     *          *      （2）无冲突后进行课程填充，之后删除临时坐标中该坐标，将此坐标填充到该教师课表
     * @param classInfoMap:
    	 * @param teachersMap:
    	 * @param classLessons:
     * @return void
    */
    @Override
    public void scheduling(Map<String, ClassInfo> classInfoMap, Map<String, Teacher> teachersMap, Map<String, List<Lesson>> classLessons) {


        /**
         * 1.初始化班级空白课表，空闲课表坐标，填充固定课程
         */
        /*这些内容可以创建一个对象*/
        String[] scheduleTypes = {"早读", "早自习", "一至五", "周六日", "晚一二（学科）", "晚三（自主）"};
        int LsNum = 10; //对应excel行数
        int days = 5;   //对应Excel列数
        /*这些内容可以创建一个对象*/

        // 获取排课类型（早读晚自习等）
        String scheduleType = scheduleTypes[2];

        /*固定课程填充代码段*/
        //  1.填充空白课表, 填入固定课程
        //  2.删除对应的空闲课表坐标，set 修改后临时坐标
        for (ClassInfo bean : classInfoMap.values()) {

            //新建课程坐标对象
            Schedule schedule = new Schedule();
            // 新建空闲课表坐标（课程坐标对象内属性）
            Map<String, Set<String>> coordinateTemp = new HashMap<>();
            // 新建空白课表（课程坐标对象内属性）
            Map<String, Lesson[][]> map = new HashMap<>();

            // 创建临时坐标
            Set<String> schedules = ScheduleUtils.createTempSchedules(LsNum, days);

            // 班级名称
            schedule.setClassName(bean.getClassName());
            // 创建空课表数组
            Lesson[][] lessons = new Lesson[LsNum][days];
            //填充固定课程
            Lesson lesson = new Lesson();
            lesson.setLsName("班会");
            lesson.setTeacherName(bean.getHeadTeacher());
            lessons[9][0] = lesson; //可升级:封装一个课表类型对象

            // put <课表类型,课表数组>
            // 不同课程安排不一样，例如早读，一至五，周六日
            map.put(scheduleType, lessons);
            //空闲课表坐标删除
            schedules.remove("9,0");    //可升级为固定课程对象获取
            coordinateTemp.put(scheduleType, schedules);

            // 课表对象set课程坐标属性
            schedule.setCoordinate(map);
            // set临时坐标属性
            schedule.setCoordinateTemp(coordinateTemp);

            //set到班级对象
            bean.setSchedule(schedule);
        }
        /*固定课程填充代码段*/
        /**
         * 2.遍历教师排课
         *      1.遍历当前教师授课集合，获取对应周课时数
         *      2.获取授课班级空闲课表坐标，
         *      3.教师课表冲突验证，不冲突填入当前课程，
         *          正式课表填入课程，教师课表填入课程，临时坐标删除
         *      4.否则获取下一个坐标，重复第三部操作
         */
        for (Teacher teacher : teachersMap.values()) {
            for (Lesson lesson : teacher.getLessons()) {

                // 获取周课时数
                String classType = classInfoMap.get(lesson.getClassId()).getClassType();
                List<Lesson> lessons = classLessons.get(ScheduleUtils.getclassLessonsKey(classType, scheduleType));
                for (Lesson bean : lessons) {
                    if(bean.getLsName().equals(lesson.getLsName()))
                        lesson.setLsNum(bean.getLsNum());
                }

                int lsNum = lesson.getLsNum();
                // 获取班级课表对象
                Schedule schedule = classInfoMap.get(lesson.getClassId()).getSchedule();
                // 获取空闲课表坐标
                Set<String> coordinates = schedule.getCoordinateTemp().get(scheduleType);

                /**
                 * 遍历临时坐标（未安排课程坐标）
                 * 这里使用迭代器，方便遍历时删除
                */
                for (Iterator<String> iterator = coordinates.iterator(); iterator.hasNext(); ) {
                    String coordinate = iterator.next();

                    //剩余排课数量
                    if(lsNum == 0) {
                        lesson.setIsSchedule(true);
                        break;
                    }
                    // 判断是否与教师课表冲突
                    if(ScheduleUtils.checkTeacherSchedule(scheduleType, coordinate, teacher)) {
                        continue;
                    }

                    int [] coordinateIndex = ScheduleUtils.getCoordinate(coordinate);
                    // 填入班级课程表
                    schedule.getCoordinate()
                            .get(scheduleType)[coordinateIndex[0]][coordinateIndex[1]] = lesson;
                    // 填入教师课程表
                    teacher.getSchedule().getCoordinate()
                            .get(scheduleType)[coordinateIndex[0]][coordinateIndex[1]] = lesson;
                    // 空闲课表坐标删除
                    iterator.remove();
                    lsNum = lsNum - 1;
                }

            }

        }
    }

}
