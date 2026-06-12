package com.henheang.heangapicenter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevToArticleDto {

    private Long id;
    private String title;
    private String url;
    private String description;

    @JsonProperty("published_at")
    private String publishedAt;

    @JsonProperty("reading_time_minutes")
    private Integer readingTimeMinutes;

    @JsonProperty("tag_list")
    private List<String> tagList;

    @JsonProperty("cover_image")
    private String coverImage;

    @JsonProperty("social_image")
    private String socialImage;

    @JsonProperty("positive_reactions_count")
    private Integer positiveReactionsCount;

    @JsonProperty("comments_count")
    private Integer commentsCount;

    @JsonProperty("body_html")
    private String bodyHtml;

    private UserDto user;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserDto {
        private String name;
        private String username;

        @JsonProperty("profile_image")
        private String profileImage;
    }
}
