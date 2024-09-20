package com.newrun5.save_pdf.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "data_items")
public class DataItem {
    private String title;
    private String content;
    private String embedding;

    public DataItem() {}

    // getters and setters

    public DataItem(String title, String content, String embedding) {
        this.title = title;
        this.content = content;
        this.embedding = embedding;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", embedding='" + embedding + '\'' +
                '}';
    }
}
