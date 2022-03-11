package com.prometheanworld.AP9.Firmware;

import com.nd.automation.core.action.Direction;
import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class AndroidSettingsTest extends BaseTest {

    private static final String PROP_EDIT_NETWORKS = "persist.settings.can_edit_networks";
    private static final String PROP_FACTORY_RESET = "persist.settings.can_factory_reset";
    private static final String PROP_EDIT_APPS = "persist.settings.can_edit_apps";
    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";
    String maximizeXpath = "//android.widget.ImageButton[@content-desc='Maximize']";
    String closeXpath = "//android.widget.ImageButton[@content-desc='Close']";

    private static final By SETTINGS_DASHBOARD = Locator.byResourceId("id/dashboard_container");

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @Override
    protected String testAppName() {
        return "Settings";
    }

    /**
     * C92377: Setting function interface has 9 modules
     * On Menu bar click user profile -> Settings => The settings interface will have 9 options, which are Network & Internet, Connected Devices, Apps, Display, Sound, Storage, Security & Location, Accessibility, System
     */
    @Test(groups= "P1")
    public void C92377has9Modules() {
        TestRail.setTestRailId("92377");

        final String[] items = {
                "Network & internet",
                "Connected devices",
                "Apps",
                "Display",
                "Sound",
                "Storage",
                "Security & location",
                "Accessibility",
                "System"
        };
        for (String item : items) {
            TestRail.addStepName("Verify that settings function interface has the module '" + item + "'");
            Assert.assertTrue(AppiumHelper.isElementExistAndSwipePageByText(item, 10),
                    "The label \"" + item + "\" should be displayed in the settings dashboard");
        }
    }

    /**
     * C92377 Setting function interface has 9 modules
     * Steps
     * 1. User clicks Application
     * 2. User clicks Settings
     */
    @Test(groups= "P1")
    public void C92377SettingFunctionInterfaceHas9Modules() {
        TestRail.setTestRailId("92377");
        TestRail.addStepName("User clicks Application");
        TestRail.addStepName("User clicks Settings");
        systemPO.startAppFromUnifiedLauncher("Settings");
        ElementHelper.click(By.xpath(maximizeXpath));
        final String[] settingsModuleItems = {
                "Network & internet",
                "Connected devices",
                "Apps",
                "Display",
                "Sound",
                "Storage",
                "Security & location",
                "Accessibility",
                "System"
        };
        Rect screen = ScreenHelper.getScreenRect();
        for (String settingsModuleItem : settingsModuleItems) {
            TestRail.addStepName("Verify that settings function interface has the module '" + settingsModuleItem + "'");
            By modules = By.xpath("//android.widget.TextView[@text='" + settingsModuleItem + "']");
            boolean isElementVisible = ElementHelper.isVisible(modules, 1);
            System.out.println("Validating.............." + settingsModuleItem);
            int numberOfTry = 10;
            while (!isElementVisible && numberOfTry > 0) {
                if (!isElementVisible) {
                    ScreenHelper.swipe(screen.getPoint(.2, .9), screen.getPoint(.2, .8));
                    System.out.println("Validating Down.............." + settingsModuleItem + "Count = " + numberOfTry);
                    isElementVisible = ElementHelper.isVisible(Locator.byText(settingsModuleItem), 1);
                }
                numberOfTry--;
                Assert.assertTrue(isElementVisible, "Unable to locate" + settingsModuleItem);
            }
        }
    }

    /**
     * C112922 that Open Android Settings app
     * Steps:
     *  1. User presses hardware Menu button => Menu bar changes from hide to show
     *  2. Open Settings app from locker => Settings app will be opened and users can configure the panel
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C112922OpenAndroidSettingsApp() {
        TestRail.setTestRailId("112922");

        try {
            Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
            TestRail.addStepName("User presses hardware Menu button");
            AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            Assert.assertTrue(
                    ElementHelper.isVisible(Locator.byResourceId("id/menu_bar"), 2),
                    "Menu bar should be showing"
            );
            TestRail.addStepName("Open Settings app from locker");
            ElementHelper.click(By.xpath(systemPO.eleMenuApplications));
            systemPO.scrollAndClickApp("Settings");
            By path = Locator.byText("Network & internet");
            Assert.assertTrue(
                    ElementHelper.isVisible(path, 2),
                    "Settings app should be opened"
            );
            ElementHelper.click(path);
            Assert.assertTrue(
                    ElementHelper.isVisible(Locator.byText("Ethernet settings"), 5),
                    "The users should configure the panel"
            );
        } finally {
            AppiumHelper.hideTaskbar();
            // This is for hiding the locker if the locker is showing.
            ScreenHelper.clickAt(0.0, 0.0);
        }
    }

    /**
     * C120694 Verify that following settings are removed from Settings App
     * Steps:
     *  1. Open Settings App and verify that following settings are removed:
     *    Attached is a list of settings to be removed:
     *    Security Update
     *    Find My Device
     *    Volume Key Shortcut
     *    Large Mouse Pointer
     *    Animations
     *    Vibration
     *    Gesture
     *    Backup
     *    Multiple Users
     *    System Update
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C120694VerifyThatFollowingSettingsAreRemovedFromSettingsApp() {
        TestRail.setTestRailId("120694");

        final String[] settingsModules = {
                "Network & internet",
                "Connected devices",
                "Apps",
                "Display",
                "Sound",
                "Storage",
                "Security & location",
                "Accessibility",
                "System"
        };
        final String[] removedModules = {
                "Security Update",
                "Find My Device",
                "Volume Key Shortcut",
                "Large Mouse Pointer",
                "Animations",
                "Vibration",
                "Gesture",
                "Backup",
                "Multiple Users",
                "System Update"
        };
        TestRail.addStepName("Open Settings App");
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        systemPO.startAppFromUnifiedLauncher("Settings");
        for (String module : removedModules) {
            Assert.assertFalse(
                    CommonOperator.scrollAndFind("com.android.settings:id/dashboard_container", module),
                    "The module '" + module + "' should be removed"
            );
        }
        for (String module : settingsModules) {
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", module);
            for (String removedModule : removedModules) {
                Assert.assertFalse(
                        CommonOperator.scrollAndFind("com.android.settings:id/list", removedModule),
                        "The module '" + removedModule + "' should be removed"
                );
            }
            Driver.pressKey(AndroidKey.BACK);
        }
    }
}