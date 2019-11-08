package com.stadio.common.service.impl;

import com.stadio.common.enu.FolderName;
import com.stadio.common.service.IStorageService;
import com.stadio.common.service.properties.StorageProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by Andy on 12/03/2017.
 */
@Service
public class StorageService implements IStorageService
{
    private final Path rootLocation;

    private Logger logger = LogManager.getLogger(StorageService.class);

    @Autowired
    public StorageService(StorageProperties properties)
    {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(Path p, MultipartFile file, String fname)
    {
        try
        {
            if (file.isEmpty())
            {
                throw new IOException("Failed to store empty file " + file.getOriginalFilename());
            }

            if (!p.toFile().exists())
            {
                Files.createDirectories(p);
            }

            if (fname == null)
            {
                fname = file.getOriginalFilename();
            }

            if (this.rootLocation.resolve(p).resolve(fname).toFile().exists())
            {
                Files.delete(this.rootLocation.resolve(p).resolve(fname));
            }


            logger.info("Store file: {}", this.rootLocation.resolve(p).resolve(fname));
            Files.copy(file.getInputStream(), this.rootLocation.resolve(p).resolve(fname));

        }
        catch (IOException e)
        {
            logger.error("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll()
    {
        try
        {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e)
        {
            logger.error("Failed to read stored files", e);
        }
        return null;
    }

    @Override
    public Path load(Path p, String filename)
    {
        return rootLocation.resolve(p).resolve(filename);
    }

    @Override
    public Resource loadAsResource(Path p, String filename)
    {
        try
        {
            if (!rootLocation.resolve(p).toFile().exists())
            {
                Files.createDirectory(rootLocation.resolve(p));
            }
            Path file = load(p, filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
            {
                logger.info("Loading: " + file.toUri());
                return resource;
            }
        }
        catch (MalformedURLException e)
        {
            logger.error("Could not read file: " + filename, e);
        }
        catch (IOException e)
        {
            logger.error("IOException: ", e);
        }
        return null;
    }

    @Override
    public Path getLocation(FolderName fn)
    {
        return this.rootLocation.resolve(fn.name());
    }

    @Override
    public void init()
    {
        try
        {
            if (!rootLocation.toFile().exists())
            {
                Files.createDirectory(rootLocation);
            }
        }
        catch (IOException e)
        {
            logger.error("Could not initialize storage", e);
        }
    }
}
