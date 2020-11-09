package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.*;

import java.sql.Date;
import java.util.List;

public interface ProfileService {
    void createProfile(Profile p);

    void updateProfile(Profile p);

    void deleteProfile(Profile p);

    Profile getProfileById(long id);

    List<Article> getProfileArticles(Profile p, FeedType type, int begin, int end, Date updated);

    List<Group> getProfileGroups(Profile p);

    List<Profile> getProfileSubscribes(Profile p);

    List<Article> getLikedArticles(Profile p);

    List<Article> getDislikedArticles(Profile p);

    List<Article> getSavedArticles(Profile p, int begin, int end, Date updated);

    List<Comment> getSavedComments(Profile p, int begin, int end, Date updated);

    int getRate(Profile p);
}
