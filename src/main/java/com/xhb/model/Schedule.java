package com.xhb.model;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: xhb
 * @date: 2023/2/11 19:58
 * @description: 课表类
 * @version: 1.0
 */
@Data
public class Schedule {

    /**
     * 班级名称
    */
    private String className;

    /**
     * 课程坐标
    */
    private Map<String,Lesson[][]> coordinate;

    /**
     * 临时课程坐标
    */
    private Map<String,Set<String>> coordinateTemp;

    public Schedule() {
    }

    public Schedule(Map<String, Lesson[][]> coordinate) {
        this.coordinate = coordinate;
    }
}
