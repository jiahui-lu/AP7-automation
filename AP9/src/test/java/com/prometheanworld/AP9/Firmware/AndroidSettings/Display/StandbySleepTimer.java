package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.SubSystem;
import com.prometheanworld.appium.frame.model.AP9.StandbySleepTimerPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class StandbySleepTimer extends BaseTest {

    private final StandbySleepTimerPO standbySleepTimerPO = POFactory.getInstance(StandbySleepTimerPO.class);

    private static final String POWER_STATUS_STANDBY = "1";
    private static final String POWER_STATUS_ON = "2";

    @Override
    protected String testAppName() {
        return "Settings";
    }

    /**
     * C88901 Verify that user can interrupt standby timer and confirm that panel will enter standby again
     * Steps:
     *  1. Go to Settings page, Display -> Standby and Sleep Timers
     *  2. Set standby timer to another value, such as 5 mins
     *   => The selected timer on the right is displayed for "5 mins"
     *  3. Wait for 5 mins without interacting with panel
     *   => Confirm that panel's screen is turned off
     *  4. Wait for some time, interact with the panel
     *   => Panel's screen will turned on again
     *  5. Wait for another 5 mins without interacting with panel
     *   => Confirm that panel's screen is turned off
     * Preconditions: 9950: Need to turn off Settings -> Display -> Proximity sensor
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C88901VerifyThatUserCanInterruptStandbyTimerAndConfirmThatPanelWillEnterStandbyAgain() {
        TestRail.setTestRailId("88901");
        boolean isMT9950 = SubSystem.isMT9950();
        boolean turnOffProximitySensor = false;
        StandbySleepTimerPO.SleepStandByTime originalStandByTime = null;
        boolean appiumServicesKilled = false;
        try {
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            if (isMT9950) {
                AndroidElement proximitySensorSwitcher = standbySleepTimerPO.findProximitySensorSwitcher();
                if ("ON".equals(proximitySensorSwitcher.getAttribute("text"))) {
                    TestRail.addStepName("9950: Need to turn off Settings -> Display -> Proximity sensor");
                    turnOffProximitySensor = true;
                    proximitySensorSwitcher.click();
                    Assert.assertTrue("OFF".equals(proximitySensorSwitcher.getAttribute("text")),
                            "Expect the switcher of proximity sensor has been turn OFF, but is ON.");
                }
            }
            TestRail.addStepName("Go to Settings page, Display -> Standby and Sleep Timers");
            CommonOperator.scrollAndClick("com.android.settings:id/list","Standby and sleep timeouts");

            TestRail.addStepName("Set standby timer to another value, such as 5 mins");
            originalStandByTime = standbySleepTimerPO.getCurrentStandByTime();
            StandbySleepTimerPO.SleepStandByTime expectStandbyTime = StandbySleepTimerPO.SleepStandByTime._5mins;
            standbySleepTimerPO.scrollStandByTimeSeekbar(expectStandbyTime);

            TestRail.addStepName("Verify the selected timer on the right is displayed for \"5 mins\"");
            By standByTimeLabel = Locator.byResourceId("com.android.settings:id/standby_label");
            ElementHelper.waitUntilVisible(standByTimeLabel, 5);
            Assert.assertTrue("5 mins".equals(ElementHelper.findElement(standByTimeLabel).getAttribute("text")),
                    "Expect the standby time has been set to 5min, but is NOT.");
            standbySleepTimerPO.navigateUp();

            final int offsetTime = 10;
            TestRail.addStepName("Wait for 5 mins without interacting with panel");
            // The Appium service will prevent the panel to the standby mode, so we have to stop the Appium service in the panel.
            standbySleepTimerPO.killAppiumServer();
            appiumServicesKilled = true;
            AppiumHelper.waitForSeconds(expectStandbyTime.getDurationSecond() + offsetTime);
            TestRail.addStepName("Confirm that panel's screen is turned off");
            AppiumHelper.exeAdbRoot();
            String powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_STANDBY,
                    "Expect the panel is in the standby mode, but is NOT.");

            TestRail.addStepName("Wait for some time, interact with the panel");
            standbySleepTimerPO.wakeUpPanelByRemote(PrometheanKey.Menu, 3);
            TestRail.addStepName("Panel's screen will turned on again");
            powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_ON,
                    "Expect the panel is in the power on, but is NOT.");

            TestRail.addStepName("Wait for another 5 mins without interacting with panel");
            standbySleepTimerPO.killAppiumServer();
            appiumServicesKilled = true;
            AppiumHelper.waitForSeconds(expectStandbyTime.getDurationSecond() + offsetTime);
            AppiumHelper.exeAdbRoot();
            standbySleepTimerPO.getPanelPowerStatus();
            TestRail.addStepName("Confirm that panel's screen is turned off");
            powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_STANDBY,
                    "Expect the panel is in the standby mode, but is NOT.");

            standbySleepTimerPO.wakeUpPanelByRemote(PrometheanKey.Menu, 3);
            powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_ON,
                    "Expect the panel is in the power on, but is NOT.");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("C88901");
            throw e;
        } finally {
            if (appiumServicesKilled) {
                // Restore the appium service.
                standbySleepTimerPO.restartAppiumServer();
            }
            if (originalStandByTime != null) {
                // Restore the standby time to the value before the automation start.
                boolean found = CommonOperator.scrollAndFind("com.android.settings:id/list","Standby and sleep timeouts");
                if (found) {
                    CommonOperator.scrollAndClick("com.android.settings:id/list","Standby and sleep timeouts");
                    standbySleepTimerPO.scrollStandByTimeSeekbar(originalStandByTime);
                    standbySleepTimerPO.navigateUp();
                }
            }
            if (turnOffProximitySensor) {
                // Restore the proximity sensor status to the value before the automation start.
                AndroidElement proximitySensorSwitcher = standbySleepTimerPO.findProximitySensorSwitcher();
                if (proximitySensorSwitcher != null && "OFF".equals(proximitySensorSwitcher.getAttribute("text"))) {
                    proximitySensorSwitcher.click();
                }
            }
        }
    }

    /**
     * C88908 Verify that user can interrupt sleep timer and wake the panel up before panel goes into sleep
     * Steps:
     *  1. Go to Settings page, Display -> Standby and Sleep Timers
     *  2. Set standby time to 3 mins
     *   2.1 Set sleep time to 10 mins
     *   2.2 Wait for panel to enter standby mode
     *   => Panel should get into stand by after 3mins and screen should be off/dimmed
     *  3. Wait for 5 mins without interacting with panel
     *   => Confirm that panel is still in standby mode
     *  4. Press the menu button on panel or remote to wake the panel
     *   => Panel screen should be up and it should not enter the sleep state
     * Preconditions: 9950: Need to turn off Settings -> Display -> Proximity sensor
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C88908VerifyThatUserCanInterruptSleepTimerAndWakeThePanelUpBeforePanelGoesIntoSleep() {
        TestRail.setTestRailId("88908");
        boolean isMT9950 = SubSystem.isMT9950();
        boolean turnOffProximitySensor = false;
        StandbySleepTimerPO.SleepStandByTime originalStandByTime = null;
        StandbySleepTimerPO.SleepStandByTime originalSleepTime = null;
        boolean appiumServicesKilled = false;
        try {
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            if (isMT9950) {
                AndroidElement proximitySensorSwitcher = standbySleepTimerPO.findProximitySensorSwitcher();
                if ("ON".equals(proximitySensorSwitcher.getAttribute("text"))) {
                    TestRail.addStepName("9950: Need to turn off Settings -> Display -> Proximity sensor");
                    turnOffProximitySensor = true;
                    proximitySensorSwitcher.click();
                    Assert.assertTrue("OFF".equals(proximitySensorSwitcher.getAttribute("text")),
                            "Expect the switcher of proximity sensor has been turn OFF, but is ON.");
                }
            }
            TestRail.addStepName("Go to Settings page, Display -> Standby and Sleep Timers");
            CommonOperator.scrollAndClick("com.android.settings:id/list","Standby and sleep timeouts");

            originalStandByTime = standbySleepTimerPO.getCurrentStandByTime();
            originalSleepTime = standbySleepTimerPO.getCurrentSleepTime();

            TestRail.addStepName("Set standby time to 3 mins");
            StandbySleepTimerPO.SleepStandByTime expectStandbyTime = StandbySleepTimerPO.SleepStandByTime._3mins;
            standbySleepTimerPO.scrollStandByTimeSeekbar(expectStandbyTime);
            TestRail.addStepName("Verify the selected timer on the right is displayed for \"3 mins\"");
            By standByTimeLabel = Locator.byResourceId("com.android.settings:id/standby_label");
            ElementHelper.waitUntilVisible(standByTimeLabel, 5);
            Assert.assertTrue("3 mins".equals(ElementHelper.findElement(standByTimeLabel).getAttribute("text")),
                    "Expect the standby time has been set to 3min, but is NOT.");

            TestRail.addStepName("Set sleep time to 10 mins");
            StandbySleepTimerPO.SleepStandByTime expectSleepTime = StandbySleepTimerPO.SleepStandByTime._10mins;
            standbySleepTimerPO.scrollSleepTimeSeekbar(expectSleepTime);
            By sleepTimeLabel = Locator.byResourceId("com.android.settings:id/sleep_label");
            ElementHelper.waitUntilVisible(sleepTimeLabel, 5);
            Assert.assertTrue("10 mins".equals(ElementHelper.findElement(sleepTimeLabel).getAttribute("text")),
                    "Expect the sleep time has been set to 10min, but is NOT.");

            standbySleepTimerPO.navigateUp();

            TestRail.addStepName("Wait for panel to enter standby mode");
            final int offsetTime = 10;
            // The Appium service will prevent the panel to the standby mode, so we have to stop the Appium service in the panel.
            standbySleepTimerPO.killAppiumServer();
            appiumServicesKilled = true;
            AppiumHelper.waitForSeconds(expectStandbyTime.getDurationSecond() + offsetTime);
            TestRail.addStepName("Panel should get into stand by after 3mins and screen should be off/dimmed");
            AppiumHelper.exeAdbRoot();
            String powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_STANDBY,
                    "Expect the panel is in the standby mode, but is NOT.");

            TestRail.addStepName("Wait for 5 mins without interacting with panel");
            AppiumHelper.waitForSeconds(StandbySleepTimerPO.SleepStandByTime._5mins.getDurationSecond() + offsetTime);
            TestRail.addStepName("Confirm that panel is still in standby mode");
            powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_STANDBY,
                    "Expect the panel is in the standby mode, but is NOT.");

            TestRail.addStepName("Press the menu button on panel or remote to wake the panel");
            standbySleepTimerPO.wakeUpPanelByRemote(PrometheanKey.Menu, 3);
            TestRail.addStepName("Panel screen should be up and it should not enter the sleep state");
            powerStatus = standbySleepTimerPO.getPanelPowerStatus();
            Assert.assertEquals(powerStatus, POWER_STATUS_ON,
                    "Expect the panel is in the power on, but is NOT.");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("C88908");
            throw e;
        } finally {
            if (appiumServicesKilled) {
                // Restore the appium service.
                standbySleepTimerPO.restartAppiumServer();
            }
            if (originalStandByTime != null || originalSleepTime != null) {
                // Restore the standby time or sleep time to the value before the automation start.
                boolean found = CommonOperator.scrollAndFind("com.android.settings:id/list","Standby and sleep timeouts");
                if (found) {
                    CommonOperator.scrollAndClick("com.android.settings:id/list","Standby and sleep timeouts");
                    if (originalStandByTime != null) {
                        standbySleepTimerPO.scrollStandByTimeSeekbar(originalStandByTime);
                    }
                    if (originalSleepTime != null) {
                        standbySleepTimerPO.scrollSleepTimeSeekbar(originalSleepTime);
                    }
                    standbySleepTimerPO.navigateUp();
                }
            }
            if (turnOffProximitySensor) {
                // Restore the proximity sensor status to the value before the automation start.
                AndroidElement proximitySensorSwitcher = standbySleepTimerPO.findProximitySensorSwitcher();
                if (proximitySensorSwitcher != null && "OFF".equals(proximitySensorSwitcher.getAttribute("text"))) {
                    proximitySensorSwitcher.click();
                }
            }
        }
    }

    /**
     * C88896 Verify that standby and sleep timer setting appears for IT admins
     * Steps:
     * 1.Android Settings --> Display --> Standby and sleep timeouts
     * 2.check there are two sliders for standby and sleep timer
     * 3.check the default standby timer is 1 hour and sleep timer is 3 minutes
     * 4.check standby and sleep timers settings should be available to user
     * Author: guangxi.chen
     */
    @Test(groups = "P1")
    public void C88896VerifyThatStandbyAndSleepTimerSettingAppearsForITAdmins(){
        TestRail.setTestRailId("88896");
        TestRail.addStepName("Android Settings --> Display --> Standby and sleep timeouts");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        TestRail.addStepName("check there are two sliders for standby and sleep timer");
        List<MobileElement> seekbars = ElementHelper.findElements(By.xpath("//*[@class='android.widget.SeekBar']"), 5);
        Assert.assertEquals(seekbars.size(),2);
        TestRail.addStepName("check the default standby timer is 1 hour and sleep timer is 3 minutes");
        Assert.assertEquals(ElementHelper.findElement(Locator.byResourceId("com.android.settings:id/standby_label")).getText(),"1 hr");
        Assert.assertEquals(ElementHelper.findElement(Locator.byResourceId("com.android.settings:id/sleep_label")).getText(),"3 mins");
        TestRail.addStepName("check standby and sleep timers settings should be available to user");
        Assert.assertTrue(seekbars.get(0).isEnabled());
        Assert.assertTrue(seekbars.get(1).isEnabled());
    }
}
