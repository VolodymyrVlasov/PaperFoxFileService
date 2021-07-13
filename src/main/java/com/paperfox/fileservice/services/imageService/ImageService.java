package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import com.paperfox.fileservice.services.imageService.utils.PDFDrawer;
import com.paperfox.fileservice.services.imageService.utils.PNGDrawer;
import com.paperfox.fileservice.services.imageService.utils.SVGDrawer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Component
public class ImageService extends ImageServiceUtils {
    @Value("${tempPath}")
    private String temporaryPath;
    private final double BLEED = 2;

    public void createWorkingFilesByProductType(ProductType type, MultipartFile originalFile, Size size) throws Exception {
        if (type == ProductType.FIGURE) {
            this.createFiguredStickerFiles(originalFile);
        } else if (type == ProductType.ROUND) {
            this.createRoundStickerFiles(originalFile);
        } else if (type == ProductType.SQUARED) {
            this.createSquaredStickerFiles(originalFile, size, type);
        } else if (type == ProductType.STICKER_SET) {
            this.createStickerSetFiles(originalFile);
        } else {
            throw new Exception("unExisted ProductType exception");
        }
    }


    // todo: make method return type model of file path which include: name of temp directory, name of original file, name of printing file, name of preview file
    private void createSquaredStickerFiles(MultipartFile originalFile, Size productSize, ProductType type) throws Exception {
        // create temp product folder (name: short uuid + time stamp)
        File productFolder = new File(temporaryPath + UUID.randomUUID().toString().split("-")[0]);
        if (!productFolder.exists()) {
            productFolder.mkdirs();
        }

        // save in temp directory original file
        Files.copy(originalFile.getInputStream(), Paths.get(productFolder + "/original_" + originalFile.getName()),
                StandardCopyOption.REPLACE_EXISTING);

        // create printing size
        Size printingSize = getPrintingSize(productSize, BLEED);

        // scale original image to printingSize and set 450 dpi -> if pdf render it to png   @File
        File printingSizeImage = PNGDrawer.scalePNGImage(ImageIO.read(originalFile.getInputStream()), printingSize, productFolder);

        // create pdf file with cut contour using size @File
        File cutContour = convertSVGtoPDF(SVGDrawer.getRectContour(productSize), productFolder);

        // create pdf with two layers using cut contour(File) and printing image(File)  @File
        File printingPDF = PDFDrawer.generatePrintingPDF(printingSizeImage,cutContour,productFolder);

        // create preview for frontend using complete pdf file(File) @String (filename)
        File previewImage = PDFDrawer.renderPDF(printingPDF, productFolder);

        // delete files, leave only directory with: original file, file for print and preview file
//        printingSizeImage.delete();
//        cutContour.delete();

    }

    private void createFiguredStickerFiles(MultipartFile originalFile) {
    }

    private void createRoundStickerFiles(MultipartFile originalFile) {

    }

    private void createStickerSetFiles(MultipartFile originalFile) {

    }

    public Resource getPreview(String fileName) {
        // todo: return result in delayed time
        return null;
    }
}
