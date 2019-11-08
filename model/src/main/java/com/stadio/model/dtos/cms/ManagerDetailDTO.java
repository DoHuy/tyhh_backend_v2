package com.stadio.model.dtos.cms;

import com.hoc68.users.documents.Manager;

import java.util.Date;

public class ManagerDetailDTO extends ManagerItemDTO
{

    private String address;

    private Date updatedDate;

    public ManagerDetailDTO(Manager manager)
    {
        super(manager);

        this.address = manager.getAddress();

        this.updatedDate = manager.getUpdatedDate();
    }
}
