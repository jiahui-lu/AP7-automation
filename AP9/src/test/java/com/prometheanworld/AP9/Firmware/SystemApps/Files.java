package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Files extends BaseTest {
    /**
     * C120724 Verify that Files app header 'Download' changed to 'Files'
     * Steps:
     *  1. Open locker
     *  2. Click on Files app => Enter the Files app
     *  3. Look at the header on the left page => The header name next to the download icon is displayed as Files
     *  4. Click on Files => Enter the files page, the directory name in the upper left corner is displayed as Files
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C120724VerifyThatFilesAppHeaderDownloadChangedToFiles() {
        TestRail.setTestRailId("120724");

        try {
            TestRail.addStepName("Open locker");
            TestRail.addStepName("Click on Files app");
            systemPO.startAppFromUnifiedLauncher("Files");
            TestRail.addStepName("Look at the header on the left page");
            AppiumHelper.waitForSeconds(2);
            ElementHelper.click(By.xpath("//android.widget.ImageButton[@content-desc=\"Show roots\"]"));
            String filesPath = "//android.widget.TextView[@text='Files'][@resource-id='android:id/title']";
            Assert.assertTrue(ElementHelper.isVisible(By.xpath(filesPath)), "The header name next to the download icon should be displayed as Files");
            TestRail.addStepName("Click on Files");
            ElementHelper.click(By.xpath(filesPath));
            String title = ElementHelper.findElement(By.xpath("//android.view.ViewGroup[@resource-id='com.android.documentsui:id/toolbar']/android.widget.TextView")).getText();
            Assert.assertEquals("Files", title, "the directory name in the upper left corner should be displayed as Files");
        } catch (Throwable e) {
            AppiumHelper.captureScreenshot("C120724-failure-" + System.currentTimeMillis());
            throw e;
        } finally {
            Adb.forceStop("com.android.documentsui");
        }
    }
}