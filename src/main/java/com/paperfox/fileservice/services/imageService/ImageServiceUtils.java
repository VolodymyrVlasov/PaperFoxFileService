package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.ProductType;
import com.paperfox.fileservice.models.Size;
import com.paperfox.fileservice.services.imageService.utils.SVGConverterUtils;
import com.paperfox.fileservice.services.imageService.utils.SVGDrawer;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

@Component
public abstract class ImageServiceUtils {
    public final double DPI = 300.0d;
    private final double MAX_SIZE = 500;
    private final String RESIZE_PREFIX_NAME = "thumb_";

    public Size getPrintingSize(Size size, double bleed) {
        Size printingSize = new Size();
        printingSize.setWidth(size.getWidth() + bleed);
        printingSize.setHeight(size.getHeight() + bleed);
        printingSize.setBorderRadius(size.getBorderRadius());
        return printingSize;
    }

    public File convertSVGtoPDF(String svg, File destinationFolder) throws IOException, TranscoderException {
        File pdfFile = new File(destinationFolder.toString() + "/cut_" + UUID.randomUUID().toString().split("-")[0] + ".pdf");
        SVGConverterUtils converter = new SVGConverterUtils();
        converter.svg2PDF(svg, converter.getOutputStream(pdfFile));
        return pdfFile;
    }

