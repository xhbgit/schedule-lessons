package com.xhb.service.impl;

import com.xhb.model.ClassInfo;
import com.xhb.model.Teacher;
import com.xhb.service.TeacherService;
import com.xhb.utils.PoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xhb
 * @since 2022-12-12
 */
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    @Override
    public Map<String, Teacher> importTeachers(String filePath, Map<String, ClassInfo> classInfoMap) {

        log.info("创建 WorkBook ...");
        Workbook workbook = PoiUtils.createWorkbook(filePath);
        log.info("读取任课表数据,同时创建班级集合");
        return PoiUtils.loadTeacherExcel(workbook, classInfoMap);

    }
}
