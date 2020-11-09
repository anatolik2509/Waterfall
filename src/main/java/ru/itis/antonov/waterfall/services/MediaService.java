package ru.itis.antonov.waterfall.services;

import org.apache.commons.fileupload.FileItem;

import java.io.FileInputStream;
import java.io.InputStream;

public interface MediaService {
    InputStream getFile(String path);

    String getMimeType(String path);

    String saveFileItem(FileItem item);

    boolean fileExist(String path);
}
