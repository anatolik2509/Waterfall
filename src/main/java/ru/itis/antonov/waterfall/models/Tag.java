package ru.itis.antonov.waterfall.models;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Tag {
    private long id;
    private String name;
}
