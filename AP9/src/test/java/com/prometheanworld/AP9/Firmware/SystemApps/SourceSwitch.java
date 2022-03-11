package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SourceSwitch extends BaseTest {
    /**
     * C92067 The Source Switch window will automatically close after 30 seconds of no activity
     * Steps:
     *  1. Press the Source hardware button on front of Panel =>
     *     Source Switch menu displays, shows image and name list of available sources and displays the source is the one currently in use
     *     High Tier: displays the live preview images of devices in source list (not in 1.0.0)
     *     Low Tier: displays the icons of devices in source list (not in 1.0.0)
     *  2. Wait for 10 seconds of no activity => The Source Switch UI will not automatically close
     *  3. Touch Source Switch UI, wait for 30 seconds of no activity => The Source Switch UI will automatically close
     *  4. Press the Source button on remote => Source Switch menu displays, shows image and name list of available sources and displays the source is the one currently in use
     *  5. Wait for 30 seconds of no activity => The Source Switch UI will automatically close
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C92067TheSourceSwitchWindowWillAutomaticallyCloseAfter30SecondsOfNoActivity() {
        TestRail.setTestRailId("92067");

        By sourcesPath = Locator.byResourceId("com.prometheanworld.sources:id/source_container_view");
        try {
            TestRail.addStepName("Press the Source hardware button on front of Panel ");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.ActivPanel);
            Assert.assertTrue(ElementHelper.isVisible(sourcesPath),
                    "Sources should be visible");
            TestRail.addStepName("Wait for 10 seconds of no activity");
            AppiumHelper.waitForSeconds(10);
            Assert.assertTrue(ElementHelper.isVisible(sourcesPath),
                    "Sources should be visible");
            TestRail.addStepName("Touch Source Switch UI, wait for 30 seconds of no activity");
            AppiumHelper.waitForSeconds(20);
            Assert.assertFalse(ElementHelper.isVisible(sourcesPath),
                    "Sources should not be visible");
            TestRail.addStepName("Press the Source button on remote");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            Assert.assertTrue(ElementHelper.isVisible(sourcesPath),
                    "Sources should be visible");
            TestRail.addStepName("Wait for 30 seconds of no activity");
            AppiumHelper.waitForSeconds(30);
            Assert.assertFalse(ElementHelper.isVisible(sourcesPath),
                    "Sources should not be visible");
        } catch (Throwable e) {
            AppiumHelper.captureScreenshot("C92067-failure-" + System.currentTimeMillis());
            throw e;
        } finally {
            if (ElementHelper.isVisible(sourcesPath)) {
                AppiumHelper.clickAt(0.0, 0.0);
            }
        }
    }
}