package com.xhb.model;

/**
 * @author: xhb
 * @date: 2023/2/11 21:32
 * @description: TODO
 * @version: 1.0
 */
public enum ScheduleDistrict {


    AM("上午"),
    PM("下午"),
    NIGHT("晚上");

    /**
     * 课间区分 早读，上午晚自习等
    */
    private String dsName;

    ScheduleDistrict(String dsName) {
        this.dsName = dsName;
    }
}
