package com.stadio.es.service.impl;

import com.stadio.common.utils.ResponseCode;
import com.stadio.es.response.ResponseResult;
import com.stadio.es.service.IChemicalEquationService;
import com.stadio.es.utils.stoichiometry.Reaction;
import com.stadio.model.es.documents.ESChemicalEquation;
import com.stadio.model.es.repository.ESChemicalEquationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChemicalEquationService extends BaseService implements IChemicalEquationService {

    @Autowired
    ESChemicalEquationRepository chemicalEquationESRepository;

    @Override
    public ResponseResult balancer(String chemicalEquation) {
        try{
        Reaction reaction = Reaction.fromString(chemicalEquation);
//        String result = JsonUtils.writeValue(reaction);
        return ResponseResult.newSuccessInstance(reaction.toString());
        }catch (Exception e){
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,"balancer failure");
        }
    }


    /**
     * @apiNote : must save content follow the standard
     * @Example : NH3 + CO2 = (NH2)2CO + H2O
     * @param content
     * @return
     */
    @Override
    public ResponseResult create(String content) {
        //
        List<ESChemicalEquation> chemicalEquationESList = chemicalEquationESRepository.findByContent(content);
        if(chemicalEquationESList!=null && chemicalEquationESList.size()>0)
            return ResponseResult.newErrorInstance(ResponseCode.EXIST_VALUE,"exist Chemical Equation ");

        ESChemicalEquation chemicalEquationES = new ESChemicalEquation();
        chemicalEquationES.setContent(content);

        chemicalEquationESRepository.save(chemicalEquationES);
        return ResponseResult.newSuccessInstance("save success");
    }

    @Override
    public ResponseResult remove(String id) {
        chemicalEquationESRepository.delete(id);
        return ResponseResult.newSuccessInstance("remove success");
    }
}
