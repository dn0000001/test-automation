package com.taf.automation.ui.support.testng;

import org.testng.annotations.Listeners;

/**
 * The TestNG base class TDD
 */
@Listeners(AllureTestNGListener.class)
public class TestNGBase extends TestNGBaseWithoutListeners {
    // It is only necessary to add the listeners for TDD
}
