package com.prometheanworld.appium.Panel.SystemFunction.SettingApp.DisplaySetting;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class Source extends BaseTest {

//      Setup method ,make sure that there are just 2 users( Guest and  Owner without Pin),and current Login user is Owner
    @BeforeMethod
    public void setUp(){
        Log.info("Setup method start");
        Driver.getAndroidDriver().closeApp();
        AppiumHelper.waitForSeconds(2);

        currentUserName = AppiumHelper.getCurrentUserName();
        if(!currentUserName.equals("Owner")) {
            AppiumHelper.logOut("DELETE");
            AppiumHelper.logIn("Owner",true,false);
            Log.info("Login to Owner");
        } else {
            Log.info("Already on Owner");
        }

        if(getSwitchStateViaAPI("unified_launcher_visible").equals("true"))
        {
            Log.info("the Unified Menu Switch is ON, no need to change. ");
        }
        else {
            Log.info("the Unified Menu Switch is OFF, need to turn on it. ");
            AppIconDisplaySetting.setSwitchStateViaAPI("unified_launcher_visible",true);
            Log.info("Reboot the panel");
            AppIconDisplaySetting.rebootPanelAndResetServices();
        }
        if(getSwitchStateViaAPI("home_source").equals("true"))
        {
            Log.info("the home_source Switch is ON, no need to change. ");
        }
        else {
            Log.info("the home_source Switch is OFF, need to turn on it. ");
            AppIconDisplaySetting.setSwitchStateViaAPI("home_source",true);
            Log.info("Reboot the panel");
            AppIconDisplaySetting.rebootPanelAndResetServices();
        }

        Log.info("Setup method end");
    }

    /** Description:  Add "Last Source" on Android Settings MOD-23
     * 1. Android Settings --> Display --> Advanced --> Power On Default Source
     * 2. Confirm that there is a Setting called: "Last Source"
     */
    @Test
    public void Test_LastSourceInAdvancedSetting(){
        TestRail.setTestRailId("83247");
        Log.info("-------------Test LastSourceInAdvancedSetting Start---------------");
        Log.info("Enter Default Source Setting Page");
        AppIconDisplaySetting.enterDisplayAdvancedPage(true,"Power On Default Source");
        AssertKt.assertPresent(Locator.byTextContains("Last Source"), 3);
        Log.info("Return to the home page");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToHomePage(panelModel,2);
        Log.info("-------------Test LastSourceInAdvancedSetting End---------------");
    }

    /** Description:  select "Last Source" when no external source is attached MOD-25
     * "1. When there are only 2 users (Owner and Guest) and Owner does not set a PIN, with no external source is attached
     * 2. Sign in as Owner, Android Settings --> Display --> Advanced --> Power On Default Source
     * 3. Select option to "Last Source"
     * 4. Reboot panel and confirm that the panel boots up into the Android home page
     * Note: Owner only"
     */
    @Test
    public void Test_LastSourceWithoutExternalSource(){
        TestRail.setTestRailId("83249");
        Log.info("-------------Test LastSourceWithoutExternalSource Start---------------");
        Log.info("Enter Default Source Setting Page");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Power On Default Source");
        AppiumHelper.waitForSeconds(2);
        Log.info("Select option to Last Source");
        ElementHelper.click(By.xpath("//android.widget.RadioButton[contains(@text, '" + "Last Source" + "')]"));
        Log.info("Reboot the panel");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppiumHelper.waitForSeconds(5);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/text_date"), 3);
        Log.info("-------------Test LastSourceWithoutExternalSource End---------------");
    }

    /** Description:  select "Last Source" when no external source is attached MOD-28
     * 1. Sign in as Owner
     * 2. Android Settings --> Display --> Advancecd --> Power On Default Source,switch to "Last Source"
     * 3. Sign in as User, confirm that the option is "Last Source"
     * 4. Switch to "Home"
     * 5. Sign in as Owner, confirm that the option is "Home"
     */
    @Test
    public void Test_DefaultSourceSettingSwitchUser(){
        TestRail.setTestRailId("83252");
        Log.info("-------------Test DefaultSourceSettingSwitchUser Start---------------");
        Log.info("Enter Default Source Setting Page");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Power On Default Source");
        AppiumHelper.waitForSeconds(2);
        Log.info("Select option to Last Source");
        ElementHelper.click(By.xpath("//android.widget.RadioButton[contains(@text, '" + "Last Source" + "')]"));
        AppiumHelper.backToHomePage(panelModel,2);
        Log.info("Setup a new use");
        //driver = androidUserTest.setUpNewUser(driver);
        AppiumHelper.setUpNewUser();
        Log.info("Enter Default Source Setting Page Again");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Power On Default Source");
        AppiumHelper.waitForSeconds(5);
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Last Source" + "')]").getAttribute("checked"), "true","The option is not Last Source");
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Home" + "')]").getAttribute("checked"), "false","The option should not be Home Source");
        ElementHelper.click(By.xpath("//android.widget.RadioButton[contains(@text, '" + "Home" + "')]"));
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToHomePage(panelModel,2);
        // Relogin to Owner
        Log.info("Relogin to Owner");
        AppiumHelper.logOut("DELETE");
        AppiumHelper.logIn("Owner",true,false);
        //click the lower-right corner point, use to hide  menu
        Log.info("click the lower-right corner point, use to hide menu");
        AppiumHelper.waitForSeconds(2);
        ScreenHelper.clickAt(0.999, 0.999);
        Log.info("Enter Default Source Setting Page Again");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Power On Default Source");
        AppiumHelper.waitForSeconds(2);
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Last Source" + "')]").getAttribute("checked"), "false","The option is not Last Source");
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Home" + "')]").getAttribute("checked"), "true","The option should not be Home Source");
        AppiumHelper.waitForSeconds(5);
        AppiumHelper.backToHomePage(panelModel,2);
        Log.info("-------------Test DefaultSourceSettingSwitchUser End---------------");
    }

    /** Description: reboot panel after switching Default Source to other options MOD-39 MOD-41
     * 1. Sign in as Owner, add User accounts with no OPS attached
     * 2. Android Settings --> Display --> Advancecd --> Power On Default Source
     * 3. Switch to "OPS"
     * 4. Reboot panel, confirm that the panel boots up into the OPS source that show no signal or a black source screen
     */
    @Test
    public void Test_DefaultSourceSettingToOPS(){
        TestRail.setTestRailId("83263,83265");
        Log.info("-------------Test DefaultSourceSettingToOPS Start---------------");
        Log.info("Enter Default Source Setting Page");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Power On Default Source");
        AppiumHelper.waitForSeconds(2);
        Log.info("Select option to OPS");
        ElementHelper.click(By.xpath("//android.widget.RadioButton[contains(@text, '" + "OPS" + "')]"));
        AppiumHelper.backToHomePage(panelModel,2);
        Log.info("Setup a new use");
        AppiumHelper.setUpNewUser();
        Log.info("Reboot the panel");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppiumHelper.waitForSeconds(5);
        if(panelModel.equals("AP7_B")){
            AssertKt.assertPresent(By.id("com.hisilicon.tvui:id/tv_tip_msg_title"), 3);
            AssertKt.assertPresent(Locator.byTextContains("No Signal"), 3);
        }
        else {
            AssertKt.assertPresent(By.id("com.mstar.tv.tvplayer.ui:id/linearlayout_nosignal_imageview"), 3);
            AssertKt.assertPresent(By.id("com.mstar.tv.tvplayer.ui:id/source_imageview"), 3);
        }
        Log.info("Enter Default Source Setting Page");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Power On Default Source");
        AppiumHelper.waitForSeconds(2);
        Log.info("Select option to Home");
        ElementHelper.click(By.xpath("//android.widget.RadioButton[contains(@text, '" + "Home" + "')]"));
        Log.info("Reboot the panel Again");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppiumHelper.waitForSeconds(5);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/text_date"), 3);
        Log.info("-------------Test DefaultSourceSettingToOPS End---------------");
    }

    /** Description:  Add "Source Detection" on Android Settings MOD-59
     * 1. Android Settings --> Display --> click "Advanced"
     * 2. There is an "Source Detection" setting in the options list
     */
    @Test
    public void Test_SourceDetectionInAdvancedSetting(){
        Log.info("-------------Test SourceDetectionInAdvancedSetting Start---------------");
        //Enter display setting page with adb command
        AppIconDisplaySetting.enterDisplaySetting();
        AppiumHelper.waitForSeconds(3);
        if(!panelModel.equals("AP7_U")){
            // Max the window,so we can decrease the scroll operation
            ElementHelper.click(By.id("android:id/maximize_window"));
        }
        AppiumHelper.waitForSeconds(2);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='"+"Advanced"+"']"));
        AppiumHelper.waitForSeconds(2);
        AssertKt.assertPresent(Locator.byTextContains("Source Detection"), 3);
        Log.info("Return to the home page");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToHomePage(panelModel,1);
        Log.info("-------------Test SourceDetectionInAdvancedSetting End---------------");

    }

    /** Description:  Three options of "Source Detection" MOD-60
     * 1. Android Settings --> Display --> Advanced --> Source Detection
     * 2. There are three options: Off, Manual, Automatic
     * 3. The default is "Off"
     */
    @Test
    public void Test_ThreeOptionsOfSourceDetection(){
        TestRail.setTestRailId("83283");
        Log.info("-------------Test ThreeOptionsOfSourceDetection Start---------------");
        Log.info("Enter Default Source Setting Page");
        AppIconDisplaySetting.enterDisplayAdvancedPage(false,"Source Detection");
        AppiumHelper.waitForSeconds(3);
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Off" + "')]").getAttribute("checked"), "true","The default option is not Off");
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Manual" + "')]").getAttribute("checked"), "false","The default option should not be Manual");
        assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.RadioButton[contains(@text, '" + "Automatic" + "')]").getAttribute("checked"), "false","The default option should not be Automatic");
        Log.info("Return to the home page");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.backToHomePage(panelModel,2);
        Log.info("-------------Test ThreeOptionsOfSourceDetection End---------------");
    }
    public static String getSwitchStateViaAPI(String switchName) {
        try {
            String IsOn = AppiumHelper.execToString("adb shell getprop persist.settings." + switchName);
            AppiumHelper.waitForSeconds(3);
            Log.info("the state of " + switchName + " is " + IsOn.replaceAll("\r|\n", ""));
            return IsOn.replaceAll("\r|\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchElementException("Get " + switchName + " state failed");
        }

    }
}
