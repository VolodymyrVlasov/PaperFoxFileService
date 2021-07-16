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
import java.util.UUID;

@Component
public class ImageService extends ImageServiceUtils {
    @Value("${tempPath}")
    private String temporaryPath;
    private final double BLEED = 2;

    public String createWorkingFilesByProductType(ProductType type, MultipartFile originalFile, Size size) throws Exception {
        if (type == ProductType.FIGURE) {
            this.createFiguredStickerFiles(originalFile);
        } else if (type == ProductType.ROUND) {
            this.createRoundStickerFiles(originalFile);
        } else if (type == ProductType.SQUARED) {
            return this.createSquaredStickerFiles(originalFile, size, type);
        } else if (type == ProductType.STICKER_SET) {
            this.createStickerSetFiles(originalFile);
        }
        throw new Exception("unExisted ProductType exception");
    }

    // todo: make method return type model of file path which include: name of temp directory, name of original file, name of printing file, name of preview file
    private String createSquaredStickerFiles(MultipartFile originalFile, Size productSize, ProductType type) throws Exception {
        // create temp product folder (name: short uuid + time stamp)
        // save in temp directory original file
        // create printing size
        // scale original image to printingSize and set 450 dpi -> if pdf render it to png   @File
        // create pdf file with cut contour using size @File
        // create pdf with two layers using cut contour(File) and printing image(File)  @File
        // create preview for frontend using complete pdf file(File) @String (filename)
        // delete files, leave only directory with: original file, file for print and preview file
        String folderName = UUID.randomUUID().toString().split("-")[0];
        File productFolder = new File(temporaryPath + folderName);
        if (!productFolder.exists()) productFolder.mkdirs();
        Files.copy(originalFile.getInputStream(), Paths.get(productFolder + "/original_" + originalFile.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
        Size printingSize = getPrintingSize(productSize, BLEED);
        File rasterPrintingImage = PNGDrawer.scalePNGImage(ImageIO.read(originalFile.getInputStream()), printingSize, productFolder);
        File printingSizeImage = PDFDrawer.convertToPDF(rasterPrintingImage, printingSize, productFolder);
        File cutContour = convertSVGtoPDF(SVGDrawer.getRectContour(productSize), productFolder);
        File printingPDF = PDFDrawer.generatePrintingPDF(printingSizeImage, cutContour, productFolder);
        File pr = createPreview(PDFDrawer.renderPDF(printingPDF));
        rasterPrintingImage.delete();
        printingSizeImage.delete();
        cutContour.delete();
        return pr.getName();
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
