package com.stadio.cms.service.impl;

import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IChapterService;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Chapter;
import com.stadio.model.dtos.cms.ChapterFormDTO;
import com.stadio.model.dtos.cms.ChapterListDTO;
import com.stadio.model.repository.main.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService extends BaseService implements IChapterService
{

    @Autowired
    ChapterRepository chapterRepository;

    @Override
    public ResponseResult processCreateOneChapter(ChapterFormDTO chapterFormDTO) {
        ResponseResult responseResult = inValidChapterForm(chapterFormDTO);
        if(responseResult!=null) {
            return responseResult;
        }

        if (!StringUtils.isNotNull(chapterFormDTO.getCode())) {
            return ResponseResult.newErrorInstance("01", this.getMessage("chapter.empty.code"));
        }

        Chapter exists = chapterRepository.findByCode(chapterFormDTO.getCode());
        if (exists != null) {
            return ResponseResult.newErrorInstance("01", this.getMessage("chapter.already.exists"));
        }


        Chapter chapter = new Chapter();
        chapter.setName(chapterFormDTO.getName());
        chapter.setDescription(chapterFormDTO.getDescription());
        chapter.setCode(chapterFormDTO.getCode());

        chapterRepository.save(chapter);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("chapter.success.create"),new ChapterListDTO(chapter));
    }

    @Override
    public ResponseResult processUpdateOneChapter(ChapterFormDTO chapterFormDTO) {
        ResponseResult responseResult = inValidChapterForm(chapterFormDTO);
        if(responseResult!=null)
            return responseResult;
        if (!StringUtils.isNotNull(chapterFormDTO.getId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("chapter.invalid.id"), null);
        }

        Chapter chapter = chapterRepository.findOne(chapterFormDTO.getId());

        if (chapter == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("chapter.invalid.chapter"), null);
        }

        chapter.setName(chapterFormDTO.getName());
        chapter.setDescription(chapterFormDTO.getDescription());
        chapter.setCode(chapterFormDTO.getCode());

        chapterRepository.save(chapter);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("chapter.success.update"), new ChapterListDTO(chapter));
    }

    @Override
    public ResponseResult processGetChapterById(String id) {
        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("chapter.invalid.id"), null);
        }
        Chapter chapter = chapterRepository.findOne(id);

        if(chapter==null||chapter.isDeleted())
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("chapter.failure.byId"),null);
        return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("chapter.success.byId"),new ChapterListDTO(chapter));
    }

    @Override
    public ResponseResult processGetAllChapter() {
        List<Chapter> chapters = chapterRepository.findAll();
        if(chapters!=null){
            List<ChapterListDTO> chapterListDTOS = new ArrayList<>();
            chapters.forEach(chapter -> {
                if(!chapter.isDeleted())
                    chapterListDTOS.add(new ChapterListDTO(chapter));
            });
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("chapter.success.all"), chapterListDTOS);
        }
        else
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("chapter.failure.all"),null);
    }

    @Override
    public ResponseResult processDeleteChapter(String id) {
        Chapter chapter = chapterRepository.findOne(id);
        if(chapter!=null){
            chapter.setDeleted(true);
            chapterRepository.save(chapter);
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("chapter.success.delete"),null);
        } else {
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("chapter.success.byId"), new ChapterListDTO(chapter));
        }
    }

    private ResponseResult inValidChapterForm(ChapterFormDTO chapterFormDTO){
        if(chapterFormDTO==null)
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("chapter.invalid.chapter"),null);
        if(!StringUtils.isNotNull(chapterFormDTO.getName()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("chapter.invalid.name"),null);
        if(!StringUtils.isNotNull(chapterFormDTO.getDescription()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("chapter.invalid.description"),null);
        return null;
    }


}
