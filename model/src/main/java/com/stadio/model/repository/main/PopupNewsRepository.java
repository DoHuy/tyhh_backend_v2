package com.stadio.model.repository.main;

import com.stadio.model.documents.PopupNews;
import com.stadio.model.repository.main.custom.PopupNewsRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PopupNewsRepository extends MongoRepository<PopupNews, String>, PopupNewsRepositoryCustom {

    List<PopupNews> findAllByShowInApp(Boolean show);


}
