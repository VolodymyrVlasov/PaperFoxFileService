package com.paperfox.fileservice.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploadService {

    String uploadFile(MultipartFile file) throws IOException;

    Resource getPreview(String fileName);
}
