package com.paperfox.fileservice.services.imageService.utils;

import com.paperfox.fileservice.models.Size;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PDFDrawer {
    public static File convertToPDF(File rasterImage, Size printingSize, File destinationFolder) throws IOException {
        PDDocument pdf = new PDDocument();
        PDPage page = new PDPage();
        float c = 2.834645857142857f;
        PDRectangle mediaBox = new PDRectangle((float) (printingSize.getWidth() * c), (float) (printingSize.getHeight() * c));
        page.setMediaBox(mediaBox);
        pdf.addPage(page);
        PDPageContentStream stream = new PDPageContentStream(pdf, page);
        PDImageXObject image = PDImageXObject.createFromFileByContent(rasterImage, pdf);
        stream.drawImage(image, 0, 0, (float) (printingSize.getWidth() * c), (float) (printingSize.getHeight() * c));
        stream.close();
        File convertedFile = new File("/" + destinationFolder.toString() + "/printing_layer.pdf");
        pdf.save(convertedFile);
        pdf.close();
        return convertedFile;
    }

    public static File generatePrintingPDF(File printFile, File cutFile, File destinationFolder) throws IOException {
        PDDocument pdf1 = PDDocument.load(printFile);
        PDDocument pdf2 = PDDocument.load(cutFile);
        PDDocument outPdf = new PDDocument();

        // Create output PDF frame
        PDRectangle pdf1Frame = pdf1.getPage(0).getCropBox();
        PDRectangle pdf2Frame = pdf2.getPage(0).getCropBox();
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
        layerUtility.appendFormAsLayer(outPdfPage, formPdf1, afLeft, "print");
        double tx = (pdf1Frame.getWidth() - pdf2Frame.getWidth()) * 0.5;
        double ty = (pdf1Frame.getHeight() - pdf2Frame.getHeight()) * 0.5;
        AffineTransform afRight = AffineTransform.getTranslateInstance(tx, ty);
        layerUtility.appendFormAsLayer(outPdfPage, formPdf2, afRight, "cut");
        File resultPdf = new File(destinationFolder.toString() + "/printing_file.pdf");
        outPdf.save(resultPdf);
        pdf1.close();
        pdf2.close();
        outPdf.close();
        return resultPdf;
    }

    public static File renderPDF(Object pdf, File productDirectory) throws IOException {
        PDDocument pdfFile;
        System.out.println("file instance: " + (pdf.getClass()));
        if (pdf instanceof File) {
            pdfFile = PDDocument.load((File) pdf);
        } else {
            pdfFile = PDDocument.load(((MultipartFile) pdf).getInputStream());
        }
        BufferedImage image = new PDFRenderer(pdfFile).renderImageWithDPI(0, 350);
        File previewFile = new File(productDirectory + "/renderedPDF.png");
        ImageIO.write(image, "png", previewFile);
        pdfFile.close();
        return previewFile;
    }
}
