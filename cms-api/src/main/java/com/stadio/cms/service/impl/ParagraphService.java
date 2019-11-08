package com.stadio.cms.service.impl;

import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IParagraphService;
import com.stadio.cms.VimeoRequest;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.cms.course.LectureDetailDTO;
import com.stadio.model.dtos.cms.ParagraphFormDTO;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.repository.main.ConfigRepository;
import com.stadio.model.repository.main.LectureRepository;
import com.stadio.model.repository.main.TopicRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class ParagraphService extends BaseService implements IParagraphService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    TopicRepository topicRepository;

    private Logger logger = LogManager.getLogger(ParagraphService.class);

    @Override
    public ResponseResult processGetDetailOfLecture(String lectureId) {

        //Logic Mới
        Lecture lecture = lectureRepository.findOne(lectureId);
        List<LectureDetailDTO> itemDTOList = new LinkedList<>();
        if (lecture != null) {
            itemDTOList.add(new LectureDetailDTO(lecture));
        }
        return ResponseResult.newSuccessInstance(itemDTOList);
    }

    @Override
    public ResponseResult processCreateDetail(ParagraphFormDTO lectureDetailFormDTO) {
        return this.processUpdateDetail(lectureDetailFormDTO);
    }

    @Override
    public ResponseResult processUpdateDetail(ParagraphFormDTO lectureDetailFormDTO) {
        ResponseResult responseResult = validFormParagraph(lectureDetailFormDTO);
        if(responseResult!=null)
            return responseResult;
        Lecture lecture;
        if (StringUtils.isNotNull(lectureDetailFormDTO.getLectureId())) {
            lecture = lectureRepository.findOne(lectureDetailFormDTO.getLectureId());
        } else {
            lecture = new Lecture();

            lecture.setSectionId(lectureDetailFormDTO.getSectionId());
            lecture.setPosition(lectureRepository.countBySectionIdIs(lectureDetailFormDTO.getSectionId()));

        }

        if(lecture != null){
            lecture.setName(lectureDetailFormDTO.getName());
            lecture.setContent(lectureDetailFormDTO.getContent());
            lecture.setVideoUrl(lectureDetailFormDTO.getVideoUrl());
            lecture.setIsFree(lectureDetailFormDTO.getIsFree());
            lecture.setDocumentUrl(lectureDetailFormDTO.getDocumentUrl());
            lecture.setReleaseDate(lectureDetailFormDTO.getReleaseDate());

            lecture.setReleaseDate(lectureDetailFormDTO.getReleaseDate());

            if (StringUtils.isNotNull(lectureDetailFormDTO.getVideoUrl())) {
                String vimeoId = StringUtils.getVideoIdFromVimeoUrl(lectureDetailFormDTO.getVideoUrl());
                try {
                    lecture.setVideoDuration(this.getVideoDurration(vimeoId));
                } catch (Exception e) {
                    logger.error("get vimeo info error: " + vimeoId, e);
                    return ResponseResult.newErrorDefaultInstance("Lỗi lấy thông tin vimeo từ vimeo");
                }
            }

            if (!StringUtils.isNotNull(lecture.getTopicId())) {
                Topic topic = new Topic();
                topicRepository.save(topic);

                lecture.setTopicId(topic.getId());
            }

            lectureRepository.save(lecture);

            return ResponseResult.newSuccessInstance(new LectureDetailDTO(lecture));
        }else
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,"paragraph.not.exist");
    }


    private int getVideoDurration(String vimeoId) throws Exception {
        Config config = configRepository.findConfigByKey(ConfigKey.VIMEO_ACCESS_TOKEN.name());
        if (config == null) {
            throw new Exception("Chưa config vimeo access token");
        }

        VimeoRequest vimeoRequest = new VimeoRequest();
        return vimeoRequest.getVimeoVideoDurration(vimeoId, config.getValue());
    }

    ResponseResult validFormParagraph(ParagraphFormDTO paragraphFormDTO){
        if(paragraphFormDTO==null)
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"paragraph.invalid.paragraph");
        if(!StringUtils.isValid(paragraphFormDTO.getName()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"paragraph.invalid.name");
        if(!StringUtils.isValid(paragraphFormDTO.getContent()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"paragraph.invalid.content");
        if(!StringUtils.isNotNull(StringUtils.getVideoIdFromVimeoUrl(paragraphFormDTO.getVideoUrl())) && paragraphFormDTO.getReleaseDate() == null)
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"Phải truyền videoUrl hoặc releaseDate");

        return null;
    }

}
