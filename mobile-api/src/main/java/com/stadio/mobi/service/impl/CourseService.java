package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.mongodb.DBObject;
import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICourseService;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.mobility.CourseItemDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.dtos.mobility.course.QuestionInLectureDTO;
import com.stadio.model.dtos.mobility.course.CourseDetailDTO;
import com.stadio.model.dtos.mobility.course.LectureDTO;
import com.stadio.model.dtos.mobility.course.LectureDetailDTO;
import com.stadio.model.dtos.mobility.teacher.TeacherDTO;
import com.stadio.model.enu.BookmarkType;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.enu.UserCourseAction;
import com.stadio.model.model.ExamIn;
import com.stadio.model.model.course.QuestionInVideo;
import com.stadio.model.redisUtils.NewestCourseRedisRepository;
import com.stadio.model.repository.main.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CourseService extends BaseService implements ICourseService {

    private static Logger logger = LogManager.getLogger(CourseService.class);

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ClazzRepository clazzRepository;

    @Autowired
    NewestCourseRedisRepository newestCourseRedisRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired QuestionRepository questionRepository;

    @Override
    public ResponseResult processGetNewestCourse() {
        List<CourseItemDTO> courseItemDTOList = new ArrayList<>();

        Map<String,String> mapNewestCourse = newestCourseRedisRepository.processGetCourse();
        if(!mapNewestCourse.isEmpty()){
            List<String> courseList = new LinkedList<>(mapNewestCourse.values());
            if(courseList!=null && courseList.size()>0){
                List<CourseItemDTO> finalCourseItemDTOList = courseItemDTOList;
                courseList.forEach(s -> {
                    CourseItemDTO courseItemDTO = JsonUtils.parse(s,CourseItemDTO.class);
                    finalCourseItemDTOList.add(courseItemDTO);
                });
            }
        }else{
            final int page =0;
            final int pageSize = 10;
            final Pageable pageableRequest = new PageRequest(page, pageSize,new Sort(Sort.Direction.DESC, "created_date"));
            List<Course> courseList = courseRepository.findAll(pageableRequest).getContent();
            courseItemDTOList = getCourseItemDTOList(courseList);
        }
        return ResponseResult.newSuccessInstance(courseItemDTOList);
    }

    @Override
    public ResponseResult processGetNewestCourseFromMongo() {
        List<CourseItemDTO> courseItemDTOList = new ArrayList<>();

        final Pageable pageableRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "created_date"));
        List<Course> courseList = courseRepository.findAllByDeletedIsFalseAndEnableIsTrue(pageableRequest).getContent();
        courseItemDTOList = getCourseItemDTOList(courseList);
        return ResponseResult.newSuccessInstance(courseItemDTOList);
    }

    @Override
    public ResponseResult processGetOtherCourseForRecommendation(String currentCourseId, int page, int pageSize) {
        List<Course> courseList;
        PageRequest request = new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "created_date"));

        if (isUserLogined()) {
            User user = getUserRequesting();
            List<UserCourse> coursesRegistered = userCourseRepository.findAllByUserIdRefAndAction(user.getId(), UserCourseAction.REGISTER);

            List<String> courseIds = new ArrayList<>();
            if (!HelperUtils.isEmptyArray(coursesRegistered)) {
                courseIds = coursesRegistered.stream().map(sc -> sc.getCourseIdRef()).collect(Collectors.toList());
            }
            if (StringUtils.isNotNull(currentCourseId)) {
                courseIds.add(currentCourseId);
            }
            //TODO Find Class relate to user
            courseList = courseRepository.findByClazzIdInAndIdNotInAndDeletedIsFalseAndEnableIsTrue(
                    Arrays.asList(user.getClazzId()),
                    courseIds,
                    request
            ).getContent();
        } else {
            List<Clazz> clazzs = clazzRepository.findAll();
            List<String> clazzIds = new ArrayList<>();
            if (!HelperUtils.isEmptyArray(clazzs)) {
                clazzIds = clazzs.stream().map(sc -> sc.getId()).collect(Collectors.toList());
            }
            courseList = courseRepository.findByClazzIdInAndIdNotInAndDeletedIsFalseAndEnableIsTrue(
                    clazzIds,
                    new ArrayList<>(),
                    request
            ).getContent();
        }

        List<CourseItemDTO> courseItemDTOList = getCourseItemDTOList(courseList);

        return ResponseResult.newSuccessInstance(courseItemDTOList);
    }

    @Override
    public ResponseResult processGetCourseByClass(String classId) {
        List<CourseItemDTO> courseItemDTOList = this.getCoursesByClass(classId);
        return ResponseResult.newSuccessInstance(courseItemDTOList);
    }

    @Override
    public List<CourseItemDTO> getCoursesByClass(String classId) {
        Sort sort = new Sort(Sort.Direction.DESC, "created_date");
        List<Course> courseList = courseRepository.findCoursesByClazzIdOrIsClazzAllIsTrueAndDeletedIsFalseAndEnableIsTrueOrderByCreatedDateDesc(classId, sort);
//        List<Course> courseList2 = courseRepository.findCoursesByClazzAllIsTrueAndDeletedIsFalseAndEnableIsTrueOrderByCreatedDateDesc();
//
//        if (!HelperUtils.isEmptyArray(courseList2)) {
//            if (HelperUtils.isEmptyArray(courseList)) {
//                courseList = courseList2;
//            } else {
//                List<String> ids = courseList.stream().map(x -> x.getId()).collect(Collectors.toList());
//                for (Course course: courseList2) {
//                    if (!ids.contains(course.getId())) {
//                        courseList.add(course);
//                    }
//                }
//            }
//        }

        List<CourseItemDTO> courseItemDTOList = getCourseItemDTOList(courseList);
        return courseItemDTOList;
    }

    @Override
    public ResponseResult processGetNewestListCourse() {
        return null;
    }

    @Override
    public ResponseResult getLecturesInCourse(String courseId) {

        List<Section> sections = sectionRepository.findByCourseIdIsAndDeletedIsOrderByPositionAsc(courseId, false);
        List<HashMap> response = new ArrayList<>();

        for (Section section: sections) {
            List<Lecture> lectures = lectureRepository.findBySectionIdIsAndDeletedIsOrderByPositionAsc(section.getId(), false);

            List<LectureDTO> lectureDTOS = new ArrayList<>();
            for (Lecture lecture: lectures) {
                lectureDTOS.add(new LectureDTO(lecture));
            }

            HashMap<String, Object> res = new HashMap<>();
            res.put("sectionName", section.getName());
            res.put("lectures", lectureDTOS);

            response.add(res);
        }

        return ResponseResult.newSuccessInstance(response);
    }

    @Override
    public ResponseResult registerCourse(String courseId) {
        Course course = courseRepository.findFirstByIdAndDeletedIsFalseAndEnableIsTrue(courseId);
        if (course == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("course.not.found"));
        }

        User user = this.getUserRequesting();

        UserCourse userCourse = userCourseRepository.findFirstByUserIdRefAndCourseIdRefAndAction(user.getId(), courseId, UserCourseAction.REGISTER);

        if (userCourse != null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("course.already.bought"));
        }

        if (course.getPrice() > 0) {
            if (user.getBalance() < course.getPrice()) {
                return ResponseResult.newErrorInstance(ResponseCode.BALANCE, this.getMessage("balance.not.enough"));
            } else {
                Transaction transaction = new Transaction();
                transaction.setObjectId(course.getId());
                transaction.setTransContent("Mua khoá học [" + course.getName() + "] mã khoá học [" + course.getCode() + "]");
                transaction.setTransType(TransactionType.COURSE);
                transaction.setUserIdRef(user.getId());
                transactionService.processDeduction(null, course.getPrice(), transaction);
            }
        }

        userCourse = new UserCourse();
        userCourse.setAction(UserCourseAction.REGISTER);
        userCourse.setUserIdRef(user.getId());
        userCourse.setCourseIdRef(courseId);
        userCourseRepository.save(userCourse);

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult getCourseDetail(String courseId) {
        Course course = courseRepository.findFirstByIdAndDeletedIsFalseAndEnableIsTrue(courseId);
        if (course == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("course.not.found"));
        }

        User user = getUserRequesting();

        List<HashMap> response = new ArrayList<>();
        CourseDetailDTO courseDetailDTO = new CourseDetailDTO(course, null);

        //Teacher
        Teacher teacher = teacherRepository.findOne(course.getTeacherId());
        if (teacher != null) {
            courseDetailDTO.setTeacher(this.getTeacherDTO(course.getTeacherId()));
        }

        //Rate
        try {
            courseDetailDTO.setVotes(userCourseRepository.getRatingCourseCount(courseId));
            courseDetailDTO.setIsRated(userCourseRepository.findFirstByUserIdRefAndCourseIdRefAndAction(user.getId(), courseId, UserCourseAction.RATE) != null);
        } catch (Exception e) {
            logger.error("coursedetail error: ", e);
        }

        //Like
        try {
            courseDetailDTO.setLikeCount(userCourseRepository.countByCourseIdRefAndAction(courseId, UserCourseAction.LIKE));
            courseDetailDTO.setIsLiked(userCourseRepository.findFirstByUserIdRefAndCourseIdRefAndAction(user.getId(), courseId, UserCourseAction.LIKE) != null);
        } catch (Exception e) {
            logger.error("coursedetail error: ", e);
        }

        //Bookmark
        try {
            Bookmark bookmark = bookmarkRepository.findOneByUserIdAndObjectIdAndBookmarkType(user.getId(), courseId, BookmarkType.COURSE);
            courseDetailDTO.setIsBookmarked(bookmark != null);
            courseDetailDTO.setBookmarkCount(bookmarkRepository.countByObjectIdAndBookmarkType(courseId, BookmarkType.COURSE));
        } catch (Exception e) {
            logger.error("coursedetail error: ", e);
        }

        //User Buy or not
        courseDetailDTO.setRegistered(this.isUserRegisteredCourse(course));

        return ResponseResult.newSuccessInstance(courseDetailDTO);
    }

    @Override
    public ResponseResult rateCourse(String courseId, double rate) {

        Course course = courseRepository.findFirstByIdAndDeletedIsFalseAndEnableIsTrue(courseId);
        if (course == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("course.not.found"));
        }

        UserCourse userCourse = userCourseRepository.
                findFirstByUserIdRefAndCourseIdRefAndAction(this.getUserRequesting().getId(), courseId, UserCourseAction.RATE);
        if (userCourse == null) {
            userCourse = new UserCourse();
        }
        userCourse.setUserIdRef(this.getUserRequesting().getId());
        userCourse.setCourseIdRef(courseId);
        userCourse.setAction(UserCourseAction.RATE);
        userCourse.setExtendInfo(rate);
        userCourseRepository.save(userCourse);

        try {
            Iterable<DBObject>  totalRates = userCourseRepository.getRatingCourseCount(courseId);

            Double sum = 0.0;
            Double count = 0.0;
            for (DBObject dbObject: totalRates) {
                Map map = dbObject.toMap();
                Double c = Double.valueOf(String.valueOf(map.get("total")));
                sum += (Double.valueOf(String.valueOf(map.get("rate"))) * c);
                count += c;
            }
            course.setAverageRating(sum / count);
            course.setNumVotes(count.longValue());
            courseRepository.save(course);
        } catch (Exception e) {
            logger.error("caculate rating error", e);
        }

        courseRepository.save(course);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult likeCourse(String courseId) {
        UserCourse userCourse = userCourseRepository.
                findFirstByUserIdRefAndCourseIdRefAndAction(this.getUserRequesting().getId(), courseId, UserCourseAction.LIKE);
        if (userCourse != null) {
            return ResponseResult.newSuccessInstance(null);
        }
        userCourse = new UserCourse();
        userCourse.setUserIdRef(this.getUserRequesting().getId());
        userCourse.setCourseIdRef(courseId);
        userCourse.setAction(UserCourseAction.LIKE);
        userCourseRepository.save(userCourse);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult getLectureDetail(String lectureId) {
        Lecture lecture = lectureRepository.findOne(lectureId);
        if (lecture == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("lecture.not.found"));
        }

        if (!lecture.getIsFree()) {
            boolean isRegistered;
            Section section = sectionRepository.findOne(lecture.getSectionId());
            Course course = courseRepository.findOne(section.getCourseId());
            isRegistered = this.isUserRegisteredCourse(course);
            if (!isRegistered && course.getPrice() > 0) {
                //If the course is not free
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("lecture.not.accessable"));
            }
        }
        LectureDetailDTO lectureDetailDTO = new LectureDetailDTO(lecture);

        List<QuestionInLectureDTO> questionInLectureDTOS = new ArrayList<>();
        if (lecture.getQuestionInVideoList() != null) {
            for (QuestionInVideo questionInVideo: lecture.getQuestionInVideoList()){
                Question question = questionRepository.findOne(questionInVideo.getQuestionId());

                QuestionInLectureDTO questionInLectureDTO = QuestionInLectureDTO.with(question);

                questionInLectureDTO.setTime(questionInVideo.getPositionInSecond());

                questionInLectureDTOS.add(questionInLectureDTO);
            }
        }

        lectureDetailDTO.setQuestions(questionInLectureDTOS);

        List<ExamItemDTO> examItemDTOS = new ArrayList<>();
        if (lecture.getExamList() != null) {
            for (ExamIn examIn: lecture.getExamList()) {
                Exam exam = examRepository.findOne(examIn.getExamId());
                examItemDTOS.add(ExamItemDTO.with(exam));
            }
        }

        Bookmark bookmark = bookmarkRepository.findOneByUserIdAndObjectIdAndBookmarkType(getUserRequesting().getId(), lecture.getId(), BookmarkType.COURSE_LECTURE);
        lectureDetailDTO.setIsBookmarked(bookmark != null);
        lectureDetailDTO.setExams(examItemDTOS);

        return ResponseResult.newSuccessInstance(lectureDetailDTO);
    }

    private boolean isUserRegisteredCourse(Course course) {

        User user = this.getUserRequesting();

        UserCourse userCourse = userCourseRepository.findFirstByUserIdRefAndCourseIdRefAndAction(user.getId(), course.getId(), UserCourseAction.REGISTER);

        if (userCourse == null) {
            return false;
        }

        return true;
    }

    private TeacherDTO getTeacherDTO(String teacherId) {
        Teacher teacher = teacherRepository.findOne(teacherId);
        if (teacher == null) {
            return null;
        }
        TeacherDTO teacherDTO = new TeacherDTO(teacher);
        List<Course> courses = courseRepository.findAllByTeacherId(teacherId);
        teacherDTO.setCourseCount((courses == null) ? 0 : courses.size());
        long sumRegister = 0;
        for (Course course: courses) {
            sumRegister += userCourseRepository.countByCourseIdRefAndAction(course.getId(), UserCourseAction.REGISTER);
        }
        teacherDTO.setStudentCount(sumRegister);
        return teacherDTO;
    }

    private List<CourseItemDTO> getCourseItemDTOList(List<Course> courseList){
        List<CourseItemDTO> courseItemDTOList = new LinkedList<>();

        if(courseList!=null){
            courseList.stream().forEach(course -> {
                CourseItemDTO courseItemDTO = new CourseItemDTO(course);

                AtomicLong lectureQuantity = new AtomicLong();
                List<Section> sectionList = sectionRepository.findByCourseIdIsAndDeletedIsOrderByPositionAsc(course.getId(),false);
                if(sectionList!=null){
                    sectionList.parallelStream().forEach(section -> {
                        long count = lectureRepository.countBySectionIdIs(section.getId());
                        lectureQuantity.addAndGet(count);
                    });
                }
                courseItemDTO.setLectureQuantity(lectureQuantity.get());
                courseItemDTOList.add(courseItemDTO);
            });
//            newestCourseRedisRepository.processPutAllCourse(courseItemDTOList);
        }

        return courseItemDTOList;
    }

    @Override
    public LectureDTO getLectureDTO(Lecture lecture) {
        LectureDTO lectureDTO = new LectureDTO(lecture);
        List<String> idList = lecture.getExamList().stream().map(ExamIn::getExamId).collect(Collectors.toList());
        lectureDTO.setExamSubmittedCount((int) userExamRepository.countByUserIdRefAndExamIdRefIn(getUserRequesting().getId(), idList));

        //Purchased??
        if (lectureDTO.getExamSubmittedCount() > 0) {
            lectureDTO.setIsPurchased(true);
        } else {
            Section section = sectionRepository.findOne(lecture.getSectionId());
            if (section != null) {
                Course course = courseRepository.findOne(section.getCourseId());
                lectureDTO.setIsPurchased(course != null && isUserRegisteredCourse(course));
            }
        }

        return lectureDTO;
    }

    @Override
    public CourseItemDTO getCourseItemDTO(Course course) {
        if (course != null) {
            CourseItemDTO courseItemDTO = new CourseItemDTO(course);
            courseItemDTO.setIsPurchased(this.isUserRegisteredCourse(course));

            //Rate
            try {
                courseItemDTO.setNumVotes(userCourseRepository.countByCourseIdRefAndAction(course.getId(), UserCourseAction.RATE));
            } catch (Exception e) {
                logger.error("coursedetail error: ", e);
            }
            return courseItemDTO;
        } else {
            return new CourseItemDTO();
        }
    }
}
