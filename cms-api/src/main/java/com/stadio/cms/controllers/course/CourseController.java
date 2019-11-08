package com.stadio.cms.controllers.course;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.*;
import com.stadio.model.dtos.cms.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/course", name = "Quản lý khóa học")
public class CourseController extends BaseController {

    private Logger logger = LogManager.getLogger(CourseController.class);

    @Autowired
    ICourseService courseService;

    @Autowired
    ITeacherService teacherService;

    @Autowired
    ISectionService sectionService;

    @Autowired
    IParagraphService paragraphService;

    @RequestMapping(value = "/search", method = RequestMethod.POST, name = "Tìm kiếm khóa học")
    public ResponseResult search(HttpServletRequest request,
                                 @RequestHeader(value = "Authorization") String token,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                 @RequestBody CourseSearchFormDTO courseSearchFormDTO) {
        return courseService.processSearchCourse(courseSearchFormDTO, page, pageSize);
    }

    @RequestMapping(value = "/by-id", method = RequestMethod.GET, name = "Thông tin khóa học")
    public ResponseResult getCourse(@RequestParam(value = "id") String id) {
        return courseService.processGetCourse(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Tạo khóa học mới")
    public ResponseResult createCourse(@RequestBody CourseFormDTO courseFormDTO) {
        return courseService.processCreateCourse(courseFormDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhật thông tin")
    public ResponseResult updateCourse(@RequestBody CourseFormDTO courseFormDTO) {
        return courseService.processUpdateCourse(courseFormDTO);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, name = "Xóa khóa học")
    public ResponseResult deleteCourse(@RequestParam(value = "id") String id) {
        return courseService.processDeleteCourse(id);
    }

    //section-manager
    @RequestMapping(value = "/section-manager/list", method = RequestMethod.GET)
    public ResponseResult getListSection(@RequestParam(value = "id") String courseId) {
        return sectionService.processGetListSection(courseId);
    }

    @RequestMapping(value = "/section-manager/create", method = RequestMethod.POST)
    public ResponseResult createSection(@RequestBody SectionFormDTO sectionFormDTO) {
        return sectionService.processCreateSection(sectionFormDTO);
    }

    @RequestMapping(value = "/section-manager/update", method = RequestMethod.POST)
    public ResponseResult updateSection(@RequestBody SectionFormDTO sectionFormDTO) {
        return sectionService.processUpdateSection(sectionFormDTO);
    }

    @RequestMapping(value = "/section-manager/delete", method = RequestMethod.POST)
    public ResponseResult deleteSection(@RequestParam(value = "id") String id) {
        return sectionService.processDeleteSection(id);
    }


    // paragraph-manager

    @RequestMapping(value = "/paragraph-manager/paragraph-of-lecture", method = RequestMethod.GET)
    public ResponseResult getListParagraphOfLecture(@RequestParam(value = "id") String lectureId) {
        return paragraphService.processGetDetailOfLecture(lectureId);
    }

    @RequestMapping(value = "/paragraph-manager/create", method = RequestMethod.POST)
    public ResponseResult createParagraph(@RequestBody ParagraphFormDTO paragraphFormDTO) {
        return paragraphService.processCreateDetail(paragraphFormDTO);
    }

    @RequestMapping(value = "/paragraph-manager/update", method = RequestMethod.POST)
    public ResponseResult updateParagraph(@RequestBody ParagraphFormDTO paragraphFormDTO) {
        return paragraphService.processUpdateDetail(paragraphFormDTO);
    }

    // teacher-manager
    @RequestMapping(value = "/teacher-manager/create", method = RequestMethod.POST)
    public ResponseResult createTeacher(@RequestBody TeacherFormDTO teacherFormDTO) {
        return teacherService.processCreateOneTeacher(teacherFormDTO);
    }

    @RequestMapping(value = "/teacher-manager/update", method = RequestMethod.POST)
    public ResponseResult updateTeacher(@RequestBody TeacherFormDTO teacherFormDTO) {
        return teacherService.processUpdateOneTeacher(teacherFormDTO);
    }

    @RequestMapping(value = "/teacher-manager/all", method = RequestMethod.GET)
    public ResponseResult getAllTeacher(HttpServletRequest request) {
        return teacherService.processGetAllTeacher();
    }

    @RequestMapping(value = "/teacher-manager/delete", method = RequestMethod.POST)
    public ResponseResult<?> deleteTeacher(@RequestParam(value = "id", required = true) String id) {
        return teacherService.processDeleteTeacher(id);
    }

}
