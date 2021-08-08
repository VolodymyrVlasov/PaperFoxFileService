package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.controllers.FileUploadController;
import com.paperfox.fileservice.models.Options;
import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import com.paperfox.fileservice.services.imageService.utils.PDFDrawer;
import com.paperfox.fileservice.services.imageService.utils.PNGDrawer;
import com.paperfox.fileservice.services.imageService.utils.SVGDrawer;
import org.apache.batik.transcoder.TranscoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageService extends ImageServiceUtils {
    @Value("${tempPath}")
    private String temporaryPath;
    private final double BLEED = 2;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);


    public String createWorkingFilesByProductType(Options options) throws Exception {
        if (options.productType == ProductType.FIGURE) {
            this.createFiguredStickerFiles(options.file);
        } else if (options.productType == ProductType.ROUND) {
            return this.createRoundStickerFiles(options);
        } else if (options.productType == ProductType.SQUARED) {
            return this.createSquaredStickerFiles(options);
        } else if (options.productType == ProductType.STICKER_SET) {
            this.createStickerSetFiles(options.file);
        }
        throw new Exception("unExisted ProductType exception " + options.productType);
    }

    // todo: make method return type model of file path which include: name of temp directory, name of original file, name of printing file, name of preview file
    private String createSquaredStickerFiles(Options options) throws Exception {
        // create temp product folder (name: short uuid + time stamp)
        // save in temp directory original file
        // create printing size
        // scale original image to printingSize and set 450 dpi -> if pdf render it to png   @File
        // create pdf file with cut contour using size @File
        // create pdf with two layers using cut contour(File) and printing image(File)  @File
        // create preview for frontend using complete pdf file(File) @String (filename)
        // delete files, leave only directory with: original file, file for print and preview file
        File productFolder = new File(temporaryPath + "/" + options.token);
        if (this.checkFolder(productFolder)) {
            Files.copy(options.file.getInputStream(), Paths.get(productFolder + "/original_" + options.file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            Size printingSize = getPrintingSize(options.size, BLEED);
            File scaledToPrintSizeRaster = PNGDrawer.scalePNGImage(options.file, printingSize, productFolder);
            File previewImage = createPreview(scaledToPrintSizeRaster, productFolder, options.token);
            File printingLayerPDF = PDFDrawer.convertToPDF(scaledToPrintSizeRaster, printingSize, productFolder);
            File cuttingLayerPDF = convertSVGtoPDF(SVGDrawer.getRectContour(options.size), productFolder);
            File composedPDF = PDFDrawer.generatePrintingPDF(printingLayerPDF, cuttingLayerPDF, productFolder);
            scaledToPrintSizeRaster.delete();
            printingLayerPDF.delete();
            cuttingLayerPDF.delete();
            return previewImage.getName();
        } else {
            return createPreview(ImageIO.read(options.file.getInputStream()), productFolder, options.token).getName();
        }
    }

    private void createFiguredStickerFiles(MultipartFile originalFile) {
    }

    private String createRoundStickerFiles(Options options) throws IOException, TranscoderException {
        File productFolder = new File(temporaryPath + "/" + options.token);
        if (this.checkFolder(productFolder)) {
            Files.copy(options.file.getInputStream(), Paths.get(productFolder + "/" + options.file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            Size printingSize = getPrintingSize(options.size, BLEED);
            File scaledToPrintSizeRaster = PNGDrawer.scalePNGImage(options.file, printingSize, productFolder);
            File previewImage = createPreview(scaledToPrintSizeRaster, productFolder, options.token);
            File printingLayerPDF = PDFDrawer.convertToPDF(scaledToPrintSizeRaster, printingSize, productFolder);
            File cuttingLayerPDF = convertSVGtoPDF(SVGDrawer.getCircleContour(options.size), productFolder);
            File composedPDF = PDFDrawer.generatePrintingPDF(printingLayerPDF, cuttingLayerPDF, productFolder);
            scaledToPrintSizeRaster.delete();
            printingLayerPDF.delete();
            cuttingLayerPDF.delete();
            return previewImage.getName();
        } else {
            return createPreview(ImageIO.read(options.file.getInputStream()), productFolder, options.token).getName();
        }
    }

    private void createStickerSetFiles(MultipartFile originalFile) {
    }

    private boolean checkFolder(File productFolder) {
        if (!productFolder.exists()) {
            productFolder.mkdirs();
            return true;
        } else {
            File[] fileList = productFolder.listFiles();
            for (File file : fileList) {
                file.delete();
            }
            return true;
        }
    }

    public Resource getPreview(String fileName, String token) throws MalformedURLException {
//        String dirName = fileName.split("_")[0];
        File root = new File(temporaryPath + "/" + token + "/" + fileName);
        System.out.println(root);
        if (root.exists()) {
            Path filePath = Paths.get(root.getPath());
            return new UrlResource(filePath.toUri());
        }
        throw new RuntimeException("Error: file not found");
    }
}
