package me.arkallic.mineCore.objects;

import java.util.List;
import java.util.UUID;

public class Mail {

    private final UUID author;
    private final List<String> messages;

    public Mail(UUID author, List<String> messages) {
        this.author = author;
        this.messages = messages;
    }

    public UUID getAuthor() {
        return author;
    }

    public List<String> getMessages() {
        return messages;
    }
}
