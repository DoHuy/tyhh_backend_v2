package com.stadio.cms.service;

import com.stadio.cms.dtos.authorization.RoleFormDTO;
import com.stadio.cms.response.ResponseResult;

public interface IRoleService {

    ResponseResult processGetRoleList();

    ResponseResult processCreateOrUpdateSingleRole(RoleFormDTO roleFormDTO);

    ResponseResult processDeleteSingleRole(String id);

    ResponseResult processDetailsRole(String id);

    ResponseResult processUpdateMenuList(String menuList);
}
