package com.paperfox.fileservice.services.imageService.utils;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DPFDrawer {
    public static PDDocument generateSideBySidePDF(File printFile, File cutFile) throws IOException {
        PDDocument pdf1 = null;
        PDDocument pdf2 = null;
        PDDocument outPdf = null;

        try {
            pdf1 = PDDocument.load(printFile);
            pdf2 = PDDocument.load(cutFile);
            outPdf = new PDDocument();

            // Create output PDF frame
            PDRectangle pdf1Frame = pdf1.getPage(0).getCropBox();
            PDRectangle outPdfFrame = new PDRectangle(pdf1Frame.getWidth(), pdf1Frame.getHeight());

            // Create output page with calculated frame and add it to the document
            COSDictionary dict = new COSDictionary();
            dict.setItem(COSName.TYPE, COSName.PAGE);
            dict.setItem(COSName.MEDIA_BOX, outPdfFrame);
            dict.setItem(COSName.CROP_BOX, outPdfFrame);
            dict.setItem(COSName.ART_BOX, outPdfFrame);
            PDPage outPdfPage = new PDPage(dict);
            outPdf.addPage(outPdfPage);

            // Source PDF pages has to be imported as form XObjects to be able to insert them at a specific point in the output page
            LayerUtility layerUtility = new LayerUtility(outPdf);
            PDFormXObject formPdf1 = layerUtility.importPageAsForm(pdf1, 0);
            PDFormXObject formPdf2 = layerUtility.importPageAsForm(pdf2, 0);

            // Add form objects to output page
            AffineTransform afLeft = new AffineTransform();
            layerUtility.appendFormAsLayer(outPdfPage, formPdf1, afLeft, "left");
            AffineTransform afRight = AffineTransform.getTranslateInstance(0.0, 0.0);
            layerUtility.appendFormAsLayer(outPdfPage, formPdf2, afRight, "right");
            return outPdf;
        } finally {
            if (pdf1 != null) pdf1.close();
            if (pdf2 != null) pdf2.close();
            if (outPdf != null) outPdf.close();
            printFile.delete();
            cutFile.delete();
        }
    }

    public BufferedImage renderPDF(PDDocument pdf) throws IOException {
        try {
            return new PDFRenderer(pdf).renderImageWithDPI(0, 300);
        } finally {
            if (pdf != null) {
                pdf.close();
            }
        }
    }
}
