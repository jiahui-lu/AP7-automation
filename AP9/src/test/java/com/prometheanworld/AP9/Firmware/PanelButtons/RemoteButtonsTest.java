package com.prometheanworld.AP9.Firmware.PanelButtons;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.appium.DriverCache;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP9.SourceSwitchPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.nd.automation.core.kotlin.AssertKt.*;
import static com.prometheanworld.appium.frame.hardware.PrometheanKey.*;
import static com.prometheanworld.appium.frame.hardware.PrometheanKey.Power;
import static com.prometheanworld.appium.frame.hardware.PrometheanKeyboard.RemoteControl;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class RemoteButtonsTest extends BaseTest {
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        // Tap outside to hide the system UI
        ScreenHelper.clickAt(Location.CENTER_TOP);
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
    }

    /**
     * C92075 Remote Control- source button(source list)
     * Steps:
     * 1. When the source menu closed, press the Source button on the remote control ---- Source menu should open, shows image and name list of available sources
     * 2. Press the Source button on the remote control again ---- Source menu should close
     * Author: huiqian.chen
     */
    @Test(groups = "P0")
    public void C92075RemoteSourceButtonCanOpenSource() {
        TestRail.setTestRailId("92075");

        TestRail.addStepName("When the source menu closed, press the Source button on the remote control");
        POFactory.getInstance(SourceSwitchPO.class).hideSourceView();
        AppiumHelper.clickKey(Sources, RemoteControl);
        TestRail.addStepName("Source menu should open, shows image and name list of available sources");
        ElementHelper.waitUntilPresent(Locator.byText("Sources"), 3);

        TestRail.addStepName("Press the Source button on the remote control again");
        AppiumHelper.clickKey(Sources, RemoteControl);
        TestRail.addStepName("Source menu should close");
        ElementHelper.waitUntilNotPresent(Locator.byText("Sources"), 3);
    }

    /**
     * C92094 Remote Control - volume up
     * Steps:
     * 1. Press the volume up button on the remote ---- Volume bar should appear and increase the volume by 1 every time you press the button
     * 2. If you hold down the "up" button ---- the volume should keep increasing until it hits Max
     * Author: huiqian.chen
     */
    @Test(groups = "P0")
    public void C92094RemoteVolumeUpButtonCanIncreaseVolume() {
        TestRail.setTestRailId("92094");

        int initVolume = 70;
        Log.info("Init: set volume to " + initVolume);
        systemPO.setVolume(initVolume);

        TestRail.addStepName("Press the volume up button on the remote");
        AppiumHelper.clickKey(VolumeUp, RemoteControl);
        TestRail.addStepName("Volume bar should appear and increase the volume by 1 every time you press the button");
        MobileElement volumeBar = ElementHelper.findElement(Locator.byResourceId("id/volume_bar"));
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), initVolume + 1);

        TestRail.addStepName("If you hold down the \"up\" button");
        AppiumHelper.longClickKey(VolumeUp, RemoteControl, 5);
        TestRail.addStepName("the volume should keep increasing until it hits Max");
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), 100);
    }

    /**
     * C92095 Remote Control - volume down
     * Steps:
     * 1. Press the volume down button on the remote control ---- Volume bar should appear on screen and volume decreases by 1 every time you press the button
     * 2. If you hold down the "down" button ---- the volume should keep decreasing until it hits Min
     * Author: huiqian.chen
     */
    @Test(groups = "P0")
    public void C92095RemoteVolumeDownButtonCanDecreaseVolume() {
        TestRail.setTestRailId("92095");

        int initVolume = 30;
        Log.info("Init: set volume to " + initVolume);
        systemPO.setVolume(initVolume);

        TestRail.addStepName("Press the volume down button on the remote control");
        AppiumHelper.clickKey(VolumeDown, RemoteControl);
        TestRail.addStepName("Volume bar should appear on screen and volume decreases by 1 every time you press the button");
        MobileElement volumeBar = ElementHelper.findElement(Locator.byResourceId("id/volume_bar"));
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), initVolume - 1);

        TestRail.addStepName("If you hold down the \"down\" button");
        AppiumHelper.longClickKey(VolumeDown, RemoteControl, 5);
        TestRail.addStepName("the volume should keep decreasing until it hits Min");
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), 0);
    }

    /**
     * C92074 Remote Control - power button(standby)
     * Steps:
     * 1. with the panel turn on, open Chrome to search for things ---- Chrome can search for things
     * 2. Quick press the power button on the remote control to turn off the screen ---- the panel should enter standby mode
     * 3. Quick press the power button again to turn the panel on ---- the previous opened Chromium can still search for things
     * Author: huiqian.chen
     */
    @Test(groups = {"P0","UserDebug"})
    public void C92074RemotePowerButtonCanEnterStandbyStatus() throws InterruptedException {
        TestRail.setTestRailId("92074");

        // adb root panel
        Adb.root();
        TestRail.addStepName("with the panel turn on, open Chrome to search for things");
        Thread.sleep(1000);
        systemPO.findAppFromApplications("Chromium").click();

        TestRail.addStepName("Quick press the power button on the remote control to turn off the screen");
        AppiumHelper.clickKey(Power, RemoteControl);
        Thread.sleep(1000);
        int powerStatus = Integer.parseInt(Adb.adb("shell getprop sys.xbh.powerstatus").get(0));
        Assert.assertEquals(powerStatus, 1);

        TestRail.addStepName("Quick press the power button again to turn the panel on");
        AppiumHelper.clickKey(Power, RemoteControl);
        Thread.sleep(1000);
        powerStatus = Integer.parseInt(Adb.adb("shell getprop sys.xbh.powerstatus").get(0));
        Assert.assertEquals(powerStatus, 2);
        AppiumHelper.showTaskbar();
        ElementHelper.findElement(Locator.byText("Chromium"));
        systemPO.closeAppOnMenuBar("Chromium");
    }

    /**
     * C112663 Remote Control - Menu button
     * Steps:
     * 1. When the menu closed, press the Menu button ---- The menu should now open on the panel
     * 2. use remote control to start navigating to open an application while not next to the panel ---- can select remote control to select application and open it.
     * 3. Press the Menu button again. ---- The menu should now open on the panel
     * Author: huiqian.chen
     */
    @Test(groups = "P0")
    public void C112663RemoteMenuButtonToShowAndHideMenu() {
        TestRail.setTestRailId("112663");

        TestRail.addStepName("When the menu closed, press the Menu button");
        AppiumHelper.hideTaskbar();
        AppiumHelper.clickKey(Menu, RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"),3);
        ElementHelper.waitUntilPresent(Locator.byText("Applications"), 3);
        assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/menu_icon_locker").getAttribute("focused"), "true", "Applications is not focused");

        TestRail.addStepName("use remote control to start navigating to open an application while not next to the panel");
        // press the remote center button to open Locker
        Adb.adb("shell input keyevent 23");
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/locker_container"),3);
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/menu_bar"),3);

        TestRail.addStepName("Press the menu button again");
        AppiumHelper.clickKey(Menu, RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"),3);
        ElementHelper.waitUntilPresent(Locator.byText("Applications"), 3);
    }

    /**
     * C120187 Verify that users can press settings button on the remote control to open and close AVI settings
     * Steps:
     * 1. Press settings button on the remote control ---- AVI settings app launches
     * 2. Press settings button on the remote control again ---- AVI settings app closes
     * Author: huiqian.chen
     */
    @Test(groups = "P0")
    public void C120187RemoteSettingsButtonCanOpenAndCloseAVISettings() {
        TestRail.setTestRailId("120187");

        TestRail.addStepName("Press settings button on the remote control");
        AppiumHelper.clickKey(Settings, RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/app_label"),3);
        ElementHelper.waitUntilPresent(Locator.byText("Panel Settings"), 3);

        TestRail.addStepName("Press settings button on the remote control again");
        AppiumHelper.clickKey(Settings, RemoteControl);
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/app_label"),3);
        ElementHelper.waitUntilNotPresent(Locator.byText("Panel Settings"), 3);
    }

    /**
     * C115478 Remote Control - power button(restart)
     * 1. Open Chromium and search for things ---- Chrome can search for things
     * 2. Long press the remote power button ---- Pops up power option dialog with three options: Cancel, Restart, Put to Sleep, the highlight is on Cancel button
     * 3. Press the right arrow button to go to Restart icon
     * 4. Press the center button ---- Pops up a rotating circle with message "Power off", then panel goes into sleep mode and restart successfully, the Chromium closed after booting, all the lights light up again
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C115478LongPressRemotePowerButtonCanRebootPanel() {
        TestRail.setTestRailId("115478");

        TestRail.addStepName("Open Chromium and search for things");
        systemPO.findAppFromApplications("Chromium").click();

        TestRail.addStepName("Long press the remote power button");
        AppiumHelper.longClickKey(Power, RemoteControl, 3);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/sleep_or_reboot_title"));

        TestRail.addStepName("Press the right arrow button to go to Restart icon");
        systemPO.setFocusToPanelShutDownMessage();
        Adb.adb("shell input keyevent 22");
        TestRail.addStepName("Press the center button");
        Adb.adb("shell input keyevent 23");
        try {
            AppiumHelper.waitForSeconds(15);
            Adb.adbConnect(Driver.getDeviceName(), 300);
            DriverCache.getDriverCache().restartDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String currentPackageName = Driver.getAndroidDriver().getCurrentPackage();
        assertNotEquals(currentPackageName, "org.chromium.chrome", "Current opened app is: " + currentPackageName);
    }

    /**
     * C115479 Remote Control - power button(cancel)
     * 1. Open Chromium and search for things ---- Can search for things
     * 2. Long press the remote power button ---- Pops up power option dialog with three options: Cancel, Restart, Put to Sleep
     * 3. Press the center button ---- The dialog disappears and panel does not turn off
     * 4. Reopen Chromium and search for things ---- Can search for things
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C115479LongPressRemotePowerButtonAndCancelReboot() {
        TestRail.setTestRailId("115479");

        TestRail.addStepName("Open Chromium and search for things");
        systemPO.findAppFromApplications("Chromium").click();

        TestRail.addStepName("Long press the remote power button");
        systemPO.setFocusToPanelShutDownMessage();
        AppiumHelper.longClickKey(Power, RemoteControl, 3);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/sleep_or_reboot_title"));

        TestRail.addStepName("Press the center button");
        Adb.adb("shell input keyevent 23");

        TestRail.addStepName("Reopen Chromium and search for things");
        String currentPackageName = Driver.getAndroidDriver().getCurrentPackage();
        assertEquals(currentPackageName, "org.chromium.chrome", "Current opened app is: " + currentPackageName);
        systemPO.closeAppOnMenuBar("Chromium");
    }

    /**
     * C113270 Remote Control - Directional Pad - Verify that default focus is on Application icon
     * 1. Press flame button on remote ---- Menu should appear and focus should be on application icon by default
     * 2. Press directional pad to select an application, such as Screen Capture app
     * 3. Press center button to open Screen Capture app ---- Screen Capture app should be open and the menu should hide; The focus is on Screen Capture app
     * 4. Press flame button on remote again ---- Menu should appear and focus should be on Application icon
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C113270DefaultFocusIsOnApplicationIcon() {
        TestRail.setTestRailId("113270");

        ScreenHelper.clickAt(Location.CENTER_TOP);
        TestRail.addStepName("Press flame button on remote");
        AppiumHelper.longClickKey(Menu, RemoteControl, 3);
        assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/menu_icon_locker").getAttribute("focused"), "true", "Applications is not focused");

        TestRail.addStepName("Press directional pad to select an application, such as Screen Capture app");
        Adb.adb("shell input keyevent 22");
        Adb.adb("shell input keyevent 22");
        assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/menu_icon_locker").getAttribute("focused"), "false", "Applications is focused");
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@text='Screen Capture']/parent::" +
                "android.widget.LinearLayout").getAttribute("focused"), "true", "Screen Capture is not focused");

        TestRail.addStepName("Press center button to open Screen Capture app");
        Adb.adb("shell input keyevent 23");
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.screencapture:id/title"));
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/menu_bar"),3);
        assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.screencapture:id/save_as").getAttribute("focused"), "true", "Screen Capture is not focused");

        TestRail.addStepName("Press flame button on remote again");
        AppiumHelper.longClickKey(Menu, RemoteControl, 3);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"),3);
        assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/menu_icon_locker").getAttribute("focused"), "true", "Applications is not focused");
    }
}