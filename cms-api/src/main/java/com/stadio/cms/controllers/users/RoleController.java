package com.stadio.cms.controllers.users;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.authorization.RoleFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "api/role", name = "Quản lý phân quyền")
@RestController
public class RoleController extends BaseController {

    @Autowired
    IRoleService roleService;

    @GetMapping(value = "/list", name = "Danh sách phân quyền")
    public ResponseEntity getRoleList() {
        ResponseResult result = roleService.processGetRoleList();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/createOrUpdate", name = "Tạo hoặc cập nhập nhóm quyền mới")
    public ResponseEntity createNewRole(
            @RequestBody RoleFormDTO roleFormDTO) {
        ResponseResult result = roleService.processCreateOrUpdateSingleRole(roleFormDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/delete", name = "Xóa nhóm quyền")
    public ResponseEntity deleteRole(@RequestParam(value = "id") String id) {
        ResponseResult result = roleService.processDeleteSingleRole(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/details", name = "Xem chi tiết nhóm quyền")
    public ResponseEntity roleDetails(@RequestParam(value = "id") String id) {
        ResponseResult result = roleService.processDetailsRole(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/menuList", name = "Cập nhập hiển thị menu")
    public ResponseEntity updateMenuList(@RequestBody  String menuList) {
        ResponseResult result = roleService.processUpdateMenuList(menuList);
        return ResponseEntity.ok(result);
    }

}
