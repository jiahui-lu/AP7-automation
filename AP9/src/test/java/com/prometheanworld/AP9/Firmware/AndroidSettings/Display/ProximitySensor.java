/*
 * © 2021 Promethean. All Rights Reserved.
 *
 * Unauthorized copying of this file or any part of this file
 * via any medium is strictly prohibited.
 */
package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.kotlin.AssumeKt;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.SubSystem;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ProximitySensor extends BaseTest {

    @Override
    protected String testAppName() {
        return "Settings";
    }

    /**
     * C122594 Verify that proximity sensor settings is moved to display Screen and after standby and sleep option
     * Open Settings> Display ---- Proximity Sensor toggle should appear in display screen after Standby and Sleep Settings
     * Open Settings> System ---- Proximity Sensor toggle should be removed from System>Advanced Screen
     */
    @Test(groups = {"P0"})
    public void C122594VerifyThatProximitySensorSettingsIsMovedToDisplayScreenAndAfterStandbyAndSleepOption() {
        TestRail.setTestRailId("122594");
        AssumeKt.assumeTrue(SubSystem.isMT9950(), "Skip Proximity Sensor test, not MT9950");

        TestRail.addStepName("Open Settings> Display");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");

        if (AppiumHelper.isElementExistAndSwipePageByText("Advanced", 10)) {
            CommonOperator.scrollAndClick("com.android.settings:id/list", "Advanced");
        }
        // check Proximity sensor exist
        assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Proximity sensor", 10), "Proximity sensor is disappear");

        // check Proximity sensor content exist
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Proximity sensor");
        AssertKt.assertPresent(Locator.byText("Reset standby and sleep timer when motion is detected"));
        AssertKt.assertPresent(Locator.byText("Illuminate screen when motion is detected"));

        // back to settings home page
        Driver.pressKey(AndroidKey.BACK);
        Driver.pressKey(AndroidKey.BACK);

        TestRail.addStepName("Open Settings> System");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "System");
        if (AppiumHelper.isElementExistAndSwipePageByText("Advanced", 10)) {
            CommonOperator.scrollAndClick("com.android.settings:id/list", "Advanced");
        }

        // check Proximity sensor not exist
        assertFalse(AppiumHelper.isElementExistAndSwipePageByText("Proximity sensor", 10), "Proximity sensor is still displayed");

    }


    /**
     * C122596 Verify that toggles are available to select for proximity sensor
     * Steps:
     * 1. Open Settings> Display> Proximity Sensor => Reset Standby and Sleep Timer when motion is detected and Illuminate
     * screen when motion is detected toggles should be available
     */
    @Test(groups = {"P0"})
    public void C122596VerifyThatTogglesAreAvailableToSelectForProximitySensor() {
        TestRail.setTestRailId("122596");
        AssumeKt.assumeTrue(SubSystem.isMT9950(), "Skip Proximity Sensor test, not MT9950");
        final By switchLocator = By.xpath("//*[@text='Proximity sensor']/../../../android.widget.LinearLayout/android.widget.Switch");
        final AtomicBoolean afterProximitySensorPage = new AtomicBoolean(false);
        final AtomicBoolean originalProximitySensorEnabled = new AtomicBoolean(false);
        setTestCaseCleaner(() -> {
            if (afterProximitySensorPage.get()) {
                Driver.pressKey(AndroidKey.BACK);
            }
            if (AppiumHelper.isElementExistAndSwipePageByText("Advanced", 10)) {
                CommonOperator.scrollAndClick("com.android.settings:id/list", "Advanced");
            }
            final boolean proximitySensorEnabled = ElementHelper.isChecked(switchLocator);
            if (proximitySensorEnabled != originalProximitySensorEnabled.get()) {
                ElementHelper.click(switchLocator);
            }
        });
        TestRail.addStepName("Open Settings> Display> Proximity Sensor");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        if (AppiumHelper.isElementExistAndSwipePageByText("Advanced", 10)) {
            CommonOperator.scrollAndClick("com.android.settings:id/list", "Advanced");
        }
        // check Proximity sensor exist
        assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Proximity sensor", 10),
                "Proximity sensor is disappear");
        // check whether or not the proximity sensor is enabled.
        // If disabled, we need to update it.
        originalProximitySensorEnabled.set(ElementHelper.isChecked(switchLocator));
        if (!originalProximitySensorEnabled.get()) {
            ElementHelper.click(switchLocator);
        }
        // check Proximity sensor content exist
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Proximity sensor");
        afterProximitySensorPage.set(true);
        AssertKt.assertPresent(Locator.byText("Reset standby and sleep timer when motion is detected"));
        AssertKt.assertPresent(Locator.byText("Illuminate screen when motion is detected"));
        AssertKt.assertTrue(ElementHelper.isEnabled(Locator.byText("Reset standby and sleep timer when motion is detected")),
                "This 'Reset standby and sleep timer when motion is detected' is disabled");
        AssertKt.assertTrue(ElementHelper.isEnabled(Locator.byText("Illuminate screen when motion is detected")),
                "This 'Illuminate screen when motion is detected' is disabled");
    }
}
