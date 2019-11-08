package com.stadio.cms.service.impl;

import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IClazzService;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Clazz;
import com.stadio.model.dtos.cms.ClazzFormDTO;
import com.stadio.model.dtos.cms.ClazzListDTO;
import com.stadio.model.repository.main.ClazzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ClazzService extends BaseService  implements IClazzService {
    @Autowired
    ClazzRepository clazzRepository;
    @Override
    public ResponseResult<?> processCreateOneClazz(ClazzFormDTO ClazzFormDTO) {
        ResponseResult responseResult = inValidClazzForm(ClazzFormDTO);
        if(responseResult!=null)
            return responseResult;

        Clazz clazz = new Clazz();
        clazz.setName(ClazzFormDTO.getName());
        clazz.setDescription(ClazzFormDTO.getDescription());
        clazz.setIdChapters(new LinkedList<String>());
        clazzRepository.save(clazz);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("clazz.success.create"),new ClazzListDTO(clazz));
    }

    @Override
    public ResponseResult<?> ProcessUpdateOneClazz(ClazzFormDTO clazzFormDTO) {
        ResponseResult responseResult = inValidClazzForm(clazzFormDTO);
        if(responseResult!=null)
            return responseResult;
        if (!StringUtils.isNotNull(clazzFormDTO.getId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("clazz.invalid.id"), null);
        }

        if(clazzFormDTO.getIdChapters()==null){
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("clazz.invalid.chapters"), null);
        }

        Clazz clazz = clazzRepository.findOneById(clazzFormDTO.getId());

        if (clazz == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("clazz.invalid.clazz"), null);
        }

        clazz.setName(clazzFormDTO.getName());
        clazz.setDescription(clazzFormDTO.getDescription());
        clazz.setIdChapters(clazzFormDTO.getIdChapters());
        clazzRepository.save(clazz);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("clazz.success.update"),new ClazzListDTO(clazz));
    }

    @Override
    public ResponseResult<?> ProcessGetClazzById(String id) {
        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("clazz.invalid.id"), null);
        }
        Clazz clazz = clazzRepository.findOneById(id);

        if(clazz==null||clazz.isDeleted())
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("clazz.failure.byId"),null);
        return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("clazz.success.byId"),new ClazzListDTO(clazz));
    }

    @Override
    public ResponseResult<?> ProcessGetAllClazz() {
        List<Clazz> clazzs = clazzRepository.findAll();
        if(clazzs!=null){
            List<ClazzListDTO> clazzListDTOS = new ArrayList<>();
            clazzs.forEach(clazz -> {
                if(!clazz.isDeleted())
                    clazzListDTOS.add(new ClazzListDTO(clazz));
            });
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("clazz.success.all"),clazzListDTOS);
        }
        else
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("clazz.failure.all"),null);
    }

    @Override
    public ResponseResult<?> ProcessDeleteClazz(String id) {
        Clazz clazz = clazzRepository.findOneById(id);
        if(clazz!=null){
            clazz.setDeleted(true);
            clazzRepository.save(clazz);
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("Clazz.success.delete"),null);
        }else
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("Clazz.success.byId"),new ClazzListDTO(clazz));

    }

    private ResponseResult inValidClazzForm(ClazzFormDTO clazzFormDTO){
        if(clazzFormDTO==null)
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("clazz.invalid.clazz"),null);
        if(!StringUtils.isNotNull(clazzFormDTO.getName()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("clazz.invalid.name"),null);
        if(!StringUtils.isNotNull(clazzFormDTO.getDescription()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("clazz.invalid.description"),null);
        return null;
    }

}
