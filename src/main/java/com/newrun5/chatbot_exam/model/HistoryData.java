package com.newrun5.chatbot_exam.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "history_data")
public class HistoryData {

    @Id
    private String id;
    private String title;
    private String content;
    private List<Float> embedding;

    public HistoryData() {}

    public HistoryData(String id, String title, String content, List<Float> embedding) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.embedding = embedding;
    }

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "HistoryData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", embedding=" + embedding +
                '}';
    }
}

