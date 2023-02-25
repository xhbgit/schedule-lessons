package com.xhb.service.impl;

import com.xhb.model.Lesson;
import com.xhb.service.LessonsService;
import com.xhb.utils.PoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: xhb
 * @date: 2023/2/11 16:02
 * @description: TODO
 * @version: 1.0
 */
@Service
@Slf4j
public class LessonsServiceImpl implements LessonsService {

    @Override
    public Map<String, List<Lesson>> importLessons(String filePath) {
        log.info("创建 WorkBook ...");
        Workbook workbook = PoiUtils.createWorkbook(filePath);
        log.info("读取任课表数据,同时创建班级集合");
        return PoiUtils.loadLessonsExcel(workbook);
    }
    
}
