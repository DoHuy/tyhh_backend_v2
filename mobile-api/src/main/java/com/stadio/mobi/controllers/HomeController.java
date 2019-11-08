package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 01/13/2018.
 */
@RestController
@RequestMapping(value = "api/home")
public class HomeController extends BaseController
{

    @Autowired IPracticeService practiceService;

    @Autowired IExamService examService;

    @Autowired IBannerService bannerService;

    @Autowired
    IHotExamService hotExamService;

    @Autowired
    IPopupNewsService popupNewsService;

    @Autowired ICourseService courseService;

    @GetMapping("listPopupNews")
    public ResponseResult getListPopupNews() {
        return this.popupNewsService.getListPopupNews();
    }

    @GetMapping(value = "/banners")
    public ResponseResult getBanners()
    {
        return bannerService.processGetBannersForMobility();
    }

    @GetMapping(value = "/practice")
    public ResponseResult getPractice()
    {
        return practiceService.processGetPractice();
    }

    @GetMapping(value = "/hotExams")
    public ResponseResult getHotExams(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "topType", required = false, defaultValue = "") String topType
    )
    {
        return hotExamService.processGetHotExams(token, topType);
    }

    @GetMapping(value = "/highlight")
    public ResponseResult getHighlight(
            @RequestHeader(value = "Authorization", required = false) String token
    )
    {
        return examService.processGetHighlight(token);
    }

    @GetMapping(value = "/recommend")
    public ResponseResult getRecommend(
            @RequestHeader(value = "Authorization", required = false) String token)
    {
        return examService.processGetRecommend(token);
    }

    @GetMapping(value = "/course-newest")
    public ResponseResult getCourseNewest(){
        return courseService.processGetNewestCourseFromMongo();
    }

    @GetMapping(value = "/course-newest-list")
    public ResponseResult getCourseNewestList(){
        return courseService.processGetNewestListCourse();
    }
}
