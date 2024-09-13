package com.newrun5.create_cur_ver2.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "financial_data")
public class DataItem {

    private String title;
    private String link;
    private String snippet;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}

