package com.taf.automation.ui.support;

import com.taf.automation.ui.support.testng.Attachment;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to document the application by taking screenshots when tests are run with the
 * proper flag.<BR>
 * <B>Note:  If running multiple tests in parallel, then this class should only be initialized once per thread</B>
 */
public class Documenter {
    /**
     * Flag to indicate whether documentation should occur, true to document else skip
     */
    private boolean documentationMode;

    /**
     * All the stored attachment events
     */
    private List<Attachment> attachmentEvents;

    /**
     * Constructor that sets documentation mode to false
     */
    public Documenter() {
        setDocumentationMode(false);
        attachmentEvents = new ArrayList<>();
    }

    /**
     * Set the documentation mode
     *
     * @param documentationMode - true to perform documentation actions, false to skip documentation actions
     */
    public void setDocumentationMode(boolean documentationMode) {
        this.documentationMode = documentationMode;
    }

    /**
     * Record the current page if necessary<BR>
     * <B>Notes:</B><BR>
     * 1) Currently documentation only consists of a screenshot
     * but this method could be enhanced to include other methods of documentation<BR>
     * 2) Uses current time as title for documentation<BR>
     * 3) Immediately documentation is associated to a step<BR>
     */
    public void record() {
        record(String.valueOf(System.currentTimeMillis()));
    }

    /**
     * Record the current page if necessary<BR>
     * <B>Notes:</B><BR>
     * 1) Currently documentation only consists of a screenshot
     * but this method could be enhanced to include other methods of documentation<BR>
     * 2) Immediately documentation is associated to a step<BR>
     *
     * @param title - Title for documentation
     */
    public void record(String title) {
        if (documentationMode) {
            TestNGBase.takeScreenshot(title);
        }
    }

    /**
     * Store the current page if necessary<BR>
     * <B>Notes:</B><BR>
     * 1) Currently documentation only consists of a screenshot
     * but this method could be enhanced to include other methods of documentation<BR>
     * 2) Uses current time as title for documentation<BR>
     * 3) The documentation is stored for later to be flushed/associated to a step<BR>
     */
    public void store() {
        store(String.valueOf(System.currentTimeMillis()));
    }

    /**
     * Store the current page if necessary<BR>
     * <B>Notes:</B><BR>
     * 1) Currently documentation only consists of a screenshot
     * but this method could be enhanced to include other methods of documentation<BR>
     * 2) The documentation is stored for later to be flushed/associated to a step<BR>
     *
     * @param title - Title for documentation
     */
    public void store(String title) {
        if (documentationMode && TestNGBase.context() != null && TestNGBase.context().getDriver() != null) {
            String alertText = Utils.dismissAlertIfPresent(TestNGBase.context().getDriver());
            if (alertText != null) {
                Attachment alertAttachment = new Attachment()
                        .withTitle("Alert on " + title)
                        .withType("text/plain")
                        .withFile(alertText.getBytes());
                attachmentEvents.add(alertAttachment);
            }

            byte[] attachment = ((TakesScreenshot) TestNGBase.context().getDriver()).getScreenshotAs(OutputType.BYTES);
            attachmentEvents.add(new Attachment().withTitle(title).withType("image/png").withFile(attachment));
        }
    }

    /**
     * Flush all the stored attachment events<BR>
     * <B>Notes:</B><BR>
     * 1) This method should be used to flush all attachment events at a specific step<BR>
     * 2) If no associated step, then all attachment events will be at the root level<BR>
     */
    public void flush() {
        for (Iterator<Attachment> iterator = attachmentEvents.iterator(); iterator.hasNext(); ) {
            iterator.next().build();
            iterator.remove();
        }
    }

}
