package com.example.yannis.mynote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListResponse {

    @SerializedName("items")
    private List<Note> notes = new ArrayList<>(0);

    public ListResponse() {
    }

    public List<Note> getNotes() {
        return notes;
    }
}