    public File createPreview(File originalFile) throws IOException {
        BufferedImage previewImage = ImageIO.read(originalFile);
        BufferedImage outputImage;
        double width = previewImage.getWidth();
        double height = previewImage.getHeight();

        if (width >= height) {
            double coefficient = width / height;
            int targetHeight = (int) Math.round(MAX_SIZE / coefficient);
            Image resultingImage = previewImage.getScaledInstance((int) MAX_SIZE, targetHeight, Image.SCALE_SMOOTH);
            outputImage = new BufferedImage(
                    (int) MAX_SIZE, (int) Math.round(MAX_SIZE / coefficient), BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            File file = new File(originalFile.getParent() + "/" + RESIZE_PREFIX_NAME + originalFile.getName().split("\\.")[0] + ".png");
            originalFile.delete();
            ImageIO.write(outputImage, "png", file);
            return file;
        } else {
            double coefficient = height / width;
            int targetWidth = (int) Math.round(MAX_SIZE / coefficient);
            Image resultingImage = previewImage.getScaledInstance(targetWidth, (int) MAX_SIZE, Image.SCALE_SMOOTH);
            outputImage = new BufferedImage(
                    targetWidth, (int) MAX_SIZE, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            File file = new File(originalFile.getAbsolutePath() + RESIZE_PREFIX_NAME + originalFile.getName() + ".jpg");
            ImageIO.write(outputImage, "png", file);
            return file;
        }
    }

    private PDRectangle getMediaBox(Size size, int bleedMM) {
        float c = 2.834645857142857f;
        PDRectangle mediaBox = new PDRectangle((float) ((size.getWidth() + bleedMM * 2) * c), (float) ((size.getHeight() + bleedMM * 2) * c));
        return mediaBox;
    }


    public BufferedImage getScaledToPrintSizeImage(MultipartFile file, Size size) throws Exception {
        int widthPx = (int) Math.round(size.width / 25.4d * this.DPI);
        int heightPx = (int) Math.round(size.height / 25.4d * this.DPI);
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        Image image = originalImage.getScaledInstance(widthPx, heightPx, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(widthPx, heightPx, Image.SCALE_SMOOTH);
        scaledImage.getGraphics().drawImage(image, 0, 0, null);
//        String[] temp = file.getName().split(".");
//        String fileType = temp[temp.length - 1];
//        scaledImage = setDpi(scaledImage, fileType);
        return scaledImage;
    }

    public BufferedImage getCutMaskImage(BufferedImage originalImage, int offsetPixels) {
        // todo: optimize method
        // todo: implement scaleCanvas() from FileServiceApplication
        BufferedImage resultImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Color black = new Color(0, 0, 0, 255);

        for (int i = 0; i < originalImage.getWidth(); i++) {
            for (int j = 0; j < originalImage.getHeight(); j++) {
                if (originalImage.getRaster().getPixel(i, j, (int[]) null)[3] > 0) {
                    for (int p = 1; p <= offsetPixels; p++) {
                        if (i + p < originalImage.getWidth() && j + p < originalImage.getHeight()) {
                            resultImage.setRGB(i + p, j, black.getRGB());
                            resultImage.setRGB(i, j + p, black.getRGB());
                            resultImage.setRGB(i + p, j + p, black.getRGB());
                        }
                        if (i - p >= 0 && j - p >= 0) {
                            resultImage.setRGB(i - p, j, black.getRGB());
                            resultImage.setRGB(i, j - p, black.getRGB());
                            resultImage.setRGB(i - p, j - p, black.getRGB());
                        }
                        if (i - p >= 0 && j + p < originalImage.getHeight()) {
                            resultImage.setRGB(i - p, j + p, black.getRGB());
                        }
                        if (i + p < originalImage.getWidth() && j - p >= 0) {
                            resultImage.setRGB(i + p, j - p, black.getRGB());
                        }
                    }
                }
            }
        }

        return resultImage;
    }


    public void getPrintPDF(File rasterDesign, Size size, ProductType type) throws Exception {
        float c = 2.834645857142857f;

//        PDDocument pdf = new PDDocument();
//        PDPage page = new PDPage();

        if (type == ProductType.ROUND) {

        } else if (type == ProductType.SQUARED) {
            if (size.getBorderRadius() != 0) {
                int bleedMM = 1;
                String cutShapeString = SVGDrawer.getRectContour(size);
                File svgTempCutFile = new File("C:/Users/User/Desktop/testImageTracer/temp/" + UUID.randomUUID().toString().split("-")[0] + ".svg");
                File pdfTempCutFile = new File("C:/Users/User/Desktop/testImageTracer/temp/" + UUID.randomUUID().toString().split("-")[0] + ".pdf");

                FileUtils.writeStringToFile(svgTempCutFile, cutShapeString);

                SVGConverterUtils converter = new SVGConverterUtils();
                converter.svg2PDF(cutShapeString, converter.getOutputStream(pdfTempCutFile));

                PDDocument pdf = new PDDocument();
                PDPage page = new PDPage();
                page.setMediaBox(getMediaBox(size, bleedMM));
                pdf.addPage(page);
                PDPage cutPage = PDDocument.load(pdfTempCutFile).getPage(0);
                pdf.addPage(cutPage);
                PDPageContentStream stream = new PDPageContentStream(pdf, page);
                PDImageXObject image = PDImageXObject.createFromFileByContent(rasterDesign, pdf);
                stream.drawImage(image, bleedMM * c, bleedMM * c, (float) (size.getWidth() * c), (float) (size.getHeight() * c));
                stream.close();

                File fileToPrint = new File(
                        "C:/Users/User/Desktop/testImageTracer/temp/" +
                                UUID.randomUUID().toString().split("-")[0] +
                                "_roundRect_" + size.getWidth() + "x" + size.getHeight() + "x" + size.getBorderRadius() + "mm.pdf");
                pdf.save(fileToPrint);
                pdf.close();
//                svgTempCutFile.delete();
//                pdfTempCutFile.delete();
            } else {
                // todo: make file to print without bleeds for rectangle shape
            }
        } else if (type == ProductType.FIGURE) {
            // todo: make file to print with bleeds for figured shape
        } else {
            throw new Exception("product type undefined");
        }
    }


    public void getPreview(BufferedImage originalImage) {
        // scale image to 500 px in max length side
        // create cut mask image
        // trace cut mask image
        // make cut contour as dashed line
        // combine scaled image with cut contour
        // return buffered image
    }

    private BufferedImage setDpi(BufferedImage scaledImage, String fileType) throws Exception {
        BufferedImage resultImage = new BufferedImage(scaledImage.getWidth(), scaledImage.getHeight(), scaledImage.getType());
        if (fileType.toLowerCase().equals("png")) {
            // for PMG, it's dots per millimeter
            double dotsPerMilli = 1.0 * DPI / 10 / 0.0254d;
            final String formatName = "png";

            for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext(); ) {
                ImageWriter writer = iw.next();
                ImageWriteParam writeParam = writer.getDefaultWriteParam();
                ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
                IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
                if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                    continue;
                }

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
                final ImageOutputStream stream = ImageIO.createImageOutputStream(scaledImage);
                try {
                    writer.setOutput(stream);
                    writer.write(metadata, new IIOImage(resultImage, null, metadata), writeParam);
                } finally {
                    stream.close();
                }
                break;
            }

        } else if (fileType.toLowerCase().equals("jpg")) {
//            FileOutputStream fos = new FileOutputStream(scaledImage.);
//            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fos);
//            JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(images[i]);
//            jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
//            jpegEncodeParam.setXDensity(600);
//            jpegEncodeParam.setYDensity(600);
//            jpegEncodeParam.setQuality(1.0f, true);
//            jpegEncoder.encode(images[i], jpegEncodeParam);
//            fos.close();

        } else if (fileType.toLowerCase().equals("jpeg")) {

        } else {
            throw new Exception("unsupported file type");
        }
        //todo: set dpi attribute. Is logic different for different file type?
        return resultImage;
    }

    private HashMap<String, Float> getTraceOption() {
        HashMap<String, Float> options = new HashMap<>();
//      Tracing
        options.put("ltres", 100f);
        options.put("qtres", 100f);
        options.put("pathomit", 100f);
//      Color quantization
        options.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
        options.put("numberofcolors", 2f);
//      options.put("mincolorratio", 0.02f);
//      options.put("colorquantcycles", 255f);
//      SVG rendering
        options.put("scale", 1f);
//      options.put("roundcoords", 10f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
        options.put("lcpr", 0f);
//      options.put("qcpr", 0f);
//      options.put("desc", 1f); // 1f means true ; 0f means false: SVG descriptions deactivated
//      options.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height
        // Selective Gauss Blur
        options.put("blurradius", 2f); // 0f means deactivated; 1f .. 5f : blur with this radius
        options.put("blurdelta", 255f); // smaller than this RGB difference will be blurred
        return options;
    }

    private byte[][] getPalette() {
        // Palette
        // This is an example of a grayscale palette
        // please note that signed byte values [ -128 .. 127 ] will be converted to [ 0 .. 255 ] in the getsvgstring function
        byte[][] palette = new byte[8][4];
        for (int colorcnt = 0; colorcnt < 8; colorcnt++) {
            palette[colorcnt][0] = (byte) (-128 + colorcnt * 32); // R
            palette[colorcnt][1] = (byte) (-128 + colorcnt * 32); // G
            palette[colorcnt][2] = (byte) (-128 + colorcnt * 32); // B
            palette[colorcnt][3] = (byte) 127;                    // A
        }
        return palette;
    }
}
