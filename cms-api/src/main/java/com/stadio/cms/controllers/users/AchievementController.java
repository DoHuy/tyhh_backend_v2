package com.stadio.cms.controllers.users;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.AchievementQuery;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IAchievementService;
import com.stadio.model.dtos.cms.AchievementFormDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/achievement", name = "Quản lý thành tích")
public class AchievementController extends BaseController {

    private Logger logger = LogManager.getLogger(AchievementController.class);

    @Autowired
    IAchievementService achievementService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Tạo thành tích")
    public ResponseResult create(@RequestBody AchievementFormDTO achievementFormDTO) {
        return achievementService.processCreateOneAchievement(achievementFormDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhật thành tích")
    public ResponseResult update(@RequestBody AchievementFormDTO achievementFormDTO) {
        return achievementService.ProcessUpdateOneAchievement(achievementFormDTO);
    }

    @RequestMapping(value = "/by-id", method = RequestMethod.GET, name = "Xem thông tin thành tích")
    public ResponseResult getById(@RequestParam(value = "id") String id) {
        return achievementService.ProcessGetAchievementById(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, name = "Danh sách tất cả thanh tích")
    public ResponseResult getAll(HttpServletRequest request) {
        return achievementService.ProcessGetAllAchievement();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, name = "Xóa thành tích")
    public ResponseResult<?> delete(@RequestParam(value = "id") String id) {
        return achievementService.ProcessDeleteAchievement(id);
    }

    @RequestMapping(value = "/checkResult", method = RequestMethod.POST)
    public ResponseResult checkResult(@RequestBody AchievementQuery achievementQuery) {
        return achievementService.achievementCheckResult(achievementQuery);
    }

    @RequestMapping(value = "/user-have", method = RequestMethod.GET)
    public ResponseResult getUserHave(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "pageSize") Integer pageSize)
    {
        return achievementService.ProcessGetUserHaveAchievement(id,page,pageSize);
    }

}
