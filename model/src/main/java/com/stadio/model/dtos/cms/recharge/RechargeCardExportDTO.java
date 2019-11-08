package com.stadio.model.dtos.cms.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoc68.users.documents.Manager;
import com.stadio.model.documents.RechargeCardExport;
import com.stadio.model.repository.user.ManagerRepository;
import lombok.Data;

import java.util.Date;

@Data
public class RechargeCardExportDTO {

    private String id;

    private String managerId;

    private String fileUrl;

    private String title;

    private long cardValue;

    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    protected Date createdDate;

    private String managerIdCreated;

    private String managerUsernameCreated;

    public RechargeCardExportDTO(RechargeCardExport export) {
        this.id = export.getId();;
        this.managerId = export.getManagerId();
        this.fileUrl = export.getFileUrl();
        this.title = export.getTitle();
        this.cardValue = export.getCardValue();
        this.quantity = export.getQuantity();
        this.createdDate = export.getCreatedDate();
        this.managerIdCreated = export.getCreatedBy();
    }

    public void with(ManagerRepository managerRepository) {
        Manager manager = managerRepository.findOne(this.getManagerId());
        if (manager != null) {
            this.managerUsernameCreated = manager.getUsername();
        }
    }

}
