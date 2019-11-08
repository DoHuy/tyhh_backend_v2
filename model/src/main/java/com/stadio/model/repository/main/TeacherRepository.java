package com.stadio.model.repository.main;

import com.stadio.model.documents.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    List<Teacher> findByDeletedIs(boolean deleted);
}
