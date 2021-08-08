package com.paperfox.fileservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperfox.fileservice.models.Options;
import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
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

import java.net.MalformedURLException;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    private MultiValueMap fileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productType") String productType,
            @RequestParam("productSize") String productSize,
            @RequestParam("productToken") String token) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        try {
            ProductType type = ProductType.asType(productType);
            ObjectMapper mapper = new ObjectMapper();
            Size size = mapper.readValue(productSize, Size.class);
            Options options = new Options();
            options.file = file;
            options.token = token;
            options.productType = type;
            options.size = size;
            String filePath = imageService.createWorkingFilesByProductType(options);
            formData.add("filePath", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @GetMapping("/api/files/{filename}/{token}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable String token) throws MalformedURLException {
        Resource file = imageService.getPreview(filename, token);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/api/file/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private void moveFile() {

    }
}
