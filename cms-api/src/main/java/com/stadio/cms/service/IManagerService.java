package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.hoc68.users.documents.Manager;
import com.stadio.model.dtos.cms.ManagerDTO;

/**
 * Cac dich vu nguoi dung quan ly trang admin
 * Created by Andy on 11/08/2017.
 */
public interface IManagerService {

    ResponseResult processCreateNewManager(String accessToken, ManagerDTO managerDTO);

    Manager getManagerRequesting();

    ResponseResult processChangePassword(String accessToken, String oldPass, String newPass);

    ResponseResult processDeleteManager(String accessToken, String id);

    ResponseResult processGetListManager(Integer page, Integer pageSize, String uri);

    ResponseResult processGetProfileManager(String id);

    ResponseResult processSearchManager(String q);

    ResponseResult processUpdateManager(ManagerDTO managerDTO);

    Manager findOneByUsername(String currentName);

}
