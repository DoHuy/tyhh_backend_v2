package com.stadio.model.dtos.cms.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.CODOrder;
import com.stadio.model.documents.RechargeCard;
import com.stadio.model.enu.CODOrderStatus;
import com.stadio.model.repository.main.RechargeCardRepository;
import lombok.Data;

import java.util.Date;

@Data
public class CODOrderDTO {

    private String id;

    private String code;

    private String userId;

    private String userCode;

    private String userPhone;

    private String userFullName;

    private long value;

    private String address;

    private String note;

    private CODOrderStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    protected Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    protected Date updatedDate;

    protected String createdBy;

    protected String updatedBy;

    private String updateReason;

    private RechargeCardDTO rechargeCard;

    public static CODOrderDTO with(CODOrder order) {
        CODOrderDTO orderDTO = new CODOrderDTO();

        orderDTO.id = order.getId();
        orderDTO.code = order.getCode();

        orderDTO.userId = order.getUserId();
        orderDTO.userCode = order.getUserCode();

        orderDTO.userPhone = order.getUserPhone();
        orderDTO.userFullName = order.getUserFullName();
        orderDTO.value = order.getValue();
        orderDTO.address = order.getAddress();
        orderDTO.note = order.getNote();
        orderDTO.status = order.getStatus();
        orderDTO.createdDate = order.getCreatedDate();
        orderDTO.createdBy = order.getCreatedBy();
        orderDTO.updatedDate = order.getUpdatedDate();
        orderDTO.updatedBy = order.getUpdatedBy();
        orderDTO.updateReason = order.getUpdateReason();
        return orderDTO;
    }

    public static CODOrderDTO with(CODOrder order, RechargeCardRepository rechargeCardRepository) {
        CODOrderDTO orderDTO = CODOrderDTO.with(order);
        if (StringUtils.isNotNull(order.getRechargeCardId())) {
            RechargeCard rechargeCard = rechargeCardRepository.findOne(order.getRechargeCardId());
            if (rechargeCard != null) {
                orderDTO.rechargeCard = new RechargeCardDTO(rechargeCard);
            }
        }
        return orderDTO;
    }
}
