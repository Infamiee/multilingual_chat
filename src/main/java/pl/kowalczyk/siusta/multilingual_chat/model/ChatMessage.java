package pl.kowalczyk.siusta.multilingual_chat.model;

import java.time.LocalDateTime;


public class ChatMessage {

    private final LocalDateTime time;
    private final String from;
    private final String message;


    public ChatMessage(String from, String message) {
        this.from = from;
        this.message = message;

        this.time = LocalDateTime.now();
    }



    public LocalDateTime getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }


}

