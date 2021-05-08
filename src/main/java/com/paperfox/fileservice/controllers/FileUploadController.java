package com.paperfox.fileservice.controllers;

import com.paperfox.fileservice.services.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    private MultipartFile fileUpload(@RequestParam("file") MultipartFile file) {
        try {
            logger.info(file.getName());
            fileUploadService.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUploadService.getPreview(file);
    }

    @RequestMapping(value = "/api/file/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private void moveFile()  {

    }
}
