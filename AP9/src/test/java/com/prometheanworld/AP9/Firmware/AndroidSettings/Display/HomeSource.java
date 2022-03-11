package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.kotlin.AssumeKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class HomeSource extends BaseTest {

    private static final String PROM_SETTINGS_HOME_SOURCE = "prom.settings.home_source";
    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";
    private static final String HOME_SOURCE_TEXT = "Home source";

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @Override
    protected String testAppName() {
        return "Settings";
    }

    /**
     * C115455 Set Home Source to on
     *
     * Android Settings -> Display -> Home Source, switch to On
     * Press the Source button => Confirm that the input source selection shows a "Home" icon that represents the android home screen
     *
     */
    @Test(groups= {"P1"})
    public void C115455VerifyHomeSourceIsOn() {
        TestRail.setTestRailId("115455");

        final String currentHomeSource = Adb.getSecureSettings(PROM_SETTINGS_HOME_SOURCE);
        Log.info("currentHomeSource=" + currentHomeSource);
        setTestCaseCleaner(() -> {
            ScreenHelper.clickAtPoint(0, 0);
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, currentHomeSource);
        });
        TestRail.addStepName("Android Settings -> Display -> Home Source, switch to On");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Display");
        final By homeSourcePath = By.xpath(getHomeSourceSwitchWidget());
        ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(HOME_SOURCE_TEXT));
        if (!ElementHelper.isChecked(homeSourcePath)) {
            ElementHelper.click(Locator.byText(HOME_SOURCE_TEXT));
        }
        Assert.assertTrue(ElementHelper.isChecked(homeSourcePath), "'" + HOME_SOURCE_TEXT + "' should be on");
        TestRail.addStepName("Press the Source button");
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.ActivPanel);
        Assert.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Sources", 5), "The window should display the dialog \"Sources\"");
        Assert.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Home", 2), "The input source selection should show a \"Home\" icon that represents the android home screen");
    }

    /**
     * C115456 Set Home Source to off when connecting external sources
     * Steps:
     * 1. Attached external sources
     * 2. Android Settings -> Display -> Home Source, switch to Off
     * 3. Press the Source button => Confirm that the Power On Default source does not show a "Home" icon and you can switch to  external sources properly
     */
    @Test(groups= {"P1"})
    public void C115456SetHomeSourceToOffWhenNoExternalSourceIsConnected() {
        TestRail.setTestRailId("115456");
        // first or all, we need to confirm that there is no external source attached.
        final String currentHomeSource = Adb.getSecureSettings(PROM_SETTINGS_HOME_SOURCE);
        final boolean originalHomeSourceEnabled = !"0".equalsIgnoreCase(currentHomeSource);
        setTestCaseCleaner(() -> {
            ScreenHelper.clickAtPoint(0, 0);
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, currentHomeSource);
        });
        TestRail.addStepName("Attached external sources");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        final List<MobileElement> sourceListItem = ElementHelper.findElements(Locator.byResourceId("id/source_list_item"));
        final int sourceItemMinimum = originalHomeSourceEnabled ? 2 : 1;
        AssumeKt.assumeTrue(sourceListItem.size() >= sourceItemMinimum,
                "There should be more than " + sourceItemMinimum + " " +
                        "external sources attached to the devices");
        TestRail.addStepName("Android Settings -> Display -> Home Source, switch to Off");
        systemPO.startAppFromUnifiedLauncher(testAppName(), true);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/dashboard_container"));
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        final By homeSourceSwitch = By.xpath(getHomeSourceSwitchWidget());
        ElementHelper.waitUntilPresent(homeSourceSwitch);
        if (ElementHelper.isChecked(homeSourceSwitch)) {
            ElementHelper.click(homeSourceSwitch);
        }
        AssertKt.assertFalse(ElementHelper.isChecked(homeSourceSwitch),
                "The home source option should not be true");

        TestRail.addStepName("Press the Source button");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        final List<MobileElement> curSourceList = ElementHelper.findElements(Locator.byResourceId("id/source_list_item"));
        AssertKt.assertTrue(curSourceList.size() > 0,
                "The source list size should be larger than zero when there are external sources");
        for (MobileElement element : curSourceList) {
            final MobileElement sourceName = element.findElement(Locator.byResourceId("id/source_item_name"));
            AssertKt.assertNotEquals(HOME_SOURCE_TEXT, sourceName.getText(),
                    "The Home source should not be displayed when the Home source option is disabled");
        }
    }

    /**
     * C115457 Set Home Source to off when no external source is connected
     * Steps:
     * 1. No external source is attached
     * 2. Android Settings -> Display -> Home Source, switch to "Off"
     * 3. Press the Source button => Confirm that the input source selection does not show a "Home" icon and show "No connected devices"
     */
    @Test(groups= {"P1"})
    public void C115457SetHomeSourceToOffWhenNoExternalSourceIsConnected() {
        TestRail.setTestRailId("115457");
        // first or all, we need to confirm that there is no external source attached.
        final String currentHomeSource = Adb.getSecureSettings(PROM_SETTINGS_HOME_SOURCE);
        final boolean originalHomeSourceEnabled = !"0".equalsIgnoreCase(currentHomeSource);
        setTestCaseCleaner(() -> {
            ScreenHelper.clickAtPoint(0, 0);
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, currentHomeSource);
        });
        TestRail.addStepName("No external source is attached");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        final List<MobileElement> sourceListItem = ElementHelper.findElements(Locator.byResourceId("id/source_list_item"));
        final int sourceItemLimitation = originalHomeSourceEnabled ? 1 : 0;
        AssumeKt.assumeTrue(sourceListItem.size() <= sourceItemLimitation,
                "There should not be more than " + sourceItemLimitation + " " +
                        "external sources attached to the devices");
        TestRail.addStepName("Android Settings -> Display -> Home Source, switch to \"Off\"");
        systemPO.startAppFromUnifiedLauncher(testAppName(), true);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/dashboard_container"));
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        final By homeSourceSwitch = By.xpath(getHomeSourceSwitchWidget());
        ElementHelper.waitUntilPresent(homeSourceSwitch);
        if (ElementHelper.isChecked(homeSourceSwitch)) {
            ElementHelper.click(homeSourceSwitch);
        }
        AssertKt.assertFalse(ElementHelper.isChecked(homeSourceSwitch),
                "The home source option should not be true");

        TestRail.addStepName("Press the Source button");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        final List<MobileElement> curSourceList = ElementHelper.findElements(Locator.byResourceId("id/source_list_item"));
        AssertKt.assertEquals(curSourceList.size(), 0,
                "The source list size should be equals with zero when there is no external source");
        AssertKt.assertPresent(Locator.byTextContains("No connected devices"));
    }

    /**
     * C115458 Switch Home Source to on again after switch it to off
     * Steps:
     *  1. Android Settings -> Display -> Home Source, switch to Off
     *  2. Press the Source button => Confirm that the input source selection does not show a "Home" icon
     *  3. Android Settings -> Display -> Home Source, switch back to On
     *  4. Press the Source button => Confirm that the input source selection shows a "Home" icon that represents the android home screen
     * Author: Yifeng Wu
     *
     */
    @Test(groups = {"P2","UserDebug"})
    public void C115458SwitchHomeSourceToOnAgainAfterSwitchItToOff() {
        TestRail.setTestRailId("115458");

        Adb.root();
        final String currentHomeSource = Adb.getSecureSettings(PROM_SETTINGS_HOME_SOURCE);
        try {
            TestRail.addStepName("Android Settings -> Display -> Home Source, switch to \"Off\"");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            final By homeSourceSwitch = By.xpath(getHomeSourceSwitchWidget());
            ElementHelper.waitUntilPresent(homeSourceSwitch);
            if (ElementHelper.isChecked(homeSourceSwitch)) {
                ElementHelper.click(homeSourceSwitch);
            }
            AssertKt.assertFalse(ElementHelper.isChecked(homeSourceSwitch),
                    "The switch 'Home source' should be off");
            TestRail.addStepName("Press the Source button");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.ActivPanel);
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("id/source_container_view")),
                    "Source app should be opened");
            List<MobileElement> sourceListItem = ElementHelper.findElements(Locator.byResourceId("id/source_item_name"));
            boolean containsHome = false;
            for (MobileElement ele : sourceListItem) {
                if ("Home".equals(ele.getText())) {
                    containsHome = true;
                    break;
                }
            }
            Assert.assertFalse(containsHome, "The source 'Home' should be hidden");
            ScreenHelper.clickAtPoint(0, 0);
            Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("id/source_container_view")),
                    "Source app should be closed");
            TestRail.addStepName("Android Settings -> Display -> Home Source, switch back to On");
            ElementHelper.click(Locator.byText(HOME_SOURCE_TEXT));
            AssertKt.assertTrue(ElementHelper.isChecked(homeSourceSwitch),
                    "The switch 'Home source' should be on");
            TestRail.addStepName("Press the Source button");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.ActivPanel);
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("id/source_container_view")),
                    "Source app should be opened");
            sourceListItem = ElementHelper.findElements(Locator.byResourceId("id/source_item_name"));
            containsHome = false;
            for (MobileElement ele : sourceListItem) {
                if ("Home".equals(ele.getText())) {
                    containsHome = true;
                    break;
                }
            }
            Assert.assertTrue(containsHome, "The source 'Home' should be displayed");
        } finally {
            // This is for closing Source
            ScreenHelper.clickAtPoint(0, 0);
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, currentHomeSource);
        }
    }

    /**
     * C115454 There is a "Home Source" option in Android Settings and is on by default
     * Steps:
     *  1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     *  2. Android Settings -> Display => Confirm that there is a Setting called: "Home Source" and is on by default
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C115454ThereISAHomeSourceOptionInAndroidSettingsAndIsOnByDefault() {
        TestRail.setTestRailId("115454");

        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        TestRail.addStepName("Android Settings -> Display");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        final By homeSourcePath = By.xpath(getHomeSourceSwitchWidget());
        ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(HOME_SOURCE_TEXT));
        Assert.assertTrue(ElementHelper.isChecked(homeSourcePath),
                "The switch 'Home source' should be on by default");
    }

    /**
     * C115461 Set "Home Source" switch via API
     * Steps:
     *  1. Use adb command: "adb shell settings put secure prom.settings.home_source 0" to set "Home Source" switch to off
     *  2. Android Settings -> Display => Verify the "Home Source" switch is off and the input source selection does not show a "Home" icon
     *  3. Use adb command: "adb shell settings put secure prom.settings.home_source 1" to set "Home Source" switch to on
     *  4. Android Settings -> Display => Verify the "Home Source" switch is on and the input source selection shows a "Home" icon
     * Author: Yifeng Wu
     *
     */
    @Test(groups = {"P2","UserDebug"})
    public void C115461SetHomeSourceSwitchViaAPI() {
        TestRail.setTestRailId("115461");

        Adb.root();
        final String currentHomeSource = Adb.getSecureSettings(PROM_SETTINGS_HOME_SOURCE);
        try {
            TestRail.addStepName("Use adb command: \"adb shell settings put secure prom.settings.home_source 0\" to set \"Home Source\" switch to off");
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, "0");
            TestRail.addStepName("Android Settings -> Display");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            final By homeSourcePath = By.xpath(getHomeSourceSwitchWidget());
            ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(HOME_SOURCE_TEXT));
            Assert.assertFalse(ElementHelper.isChecked(homeSourcePath),
                    "The switch 'Home source' should be off");
            TestRail.addStepName("Use adb command: \"adb shell settings put secure prom.settings.home_source 1\" to set \"Home Source\" switch to on");
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, "1");
            TestRail.addStepName("Android Settings -> Display");
            Assert.assertTrue(ElementHelper.isChecked(homeSourcePath),
                    "The switch 'Home source' should be on");
        } finally {
            Adb.setSecureSettings(PROM_SETTINGS_HOME_SOURCE, currentHomeSource);
        }
    }

    private String getHomeSourceSwitchWidget() {
        return "//*[ends-with(@resource-id, 'id/list')]" +
                "/*[descendant::*[@text='" + HomeSource.HOME_SOURCE_TEXT + "']]//*[ends-with(@resource-id, 'id/switch_widget')]";
    }
}