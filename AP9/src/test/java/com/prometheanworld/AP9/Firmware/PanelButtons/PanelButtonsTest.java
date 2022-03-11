package com.prometheanworld.AP9.Firmware.PanelButtons;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.appium.DriverCache;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
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

import static com.nd.automation.core.kotlin.AssertKt.assertEquals;
import static com.nd.automation.core.kotlin.AssertKt.assertNotEquals;
import static com.prometheanworld.appium.frame.hardware.PrometheanKey.*;
import static com.prometheanworld.appium.frame.hardware.PrometheanKeyboard.ActivPanel;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class PanelButtonsTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        // Tap outside to hide the task bar and locker
        ScreenHelper.clickAt(Location.CENTER);
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
        AppiumHelper.hideTaskbar();
    }

    /**
     * C112658 Menu - show/hide the taskbar
     * When the Menu is closed, and the panel is turned on
     * 1. Press the Menu button for the menu. ---- The menu should now open on the panel.
     * 2. Press the Menu button again. ---- The menu should close.
     */
    @Test(groups = "P0")
    public void C112658PanelMenuButtonCanShowAndHideTheTaskBar() {
        // start test
        TestRail.setTestRailId("112658");
        TestRail.addStepName("1. Hide the taskbar");
        AppiumHelper.hideTaskbar();
        TestRail.addStepName("2. Press the menu button");
        AppiumHelper.clickKey(Menu, ActivPanel);
        TestRail.addStepName("3. The taskbar should now open on the panel");
        ElementHelper.waitUntilPresent(Locator.byText("Applications"), 3);
        TestRail.addStepName("4. Press the menu button again");
        AppiumHelper.clickKey(Menu, ActivPanel);
        TestRail.addStepName("5. The taskbar should now close on the panel");
        ElementHelper.waitUntilNotPresent(Locator.byText("Applications"), 3);
    }

    /**
     * C112645 Sources - Panel Button opens sources
     * When the source menu closed and no external source is attached
     * 1. Press the panel Source button ---- Source Switch menu displays, shows image and name
     * list of available sources and displays the "Home" source is the one currently in use
     * 2. Press the panel Source button again ---- Source view close
     */
    @Test(groups = "P0")
    public void C112645PanelSourceButtonCanOpenSource() {
        TestRail.setTestRailId("112645");
        TestRail.addStepName("1. Hide the source view");
        POFactory.getInstance(SourceSwitchPO.class).hideSourceView();
        TestRail.addStepName("2. Press the source button");
        AppiumHelper.clickKey(Sources, ActivPanel);
        TestRail.addStepName("3. The source should now open on the panel");
        ElementHelper.waitUntilPresent(Locator.byText("Sources"), 3);
        TestRail.addStepName("4. Press the source button again");
        AppiumHelper.clickKey(Sources, ActivPanel);
        TestRail.addStepName("5. The source list view should now close on the panel");
        ElementHelper.waitUntilNotPresent(Locator.byText("Sources"), 3);
    }

    /**
     * C112640 Panel Volume - Volume Down
     * 1. Press the volume down button ---- Volume bar should appear and volume should decrease
     * by 1 every time you press the button
     * 2. Hold the "down" button ---- the volume should keep decreasing until it hits Min
     */
    @Test(groups = "P0")
    public void C112640PanelVolumeDownButtonCanDecreaseVolume() {
        TestRail.setTestRailId("112640");

        int initVolume = 30;
        TestRail.addStepName("Init: set volume to " + initVolume);
        POFactory.getInstance(SystemPO.class).setVolume(initVolume);

        TestRail.addStepName("1. Click volume down button, volume bar should appear and " +
                "volume should decrease by 1");
        AppiumHelper.clickKey(VolumeDown, ActivPanel);
        MobileElement volumeBar = ElementHelper.findElement(Locator.byResourceId("id/volume_bar"));
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), initVolume - 1);

        TestRail.addStepName("2. Hold the down button, the volume should keep decreasing until it hits min");
        AppiumHelper.longClickKey(VolumeDown, ActivPanel, 5);
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), 0);
    }

    /**
     * C112639 Panel Volume - Volume Up
     * 1. Press the volume up button ---- Volume bar should appear and increase the volume by
     * 1 every time you press the button
     * 2. Hold the "up" button ---- the volume should keep increasing until it hits max
     */
    @Test(groups = "P0")
    public void C112639PanelVolumeUpButtonCanIncreaseVolume() {
        TestRail.setTestRailId("112639");

        int initVolume = 70;
        TestRail.addStepName("Init: set volume to " + initVolume);
        POFactory.getInstance(SystemPO.class).setVolume(initVolume);

        TestRail.addStepName("1. Click volume up button, volume bar should appear and " +
                "volume should increase by 1");
        AppiumHelper.clickKey(VolumeUp, ActivPanel);
        MobileElement volumeBar = ElementHelper.findElement(Locator.byResourceId("id/volume_bar"));
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), initVolume + 1);

        TestRail.addStepName("2. Hold the up button, the volume should keep increasing until it hits max");
        AppiumHelper.longClickKey(VolumeUp, ActivPanel, 5);
        Assert.assertEquals(Integer.parseInt(volumeBar.getText().split("\\.")[0]), 100);
    }

    /**
     * C112636 Power- Long press to Sleep and click Put to Sleep button to turn off panel
     * 1. Long press the power button ---- Pops up power option dialog: "Be sure all your work is saved",
     * "Putting the Panel to sleep or restarting will cause you to lose any unsaved data." and three
     * options: Cancel, Restart, Put to Sleep
     * 2. Click Put to Sleep button ---- Pops up a rotating circle with message "Power off", then panel
     * goes into sleep mode successfully
     */
