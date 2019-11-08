package com.stadio.cms.service;

import com.stadio.cms.dtos.user.MakeUserTransactionDTO;
import com.stadio.cms.dtos.user.UserProfileDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.UserSearchFormDTO;

/**
 * Cac dich vu lien quan toi nguoi dung
 * Created by Andy on 11/08/2017.
 */
public interface IUserService {
    ResponseResult processSearchUser(UserSearchFormDTO userSearchFormDTO, Integer page, Integer pageSize, String uri);

    ResponseResult getProfileByUserId(String id);

    ResponseResult updateProfile(UserProfileDTO userProfileDTO);

    ResponseResult lockUser(String userId);

    ResponseResult unlockUser(String userId);

    ResponseResult resetPassword(String userId);

    ResponseResult getPaymentHistories(String userId, Integer page, Integer pageSize);

    ResponseResult makePayment(MakeUserTransactionDTO makeUserTransactionDTO);

}