package com.stadio.mediation.service;

import com.stadio.model.documents.MediaDocument;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentService {

    Resource loadDocumentsAsResource(String doc, boolean isSecret);

    String processSaveToStorage(MultipartFile multipartFile, boolean isSecret);

    MediaDocument processExtractMedia(MultipartFile file, String url);
}
