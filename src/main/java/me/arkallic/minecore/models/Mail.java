package me.arkallic.minecore.models;

import java.util.List;
import java.util.UUID;

public class Mail {

    private final String author;
    private final String message;

    public Mail(String author, String message) {
        this.author = author;
        this.message= message;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
