package com.paperfox.fileservice.services.imageService.utils;

import org.springframework.stereotype.Component;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
@Component
public class PNG {
    private double DPI = 300;
    private String TEMP_PATH = "C:/Users/User/Desktop/testImageTracer/temp/";

    public File scalePNGImage(BufferedImage gridImage) throws IOException, InterruptedException {
        String FILE_TYPE = "png";
        String uniqName = UUID.randomUUID().toString().split("-")[0] + ".";
        File output = new File(TEMP_PATH + uniqName + "" + FILE_TYPE);

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(FILE_TYPE); iw.hasNext(); ) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }

            setDPI(metadata);

            final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
            try {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(gridImage, null, metadata), writeParam);
            } finally {
                stream.close();
            }
            break;
        }
//        BufferedImage resultImage = ImageIO.read(output);
//        output.delete();
        return output;
    }

    private void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {
        double INCH_2_CM = 2.54;

        // for PMG, it's dots per millimeter
        double dotsPerMilli = 1.0 * DPI / 10 / INCH_2_CM;

        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }
}
