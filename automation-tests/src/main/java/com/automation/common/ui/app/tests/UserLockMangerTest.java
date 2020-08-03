package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.JexlExpressionDO;
import com.taf.automation.locking.UserLockManager;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Date;

public class UserLockMangerTest extends TestNGBase {
    @Features("Framework")
    @Stories("Validate the UserLockManager is functioning properly")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"delay", "data-set"})
    @Test
    public void testLockingUser(
            @Optional("1000") String delay,
            @Optional("data/ui/locking/UserLockManger_User1_TestData.xml") String dataSet
    ) {
        new JexlExpressionDO(getContext()).fromResource(dataSet);
        Helper.log("" + new Date() + " User (" + UserLockManager.getInstance().getUser() + ") sleeping for " + delay, true);
        Utils.sleep(NumberUtils.toInt(delay, 1000));
    }

    @AfterTest(alwaysRun = true)
    private void unlockUser() {
        UserLockManager.getInstance().unlock();
    }

}
