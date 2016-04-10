package com.example.mattiaspernhult.p2.models;

import java.io.Serializable;

/**
 * Created by mattiaspernhult on 2015-10-29.
 */
public class TextChat implements Serializable {

    private String message;
    private String name;
    private String group;
    private byte[] imageBuffer;
    private boolean image;

    public TextChat(String message, String name, String group) {
        this.message = message;
        this.name = name;
        this.group = group;
        this.image = false;
    }

    public TextChat(String message, String name, String group, byte[] imageBuffer) {
        this.message = message;
        this.name = name;
        this.group = group;
        this.imageBuffer = imageBuffer;
        this.image = true;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getImageBuffer() {
        return imageBuffer;
    }

    public boolean isImage() {
        return image;
    }
}
