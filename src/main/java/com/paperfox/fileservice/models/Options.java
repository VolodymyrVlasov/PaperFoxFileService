package com.paperfox.fileservice.models;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class Options {
    public ProductType productType;
    public Size size;
    public String token;
    public MultipartFile file;
    public File productFolder;

    @Override
    public String toString() {
        return "\n\tOptions:" +
                "\n\t\tproductType=" + productType +
                "\n\t\tsize=" + size +
                "\n\t\ttoken='" + token + '\'' +
                "\n\t\tfile=" + file.getOriginalFilename() +
                "\n\t\tproductFolder=" + productFolder;
    }
}
