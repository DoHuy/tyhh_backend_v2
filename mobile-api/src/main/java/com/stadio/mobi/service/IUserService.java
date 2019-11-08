package com.stadio.mobi.service;

import com.stadio.mobi.dtos.user.UserForgotPassDTO;
import com.stadio.mobi.dtos.user.UserChangePassDTO;
import com.stadio.mobi.dtos.user.UserUpdateProfileDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.TransactionType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Cac dich vu lien quan toi nguoi dung
 * Created by Andy on 11/08/2017.
 */
public interface IUserService
{
    ResponseResult processGetProfile(String accessToken);

    ResponseResult processUpdateProfile(UserUpdateProfileDTO userUpdateProfileDTO);

    ResponseResult processUpdateAvatar(MultipartFile img);

    ResponseResult processAddToBookmark(String accessToken, String examId, String categoryId);

    ResponseResult processAddToBookmarkV2(String objectId, String objectType);

    ResponseResult processRemoveBookmark(String accessToken, String examId, String categoryId);

    ResponseResult processRemoveBookmarkV2(String objectId, String objectType);

    ResponseResult processGetListBought(String accessToken);

    ResponseResult processGetListBookmark(String accessToken);

    ResponseResult processGetListBookmarkV2(int page, int pageSize);

    ResponseResult processGetHistory(String accessToken);

    ResponseResult processLogout();

    ResponseResult processLockUser();

    ResponseResult processGetTransactionHistory(int page, int limit, String accessToken);

    ResponseResult processGetAchievements(String accessToken);

}
