package com.paperfox.fileservice.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class PreviewService {

    public void makeOffsetPathToVectorize(MultipartFile file) {
        // todo: make parallel contour to vectorize
    }

    public  void vectorizeImage() {
        // todo: make vector from raster file
    }

    public void mergePreviewImage(File pdfToPrint){
        // todo: create preview with biggest side 500px
    }
}
