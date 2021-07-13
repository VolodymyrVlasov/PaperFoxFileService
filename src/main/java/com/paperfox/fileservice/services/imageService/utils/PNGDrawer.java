package com.paperfox.fileservice.services.imageService.utils;

import com.paperfox.fileservice.models.Size;
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
public class PNGDrawer {

    public static File scalePNGImage(BufferedImage gridImage, Size printingSize, File productDirectory) throws IOException {
        String FILE_TYPE = "png";
        String uniqName = UUID.randomUUID().toString().split("-")[0] + ".";
        File output = new File(productDirectory + "/print_" + uniqName + "" + FILE_TYPE);

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
                writer.write(metadata, new IIOImage(gridImage, null, metadata), writeParam);
            } finally {
                stream.close();
            }
            break;
        }
        return output;
    }

    private static void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {
        double INCH_2_CM = 25.4;
        double DPI = 450;
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
