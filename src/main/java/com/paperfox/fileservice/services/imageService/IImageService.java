package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.Size;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface IImageService {
    BufferedImage getScaledToPrintSizeImage(MultipartFile file, Size size) throws IOException;

    BufferedImage getCutMaskImage(BufferedImage originalImage, int offsetPixels);

    File getCutContour(BufferedImage cutMaskImage) throws Exception;

    void getPrintPDF(File rasterDesign, File cutPath);

    void getPreview(BufferedImage originalImage);
}
