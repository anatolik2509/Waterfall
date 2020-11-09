package ru.itis.antonov.waterfall.listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itis.antonov.waterfall.repositories.*;
import ru.itis.antonov.waterfall.services.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        Properties properties = new Properties();
        try {
            properties.load(servletContext.getResourceAsStream("WEB-INF/properties/db.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setDriverClassName(properties.getProperty("db.driver.classname"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.max-pool-size")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        servletContext.setAttribute("dataSource", dataSource);

        ArticleRepository articleRepository = new ArticleRepositoryJdbcImpl(dataSource);
        CommentRepository commentRepository = new CommentRepositoryJdbcImpl(dataSource);
        GroupRepository groupRepository = new GroupRepositoryJdbcImpl(dataSource);
        ProfileRepository profileRepository = new ProfileRepositoryJdbcImpl(dataSource);

        ArticleService articleService = new ArticleServiceJdbcImpl(articleRepository,
                commentRepository,
                groupRepository,
                profileRepository);
        GroupService groupService = new GroupServiceJdbcImpl(groupRepository, articleRepository);
        ProfileService profileService = new ProfileServiceJdbcImpl(profileRepository,
                articleRepository,
                groupRepository,
                commentRepository);
        SecurityService securityService = new SecurityServiceJdbcImpl(profileRepository, new BCryptPasswordEncoder());
        MediaService mediaService = new MediaServiceFileImpl(Paths.get("C:/repository/"));


        servletContext.setAttribute("articleService", articleService);
        servletContext.setAttribute("groupService", groupService);
        servletContext.setAttribute("profileService", profileService);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("mediaService", mediaService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
