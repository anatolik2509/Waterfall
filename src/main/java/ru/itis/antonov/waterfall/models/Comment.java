package ru.itis.antonov.waterfall.models;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Comment {
    private Long id;
    private Article article;
    private Comment parent;
    private Profile author;
    private String content;
    private List<Comment> childes;
    private List<String> attachmentsPaths;
    private Date date;
    private int rate;
    private int userRate;
}
