package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Helper;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Masking file test
 */
@SuppressWarnings("java:S3252")
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
        AssertJUtil.assertThat(maskedFile).as("Failed to create masked file").isNotNull();
        Helper.log("Masked file:  " + maskedFile, true);

        String unmaskedFile = Helper.unmaskFile(maskedFile, extension);
        AssertJUtil.assertThat(unmaskedFile).as("Failed to unmasked file").isNotNull();
        Helper.log("Unmasked file:  " + unmaskedFile, true);
    }

}
