/*
 * © 2021 Promethean. All Rights Reserved.
 *
 * Unauthorized copying of this file or any part of this file
 * via any medium is strictly prohibited.
 */

package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class Menu extends BaseTest {

     private static final String SETTINGS_SHOW_MENU = "display_show_menu";

    @Override
    protected String testAppName() {
        return "Settings";
    }

    /**
     * C115037 Verify that Menu bar is available when "Menu" switch is on
     * Android Settings --> Display --> Menu --> turn on "Menu" switch ---- turn on "Menu" switch successfully
     * Press menu button (promethean flame) under panel screen to evoke/hide unified menu ---- evoke/hide unified menu successfully
     * Press menu button (promethean flame) on remote to evoke/hide unified menu ---- evoke/hide unified menu successfully
     *
     */
    @Test(groups = {"P1"})
    public void C115037VerifyThatMenuBarIsAvailableWhenMenuSwitchIsOn() {
        TestRail.setTestRailId("115037");
        final String originShowMenu = Adb.getGlobalSettings(SETTINGS_SHOW_MENU);
        setTestCaseCleaner(() -> Adb.setGlobalSettings(SETTINGS_SHOW_MENU, originShowMenu));

        // precondition
        Adb.setGlobalSettings(SETTINGS_SHOW_MENU, "0");

        TestRail.addStepName("Android Settings --> Display --> Menu --> turn on \"Menu\" switch");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");

        // check menu is off
        By menuSwitch = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='Menu']]//*[ends-with(@resource-id, 'id/switch_widget')]");
        assertFalse(ElementHelper.isChecked(menuSwitch), "Menu is enable");

        // click menu
        ElementHelper.click(Locator.byText("Menu"));

        // when menu from off to on, menu would appear first
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        // because the current focus is on menu_bar, not on 'Applications', so we should hide it by panel button
        AppiumHelper.hideTaskbarByPanel();
        // when menu appear, it would gain focus, we can not fetch the switch element, so we check it after menu hide
        assertTrue(ElementHelper.isChecked(menuSwitch), "Menu is disable");

        TestRail.addStepName("Press menu button (promethean flame) under panel screen to evoke/hide unified menu");
        AppiumHelper.showTaskbarByPanel();
        AppiumHelper.hideTaskbarByPanel();

        TestRail.addStepName("Press menu button (promethean flame) on remote to evoke/hide unified menu");
        AppiumHelper.showTaskbar();
        AppiumHelper.hideTaskbar();
    }

    /**
     * C115038 Verify that Menu bar is not available when "Menu" switch is off
     * Android Settings --> Display --> Menu --> turn off "Menu" switch ---- turn off "Menu" switch successfully, menu bar is hidden on the screen
     * Press menu button (promethean flame) under panel screen to evoke unified menu ---- can't open menu bar, open application locker directly
     * Press menu button (promethean flame) on remote to evoke unified menu ---- can't open menu bar, open application locker directly
     *
     */
    @Test(groups = {"P1"})
    public void C115038VerifyThatMenuBarIsNotAvailableWhenMenuSwitchIsOff() {
        TestRail.setTestRailId("115038");
        final String originShowMenu = Adb.getGlobalSettings(SETTINGS_SHOW_MENU);
        setTestCaseCleaner(() -> Adb.setGlobalSettings(SETTINGS_SHOW_MENU, originShowMenu));

        // precondition
        Adb.setGlobalSettings(SETTINGS_SHOW_MENU, "1");
        if (ElementHelper.isPresent(Locator.byResourceId("id/menu_bar"), 5)) {
            AppiumHelper.hideTaskbarByPanel();
        }

        TestRail.addStepName("Android Settings --> Display --> Menu --> turn off \"Menu\" switch");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");

        // check menu is on before click
        By menuSwitch = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='Menu']]//*[ends-with(@resource-id, 'id/switch_widget')]");
        assertTrue(ElementHelper.isChecked(menuSwitch), "Menu is enable");

        // click menu
        ElementHelper.click(Locator.byText("Menu"));
        // check menu not visible
        assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar"), 5), "Menu is visible");
        // check menu is off
        assertFalse(ElementHelper.isChecked(menuSwitch), "Menu is enable");

        TestRail.addStepName("Press menu button (promethean flame) under panel screen to evoke/hide unified menu");
        // open locker
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar")), "Menu is visible");

        // close locker
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar")), "Menu is visible");

        TestRail.addStepName("Press menu button (promethean flame) on remote to evoke unified menu");
        // open locker
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar")), "Menu is visible");

        // close locker
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("com.prometheanworld.locker:id/app_grid"), 5);
        assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar")), "Menu is visible");
    }

    /**
     * C115035 verify "Menu" setting
     * Steps:
     *  1. Android Settings --> Display --> Menu => 1. A Setting called "Menu"
     *                                             2. User can turn "Menu" switch to "On" or "Off"
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C115035VerifyMenuSetting() {
        TestRail.setTestRailId("115035");

        final String originShowMenu = Adb.getGlobalSettings(SETTINGS_SHOW_MENU);
        final String menuModule = "Menu";
        try {
            TestRail.addStepName("Android Settings --> Display --> Menu");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            Assert.assertTrue(
                    CommonOperator.scrollAndFind("com.android.settings:id/list", menuModule),
                    "The module '" + menuModule + "' should be displayed"
            );
            By menuSwitch = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='Menu']]//*[ends-with(@resource-id, 'id/switch_widget')]");
            if (!ElementHelper.isChecked(menuSwitch)) {
                CommonOperator.scrollAndClick("com.android.settings:id/list", menuModule);
            }
            assertTrue(ElementHelper.isChecked(menuSwitch), "The switch 'Menu' should be on");
            CommonOperator.scrollAndClick("com.android.settings:id/list", menuModule);
            assertFalse(ElementHelper.isChecked(menuSwitch), "The switch 'Menu' should be off");
        } finally {
            setGlobalSettings(SETTINGS_SHOW_MENU, originShowMenu);
        }
    }

    /**
     * C115463 Set Menu option via API
     * Steps:
     *  1. Use adb command: "adb shell settings put global display_show_menu 0" to set "Menu" switch to off
     *  2. Android Settings -> Display => Verify the "Menu" switch is off and the taskbar does not display at the bottom
     *  3. Use adb command: "adb shell settings put global display_show_menu 1" to set "Menu" switch to on
     *  4. Android Settings -> Display => Verify the "Menu" switch is on and the taskbar displays at the bottom
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C115463SetMenuOptionViaAPI() {
        TestRail.setTestRailId("115463");

        final String originShowMenu = Adb.getGlobalSettings(SETTINGS_SHOW_MENU);
        final String menuModule = "Menu";
        try {
            TestRail.addStepName("Use adb command: \"adb shell settings put global display_show_menu 0\" to set \"Menu\" switch to off");
            setGlobalSettings(SETTINGS_SHOW_MENU, "0");
            TestRail.addStepName("Android Settings -> Display");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            Assert.assertTrue(
                    CommonOperator.scrollAndFind("com.android.settings:id/list", menuModule),
                    "The module '" + menuModule + "' should be displayed"
            );
            By menuSwitch = By.xpath("//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='Menu']]//*[ends-with(@resource-id, 'id/switch_widget')]");
            assertFalse(ElementHelper.isChecked(menuSwitch), "The switch 'Menu' should be off");
            if (!ElementHelper.isPresent(Locator.byResourceId("id/menu_bar"))) {
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            }
            assertFalse(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar")), "The task bar should not be visible");
            TestRail.addStepName("Use adb command: \"adb shell settings put global display_show_menu 1\" to set \"Menu\" switch to on");
            setGlobalSettings(SETTINGS_SHOW_MENU, "1");
            Adb.forceStop("com.android.settings");
            TestRail.addStepName("Android Settings -> Display");
            systemPO.startAppFromUnifiedLauncher("Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            Assert.assertTrue(
                    CommonOperator.scrollAndFind("com.android.settings:id/list", menuModule),
                    "The module '" + menuModule + "' should be displayed"
            );
            assertTrue(ElementHelper.isChecked(menuSwitch), "The switch 'Menu' should be on");
            if (!ElementHelper.isPresent(Locator.byResourceId("id/menu_bar"))) {
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            }
            assertTrue(ElementHelper.isPresent(Locator.byResourceId("id/menu_bar")), "The task bar should be visible");
        } finally {
            setGlobalSettings(SETTINGS_SHOW_MENU, originShowMenu);
            systemPO.hideMenuBar();
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    private void setGlobalSettings(String key, String value) {
        if (value == null || "null".equals(value)) {
            Adb.deleteGlobalSettings(key);
        } else {
            Adb.setGlobalSettings(key, value);
        }
    }
}
