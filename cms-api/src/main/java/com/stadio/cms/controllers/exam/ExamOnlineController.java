package com.stadio.cms.controllers.exam;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.examOnline.ExamOnlineTestForm;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IOnlineTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/examOnline", name = "Quản lý thi trực tuyến")
public class ExamOnlineController extends BaseController {

    @Autowired
    IOnlineTestService onlineTestService;

    /**
     * Content manager hoac admin chon danh sach de thi va tao de thi online.
     * @param examOnlineTestForm
     * @return
     */
    @PostMapping(value = "/create", name = "Thêm đề thi trực tuyến")
    public ResponseEntity createdExamOnlineTest(
            @RequestBody ExamOnlineTestForm examOnlineTestForm) {
        ResponseResult result = onlineTestService.processCreateOrUpdateExamOnlineTest(examOnlineTestForm);
        return ResponseEntity.ok(result);
    }

    /**
     * Lay thong tin co ban thong tin de thi online
     */
    @GetMapping(value = "/get", name = "Lấy thông tin đề thi trực tuyến")
    public ResponseEntity update(
            @RequestParam(value = "id") String id
    ) {
        ResponseResult result = onlineTestService.processGetShortInformation(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Mo dang ky lich thi online test.
     * @param id
     * @return
     */
    @GetMapping(value = "/openRegister", name = "Cho phép mở đăng ký")
    public ResponseEntity openRegister(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processOpeningRegister(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Cho phep huy de thi thu
     * @return
     */
    @GetMapping(value = "/cancel", name = "Hủy đề thi trực tuyến")
    public ResponseEntity cancelExamOnlineTest(
            @RequestParam(value = "id") String id
    ) {
        ResponseResult result = onlineTestService.processCancelExamOnlineTest(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Hien thi danh sach thi thu duoc phan theo page.
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/list", name = "Xem danh sách đề thi trưc tuyến")
    public ResponseEntity listExamOnlineTest(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        ResponseResult result = onlineTestService.processGetListExamOnline(page, limit);
        return ResponseEntity.ok(result);
    }

    /**
     * Hien thi chi tiet mot de thi thu online.
     * @param id
     * @return
     */
    @GetMapping(value = "/details", name = "Thông tin chi tiết")
    public ResponseEntity detailsExamOnlineTest(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processDetailsExamOnlineTest(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Danh sach nguoi dung dang ky tham gia ki thi online
     * @param page
     * @param limit
     * @param id
     * @return
     */
    @GetMapping(value = "/joiner", name = "Danh sách đăng ký tham gia")
    public ResponseEntity listJoiners(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processGetListJoinerExamOnline(id, page, limit);
        return ResponseEntity.ok(result);
    }

    /**
     * Lay danh sach bang diem
     * @param page
     * @param limit
     * @param id
     * @return
     */
    @GetMapping(value = "/tablePoint", name = "Bảng điểm")
    public ResponseEntity tablePoint(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "id") String id) {
        ResponseResult results = onlineTestService.processGetTablePoint(id, page, limit);
        return ResponseEntity.ok(results);
    }


    /**
     * Message nhac nho hoc sinh truoc va sau khi thi
     * @param id
     * @return
     */
    @GetMapping(value = "/pushRemind", name = "Gửi thông báo nhắc nhở")
    public ResponseEntity pushRemind(
            @RequestParam(value = "id") String id) {
        ResponseResult results = onlineTestService.processPushMessageRemind(id);
        return ResponseEntity.ok(results);
    }

    /**
     * Message thong bao ket qua cho thi sinh tham du ky thi va gui ket qua
     * @param id
     * @return
     */
    @GetMapping(value = "/pushResults", name = "Gửi thông báo kết quả thi")
    public ResponseEntity pushResults(
            @RequestParam(value = "id") String id) {
        ResponseResult results = onlineTestService.processPushMessageResults(id);
        return ResponseEntity.ok(results);
    }

}
