package com.paperfox.fileservice.controllers;

import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.services.FileUploadService;
import com.paperfox.fileservice.services.imageService.ImageService;
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

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    private MultiValueMap fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("productType") String productType) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        try {
            ProductType type = ProductType.asType(productType);
            String filePath = fileUploadService.uploadFile(file);

            formData.add("filePath", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }



    @GetMapping("/api/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        Resource file = fileUploadService.getPreview(filename);
        Resource file = imageService.getPreview(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/api/file/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private void moveFile() {

    }
}
