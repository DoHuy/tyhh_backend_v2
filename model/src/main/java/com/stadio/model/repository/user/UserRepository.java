package com.stadio.model.repository.user;

import com.hoc68.users.documents.User;
import com.stadio.model.repository.main.custom.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Andy on 11/10/2017.
 */
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom
{

    User findOneByUsername(String username);

    User findOneByFacebookId(String facebookId);

    User findOneByGoogleId(String googleId);

    User findByCode(String code);

    User findFirstById(String id);

    User findFirstByCode(String code);

    User findFirstByPhone(String phone);

    User findFirstByEmail(String email);

    Page<User> findAllBy(Pageable pageable);
}