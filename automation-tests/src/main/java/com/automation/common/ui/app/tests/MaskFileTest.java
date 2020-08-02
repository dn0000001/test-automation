package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Masking file test
 */
public class MaskFileTest extends TestNGBase {
    @Features("File Masking Utility")
    @Stories("Mask and Unmask a file")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"file", "extension"})
    @Test
    public void performMaskUnMaskTest(@Optional("data/ui/testingExcel.xls") String file,
                                      @Optional(".xls") String extension
    ) {
        String maskedFile = Helper.maskFile(file);
        assertThat("Failed to create masked file", maskedFile, notNullValue());
        Helper.log("Masked file:  " + maskedFile, true);

        String unmaskedFile = Helper.unmaskFile(maskedFile, extension);
        assertThat("Failed to unmasked file", unmaskedFile, notNullValue());
        Helper.log("Unmasked file:  " + unmaskedFile, true);
    }

}
