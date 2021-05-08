package com.paperfox.fileservice.controllers;

import com.paperfox.fileservice.services.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    private MultiValueMap fileUpload(@RequestParam("file") MultipartFile file) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        try {
            logger.info(file.getName());
            String filePath = fileUploadService.uploadFile(file);
            formData.add("filePath", filePath);
//            formData.add("preview", fileUploadService.getPreviewSt().getFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formData;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file =  fileUploadService.getPreview(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/api/file/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private void moveFile() {

    }
}
