package com.xhb.model;

import lombok.Data;

/**
 * @author: xhb
 * @date: 2023/2/11 16:17
 * @description: TODO
 * @version: 1.0
 */
@Data
public class Lesson {

    /**
     * 所属班级名分类
     */
    private String classType;

    /**
     * 所属班级年级
     */
    private String classLv;

    /**
     * 所属班级id
    */
    private String classId;

    /**
     * 课程名称
    */
    private String lsName;

    /**
     * 课时数量
    */
    private int lsNum;

    /**
     * 任课老师名称
    */
    private String teacherName;

    /**
     * 是否排课
     */
    private Boolean isSchedule = false;

    public Lesson() {
    }

    public Lesson(String classType, String lsName, int lsNum) {
        this.classType = classType;
        this.lsName = lsName;
        this.lsNum = lsNum;
    }


}
