package com.stadio.cms.controllers.users;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.UserPointFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IUserPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("api/userPoint")
public class UserPointController extends BaseController {

    @Autowired
    IUserPointService userPointService;

    @PostMapping(value = "/create", name = "Cập nhật điểm thành tích")
    public ResponseEntity createdFAQ(
            @RequestBody @Valid UserPointFormDTO userPointFormDTO) {
        ResponseResult result = userPointService.processCreateUserPoint(userPointFormDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/user", name = "Xem thành tích người dùng trong tháng")
    public ResponseEntity getUserPoint(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "month", required = false) String month) {
        ResponseResult result = userPointService.processGetUserPoint(month, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/rank", name = "Xem điểm xếp hạng theo tháng")
    public ResponseEntity getRank(@RequestParam(value = "month", required = false) String month) throws ParseException { //dd-MM-yyyy
        ResponseResult result = userPointService.processGetRank(month);
        return ResponseEntity.ok(result);
    }

}
