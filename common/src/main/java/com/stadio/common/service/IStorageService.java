package com.stadio.common.service;

import com.stadio.common.enu.FolderName;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Cac dich vu cho viec upload file
 * Created by Andy on 11/08/2017.
 */
public interface IStorageService
{
    void init();

    void store(Path p, MultipartFile file, String fname);

    Stream<Path> loadAll();

    Path load(Path p, String filename);

    Resource loadAsResource(Path p, String filename);

    Path getLocation(FolderName fn);
}
