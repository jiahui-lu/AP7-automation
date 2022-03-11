/*
 * © 2021 Promethean. All Rights Reserved.
 *
 * Unauthorized copying of this file or any part of this file
 * via any medium is strictly prohibited.
 */
package com.prometheanworld.AP9.Firmware.AndroidSettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class UserAccess extends BaseTest {
    public static final String SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS = "prome_lock_admin_access";
    private static final String ENABLE_APPS = "prome_enable_apps";

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        super.afterMethod();
        try {
            AppiumHelper.exeAdbRoot();
            Adb.setGlobalSettings("prome_lock_admin_access", "0");
        } catch (Exception e) {}
    }

    /**
     * C120331 Verify that settings, panel management and update can be hidden by pressing the Konami code sequence
     * Preconditions:
     *   When settings, panel management and update are unhidden and enable apps is on
     * Press volume down button to reduce the volume down to zero
     * Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down
     * Pop up a lock confirmation message with two options: Lock Access and Cancel ---- Lock admin access
     * Select Lock Access ---- settings, panel management and update app are hidden in Application Locker
     */
    @Test(groups = {"P0"})
    public void C120331VerifyThatSettingsPanelManagementAndUpdateCanBeHiddenByPressingTheKonamiCodeSequence() {
        TestRail.setTestRailId("120331");
        final int originVolume = systemPO.getVolume();
        final String originAccess = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        final String originEnableApps = Adb.getGlobalSettings(ENABLE_APPS);
        setTestCaseCleaner(() -> {
            // click outside to close access window or locker
            AppiumHelper.captureScreenshot("01");
            AppiumHelper.clickAt(0d, 0d);
            systemPO.setVolume(originVolume);
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, originAccess);
            Adb.setGlobalSettings(ENABLE_APPS, originEnableApps);
        });
        // precondition
        if ("1".equals(originAccess)) {
            Log.info("set prome_lock_admin_access 0");
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, "0");
        }
        if ("0".equals(originEnableApps)) {
            Log.info("set prome_enable_apps 1");
            Adb.setGlobalSettings(ENABLE_APPS, "1");
        }

        // down volume
        TestRail.addStepName("Press volume down button to reduce the volume down to zero");
        describeVolumeToZero();

        // press l-l-r-r-u-u-d-d
        TestRail.addStepName("Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down");
        pressKonamiCode();

        // check access dialog
        TestRail.addStepName("Pop up a lock confirmation message with two options: Lock Access and Cancel");

        AssertKt.assertPresent(Locator.byText("Lock admin access"));
        AssertKt.assertPresent(By.id("com.prometheanworld.panelbuttonservice:id/primary_button"));
        AssertKt.assertPresent(By.id("com.prometheanworld.panelbuttonservice:id/secondary_button"));

        TestRail.addStepName("Select Lock Access");
        ElementHelper.click(By.id("com.prometheanworld.panelbuttonservice:id/primary_button"));

        // check key change int settings
        String currentAccess = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        Assert.assertEquals(currentAccess, "1");

        // check apps dismiss in locker
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);

        int lockerPageSize = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab)).size();
        String itemLabelXpath = "//*[@resource-id=\"com.prometheanworld.locker:id/app_txt\"]";

        for (int currentPage = 1; currentPage <= lockerPageSize; currentPage++) {
            if (currentPage != 1) {
                systemPO.swipePageByTab(currentPage);
                AppiumHelper.waitForSeconds(1);
            }
//            List<MobileElement> items = ElementHelper.findElements(By.xpath(itemLabelXpath), 5);
//            for (MobileElement item : items) {
//                Assert.assertNotEquals(item.getText(), "Settings", "settings appear");
//                Assert.assertNotEquals(item.getText(), "Update", "update appear");
//                Assert.assertNotEquals(item.getText(), "Panel Management", "management appear");
//            }
            List<String> appNames = Arrays.asList(systemPO.getAllElementsText(itemLabelXpath));
            Log.info(appNames);
            Assert.assertFalse(appNames.contains("Settings"), "settings appear");
            Assert.assertFalse(appNames.contains("Update"), "update appear");
            Assert.assertFalse(appNames.contains("Panel Management"), "management appear");
        }
    }


    /**
     * C120332 Verify that hidden settings, panel management and update are still disable after rebooting
     * Steps:
     * 1. Press volume down button to reduce the volume down to zero
     * 2. Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down
     * 3. Pop up a lock confirmation message with two options: Lock Access and Cancel
     * 4. Select Lock Access
     * 5. Reboot the panel
     */
    @Test(groups = {"P1"})
    public void C120332VerifyThatHiddenSettingsPanelManagermentAndUpdateAreStillDisableAfterRebooting() {
        TestRail.setTestRailId("120332");
        final int originVolume = systemPO.getVolume();
        final String originAccess = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        final String originEnableApps = Adb.getGlobalSettings(ENABLE_APPS);
        setTestCaseCleaner(() -> {
            // click outside to close access window or locker
            AppiumHelper.captureScreenshot("01");
            AppiumHelper.clickAt(0d, 0d);
            systemPO.setVolume(originVolume);
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, originAccess);
            Adb.setGlobalSettings(ENABLE_APPS, originEnableApps);
        });
        // precondition
        if ("1".equals(originAccess)) {
            Log.info("set prome_lock_admin_access 0");
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, "0");
        }
        if ("0".equals(originEnableApps)) {
            Log.info("set prome_enable_apps 1");
            Adb.setGlobalSettings(ENABLE_APPS, "1");
        }

        // down volume
        TestRail.addStepName("Press volume down button to reduce the volume down to zero");
        describeVolumeToZero();

        // press l-l-r-r-u-u-d-d
        TestRail.addStepName("Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down");
        pressKonamiCode();

        // check access dialog
        TestRail.addStepName("Pop up a lock confirmation message with two options: Lock Access and Cancel");

        AssertKt.assertPresent(Locator.byText("Lock admin access"));
        AssertKt.assertPresent(By.id("com.prometheanworld.panelbuttonservice:id/primary_button"));
        AssertKt.assertPresent(By.id("com.prometheanworld.panelbuttonservice:id/secondary_button"));

        TestRail.addStepName("Select Lock Access");
        ElementHelper.click(By.id("com.prometheanworld.panelbuttonservice:id/primary_button"));

        // check key change int settings
        String currentAccess = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        Assert.assertEquals(currentAccess, "1");

        // check apps dismiss in locker
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);

        int lockerPageSize = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab)).size();
        String itemLabelXpath = "//*[@resource-id=\"com.prometheanworld.locker:id/app_txt\"]";

        for (int currentPage = 1; currentPage <= lockerPageSize; currentPage++) {
            if (currentPage != 1) {
                systemPO.swipePageByTab(currentPage);
                AppiumHelper.waitForSeconds(1);
            }
            List<String> appNames = Arrays.asList(systemPO.getAllElementsText(itemLabelXpath));
            Log.info(appNames);
            Assert.assertFalse(appNames.contains("Settings"), "settings appear");
            Assert.assertFalse(appNames.contains("Update"), "update appear");
            Assert.assertFalse(appNames.contains("Panel Management"), "management appear");
        }
        TestRail.addStepName("Reboot the panel");
        AppiumHelper.rebootPanel();
        // verify the locker again when rebooting successfully
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        for (int currentPage = 1; currentPage <= lockerPageSize; currentPage++) {
            if (currentPage != 1) {
                systemPO.swipePageByTab(currentPage);
                AppiumHelper.waitForSeconds(1);
            }
            List<String> appNames = Arrays.asList(systemPO.getAllElementsText(itemLabelXpath));
            Log.info(appNames);
            Assert.assertFalse(appNames.contains("Settings"), "settings appear");
            Assert.assertFalse(appNames.contains("Update"), "update appear");
            Assert.assertFalse(appNames.contains("Panel Management"), "management appear");
        }
    }

    /**
     * C120335 Verify that users can unhide settings, panel management and update
     * Steps:
     * 1. Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down
     * 2. Pop up a lock confirmation message with two options: UnLock Access and Cancel
     * 3. Select UnLock Access
     */
    @Test(groups = {"P1"})
    public void C120335VerifyThatUsersCanUnHideSettingsPanelManagementAndUpdate() {
        TestRail.setTestRailId("120335");
        final int originVolume = systemPO.getVolume();
        final String originAccess = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        final String originEnableApps = Adb.getGlobalSettings(ENABLE_APPS);
        setTestCaseCleaner(() -> {
            // click outside to close access window or locker
            AppiumHelper.captureScreenshot("01");
            AppiumHelper.clickAt(0d, 0d);
            systemPO.setVolume(originVolume);
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, originAccess);
            Adb.setGlobalSettings(ENABLE_APPS, originEnableApps);
        });
        // precondition
        if (!"1".equals(originAccess)) {
            Log.info("set prome_lock_admin_access 1");
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, "1");
        }
        if (!"0".equals(originEnableApps)) {
            Log.info("set prome_enable_apps 0");
            Adb.setGlobalSettings(ENABLE_APPS, "0");
        }
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        TestRail.addStepName("Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down");
        describeVolumeToZero();
        pressKonamiCode();
        TestRail.addStepName("Pop up a lock confirmation message with two options: UnLock Access and Cancel");
        ElementHelper.waitUntilPresent(Locator.byTextContains("Unlock admin access"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/secondary_button"));
        final By btnUnlockAccess = Locator.byResourceId("id/primary_button");
        ElementHelper.waitUntilPresent(btnUnlockAccess);
        TestRail.addStepName("Select UnLock Access");
        ElementHelper.click(btnUnlockAccess);

        final String currentAccess = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        Assert.assertEquals(currentAccess, "0", "The prome_lock_admin_access should be equals with zero");

        // check apps dismiss in locker
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        ElementHelper.waitUntilPresent(Locator.byText("Settings"));
        ElementHelper.waitUntilPresent(Locator.byText("Update"));
        ElementHelper.waitUntilPresent(Locator.byText("Panel Management"));
    }

    private void describeVolumeToZero() {
        int volumeDownTimes = 0;
        int currentVolume;
        do {
            AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.RemoteControl);
            currentVolume = systemPO.getVolume();
            volumeDownTimes += 1;
        } while (currentVolume > 0 && volumeDownTimes <= 100);
        Assert.assertEquals(currentVolume, 0, "volume can not down to 0");
    }

    private void pressKonamiCode() {
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);

        AppiumHelper.clickKey(PrometheanKey.RemoteRight, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteRight, PrometheanKeyboard.RemoteControl);

        AppiumHelper.clickKey(PrometheanKey.RemoteUp, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteUp, PrometheanKeyboard.RemoteControl);

        AppiumHelper.clickKey(PrometheanKey.RemoteDown, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteDown, PrometheanKeyboard.RemoteControl);
    }
}
