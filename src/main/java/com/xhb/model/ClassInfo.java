package com.xhb.model;

import lombok.Data;

/**
 * @author: xhb
 * @date: 2023/2/6 18:36
 * @description: TODO
 * @version: 1.0
 */
@Data
public class ClassInfo {

    /**
     * 班级id
    */
    private Integer id;
    /**
     * 年级
     */
    private String classLv;
    /**
     * 班级编号(几班)
    */
    private Integer graderNo;
    /**
     * 班级全名
    */
    private String className;
    /**
     * 班级类别
    */
    private String classType;

    /**
     * 班主任编号
    */
    private String headTeacher;

    /**
     * 班级课表
     */
    private Schedule schedule;


    public ClassInfo() {
    }

    public ClassInfo(String classLv, Integer graderNo, String classType) {
        this.classLv = classLv;
        this.graderNo = graderNo;
        this.classType = classType;
        this.className = this.classLv + this.graderNo  + "班";;
    }


}
