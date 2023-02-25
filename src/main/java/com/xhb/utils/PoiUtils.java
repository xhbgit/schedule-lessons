package com.xhb.utils;

import com.xhb.model.ClassInfo;
import com.xhb.model.Lesson;
import com.xhb.model.Teacher;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xhb
 * @date: 2023/2/15 22:25
 * @description: TODO
 * @version: 1.0
 */
public class PoiUtils {
    /**
     * @author xhb
     * @date 2023/2/15 22:33
     * @description 根据文件类型生成 Workbook对象
     * @param filePath:文件路径
     * @return Workbook
    */
    public static Workbook createWorkbook(String filePath) {
        // 创建 WorkBook 工作簿
        Workbook workbook = null;
        FileInputStream fileInputStream =null;
        // 判断表格文件格式
        try {
            // 判断文件是否存在
            File excelFile = new File(filePath);
            if (!excelFile.exists()) {
                throw new Exception("文件不存在！");
            }
            fileInputStream = new FileInputStream(excelFile);
            workbook = WorkbookFactory.create(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }

    /**
     * @author xhb
     * @date 2023/2/16 22:22
     * @description 解析教师任课表
     * @param workbook :
     * @param classInfoMap
     * @return Map<String,Teacher>
    */
    public static Map<String, Teacher> loadTeacherExcel(Workbook workbook, Map<String, ClassInfo> classInfoMap) {

        Map<String, Teacher> teacherMap = new HashMap<>();
        // 获取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        // 获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        //获取年级(第一行)、班次（第二行）、班级类别（第三行）、教师信息开始行（第四行）
        int titlenum = 0;
        int sailingsnum = 1;
        int typenum = 2;
        int headTeacherNum = 3;

        // 获取标题
        Row titlerow = sheet.getRow(titlenum);
        String title = titlerow.getCell(0).getStringCellValue();
        // 获取班次集合
        Row sailingsRow = sheet.getRow(sailingsnum);
        // 获取班级选课类型集合
        Row teachTypeRow = sheet.getRow(typenum);
        // 获取班级选课类型集合
        Row headTeacherRow = sheet.getRow(headTeacherNum);
        // 标题拆分出年级
        String grader = title.split("年级")[0];

        //填充班级集合数据
        for (int i = 1; i < sailingsRow.getLastCellNum(); i++) {
            int graderNo = (int)sailingsRow.getCell(i).getNumericCellValue();
            String calssType = teachTypeRow.getCell(i).getStringCellValue();
            String className = grader + graderNo + "班";
            ClassInfo classInfo = new ClassInfo(grader, graderNo, calssType);
            classInfo.setHeadTeacher(headTeacherRow.getCell(i).getStringCellValue());
            classInfoMap.put(className,classInfo);
        }
        // 遍历任课教师数据
        for (int i = typenum+1; i < rownum; i++) {
            Row row = sheet.getRow(i);
            String teach = null;
            for (int j = 0; j < row.getLastCellNum(); j++) {
                //获取课程名
                if (j==0){
                    teach = row.getCell(j).getStringCellValue();
                }else {
                    //获取此课程教师
                    // 教师姓名
                    String name = row.getCell(j).getStringCellValue();
                    // 非空
                    if (name.isEmpty())
                        continue;
                    // 获取班级编号(几班)
                    int graderNo = (int)sailingsRow.getCell(j).getNumericCellValue();

                    /*教师安排授课任务逻辑，任课集合新增一条*/
                    Teacher teacher = teacherMap.get(name);
                    if (teacher==null) {
                        teacher = new Teacher();
                    }
                    List<Lesson> lessons = teacher.getLessons();
                    if (lessons==null){
                        lessons = new ArrayList<Lesson>();
                    }
                    Lesson lesson = new Lesson();
                    lesson.setClassId(grader+graderNo+"班");
                    lesson.setLsName(teach);
                    lesson.setTeacherName(name);
                    lessons.add(lesson);
                    teacher.setLessons(lessons);
                    //班主任判断
                    if (teach.equals("班主任")){
                        teacher.setCalssId(grader+graderNo+"班");
                    }
                    // 教师map覆盖
                    teacherMap.put(name,teacher);
                }
            }
        }
        return teacherMap;
    }


    /**
     * @author xhb
     * @date 2023/2/16 18:58
     * @description TODO
     * @param workbook:
     * @return Map<String,List<Lesson>>
    */
    public static Map<String, List<Lesson>> loadLessonsExcel(Workbook workbook) {

        Map<String, List<Lesson>> lessonsMap = new HashMap<>();
        //存放所有选课组合
        List<Lesson> calssTypeList = new ArrayList<>();

        //循环行数（8行一个循环）
        int cycleRowNum = 9;
        // 班级选课行
        int classTypeRowIndex = 0;
        // 科目标题行
        int lessonsTypeRowIndex = 1;
        // 工作日课程对应行
        int weekdaysRowIndex = 4;

        // 获取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        // 获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        /**
         * 9行一个循环，计算有几套课程安排，初始index + index * 9 为对应行数
        */
        int classTypeNum = rownum /cycleRowNum;
        for (int i = 0; i < classTypeNum; i++) {

            ArrayList<Lesson> lessons = new ArrayList<>();

            // 计算 数据对应行 index
            int startIndex = i * cycleRowNum;
            int classTypeIndex = classTypeRowIndex + startIndex;
            int lessonsTypeIndex = lessonsTypeRowIndex + startIndex;
            int weekdaysIndex = weekdaysRowIndex + startIndex;

            // 获取选课类型  举例：物化生、史地政
            String classType = sheet.getRow(classTypeIndex).getCell(0).getStringCellValue();
            // 获取课程名数据行
            Row lessonsTypeRow = sheet.getRow(lessonsTypeIndex);
            // 获取周一至周五各课节数行数据
            Row weekDayLessonsRow = sheet.getRow(weekdaysIndex);

            /*
             * 填充数据
             *  1.选课类型到选课集合list
             *  2.课程数据到map
            */
            //填充选课
            Lesson clssTypeBean = new Lesson();
            clssTypeBean.setClassType(classType);
            calssTypeList.add(clssTypeBean);

            // list集合填充课程
            String scheduleType = null;
            for (int j = 0; j < weekDayLessonsRow.getLastCellNum(); j++) {
                if (j ==0 ){
                    scheduleType = weekDayLessonsRow.getCell(j).getStringCellValue();
                }else {
                    // System.out.println("---------------------列索引值："+j);
                    int lessonNum = (int)weekDayLessonsRow.getCell(j).getNumericCellValue();
                    // 课时为0跳出本次循环
                    if (lessonNum == 0){
                        continue;
                    }
                    Lesson lesson = new Lesson(scheduleType,lessonsTypeRow.getCell(j).getStringCellValue(),lessonNum);
                    lessons.add(lesson);
                }
            }
            // 这里进行了key拼接 举例：物化生-一至五
            // lessonsMap.put(classType+"-"+scheduleType,lessons);
            lessonsMap.put(ScheduleUtils.getclassLessonsKey(classType,scheduleType),lessons);
        }
        lessonsMap.put("选课类型",calssTypeList);

        return lessonsMap;
    }

    public static boolean createSchedule(Map<String, ClassInfo> classInfoMap,String filePath){


        return false;
    }
}
