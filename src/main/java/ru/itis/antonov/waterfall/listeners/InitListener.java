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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.jdbc.ScriptRunner;

@WebListener
public class InitListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger("init-logger");

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
        hikariConfig.setJdbcUrl(getProperty(properties.getProperty("db.url")));
        logger.log(Level.INFO, "Connecting to db " + getProperty(properties.getProperty("db.url")));
        hikariConfig.setDriverClassName(properties.getProperty("db.driver.classname"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.max-pool-size")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        try {
            ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());
            scriptRunner.runScript(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("db-init.sql"))));
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    private static String getProperty(String property) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
        Matcher matcher = pattern.matcher(property);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, System.getenv().get(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
