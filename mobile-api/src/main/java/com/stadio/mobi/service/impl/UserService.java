package com.stadio.mobi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.common.custom.RequestHandler;
import com.stadio.common.define.Constant;
import com.stadio.common.request.SSORequest;
import com.stadio.common.service.PasswordService;
import com.stadio.common.utils.FileUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.dtos.AchievementItemDTO;
import com.stadio.mobi.dtos.UserAchievementDTO;
import com.stadio.mobi.dtos.user.UserForgotPassDTO;
import com.stadio.mobi.dtos.user.UserProfileDTO;
import com.stadio.mobi.dtos.user.UserChangePassDTO;
import com.stadio.mobi.dtos.user.UserUpdateProfileDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IConfigService;
import com.stadio.mobi.service.ICourseService;
import com.stadio.mobi.service.IUserService;
import com.stadio.mobi.validation.UserValidation;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.cms.LectureItemDTO;
import com.stadio.model.dtos.mobility.BookmarkDetailDTO;
import com.stadio.model.dtos.mobility.CourseItemDTO;
import com.stadio.model.dtos.mobility.ExamHistoryDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.dtos.mobility.course.LectureDTO;
import com.stadio.model.enu.BookmarkType;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.enu.UserCourseAction;
import com.stadio.model.model.ExamIn;
import com.stadio.model.repository.main.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.FilePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Andy on 01/19/2018.
 */
@Service
public class UserService extends BaseService implements IUserService
{

    @Autowired
    ExamRepository examRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired UserValidation validation;

    @Autowired UserExamRepository userExamRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired ClazzRepository clazzRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    IConfigService configService;

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    UserAchievementRepository userAchievementRepository;

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    ICourseService courseService;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Value("${app.auth-server.host}")
    private String authServer;

    @Value("${domain.mediation}")
    private String domainMedia;

    private static Logger logger = LogManager.getLogger(UserService.class);

