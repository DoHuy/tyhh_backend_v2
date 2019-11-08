package com.stadio.model.repository.main.custom;

import com.hoc68.users.documents.Manager;

import java.util.List;

public interface ManagerRepositoryCustom
{
    List<Manager> findManagerByPage(Integer page, Integer pageSize);

    List<Manager> queryWithKeyword(String q);
}
