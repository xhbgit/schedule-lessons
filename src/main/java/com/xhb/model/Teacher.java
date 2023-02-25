package com.xhb.model;

import lombok.Data;

import java.util.List;

/**
 * @author: xhb
 * @date: 2023/2/6 18:00
 * @description: TODO
 * @version: 1.0
 */
@Data
public class Teacher {



    /**
     * id
     */
    private String id;
    /**
     * 名称
    */
    private String name;
    /**
     * 负责班级(班主任)
    */
    private String calssId;

    /**
     * 担任课程
    */
    private List<Lesson> lessons;

    /**
     * 教师课表
    */
    private Schedule schedule;

}
