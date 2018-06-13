package com.example.yannis.mynote;


import java.util.Calendar;

public class Note {

    private String content;
    private long dateInMillis;
    private String websafeKey;

    public Note() {

    }

    public Note(String content) {
        this.content = content;
        this.dateInMillis = System.currentTimeMillis();
//        dateInMillis = 1L;
    }

    public String getContent() {
        return content;
    }

    public long getDateInMillis() {
        return dateInMillis;
    }

    public String getWebsafeKey() {
        return websafeKey;
    }

    @Override
    public String toString() {
        // This is called by the ArrayAdapter class to get a textual description of the item
        return content;
    }
}
