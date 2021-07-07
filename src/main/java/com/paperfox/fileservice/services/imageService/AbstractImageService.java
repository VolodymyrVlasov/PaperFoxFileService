package com.paperfox.fileservice.services.imageService;

import com.paperfox.fileservice.models.Size;
import jankovicsandras.imagetracer.ImageTracer;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractImageService implements IImageService {
    public final double DPI = 300.0d;

    @Override
    public BufferedImage getScaledToPrintSizeImage(MultipartFile file, Size size) throws IOException {
        int widthPx = (int) Math.round(size.width / 25.4d * this.DPI);
        int heightPx = (int) Math.round(size.height / 25.4d * this.DPI);
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage scaledImage = (BufferedImage) originalImage.getScaledInstance(widthPx, heightPx, Image.SCALE_SMOOTH);
        scaledImage = setDpi(scaledImage);
        return scaledImage;
    }

    @Override
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

    @Override
    public File getCutContour(BufferedImage cutMaskImage) throws Exception {
        String vector = ImageTracer.imageToSVG(cutMaskImage, getTraceOption(), null); // getPalette()
//            ImageTracer.saveString("C:/Users/User/Desktop/testImageTracer/result/vector.svg", vector);
//            ImageTracer.saveString("C:/Users/User/Desktop/testImageTracer/result/vector.svg",
//                    ImageTracer.imageToSVG(cutMaskImage, options, null)
//            );
        // todo: dont work with storage in this class
        File file = new File("vector.svg");
        FileUtils.writeStringToFile(file, vector);
        return file;
    }

    @Override
    public void getPrintPDF(File rasterDesign, File cutPath) {
        // create new PDF document
        // push scaled in print size raster design (300 dpi png rgba) in PRINT layer
        // push scaled in print size vector shape as solid line in CUT layer
        // return serialized document
    }

    @Override
    public void getPreview(BufferedImage originalImage) {
        // scale image to 500 px in max length side
        // create cut mask image
        // trace cut mask image
        // make cut contour as dashed line
        // combine scaled image with cut contour
        // return buffered image
    }

    private BufferedImage setDpi(BufferedImage scaledImage) {
        BufferedImage resultImage = new BufferedImage(scaledImage.getWidth(), scaledImage.getHeight(), scaledImage.getType());
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
