package com.paperfox.fileservice;

import com.paperfox.fileservice.services.FileUploadService;
import jankovicsandras.imagetracer.ImageTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootApplication
public class FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);

        FileUploadService fileUploadService = new FileUploadService();
//
//		try {
//
//			BufferedImage bufferedImage = fileUploadService.resizeImage("C:/Users/User/Desktop/test.jpg");
//			File file = new File("C:/Users/User/Desktop/resize.jpg");
//			ImageIO.write(bufferedImage, "JPG", file);
//			System.out.println(
//					"width: " + bufferedImage.getWidth() +
//							"px height: " + bufferedImage.getHeight()
//			);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        //C:\Users\User\Desktop\TEST IMAGE TRASSER

//        try {
//            // Options
//            HashMap<String, Float> options = new HashMap<String, Float>();
//
//            // Tracing
//            options.put("ltres", 1f);
//            options.put("qtres", 1f);
//            options.put("pathomit", 8f);
//
//            // Color quantization
//            options.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
//            options.put("numberofcolors", 1f);
//            options.put("mincolorratio", 0.02f);
//            options.put("colorquantcycles", 1f);
//
//            // SVG rendering
//            options.put("scale", 1f);
//            options.put("roundcoords", 1f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
//            options.put("lcpr", 0f);
//            options.put("qcpr", 0f);
//            options.put("desc", 1f); // 1f means true ; 0f means false: SVG descriptions deactivated
//            options.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height
//
//            // Selective Gauss Blur
//            options.put("blurradius", 0f); // 0f means deactivated; 1f .. 5f : blur with this radius
//            options.put("blurdelta", 20f); // smaller than this RGB difference will be blurred
//
//            // Palette
//            // This is an example of a grayscale palette
//            // please note that signed byte values [ -128 .. 127 ] will be converted to [ 0 .. 255 ] in the getsvgstring function
//            byte[][] palette = new byte[8][4];
//            for (int colorcnt = 0; colorcnt < 8; colorcnt++) {
//                palette[colorcnt][0] = (byte) (-128 + colorcnt * 32); // R
//                palette[colorcnt][1] = (byte) (-128 + colorcnt * 32); // G
//                palette[colorcnt][2] = (byte) (-128 + colorcnt * 32); // B
//                palette[colorcnt][3] = (byte) 127;              // A
//            }
//
//
//            ImageTracer.saveString("C:/Users/User/Desktop/testImageTracer/2_vectorized.svg",
//                    ImageTracer.imageToSVG("C:/Users/User/Desktop/testImageTracer/2.jpg", null, null)
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            Path filePath = Paths.get("C:/Users/User/Desktop/testImageTracer/1.png");
            File originalFile = new File(filePath.toString());
            BufferedImage previewImage = ImageIO.read(originalFile);
            previewImage = paintPixels(previewImage);
            ImageIO.write(previewImage, "png", new File("C:/Users/User/Desktop/testImageTracer/1_changed.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage paintPixels(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
               int[] pixel = image.getRaster().getPixel(i, j, (int[]) null);

               if (pixel[3] == 255 && pixel[0] != 255 && pixel[1] != 255 && pixel[2] != 255) {
                   resultImage.setRGB(i, j, new Color(0, 0, 0, 255).getRGB());
               }
            }
        }

        for (int i = 0; i < resultImage.getWidth(); i++) {
            for (int j = 0; j < resultImage.getHeight(); j++) {
                int[] pixel = resultImage.getRaster().getPixel(i, j, (int[]) null);
                System.out.println(Arrays.asList(pixel));
            }
        }

        return resultImage;
    }
}
