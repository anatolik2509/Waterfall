package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.Profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public interface SecurityService {

    public static final String EMAIL_REGEXP = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String AUTH_COOKIE_NAME = "user_uuid";

    UUID registration(Profile p, String password, HttpSession session);

    Profile getProfileByUUID(UUID uuid);

    UUID authorize(String login, String password, HttpSession session);

    boolean isAuthenticated(HttpServletRequest request, HttpSession session);

    void validateProfile(Profile p);

    void logout(UUID uuid);

    void logout(HttpServletRequest req, HttpSession session);

    void fullLogout(Profile p);
}
