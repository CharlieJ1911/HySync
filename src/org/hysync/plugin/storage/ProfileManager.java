package org.hysync.plugin.storage;

import org.hysync.plugin.HySync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {
    private HySync hysync;
    private static Map<UUID, HyProfile> profiles;

    public ProfileManager(HySync hysync){
        this.hysync = hysync;
        profiles = new HashMap<>();
    }

    public static Map<UUID, HyProfile> getProfiles(){
        return profiles;
    }
}
