package com.paperfox.fileservice.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class PrintService {
    public void resizeToPrintSize(MultipartFile file, Size size) {
        //todo: resize image ti direct size in mm  with 300 dpi
    }

    public void makeOffsetPathToVectorize(MultipartFile file) {
        // todo: make parallel contour to vectorize
    }

    public  void vectorizeImage() {
        // todo: make vector from raster file
    }

    public void mergeFileToPrint(File rasterDesign, File cutPath){
        // todo: merge image to pdf file with 2 layers for cut and print
    }

    public void createPreviewFromFileToPrint(File pdfToPrint){
        // todo: create preview with biggest side 500px
    }
}
