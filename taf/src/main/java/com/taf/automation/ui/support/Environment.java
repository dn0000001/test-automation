package com.taf.automation.ui.support;

/**
 * This class holds all the supported environments
 */
public enum Environment {
    QA("QA", false), //
    DEV("DEV", false), //
    PROD("PROD", true); //

    private String name;
    private boolean production;

    /**
     * Constructor
     *
     * @param name       - Name
     * @param production - true if production environment
     */
    private Environment(String name, boolean production) {
        this.name = name;
        this.production = production;
    }

    public String toString() {
        return name;
    }

    public boolean isProdEnv() {
        return production;
    }

}
