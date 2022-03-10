import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import ru.itis.antonov.waterfall.repositories.ArticleRepository;
import ru.itis.antonov.waterfall.repositories.CommentRepository;
import ru.itis.antonov.waterfall.repositories.GroupRepository;
import ru.itis.antonov.waterfall.repositories.ProfileRepository;
import ru.itis.antonov.waterfall.services.ProfileService;
import ru.itis.antonov.waterfall.services.ProfileServiceJdbcImpl;
import ru.itis.antonov.waterfall.models.Profile;

import java.util.Optional;

public class UserTest {

    private ProfileService profileService;

    private ProfileRepository profileRepository;
    private ArticleRepository articleRepository;
    private GroupRepository groupRepository;
    private CommentRepository commentRepository;

    @Before
    public void setUp(){
        profileRepository = Mockito.mock(ProfileRepository.class);
        Mockito.when(profileRepository.findById(3L)).thenReturn(Optional.of(new Profile(3L, "aaa", "bbb", "12345")));
        articleRepository = Mockito.mock(ArticleRepository.class);
        groupRepository = Mockito.mock(GroupRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        profileService = new ProfileServiceJdbcImpl(profileRepository, articleRepository, groupRepository, commentRepository);
    }

    @Test
    public void getUserByIdTest(){
        Profile profile = profileService.getProfileById(3);
        Assert.assertEquals(profile, new Profile(3L, "aaa", "bbb", "12345"));
    }
}
