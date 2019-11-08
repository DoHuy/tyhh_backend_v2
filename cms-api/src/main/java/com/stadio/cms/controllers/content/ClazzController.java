package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IClazzService;
import com.stadio.model.dtos.cms.ClazzFormDTO;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/clazz", name = "Quản lý lớp")
public class ClazzController extends BaseController {

    private Logger logger = LogManager.getLogger(ClazzController.class);
    @Autowired
    IClazzService clazzService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Tạo lớp mới")
    public ResponseResult create(@RequestBody ClazzFormDTO clazzFormDTO)
    {
        return clazzService.processCreateOneClazz(clazzFormDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhật thông tin lớp")
    public ResponseResult update(@RequestBody ClazzFormDTO clazzFormDTO)
    {
        return clazzService.ProcessUpdateOneClazz(clazzFormDTO);
    }

    @RequestMapping(value = "/by-id", method = RequestMethod.GET, name = "Lấy thông tin lớp theo ID")
    public ResponseResult getById(@RequestParam(value = "id",required = true) String id)
    {
        return clazzService.ProcessGetClazzById(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, name = "Lấy tất cả thông tin Lớp")
    public ResponseResult getAll(HttpServletRequest request)
    {
        return clazzService.ProcessGetAllClazz();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, name = "Xóa lớp đã tạo")
    public ResponseResult<?> delete(@RequestParam(value = "id",required = true) String id)
    {
        return clazzService.ProcessDeleteClazz(id);
    }








}