//    @Test
    public void C112636testPanelPowerButtonCanEnterSleepStatus() {
        TestRail.setTestRailId("112636");

        TestRail.addStepName("1. Long press the power button, pops up power option dialog");
        AppiumHelper.longClickKey(Power, ActivPanel, 3);
        ElementHelper.findElement(Locator.byResourceId("id/sleep_or_reboot_title"));

        TestRail.addStepName("2. Click Put to Sleep button, panel goes into sleep mode successfully");
        ElementHelper.findElement(Locator.byResourceId("id/sleep_or_reboot_button_sleep")).click();
        // todo check panel has shutdown, and we need a function to restart panel, otherwise
        //  it will affect other tests.

    }

    /**
     * C112635 Power- Quick Press to Standby
     * 1. Open Chromium
     * 2. Quick press the power button to turn off the screen ---- Enter standby mode
     * 3. Quick press the power button again ---- wake the panel up
     * 4. Chromium is running
     */
    @Test(groups = {"P0","UserDebug"})
    public void C112635PanelPowerButtonCanEnterStandbyStatus() throws InterruptedException {
        TestRail.setTestRailId("112635");

        // adb root panel
        Adb.root();

        TestRail.addStepName("1. Open Chromium");
        Thread.sleep(1000);
        POFactory.getInstance(SystemPO.class).findAppFromApplications("Chromium").click();

        TestRail.addStepName("2. Quick press the power button to turn off the screen");
        AppiumHelper.clickKey(Power, ActivPanel);
        Thread.sleep(1000);
        int powerStatus = Integer.parseInt(Adb.adb("shell getprop sys.xbh.powerstatus").get(0));
        Assert.assertEquals(powerStatus, 1);

        TestRail.addStepName("3. Quick press the power button again to wake panel");
        AppiumHelper.clickKey(Power, ActivPanel);
        Thread.sleep(1000);
        powerStatus = Integer.parseInt(Adb.adb("shell getprop sys.xbh.powerstatus").get(0));
        Assert.assertEquals(powerStatus, 2);

        TestRail.addStepName("4. Check chromium is running");
        AppiumHelper.showTaskbar();
        ElementHelper.findElement(Locator.byText("Chromium"));
        POFactory.getInstance(SystemPO.class).closeAppOnMenuBar("Chromium");
    }

    /**
     * C115441 Power- Long press to Sleep and click Restart button to reboot panel
     * 1. Open Chromium and search for things
     * 2. Long press the power button ---- Pops up power option dialog with three options: Cancel, Restart, Put to Sleep
     * 3. Click Restart button ---- Pops up a rotating circle with message "Power off", then panel goes into sleep mode and restart successfully, the Chromium closed after booting
     */
    @Test(groups = "P1")
    public void C115441LongPressPanelPowerButtonCanRebootPanel() {
        TestRail.setTestRailId("115441");

        TestRail.addStepName("Open Chromium and search for things");
        POFactory.getInstance(SystemPO.class).findAppFromApplications("Chromium").click();

        TestRail.addStepName("Long press the power button");
        AppiumHelper.longClickKey(Power, ActivPanel, 3);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/sleep_or_reboot_title"));

        TestRail.addStepName("Click Restart button");
        ElementHelper.findElement(Locator.byResourceId("id/sleep_or_reboot_button_reboot")).click();
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
     * C115442 Power- Power- Long press to Sleep and click Cancel button
     * 1. Open Chromium and search for things ---- Can search for things
     * 2. Long press the power button ---- Pops up power option dialog with three options: Cancel, Restart, Put to Sleep
     * 3. Click Cancel button ---- The dialog disappears and panel does not turn off
     * 4. Reopen Chromium and search for things ---- Can search for things
     */
    @Test(groups = "P1")
    public void C115442LongPressPanelPowerButtonAndCancelReboot() {
        TestRail.setTestRailId("115442");

        TestRail.addStepName("Open Chromium and search for things");
        POFactory.getInstance(SystemPO.class).findAppFromApplications("Chromium").click();

        TestRail.addStepName("Long press the power button");
        AppiumHelper.longClickKey(Power, ActivPanel, 3);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/sleep_or_reboot_title"));

        TestRail.addStepName("Click Cancel button");
        ElementHelper.findElement(Locator.byResourceId("id/sleep_or_reboot_button_cancel")).click();

        String currentPackageName = Driver.getAndroidDriver().getCurrentPackage();
        assertEquals(currentPackageName, "org.chromium.chrome", "Current opened app is: " + currentPackageName);
        POFactory.getInstance(SystemPO.class).closeAppOnMenuBar("Chromium");
    }
}
