package com.stadio.model.repository.main;

import com.stadio.model.documents.Image;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Andy on 12/23/2017.
 */

public interface ImageRepository extends MongoRepository<Image, String>
{
    List<Image> findTopByCreatedDate();
}
