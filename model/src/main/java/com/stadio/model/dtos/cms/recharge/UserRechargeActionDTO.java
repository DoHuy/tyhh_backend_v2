package com.stadio.model.dtos.cms.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.RechargeCard;
import com.stadio.model.documents.UserRechargeAction;
import com.stadio.model.repository.main.RechargeCardRepository;
import com.stadio.model.repository.user.UserRepository;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@Data
public class UserRechargeActionDTO {

    private String id;

    private String userIdUsed;

    private String userCodeUsed;

    private String cardNumber;

    private String cardSerial;

    private boolean isSuccess;

    private boolean isShouldLockUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date createdDate;

    public UserRechargeActionDTO(UserRechargeAction action, RechargeCardRepository rechargeCardRepository) {
        this.id = action.getId();
        this.userIdUsed = action.getUserId();
        this.userCodeUsed = action.getUserCode();
        this.cardNumber = action.getCardNumber();
        this.isSuccess = action.isSuccess();
        if (this.isSuccess) {
            try {
                this.cardSerial = rechargeCardRepository.findFirstByCardNumber(this.cardNumber).getSerial();
            } catch (Exception e) {
//                logger.error("Get card serial exception", e);
            };
        }
        this.isShouldLockUser = action.isShouldLockUser();
        this.createdDate = action.getCreatedDate();
    }
}
