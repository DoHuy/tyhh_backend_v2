package com.stadio.mobi.controllers;

import com.stadio.mobi.dtos.examOnline.ExamOnlineSubmitDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICommentService;
import com.stadio.mobi.service.IOnlineTestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/examOnline")
public class ExamOnlineController extends BaseController {

    private Logger logger = LogManager.getLogger(ExamOnlineController.class);

    @Autowired
    IOnlineTestService onlineTestService;

    @Autowired
    ICommentService commentService;

    /**
     * Danh sach cac de thi online dang mo cho nguoi dung dang ky
     * List Exam Online was opening for users can subscribe which is valid.
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity listExamOnline() {
        ResponseResult result = onlineTestService.processGetListExamOnlineOpening();
        return ResponseEntity.ok(result);
    }

    /**
     * Cho phep nguoi dung dang ky tham gia thi online
     * Allow users can subscribe ExamOnline
     * @param id
     * @return
     */
    @GetMapping(value = "/subscribe")
    public ResponseEntity subscribeExamOnline(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processUserSubscribeExamOnline(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Cho phep nguoi dung huy dang ky tham gia thi online
     * @param id
     * @return
     */
    @GetMapping(value = "/cancel")
    public ResponseEntity cancelExamOnline(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processUserCancelExamOnline(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/likes")
    public ResponseEntity actionLike(@RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processAllowUserCanLikeIt(id);
        return ResponseEntity.ok(result);
    }

    /**
     *  Nhan danh sach cau hoi trong de thi
     * @param id
     * @return
     *  - Neu chua toi thoi gian lam bai, tra ve thong tin de thi, thoi gian bat dau va thoi gian ket thuc
     *  - Neu den thoi gian lam bai thi tra ve danh sach cac cau hoi trong de thi
     *  - Neu qua thoi gian bat dau 15 phut thi tra ve thong bao het thoi gian nhan de thi,
     */
    @GetMapping(value = "/questionList")
    public ResponseEntity questionList(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processGetQuestionList(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Hien thi thong tin chi tiet de thi
     * @param id
     * @return
     */
    @GetMapping(value = "/details")
    public ResponseEntity details(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processGetDetails(id);
        return ResponseEntity.ok(result);
    }


    /**
     * Lay danh sach binh luan sap xep theo thoi gian
     * @param id
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/comment/list")
    public ResponseEntity commentList(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        ResponseResult result = commentService.getListCommentFromExamOnline(id, page, limit);
        return ResponseEntity.ok(result);
    }


    /**
     * Gui tin nhan di
     * @param id
     */
    @PostMapping(value = "/comment/send")
    public ResponseEntity sendComment(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "message") String message) {
        ResponseResult result = commentService.processSendingMessage(id, message);
        return ResponseEntity.ok(result);
    }

    /**
     * Cho phep nguoi dung submit dap an
     * @return
     */
    @PostMapping(value = "/submit")
    public ResponseEntity submitExamOnline(
            @RequestBody ExamOnlineSubmitDTO examOnlineSubmitDTO) {
        ResponseResult result = onlineTestService.processSubmitExamOnline(examOnlineSubmitDTO.getExamOnlineId(), examOnlineSubmitDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Lich su cac lan thi da dien ra
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/history")
    public ResponseEntity getExamOnlineHistory(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        ResponseResult result = onlineTestService.processGetListExamOnlineFinished(page, limit);
        return ResponseEntity.ok(result);
    }

    /**
     * Nguoi dung co the xem bang diem sau khi thi xong
     * @param id - examOnline Id
     * @return
     */
    @GetMapping(value = "/tablePoint")
    public ResponseEntity getTablePoint(
            @RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processGetTablePointOfExamOnline(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Cho phep nguoi dung sau khi thi xong co the xem de thi va dap an (khong co loi giai)
     * @return
     */
    @GetMapping(value = "/results")
    public ResponseEntity results(@RequestParam(value = "id") String id) {
        ResponseResult result = onlineTestService.processGetResultsOfExamOnline(id);
        return ResponseEntity.ok(result);
    }


}
