/*
 * © 2022 Promethean. All Rights Reserved.
 * Unauthorized copying of this file or any part of this file via any medium is strictly prohibited.
 */

package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.kotlin.AssumeKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class EnableWindowing extends BaseTest {
    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";

    @Override
    protected String testAppName() {
        return "Settings";
    }


    /**
     * C115042 Verify that the multi-window switch can switch to off
     * Steps:
     *  1. Open Android Settings app and click Display option
     *  2. Switch Enable Windowing to Off => Pops up a message with "Restart Required", "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later." and two buttons: Later and Restart
     *  3. Click Restart button to reboot panel =>
     *  4. Open Android Settings app after reboot => Displays all apps as full screen apps and display the bar
     *  5. Click Display option => Confirm that the Enable Windowing is Off
     *  Author: Ning Lu
     */
    @Test(groups = "P1")
    public void C115042VerifyThatTheMultiWindowSwitchCanSwitchToOff() {
        TestRail.setTestRailId("115042");
        TestRail.addStepName("Open Android Settings app and click Display option");
        final By switchLocator = By.xpath(getEnableWindowingSwitchWidget());
        final AtomicBoolean originalWindowingEnabled = new AtomicBoolean(false);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        originalWindowingEnabled.set(ElementHelper.isChecked(switchLocator));
        AssumeKt.assumeTrue(originalWindowingEnabled.get(),
                "The Enable windowing should be enabled before clicking the switch");
        setTestCaseCleaner(() -> {
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                    "Enable windowing is disappear");
            if (originalWindowingEnabled.get() != ElementHelper.isChecked(switchLocator)) {
                ElementHelper.click(switchLocator);
                if (ElementHelper.isPresent(Locator.byText("Restart"), 10L)) {
                    rebootPanel(() -> ElementHelper.click(Locator.byText("Restart")));
                }
            }
        });
        TestRail.addStepName("Switch Enable Windowing to Off");
        ElementHelper.click(switchLocator);
        TestRail.addStepName("Click Restart button to reboot panel");
        if (ElementHelper.isPresent(Locator.byText("Restart"), 10L)) {
            rebootPanel(() -> ElementHelper.click(Locator.byText("Restart")));
        }
        TestRail.addStepName("Open Android Settings app after reboot");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        checkAppFullScreen(true);
        TestRail.addStepName("Click Display option");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        AssertKt.assertFalse(ElementHelper.isChecked(switchLocator),
                "The Enable windowing option should be off state");
    }

    /**
     * C115417 Verify that the multi-window switch can switch to on
     * 1. Open Android Settings app and click Display option => Settings app does not display in windowed state
     * 2. Switch Enable Windowing to On => Pops up a message with "Restart Required", "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later." and two buttons: Later and Restart
     * 3. Click Restart button to reboot panel
     * 4. Open Android Settings app after reboot => Confirm that the Settings app displays in windowed state
     * 5. Click Display option => Confirm that the Enable Windowing is On
     * Author Ning Lu
     */
    @Test(groups = "P1")
    public void C115417VerifyThatTheMultiWindowSwitchCanSwitchToOn() {
        TestRail.setTestRailId("115417");
        TestRail.addStepName("Open Android Settings app and click Display option");
        final By switchLocator = By.xpath(getEnableWindowingSwitchWidget());
        final AtomicBoolean originalWindowingEnabled = new AtomicBoolean(false);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        originalWindowingEnabled.set(ElementHelper.isChecked(switchLocator));
        AssumeKt.assumeFalse(originalWindowingEnabled.get(),
                "The Enable windowing should be off before clicking the switch");
        setTestCaseCleaner(() -> {
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                    "Enable windowing is disappear");
            if (originalWindowingEnabled.get() != ElementHelper.isChecked(switchLocator)) {
                ElementHelper.click(switchLocator);
                if (ElementHelper.isPresent(Locator.byText("Restart"), 10L)) {
                    rebootPanel(() -> ElementHelper.click(Locator.byText("Restart")));
                }
            }
        });
        TestRail.addStepName("Switch Enable Windowing to On");
        ElementHelper.click(switchLocator);
        TestRail.addStepName("Click Restart button to reboot panel");
        if (ElementHelper.isPresent(Locator.byText("Restart"), 10L)) {
            rebootPanel(() -> ElementHelper.click(Locator.byText("Restart")));
        }
        TestRail.addStepName("Open Android Settings app after reboot");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        checkAppFullScreen(false);
        TestRail.addStepName("Click Display option");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        AssertKt.assertTrue(ElementHelper.isChecked(switchLocator),
                "The Enable windowing option should be on state");
    }

    /**
     * C114989 Verify that panel is in windowed state by default
     * Steps:
     *  1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     *  2. Open Android Settings app => Confirm that the Settings app is in windowed mode
     *  3. Click Display option => There is a "Enable Windowing" switch on Display page and it is On by default
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P1")
    public void C114989VerifyThatPanelIsInWindowedStateByDefault() {
        TestRail.setTestRailId("114989");

        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        TestRail.addStepName("Open Android Settings app");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should be in windowed mode");
        TestRail.addStepName("Click Display option");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        Assert.assertTrue(CommonOperator.scrollAndFind("com.android.settings:id/list", "Enable windowing"),
                "There should be a \"Enable Windowing\" switch on Display page");
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be On by default");
    }

    /**
     * C115424 Switch back to on when not reboot panel
     * Steps:
     *  1. Open Android Settings app and click Display option => Confirm that the Settings app is in windowed mode
     *  2. Switch Enable Windowing to Off => Pops up a message with "Restart Required", "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later." and two buttons: Later and Restart
     *  3. Click Later button => Confirm that panel still in multi-window state
     *  4. Switch Enable Windowing back to On => No confirm message pops up and panel still in multi-window state
     *  5. Reboot panel manually => Confirm that panel still in multi-window state
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C115424SwitchBackToOnWhenNotRebootPanel() {
        TestRail.setTestRailId("115424");

        TestRail.addStepName("Open Android Settings app and click Display option");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window"), 2000),
                "The Settings app should be in windowed mode");
        TestRail.addStepName("Switch Enable Windowing to Off");
        ElementHelper.click(Locator.byText("Enable windowing"));
        setTestCaseCleaner(() -> {
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                    "Enable windowing is disappear");
            if (!ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget()))) {
                ElementHelper.click(Locator.byText("Enable windowing"));
            }
            systemPO.hideMenuBar();
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            Adb.forceStop("com.prometheanworld.locker");
        });
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Restart required"), 2000),
                "The dialog that title is 'Restart required' should be visible");
        String content = "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later.";
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText(content)),
                "The dialog that content is '" + content + "' should be visible");
        TestRail.addStepName("Click Later button");
        ElementHelper.click(Locator.byText("Later"));
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be off");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should still be in windowed mode");
        TestRail.addStepName("Switch Enable Windowing back to On");
        ElementHelper.click(Locator.byText("Enable windowing"));
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("Restart required")),
                "The dialog that title is 'Restart required' should be not visible");
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should still be in windowed mode");
        AppiumHelper.rebootPanel();
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window"), 2000),
                "The Settings app should still be in windowed mode");
    }

    /**
     * C115049 Switch back and forth between on and off
     * Steps:
     *  1. Open Android Settings app and click Display option
     *  2. Switch Enable Windowing to Off then reboot panel => Confirm that panel does not in windowed state
     *  3. Switch Enable Windowing to on then reboot panel => Confirm that panel is in windowed state
     *  4. Repeat steps 2-3 for some times => Confirm that panel displays and runs normally
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C115049SwitchBackAndForthBetweenOnAndOff() {
        TestRail.setTestRailId("115049");

        revertConfigurationAfterComplete();
        setEnableWindowingTo(true);

        TestRail.addStepName("Repeat steps 2-3 for some times");
        for (int i = 0; i < 2; i++) {
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            TestRail.addStepName("Open Android Settings app and click Display option");
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                    "Enable windowing is disappear");
            Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                    "The switch 'Enable windowing' should be on");
            TestRail.addStepName("Switch Enable Windowing to Off then reboot panel");
            ElementHelper.click(Locator.byText("Enable windowing"));
            ElementHelper.waitUntilPresent(Locator.byText("Restart required"));
            rebootPanel(() -> {
                ElementHelper.click(Locator.byText("Restart"));
            });
            systemPO.startAppFromUnifiedLauncher(testAppName());
            checkAppFullScreen(true);
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                    "Enable windowing is disappear");
            Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                    "The switch 'Enable windowing' should be off");
            TestRail.addStepName("Switch Enable Windowing to on then reboot panel");
            ElementHelper.click(Locator.byText("Enable windowing"));
            ElementHelper.waitUntilPresent(Locator.byText("Restart required"));
            rebootPanel(() -> {
                ElementHelper.click(Locator.byText("Restart"));
            });
            systemPO.startAppFromUnifiedLauncher(testAppName());
            checkAppFullScreen(false);
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                    "Enable windowing is disappear");
            Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                    "The switch 'Enable windowing' should be on");
        }
    }

    /**
     * C115425 Switch back to off when not reboot panel
     * Steps:
     *  1. Open Android Settings app and click Display option => Confirm that the Settings app does not in windowed mode
     *  2. Switch Enable Windowing to On
     *     => Pops up a message with "Restart Required", "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later." and two buttons: Later and Restart
     *  3. Click Later button => Confirm that panel is still in non-windowed state
     *  4. Switch Enable Windowing back to Off => No confirm message pops up and panel still in non-windowed state
     *  5. Reboot panel manually => Confirm that panel is still in non-windowed state
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C115425SwitchBackToOffWhenNotRebootPanel() {
        TestRail.setTestRailId("115425");

        revertConfigurationAfterComplete();
        setEnableWindowingTo(false);

        TestRail.addStepName("Open Android Settings app and click Display option");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        checkAppFullScreen(true);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be off");
        TestRail.addStepName("Switch Enable Windowing to On");
        ElementHelper.click(Locator.byText("Enable windowing"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Restart required"), 2000),
                "The dialog that title is 'Restart required' should be visible");
        String content = "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later.";
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText(content)),
                "The dialog that content is '" + content + "' should be visible");
        TestRail.addStepName("Click Later button");
        ElementHelper.click(Locator.byText("Later"));
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
        Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should still be in non-windowed mode");
        TestRail.addStepName("Switch Enable Windowing back to Off");
        ElementHelper.click(Locator.byText("Enable windowing"));
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText(content)),
                "The dialog that content is '" + content + "' should be visible");
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
        Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should still be in non-windowed mode");
        AppiumHelper.rebootPanel();
        systemPO.startAppFromUnifiedLauncher(testAppName());
        checkAppFullScreen(true);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be off");
    }

    /**
     * C115416 Verify that the multi-window switch can switch to off but not reboot
     * Steps:
     *  1. Open Android Settings app and click Display option
     *  2. Switch Enable Windowing to off =>
     *     Pops up a message with "Restart Required", "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later." and two buttons: Later and Restart
     *  3. Click Later button => Confirm that panel still in multi-window state
     *  4. Reboot panel manually
     *  5. Open Android Settings app after reboot
     *  6. Click Display option => Confirm that the Enable Windowing is Off
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C115416VerifyThatTheMultiWindowSwitchCanSwitchToOffButNotReboot() {
        TestRail.setTestRailId("115416");

        revertConfigurationAfterComplete();
        setEnableWindowingTo(true);

        TestRail.addStepName("Open Android Settings app and click Display option");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
        TestRail.addStepName("Switch Enable Windowing to Off");
        ElementHelper.click(Locator.byText("Enable windowing"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Restart required"), 2000),
                "The dialog that title is 'Restart required' should be visible");
        String content = "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later.";
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText(content)),
                "The dialog that content is '" + content + "' should be visible");
        TestRail.addStepName("Click Later button");
        ElementHelper.click(Locator.byText("Later"));
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be off");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should still be in windowed mode");
        TestRail.addStepName("Reboot panel manually");
        AppiumHelper.rebootPanel();
        TestRail.addStepName("Open Android Settings app after reboot");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        checkAppFullScreen(true);
        TestRail.addStepName("Click Display option");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be off");
    }

    /**
     * C115418 Verify that the multi-window switch can switch to on but not reboot
     * Steps:
     *  1. Open Android Settings app and click Display option
     *  2. Switch Enable Windowing to on =>
     *     Pops up a message with "Restart Required", "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later." and two buttons: Later and Restart
     *  3. Click Later button => Confirm that panel is still in non-windowed state
     *  4. Reboot panel manually
     *  5. Open Android Settings app after reboot => Confirm that the Settings app displays in windowed state
     *  6. Click Display option => Confirm that the Enable Windowing is On
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C115418VerifyThatTheMulTiWindowSwitchCanSwitchToOnButNotReboot() {
        TestRail.setTestRailId("115418");

        revertConfigurationAfterComplete();
        setEnableWindowingTo(false);

        TestRail.addStepName("Open Android Settings app and click Display option");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be off");
        TestRail.addStepName("Switch Enable Windowing to on");
        ElementHelper.click(Locator.byText("Enable windowing"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Restart required"), 2000),
                "The dialog that title is 'Restart required' should be visible");
        String content = "To turn change Windowing options, the panel will need to restart. You can also choose to wait for later.";
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText(content)),
                "The dialog that content is '" + content + "' should be visible");
        TestRail.addStepName("Click Later button");
        ElementHelper.click(Locator.byText("Later"));
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
        Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")),
                "The Settings app should still be in non-windowed mode");
        TestRail.addStepName("Reboot panel manually");
        AppiumHelper.rebootPanel();
        TestRail.addStepName("Open Android Settings app after reboot");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        checkAppFullScreen(false);
        TestRail.addStepName("Click Display option");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())),
                "The switch 'Enable windowing' should be on");
    }

    /**
     * Displays all apps as full screen apps and display the bar
     * @param expectedFullScreen expected full screen or not
     */
    private void checkAppFullScreen(boolean expectedFullScreen) {
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/dashboard_container"));
        AssertKt.assertPresent(Locator.byResourceId("id/caption"));
        final boolean isMaximizeWindowIconVisible = ElementHelper.isVisible(Locator.byResourceId("id/maximize_window"));
        if (expectedFullScreen) {
            AssertKt.assertTrue(!isMaximizeWindowIconVisible,
                    "The maximize_window button should be invisible");
        } else {
            AssertKt.assertTrue(isMaximizeWindowIconVisible,
                    "The maximize_window button should be visible");
        }
    }

    private void rebootPanel(@Nullable Runnable rebootRunnable) {
        try {
            String ip = AppiumHelper.getDeviceStatus();
            Log.info("Reboot panel.");
            if (rebootRunnable != null) {
                rebootRunnable.run();
            }
            AppiumHelper.waitForSeconds(5);
            if (!(ip.equals(AppiumHelper.getDeviceStatus()))) {
                Log.info("Reboot not complete,wait for more 30s");
                AppiumHelper.waitForSeconds(30);
            }
            AppiumHelper.checkAndConnectDevice();
        } catch (Exception e) {
            e.printStackTrace();
            //throw new NoSuchElementException("Reboot failed");
        }
    }

    /**
     * revert configuration after complete the test.
     * please call this method in the beginning.
     *
     */
    private void revertConfigurationAfterComplete() {
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        AssertKt.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Enable windowing", 10),
                "Enable windowing is disappear");
        boolean originValue = ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget()));
        setTestCaseCleaner(() -> {
            setEnableWindowingTo(originValue);
        });
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
    }

    private void setEnableWindowingTo(boolean isOn) {
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.waitUntilVisible(Locator.byText("Enable windowing"));
        if (ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget())) != isOn) {
            ElementHelper.click(Locator.byText("Enable windowing"));
        }
        if (ElementHelper.isVisible(Locator.byText("Restart required"))) {
            ElementHelper.click(Locator.byText("Later"));
        }
        if (ElementHelper.isVisible(Locator.byResourceId("android:id/maximize_window")) != isOn) {
            AppiumHelper.rebootPanel();
        } else {
            systemPO.hideMenuBar();
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    private String getEnableWindowingSwitchWidget() {
        return "//*[ends-with(@resource-id, 'id/list')]" +
                "/*[descendant::*[@text='Enable windowing']]//*[ends-with(@resource-id, 'id/switch_widget')]";
    }
}