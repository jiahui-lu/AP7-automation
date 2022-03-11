package com.prometheanworld.appium.Panel.SystemFunction.SettingApp.DisplaySetting;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.appium.DriverCache;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Huangjie on 2020/7/1.
 */
@Listeners({TestRailListener.class, TestStatusListener.class})
public class AppIconDisplaySetting extends BaseTest {
    //AndroidUserTest androidUserTest = new AndroidUserTest();

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        ScreenHelper.clickAt(Location.CENTER);
    }

    /**
     * Description:  Add "Unified Menu" on Android Settings  MOD-01
     * 1. Android Settings --> Display
     * 2. Confirm that there is a Setting called: "Unified Menu" and a switch
     * 3. "Unified Menu" is "On" by default
     */
    @Test
    public void UnifiedMenuInDisPlaySetting_Test() {
        TestRail.setTestRailId("83225");
        Log.info("-------------Test UnifiedMenuInDisPlaySetting Start---------------");
        enterDisplaySetting();
        AssertKt.assertPresent(Locator.byTextContains("Unified Menu"), 3);
        AndroidElement MenuSwitch = Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[contains(@text, '" + "Unified Menu" + "')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout/android.widget.Switch");
        assertEquals(MenuSwitch.getText(), "ON", "Unified Menu is not On by default");
        Log.info("click the Navigate up button, back to the home page");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToPreviousPage(panelModel, 1);
        Log.info("-------------Test UnifiedMenuInDisPlaySetting End---------------");
    }

    /**
     * Description:  Switch "Unified Menu" to on again after switch it to off  MOD-04
     * 1. Android Settings --> Display --> Unified Menu
     * 2. Switch to "Off"
     * 3. Confirm that the unified menu with the three carrots is not visible and doesn't display when touching the sides of the panel
     * 4. Switch back to "On"
     * 5. Confirm that the unified menu with the three carrots are visible and function
     */
    @Test
    public void UnifiedMenuSwitchOffAndOn_Test() {
        TestRail.setTestRailId("83228");
        Log.info("-------------Test UnifiedMenuInDisPlaySetting Start---------------");
        setSwitchState("Unified Menu", "OFF");
        checkMenuInSpecifySide("Left", false);
        setSwitchState("Unified Menu", "ON");
        checkMenuInSpecifySide("Left", true);
        Log.info("-------------Test UnifiedMenuInDisPlaySetting End---------------");
    }

    /**
     * Description:  Reboot panel after set "Unified Menu" to off  MOD-05
     * 1. Android Settings --> Display --> Unified Menu
     * 2. Switch to "Off"
     * 3. Confirm that the unified menu with the three carrots is not visible and doesn't display when touching the sides of the panel
     * 4. Reboot panel and confirm that the unified menu with the three carrots is not visible and doesn't display when touching the sides of the panel
     */
    @Test
    public void RebootPanelAfterSetUnifiedMenu0ff_Test() {
        TestRail.setTestRailId("83229");
        Log.info("-------------Test RebootPanelAfterSetUnifiedMenu0ff Start---------------");
        // Enter Display Settings page, And set Unified Menu to OFF
        setSwitchState("Unified Menu", "OFF");
        checkMenuInSpecifySide("Bottom", false);
        rebootPanelAndResetServices();
        checkMenuInSpecifySide("Right", false);
        // Enter Display Settings page, And set Unified Menu to ON
        setSwitchState("Unified Menu", "ON");
        checkMenuInSpecifySide("Bottom", true);
        Log.info("-------------Test RebootPanelAfterSetUnifiedMenu0ff End---------------");
    }

    /**
     * Writers：jiahui lu
     * Description:  Add "Home Source" on Android Settings  MOD-08
     * 1. Android Settings --> Display
     * 2. Confirm that there is a Setting called: "Home Source" and a switch
     * 3. "Home Source" is "On" by default
     */
    @Test
    public void HomeSource_In_DisPlay_Setting_Test() {
        TestRail.setTestRailId("83232");
        Log.info("-------------Test HomeSourceInDisPlaySetting Start---------------");
        enterDisplaySetting();
        AssertKt.assertPresent(Locator.byTextContains("Home Source"), 3);
        AndroidElement MenuSwitch = Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[contains(@text, '" + "Home Source" + "')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout/android.widget.Switch");
        assertEquals(MenuSwitch.getText(), "ON", "Home Source is not On by default");
        Log.info("click the Navigate up button, back to the home page");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToPreviousPage(panelModel, 1);
        Log.info("-------------Test HomeSourceInDisPlaySetting End---------------");
    }

    /**
     * Writers：jiahui lu
     * Description: When the "Home Source" switch is on
     * 1.Android Settings --> Display --> Home Source
     * 2.Switch to "On"
     * 3.Unified Menu --> Source
     * confirm that the input source selection shows a "Home" icon that represents the android home screen
     */
    @Test
    public void Home_Soures_switch_is_On_Test() {
        TestRail.setTestRailId("83233");
        enterDisplaySetting();
        AndroidElement Switch_status = Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[contains(@text, '" + "Home Source" + "')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout/android.widget.Switch");
        String status = Switch_status.getText();
        if (status == "OFF") {
            Log.info("The Home Source need Switch to ON status");
            ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Home Source" + "']"));
            checkHomeSourceStatus("Bottom", true);
        } else {
            checkHomeSourceStatus("Bottom", true);
        }
        ElementHelper.click(By.id("android:id/close_window"));
    }

    /**
     * Writers：jiahui lu
     * Description: When the "Home Source" switch is off
     * 1.Attached external sources or No external source is attached
     * 2.Android Settings --> Display --> Home Source
     * 2.Switch to "Off"
     * 3.Unified Menu --> Source
     * confirm that the input source selection does not show a "Home" icon and show "no connected devices"
     */
    @Test
    public void Turn_off_HomeSource_check_External_sources_Test() throws InterruptedException {
        TestRail.setTestRailId("83234,83235");
        enterDisplaySetting();
        AndroidElement Switch_status = Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[contains(@text, '" + "Home Source" + "')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout/android.widget.Switch");
        String status = Switch_status.getText();
        if (status.equals("ON")) {
            Log.info("The Home Source need Switch to OFF status");
            ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Home Source" + "']"));
        }
        ElementHelper.click(By.id("android:id/close_window"));
        AppiumHelper.showBottomMenu();
        ElementHelper.click(Locator.byContentDesc("Source"));
        if (ElementHelper.isPresent(Locator.byTextContains("HDMI2"), 3)) {
            Log.info("Attached external sources");
            AssertKt.assertNotPresent(Locator.byTextContains("Home"), 3);
            ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/icon_button"));
            AppiumHelper.waitForSeconds(10);
            AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/text_date"), 3);
            AppiumHelper.showBottomMenu();
            ElementHelper.click(Locator.byContentDesc("Home"));
        } else {
            Log.info("No external source is attached");
            AssertKt.assertPresent(Locator.byTextContains("No connected devices"), 3);
            AssertKt.assertNotPresent(Locator.byTextContains("Home"), 3);
        }
    }


    /**
     * Description:  Switch "Home Source" to on again after switch it to off  MOD-12
     * 1. Android Settings --> Display --> Home Source
     * 2. Switch to "Off"
     * 3. Unified Menu --> Source, confirm that the input source selection does not show a "Home" icon
     * 4. Switch back to "On"
     * 5. Unified Menu --> Source, confirm that the input source selection shows a "Home" icon that represents the android home screen
     */
    @Test
    public void HomeSource_to_on_again_after_switch_it_to_off_Test() {
        TestRail.setTestRailId("83236");
        Log.info("-------------Test HomeSourceSwitchOffAndOn Start---------------");
        setSwitchState("Home Source", "OFF");
        checkHomeSourceStatus("Left", false);
        setSwitchState("Home Source", "ON");
        checkHomeSourceStatus("Left", true);
        Log.info("-------------Test HomeSourceSwitchOffAndOn End---------------");
    }

    /**
     * Description:  Add "Enable Apps" on Android Settings  MOD-16
     * 1. Android Settings --> Display
     * 2. Confirm that there is a Setting called: "Enable Apps" and a switch
     */
    @Test
    public void EnableAppInDisPlaySetting_Test() {
        TestRail.setTestRailId("83240");
        Log.info("-------------Test EnableAppInDisPlaySetting Start---------------");
        Log.info("Enter display setting page with adb command");
        enterDisplaySetting();
        AppiumHelper.waitForSeconds(2);
        AssertKt.assertPresent(Locator.byTextContains("Enable Apps"), 3);
        AssertKt.assertPresent(Locator.byTextContains("Hide non-system apps in the locker"), 3);
        AndroidElement EnableAppsSwitch = Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[contains(@text, '" + "Enable Apps" + "')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout/android.widget.Switch");
        assertEquals(EnableAppsSwitch.getText(), "ON", "Enable Apps is not On by default");
        Log.info("click the Navigate up/back button, return to the home page");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToPreviousPage(panelModel, 1);
        Log.info("-------------Test EnableAppInDisPlaySetting End---------------");
    }

    /**
     * Description:  When the "Enable Apps" switch is on  MOD-17
     * 1. Android Settings --> Display --> Enable Apps
     * 2. Switch to "On"
     * 3. Unified Menu --> Locker --> Apps
     * 4. Confirm that all apps are enabled and are visible in the apps locker
     */
    @Test
    public void _EnableAppSwitchOnAndCheck_Test() {
        TestRail.setTestRailId("83241");
        Log.info("-------------Test EnableAppSwitchOnAndCheck Start---------------");
        setSwitchState("Enable Apps", "ON");
        Log.info("Confirm that all apps are enabled and are visible in the apps locker");
        checkAppsVisibleStatus(true);
        Log.info("-------------Test EnableAppSwitchOnAndCheck End---------------");
    }

    /**
     * Writers：jiahui lu
     * Description:  Switch "Enable Apps" to on again after switch it to off
     * 1.Android Settings --> Display --> Enable Apps
     * 2.Switch to "Off"
     * 3.Confirm that only Update, Settings, and Panel Management are enabled and visible in Locker
     * 4.Switch back to "On"
     * Confirm that all apps are enabled and are visible in the apps locker
     */
    @Test
    public void Enableapp_to_on_again_after_switch_it_to_off_Test() {
        TestRail.setTestRailId("83243");
        setSwitchState("Enable Apps", "OFF");
        checkAppsVisibleStatus(false);
        setSwitchState("Enable Apps", "ON");
        checkAppsVisibleStatus(true);
    }

    /**
     * Writers：jiahui lu
     * Description:  Turn off Home Source,verify that the Home icon on the Unified Menu is available
     * 1.Attached external sources,such as OPS and HDMI
     * 2.click Unified Menu ->locker->Settings ->Display,turn off "Home source"
     * 3.click Unified Menu ->Home
     * Confirm Home icon can be clicked normally,The screen Still displayed on the home page
     * 4.click Unified Menu ->Source,switch to OPS devices
     * 5.click Unified Menu ->Home
     * Confirm Home icon can be clicked normally,The screen switches to the home page
     */
    @Test
    public void Turn_Off_HomeSource_Click_Home_Menu_Test() throws InterruptedException {
        TestRail.setTestRailId("119887");
        setSwitchState("Home Source", "OFF");
        checkHomeSourceStatus("Bottom",false);
        AppiumHelper.showBottomMenu();
        ElementHelper.click(Locator.byContentDesc("Home"));
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/text_date"), 3);
        AppiumHelper.waitForSeconds(5);
        AppiumHelper.showBottomMenu();
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Source" + "']"));
        AppiumHelper.waitForSeconds(5);
        AssertKt.assertNotPresent(Locator.byTextContains("Home"), 3);
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/icon_button"));
        AppiumHelper.waitForSeconds(10);
        AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/text_date"), 3);
        AppiumHelper.showBottomMenu();
        ElementHelper.click(Locator.byContentDesc("Home"));
        AppiumHelper.waitForSeconds(5);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/text_date"), 3);
    }

    /**
     * Writers：jiahui lu
     * Description:  Disable the Enable App and run App Info under Apps
     * 1.click Unified Menu ->locker->Settings ->Display,turn off "Enable App"
     * 2.Click back to Settings page,click Apps->App info
     * Open the App Info page normally,prompt "No apps"
     */
    @Test
    public void Turn_Off_EnableApp_Operate_Settings_under_Apps_Test() {
        TestRail.setTestRailId("119889");
        setSwitchState("Enable Apps", "OFF");
        checkAppsVisibleStatus(false);
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        CommonOperator.scrollAndClick("com.android.settings:id/list", "Apps");
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "App info" + "']"));
        AssertKt.assertPresent(By.id("android:id/empty"), 3);
        ElementHelper.click(By.id("android:id/close_window"));
    }

    public static void setSwitchState(String switchName, String state) {

        //enter display setting page with adb command
        enterDisplaySetting();
        AppiumHelper.waitForSeconds(3);
        if (!panelModel.equals("AP7_U")) {
            // Max the window,so we can decrease the scroll operation
            ElementHelper.click(By.id("android:id/maximize_window"));
        }
        AndroidElement MenuSwitch = Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[contains(@text, '" + switchName + "')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout/android.widget.Switch");
        if (MenuSwitch.getText().equals(state)) {
            Log.info("The " + switchName + " Switch is already " + state + ",no need to change");
        } else {
            Log.info("Change the state of " + switchName + " to " + state);
            MenuSwitch.click();
            AppiumHelper.waitForSeconds(2);
            assertEquals(MenuSwitch.getText(), state, "The switch status does not set succesfully.");
        }
        if (!panelModel.equals("AP7_U")) {
            // close the pin setting page back to the desktop
            ElementHelper.click(By.id("android:id/close_window"));
        } else {
            ElementHelper.click(By.id("com.android.settings:id/back_button"));
        }
    }

    public static void enterDisplaySetting() {
        try {
            // Enter Display Settings page
            Log.info("Enter display setting page with adb command");
            AppiumHelper.execToString("adb shell am start com.android.settings/com.android.settings.DisplaySettings");
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppiumHelper.waitForSeconds(2);
    }

    public static void enterDisplayAdvancedPage(boolean checkMode, String pageName) {
        //Enter display setting page with adb command
        enterDisplaySetting();
        AppiumHelper.waitForSeconds(3);
        if (!panelModel.equals("AP7_U")) {
            // Max the window,so we can decrease the scroll operation
            ElementHelper.click(By.id("android:id/maximize_window"));
        }
        AppiumHelper.waitForSeconds(2);
        if (checkMode) {
            AssertKt.assertPresent(Locator.byTextContains("Source Detection"), 3);
            AssertKt.assertPresent(Locator.byTextContains("Power On Default Source"), 3);
        }
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Advanced" + "']"));
        AppiumHelper.waitForSeconds(2);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + pageName + "']"));
    }

    public static void setSwitchStateViaAPI(String switchName, boolean state) {
        try {
            AppiumHelper.execToString("adb shell setprop persist.settings." + switchName + " " + state);
            AppiumHelper.waitForSeconds(3);
            Log.info("Change the state of " + switchName + " to " + state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchElementException("Set " + switchName + " to " + state + " failed");
        }
    }

    // check Menu In specify Side
    public static void checkMenuInSpecifySide(String position, boolean state) {
        Log.info("Check the Menu status from " + position);
        openMenuFromSpecifySide(position);
        AppiumHelper.waitForSeconds(3);
        // assert icon(button and title) shows up in the unified menu
        if (state) {
            AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/lockerBtn"), 3);
        } else {
            AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/lockerBtn"), 3);
        }
        //click the lower-right corner point, use to hide bottom menu
        Log.info("click the lower-right corner point, use to hide bottom menu");
        AppiumHelper.waitForSeconds(2);
        ScreenHelper.clickAt(0.999, 0.999);
    }

    // check Home Source Status
    public static void checkHomeSourceStatus(String position, boolean state) {
        Log.info("Check the Home Source status from " + position);
        openMenuFromSpecifySide(position);
        AppiumHelper.waitForSeconds(3);
        ElementHelper.click(Locator.byContentDesc("Source"));
        AppiumHelper.waitForSeconds(2);
        // assert icon(button and title) shows up in the unified menu
        if (state) {
            AssertKt.assertPresent(Locator.byTextContains("Home"), 3);
            assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/icon_button").getText(), "HOME");
            AssertKt.assertNotPresent(Locator.byTextContains("No connected devices"), 3);
        } else {
//            AssertKt.assertPresent(Locator.byTextContains("No connected devices"), 3);
            AssertKt.assertNotPresent(Locator.byTextContains("Home"), 3);
        }
        //click the lower-right corner point, use to hide bottom menu
        Log.info("click the lower-right corner point, use to hide bottom menu");
        AppiumHelper.waitForSeconds(2);
        ScreenHelper.clickAt(0.999, 0.999);
    }

    //check Apps Visible Status
    public static void checkAppsVisibleStatus(boolean state) {
        AppiumHelper.startAppFromBottomMenu("Locker");
        AppiumHelper.waitForSeconds(2);
        if (state) {
            assertTrue(CommonOperator.ScrollAndFindApp("Chromium", 0), "The Chromium should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Settings", 0), "The Settings should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Files", 0), "The Files should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Promethean Store", 0), "The Promethean Store should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Screen Share", 0), "The Screen Share should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Panel Management", 0), "The Panel Management should be displayed'");
            //assertTrue(CommonOperator.ScrollAndFindApp("ActivCast",0), "The ActivCast should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Update", 0), "The Update should be displayed'");
            //assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/apps_grid_container").findElementsByClassName("android.widget.LinearLayout").size(),15);
        } else {
            assertTrue(CommonOperator.ScrollAndFindApp("Settings", 0), "The Settings should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Panel Management", 0), "The Panel Management should be displayed'");
            assertTrue(CommonOperator.ScrollAndFindApp("Update", 0), "The Update should be displayed'");
            //assertFalse(CommonOperator.ScrollAndFindApp("ActivCast",0), "The ActivCast should not be displayed'");
            assertFalse(CommonOperator.ScrollAndFindApp("Files", 0), "The Files should not be displayed'");
            //assertEquals(Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/apps_grid_container").findElementsByClassName("android.widget.LinearLayout").size(),3);
        }
        //click the lower-right corner point, use to hide menu
        Log.info("click the lower-right corner point, use to hide  menu");
        AppiumHelper.waitForSeconds(2);
        ScreenHelper.clickAt(0.999, 0.999);
    }

    public static void rebootPanelAndResetServices() {
        AppiumHelper.rebootPanel();
        DriverCache.getDriverCache().restartDriver();
        AppiumHelper.clickOwnerInUserPage();
    }

    public static void openMenuFromSpecifySide(String position) {
        try {
            if (position.equals("Left")) {
                AppiumHelper.showLeftMenu();
            } else if (position.equals("Bottom")) {
                AppiumHelper.showBottomMenu();
            } else if (position.equals("Right")) {
                AppiumHelper.showRightMenu();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



