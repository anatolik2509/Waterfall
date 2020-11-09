package ru.itis.antonov.waterfall.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.antonov.waterfall.exceptions.*;
import ru.itis.antonov.waterfall.models.Profile;
import ru.itis.antonov.waterfall.repositories.ProfileRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;


public class SecurityServiceJdbcImpl implements SecurityService {

    private ProfileRepository profileRepository;

    private PasswordEncoder passwordEncoder;

    private Logger logger;

    public SecurityServiceJdbcImpl(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        logger = LoggerFactory.getLogger("Security Service");
    }

    @Override
    public UUID registration(Profile p, String rawPassword, HttpSession session) {
        validateProfile(p);
        if(rawPassword.length() < 6) throw new WeakPasswordException("Password too short");
        String hash = passwordEncoder.encode(rawPassword);
        p.setPasswordHash(hash);
        UUID uuid = UUID.randomUUID();
        profileRepository.save(p);
        profileRepository.addUUID(p, uuid);
        session.setAttribute("user", p);
        return uuid;
    }

    @Override
    public Profile getProfileByUUID(UUID uuid) {
        return profileRepository.getProfileByUUID(uuid);
    }

    @Override
    public UUID authorize(String login, String rawPassword, HttpSession session) {
        Profile p = profileRepository.getByEmail(login);
        if(p == null) throw new NoSuchLoginException("No email " + login);
        if(passwordEncoder.matches(rawPassword, p.getPasswordHash())){
            UUID uuid = UUID.randomUUID();
            profileRepository.addUUID(p, uuid);
            session.setAttribute("user", p);
            return uuid;
        }
        else throw new WrongPasswordException();
    }

    @Override
    public boolean isAuthenticated(HttpServletRequest request, HttpSession session) {
        if(session.getAttribute("user") != null){
            return true;
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie c : cookies){
                if(c.getName().equals(AUTH_COOKIE_NAME)){
                    Profile p = profileRepository.getProfileByUUID(UUID.fromString(c.getValue()));
                    if(p != null){
                        session.setAttribute("user", p);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void validateProfile(Profile p) {
        Matcher m = Pattern.compile(EMAIL_REGEXP).matcher(p.getEmail());
        if(!m.matches()){
            throw new InvalidEmailException(p.getEmail() + " is not email");
        }
        if(profileRepository.getByEmail(p.getEmail()) != null){
            throw new OccupiedLoginException(p.getEmail() + " is occupied");
        }
    }

    @Override
    public void logout(UUID uuid) {
        profileRepository.removeUUID(uuid);
    }

    @Override
    public void logout(HttpServletRequest req, HttpSession session) {
        Cookie[] cookies = req.getCookies();
        for(Cookie c : cookies){
            if(c.getName().equals(SecurityService.AUTH_COOKIE_NAME)){
                logout(UUID.fromString(c.getValue()));
            }
        }
        session.removeAttribute("user");
    }

    @Override
    public void fullLogout(Profile p) {
        profileRepository.deleteAllUUIDS(p);
    }
}