    @Override
    public ResponseResult processGetProfile(String accessToken)
    {
        User user = this.getUserRequesting();
        if (user == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"));
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO(user, clazzRepository);
        return ResponseResult.newSuccessInstance(userProfileDTO);
    }

    @Override
    public ResponseResult processUpdateProfile(UserUpdateProfileDTO userProfileDTO) {
        User user = this.getUserRequesting();
        if (user == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"));
        }
        user.setUpdatedDate(new Date());
        if (StringUtils.isNotNull(userProfileDTO.getFullname()))
        {
            user.setFullName(userProfileDTO.getFullname());
        }
        if (StringUtils.isNotNull(userProfileDTO.getClazzId()))
        {
            Clazz clazz = clazzRepository.findOneById(userProfileDTO.getClazzId());

            if (clazz != null){
                user.setClazzId(userProfileDTO.getClazzId());
            }else {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.invalid.clazzId"));
            }
        }

        if (StringUtils.isNotNull(userProfileDTO.getPhone()))
        {
            if (StringUtils.isPhoneNumber(userProfileDTO.getPhone()))
            {
                if (user.getPhoneComfirmed() && !userProfileDTO.getPhone().equals(user.getPhone())) {
                    return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("user.comfirmed.phone"), null);
                }else{
                    user.setPhone(userProfileDTO.getPhone());
                }
            }
            else
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("user.invalid.phone"), null);
            }
        }

        if (userProfileDTO.getBirthday() != null)
        {
            SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
            try
            {
                user.setBirthDay(fm.parse(userProfileDTO.getBirthday()));
            }
            catch (Exception e)
            {
                logger.error("Parse birthday exception: ", e);
            }

        }

        if (StringUtils.isNotNull(userProfileDTO.getEmail()))
        {
            if (StringUtils.isValidEmailAddress(userProfileDTO.getEmail()))
            {
                if (!userProfileDTO.getEmail().equals(user.getEmail())) {
                    User tempUser = userRepository.findFirstByEmail(userProfileDTO.getEmail());
                    if (tempUser != null)
                    {
                        return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("user.existed.email"), null);
                    }
                    user.setEmail(userProfileDTO.getEmail());
                }
            }
            else
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("user.invalid.email"), null);
            }
        }

        if (StringUtils.isNotNull(userProfileDTO.getAddress()))
        {
            user.setAddress(userProfileDTO.getAddress());
        }


        if (StringUtils.isNotNull(userProfileDTO.getIdNumber()) &&
                userProfileDTO.getIdIssueDate() == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("user.invalid.IdIssueDate"), null);
        }

        if (!StringUtils.isNotNull(userProfileDTO.getIdNumber()) &&
                userProfileDTO.getIdIssueDate() != null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("user.invalid.IdNumber"), null);
        }

        if (StringUtils.isNotNull(userProfileDTO.getIdNumber()) &&
                userProfileDTO.getIdIssueDate() != null)
        {
            user.setIdNumber(userProfileDTO.getIdNumber());
            user.setIdIssueDate(userProfileDTO.getIdIssueDate());
        }
        userRepository.save(user);
        UserProfileDTO userProfile = new UserProfileDTO(user, clazzRepository);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("success"), userProfile);
    }

    @Override
    public ResponseResult processUpdateAvatar(MultipartFile img) {
        try {
            User user = this.getUserRequesting();
            if (user == null) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"));
            }
            String url = this.domainMedia + "/api/media/upload-image";
            AsyncHttpClient c = Dsl.asyncHttpClient();
            RequestBuilder requestBuilder = new RequestBuilder();
            requestBuilder.setHeader("Authorization",this.requestHandler.getToken());
            requestBuilder.setUrl(url);
            requestBuilder.setMethod("POST");
            FilePart part;
            try {
                part = new FilePart("file", FileUtils.multipartToFile(img,"file"));
            } catch (Exception e) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("request.multipart.error"));
            }
            requestBuilder.addBodyPart(part);
            String avatarUrl = null;
            Future<Response> whenResponse = c.prepareRequest(requestBuilder).execute();
            try {
                Response response = whenResponse.get();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode data = objectMapper.readTree(response.getResponseBody());

                JsonNode imageData = data.get("data");
                avatarUrl = imageData.get("imageUrl").textValue();
            } catch (Exception e) {
                logger.error("upload avatar error", e);
                return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("request.multipart.error"));
            } finally {
                try {
                    c.close();
                } catch (Exception e){}
            }
            if (StringUtils.isNotNull(avatarUrl)) {
                user.setAvatar(avatarUrl);
                user.setUpdatedDate(new Date());
                userRepository.saveUpdate(user);

                return ResponseResult.newSuccessInstance(new UserProfileDTO(user,clazzRepository));
            } else {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("request.multipart.error"));
            }
        } catch (Exception e) {
            return ResponseResult.newErrorInstance(ResponseCode.MULTIPART_EXCEPTION,e.toString());
        }
    }

    @Override
    public ResponseResult processAddToBookmark(String accessToken, String examId, String categoryId) {
        Bookmark bookmark = new Bookmark();
        User user = this.getUserRequesting();

        if (StringUtils.isNotNull(examId)){
            Exam exam = examRepository.findOne(examId);
            if (exam == null)
            {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.bookmark.notfound"));
            }

            if (bookmarkRepository.findOneByUserIdAndExamId(user.getId(), examId) != null){
                return ResponseResult.newSuccessInstance(bookmark);
            }

            bookmark.setExamId(exam.getId());
            bookmark.setName(exam.getName());

            //new logic
            bookmark.setObjectId(exam.getId());
            bookmark.setBookmarkType(BookmarkType.EXAM);
        } else if (StringUtils.isNotNull(categoryId)) {
            Category category = categoryRepository.findOne(categoryId);
            if (category == null) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.bookmark.notfound"));
            }

            if (bookmarkRepository.findOneByUserIdAndCategoryId(user.getId(), categoryId) != null){
                return ResponseResult.newSuccessInstance(bookmark);
            }

            bookmark.setCategoryId(categoryId);
            bookmark.setName(category.getName());
            //new logic
            bookmark.setObjectId(categoryId);
            bookmark.setBookmarkType(BookmarkType.EXAM_CATEGORY);
        }else{
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.bookmark.notfound"));
        }

        bookmark.setUserId(user.getId());
        bookmarkRepository.save(bookmark);

        return ResponseResult.newSuccessInstance(bookmark);
    }

    private ResponseResult addBookmark(String objectId, String objectType) {
        Bookmark bookmark = new Bookmark();
        User user = getUserRequesting();

        BookmarkType type = BookmarkType.valueOf(objectType);

        if (BookmarkType.EXAM == type) {
            Exam exam = examRepository.findOne(objectId);
            if (exam == null) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.bookmark.notfound"));
            }
            bookmark.setName(exam.getName());
        } else if (BookmarkType.EXAM_CATEGORY == type) {
            Category category = categoryRepository.findOne(objectId);
            if (category == null) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.bookmark.notfound"));
            }
            bookmark.setName(category.getName());
        } else if (BookmarkType.COURSE == type) {
            Course course = courseRepository.findFirstByIdAndDeletedIsFalseAndEnableIsTrue(objectId);
            if (course == null) {
                return ResponseResult.newErrorDefaultInstance(getMessage("course.not.found"));
            }
            bookmark.setName(course.getName());
        } else if (BookmarkType.COURSE_LECTURE == type) {
            Lecture lecture = lectureRepository.findOne(objectId);
            if (lecture == null) {
                return ResponseResult.newErrorDefaultInstance(getMessage("lecture.not.found"));
            }
            bookmark.setName(lecture.getName());
        } else {
            return ResponseResult.newErrorDefaultInstance(getMessage("user.bookmark.incorrect.object.type"));
        }

        if (bookmarkRepository.findOneByUserIdAndObjectIdAndBookmarkType(
                user.getId(), objectId, type) != null) {
            return ResponseResult.newSuccessInstance(bookmark);
        }

        bookmark.setUserId(user.getId());
        bookmark.setBookmarkType(type);
        bookmark.setObjectId(objectId);
        bookmarkRepository.save(bookmark);
        return ResponseResult.newSuccessInstance(bookmark);
    }

    @Override
    public ResponseResult processAddToBookmarkV2(String objectId, String objectType) {
        boolean isCorrectType = false;
        for (BookmarkType bookmarkType: BookmarkType.values()) {
            if (objectType.equals(bookmarkType.name())) {
                isCorrectType = true;
            }
        }
        if (!isCorrectType) {
            return ResponseResult.newErrorDefaultInstance(getMessage("user.bookmark.incorrect.object.type"));
        }
        return this.addBookmark(objectId, objectType);
    }

    @Override
    public ResponseResult processRemoveBookmark(String accessToken, String examId, String categoryId) {

        if (!StringUtils.isNotNull(examId) && !StringUtils.isNotNull(categoryId))
        {
            return ResponseResult.newErrorInstance("01", getMessage("user.bookmark.notfound"));
        }

        User user = this.getUserRequesting();

        if (StringUtils.isNotNull(examId)){

            Bookmark bookmark = bookmarkRepository.findOneByUserIdAndExamId(user.getId(), examId);

            if (bookmark != null){
                bookmarkRepository.delete(bookmark);
            }
        }else{
            Bookmark bookmark = bookmarkRepository.findOneByUserIdAndCategoryId(user.getId(), categoryId);

            if (bookmark != null){
                bookmarkRepository.delete(bookmark);
            }
        }
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processRemoveBookmarkV2(String objectId, String objectType) {
        boolean isCorrectType = false;
        for (BookmarkType bookmarkType: BookmarkType.values()) {
            if (objectType.equals(bookmarkType.name())) {
                isCorrectType = true;
            }
        }
        if (!isCorrectType) {
            return ResponseResult.newErrorDefaultInstance(getMessage("user.bookmark.incorrect.object.type"));
        }
        Bookmark bookmark = bookmarkRepository.findOneByUserIdAndObjectIdAndBookmarkType(
                getUserRequesting().getId(), objectId, BookmarkType.valueOf(objectType)
        );
        if (bookmark != null){
            bookmarkRepository.delete(bookmark);
        }

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processGetListBought(String accessToken)
    {
        User user = this.getUserRequesting();

        List<Transaction> transactionList = transactionRepository.findAllByUserIdRefAndTransTypeOrderByCreatedDateDesc(user.getId(), TransactionType.EXAM);

        List<ExamItemDTO> examItemDTOList = new ArrayList<>();

        for (Transaction transaction: transactionList){
            Exam exam = examRepository.findOne(transaction.getObjectId());
            ExamItemDTO examItemDTO = ExamItemDTO.with(exam);
            examItemDTOList.add(examItemDTO);
        }

        List<UserCourse> userCourseList = userCourseRepository.findAllByUserIdRefAndActionOrderByCreatedDateDesc(user.getId(), UserCourseAction.REGISTER);

        List<CourseItemDTO> courseItemDTOList = new ArrayList<>();

        for (UserCourse userCourse: userCourseList) {
            Course course = courseRepository.findOne(userCourse.getCourseIdRef());
            CourseItemDTO courseItemDTO = new CourseItemDTO(course);
            courseItemDTO.setIsPurchased(true);
            courseItemDTOList.add(courseItemDTO);
        }

        Map<String, Object> map = new HashMap<>();

        map.put("exams", examItemDTOList);
        map.put("courses", courseItemDTOList);

        return ResponseResult.newSuccessInstance(map);
    }

    @Override
    public ResponseResult processGetListBookmark(String accessToken)
    {
        User user = this.getUserRequesting();
        PageRequest request = new PageRequest(1, Integer.MAX_VALUE);
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByUserIdOrderByCreatedDateDesc(user.getId(), request);
        List<Bookmark> bookmarkList = new ArrayList<>();
        if (bookmarks != null && bookmarks.size() != 0){
            for (Bookmark bookmark: bookmarks){
                if (StringUtils.isNotNull(bookmark.getCategoryId())){
                    Category category = categoryRepository.findOne(bookmark.getCategoryId());
                    if (category != null) {
                        bookmarkList.add(bookmark);
                    }else{
                        bookmarkRepository.delete(bookmark.getId());
                    }
                }else if (StringUtils.isNotNull(bookmark.getExamId())){
                    Exam exam = examRepository.findOne(bookmark.getExamId());
                    if (exam != null && exam.isEnable() && !exam.isDeleted()) {
                        bookmarkList.add(bookmark);
                    }else{
                        bookmarkRepository.delete(bookmark.getId());
                    }
                }
            }
        }

        return ResponseResult.newSuccessInstance(bookmarkList);
    }

    @Override
    public ResponseResult processGetListBookmarkV2(int page, int pageSize)
    {
        User user = this.getUserRequesting();

        if (page <= 0) {
            page = 1;
        }

        PageRequest request = new PageRequest(page - 1, 10);

        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByUserIdOrderByCreatedDateDesc(user.getId(), request);
        List<BookmarkDetailDTO> bookmarkList = new ArrayList<>();
        if (bookmarks != null && bookmarks.size() != 0){
            for (Bookmark bookmark: bookmarks){
                try {
                    if (BookmarkType.EXAM_CATEGORY == bookmark.getBookmarkType()){
                        Category category = categoryRepository.findOne(bookmark.getObjectId());
                        if (category != null) {
                            bookmarkList.add(new BookmarkDetailDTO(bookmark));
                        } else {
                            bookmarkRepository.delete(bookmark.getId());
                        }
                    } else if (BookmarkType.EXAM == bookmark.getBookmarkType()){
                        Exam exam = examRepository.findOne(bookmark.getObjectId());
                        if (exam != null && exam.isEnable() && !exam.isDeleted()) {
                            BookmarkDetailDTO bookmarkDetailDTO = new BookmarkDetailDTO(bookmark);

                            ExamItemDTO examItemDTO = ExamItemDTO.with(exam);

                            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examItemDTO.getId());

                            examItemDTO.setDidDone(userExam != null);

                            if (userExam != null && examItemDTO.getPrice() > 0) {
                                examItemDTO.setPrice(-1);
                            }

                            bookmarkDetailDTO.setBookmarkDetail(examItemDTO);
                            bookmarkList.add(bookmarkDetailDTO);
                        }else{
                            bookmarkRepository.delete(bookmark.getId());
                        }
                    } else if (BookmarkType.COURSE == bookmark.getBookmarkType()){
                        Course course = courseRepository.findFirstByIdAndDeletedIsFalseAndEnableIsTrue(bookmark.getObjectId());
                        if (course != null) {
                            BookmarkDetailDTO bookmarkDetailDTO = new BookmarkDetailDTO(bookmark);
                            //Logic
                            CourseItemDTO courseItemDTO = courseService.getCourseItemDTO(course);
                            bookmarkDetailDTO.setBookmarkDetail(courseItemDTO);

                            bookmarkList.add(bookmarkDetailDTO);
                        } else {
                            bookmarkRepository.delete(bookmark.getId());
                        }
                    } else if (BookmarkType.COURSE_LECTURE == bookmark.getBookmarkType()) {
                        Lecture lecture = lectureRepository.findOne(bookmark.getObjectId());
                        if (lecture != null) {
                            BookmarkDetailDTO bookmarkDetailDTO = new BookmarkDetailDTO(bookmark);
                            bookmarkDetailDTO.setBookmarkDetail(courseService.getLectureDTO(lecture));
                            bookmarkList.add(bookmarkDetailDTO);
                        } else {
                            bookmarkRepository.delete(bookmark.getId());
                        }
                    }
                } catch (Exception e) {
                    logger.error("error bookmarktype,id: " + bookmark.getId(),e);
                }
            }
        }

        return ResponseResult.newSuccessInstance(bookmarkList);
    }

    @Override
    public ResponseResult processGetHistory(String accessToken)
    {
        User user = this.getUserRequesting();
        List<UserExam> userExamList = userExamRepository.findByUserIdRefOrderByCreatedDateDesc(user.getId());

        List<ExamHistoryDTO> examHistoryDTOList = new ArrayList<>();
        userExamList.forEach(userExam ->
        {
            Exam exam = examRepository.findOne(userExam.getExamIdRef());
            if (exam != null)
            {
                ExamHistoryDTO examHistoryDTO = ExamHistoryDTO.with(exam);
                examHistoryDTO.setQuantity(userExam.getTotal());
                examHistoryDTO.setCorrectNumber(userExam.getCorrectNumber());
                examHistoryDTO.setCreatedDate(userExam.getCreatedDate());
                examHistoryDTO.setDuration(userExam.getDuration());
                examHistoryDTOList.add(examHistoryDTO);
            }
        });
        return ResponseResult.newSuccessInstance(examHistoryDTOList);
    }

    @Override
    public ResponseResult processLogout() {
        User user = this.getUserRequesting();
        if (user != null) {
            List<Device> deviceList = deviceRepository.findDeviceByUserId(user.getId());
            if (deviceList != null && deviceList.isEmpty()) {
                for (Device device: deviceList) {
                    device.setUserId(null);
                    deviceRepository.save(device);
                }
            }
        }
        //Revoke token

        SSORequest ssoRequest = new SSORequest();
        try {
            ssoRequest.revokeToken(this.authServer ,this.requestHandler.getToken(),null);
        } catch (Exception e) {
            logger.error("Request sso revoke error: " + e.getMessage());
        }
        return ResponseResult.newSuccessInstance(getMessage("success"));
    }

    @Override
    public ResponseResult processLockUser() {
        this.processLogout();
        User user = this.getUserRequesting();
        user.setEnabled(false);
        userRepository.saveUpdate(user);
        return null;
    }

    @Override
    public ResponseResult processGetTransactionHistory(int page, int limit, String accessToken)
    {
        User user = this.getUserRequesting();
        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "created_time"));
        List<Transaction> transaction = transactionRepository.findByLastest(user.getId(), request).getContent();
        return ResponseResult.newSuccessInstance(transaction);
    }

    @Override
    public ResponseResult processGetAchievements(String accessToken) {

        User user = this.getUserRequesting();

        List<UserAchievementDTO> userAchievementDTOList = new ArrayList<>();
        if (user != null) {
            List<UserAchievement> userAchievementList = userAchievementRepository.findByUserId(user.getId());
            userAchievementList.forEach(userAchievement -> {
                UserAchievementDTO userAchievementDTO = new UserAchievementDTO();
                userAchievementDTO.setId(userAchievement.getId());
                userAchievementDTO.setCreatedDate(userAchievement.getCreatedDate());

                Achievement achievement = achievementRepository.findOne(userAchievement.getAchievementId());
                if (achievement != null){
                    userAchievementDTO.setAchievement(new AchievementItemDTO(achievement));
                }

                userAchievementDTOList.add(userAchievementDTO);
            });
        }
        return ResponseResult.newSuccessInstance(userAchievementDTOList);
    }
}
