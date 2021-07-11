package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface IImageService {
    BufferedImage getScaledToPrintSizeImage(MultipartFile file, Size size) throws Exception;

    BufferedImage getCutMaskImage(BufferedImage originalImage, int offsetPixels);

    File getCutContour(BufferedImage cutMaskImage) throws Exception;

    void getPrintPDF(File rasterDesign, Size size, ProductType type) throws Exception;

    void getPreview(BufferedImage originalImage);
}
