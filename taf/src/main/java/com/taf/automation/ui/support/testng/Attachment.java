package com.taf.automation.ui.support.testng;

import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;

/**
 * This class holds information to add an attachment to the Allure report
 */
public class Attachment {
    private String title;
    private String type;
    private byte[] file;

    /**
     * @return the title of attachment. Shown at report as name of attachment
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title of attachment. Shown at report as name of attachment
     *
     * @param title - title
     * @return Attachment
     */
    public Attachment withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @return MIME-type of attachment
     */
    public String getType() {
        return type;
    }

    /**
     * Set MIME-type of attachment
     *
     * @param type - MIME-type
     * @return Attachment
     */
    public Attachment withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return file as byte array
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * Set file attachment
     *
     * @param file - file as byte array
     * @return Attachment
     */
    public Attachment withFile(byte[] file) {
        this.file = file;
        return this;
    }

    /**
     * Use the data of the class to add an attachment to the Allure report
     */
    public void build() {
        MakeAttachmentEvent ev = new MakeAttachmentEvent(getFile(), getTitle(), getType());
        Allure.LIFECYCLE.fire(ev);
    }

}
