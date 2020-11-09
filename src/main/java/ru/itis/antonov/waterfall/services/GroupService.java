package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.FeedType;
import ru.itis.antonov.waterfall.models.Group;

import java.sql.Date;
import java.util.List;

public interface GroupService {
    Group getGroupById(long id);

    void updateGroup(Group g);

    void deleteGroup(Group g);

    void createGroup(Group g);

    List<Article> getGroupArticles(Group g, FeedType feedType, int begin, int end, Date updated);
}
