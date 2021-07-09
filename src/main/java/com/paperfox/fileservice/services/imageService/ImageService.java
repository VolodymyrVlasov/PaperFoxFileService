package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.ProductType;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageService extends AbstractImageService {

    public void createWorkingFilesByProductType(ProductType type, MultipartFile originalFile) throws Exception {
        if (type == ProductType.FIGURE) {
            this.createFiguredFiles(originalFile);
        } else if (type == ProductType.ROUND || type == ProductType.SQUARED) {
            this.createStandardShapeFiles(originalFile);
        } else if (type == ProductType.STICKER_SET) {
            this.createStickerSetFiles(originalFile);
        } else {
            throw new Exception("unExisted ProductType exception");
        }
    }

    private void createFiguredFiles(MultipartFile originalFile) {
    }

    private void createStandardShapeFiles(MultipartFile originalFile) {

    }

    private void createStickerSetFiles(MultipartFile originalFile) {

    }

    public Resource getPreview(String fileName){
        // todo: return result in delayed time
        return null;
    }
}
