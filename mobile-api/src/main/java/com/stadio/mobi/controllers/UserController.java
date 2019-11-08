package com.stadio.mobi.controllers;

import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.dtos.user.UserForgotPassDTO;
import com.stadio.mobi.dtos.user.UserChangePassDTO;
import com.stadio.mobi.dtos.user.UserUpdateProfileDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * Created by Andy on 01/14/2018.
 */
@RestController
@RequestMapping(value = "api/user")
public class UserController extends BaseController
{
    @Autowired
    IUserService userService;
//
//    @PostMapping(value = "/register")
//    public ResponseEntity userRegister(
//            @RequestParam(value = "phone", required = false) String phone,
//            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "password") String password)
//    {
//        ResponseResult result = userService.processRegisterUser(phone, email, password);
//        return ResponseEntity.ok(result);
//    }

    @GetMapping(value = "/profile")
    public ResponseEntity getUserProfile(
            @RequestHeader(value = "Authorization") String accessToken)
    {
        ResponseResult result = userService.processGetProfile(accessToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/updateProfile")
    public ResponseEntity updateProfile(@Valid @RequestBody UserUpdateProfileDTO userUpdateProfileDTO,
                                        BindingResult result)
    {
        if(result.hasErrors()) {
            return ResponseEntity.ok(ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, getMessage("request.error.param")));
        }
        return ResponseEntity.ok(userService.processUpdateProfile(userUpdateProfileDTO));
    }

    @PostMapping("/updateAvatar")
    public ResponseEntity changeAvatar(
            @RequestParam(value = "img", required = true) MultipartFile file) {
        return ResponseEntity.ok(this.userService.processUpdateAvatar(file));
    }

    @GetMapping(value = "/history")
    public ResponseEntity getExamHistory(
            @RequestHeader(value = "Authorization") String accessToken)
    {
        ResponseResult result = userService.processGetHistory(accessToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/addToBookmark")
    public ResponseEntity actionBookmark(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestParam(value = "examId", required = false) String examId,
            @RequestParam(value = "categoryId", required = false) String categoryId )
    {
        ResponseResult result = userService.processAddToBookmark(accessToken, examId, categoryId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/addToBookmarkV2")
    public ResponseEntity actionBookmarkV2(
            @RequestParam(value = "objectId") String objectId,
            @RequestParam(value = "objectType") String objectType)
    {
        ResponseResult result = userService.processAddToBookmarkV2(objectId, objectType);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/removeBookmark")
    public ResponseEntity actionRemoveBookmark(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestParam(value = "examId", required = false) String examId,
            @RequestParam(value = "categoryId", required = false) String categoryId )
    {
        ResponseResult result = userService.processRemoveBookmark(accessToken, examId, categoryId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/removeBookmarkV2")
    public ResponseEntity actionRemoveBookmark(
            @RequestParam(value = "objectId") String objectId,
            @RequestParam(value = "objectType") String objectType)
    {
        ResponseResult result = userService.processRemoveBookmarkV2(objectId, objectType);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/bought")
    public ResponseEntity getListBought(
            @RequestHeader(value = "Authorization") String accessToken
    ) {
        ResponseResult result = userService.processGetListBought(accessToken);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/listBookmarks")
    public ResponseEntity getListBookmarks(
            @RequestHeader(value = "Authorization") String accessToken
    ) {
        ResponseResult result = userService.processGetListBookmark(accessToken);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/listBookmarksV2")
    public ResponseEntity getListBookmarks(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "20", required = false) int limit
    ) {
        ResponseResult result = userService.processGetListBookmarkV2(page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/transactionHistory")
    public ResponseEntity transactionHistory(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit
    )
    {
        ResponseResult result = userService.processGetTransactionHistory(page, limit, accessToken);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/achievements")
    public ResponseEntity getAchievements(
            @RequestHeader(value = "Authorization") String accessToken
    )
    {
        ResponseResult result = userService.processGetAchievements(accessToken);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout() {
        ResponseResult result = userService.processLogout();
        return ResponseEntity.ok(result);
    }
}
