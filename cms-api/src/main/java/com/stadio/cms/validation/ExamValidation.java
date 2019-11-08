package com.stadio.cms.validation;

import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.dtos.cms.ExamFormDTO;
import com.stadio.model.enu.ExamType;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Andy on 01/31/2018.
 */
@Component
public class ExamValidation extends CustomValidate
{
    public ResponseResult inValidExamForm(ExamFormDTO examFormDTO)
    {
        if (examFormDTO == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, this.getMessage("exam.invalid.exam"), null);
        }
        else if (!StringUtils.isNotNull(examFormDTO.getCode()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, this.getMessage("exam.invalid.code"), null);
        }
        else if (!StringUtils.isNotNull(examFormDTO.getName()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.name"), null);
        }
        else if (examFormDTO.getTime() == null || examFormDTO.getTime() < 0)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.time"), null);
        }
        else if (examFormDTO.getPrice() == null || examFormDTO.getPrice() < 0)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.price"), null);
        }
        else if (!StringUtils.isNotNull(examFormDTO.getClazzId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.clazz"), null);
        }
        else if (!EnumUtils.isValidEnum(ExamType.class, examFormDTO.getType()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.type"), null);
        }

        return null;
    }

}
