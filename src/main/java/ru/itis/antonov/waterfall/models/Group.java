package ru.itis.antonov.waterfall.models;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Group {
    private long id;
    private String name;
    private String description;
}
