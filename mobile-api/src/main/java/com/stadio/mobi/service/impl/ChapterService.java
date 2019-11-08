package com.stadio.mobi.service.impl;

import com.stadio.mobi.controllers.ChapterController;
import com.stadio.mobi.controllers.ExamController;
import com.stadio.mobi.dtos.ChapterItemDTO;
import com.stadio.mobi.dtos.ClassItemDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IChapterService;
import com.stadio.mobi.service.ICourseService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.CourseItemDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.repository.main.ChapterRepository;
import com.stadio.model.repository.main.ClazzRepository;
import com.stadio.model.repository.main.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService extends BaseService implements IChapterService
{

    @Autowired ClazzRepository clazzRepository;

    @Autowired ChapterRepository chapterRepository;

    @Autowired ExamRepository examRepository;

    @Autowired ExamService examService;

    @Autowired
    ICourseService courseService;

    @Override
    public ResponseResult processChapterList()
    {
        List<Clazz> clazzList = clazzRepository.findAll();

        List<ClassItemDTO> classItemDTOList = new ArrayList<>();
        for (Clazz ck: clazzList)
        {
            if (ck.isDeleted()) {
                continue;
            }
            ClassItemDTO classItemDTO = new ClassItemDTO();
            classItemDTO.setId(ck.getId());
            classItemDTO.setClazzName(ck.getName());

            List<ChapterItemDTO> chapterItemDTOList = new ArrayList<>();
            for (String chapterId: ck.getIdChapters())
            {
                Chapter chapter = chapterRepository.findOne(chapterId);
                if (chapter != null)
                {
                    ChapterItemDTO chapterItemDTO = new ChapterItemDTO();
                    chapterItemDTO.setId(chapterId);

                    chapterItemDTOList.add(chapterItemDTO);

                    String url = MvcUriComponentsBuilder.fromMethodName(ChapterController.class, "examByChapterId", null, chapterId, 1, 10)
                            .host(host).port(port)
                            .build().toString();

                    chapterItemDTO.getActions().put(ActionBase.EXAM_LIST, url);

                    chapterItemDTO.setChapterName(chapter.getName());
                }

            }
            classItemDTO.setChapterList(chapterItemDTOList);

            List<CourseItemDTO> courseList = courseService.getCoursesByClass(ck.getId());

            classItemDTO.setCourseList(courseList);

            List<ExamItemDTO> examItemDTOList = new ArrayList<>();
            PageRequest pageRequest = new PageRequest(0, 7, new Sort(Sort.Direction.DESC, "created_date"));
            List<Exam> examList = examRepository.findExamByClazzIdOrderByCreatedByDesc(ck.getId(), pageRequest).getContent();
            for (Exam exam: examList) {
                ExamItemDTO examItemDTO = ExamItemDTO.with(exam);

                String detailsUrl = MvcUriComponentsBuilder
                        .fromMethodName(ExamController.class, "getExamDetails", exam.getId())
                        .host(host).port(port)
                        .build().toString();

                examItemDTO.getActions().put(ActionBase.DETAILS, detailsUrl);
                examItemDTOList.add(examItemDTO);
            }

            classItemDTO.setExamList(examItemDTOList);

            classItemDTOList.add(classItemDTO);
        }

        return ResponseResult.newSuccessInstance(classItemDTOList);
    }

    @Override
    public ResponseResult processGetExamListByChapterId(String token, String chapterId, int page, int limit)
    {
        PageRequest request = new PageRequest(page - 1, limit);
        List<Exam> examList = examRepository.findExamByChapterIdOrderByCreatedByDesc(chapterId, request).getContent();
        List<ExamItemDTO> examItemDTOList = examService.getListExamItemDTO(examList, token);
        return ResponseResult.newSuccessInstance(examItemDTOList);
    }

    @Override
    public ResponseResult processGetExamListByChapterCode(String token, String code, int page, int limit)
    {
        Chapter chapter = chapterRepository.findByCode(code);
        List<ExamItemDTO> examItemDTOList = new ArrayList<>();
        if (chapter != null)
        {
            PageRequest request = new PageRequest(page - 1, limit);
            List<Exam> examList = examRepository.findExamByChapterIdOrderByCreatedByDesc(chapter.getId(), request).getContent();
            examItemDTOList = examService.getListExamItemDTO(examList, token);

        }
        return ResponseResult.newSuccessInstance(examItemDTOList);
    }
}
