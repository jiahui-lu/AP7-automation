package com.prometheanworld.AP9.Firmware.AndroidSettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NetworkInternetTest extends BaseTest {

    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";

    /**
     * C116432 Verify that MAC Address is visible under About device information
     * Steps:
     *  1. Click on Applications icon => Application Locker should open
     *  2. Go to Settings>System> About Device> Check WIFI MAC Address or Ethernet is displayed => WIFI MAC Address should be visible to user.
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C116432VerifyThatMACAddressIsVisibleUnderAboutDeviceInformation() {
        TestRail.setTestRailId("116432");

        try {
            final String module = "Wi\u2011Fi MAC address";
            TestRail.addStepName("Click on Applications icon");
            AppiumHelper.openAppsFromTaskBar("Applications");
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("id/locker_container")),
                    "The locker should be opened");
            TestRail.addStepName("Go to Settings>System> About Device> Check WIFI MAC Address or Ethernet is displayed");
            systemPO.scrollAndClickApp("Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "System");
            CommonOperator.scrollAndClick("android:id/list", "About ActivPanel");
            Assert.assertTrue(CommonOperator.scrollAndFind("id/list", module),
                    "The `" + module + "` module should be visible");
            String path = "//*[@text='" + module + "']/../android.widget.TextView[contains(@resource-id, 'id/summary')]";
            Assert.assertTrue(ElementHelper.isVisible(By.xpath(path)),
                    "The summary should be visible");
            String summary = ElementHelper.findElement(By.xpath(path)).getText();
            Assert.assertTrue(summary != null && !summary.equals(""),
                    "The summary should be not empty");
        } finally {
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        }
    }
}
