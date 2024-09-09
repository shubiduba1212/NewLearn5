package com.newrun5.springai;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleRequest
{
    private String title;
    private String content;
    private String author;
    private Date createdAt; // 작성일 필드

    @JsonSetter
    public void setCreatedAt(Date createdAt)
    {
        // createdAt이 null인 경우 현재 날짜로 설정
        if (createdAt == null)
        {
            this.createdAt = new Date();
        }
        else
        {
            this.createdAt = createdAt;
        }
    }
}

