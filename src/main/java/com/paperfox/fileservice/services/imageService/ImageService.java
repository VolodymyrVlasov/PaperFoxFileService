package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import com.paperfox.fileservice.services.imageService.utils.PNGDrawer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;

@Component
public class ImageService extends ImageServiceUtils {

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
    private void createSquaredStickerFiles(MultipartFile originalFile, Size size, ProductType type) throws Exception {
        // create temp directory (name: short uuid + time stamp)

        // save in temp directory original file

        // create printing size
        Size printingSize = new Size();
        printingSize.setWidth(size.getWidth() + BLEED);
        printingSize.setHeight(size.getHeight() + BLEED);
        printingSize.setBorderRadius(size.getBorderRadius());

        // scale original image to printingSize and set 450 dpi  -> if pdf render it to png   @File
        BufferedImage scaledImage = super.getScaledToPrintSizeImage(originalFile, printingSize);
        File png = new PNGDrawer().scalePNGImage(scaledImage);

        // create svg cut contour using size @File

        // create pdf with two layers using cut contour(File) and printing image(File)  @File

        // create preview for frontend using complete pdf file(File) @String (filename)

        // delete files, leave only directory with: original file, file for print and preview file

        super.getPrintPDF(png, size, type);
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
