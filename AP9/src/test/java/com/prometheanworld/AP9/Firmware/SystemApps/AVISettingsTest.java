package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AVISettingsTest extends BaseTest {

    /**C90108 Verify that users can launch AVSettings app from AVI settings button on volume slider
    Steps
    1.Press the Volume up/down button on the panel
    2.Click the AVI settings button on the right of volume slider
    Author:Sita
     */
    @Test(groups= "P0")
    public void C90108UsersCanLaunchAVSettingsApp() {
        TestRail.setTestRailId("90108");
        TestRail.addStepName("Press the Volume up/down button on the panel");
        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.ActivPanel);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_bar"), 10));
        TestRail.addStepName("Click the AVI settings button on the right of volume slider");
        AppiumHelper.findElementBy(By.id("com.prometheanworld.avisettings:id/launch_avi")).click();
        //Verifying AVI Settings window opens to Audio tab above all others windows
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_label"), 10));
    }

    /**C90109 Verify that user can launch AVI Settings app from remote
        Steps
        1.Press the Settings button on the remote
        2.Press the Settings button on the remote again
        3.Presses the Volume up button on the remote
        4.Click the AVI settings button on the right of volume slider
        5.Presses the Volume down buttons on the remote
        6.Click the AVI settings button on the right of volume slider
        Author:Sita
         */
    @Test(groups= "P0")
    public void C90109UserCanLaunchAVISettingsAppFromRemote() {
        TestRail.setTestRailId("90109");
        TestRail.addStepName("Press the Settings button on the remote");
        AppiumHelper.showTaskbar();
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/backlight_label"), 10));//Verifying that AVI Settings app opens to Visuals tab above all others windows by checking the visibility of visual tab's
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/select_audio_settings"), 10));//Verifying that the other two tabs are Audio and Interaction
        Assert.assertTrue(AppiumHelper.isElementVisible("//android.widget.ImageView[@content-desc=\"Interaction\"]", null, 10));

        TestRail.addStepName("Press the Settings button on the remote again");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));

        TestRail.addStepName("Presses the Volume up button on the remote");
        AppiumHelper.clickKey(PrometheanKey.VolumeUp, PrometheanKeyboard.RemoteControl);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_bar"), 10));

        TestRail.addStepName("Click the AVI settings button on the right of volume slider");
        AppiumHelper.findElementBy(By.id("com.prometheanworld.avisettings:id/launch_avi")).click();
        //Verifying AVI Settings window opens to Audio tab above all others windows
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_label"), 10));

        TestRail.addStepName("Presses the Volume down buttons on the remote");
        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.RemoteControl);
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_bar"), 10));

        TestRail.addStepName("Click the AVI settings button on the right of volume slider");
        AppiumHelper.findElementBy(By.id("com.prometheanworld.avisettings:id/launch_avi")).click();
        //Verifying AVI Settings window opens to Audio tab above all others windows
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_label"), 10));
    }

    /**C90110 Verify that AVI Settings app lists are displayed correctly
        Steps
        1.Open the AVI Seetings app -> Visual tab
        2.Switch to Audio tab
        3.Switch to Interaction tab
        Author:Sita
         */
    @Test(groups= "P1")
    public void C90110AVISettingsAppListsAreDisplayedCorrectly() {
        TestRail.setTestRailId("90110");
        TestRail.addStepName("Open the AVI Seetings app -> Visual tab");
        AppiumHelper.showTaskbar();
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        //Verifying the Visual tab page opens and there are the following sliders:
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/energy_star"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/backlight_auto"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/backlight_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/brightness_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/contrast_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/hue_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/saturation_label"), 10));
        AppiumHelper.swipeByElements(By.id("com.prometheanworld.avisettings:id/saturation_label"), By.id("com.prometheanworld.avisettings:id/brightness_label"));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/sharpness_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/reduce_bluelight_label"), 10));
        AppiumHelper.swipeByElements(By.id("com.prometheanworld.avisettings:id/hue_label"), By.id("com.prometheanworld.avisettings:id/reduce_bluelight_label"));

        TestRail.addStepName("Switch to Audio tab");
        final By audio = Locator.byText("Audio"); //Verifying the Audio tab page opens and there are the following sliders present:
        ElementHelper.click(audio);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/volume_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/balance_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/treble_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/mid_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/bass_label"), 10));
        AppiumHelper.swipeByElements(By.id("com.prometheanworld.avisettings:id/bass_label"), By.id("com.prometheanworld.avisettings:id/treble_label"));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/subwoofer_label"), 10));

        TestRail.addStepName("Switch to Interaction tab");
        final By interaction = Locator.byText("Interaction"); //Verifying that the Interaction tab page opens and there are following moods present:
        ElementHelper.click(interaction);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/palm_mode"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/palm_mode_reject"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/palm_mode_eraser"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/thick_end"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/thick_end_pen"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/thick_end_eraser"), 10));
    }

    /**C115128 Verify that AVSettings will automatically close after 30 seconds of no activity
    Steps
    1.Press the remote settings button
    2.Wait for 10 seconds and click on the AV Settings window
    3.Wait for 30 seconds of no activity
    4.Presses the hardware Volume up buttons on the panel
    5.Click the gear icon in the right of volume changer UI
    6.Wait for 30 seconds of no activity
    Author:Sita
     */
    @Test(groups= "P1")
    public void C115128AVSettingsAutomaticallyCloseAfter30Seconds() {
        TestRail.setTestRailId("115128");
        TestRail.addStepName("Press the remote settings button");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        //Verifying AV Settings window opens to Visuals tab above all other windows, the other two tabs are Audio and Interaction
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/backlight_label"), 10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/select_audio_settings"), 10));//Verifying that the other two tabs are Audio and Interaction
        Assert.assertTrue(AppiumHelper.isElementVisible("//android.widget.ImageView[@content-desc=\"Interaction\"]", null, 10));

        TestRail.addStepName("Wait for 10 seconds and click on the AV Settings window");
        AppiumHelper.waitForSeconds(10);
        AppiumHelper.findElementBy(By.id("com.prometheanworld.avisettings:id/backlight_label")).click();
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));

        TestRail.addStepName("Wait for 30 seconds of no activity");
        AppiumHelper.waitForSeconds(30);
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));

        TestRail.addStepName("Presses the hardware Volume up buttons on the panel");
        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.ActivPanel);

        TestRail.addStepName("Click the gear icon in the right of volume changer UI");
        AppiumHelper.findElementBy(By.id("com.prometheanworld.avisettings:id/launch_avi")).click();

        TestRail.addStepName("Wait for 30 seconds of no activity");
        AppiumHelper.waitForSeconds(30);
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));
    }

    /**C115357 Verify that AVSettings hides when user taps else where on panel
    Steps
    1.Press remote settings button
    2.Tap on screen outside the AVSettings app
    Author:Sita
     */
    @Test(groups= "P1")
    public void C115357AVSettingsHidesWhenTapsElseWhere() {
        TestRail.setTestRailId("115357");
        TestRail.addStepName("Press remote settings button");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));

        TestRail.addStepName("Tap on screen outside the AVSettings app");
        AppiumHelper.showTaskbar();
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));
    }

    /**C115358 Verify that AVSettings hides if user presses the remote settings button again
    Steps
    1.Press the remote settings button
    2.Press the remote settings button again
    Author:Sita
     */
    @Test(groups= "P1")
    public void C115358AVSettingsHidesIfRemoteSettingsButtonPressed() {
        TestRail.setTestRailId("115358");
        TestRail.addStepName("Press the remote settings button");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));

        TestRail.addStepName("Press the remote settings button again");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));
    }

    /**C115359 Verify that AVSettings hide timer resets when user interacts with it
    Steps
    1.Press the remote settings button
    2.Wait for about 10s, press anywhere on the AVSettings app
    3.Wait for 30s and don't interact with panel
    Author:Sita
     */
    @Test(groups= "P2")
    public void C115359AVSettingsHideTimerResets() {
        TestRail.setTestRailId("115359");
        TestRail.addStepName("Press the remote settings button");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));
        TestRail.addStepName("Wait for about 10s, press anywhere on the AVSettings app");
        AppiumHelper.waitForSeconds(10);
        final By interaction = Locator.byText("Interaction");
        ElementHelper.click(interaction);
        TestRail.addStepName("Wait for 30s and don't interact with panel");
        AppiumHelper.waitForSeconds(30);
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.avisettings:id/app_label"), 10));
    }
}