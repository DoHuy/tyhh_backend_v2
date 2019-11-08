package com.stadio.cms.controllers.users;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.user.MakeUserTransactionDTO;
import com.stadio.cms.dtos.user.UserProfileDTO;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IUserService;
import com.stadio.model.dtos.cms.UserSearchFormDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Andy on 11/10/2017.
 */
@RestController
@RequestMapping(value = "api/user", name = "Quản lý Người dùng")
public class UserController extends BaseController {
    @Autowired
    IUserService userService;

    @PostMapping
    public ResponseResult createUser(HttpServletRequest request) {
        return ResponseResult.newInstance(ResponseCode.SUCCESS, "Đã Tạo các user sau", this.getCurrentUserManager());
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, name = "Tìm kiếm người dùng")
    public ResponseResult search(HttpServletRequest request,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
                                 @RequestBody UserSearchFormDTO userSearchFormDTO) {
        return userService.processSearchUser(userSearchFormDTO, page, pageSize, this.requestURI(request));
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST, name = "Thông tin người dùng")
    public ResponseResult profile(@RequestParam(value = "id") String id) {
        return userService.getProfileByUserId(id);
    }

    @RequestMapping(value = "/profile/paymentHistory", method = RequestMethod.POST, name = "Lịch sử thanh toán")
    public ResponseResult profilePayment(@RequestParam(value = "id") String id) {
        return userService.getProfileByUserId(id);
    }

    @RequestMapping(value = "/profile/submitExamHistory", method = RequestMethod.POST, name = "Lịch sử làm bài")
    public ResponseResult profileExam(@RequestParam(value = "id") String id) {
        return userService.getProfileByUserId(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhập thông tin người dùng")
    public ResponseResult profile(@RequestBody UserProfileDTO userProfile) {
        return userService.updateProfile(userProfile);
    }

    @RequestMapping(value = "/lock", method = RequestMethod.POST, name = "Khóa người dùng")
    public ResponseResult lockUser(@RequestParam String id) {
        return userService.lockUser(id);
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST, name = "Mở khóa người dùng")
    public ResponseResult unlockUser(@RequestParam String id) {
        return userService.unlockUser(id);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, name = "Thiết lập lại mật khẩu")
    public ResponseResult resetPassword(@RequestParam String id) {
        return userService.resetPassword(id);
    }

    @RequestMapping(value = "/paymentHistory", method = RequestMethod.GET, name = "Lịch sử thanh toán")
    public ResponseResult profileExam(@RequestParam(value = "id") String id,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize) {
        return userService.getPaymentHistories(id, page, pageSize);
    }

    @RequestMapping(value = "/makePayment", method = RequestMethod.POST, name = "Nạp tiền vào tài khoản")
    public ResponseResult profileExam(@Valid @RequestBody MakeUserTransactionDTO makeUserTransactionDTO,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, result.getAllErrors().toString());
        }
        return userService.makePayment(makeUserTransactionDTO);
    }
}
