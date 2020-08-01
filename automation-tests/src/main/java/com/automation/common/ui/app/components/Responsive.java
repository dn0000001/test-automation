package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Enumeration to help working with responsive elements
 */
public enum Responsive {
    SignIn(450, true),
    LandingPage(480, false);

    private boolean hides;
    private long width;
    private boolean displayed;

    /**
     * Constructor that initializes the <B>isDisplayed</B> variable
     *
     * @param width - the width used to determine if the element is displayed
     * @param hides - <B>true</B> for the element will be displayed when the (current) page width is greater than or
     *              equal to the width otherwise it is hidden<BR>
     *              <B>false</B> for the element will be displayed when the (current) page width is less than the width
     *              otherwise it is hidden<BR>
     */
    Responsive(long width, boolean hides) {
        this.width = width;
        this.hides = hides;
        isCurrentlyDisplayed();
    }

    /**
     * @return the stored displayed value indicating whether the element is supposed to be displayed
     */
    public boolean isDisplayed() {
        return displayed;
    }

    /**
     * @return the width at which the element should be displayed, for an element that hides it is the min width that
     * it should be displayed else it is max width it should still be displayed
     */
    public long getWidthToDisplay() {
        long offset = (hides) ? 0 : 1;
        return width - offset;
    }

    /**
     * @return Check if element is currently supposed to be displayed and update <B>isDisplayed</B> variable
     */
    public boolean isCurrentlyDisplayed() {
        if (hides) {
            displayed = Utils.getPageWidth() >= width;
        } else {
            displayed = Utils.getPageWidth() < width;
        }

        return displayed;
    }

    public Dimension adjustWidthForResponsive(WebDriver driver) {
        return adjustWidthForResponsive(driver, this);
    }

    /**
     * Adjust the width of the window for the responsive element to be displayed
     *
     * @param driver     - WebDriver
     * @param responsive - Responsive element enumeration
     * @return the initial dimensions of the window before re-sizing
     */
    @Step("Adjust width of window for responsive element to be displayed")
    public static Dimension adjustWidthForResponsive(WebDriver driver, Responsive responsive) {
        Dimension initialDimensions = driver.manage().window().getSize();
        driver.manage().window().setSize(new Dimension((int) responsive.getWidthToDisplay(), initialDimensions.getHeight()));
        return initialDimensions;
    }

    /**
     * Reset the window dimensions
     *
     * @param driver            - WebDriver
     * @param initialDimensions - Initial Dimensions to reset to
     */
    @Step("Reset dimensions of window")
    public static void resetDimensions(WebDriver driver, Dimension initialDimensions) {
        driver.manage().window().setSize(initialDimensions);
    }

}
