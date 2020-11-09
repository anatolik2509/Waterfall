package ru.itis.antonov.waterfall.services;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class MediaServiceFileImpl implements MediaService{

    private Path repositoryPath;

    public MediaServiceFileImpl(Path path){
        repositoryPath = path;
    }

    public String saveFileItem(FileItem item){
        String fileName = RandomStringUtils.random(10, true, true);
        String ext;
        try {
            ext = MimeTypes.getDefaultMimeTypes().forName(item.getContentType()).getExtension();
            fileName += ext;
            item.write(repositoryPath.resolve(fileName).toFile());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return fileName;
    }

    @Override
    public boolean fileExist(String path) {
        return repositoryPath.resolve(path).toFile().exists();
    }

    @Override
    public InputStream getFile(String path) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(repositoryPath.resolve(path).toFile());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return inputStream;
    }

    @Override
    public String getMimeType(String path) {
        try {
            return Files.probeContentType(repositoryPath.resolve(path));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
