package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.face.Feature;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IBannerService;
import com.stadio.model.dtos.cms.BannerFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/banner", name = "Quản lý banner")
public class BannerController extends BaseController {

    @Autowired
    IBannerService bannerService;

    @GetMapping(value = "/list", name = "Danh sách banner")
    public ResponseResult getBanners() {
        return bannerService.processGetBanners();
    }

    @PostMapping(value = "/add", name = "Thêm banner mới")
    public ResponseResult addBanner(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody BannerFormDTO bannerFormDTO) {
        return bannerService.processAddBanner(bannerFormDTO, token);
    }

    @PostMapping(value = "/update", name = "Câp nhật banner")
    public ResponseResult updateBanner(
            @RequestBody BannerFormDTO bannerFormDTO) {
        return bannerService.processUpdateBanner(bannerFormDTO);
    }

    @GetMapping(value = "/delete", name = "Xóa banner")
    public ResponseResult deleteBanner(
            @RequestParam(value = "id") String id) {
        return bannerService.processDeleteBanner(id);
    }


}
