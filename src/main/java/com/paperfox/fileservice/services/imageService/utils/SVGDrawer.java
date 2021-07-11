package com.paperfox.fileservice.services.imageService.utils;

import com.paperfox.fileservice.models.Size;

public class SVGDrawer {
    public static String getCircle(Size size) {
        double c = 5.29d;
        double diameter = size.getDiameter() * c;

        return "<svg width=\"" + diameter + "\" height=\"" + diameter + "\" viewBox=\"0 0 " + diameter + " " + diameter +
                "\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<circle cx=\"" + diameter * 0.5 + "\" cy=\"" + diameter * 0.5 +
                "\" r=\"50\" stroke=\"#FF7A00\" stroke-linecap=\"round\" stroke-dasharray=\"10 5\" />" +
                "</svg>";
    }

    public static String getRounderRect(Size size) {
        double c = 5.29d;
        double width = size.getWidth() * c;
        double height = size.getHeight() * c;
        double borderRadius = size.getBorderRadius() * c;

        return "<svg width=\"" + width + "\" height=\"" + height + "\" viewBox=\"0 0 " + width + " " + height +
                "\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<rect x=\"0\" y=\"0\" width=\"" + width + "\" height=\"" + height + "\" rx=\"" + borderRadius + "\" stroke=\"#FF7A00\" stroke-linecap=\"round\" " +
                "stroke-dasharray=\"10 5\"/></svg>";
    }
}
