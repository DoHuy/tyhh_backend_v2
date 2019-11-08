package com.stadio.cms.service.impl;

import com.stadio.cms.dtos.HotExamItemDTO;
import com.stadio.cms.dtos.HotExamSearchDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IHotExamService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamHot;
import com.stadio.model.dtos.cms.ExamHotFormDTO;
import com.stadio.model.enu.TopType;
import com.stadio.model.redisUtils.HotExamRedisRepository;
import com.stadio.model.repository.main.ExamHotRepository;
import com.stadio.model.repository.main.ExamRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotExamService extends BaseService implements IHotExamService
{

    @Autowired ExamRepository examRepository;

    @Autowired
    ExamService examService;

    @Autowired
    ExamHotRepository examHotRepository;

    @Autowired
    HotExamRedisRepository hotExamRedisRepository;

    @Override
    public ResponseResult processSearchExam(String q)
    {
        PageRequest request = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "created_date"));
        List<Exam> examList = (StringUtils.isNotBlank(q)) ? examRepository.fullTextSearch(q, 1, 10) : examRepository.findExamNewest(request).getContent();

        List<HotExamSearchDTO> hotExamSearchDTOList = new ArrayList<>();
        examList.forEach(exam ->
        {
            if (exam.isEnable() && !exam.isDeleted())
            {
                HotExamSearchDTO hotExamSearchDTO = HotExamSearchDTO.with(exam);
                hotExamSearchDTOList.add(hotExamSearchDTO);
            }
        });
        return ResponseResult.newSuccessInstance(hotExamSearchDTOList);
    }

    @Override
    public ResponseResult processCreateExamHot(ExamHotFormDTO examHotFormDTO)
    {

        Exam exam = examRepository.findOne(examHotFormDTO.getExamIdRef());

        if (exam == null)
        {
            exam = examRepository.findExamByCode(examHotFormDTO.getExamCode());
        }

        if (exam == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("examHot.invalid.notfound"), null);
        }

        ResponseResult responseResult = this.isValidExamHotFormDTO(examHotFormDTO);

        if (responseResult != null)
        {
            return responseResult;
        }

        ExamHot examHot = new ExamHot();
        examHot.setExamIdRef(exam.getId());
        examHot.setPosition(examHotFormDTO.getPosition());
        examHot.setTopType(TopType.valueOf(examHotFormDTO.getTopType()));
        ExamHot examHotsave = examHotRepository.save(examHot);

        hotExamRedisRepository.processPutHotExam(examHotsave,exam);

        HotExamItemDTO hotExamItemDTO = HotExamItemDTO.with(exam, examHot);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("examHot.success.create"), hotExamItemDTO);
    }

    /**
     * ExamHot and Exam have the same Id
     * Reload your ExamHot Id after update ExamHot if need (get new id to delete, update...)
     * @param examHotFormDTO
     * @return
     */
    @Override
    public ResponseResult processUpdateExamHot(ExamHotFormDTO examHotFormDTO)
    {
        ResponseResult responseResult = this.isValidExamHotFormDTO(examHotFormDTO);

        if (responseResult != null)
        {
            return responseResult;
        }

        ExamHot examHot = examHotRepository.findOne(examHotFormDTO.getId());

        if (examHot == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("examHot.invalid.id"), null);
        }
        else
        {
            Exam exam = examRepository.findOne(examHotFormDTO.getExamIdRef());

            if(exam!=null)
            examHot.setExamIdRef(exam.getId());
            else
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("examHot.invalid.exam"), null);

            examHot.setPosition(examHotFormDTO.getPosition());

            examHot.setId(examHotFormDTO.getExamIdRef());

            ExamHot examHotsave = examHotRepository.save(examHot);

            hotExamRedisRepository.processPutHotExam(examHotsave,exam);

            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("examHot.success.update"), examHot);
        }
    }

    /**
     * Note: ExamHot and Exam have the same Id
     * @param id
     * @return
     */
    @Override
    public ResponseResult processDeleteExamHot(String id)
    {
        if (!com.stadio.common.utils.StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("examHot.invalid.id"), null);
        }

        ExamHot examHot = examHotRepository.findOne(id);
        if (examHot != null)
        {
            hotExamRedisRepository.processDeleteHotExam(examHot);
            examHotRepository.delete(id);
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("examHot.success.delete"), null);
        }
        else
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("examHot.invalid.notfound"), null);
        }
    }

    @Override
    public ResponseResult processGetListExamHot(String topType)
    {

        List<ExamHot> examHotList = (StringUtils.isNotBlank(topType)) ?
                examHotRepository.findByTopTypeOrderByPositionAsc(TopType.valueOf(topType))
                : examHotRepository.findByTopTypeOrderByPositionAsc(TopType.HOME_SCREEN);

        List<HotExamItemDTO> hotExamItemDTOList = new ArrayList<>();

        for (ExamHot examHot: examHotList)
        {
            Exam exam = examRepository.findOne(examHot.getExamIdRef());
            if (exam != null)
            {
                HotExamItemDTO hotExamItemDTO = HotExamItemDTO.with(exam, examHot);
                hotExamItemDTOList.add(hotExamItemDTO);
            }

        }
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("examHot.success.getList"), hotExamItemDTOList);
    }

    private ResponseResult isValidExamHotFormDTO(ExamHotFormDTO examHotFormDTO)
    {
        if (examHotFormDTO.getPosition() == null || examHotFormDTO.getPosition() < 1)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("examHot.invalid.position"), null);
        }

        ExamHot examHot = examHotRepository.findExamHotByExamIdRefAndAndTopType(examHotFormDTO.getExamIdRef(), TopType.valueOf(examHotFormDTO.getTopType()));

        if (examHot != null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("examHot.invalid.exist"), null);
        }

        return null;
    }
}
