package com.paperfox.fileservice.services.imageService.utils;

import com.paperfox.fileservice.models.Size;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

@Component
public class PNGDrawer {
    private final static int DPI = 350;

    public static File scalePNGImage(MultipartFile originalImage, Size mmSize, File productDirectory) throws IOException {
        String FILE_TYPE = "png";
        BufferedImage image;
        File renderedPDF = null;
        System.out.println("Content type: " + originalImage.getContentType());
        if (Objects.equals(originalImage.getContentType(), "application/pdf")) {
            renderedPDF = PDFDrawer.renderPDF(originalImage, productDirectory);
            image = ImageIO.read(renderedPDF);
        } else {
            image = ImageIO.read(originalImage.getInputStream());
        }
        Size pixelSize = getPrintingSizeInPx(mmSize);
        Image scaledImage = image.getScaledInstance((int) pixelSize.width, (int) pixelSize.height, Image.SCALE_SMOOTH);
        image = new BufferedImage((int) pixelSize.width, (int) pixelSize.height, BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(scaledImage, 0, 0, null);
//        String[] arr = originalImage.getOriginalFilename().split("\\.");
//        String fileName = originalImage.getOriginalFilename().replaceAll(arr[arr.length - 1], "");
        File output = new File(productDirectory + "/print_size." + FILE_TYPE);

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(FILE_TYPE); iw.hasNext(); ) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }

            setDPI(metadata);
            final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
            try {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            } finally {
                stream.close();
            }
            break;
        }
        if (renderedPDF != null) {
            renderedPDF.delete();
        }
        return output;
    }

    private static Size getPrintingSizeInPx(Size size) {
        double pixelPerMillimeter = DPI / 25.4;
        Size pixelSize = new Size();
        if (size.getDiameter() > 0) {
            pixelSize.setWidth(Math.round(size.getDiameter() * pixelPerMillimeter));
            pixelSize.setHeight(Math.round(size.getDiameter() * pixelPerMillimeter));
        } else {
            pixelSize.setWidth(Math.round(size.getWidth() * pixelPerMillimeter));
            pixelSize.setHeight(Math.round(size.getHeight() * pixelPerMillimeter));
        }
        System.out.println("\n<-- pixelSize -->" +
                "\n\t\tdiameter: " + size.getDiameter() + "mm. " +
                "\n\t\twidth: " + size.getWidth() + "mm. " + pixelSize.getWidth() + "px." +
                "\n\t\theight: " + size.getHeight() + "mm. " + pixelSize.getHeight() + "px.");
        return pixelSize;
    }

    private static void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {
        double INCH_2_CM = 25.4;
        double dotsPerMillimeter = DPI / INCH_2_CM;

        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMillimeter));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMillimeter));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }
}
