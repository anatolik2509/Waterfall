package ru.itis.antonov.waterfall.models;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Profile {
    private long id;
    private String nickname;
    private String email;
    private String passwordHash;
}
