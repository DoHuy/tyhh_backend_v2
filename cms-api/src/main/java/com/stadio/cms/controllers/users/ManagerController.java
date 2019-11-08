package com.stadio.cms.controllers.users;

import com.stadio.cms.controllers.BaseController;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.hoc68.users.documents.Manager;
import com.stadio.model.dtos.cms.ManagerDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 11/08/2017.
 */
@RestController
@RequestMapping(value = "api/manager", name = "Quản lý Nhân sự")
public class ManagerController extends BaseController {

    private Logger logger = LogManager.getLogger(ManagerController.class);

    /**
     * Test dau vao API
     *
     * @return
     */
    @GetMapping("/simple")
    public ResponseResult simple(HttpServletRequest request) {
        Map<String, String> body = new HashMap<>();
        return ResponseResult.newInstance(ResponseCode.SUCCESS, "", body);
    }

    @PostMapping(value = "/register", name = "Tạo tài khoản nhân sự")
    public ResponseResult register(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ManagerDTO managerDTO) {
        return managerService.processCreateNewManager(token, managerDTO);
    }


    @PostMapping(value = "/update", name = "Cập nhập thông tin nhân sự")
    public ResponseResult update(@RequestBody ManagerDTO managerDTO) {
        return managerService.processUpdateManager(managerDTO);
    }


    @PostMapping(value = "/changePassword", name = "Thay đổi mật khẩu")
    public ResponseResult changePassword(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "oldPass") String oldPass,
            @RequestParam(value = "newPass") String newPass) {
        return managerService.processChangePassword(token, oldPass, newPass);
    }

    @GetMapping(value = "/delete", name = "Xóa tài khoản nhân sự")
    public ResponseResult delete(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "id") String id) {
        return managerService.processDeleteManager(token, id);
    }

    @GetMapping(value = "/list", name = "Hiển thị danh sách")
    public ResponseResult list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize,
            HttpServletRequest request) {
        return managerService.processGetListManager(page, pageSize, this.requestURI(request));
    }


    @GetMapping(value = "/details", name = "Xem thông tin chi tiết")
    public ResponseResult details(@RequestParam(value = "id") String id) {
        return managerService.processGetProfileManager(id);
    }

    @GetMapping(value = "/search", name = "Tìm kiếm thông tin")
    public ResponseResult search(
            @RequestParam(value = "q") String q) {
        return managerService.processSearchManager(q);
    }

    @GetMapping(value = "/profile")
    public ResponseResult profile(
            @RequestHeader(value = "Authorization") String token
    ) {
        Manager manager = managerService.getManagerRequesting();
        return ResponseResult.newInstance("00", "", manager);
    }

}