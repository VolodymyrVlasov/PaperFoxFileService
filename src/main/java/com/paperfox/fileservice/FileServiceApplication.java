package com.paperfox.fileservice;

import com.paperfox.fileservice.services.FileUploadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	}

}
