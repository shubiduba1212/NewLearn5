package com.newrun5.create_course.dto;

public class CurriculumDto {
    private String topic;
    private String reason;
    private String difficulty;

    public CurriculumDto() {}

    public CurriculumDto(String topic, String reason, String difficulty) {
        this.topic = topic;
        this.reason = reason;
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "CurriculumDto{" +
                "topic='" + topic + '\'' +
                ", reason='" + reason + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
