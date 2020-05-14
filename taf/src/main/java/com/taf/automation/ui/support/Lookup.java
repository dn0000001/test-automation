package com.taf.automation.ui.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores variables that can be retrieved later (from JEXL expressions)
 */
public class Lookup {
    private static final ThreadLocal<Map<String, Object>> storedVariables = ThreadLocal.withInitial(HashMap::new);

    private Lookup() {
        //
    }

    private static class LazyHolder {
        private static final Lookup INSTANCE = new Lookup();
    }

    public static Lookup getInstance() {
        return Lookup.LazyHolder.INSTANCE;
    }

    private static Map<String, Object> getStoredVariables() {
        return storedVariables.get();
    }

    /**
     * Unloads the thread local variable to prevent sonar violation
     * "ThreadLocal" variables should be cleaned up when no longer used.<BR>
     * <B>Note: </B> This should not really be needed to be called but you would probably call it after a test.
     *
     * @return Lookup
     */
    public Lookup unload() {
        storedVariables.remove();
        return this;
    }

    public Lookup put(String key, Object value) {
        getStoredVariables().put(key, value);
        return this;
    }

    public Object get(String key) {
        return getStoredVariables().get(key);
    }

    public Object getOrDefault(String key, Object defaultValue) {
        return getStoredVariables().getOrDefault(key, defaultValue);
    }

    public Lookup append(String key, String value) {
        getStoredVariables().put(key, "" + getOrDefault(key, "") + value);
        return this;
    }

    public Lookup remove(String key) {
        getStoredVariables().remove(key);
        return this;
    }

}
