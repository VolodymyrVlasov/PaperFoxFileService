package com.paperfox.fileservice.services.imageService.utils;

import com.paperfox.fileservice.models.Size;
import jankovicsandras.imagetracer.ImageTracer;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SVGDrawer {
    public static String getCircleContour(Size size) {
        double c = 5.29d;
        double diameter = size.getDiameter() * c;

        return "<svg width=\"" + diameter + "\" height=\"" + diameter + "\" viewBox=\"0 0 " + diameter + " " + diameter +
                "\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<circle cx=\"" + diameter * 0.5 + "\" cy=\"" + diameter * 0.5 +
                "\" r=\"50\" stroke=\"#FF7A00\" stroke-linecap=\"round\" stroke-dasharray=\"10 5\" />" +
                "</svg>";
    }

    public static String getRectContour(Size size) {
        double c = 5.29d;
        double width = size.getWidth() * c;
        double height = size.getHeight() * c;
        double borderRadius = size.getBorderRadius() * c;

        return "<svg width=\"" + width + "\" height=\"" + height + "\" viewBox=\"0 0 " + width + " " + height +
                "\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<rect x=\"0\" y=\"0\" width=\"" + width + "\" height=\"" + height + "\" rx=\"" + borderRadius + "\" stroke=\"#FF7A00\" stroke-linecap=\"round\" " +
                "stroke-dasharray=\"10 5\"/></svg>";
    }

    public static String getFiguredContour() {
        return "";
    }

    private String getCutContour(BufferedImage cutMaskImage) throws Exception {
        return ImageTracer.imageToSVG(cutMaskImage, getTraceOption(), null); // getPalette()
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
