package com.stadio.model.repository.user.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.SequenceIndex;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.SequenceKey;
import com.stadio.model.redisUtils.RedisRepository;
import com.stadio.model.repository.main.SequenceIndexRepository;
import com.stadio.model.repository.main.custom.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl implements UserRepositoryCustom
{

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    MongoTemplate mongoTemplate;

    @Autowired RedisRepository redisRepository;

    @Autowired
    SequenceIndexRepository sequenceIndexRepository;

    @Override
    public Long searchUserQuantity(Map userSearch)
    {
        Query query = new Query();
        if (StringUtils.isValid((String) userSearch.get("clazzId")))
        {
            query.addCriteria(Criteria.where("clazzId").is(userSearch.get("clazzId")));
        }
        if (StringUtils.isValid((String) userSearch.get("code")))
        {
            query.addCriteria(Criteria.where("code").regex((String) userSearch.get("code")));
        }
        if (StringUtils.isValid((String) userSearch.get("fullName")))
        {
            query.addCriteria(Criteria.where("fullName").regex((String) userSearch.get("fullName")));
        }
        query.addCriteria(Criteria.where("deleted").is(false));

        return mongoTemplate.count(query, User.class);
    }

    @Override
    public List findUserByPage(Integer page, Integer pageSize, Map userSearch)
    {
        Query query = new Query();
        if (StringUtils.isValid((String) userSearch.get("clazzId")))
        {
            query.addCriteria(Criteria.where("clazzId").is(userSearch.get("clazzId")));
        }
        if (StringUtils.isValid((String) userSearch.get("code")))
        {
            query.addCriteria(Criteria.where("code").regex((String) userSearch.get("code")));
        }
        if (StringUtils.isValid((String) userSearch.get("fullName")))
        {
            query.addCriteria(Criteria.where("fullName").regex((String) userSearch.get("fullName")));
        }
        query.addCriteria(Criteria.where("deleted").is(false));
        final Pageable pageableRequest = new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC,"created_date"));
        query.with(pageableRequest);

        return mongoTemplate.find(query, User.class);
    }

    @Override
    public User saveUpdate(User user) {
        user.setUpdatedDate(new Date());
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User saveNew(User user) {
//
//        byte[] bytes = user.getId().getBytes();
//        Adler32 hash=new Adler32();
//        hash.update(bytes, 0, bytes.length);
//        user.setCode(Long.toHexString(hash.getValue()));
//        mongoTemplate.save(user);
//
//        Query query = new Query();
//        query.with(new Sort(Sort.Direction.DESC, "id"));
//        query.limit(1);
//
//        List<User> lastUsers = mongoTemplate.find(query, User.class);
//        if (lastUsers != null && lastUsers.size() > 0) {
//            User lastUser = lastUsers.get(0);
//            user.setSequence(lastUser.getSequence() + 1);
//        } else {
//            user.setSequence(0);
//        }

        Long seq = 0L;
        SequenceIndex userSeq = sequenceIndexRepository.findFirstBySequenceKey(SequenceKey.User.name());
        if (userSeq == null) {
            SequenceIndex sequenceIndex = new SequenceIndex();
            sequenceIndex.setSequenceKey(SequenceKey.User.name());
            sequenceIndex.setCurrentSequence(0);
            sequenceIndexRepository.save(sequenceIndex);
        } else {
            seq = userSeq.getCurrentSequence() + 1;
            userSeq.setCurrentSequence(seq);
            sequenceIndexRepository.save(userSeq);
        }
        user.setSequence(seq);
        user.setCode(StringUtils.toStringWithMinLenght(Long.toHexString(user.getSequence()),6));
        mongoTemplate.save(user);

        return user;
    }
}
