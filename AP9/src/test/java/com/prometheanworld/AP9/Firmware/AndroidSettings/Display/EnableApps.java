package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class EnableApps extends BaseTest {

    private static final String SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS = "prome_lock_admin_access";
    private static final String PROME_ENABLE_APPS = "prome_enable_apps";
    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";
    private static final String ENABLE_APPS_TEXT = "Enable apps";
    private static final String[] APPS_LIST_WHEN_ENABLE_APPS_OFF = {
            "Update",
            "Settings",
            "Panel Management"
    };

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @Override
    protected String testAppName() {
        return "Settings";
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        super.afterMethod();
        try {
            AppiumHelper.exeAdbRoot();
            Adb.setGlobalSettings("prome_lock_admin_access", "0");
        } catch (Exception e) {}
    }

    /**
     * C122575 Verify that "Default apps" in settings is enabled when "Enable apps" button switch to 'on
     *
     * Click Settings->display->set 'Enable app' option as 'on' => 'Enable app' is turned on
     * Click APPS->Check 'Default apps' => 'Default apps' is enabled
     * Click 'Default app' and edit it => user can enter and edit 'Default apps' successfully
     *
     */
    @Test(groups= {"P1","UserDebug"})
    public void C122575VerifyDefaultAppsEnabledWhenEnableAppsOn() {
        TestRail.setTestRailId("122575");

        Adb.root();
        final String defaultEnableApps = Adb.getGlobalSettings(PROME_ENABLE_APPS);
        setTestCaseCleaner(() -> Adb.setGlobalSettings(PROME_ENABLE_APPS, defaultEnableApps));
        Log.info("defaultEnableApps=" + defaultEnableApps);
        TestRail.addStepName("Click Settings->display->set 'Enable app' option as 'on'");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Display");
        By enableAppsPath = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='" + ENABLE_APPS_TEXT + "']]//*[ends-with(@resource-id, 'id/switch_widget')]");
        ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(ENABLE_APPS_TEXT));
        if (!ElementHelper.isChecked(enableAppsPath)) {
            ElementHelper.click(Locator.byText(ENABLE_APPS_TEXT));
        }
        Assert.assertTrue(ElementHelper.isChecked(enableAppsPath), "'" + ENABLE_APPS_TEXT + "' should be on");
        Driver.pressKey(AndroidKey.BACK);
        TestRail.addStepName("Click APPS->Check 'Default apps'");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Apps");
        Assert.assertTrue(CommonOperator.scrollAndFind("com.android.settings:id/list", "Default apps"),
                "The label \"Default apps\" should be displayed in the dashboard");
        TestRail.addStepName("Click 'Default app' and edit it");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Default apps");
        Assert.assertTrue(AppiumHelper.isElementExistAndSwipePageByText("Browser app", 10),
                "The label \"Browser app\" should be displayed in the dashboard");
    }

    /**
     * C122571 Verify that "Default apps" in settings is disabled when "Enable apps" button switch to 'off'
     * "Default apps" in settings is disable when  "Enable apps"  button switch to 'off'
     * Click Settings->display->set 'Enable app' option as 'off' ---- 'Enable app' is turned off
     * Click APPS->Check 'Default apps' ---- 'Default apps' is disabled
     *
     */
    @Test(groups= {"P0"})
    public void C122571VerifyThatDefaultAppsInSettingsIsDisabledWhenEnableAppsButtonSwitchToOff() {
        TestRail.setTestRailId("122571");
        final String defaultEnableApps = Adb.getGlobalSettings(PROME_ENABLE_APPS);
        setTestCaseCleaner(() -> Adb.setGlobalSettings(PROME_ENABLE_APPS, defaultEnableApps));
        TestRail.addStepName("Click Settings->display->set 'Enable app' option as 'off'");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Display");
        By enableAppsPath = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='" + ENABLE_APPS_TEXT + "']]//*[ends-with(@resource-id, 'id/switch_widget')]");
        ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(ENABLE_APPS_TEXT));
        if (ElementHelper.isChecked(enableAppsPath)) {
            ElementHelper.click(Locator.byText(ENABLE_APPS_TEXT));
        }
        // verify Enable apps is off
        Assert.assertFalse(ElementHelper.isChecked(enableAppsPath), "'" + ENABLE_APPS_TEXT + "' should be off");
        // back to settings home page
        Driver.pressKey(AndroidKey.BACK);
        TestRail.addStepName("Click APPS->Check 'Default apps'");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Apps");
        // verify Default apps is Gone
        Assert.assertFalse(CommonOperator.scrollAndFind("com.android.settings:id/list", "Default apps"),
                "The label \"Default apps\" should be disappear in the dashboard");
    }

    /**
     * C122573 Verify that just Update, Settings, and Panel Management apps are visible when "Enable Apps" button switch to 'off'
     *
     * Click Settings -> display -> set 'Enable app' option as 'off' => 'Enable app' is turned off
     * Check all of the apps in locker => Just the following apps are visible: Update, Settings, and Panel Management
     * Press flame button to open menu bar => Confirm that no user profile icon on the menu bar
     *
     */
    @Test(groups= {"P1"})
    public void C122573VerifyOnlyUpdateSettingsPanelManagementWhenEnableAppsOff() {
        TestRail.setTestRailId("122573");

        final String defaultEnableApps = Adb.getGlobalSettings(PROME_ENABLE_APPS);
        setTestCaseCleaner(() -> {
            ScreenHelper.clickAtPoint(0, 0);
            Adb.setGlobalSettings(PROME_ENABLE_APPS, defaultEnableApps);
        });
        Set<String> appsSet = new HashSet<>(Arrays.asList(APPS_LIST_WHEN_ENABLE_APPS_OFF));
        TestRail.addStepName("Click Settings -> display -> set 'Enable app' option as 'off'");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Display");
        By enableAppsPath = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='" + ENABLE_APPS_TEXT + "']]//*[ends-with(@resource-id, 'id/switch_widget')]");
        ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(ENABLE_APPS_TEXT));
        if (ElementHelper.isChecked(enableAppsPath)) {
            ElementHelper.click(Locator.byText(ENABLE_APPS_TEXT));
        }
        // verify Enable apps is off
        Assert.assertFalse(ElementHelper.isChecked(enableAppsPath), "'" + ENABLE_APPS_TEXT + "' should be off");
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);

        TestRail.addStepName("Check all of the apps in locker");
        systemPO.startLocker();
        List<MobileElement> tabs = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab), 5);
        List<AndroidElement> apps = new ArrayList<>();
        for(MobileElement tabEle : tabs) {
            tabEle.click();
            apps.addAll(Driver.getAndroidDriver().findElements(Locator.byResourceId("id/app_txt")));
        }
        boolean onlyHaveApps = true;
        if (apps.size() == appsSet.size()) {
            for (AndroidElement app : apps) {
                if (!appsSet.remove(app.getText())) {
                    onlyHaveApps = false;
                    break;
                }
            }
        } else {
            onlyHaveApps = false;
        }
        Assert.assertTrue(onlyHaveApps, "Just the following apps are visible: Update, Settings, and Panel Management");

        TestRail.addStepName("Press flame button to open menu bar");
        systemPO.startMenuBar();
        Assert.assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/user_layout"), 5), "Confirm that no user profile icon on the menu bar");
    }

    /**
     * C122574 Verify that all apps are enabled and are visible in the apps locker when "Enable Apps" button switch to 'on
     *
     * 1. Click Settings->display->set 'Enable app' option as 'on' => 'Enable app' is turned on
     * 2. Check all of the apps in locker => all apps are enabled and are visible in the apps locker
     * 3. Press flame button to open menu bar => Confirm that the user profile icon displays properly on the menu bar
     *
     */
    @Test(groups= {"P1","UserDebug"})
    public void C122574VerifyThatAllAppsAreEnabledAndAreVisibleInTheAppsLockerWhenEnableAppsButtonSwitchToon() {
        TestRail.setTestRailId("122574");

        Adb.root();
        final String defaultEnableApps = Adb.getGlobalSettings(PROME_ENABLE_APPS);
        final String defaultAdminAccessLockStatus = Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
        setTestCaseCleaner(() -> {
            ScreenHelper.clickAt(0D, 0D);
            Adb.forceStop(testAppName());
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, defaultAdminAccessLockStatus);
            Adb.setGlobalSettings(PROME_ENABLE_APPS, defaultEnableApps);
        });
        // precondition
        if (!"0".equals(defaultAdminAccessLockStatus)) {
            Adb.setGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, "0");
            Assert.assertEquals(Adb.getGlobalSettings(SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS), "0",
                    "The admin access lock value should be equals with zero");
        }
        if (!"0".equals(defaultEnableApps)) {
            Adb.setGlobalSettings(PROME_ENABLE_APPS, "0");
            Assert.assertEquals(Adb.getGlobalSettings(PROME_ENABLE_APPS), "0",
                    "The enable apps value should be equals with zero");
        }
        // verify that only admin apps display in the locker
        final List<String> adminAppNames = Arrays.asList("Settings", "Update", "Panel Management");
        doVerificationInLocker(androidElement -> {
            final String itemAppName = androidElement.getText();
            Assert.assertTrue(adminAppNames.contains(itemAppName),
                    "The \"" + itemAppName + "\" is not a admin access app");
            return null;
        });

        // verify Enable apps is off
        final By enableAppsPath = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='"
                + ENABLE_APPS_TEXT + "']]//*[ends-with(@resource-id, 'id/switch_widget')]");
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Display");
        Assert.assertFalse(ElementHelper.isChecked(enableAppsPath), "'" + ENABLE_APPS_TEXT + "' should be off");

        TestRail.addStepName("Click Settings->display->set 'Enable app' option as 'on'");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Display");
        ElementHelper.scrollUntilPresent(By.id("com.android.settings:id/list"), Locator.byText(ENABLE_APPS_TEXT));
        ElementHelper.click(Locator.byText(ENABLE_APPS_TEXT));
        Assert.assertTrue(ElementHelper.isChecked(enableAppsPath), "'" + ENABLE_APPS_TEXT + "' should be on");

        TestRail.addStepName("Check all of the apps in locker");
        final AtomicBoolean hasOtherApps = new AtomicBoolean(false);
        doVerificationInLocker(androidElement -> {
            final String itemAppName = androidElement.getText();
            if (!adminAppNames.contains(itemAppName)) {
                hasOtherApps.set(true);
            }
            return null;
        });
        Assert.assertTrue(hasOtherApps.get(), "The locker should have apps that are not system apps");
        ScreenHelper.clickAt(0D, 0D);
        TestRail.addStepName("Press flame button to open menu bar");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        // when menu from off to on, menu would appear first
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/user_icon"));
    }

    private void doVerificationInLocker(@NotNull Function<AndroidElement, Void> verification) {
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        int lockerPageSize = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab)).size();
        final String itemLabelXpath = "//*[@resource-id=\"com.prometheanworld.locker:id/app_txt\"]";
        for (int currentPage = 1; currentPage <= lockerPageSize; currentPage++) {
            if (currentPage != 1) {
                systemPO.swipePageByTab(currentPage);
                AppiumHelper.waitForSeconds(1);
            }
            List<AndroidElement> items = Driver.getAndroidDriver().findElements(By.xpath(itemLabelXpath));
            for (AndroidElement item : items) {
                verification.apply(item);
            }
        }
    }
}
