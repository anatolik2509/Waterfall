package ru.itis.antonov.waterfall.models;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Article {
    private long id;
    private String title;
    private String content;
    private Profile author;
    private Group group;
    private List<Tag> tags;
    private List<Comment> comments;
    private Date date;
    private int rate;
    private int userRate;
    private boolean saved;
    private int commentAmount;
}
