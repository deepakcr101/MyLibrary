package com.deepak.library.dto;

import lombok.Data;

@Data
public class AddBookRequest {
    private String title;
    private String authorName;

    // No-args constructor (Required for Jackson)
    public AddBookRequest() {}

    // Add this All-args constructor
    public AddBookRequest(String title, String authorName) {
        this.title = title;
        this.authorName = authorName;
    }

    // ... getters and setters
}

