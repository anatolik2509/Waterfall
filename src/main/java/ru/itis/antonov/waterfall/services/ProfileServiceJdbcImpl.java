package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.*;
import ru.itis.antonov.waterfall.repositories.ArticleRepository;
import ru.itis.antonov.waterfall.repositories.CommentRepository;
import ru.itis.antonov.waterfall.repositories.GroupRepository;
import ru.itis.antonov.waterfall.repositories.ProfileRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class ProfileServiceJdbcImpl implements ProfileService {

    private ProfileRepository profileRepository;

    private ArticleRepository articleRepository;

    private GroupRepository groupRepository;

    private CommentRepository commentRepository;

    public ProfileServiceJdbcImpl(ProfileRepository profileRepository,
                                  ArticleRepository articleRepository,
                                  GroupRepository groupRepository,
                                  CommentRepository commentRepository) {
        this.profileRepository = profileRepository;
        this.articleRepository = articleRepository;
        this.groupRepository = groupRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void createProfile(Profile p) {
        profileRepository.save(p);
    }

    @Override
    public void updateProfile(Profile p) {
        profileRepository.update(p);
    }

    @Override
    public void deleteProfile(Profile p) {
        profileRepository.delete(p);
    }

    @Override
    public Profile getProfileById(long id) {
        Optional<Profile> a = profileRepository.findById(id);
        return a.orElse(null);
    }

    @Override
    public List<Article> getProfileArticles(Profile p, FeedType type, int begin, int end, Date updated) {
        return articleRepository.getArticlesByAuthor(p, type, begin, end, updated);
    }

    @Override
    public List<Group> getProfileGroups(Profile p) {
        return groupRepository.profileGroups(p);
    }

    @Override
    public List<Profile> getProfileSubscribes(Profile p) {
        return profileRepository.getProfileSubscribes(p);
    }

    @Override
    public List<Article> getLikedArticles(Profile p) {
        return null;
    }

    @Override
    public List<Article> getDislikedArticles(Profile p) {
        return null;
    }

    @Override
    public List<Article> getSavedArticles(Profile p, int begin, int end, Date updated) {
        return articleRepository.savedArticles(p, begin, end, updated);
    }

    @Override
    public List<Comment> getSavedComments(Profile p, int begin, int end, Date updated) {
        return commentRepository.getSavedComments(p, begin, end, updated);
    }

    @Override
    public int getRate(Profile p) {
        return 0;
    }
}
