package com.paperfox.fileservice;

import com.paperfox.fileservice.services.FileUploadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);

        FileUploadService fileUploadService = new FileUploadService();


        String fileName = "1.png";

        String file = "<svg width=\"1148\" height=\"1114\" version=\"1.1\" desc=\"Created with ImageTracer.java version 1.1.2\">    <path desc=\"l 1 p 1\" fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"0.00392156862745098\" d=\"M 866.5 0.0 L 1148.0 0.5 L 1147.5 156.0 L 1137.5 135.0 L 1095.0 109.5 L 1094.5 108.0 L 1018.0 67.5 L 1014.5 63.0 L 982.5 48.0 L 980.5 48.0 L 936.5 28.0 L 934.5 28.0 L 893.5 11.0 L 891.5 12.0 L 866.5 0.0 Z\" />\n</svg>";

        try {
            Path filePath = Paths.get("C:/Users/User/Desktop/testImageTracer/" + fileName);
            File originalFile = new File(filePath.toString());
            BufferedImage previewImage = ImageIO.read(originalFile);
            previewImage = scaleCanvas(previewImage, 16);
//            previewImage = createOffsetPath(previewImage, 16);
//            String vector = makeVector(previewImage);
//
//            String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
//            DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
//            Document doc = impl.createDocument(vector, "svg", null);

            File svg = new File("C:/Users/User/Desktop/testImageTracer/result/vector.svg");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(svg);
            Node node = document.getDocumentElement();
            NodeList nodeList = node.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node attributeOpacity, attributeColor;
                Node item = nodeList.item(i);
                if (item.getAttributes() != null) {
                    attributeOpacity = nodeList.item(i).getAttributes().getNamedItem("opacity");
                    attributeColor = item.getAttributes().getNamedItem("fill");
                    double opacityValue = Double.parseDouble(attributeOpacity.getNodeValue());
                    if (opacityValue < 0.9) {
                        attributeOpacity.setNodeValue("1.0");
                        attributeColor.setNodeValue("rgb(255,0,0)");
                    }
//                    System.out.println(nodeList.item(i).getAttributes().getNamedItem("fill").getNodeValue());
                }
            }

//            System.out.println(document);
            DOMSource dom = new DOMSource(document);
            Transformer transformer = TransformerFactory.newDefaultInstance().newTransformer();
            Result result = new StreamResult(svg);
            transformer.transform(dom, result);
            ImageIO.write(previewImage, "png", new File("C:/Users/User/Desktop/testImageTracer/result/original_image_with_bleeds_" + fileName));

            ImageIO.write(previewImage, "png", new File("C:/Users/User/Desktop/testImageTracer/result/original_image_with_bleeds_" + fileName));
            ImageIO.write(previewImage, "png", new File("C:/Users/User/Desktop/testImageTracer/result/raster_cutting_mask_" + fileName));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage paintBlackPixels(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int[] pixel = image.getRaster().getPixel(i, j, (int[]) null);
                if (pixel.length == 4) {
                    if (pixel[3] != 0 && pixel[0] != 255 && pixel[1] != 255 && pixel[2] != 255) {
                        resultImage.setRGB(i, j, new Color(0, 0, 0, 255).getRGB());
                    }
                } else {
                    if (pixel[0] != 255 && pixel[1] != 255 && pixel[2] != 255) {
                        resultImage.setRGB(i, j, new Color(0, 0, 0, 255).getRGB());
                    }
                }
            }
        }
        return resultImage;
    }

    /**
     * Make image size bigger up to scaleSize x2 and copy image in the center position
     *
     * @param image     - original image
     * @param scaleSize - bleed size in pixels
     * @return new image with transparent bleeds
     */

    public static BufferedImage scaleCanvas(BufferedImage image, int scaleSize) {
        BufferedImage resultImage = new BufferedImage(
                image.getWidth() + scaleSize * 2, image.getHeight() + scaleSize * 2, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < image.getWidth(); i++) {
            if (i + scaleSize >= resultImage.getWidth()) break;
            for (int j = 0; j < image.getHeight(); j++) {
                if (j + scaleSize >= resultImage.getHeight()) break;
                int[] targetPixel = image.getRaster().getPixel(i, j, (int[]) null);
                if (targetPixel.length == 3) {
                    resultImage.setRGB(
                            i + scaleSize,
                            j + scaleSize,
                            new Color(targetPixel[0], targetPixel[1], targetPixel[2], 255).getRGB());
                } else {
                    resultImage.setRGB(
                            i + scaleSize,
                            j + scaleSize,
                            new Color(targetPixel[0], targetPixel[1], targetPixel[2], targetPixel[3]).getRGB());
                }
            }
        }
        return resultImage;
    }

//    public static BufferedImage createOffsetPath(BufferedImage image, int offsetSize) {
//        BufferedImage resultImage = new BufferedImage(
//                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//        Color black = new Color(0, 0, 0, 255);
//
//        for (int i = 0; i < image.getWidth(); i++) {
//            for (int j = 0; j < image.getHeight(); j++) {
//                if (image.getRaster().getPixel(i, j, (int[]) null)[3] > 0) {
//                    for (int p = 1; p <= offsetSize; p++) {
//                        if (i + p < image.getWidth() && j + p < image.getHeight()) {
//                            resultImage.setRGB(i + p, j, black.getRGB());
//                            resultImage.setRGB(i, j + p, black.getRGB());
//                            resultImage.setRGB(i + p, j + p, black.getRGB());
//                        }
//
//                        if (i - p >= 0 && j - p >= 0) {
//                            resultImage.setRGB(i - p, j, black.getRGB());
//                            resultImage.setRGB(i, j - p, black.getRGB());
//                            resultImage.setRGB(i - p, j - p, black.getRGB());
//                        }
//
//                        if (i - p >= 0 && j + p < image.getHeight()) {
//                            resultImage.setRGB(i - p, j + p, black.getRGB());
//                        }
//                        if (i + p < image.getWidth() && j - p >= 0) {
//                            resultImage.setRGB(i + p, j - p, black.getRGB());
//                        }
//                    }
//                }
//            }
//        }
//        return resultImage;
//    }
//
//    public static String makeVector(BufferedImage imageToTrace) {
//        String vector = "no data";
//        try {
//            // Options
//            HashMap<String, Float> options = new HashMap<String, Float>();
//
//            // Tracing
//            options.put("ltres", 100f);
//            options.put("qtres", 100f);
//            options.put("pathomit", 100f);
////            // Color quantization
//            options.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
//            options.put("numberofcolors", 2f);
////            options.put("mincolorratio", 0.02f);
////            options.put("colorquantcycles", 255f);
//            // SVG rendering
//            options.put("scale", 1f);
////            options.put("roundcoords", 10f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
//            options.put("lcpr", 0f);
////            options.put("qcpr", 0f);
////            options.put("desc", 1f); // 1f means true ; 0f means false: SVG descriptions deactivated
////            options.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height
//
//            // Selective Gauss Blur
//            options.put("blurradius", 2f); // 0f means deactivated; 1f .. 5f : blur with this radius
//            options.put("blurdelta", 255f); // smaller than this RGB difference will be blurred
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
//            vector = ImageTracer.imageToSVG(imageToTrace, options, null);
//            ImageTracer.saveString("C:/Users/User/Desktop/testImageTracer/result/vector.svg", vector);
////            return vector;
//            ImageTracer.saveString("C:/Users/User/Desktop/testImageTracer/result/vector.svg",
//                    ImageTracer.imageToSVG(imageToTrace, options, null)
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return vector;
//    }
}
