package com.paperfox.fileservice;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "svg")
public class SVG {
    @XmlAttribute(name = "width")
    public int width;
    @XmlAttribute(name = "height")
    public int height;
    @XmlAttribute(name = "version")
    public String version;
//    @XmlAttribute(name = "xmlns")
//    public String xmlns;
    @XmlAttribute(name = "desc")
    public String desc;
    @XmlElement(name="path")
    public List<Path> pathList;

//    public int getWidth() {
//        return width;
//    }

//    public void setWidth(int width) {
//        this.width = width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }

//    public List<Path> getPathList() {
//        return pathList;
//    }

//    public void setPathList(List<Path> pathList) {
//        this.pathList = pathList;
//    }


    @Override
    public String toString() {
        return "SVG{" +
                "width=" + width +
                ", height=" + height +
                ", version='" + version + '\'' +
                ", desc='" + desc + '\'' +
                ", pathList=" + pathList +
                '}';
    }
}

@XmlRootElement(name = "path")
class Path {
    @XmlAttribute(name = "desc")
    public String desc;
    @XmlAttribute(name = "fill")
    public String fill;
    @XmlAttribute(name = "stroke")
    public int stroke;
    @XmlAttribute(name = "stroke-width")
    public int strokeWidth;
    @XmlAttribute(name = "stroke-linejoin")
    public String strokeLinejoin;
    @XmlAttribute(name = "stroke-linecap")
    public String strokeLinecap;
    @XmlAttribute(name = "stroke-dasharray")
    public double strokeDasharray;
    @XmlAttribute(name = "opacity")
    public double opacity;
    @XmlAttribute(name = "d")
    public String d;

//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getFill() {
//        return fill;
//    }
//
//    public void setFill(String fill) {
//        this.fill = fill;
//    }
//
//    public int getStroke() {
//        return stroke;
//    }
//
//    public void setStroke(int stroke) {
//        this.stroke = stroke;
//    }
//
//    public int getStrokeWidth() {
//        return strokeWidth;
//    }
//
//    public void setStrokeWidth(int strokeWidth) {
//        this.strokeWidth = strokeWidth;
//    }
//
//    public String getStrokeLinejoin() {
//        return strokeLinejoin;
//    }
//
//    public void setStrokeLinejoin(String strokeLinejoin) {
//        this.strokeLinejoin = strokeLinejoin;
//    }
//
//    public String getStrokeLinecap() {
//        return strokeLinecap;
//    }
//
//    public void setStrokeLinecap(String strokeLinecap) {
//        this.strokeLinecap = strokeLinecap;
//    }
//
//    public double getStrokeDasharray() {
//        return strokeDasharray;
//    }
//
//    public void setStrokeDasharray(double strokeDasharray) {
//        this.strokeDasharray = strokeDasharray;
//    }
//
//    public double getOpacity() {
//        return opacity;
//    }
//
//    public void setOpacity(double opacity) {
//        this.opacity = opacity;
//    }
//
//    public String getD() {
//        return d;
//    }
//
//    public void setD(String d) {
//        this.d = d;
//    }

    @Override
    public String toString() {
        return "Path{" +
                "desc='" + desc + '\'' +
                ", fill='" + fill + '\'' +
                ", stroke=" + stroke +
                ", strokeWidth=" + strokeWidth +
                ", strokeLinejoin='" + strokeLinejoin + '\'' +
                ", strokeLinecap='" + strokeLinecap + '\'' +
                ", strokeDasharray=" + strokeDasharray +
                ", opacity=" + opacity +
                ", d='" + d + '\'' +
                '}';
    }
}