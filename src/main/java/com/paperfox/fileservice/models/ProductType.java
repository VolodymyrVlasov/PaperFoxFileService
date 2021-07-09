package com.paperfox.fileservice.models;

public enum ProductType {
    ROUND ("ROUND"),
    SQUARED ("SQUARED"),
    FIGURE  ("FIGURE"),
    STICKER_SET  ("STICKER_SET");

    public final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProductType asType(String str) throws Exception {
        for (ProductType type : values()) {
            if (type.name.equals(str)) {
                return type;
            }
        }
        throw new Exception("type not found");
     }
}
