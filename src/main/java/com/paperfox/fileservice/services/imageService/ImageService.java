package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import com.paperfox.fileservice.services.imageService.utils.PNG;
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

    private void createSquaredStickerFiles(MultipartFile originalFile, Size size, ProductType type) throws Exception {
        Size printingSize = new Size();
        printingSize.setWidth(size.getWidth() + BLEED);
        printingSize.setHeight(size.getHeight() + BLEED);
        printingSize.setBorderRadius(size.getBorderRadius());
        BufferedImage scaledImage = super.getScaledToPrintSizeImage(originalFile, printingSize);
        File png = new PNG().scalePNGImage(scaledImage);
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
