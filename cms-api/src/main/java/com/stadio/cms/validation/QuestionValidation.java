package com.stadio.cms.validation;

import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.dtos.cms.AnswerDTO;
import com.stadio.model.dtos.cms.QuestionFormDTO;
import com.stadio.model.enu.QuestionType;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Andy on 02/16/2018.
 */
@Component
public class QuestionValidation extends CustomValidate
{

    public ResponseResult inValidQuestionForm(QuestionFormDTO questionFormDTO)
    {
        if (questionFormDTO == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.question"), null);
        }
        if (!StringUtils.isNotNull(questionFormDTO.getContent()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.content"), null);
        }

//        if (!StringUtils.isNotNull(questionFormDTO.getExplain()))
//        {
//            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.explain"), null);
//        }

        if (!EnumUtils.isValidEnum(QuestionType.class, questionFormDTO.getType()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.type"), null);
        }

        if (questionFormDTO.getClazzId() == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.clazz"), null);
        }

        if (questionFormDTO.getAnswers().size() != 4)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.miss.answers"), null);
        }

        List<AnswerDTO> answers = questionFormDTO.getAnswers();
        for (int pos = 0; pos < answers.size(); pos++)
        {
            if (answers.get(pos) == null)
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.answer.invalid"), null);
            }
            if (!StringUtils.isNotNull(answers.get(pos).getCode()))
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.answer.invalid.code"), null);
            }
            if (!StringUtils.isNotNull(answers.get(pos).getContent()))
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.answer.invalid.content"), null);
            }
        }

        return null;
    }
}
