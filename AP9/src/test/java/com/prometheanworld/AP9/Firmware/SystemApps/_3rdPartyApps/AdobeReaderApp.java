package com.prometheanworld.AP9.Firmware.SystemApps._3rdPartyApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class AdobeReaderApp extends BaseTest {
    /**
     * C92485 Verify that Adobe reader app is installed as part of build
     * Steps:
     *  1. Open locker => Adobe app should be visible and available
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P0")
    public void C92485VerifyThatAdobeReaderAppIsInstalledAsPartOfBuild() {
        TestRail.setTestRailId("92485");

        try {
            TestRail.addStepName("Open locker");
            final String appName = "Adobe Acrobat";
            systemPO.startLocker();
            AndroidElement adobeEle = null;
            List<MobileElement> tabs = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab), 5);
            for (MobileElement tabEle : tabs) {
                tabEle.click();
                AndroidElement eleApp = AppiumHelper.findElement("//android.widget.TextView[@text='" + appName + "']", 2);
                if (eleApp != null && eleApp.isDisplayed()) {
                    adobeEle = eleApp;
                    break;
                }
            }
            Assert.assertNotNull(adobeEle, "Adobe app should be visible");
            adobeEle.click();
            AppiumHelper.waitForSeconds(1);
            List<String> list = Adb.adb("shell dumpsys activity | grep mResume");
            Assert.assertTrue(!list.isEmpty() && list.get(0).contains("com.adobe.reader"), "Adobe app should be available");
        } finally {
            systemPO.hideMenuBar();
            Adb.forceStop("com.prometheanworld.locker");
            Adb.forceStop("com.adobe.reader");
        }
    }
}
