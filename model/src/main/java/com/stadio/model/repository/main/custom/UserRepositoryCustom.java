package com.stadio.model.repository.main.custom;

import com.hoc68.users.documents.User;

import java.util.List;
import java.util.Map;

public interface UserRepositoryCustom {

    Long searchUserQuantity(Map userSearch);

    List findUserByPage(Integer page, Integer pageSize, Map userSearch);

    User saveUpdate(User user);

    User saveNew(User user);

}
