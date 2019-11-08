package com.stadio.mobi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.service.IMigrateDBService;
import com.stadio.model.documents.Bookmark;
import com.stadio.model.documents.Category;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamCategory;
import com.stadio.model.enu.BookmarkType;
import com.stadio.model.repository.main.BookmarkRepository;
import com.stadio.model.repository.main.CategoryRepository;
import com.stadio.model.repository.main.ExamCategoryRepository;
import com.stadio.model.repository.main.ExamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MigrateDBService implements IMigrateDBService {

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ExamCategoryRepository examCategoryRepository;

    private static Logger logger = LogManager.getLogger(MigrateDBService.class);

    @Override
    public void runMigrate() {

        this.migrateExamWithHasCorrectionDetail();
        this.migrateExamInCategory();
    }

    private void migrateBookmark() {
        CompletableFuture<Boolean> combinedFuture = CompletableFuture.supplyAsync(
                () ->
                {
                    List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkTypeIsNull();
                    if (!HelperUtils.isEmptyArray(bookmarks)) {
                        logger.info("Migrate start: tab_bookmark");
                        for (Bookmark bookmark: bookmarks) {
                            if (StringUtils.isNotNull(bookmark.getExamId())) {
                                bookmark.setObjectId(bookmark.getExamId());
                                bookmark.setBookmarkType(BookmarkType.EXAM);
                            } else if (StringUtils.isNotNull(bookmark.getCategoryId())) {
                                bookmark.setObjectId(bookmark.getCategoryId());
                                bookmark.setBookmarkType(BookmarkType.EXAM_CATEGORY);
                            } else {
                                logger.info("Migrate WARNING: tab_bookmark, id:" + bookmark.getId());
                            }
                            bookmarkRepository.save(bookmark);
                        }
                        logger.info("Migrate success: tab_bookmark");
                    }
                    return true;
                }
        );
    }

    private void migrateExamWithHasCorrectionDetail() {
        CompletableFuture<Boolean> combinedFuture = CompletableFuture.supplyAsync(
                () ->
                {
                    List<Exam> exams = examRepository.findAllByHasCorrectionDetailIsNull();
                    if (!HelperUtils.isEmptyArray(exams)) {
                        logger.info("Migrate start: migrateExamWithHasCorrectionDetail");
                        for (Exam exam: exams) {
                            exam.setHasCorrectionDetail(Boolean.TRUE);
                            examRepository.save(exam);
                        }
                        logger.info("Migrate success: migrateExamWithHasCorrectionDetail");
                    }
                    return true;
                }
        );
    }

    private void migrateExamInCategory() {
        CompletableFuture<Boolean> combinedFuture = CompletableFuture.supplyAsync(
                () ->
                {
                    List<Category> categories = categoryRepository.findAllByExamIdsIsNotNull();
                    if (!HelperUtils.isEmptyArray(categories)) {
                        logger.info("Migrate start: migrateExamInCategory");
                        for (Category category: categories) {

                            if (!HelperUtils.isEmptyArray(category.getExamIds())) {
                                for (Exam exam: category.getExamIds()) {
                                    ExamCategory examCategory = new ExamCategory();
                                    examCategory.setCategoryId(category.getId());
                                    examCategory.setExamId(exam.getId());
                                    examCategoryRepository.save(examCategory);
                                }
                            }

                            category.setExamIds(null);
                            categoryRepository.save(category);
                        }
                        logger.info("Migrate success: migrateExamInCategory");
                    }
                    return true;
                }
        );
    }
}
