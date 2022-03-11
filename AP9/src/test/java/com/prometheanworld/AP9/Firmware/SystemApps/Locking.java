/*
 * © 2022 Promethean. All Rights Reserved.
 * Unauthorized copying of this file or any part of this file via any medium is strictly prohibited.
 */

package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyEvent;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.IdentityPO;
import com.prometheanworld.appium.frame.model.AP9.LockingPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class Locking extends BaseTest {

    private final LockingPO lockingPO = POFactory.getInstance(LockingPO.class);
    private final IdentityPO identityPO = POFactory.getInstance(IdentityPO.class);

    /**
     * C138563 Verify that the Sign out button is removed in guest mode
     * Steps:
     * 1. Press the flame button
     * 2. Click on the profile icon, then click on Lock Screen icon
     * 3. Set PIN to "1111"
     * Author: Ning Lu
     */
    @Test(groups = {"P1"})
    public void C138563VerifyThatTheSignOutButtonIsRemovedInGuestMode() {
        TestRail.setTestRailId("138563");
        setTestCaseCleaner(() -> {
            if (ElementHelper.isPresent(Locator.byText("Cancel"))) {
                ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
            }
        });
        // make sure the current user is the guest
        systemPO.resetInitialEnvironment();

        TestRail.addStepName("Press the flame button");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Guest")), "The panel should not be logged in");

        TestRail.addStepName("Click on the profile icon, then click on Lock Screen icon");
        ElementHelper.click(Locator.byText("Guest"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));

        TestRail.addStepName("Set PIN to \"1111\"");
        lockingPO.enterPINAndSubmit("1111");
        ElementHelper.waitUntilPresent(Locator.byText("Retype PIN to confirm"));
        lockingPO.enterPINAndSubmit("1111");
        ElementHelper.waitUntilPresent(Locator.byText("Unlock with your PIN"));
        AssertKt.assertFalse(ElementHelper.isVisible(Locator.byResourceId("id/sign_out_button")),
                "The sign out button should not be displayed when the panel in the guest mode");
        lockingPO.enterPINAndSubmit("1111");
        ElementHelper.waitUntilNotPresent(Locator.byText("Unlock with your PIN"));
    }


    /**
     * C138566 Verify that panel can support a PIN of 4 to 16 characters when sign in as guest
     * Steps:
     * 1. Press the flame button
     * 2. Click on the profile icon, then click on Lock Screen icon
     * 3. Enter "1" in the password field, click check icon
     * 4. Enter "11111111111111111" (Enter 17 "1") in the password field, click check icon
     * Author: Ning Lu
     */
    @Test(groups = {"P1"})
    public void C138566VerifyThatPanelCanSupportAPINOf4To16CharactersWhenSignInAsGuest() {
        TestRail.setTestRailId("138566");
        setTestCaseCleaner(() -> {
            if (ElementHelper.isPresent(Locator.byText("Cancel"))) {
                ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
            }
        });
        // make sure the current user is the guest
        systemPO.resetInitialEnvironment();

        TestRail.addStepName("Press the flame button");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Guest")), "The panel should not be logged in");

        TestRail.addStepName("Click on the profile icon, then click on Lock Screen icon");
        ElementHelper.click(Locator.byText("Guest"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));

        TestRail.addStepName("Enter \"1\" in the password field, click check icon");
        lockingPO.enterPINAndSubmit("1");
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Your PIN should be between 4 and 16 digits long"), 2L),
                "The error message should be displayed when the user input the illegal pin code"
        );
        TestRail.addStepName("Enter \"11111111111111111\" (Enter 17 \"1\") in the password field, click check icon");
        lockingPO.enterPIN("11111111111111111");
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Your PIN should be between 4 and 16 digits long"), 2L),
                "The error message should be displayed when the user input the illegal pin code"
        );
        ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
        ElementHelper.waitUntilNotPresent(Locator.byText("Create Screen Lock PIN"));
    }

    /**
     * C138658 Verify that only Power button is available when panel is locked
     * Steps:
     * 1. Press the flame button
     * 2. Click on the profile icon, then click on Lock Screen icon
     * 3. Quick press the power button on the panel
     * 4. Quick press the power button on the remote control
     * 5. Long press the power button on the panel
     * 6. Long press the power button on the remote control
     * Author: Ning Lu
     */
    @Test(groups = {"P1"})
    public void C138658VerifyThatOnlyPowerButtonIsAvailableWhenPanelIsLocked() {
        TestRail.setTestRailId("138658");
        setTestCaseCleaner(() -> {
            ScreenHelper.clickAt(0, 0);
            if (ElementHelper.isPresent(Locator.byText("Cancel"))) {
                ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
            }
        });
        // make sure the current user is the guest
        systemPO.resetInitialEnvironment();

        TestRail.addStepName("Press the flame button");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Guest")), "The panel should not be logged in");

        TestRail.addStepName("Click on the profile icon, then click on Lock Screen icon");
        ElementHelper.click(Locator.byText("Guest"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));

        TestRail.addStepName("Quick press the power button on the panel");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Power, PrometheanKeyboard.ActivPanel);
        TestRail.addStepName("Quick press the power button on the remote control");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Power, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Create Screen Lock PIN"), 5L),
                "The screen should be turn on"
        );

        TestRail.addStepName("Long press the power button on the panel");
        AppiumHelper.sendLongEvent(PrometheanKeyEvent.Power, PrometheanKeyboard.ActivPanel, 2L);
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Sleep or Restart"), 2L),
                "The shutdown title should be displayed"
        );
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Cancel"), 2L),
                "The cancel button should be displayed"
        );
        TestRail.addStepName("Click Cancel button, long press the power button on the remote control");
        ElementHelper.click(Locator.byTextContains("Cancel"));
        AppiumHelper.sendLongEvent(PrometheanKeyEvent.Power, PrometheanKeyboard.RemoteControl, 2L);
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Sleep or Restart"), 2L),
                "The shutdown title should be displayed"
        );
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Cancel"), 2L),
                "The cancel button should be displayed"
        );
        ElementHelper.clickWhenVisible(Locator.byTextContains("Cancel"));
        ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
        ElementHelper.waitUntilNotPresent(Locator.byText("Create Screen Lock PIN"));
    }

    /**
     * C176783 Verify that user cannot unlock panel from locked screen when entering the wrong PIN
     * Steps:
     * 1. Enter "111" then click check button
     * 2. Enter "11111111111111111" (Enter 17 "1"), click check icon
     * 3. Enter "11111" then click check button
     * 4. Enter "1111" then click check button
     * Author: Ning Lu
     */
    @Test(groups = {"P1"})
    public void C176783VerifyThatUserCannotUnlockPanelFromLockedScreenWhenEnteringTheWrongPIN() {
        TestRail.setTestRailId("176783");
        setTestCaseCleaner(() -> {
            if (ElementHelper.isPresent(Locator.byText("Cancel"))) {
                ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
            }
        });
        // make sure the current user is the guest
        systemPO.resetInitialEnvironment();
        // Pre-condition
        // set the pin to "1111" and lock the panel screen
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Guest")), "The panel should not be logged in");
        ElementHelper.click(Locator.byText("Guest"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));
        lockingPO.setPIN("1111");

        TestRail.addStepName("Enter \"111\" then click check button");
        lockingPO.enterPINAndSubmit("111");
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Your PIN should be between 4 and 16 digits long"), 2L),
                "The error message should be displayed when the user input the illegal pin code"
        );

        TestRail.addStepName("Enter \"11111111111111111\" (Enter 17 \"1\"), click check icon");
        lockingPO.enterPIN("11111111111111111");
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Your PIN should be between 4 and 16 digits long"), 2L),
                "The error message should be displayed when the user input the illegal pin code"
        );

        TestRail.addStepName("Enter \"11111\" then click check button");
        lockingPO.enterPINAndSubmit("11111");
        AssertKt.assertTrue(
                ElementHelper.isVisible(Locator.byTextContains("Incorrect PIN, please try again"), 2L),
                "The error message should be displayed when the user input the incorrect pin code"
        );

        TestRail.addStepName("Enter \"1111\" then click check button");
        lockingPO.enterPINAndSubmit("1111");
        ElementHelper.waitUntilNotPresent(Locator.byText("Unlock with your PIN"));
    }

    /**
     * C138657 Verify that other system UI does not appear when panel is locked
     * Steps:
     * 1. Press the flame button
     * 2. Click on the profile icon, then click on Lock Screen icon
     * 3. Press the flame, volume up/down, freeze, touch-lock and source buttons on the panel
     * 4. Press the flame, volume up/down, freeze, touch-lock, source and setting buttons on the remote control
     * Author: Ning Lu
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C138657VerifyThatOtherSystemUIDoesNotAppearWhenPanelIsLocked() {
        TestRail.setTestRailId("138657");
        setTestCaseCleaner(() -> {
            if (ElementHelper.isVisible(Locator.byResourceId("id/freeze"))) {
                AppiumHelper.sendEvent(PrometheanKeyEvent.Freeze, PrometheanKeyboard.ActivPanel);
            }
            if (ElementHelper.isVisible(Locator.byResourceId("id/touch_lock")) || systemPO.isTouchLocked()) {
                AppiumHelper.sendEvent(PrometheanKeyEvent.Touch, PrometheanKeyboard.ActivPanel);
            }
            if (ElementHelper.isVisible(Locator.byResourceId("id/volume_left")) ||
                    ElementHelper.isVisible(Locator.byResourceId("id/source_container_view")) ||
                    ElementHelper.isVisible(Locator.byText("Panel Settings"))) {
                ScreenHelper.clickAt(0.1, 0.1);
            }
            if (ElementHelper.isPresent(Locator.byText("Cancel"))) {
                ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
            }
        });
        // make sure the current user is the guest
        systemPO.resetInitialEnvironment();

        TestRail.addStepName("Press the flame button");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AppiumHelper.clickKey(PrometheanKey.RemoteLeft, PrometheanKeyboard.RemoteControl);
        AssertKt.assertTrue(ElementHelper.isPresent(Locator.byText("Guest")), "The panel should not be logged in");

        TestRail.addStepName("Click on the profile icon, then click on Lock Screen icon");
        ElementHelper.click(Locator.byText("Guest"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));

        TestRail.addStepName("Press the flame, volume up/down, freeze, touch-lock and source buttons on the panel");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.ActivPanel);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byText("Applications"), 2L),
                "The flame button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.VolumeDown, PrometheanKeyboard.ActivPanel);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/volume_left"), 2L),
                "The volume down button should not be responded to when the panel is locked"
        );
        ScreenHelper.clickAt(0.1, 0.1);
        AppiumHelper.sendEvent(PrometheanKeyEvent.VolumeUp, PrometheanKeyboard.ActivPanel);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/volume_right"), 2L),
                "The volume up button should not be responded to when the panel is locked"
        );
        ScreenHelper.clickAt(0.1, 0.1);
        AppiumHelper.sendEvent(PrometheanKeyEvent.Freeze, PrometheanKeyboard.ActivPanel);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/freeze"), 2L),
                "The freeze button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.Touch, PrometheanKeyboard.ActivPanel);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/touch_lock"), 2L),
                "The touch button should not be responded to when the panel is locked"
        );
        AssertKt.assertFalse(
                systemPO.isTouchLocked(),
                "The touch button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.ActivPanel);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/source_container_view"), 2L),
                "The source button should not be responded to when the panel is locked"
        );

        TestRail.addStepName("Press the flame, volume up/down, freeze, touch-lock, source and setting buttons on the remote control");
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byText("Applications"), 2L),
                "The flame button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.VolumeDown, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/volume_left"), 2L),
                "The volume down button should not be responded to when the panel is locked"
        );
        ScreenHelper.clickAt(0.1, 0.1);
        AppiumHelper.sendEvent(PrometheanKeyEvent.VolumeUp, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/volume_right"), 2L),
                "The volume up button should not be responded to when the panel is locked"
        );
        ScreenHelper.clickAt(0.1, 0.1);
        AppiumHelper.sendEvent(PrometheanKeyEvent.Freeze, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/freeze"), 2L),
                "The freeze button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.Touch, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/touch_lock"), 2L),
                "The touch button should not be responded to when the panel is locked"
        );
        AssertKt.assertFalse(
                systemPO.isTouchLocked(),
                "The touch button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byResourceId("id/source_container_view"), 2L),
                "The source button should not be responded to when the panel is locked"
        );
        AppiumHelper.sendEvent(PrometheanKeyEvent.Settings, PrometheanKeyboard.RemoteControl);
        AssertKt.assertFalse(
                ElementHelper.isVisible(Locator.byText("Panel Settings"), 2L),
                "The settings button should not be responded to when the panel is locked"
        );
        ElementHelper.clickWhenVisible(Locator.byText("Cancel"));
        ElementHelper.waitUntilNotPresent(Locator.byText("Create Screen Lock PIN"));
    }

    /**
     * C138608 Verify that user can create a pin to lock panel when sign in as user
     * 1. Press the flame button
     * 2. Click on the profile icon, then click on Lock Scrren icon
     * 3. Enter "1111" in the password field then press the "Lock" button
     * 4. Enter "1111" again then press the "Lock" button
     * Author:Sita
     */
    /*
    @Test(groups = {"P0", "UserDebug"})
    public void C138608UserCanCreatePinWhenSignInAsUser() {
        TestRail.setTestRailId("138608");
        systemPO.setPropMdmEnvByFile("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
        AppiumHelper.waitForSeconds(20);
        AppiumHelper.showTaskbar();
        TestRail.addStepName("Click on the profile icon, then click on Lock Scrren icon");
        ElementHelper.click(Locator.byResourceId("com.prometheanworld.unifiedlauncher:id/user_layout"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        //Lock screen displays
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));
        TestRail.addStepName("Enter \"1111\" in the password field then press the \"Lock\" button");
        lockingPO.enterPINAndSubmit("1111");
        //Display message: "Retype PIN to confirm"
        ElementHelper.waitUntilPresent(Locator.byText("Retype PIN to confirm"));
        TestRail.addStepName("Enter \"1111\" again then press the \"Lock\" button");
        lockingPO.enterPINAndSubmit("1111");
        ElementHelper.waitUntilPresent(Locator.byText("Unlock with your PIN"));
        //Unlocking the panel and sing out
        lockingPO.enterPINAndSubmit("1111");
        AppiumHelper.waitForSeconds(20);
        systemPO.signOut(false,false);
    }*/

    /**C138561 Verify that user can create a pin to lock panel when sign in as guest
     * 1. Press the flame button
     * 2. Click on the profile icon, then click on Lock Scrren icon
     * 3. Enter "1111" in the password field then press the "Lock" button
     * 4. Enter "1111" again then press the "Lock" button
     * Author:Sita */
    @Test(groups = "P0")
    public void C138561UserCanCreatePinToLockPanelWhenSignInAsGuest(){
        TestRail.setTestRailId("138561");
        TestRail.addStepName("Press the flame button");
        // make sure the current user is the guest
        systemPO.resetInitialEnvironment();
        AppiumHelper.showTaskbar();
        TestRail.addStepName("Click on the profile icon, then click on Lock Scrren icon");
        ElementHelper.clickWhenVisible(By.id("com.prometheanworld.unifiedlauncher:id/user_layout"));
        ElementHelper.clickWhenVisible(Locator.byText("Lock Screen"));
        //Lock screen displays
        ElementHelper.waitUntilPresent(Locator.byText("Create Screen Lock PIN"));
        TestRail.addStepName("Enter \"1111\" in the password field then press the \"Lock\" button");
        lockingPO.enterPINAndSubmit("1111");
        //Display message: "Retype PIN to confirm"
        ElementHelper.waitUntilPresent(Locator.byText("Retype PIN to confirm"));
        TestRail.addStepName("Enter \"1111\" again then press the \"Lock\" button");
        lockingPO.enterPINAndSubmit("1111");
        ElementHelper.waitUntilPresent(Locator.byText("Unlock with your PIN"));
        //Unlock the panel
        lockingPO.enterPINAndSubmit("1111");
    }
}