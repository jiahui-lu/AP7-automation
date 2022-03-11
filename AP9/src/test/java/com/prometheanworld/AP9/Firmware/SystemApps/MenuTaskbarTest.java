package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.SettingConfigurationPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class MenuTaskbarTest extends BaseTest {
    private SettingConfigurationPO configurationPO = POFactory.getInstance(SettingConfigurationPO.class);
    protected String eleUsbStorage = "//*[@resource-id='com.android.documentsui:id/eject_icon']/../..";
    private String maximizeXpath = "//android.widget.ImageButton[@content-desc='Maximize']";
    private String closeXpath = "//android.widget.ImageButton[@content-desc='Close']";
    private String appXpath = "//*[@text='Applications']";
    private String chromiumXpath = "//*[@text='Chromium']";
    private String annotateXpath = "//*[@text='Annotate']";
    private String fullScreenXpath = "//*[@text='enter full screen']";
    String ProjectPath=configurationPO.getProjectPath();



    private String pushFileSDCARD = "adb push " + ProjectPath + "\\updateFile\\MenuTaskbar\\trailer.mp4 /sdcard/";
    private String deleteSDCARDFile = "adb shell rm sdcard/trailer.mp4";
    /*private String pushFileUSB = "adb push " + ProjectPath + "\\updateFile\\MenuTaskbar\\trailer.mp4 "+USBPath;

    private String deleteUSBFile = "adb shell rm "+USBPath+"/trailer.mp4";*/

    /**
     * C116266 Verify that menu bar auto-hides after 2 minutes(120s) of non-interactivity
     * Press menu button to open menu bar;----Menu bar appears
     * Don't use panel for 2 minutes;----Menu bar should hide after 2 minutes of non-interactivity
     * Press menu button on remote;----Menu bar should appear
     * Don't use panel for 2 minutes;----Menu bar should hide after 2 minutes of non-interactivity
     */
    @Test(groups= "P1")
    public void C116266VerifyThatMenuBarAutoHidesAfter2MinutesOfNonInteractivity() {
        TestRail.setTestRailId("116266");

        TestRail.addStepName("Press menu button to open menu bar,Don't use panel for 2 minutes and menu bar should hide");
        AppiumHelper.hideTaskbar();
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_layout"), 3);
        AppiumHelper.waitForSeconds(120);
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/menu_icon_layout"), 20);

        TestRail.addStepName("Press menu button on remote,Don't use panel for 2 minutes and menu bar should hide");
        AppiumHelper.hideTaskbar();
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_layout"), 3);
        AppiumHelper.waitForSeconds(120);
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/menu_icon_layout"), 20);
    }

    /**
     * C90114 Verify that Menu bar is hidden when media is displayed full screen
     * Steps
     * 1. Plug in a USB disk with video files or copy videos to the panel local storage
     * 2. Open Files app
     * 3. Click on video to play it
     * 4. Switch to floating window
     * 5. Switch to full screen window (TBD)
     * Author: Sita
     */
    @Test(groups= "P2")
    public void C90114VerifyThatMenuBarIsHiddenWhenMediaIsDisplayedFullScreen() throws Exception {
        String USBPath=AppiumHelper.getUSB();
        String pushFileUSB = "adb push " + ProjectPath + "\\updateFile\\MenuTaskbar\\trailer.mp4 "+USBPath;

        String deleteUSBFile = "adb shell rm "+USBPath+"/trailer.mp4";
        TestRail.setTestRailId("90114");
        TestRail.addStepName("Plug in an USB disk with video files or copy videos to the panel local storage");
        //Playing a video from the USB
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        try {

            Runtime.getRuntime().exec(pushFileUSB);
            TestRail.addStepName("Open Files app");
            AppiumHelper.showTaskbar();
            AppiumHelper.findElementBy(Locator.byText("Applications")).click();
            ElementHelper.waitUntilVisible(Locator.byText("Files"),10);
            AppiumHelper.findElementBy(Locator.byText("Files")).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
            ElementHelper.waitUntilPresent(Locator.byContentDesc("Show roots"), 10);
            AppiumHelper.findElementBy(By.xpath("//android.widget.ImageButton[@content-desc='Show roots']")).click();
            AppiumHelper.findElement(maximizeXpath, 5).click();
            ElementHelper.click(By.xpath(eleUsbStorage));
            Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
            TestRail.addStepName("Click on video to play it");
            AppiumHelper.findElement(maximizeXpath, 5).click();
            AppiumHelper.findElementBy(By.xpath("//android.widget.TextView[@text='trailer.mp4']")).click();
            TestRail.addStepName("Switch to floating window");
            ElementHelper.waitUntilPresent(Locator.byResourceId("android:id/maximize_window"), 10);
            AppiumHelper.findElement(maximizeXpath, 5).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
            TestRail.addStepName("Switch to full screen window (TBD)");
            ElementHelper.waitUntilPresent(Locator.byResourceId("android:id/maximize_window"), 10);
            AppiumHelper.findElement(maximizeXpath, 5).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
            AppiumHelper.findElement(closeXpath).click();
            Runtime.getRuntime().exec(deleteUSBFile);
            ElementHelper.waitUntilPresent(Locator.byResourceId("android:id/close_window"), 10);
            AppiumHelper.findElementBy(By.id("android:id/close_window")).click();
        } catch (Exception e) {
            Assert.fail("Test Case Failed due to: " + e.getMessage());
        }
        //PLaying video from internal storage
        try {

            Runtime.getRuntime().exec(pushFileSDCARD);
            TestRail.addStepName("Open Files app");
            AppiumHelper.showTaskbar();
            AppiumHelper.findElementBy(Locator.byText("Applications")).click();
            ElementHelper.waitUntilVisible(Locator.byText("Files"),10);
            AppiumHelper.findElementBy(Locator.byText("Files")).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            ElementHelper.waitUntilPresent(Locator.byContentDesc("Show roots"),10);
            AppiumHelper.findElementBy(By.xpath("//android.widget.ImageButton[@content-desc='Show roots']")).click();
            ElementHelper.waitUntilPresent(Locator.byText("Internal Storage"), 10);
            AppiumHelper.findElementBy(By.xpath("//android.widget.LinearLayout[6]")).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            TestRail.addStepName("Click on video to play it");
            AppiumHelper.findElement(maximizeXpath, 5).click();
            AppiumHelper.findElementBy(By.xpath("//android.widget.TextView[@text='trailer.mp4']")).click();
            TestRail.addStepName("Switch to floating window");
            ElementHelper.waitUntilPresent(Locator.byResourceId("android:id/maximize_window"),10);
            AppiumHelper.findElement(maximizeXpath, 5).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            TestRail.addStepName("Switch to full screen window (TBD)");
            ElementHelper.waitUntilPresent(Locator.byResourceId("android:id/maximize_window"),10);
            AppiumHelper.findElement(maximizeXpath, 5).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            AppiumHelper.findElement(closeXpath).click();
            Runtime.getRuntime().exec(deleteSDCARDFile);
            ElementHelper.waitUntilPresent(Locator.byResourceId("android:id/close_window"), 10);
            AppiumHelper.findElementBy(By.id("android:id/close_window")).click();
        } catch (Exception e) {
            Assert.fail("Test Case Failed due to: "+e.getMessage());
        }
    }

    /**
     * C90134 Verify that users can call up menu bar when playing video in full screen
     * Steps
     * 1. Playing a video and switch to full screen (TBD)
     * 2. Press the Menu button
     * Author: Sita
     */
    @Test(groups= "P2")
    public void C90134VerifyThatUsersCanCallUpMenuBarWhenPlayingVideoInfullScreen() {
        TestRail.setTestRailId("90134");
        TestRail.addStepName("Playing a video and switch to full screen (TBD)");
        try {
            Runtime.getRuntime().exec(pushFileSDCARD);
            AppiumHelper.showTaskbar();
            AppiumHelper.findElementBy(Locator.byText("Applications")).click();
            ElementHelper.waitUntilVisible(Locator.byText("Files"),10);
            AppiumHelper.findElementBy(Locator.byText("Files")).click();
            ElementHelper.waitUntilPresent(Locator.byContentDesc("Show roots"),10);
            AppiumHelper.findElementBy(By.xpath("//android.widget.ImageButton[@content-desc='Show roots']")).click();
            ElementHelper.waitUntilPresent(Locator.byText("Internal Storage"), 10);
            final By internalStorage = Locator.byText("Internal Storage");
            ElementHelper.click(internalStorage);
            AppiumHelper.findElement(maximizeXpath, 5).click();
            AppiumHelper.findElementBy(By.xpath("//android.widget.TextView[@text='trailer.mp4']")).click();
            Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            TestRail.addStepName("Press the Menu button");
            AppiumHelper.showTaskbar();
            Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            AppiumHelper.waitForSeconds(121);
            Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
            Runtime.getRuntime().exec(deleteSDCARDFile);
            AppiumHelper.findElement(closeXpath).click();
        } catch (IOException e) {
            Assert.fail("Test Case failed due to: "+e.getMessage());
        }
    }

    /**
     * C116268 Verify that users can reset the auto-hide timer by controlling the menu bar with the remote
     * Steps
     * 1. Press remote menu button
     * 2. Press the left/right buttons on the remote for 10 seconds
     * 3. Wait for another 120 seconds of non-interactivity
     * Author: Sita
     */
    @Test(groups= "P2")
    public void C116268VerifyThatUsersCanResetTheAutoHideTimerByControllingTheMenuBarWithTheRemote() {
        int time = 0;
        TestRail.setTestRailId("116268");
        TestRail.addStepName("Press remote menu button");
        AppiumHelper.showTaskbar();
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
        TestRail.addStepName("Press the left/right buttons on the remote for 10 seconds");
        while (time < 11) {
            AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
            time++;
            Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
        }
        TestRail.addStepName("Wait for another 120 seconds of non-interactivity");
        AppiumHelper.waitForSeconds(120);
        Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"),10));
    }

    /**
     * C116293 Verify that menuBar will disappear after opening apps
     * Steps
     * 1. Press the menu button on the panel
     * 2. Click on Applications icon
     * 3. Press the menu button again
     * 4. Click on Annotate icon to open it
     * Author: Sita
     */
    @Test(groups= "P2")
    public void C116293VerifyThatMenuBarWillDisappearAfterOpeningApps() {
        TestRail.setTestRailId("116293");
        TestRail.addStepName("Press the menu button on the panel");
        AppiumHelper.showTaskbar();
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
        TestRail.addStepName("Click on Applications icon");
        AppiumHelper.findElementBy(By.xpath(appXpath)).click();
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.locker:id/tv_title"), 10));
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
        TestRail.addStepName("Press the menu button again");
        AppiumHelper.showTaskbar();
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
        TestRail.addStepName("Click on Annotate icon to open it");
        AppiumHelper.showTaskbar();
        AppiumHelper.findElementBy(By.xpath(annotateXpath)).click();
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.unifiedlauncher:id/toolbar_layout"), 10));
        Adb.forceStop("com.prometheanworld.annotate");
    }
}
