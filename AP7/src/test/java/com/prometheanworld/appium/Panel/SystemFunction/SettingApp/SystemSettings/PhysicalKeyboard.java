package com.prometheanworld.appium.Panel.SystemFunction.SettingApp.SystemSettings;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP7.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author: lujiahui
 * @time: 2021/11/15
 */

public class PhysicalKeyboard extends BaseTest {

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        ScreenHelper.clickAt(Location.CENTER);
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
        POFactory.getInstance(SystemPO.class).closeAllOpenAppsOnRecentApps();
    }

    /**
     * C120860 Add the Physical Keyboard setting option
     * 1.Unified Meun-->locker-->Settings-->System-->Languages & input
     * Comfirm that add Physical Keyboard setting option
     */
    @Test
    public void C120860AddThePhysicalKeyboardSettingOptionTest() {
        TestRail.setTestRailId("C120860");

        TestRail.addStepName("Go to Unified Meun-->locker-->Settings-->System-->Languages & input page,Comfirm that add Physical Keyboard setting option");
        enterLanguageInputPage();
        AssertKt.assertPresent(Locator.byTextContains("Physical keyboard"), 3);
        ElementHelper.click(By.id("android:id/close_window"));
    }

    /**
     * C120877 Active input methods turn on other language,view Physical Keyboard(connected Physical Keyboard)
     * 1.Connected Physical Keyboard:QWERTY keyboard
     * 2.Click Unified Meun-->locker-->Settings-->System-->Languages & input-->Virtual Keyboard-->Android Keyboard-->>Languages
     * 3.Turn off Use system language then Turn on French,German language on Active input methods list
     * 4.Click back to Languages & input page
     * Comfirm that: Physical Keyboard setting option tip the Chicony HP Elite USB Keyboard
     * 5.Click Physical Keyboard setting option
     * Comfirm that:
     * Physical Keyboard page show the item:Chicony HP Elite USB Keyboard
     * disaply:
     * Android Keyboard (AOSP)-English (US)
     * Android Keyboard (AOSP)-French
     * Android Keyboard (AOSP)-German
     */
    @Test
    public void C120877ActiveInputMethodsTurnOnOtherLanguageViewPhysicalKeyboardTest() {
        TestRail.setTestRailId("C120877");

        TestRail.addStepName("Connected Physical Keyboard,go to Virtual_keyboard_language turn on other Keyboard Language");
        enterLanguageInputPage();
        goToVirtualkeyboardLanguagePage();
        clearTurnOnActiveInputMethodslanguage("false");
        CommonOperator.scrollAndClick("com.android.settings:id/list","French");
        CommonOperator.scrollAndClick("com.android.settings:id/list","German");
        backTolanguageInputPage();

        TestRail.addStepName("Go to Physical Keyboard page,Comfirm disaply item and Keyboard Language ");
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Physical keyboard" + "']"));
        AssertKt.assertPresent(Locator.byTextContains("Lite-On Technology Corp. Lenovo USB Keyboard"), 3);
        AssertKt.assertPresent(Locator.byTextContains("Android Keyboard (AOSP) - English (US)"), 3);
        AssertKt.assertPresent(Locator.byTextContains("Android Keyboard (AOSP) - French"), 3);
        AssertKt.assertPresent(Locator.byTextContains("Android Keyboard (AOSP) - German"), 3);
        ElementHelper.click(By.id("android:id/close_window"));
    }

    /**
     * C120875 Select the keyboard layout language
     * 1.Connected Physical Keyboard:wireless AZERTY keyboard
     * 2.Virtual Keyboard settings Turn on Use system language
     * 3.Click Unified Meun-->locker-->Settings-->System-->Languages & input-->Physical Keyboard
     * 4.Click content:Android Keyboard (AOSP)-English (US) on Physical Keyboard page
     * Comfirm that Go to keyboard layout page and show language list
     * 5.Swipe up and down the list of national keyboards
     * can swipe normally to view the national keyboards list
     * 6.Click French keyboards
     * Select French keyboard successfully, return to Physical Keyboard page
     * Display France under Android Keyboard (AOSP)-English (US)
     * 7.Click content:Android Keyboard (AOSP)-English (US) on Physical Keyboard page again
     * Comfirm that Go to keyboard layout page and show language list
     * 8.Swipe up the list of national keyboards
     * 9.Click English (US) keyboards
     * Select English (US) keyboard successfully, return to Physical Keyboard page
     * Display English (US) under Android Keyboard (AOSP)-English (US)
     */
    @Test
    public void C120875SelectThekeyboardLayoutLanguageTest() {
        TestRail.setTestRailId("C120875");

        TestRail.addStepName("Connected Physical Keyboard,go to Virtual_keyboard_language turn on Use system Language");
        enterLanguageInputPage();
        goToVirtualkeyboardLanguagePage();
        turnOnOrOffUseSystemLanguage("true");
        backTolanguageInputPage();

        TestRail.addStepName("Go to Physical Keyboard page,Click Keyboard Language to Layout page select Keyboard layout style ");
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Physical keyboard" + "']"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Android Keyboard (AOSP) - English (US)" + "']"));
        CommonOperator.scrollAndClick("com.android.settings:id/list","French");
        AssertKt.assertPresent(Locator.byTextContains("French"), 3);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Android Keyboard (AOSP) - English (US)" + "']"));
        CommonOperator.scrollAndClick("com.android.settings:id/list","English (US)");
        AssertKt.assertPresent(Locator.byTextContains("English (US)"), 3);
        ElementHelper.click(By.id("android:id/close_window"));
    }

    public void enterLanguageInputPage() {
        try {
            AppiumHelper.execToString("adb shell am start com.android.settings/com.android.settings.LanguageSettings");
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppiumHelper.waitForSeconds(3);
    }

    public void goToVirtualkeyboardLanguagePage() {
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Virtual keyboard" + "']"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Android Keyboard (AOSP)" + "']"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='" + "Languages" + "']"));
        ElementHelper.click(By.id("android:id/maximize_window"));
        AppiumHelper.waitForSeconds(2);
    }

    public void turnOnOrOffUseSystemLanguage(String state) {
        AndroidElement languageState = Driver.getAndroidDriver().findElementByXPath("//android.support.v7.widget.RecyclerView[@resource-id='com.android.settings:id/list']/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Switch");
        if (languageState.getAttribute("checked").equals(state)) {
            Log.info("The use system language Switch is already " + state + ",no need to change");
        } else {
            Log.info("Change the state of use system language to " + state);
            languageState.click();
            AppiumHelper.waitForSeconds(2);
            AndroidElement languageStateAgain = Driver.getAndroidDriver().findElementByXPath("//android.support.v7.widget.RecyclerView[@resource-id='com.android.settings:id/list']/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Switch");
            assertEquals(languageStateAgain.getAttribute("checked"), state, "The use system language switch status does not set succesfully.");
        }
    }

    public void backTolanguageInputPage(){
        for (int i=1;i<4;i++) {
            ElementHelper.click(Locator.byContentDesc("Navigate up"));
            AppiumHelper.waitForSeconds(2);
        }
        AppiumHelper.waitForSeconds(2);
    }

    public void clearTurnOnActiveInputMethodslanguage(String state){
        AndroidElement languageState = Driver.getAndroidDriver().findElementByXPath("//android.support.v7.widget.RecyclerView[@resource-id='com.android.settings:id/list']/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Switch");
        if (languageState.getAttribute("checked").equals(state)) {
            Log.info("The use system language Switch is already " + state + ",need to change true then change " + state +" Clear turn on active input methods language." );
            languageState.click();
            languageState.click();
            AndroidElement languageStateAgainOne = Driver.getAndroidDriver().findElementByXPath("//android.support.v7.widget.RecyclerView[@resource-id='com.android.settings:id/list']/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Switch");
            assertEquals(languageStateAgainOne.getAttribute("checked"), state, "The use system language switch status does not set succesfully.");
        } else {
            Log.info("Change the state of use system language to " + state +" Clear turn on active input methods language.");
            languageState.click();
            AppiumHelper.waitForSeconds(2);
            AndroidElement languageStateAgainTwo = Driver.getAndroidDriver().findElementByXPath("//android.support.v7.widget.RecyclerView[@resource-id='com.android.settings:id/list']/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Switch");
            assertEquals(languageStateAgainTwo.getAttribute("checked"), state, "The use system language switch status does not set succesfully.");
        }
    }

}