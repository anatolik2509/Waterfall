package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.FeedType;
import ru.itis.antonov.waterfall.models.Group;
import ru.itis.antonov.waterfall.repositories.ArticleRepository;
import ru.itis.antonov.waterfall.repositories.GroupRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class GroupServiceJdbcImpl implements GroupService {

    private GroupRepository groupRepository;

    private ArticleRepository articleRepository;

    public GroupServiceJdbcImpl(GroupRepository groupRepository, ArticleRepository articleRepository) {
        this.groupRepository = groupRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Group getGroupById(long id) {
        Optional<Group> a = groupRepository.findById(id);
        return a.orElse(null);
    }

    @Override
    public void updateGroup(Group g) {
        groupRepository.update(g);
    }

    @Override
    public void deleteGroup(Group g) {
        groupRepository.delete(g);
    }

    @Override
    public void createGroup(Group g) {
        groupRepository.save(g);
    }

    @Override
    public List<Article> getGroupArticles(Group g, FeedType feedType, int begin, int end, Date updated) {
        return articleRepository.getArticlesByGroup(g, feedType, begin, end, updated);
    }
}
