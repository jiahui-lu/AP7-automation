package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyEvent;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.OOBEPO;
import com.prometheanworld.appium.frame.model.AP9.SourceSwitchPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EULA extends BaseTest {
    private final OOBEPO oobePO = POFactory.getInstance(OOBEPO.class);

    private static final By SET_UP_ACTIVPANEL = Locator.byText("Set Up ActivPanel");
    private static final By CONTINUE = Locator.byText("Continue");
    private static final By SKIP = Locator.byText("Skip");
    private static final By SKIP_SET_UP = Locator.byText("Skip Set Up");
    private static final By BACK = Locator.byText("Back");
    private static final By ACCEPT = Locator.byText("Accept");
    private static final By FOCUSED = By.xpath("//*[@focused='true']");
    // 0: 0ff 1: standby 2: on, need root permission
    private static final String POWER_STATUS = "sys.xbh.powerstatus";

    private static final String EULA_PACKAGE_NAME = "com.prometheanworld.eula";

    /**
     * C120971 Verify that users can accept EULA when using the panel for the first time
     * Steps:
     * 1. Bin flash or factory reset panel
     * 2. Click Set Up ActivPanel button to start the OOBE process
     * 3. Click Skip button on the Update ActivPanel page
     * 4. Click Accept button
     */
    @Test(groups = {"P0", "UserDebug"})
    public void C120971VerifyThatUsersCanAcceptEULAWhenUsingThePanelForTheFirstTime() {
        TestRail.setTestRailId("120971");
        Assert.assertNotNull(oobePO);
        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Bin flash or factory reset panel");
        oobePO.startOOBE();
        AppiumHelper.rebootPanel(false);
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SET_UP_ACTIVPANEL);
        TestRail.addStepName("Click Set Up ActivPanel button to start the OOBE process");
        ElementHelper.clickWhenVisible(SET_UP_ACTIVPANEL);
        // show Select Language page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Language"));
        ElementHelper.waitUntilPresent(CONTINUE);
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Select Timezone page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Timezone"));
        ElementHelper.waitUntilPresent(CONTINUE);
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Update ActivPanel page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Update ActivPanel"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(SKIP);
        TestRail.addStepName("Click Skip button on the Update ActivPanel page");
        ElementHelper.clickWhenVisible(SKIP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(ACCEPT);
        TestRail.addStepName("Click the Accept button");
        ElementHelper.clickWhenVisible(ACCEPT);
        // verify the welcome page
        if (!ElementHelper.isPresent(Locator.byResourceId("id/hello"), 5)) {
            ScreenHelper.clickAt(0.5, 0.5);
        }
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/hello"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/welcome"));
        // verify the menu
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        Assert.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName());
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }

    /**
     * C122083 Verify that panel will back to the EULA page after reboot
     * Steps:
     * 1. Bin flash or factory reset panel
     * 2. Click Skip button to skip the OOBE process
     * 3. Long press the power button
     * 4. Press the power button
     * Author: Ning Lu
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C122083VerifyThatPanelWillBackToTheEULAPageAfterReboot() {
        TestRail.setTestRailId("122083");
        Assert.assertNotNull(oobePO);
        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Bin flash or factory reset panel");
        oobePO.startOOBE();
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SKIP_SET_UP);
        TestRail.addStepName("Click Skip button to skip the OOBE process");
        ElementHelper.clickWhenVisible(SKIP_SET_UP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(ACCEPT);
        TestRail.addStepName("Long press the power button");
        AppiumHelper.rebootPanel(false);
        TestRail.addStepName("Press the power button");
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(ACCEPT);
        AssertKt.assertFalse(ElementHelper.isPresent(BACK), "The back button should not be displayed when the panel reboot");
        ElementHelper.clickWhenVisible(ACCEPT);
        // verify the welcome page
        if (!ElementHelper.isPresent(Locator.byResourceId("id/hello"), 5)) {
            ScreenHelper.clickAt(0.5, 0.5);
        }
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/hello"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/welcome"));
        // verify the menu
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        Assert.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName());
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }

    /**
     * C123255 Verify that EULA app cannot be closed until accepted by the user
     * Steps:
     * 1. Adb connect to panel
     * 2. Reset the EULA app by using these commands:
     * adb shell pm clear com.prometheanworld.eula
     * adb shell settings put secure com.prometheanworld.eula true
     * 3. Reboot panel
     * 4. Force stop EULA app by using this command: adb shell am force-stop com.prometheanworld.eula
     * 5. Click Accept button
     * Author: Ning Lu
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C123255VerifyThatEULAAppCannotBeClosedUntilAcceptedByTheUser() {
        TestRail.setTestRailId("123255");
        Assert.assertNotNull(oobePO);
        setTestCaseCleaner(oobePO::restoreAfterOOBE);

        TestRail.addStepName("Adb connect to panel");
        TestRail.addStepName(
                "Reset the EULA app by using these commands:\n" +
                        "adb shell pm clear com.prometheanworld.eula\n" +
                        "adb shell settings put secure com.prometheanworld.eula true"
        );
        oobePO.startOOBE();

        TestRail.addStepName("Reboot panel");
        AppiumHelper.rebootPanel(false);
        // show the first step of the OOBE page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SKIP_SET_UP);
        ElementHelper.clickWhenVisible(SKIP_SET_UP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(ACCEPT);

        TestRail.addStepName("Force stop EULA app by using this command: adb shell am force-stop com.prometheanworld.eula");
        Adb.forceStop(EULA_PACKAGE_NAME);
        // show the first step of the OOBE page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SKIP_SET_UP);
        ElementHelper.clickWhenVisible(SKIP_SET_UP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(ACCEPT);

        TestRail.addStepName("Click Accept button");
        ElementHelper.click(ACCEPT);
        // verify the welcome page
        if (!ElementHelper.isPresent(Locator.byResourceId("id/hello"), 5)) {
            ScreenHelper.clickAt(0.5, 0.5);
        }
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/hello"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/welcome"));
        // verify the menu
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        Assert.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName());
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }

    /**
     * C129132 Verify that Back button only available on pages with the back icon before users accept the EULA
     * Steps:
     * 1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     * 2. Press the remote back button on the Welcome page
     * 3. Press the remote back button on the Select Language page (there is no Back icon)
     * 4. Press the remote back button on the Select Timezone page and Network Connection page (there is a Back icon)
     * 5. Try to online update panel on Update ActivPanel page, then press the remote back button
     * 6. Press the remote back button on the EULA page (there is a Back icon)
     * Author: Ning Lu
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C129132VerifyThatBackButtonOnlyAvailableOnPagesWithTheBackIconBeforeUsersAcceptTheEULA() {
        TestRail.setTestRailId("129132");
        Assert.assertNotNull(oobePO);
        setTestCaseCleaner(oobePO::restoreAfterOOBE);

        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        oobePO.startOOBE();
        AppiumHelper.rebootPanel(false);
        // show the first step of the OOBE page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SET_UP_ACTIVPANEL);

        TestRail.addStepName("Press the remote back button on the Welcome page");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Back, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Welcome to Promethean")),
                "The back button does not work on the welcome page");
        AssertKt.assertTrue(ElementHelper.isPresent(SET_UP_ACTIVPANEL),
                "The back button does not work on the welcome page");
        ElementHelper.click(SET_UP_ACTIVPANEL);
        // show Select Language page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Language"));
        ElementHelper.waitUntilPresent(CONTINUE);

        TestRail.addStepName("Press the remote back button on the Select Language page (there is no Back icon)");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Back, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Select Language")),
                "The back button does not work on the select language page");
        AssertKt.assertTrue(ElementHelper.isPresent(CONTINUE),
                "The back button does not work on the select language page");
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Select Timezone page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Timezone"));
        ElementHelper.waitUntilPresent(CONTINUE);
        ElementHelper.waitUntilPresent(BACK);

        TestRail.addStepName("Press the remote back button on the Select Timezone page and Network Connection page (there is a Back icon)");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Back, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Select Language"), 5L),
                "The back button should be work on the select timezone page");
        AssertKt.assertTrue(ElementHelper.isPresent(CONTINUE),
                "The back button should be work on the select timezone page");
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Select Timezone page again
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Timezone"));
        ElementHelper.waitUntilPresent(CONTINUE);
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Update ActivPanel page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Update ActivPanel"));
        ElementHelper.waitUntilPresent(Locator.byTextContains("Online Update Now"));
        ElementHelper.waitUntilVisible(SKIP);

        TestRail.addStepName("Try to online update panel on Update ActivPanel page, then press the remote back button");
        final boolean isOtaUpdateEnabled = AssertKt.noExceptions(() -> {
            ElementHelper.waitUntilEnabled(Locator.byTextContains("Online Update Now"), 5L);
            return null;
        });
        if (isOtaUpdateEnabled) {
            ElementHelper.clickWhenVisible(Locator.byTextContains("Online Update Now"));
            ElementHelper.waitUntilPresent(Locator.byTextContains("Install Update"));
            ElementHelper.waitUntilPresent(Locator.byTextContains("Cancel"));
            AppiumHelper.sendEvent(PrometheanKeyEvent.Back, PrometheanKeyboard.RemoteControl);
            AssertKt.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Install Update")),
                    "The back button does not work on the online update page");
            AssertKt.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Cancel")),
                    "The back button does not work on the online update page");
            ElementHelper.clickWhenVisible(Locator.byTextContains("Cancel"));
            ElementHelper.waitUntilPresent(Locator.byTextContains("Update ActivPanel"));
        } else {
            AssertKt.assertTrue(
                    ElementHelper.isVisible(Locator.byTextContains("The latest updates have already been installed")),
                    "The OTA update button is disabled but the panel is not the latest updates."
            );
        }
        ElementHelper.clickWhenVisible(SKIP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(ACCEPT);

        TestRail.addStepName("Press the remote back button on the EULA page (there is a Back icon)");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Back, PrometheanKeyboard.RemoteControl);
        // show Update ActivPanel page again
        ElementHelper.waitUntilPresent(Locator.byTextContains("Update ActivPanel"));
        ElementHelper.clickWhenVisible(SKIP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.clickWhenVisible(ACCEPT);
        // verify the welcome page
        if (!ElementHelper.isPresent(Locator.byResourceId("id/hello"), 5)) {
            ScreenHelper.clickAt(0.5, 0.5);
        }
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/hello"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/welcome"));
        // verify the menu
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        Assert.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName());
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }


    /**
     * C122075 Verify that users can back to OOBE on the EULA page
     * Steps:
     * 1. Bin flash or factory reset panel
     * 2. Click Skip Set Up button to skip the OOBE process
     * 3. Click Back button
     * 4. Click Set Up ActivPanel button to restart the OOBE process
     * 5. Click Skip button on the Update ActivPanel page
     * 6. Click Back button
     * Author: Ning Lu
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C122075VerifyThatUsersCanBackToOOBEOnTheEULAPage() {
        TestRail.setTestRailId("122075");
        Assert.assertNotNull(oobePO);
        setTestCaseCleaner(oobePO::restoreAfterOOBE);

        TestRail.addStepName("Bin flash or factory reset panel");
        oobePO.startOOBE();
        AppiumHelper.rebootPanel(false);
        // show the first step of the OOBE page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));

        TestRail.addStepName("Click Skip Set Up button to skip the OOBE process");
        ElementHelper.clickWhenVisible(SKIP_SET_UP);
        // show EULA page
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byTextContains("End User License Agreement"), 5L),
                "The EULA page should be displayed after clicking on the skip set up button");

        TestRail.addStepName("Click Back button");
        ElementHelper.clickWhenVisible(BACK);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Welcome to Promethean"), 5L),
                "The first step of the OOBE page should be displayed after clicking the back button on the EULA page");

        TestRail.addStepName("Click Set Up ActivPanel button to restart the OOBE process");
        ElementHelper.clickWhenVisible(SET_UP_ACTIVPANEL);
        // show Select Language page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Language"));
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Select Timezone page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Timezone"));
        ElementHelper.clickWhenVisible(CONTINUE);
        // show Update ActivPanel page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Update ActivPanel"));

        TestRail.addStepName("Click Skip button on the Update ActivPanel page");
        ElementHelper.clickWhenVisible(SKIP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(BACK);

        TestRail.addStepName("Click Back button");
        ElementHelper.clickWhenVisible(BACK);
        // show Update ActivPanel page again
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Update ActivPanel"), 5L),
                "The Select timezone page should be displayed after clicking the back button on the EULA page");
        ElementHelper.clickWhenVisible(SKIP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.click(ACCEPT);
        // verify the welcome page
        if (!ElementHelper.isPresent(Locator.byResourceId("id/hello"), 5)) {
            ScreenHelper.clickAt(0.5, 0.5);
        }
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/hello"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/welcome"));
        // verify the menu
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        Assert.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName());
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }

    /**
     * C122082 Only Power, Directional Pad buttons are available before users accept the EULA
     * Steps:
     * 1. Bin flash or factory reset panel
     * 2. Click Skip button to skip the OOBE process
     * 3. Press the power button on the panel
     * 4. Press the power button on the remote control
     * 5. Press the Directional Pad on the remote control
     * Author: Rock Shi
     */
    @Test(groups = {"P0", "UserDebug"})
    public void C122082OnlyPowerDirectionalPadButtonsAreAvailableBeforeUsersAcceptTheEULA() {
        TestRail.setTestRailId("122082");
        Assert.assertNotNull(oobePO);
        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Bin flash or factory reset panel");
        oobePO.startOOBE();
        AppiumHelper.exeAdbRoot();
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SKIP_SET_UP);
        TestRail.addStepName("Click Skip button to skip the OOBE process");
        ElementHelper.clickWhenVisible(SKIP_SET_UP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(ACCEPT);

        // Press the power button on the panel
        TestRail.addStepName("Press the power button on the panel");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Power, PrometheanKeyboard.ActivPanel);
        AppiumHelper.waitForSeconds(1);
        Assert.assertEquals(Adb.getAndroidProp(POWER_STATUS), "1");

        // Press the power button on the remote control
        TestRail.addStepName("Press the power button on the remote control");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Power, PrometheanKeyboard.RemoteControl);
        AppiumHelper.waitForSeconds(1);
        Assert.assertEquals(Adb.getAndroidProp(POWER_STATUS), "2");

        // Press the Directional Pad on the remote control
        TestRail.addStepName("Press the Directional Pad on the remote control");
        // check focus mode
        if (!ElementHelper.isPresent(FOCUSED)) {
            // enter focus mode
            AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteCenter, PrometheanKeyboard.RemoteControl);
        }

        Rect focusedRect = ElementHelper.getRect(FOCUSED);
        Rect backRect = ElementHelper.getRect(BACK);
        if (focusedRect.contains(backRect)) {
            // ensure focus level same with BACK Button
            AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteRight, PrometheanKeyboard.RemoteControl);
        }

        // focus on back button
        AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteRight, PrometheanKeyboard.RemoteControl);
        AppiumHelper.waitForSeconds(1);

        String focusedText;
        String preFocusedText = ElementHelper.getText(FOCUSED);
        // Press Right
        AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteRight, PrometheanKeyboard.RemoteControl);
        AppiumHelper.waitForSeconds(1);
        focusedText = ElementHelper.getText(FOCUSED);
        Assert.assertNotEquals(focusedText, preFocusedText);

        // Press Left
        AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.waitForSeconds(1);
        focusedText = ElementHelper.getText(FOCUSED);
        Assert.assertNotEquals(focusedText, preFocusedText);

        // focus text position related the license content of Eula, When it change, maybe need change the assert
        // Press UP
        preFocusedText = ElementHelper.getText(FOCUSED);
        AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteUp, PrometheanKeyboard.RemoteControl);
        AppiumHelper.waitForSeconds(1);
        focusedText = ElementHelper.getText(FOCUSED);
        Assert.assertNotEquals(focusedText, preFocusedText);

        // Press DOWN
        preFocusedText = focusedText;
        AppiumHelper.sendEvent(PrometheanKeyEvent.RemoteDown, PrometheanKeyboard.RemoteControl);
        AppiumHelper.waitForSeconds(1);
        focusedText = ElementHelper.getText(FOCUSED);
        Assert.assertNotEquals(focusedText, preFocusedText);
    }
}
