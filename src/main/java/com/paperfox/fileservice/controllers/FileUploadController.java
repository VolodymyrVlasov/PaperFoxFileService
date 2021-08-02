package com.paperfox.fileservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import com.paperfox.fileservice.services.FileUploadService;
import com.paperfox.fileservice.services.imageService.ImageService;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    private MultiValueMap fileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productType") String productType,
            @RequestParam("productSize") String productSize) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        try {
            logger.info("input file name ->>> " +file.getOriginalFilename());
            ProductType type = ProductType.asType(productType);
            ObjectMapper mapper = new ObjectMapper();
            Size size = mapper.readValue(productSize, Size.class);
            String filePath  = imageService.createWorkingFilesByProductType(type, file, size);
            formData.add("filePath", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @GetMapping("/api/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
//        System.out.println("TEST");
        Resource file = fileUploadService.getPreview(filename);
//        Resource file = imageService.getPreview(filename);

//        Path filePath = Paths.get("D:/fileService/temp/" + filename);
//        System.out.println(filePath);
//        Resource file = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/api/file/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private void moveFile() {

    }
}
