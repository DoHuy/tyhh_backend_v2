package com.stadio.cms.service.impl;

import com.stadio.cms.model.PageInfo;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.ICourseService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Course;
import com.stadio.model.documents.Topic;
import com.stadio.model.dtos.cms.CourseDetailDTO;
import com.stadio.model.dtos.cms.CourseFormDTO;
import com.stadio.model.dtos.cms.CourseItemDTO;
import com.stadio.model.dtos.cms.CourseSearchFormDTO;
import com.stadio.model.repository.main.CourseRepository;
import com.stadio.model.repository.main.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CourseService extends BaseService  implements ICourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TopicRepository topicRepository;

    @Override
    public ResponseResult processSearchCourse(CourseSearchFormDTO courseSearchFormDTO, int page, int pageSize) {
        List<Course> courseList = courseRepository.search(courseSearchFormDTO,page,pageSize);
        List<CourseItemDTO> courseItemDTOList = new LinkedList<>();
        if(courseList!=null&& courseList.size()>0){
            courseList.parallelStream().forEach(course -> {
                CourseItemDTO courseItemDTO = new CourseItemDTO(course);
                courseItemDTOList.add(courseItemDTO);
            });
        }
        long quantity = courseRepository.countSearch(courseSearchFormDTO);
        PageInfo pageInfo = new PageInfo(page, quantity, pageSize, "");
        TableList tableList = new TableList(pageInfo,courseItemDTOList);
        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult processCreateCourse(CourseFormDTO courseFormDTO) {
        ResponseResult responseResult = validateFormCourse(courseFormDTO);
        if(responseResult !=null)
            return responseResult;
        Course course = courseRepository.findFirstByCode(courseFormDTO.getCode());
        if(course!=null)
            return ResponseResult.newErrorInstance(ResponseCode.EXIST_VALUE,"course.exist");
        course = new Course();

        course.setCode(courseFormDTO.getCode());
        course.setName(courseFormDTO.getName());
        course.setPrice(courseFormDTO.getPrice());
        course.setTeacherId(courseFormDTO.getTeacherId());
        course.setClazzId(courseFormDTO.getClazzId());
        course.setChapterId(courseFormDTO.getChapterId());
        course.setTime2pass(courseFormDTO.getTime2pass());
        course.setDescription(courseFormDTO.getDescription());
        course.setPictureUrl(courseFormDTO.getPictureUrl());
        course.setEnable(courseFormDTO.getEnable());
        course.setKeywords(courseFormDTO.getKeywords());
        course.setVideoPreviewUrl(courseFormDTO.getVideoPreviewUrl());
        course.setShareLinkUrl(courseFormDTO.getShareLinkUrl());

        Topic topic = new Topic();
        topicRepository.save(topic);

        course.setTopicId(topic.getId());

        courseRepository.save(course);
        return ResponseResult.newSuccessInstance(course);
    }

    @Override
    public ResponseResult processUpdateCourse(CourseFormDTO courseFormDTO) {
        ResponseResult responseResult = validateFormCourse(courseFormDTO);
        if(responseResult !=null)
            return responseResult;
        Course course = courseRepository.findOne(courseFormDTO.getId());
        if(course==null)
            return ResponseResult.newErrorInstance(ResponseCode.EXIST_VALUE,"course.not.exist");

        course.setCode(courseFormDTO.getCode());
        course.setName(courseFormDTO.getName());

        if(course.getPrice()!=courseFormDTO.getPrice()){
            course.setPriceOld(course.getPrice());
            course.setPrice(courseFormDTO.getPrice());
        }

        course.setTeacherId(courseFormDTO.getTeacherId());
        course.setClazzId(courseFormDTO.getClazzId());
        course.setChapterId(courseFormDTO.getChapterId());
        course.setTime2pass(courseFormDTO.getTime2pass());
        course.setDescription(courseFormDTO.getDescription());
        course.setPictureUrl(courseFormDTO.getPictureUrl());
        course.setEnable(courseFormDTO.getEnable());
        course.setKeywords(courseFormDTO.getKeywords());
        course.setVideoPreviewUrl(courseFormDTO.getVideoPreviewUrl());
        course.setShareLinkUrl(courseFormDTO.getShareLinkUrl());

        courseRepository.save(course);
        return ResponseResult.newSuccessInstance(course);
    }

    @Override
    public ResponseResult processDeleteCourse(String id) {
        Course course = courseRepository.findOne(id);
        if(course!=null){
            course.setDeleted(true);
            courseRepository.save(course);
        }
        return ResponseResult.newSuccessInstance("delete.success");

    }

    @Override
    public ResponseResult processGetCourse(String id) {
        Course course = courseRepository.findOne(id);
        if(course!=null){
            CourseDetailDTO courseDetailDTO = new CourseDetailDTO(course);
            return ResponseResult.newSuccessInstance(courseDetailDTO);
        }
        else return ResponseResult.newErrorInstance(ResponseCode.FAIL,"course.not.exist");
    }

    ResponseResult validateFormCourse(CourseFormDTO courseFormDTO){
        if(courseFormDTO==null){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"course.invalid.");
        }
        if(!StringUtils.isValid(courseFormDTO.getCode())){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"course.invalid.code");
        }
        if(!StringUtils.isValid(courseFormDTO.getName())){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"course.invalid.name");
        }
        if(courseFormDTO.getPrice()==null){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"course.invalid.price");
        }
        if(courseFormDTO.getTime2pass()==null){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"course.invalid.time");
        }

        return null;
    }
}
