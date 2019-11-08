package com.stadio.model.dtos.cms.recharge;

import com.stadio.model.enu.CODOrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CODOrderSearchFormDTO {

    private String code;
    private String userCode;
    private String userPhone;
    private String userFullName;
    private String address;
    private CODOrderStatus status;
    private long value;

    @NotNull
    private int page;

    @NotNull
    private int pageSize;

}
