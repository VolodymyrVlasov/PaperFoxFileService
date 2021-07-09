package com.paperfox.fileservice.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUploadService implements IFileUploadService {
    private final double MAX_SIZE = 500;
    private final String RESIZE_PREFIX_NAME = "thumb_";
    @Value("${tempPath}")
    private String temporaryPath;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        //todo: create directory with UUID name, dont use UUID in file name
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(temporaryPath + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @Override
    public Resource getPreview(String fileName) {
        try {
            Path filePath = Paths.get(temporaryPath + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                System.out.println("file is exist");
                Path fileSavePath = Paths.get(temporaryPath + RESIZE_PREFIX_NAME + fileName);
                Resource resourceThumb = new UrlResource(fileSavePath.toUri());
                if (!resourceThumb.exists() || !resourceThumb.isReadable()) {
                    File file = new File(fileSavePath.toString());
                    String[] temp = fileName.split("\\.");
                    if (temp[temp.length - 1].toLowerCase().equals("pdf")) {
                        ImageIO.write(renderPDF(fileName), "JPG", file);
                    } else {
                        ImageIO.write(resizeImage(fileName), "JPG", file);
                        System.out.println("file was write to storage");
                    }
                    return resourceThumb;
                }
                return resourceThumb;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public BufferedImage renderPDF(String fileName) throws IOException {
        Path filePath = Paths.get(temporaryPath + fileName);
        File originalFile = new File(filePath.toString());
        if (originalFile.exists()) {
            PDDocument doc = new PDDocument();
            try {
                PDFRenderer pr = new PDFRenderer(PDDocument.load(originalFile));
                return pr.renderImageWithDPI(0, 300);

            } finally {
                if (doc != null) {
                    doc.close();
                }
            }
        }
        throw new IOException("File doesn't exist");
    }

    public BufferedImage resizeImage(String fileName) throws IOException {
        Path filePath = Paths.get(temporaryPath + fileName);
        File originalFile = new File(filePath.toString());
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
            return outputImage;
        } else {
            double coefficient = height / width;
            int targetWidth = (int) Math.round(MAX_SIZE / coefficient);
            Image resultingImage = previewImage.getScaledInstance(targetWidth, (int) MAX_SIZE, Image.SCALE_SMOOTH);
            outputImage = new BufferedImage(
                    targetWidth, (int) MAX_SIZE, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            return outputImage;
        }
    }
}












