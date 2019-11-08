package com.stadio.model.repository.user;

import com.hoc68.users.documents.Manager;
import com.stadio.model.repository.main.custom.ManagerRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by Andy on 11/08/2017.
 */
public interface ManagerRepository extends MongoRepository<Manager, String>, ManagerRepositoryCustom
{
    Manager findOneByUsername(String username);

    @Query("{ 'email': ?0 }")
    public List<Manager> findManagerByEmail(String email);

    @Query("{ 'phone': ?0 }")
    public List<Manager> findManagerByPhone(String phone);

    @Query("{ 'username': ?0 }")
    public List<Manager> findManagerByUsername(String username);

}
