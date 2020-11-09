package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.*;

import java.util.List;
import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile>{
    List<Profile> getProfileSubscribes(Profile p);

    Profile getProfileByUUID(UUID uuid);

    void deleteAllUUIDS(Profile p);

    void addUUID(Profile p, UUID uuid);

    void removeUUID(UUID uuid);

    void refreshUUIDs();

    void subscribe(Profile subscriber, Profile subscribe);

    void unsubscribe(Profile subscriber, Profile subscribe);

    boolean isSubscribed(Profile subscriber, Profile subscribe);

    Profile getByEmail(String email);

    Profile getWithName(long id);
}
