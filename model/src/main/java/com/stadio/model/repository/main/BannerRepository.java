package com.stadio.model.repository.main;

import com.stadio.model.documents.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BannerRepository extends MongoRepository<Banner, String>
{
    List<Banner> findBannerByEnableOrderByPositionAsc(boolean enable);

}
