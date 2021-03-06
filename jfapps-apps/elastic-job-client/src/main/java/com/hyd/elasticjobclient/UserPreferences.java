package com.hyd.elasticjobclient;

import java.util.*;
import java.util.prefs.Preferences;
import org.apache.commons.lang3.StringUtils;

public class UserPreferences {

    public static final String SPLIT = ";";

    private static Preferences getPreferences() {
        return Preferences.userRoot().node("com.hyd.elastic-job-client");
    }

    public static void save(ConfigKey key, String value) {
        Preferences preferences = getPreferences();
        preferences.put(key.name(), value);
    }

    public static void append(ConfigKey key, String value) {
        Preferences preferences = getPreferences();
        String[] split = preferences.get(key.name(), "").split(SPLIT);
        if (split.length == 0) {
            preferences.put(key.name(), value);
        } else {
            List<String> values = new ArrayList<>(new HashSet<>(Arrays.asList(split)));
            if (!values.contains(value)) {
                values.add(value);
            }
            values.removeIf(StringUtils::isBlank);
            preferences.put(key.name(), String.join(SPLIT, values));
        }
    }

    public static List<String> get(ConfigKey key) {
        Preferences preferences = getPreferences();
        String[] split = preferences.get(key.name(), "").split(SPLIT);
        if (split.length == 0) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(split);
        }
    }
}
