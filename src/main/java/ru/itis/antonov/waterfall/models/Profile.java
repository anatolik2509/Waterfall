package ru.itis.antonov.waterfall.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Profile {
    private long id;
    private String nickname;
    private String email;
    private String passwordHash;
}
