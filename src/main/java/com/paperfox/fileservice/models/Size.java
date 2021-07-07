package com.paperfox.fileservice.models;

public class Size {
    public double width;
    public double height;
    public double diameter;
    public double borderRadius;

    public Size(double width, double height, double diameter, double borderRadius) {
        this.width = width;
        this.height = height;
        this.diameter = diameter;
        this.borderRadius = borderRadius;
    }

//    public Size(int width, int height) {
//        this.width = width;
//        this.height = height;
//    }

    public Size(int width, int height, int borderRadius) {
        this.width = width;
        this.height = height;
        this.borderRadius = borderRadius;
    }

    public Size(int diameter) {
        this.diameter = diameter;
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                ", diameter=" + diameter +
                ", borderRadius=" + borderRadius +
                '}';
    }
}