package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.FeedType;
import ru.itis.antonov.waterfall.models.Group;
import ru.itis.antonov.waterfall.models.Profile;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group>{
    List<Group> profileGroups(Profile p);

    void subscribe(Profile p, Group g);

    void unsubscribe(Profile p, Group g);

    boolean isSubscribed(Profile p, Group g);

    Group getWithName(long id);
}
