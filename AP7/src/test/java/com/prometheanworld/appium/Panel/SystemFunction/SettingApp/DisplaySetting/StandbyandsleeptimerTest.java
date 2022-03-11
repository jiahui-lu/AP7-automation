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
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.annotations.*;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class StandbyandsleeptimerTest extends BaseTest {

    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        try {
            ScreenHelper.clickAt(0.25, 0.25);
        }
        catch (Exception e){}
        //AppiumHelper.waitForSeconds(5);
    }

    @BeforeTest
    public void setup() {

    }

    @AfterMethod
    public void teardown() {
        try {
            ScreenHelper.clickAt(0.5, 0.999);
        }catch (Exception e){
        }
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            default:
                Driver.getAndroidDriver().findElementByXPath("//android.widget.Button[@resource-id='android:id/close_window']").click();
                Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 8, 17);
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.sleepseekbarxpath), 0, 0, 17);
                try {
                    ScreenHelper.clickAt(0.25, 0.25);
                }catch (Exception e){
                }
                Driver.getAndroidDriver().findElementByXPath("//android.widget.Button[@resource-id='android:id/close_window']").click();
            case "AP7_U":
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 8, 17);
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.sleepseekbarxpath), 0, 0, 17);
                try {
                    ScreenHelper.clickAt(0.5, 0.999);
                }catch (Exception e){
                    ElementHelper.click(By.xpath("//android.widget.TextView[@text='Home']"));
                }
                ElementHelper.click(By.xpath("//android.widget.TextView[@text='Home']"));
                ScreenHelper.clickAt(0.5, 0.5);
             }
        }


    /**
     * ASA-01 Timer Settings: Default value of timer
     * steps:
     * Go to Settings page, Display -> Standby and Sleep Timers
     * Check the default values for silders
     * <p>
     * Result:
     * Count down timer pops up one minute before panel goes on standby
     * Verify panel goes to standby after one minute count down has ended
     */
    @Test
    public void ASA_01TimerSettingsDefaultvalueoftimerTest() {
        TestRail.setTestRailId("83090");
        Log.info("-------------Test ASA_01TimerSettingsDefaultvalueoftimer Start---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            default:
                Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
                Driver.getAndroidDriver().findElement(Locator.byText("Display")).click();
                AppiumHelper.waitForSeconds(2);
                Driver.getAndroidDriver().findElement(Locator.byText("Advanced")).click();
                AppiumHelper.waitForSeconds(2);
                AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='android:id/summary' and @text='Standby after 1 hour(s) and 0 minute(s) of inactivity. Sleep after an additional 0 hour(s) and 3 minute(s) of inactivity.']").isDisplayed(),"The default value outside is correct");
                Driver.getAndroidDriver().findElement(Locator.byText("Standby and Sleep Timers")).click();
                AppiumHelper.waitForSeconds(2);
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_label']").getText(), "1 hr", "standby timer is not 1 hour");
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/sleep_label']").getText(), "3 mins", " sleep timer is not 3 minutes");
                break;
            case "AP7_U":
                Driver.getAndroidDriver().findElement(Locator.byText("Display")).click();
                AppiumHelper.waitForSeconds(2);
                Driver.getAndroidDriver().findElement(Locator.byText("Advanced")).click();
                AppiumHelper.waitForSeconds(2);
                AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='android:id/summary' and @text='Standby after 1 hour(s) and 0 minute(s) of inactivity. Sleep after an additional 0 hour(s) and 3 minute(s) of inactivity.']").isDisplayed(),"The default value outside is correct");
                Driver.getAndroidDriver().findElement(Locator.byText("Standby and Sleep Timers")).click();
                AppiumHelper.waitForSeconds(2);
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_label']").getText(), "1 hr", "standby timer is not 1 hour");
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/sleep_label']").getText(), "3 mins", " sleep timer is not 3 minutes");
                break;
        }
    }

    /**
     * Timer setting: Set the standby timer for 3 minutes
     * steps:
     * Go to Settings page, Display -> Standby and Sleep Timers
     * Set standby timer to the minimum (3 min), the selected time on the right is displayed for "3 min"
     * Wait for 3 mins without interacting with panel
     * Interact with the panel
     * result:
     * 3.panel's screen is turned off
     * 4.panel's screen will turned on again
     */
    @Test
    public void ASA_04TimersettingSetthestandbytimerfor3minutesTest() throws Exception {
        TestRail.setTestRailId("83093");
        Log.info("-------------Test ASA_04TimersettingSetthestandbytimerfor3minutes Start---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            case "AP7_B":
                TestRail.onTestFailure("ASA_04TimersettingSetthestandbytimerfor3minutesTest","lango can not do this");
                break;
            default:
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 0, 17);
                ScreenHelper.clickAt(0.0838541666666667, 0.1490740740740741);
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_label']").getText(), "3 mins", "Slide position shows not correct");
                AppiumHelper.waitForSeconds(250);
                AssertKt.assertEquals(AppiumHelper.execToString("adb shell getprop persist.backlight.status").substring(0, 1), "0", "panel not in standby");
                break;
            }
    }



    /**
     * Timer Settings: Slide the standby timer to multiple values
     * steps:
     * 1.Go to Settings page, Display -> Standby and Sleep Timers
     * 2. Slide the standby timer slider to multiple values and pause briefly
     * result:
     * The sliding effect is normal. After sliding to the value, the data displayed on the right is consistent with that marked on the slider
     */
    @Test
    public void TimerSettingsSlidethestandbytimertomultiplevaluesTest() {
        TestRail.setTestRailId("116672");
        Log.info("-------------Test TimerSettingsSlidethestandbytimertomultiplevalues Start---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            case "AP7_U":
                settingsPO.navigateToStandbyTime();
                for ( int i = 0; i < 18; i ++){
                    ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, i, 17);
                    AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.standbyshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                    while (Driver.getAndroidDriver().findElementByXPath(settingsPO.standbyseekbarxpath).getText() == "Never" ){
                        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), "Never", "Slide position shows not correct");
                    }
                }
                break;

            default:
                Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
                settingsPO.navigateToStandbyTime();
                for (int i = 0; i < settingsPO.expect.length; i++) {
                    ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, i, settingsPO.expect.length - 1);
                    ElementHelper.waitUntilTextToBe(By.xpath(settingsPO.standbyshowxpath), settingsPO.expect[i]);
                    if (ElementHelper.getText(By.xpath(settingsPO.standbyshowxpath)).equals("Never")) {
                        String sleepTime = ElementHelper.getText(By.xpath(settingsPO.sleepshowxpath));
                        AssertKt.assertEquals(sleepTime, "Never", "Sleep time should be 'Never' when Standby time is 'Never'");
                    }
                }
                break;

        }
        Log.info("-------------Test TimerSettingsSlidethestandbytimertomultiplevalues End---------------");
    }

    /**
     * Timer Settings: Click the standby timer to multiple values
     * steps:
     * 1.Go to Settings page, Display -> Standby and Sleep Timers
     * 2. Click the standby timer to multiple values and pause briefly
     * result:
     * Click and the effect is normal. After clicking the value, the right display is consistent with the slider marked data
     */
    @Test
    public void TimerSettingsClickthestandbytimertomultiplevaluesTest() {
        TestRail.setTestRailId("116673");
        Log.info("-------------Test TimerSettingsSlidethestandbytimertomultiplevalues Start---------------");
        int x = 161;
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            default:
                Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
                settingsPO.navigateToStandbyTime();
                ScreenHelper.clickAtPoint(51, 45);
                for ( int i = 0; i < 18; i ++){
                    ScreenHelper.clickAtPoint(x, 339);
                    AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.standbyshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                    while (Driver.getAndroidDriver().findElementByXPath(settingsPO.standbyshowxpath).getText() == "Never" ){
                        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), "Never", "Slide position shows not correct");
                    }
                    x = x + 41;
                }
                break;
            case "AP7_U":
                settingsPO.navigateToStandbyTime();
                ScreenHelper.clickAtPoint(51, 45);
                for ( int i = 0; i < 18; i ++){
                    ScreenHelper.clickAtPoint(x, 339);
                    AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.standbyshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                    while (Driver.getAndroidDriver().findElementByXPath(settingsPO.standbyshowxpath).getText() == "Never" ){
                        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), "Never", "Slide position shows not correct");
                    }
                    x = x + 41;
                }
                break;
        }

    }

    /**
     * Standby and sleep timer Settings: Watch video timer does not run
     * steps:
     * 1.Go to Settings page, Display -> Standby and Sleep Timers
     * 2. Set the standby time and sleep to other values, such as 3 minutes
     * 3. After you open a video saved in the device, the device does not interact with the panel
     * result:
     *  The video keeps playing without lock screen or sleep
     */
    @Test
    public void StandbyandsleeptimerSettingsWatchvideotimerdoesnotrunTest() throws Exception {
        TestRail.setTestRailId("116680");
        Log.info("-------------Test StandbyandsleeptimerSettingsWatchvideotimerdoesnotrun Start---------------");
        String url = "https://www.bilibili.com/video/BV1az4y1C7za?from=search&seid=17686371645255346904";
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        settingsPO.navigateToStandbyTime();
        ElementHelper.scrollProgressBar(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_timeout']/android.widget.LinearLayout[1]/android.widget.SeekBar[1]"),
                0, 0, 17);
        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_label']").getText(), "3 mins", "Slide position shows not correct");
        Driver.getAndroidDriver().findElementByXPath("//android.widget.Button[@resource-id='android:id/close_window']").click();
        AppiumHelper.StartAppFromUnifiedLauncher("Chromium");
        Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
        Driver.getAndroidDriver().findElementByXPath("//android.widget.EditText[@resource-id='org.chromium.chrome:id/url_bar']").click();
        Driver.getAndroidDriver().findElementByXPath("//android.widget.EditText[@resource-id='org.chromium.chrome:id/url_bar']").setValue(url);
        Driver.getAndroidDriver().findElementByXPath("//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.ImageView[1]").click();
        AppiumHelper.waitForSeconds(5);
        ScreenHelper.clickAt(0.5,0.5);
        AppiumHelper.waitForSeconds(200);
        switch (AppiumHelper.getPanelModel()) {
            case "AP7_B":
                ScreenHelper.clickAt(0.25, 0.25);
                AssertKt.assertEquals(AppiumHelper.execToString("adb shell getprop sys.powersaving.time").substring(0, 1), "0", "panel not in standby");
                break;
            default:
                AssertKt.assertEquals(AppiumHelper.execToString("adb shell getprop persist.backlight.status"), "0", "panel not in standby");
                break;
        }
        Log.info("-------------Test StandbyandsleeptimerSettingsWatchvideotimerdoesnotrun End---------------");
    }
    /**
     * Sleep Timer Settings: Slide the standby timer to multiple values
     * steps:
     * 1.Go to Settings page, Display -> Standby and Sleep Timers
     * 2. Slide the sleep timer slider to multiple values and pause briefly
     * result:
     * 2. The sliding effect is normal. After sliding to the value, the data displayed on the right is consistent with that marked on the slider
     */
    @Test
    public void SleepTimerSettingsSlidethestandbytimertomultiplevaluesTest(){
        TestRail.setTestRailId("116675");
        Log.info("-------------Test SleepTimerSettingsSlidethestandbytimertomultiplevalues Start---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            default:
                Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 0, 17);
                for ( int i = 0; i < 18; i ++){
                    ElementHelper.scrollProgressBar(By.xpath(settingsPO.sleepseekbarxpath), 0, i, 17);
                    AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepseekbarxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                }
                break;
            case "AP7_U":
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 0, 17);
                for ( int i = 0; i < 18; i ++){
                    ElementHelper.scrollProgressBar(By.xpath(settingsPO.sleepseekbarxpath), 0, i, 17);
                    AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                }
                break;

        }
        Log.info("-------------Test SleepTimerSettingsSlidethestandbytimertomultiplevalues End---------------");
    }

    /**
     * Sleep Timer Settings: Click the standby timer to multiple values
     * steps:
     * 1.Go to Settings page, Display -> Standby and Sleep Timers
     * 2. Click the sleep timer to multiple values and pause briefly
     * result:
     * 2. Click and the effect is normal. After clicking the value, the right display is consistent with the slider marked data
     */
    @Test
    public void SleepTimerSettingsClickthestandbytimertomultiplevaluesTest(){
        TestRail.setTestRailId("116676");
        Log.info("-------------Test SleepTimerSettingsSlidethestandbytimertomultiplevalues Start---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        int x = 161;
        switch (AppiumHelper.getPanelModel()) {
            default:
                Driver.getAndroidDriver().findElement(Locator.byResourceId("android:id/maximize_window")).click();
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 0, 17);
                for ( int i = 0; i < 18; i ++){
                    ScreenHelper.clickAtPoint(x, 781);
                    AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                    x = x + 42;
                }
                break;
            case "AP7_U":
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath(settingsPO.standbyseekbarxpath), 0, 0, 17);
                for ( int i = 0; i < 18; i ++){
                    if (i == 17){
                        ScreenHelper.clickAtPoint(846, 781);
                        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                    }
                    else {
                        ScreenHelper.clickAtPoint(x, 781);
                        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath(settingsPO.sleepshowxpath).getText(), settingsPO.expect[i], "Slide position shows not correct");
                        x = x + 42;
                    }
                }
                break;
        }
        Log.info("-------------Test SleepTimerSettingsSlidethestandbytimertomultiplevalues End---------------");
    }

    /**
     * Timer setting: Set the standby timer time to 10 minutes
     * steps:
     * 1.Go to Settings page, Display -> Standby and Sleep Timers
     * 2. Set the standby time to 10 minutes and wait 10 minutes
     * 3.Interact with the panel
     * result:
     * Confirm that the screen is closed
     * 4.panel's screen will turned on again
     */
    @Test
    public void TimersettingSetthestandbytimertimeto10minutesTest() throws Exception {
        TestRail.setTestRailId("116674");
        Log.info("-------------Test TimersettingSetthestandbytimertimeto10minutes Start---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            case "AP7_B":
                TestRail.onTestFailure("TimersettingSetthestandbytimertimeto10minutesTest","lango can not do this");
                break;
            default:
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_timeout']/android.widget.LinearLayout[1]/android.widget.SeekBar[1]"),
                        0, 2, 17);
                ScreenHelper.clickAt(0.25, 0.25);
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_label']").getText(), "3 mins", "Slide position shows not correct");
                AppiumHelper.waitForSeconds(650);
                AssertKt.assertEquals(AppiumHelper.execToString("adb shell getprop persist.backlight.status").substring(0, 1), "0", "panel not in standby");
                break;
        }
        Log.info("-------------Test TimersettingSetthestandbytimertimeto10minutes End---------------");

    }

    /**
     * Timer Settings: Set the standby timer time to never
     * steps:
     * Go to Settings page, Display -> Standby and Sleep Timers
     * Set standby timer to the maximum (Never)
     * Leave the panel for one night (more than 12 hrs)
     * result:
     * 2.the selected time on the right is displayed for "Never",The Sleep Timer setting is grayed out (in a value of "Never")
     * 3.confirm that the panel screen is still on
     */

    @Test
    public void ASA_05TimerSettingsSetthestandbytimertimetoneverTest() throws Exception {
        TestRail.setTestRailId("83094");
        Log.info("-------------Test ASA_05TimerSettingsSetthestandbytimertimetoneverTest begin---------------");
        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switch (AppiumHelper.getPanelModel()) {
            case "AP7_B":
                TestRail.onTestFailure("ASA_05TimerSettingsSetthestandbytimertimetoneverTest","lango can not do this");
            default:
                settingsPO.navigateToStandbyTime();
                ElementHelper.scrollProgressBar(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_timeout']/android.widget.LinearLayout[1]/android.widget.SeekBar[1]"),
                        0, 0, 17);
                ScreenHelper.clickAt(0.25, 0.25);
                AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanworld.sleepstandbytimer:id/standby_label']").getText(), "3 mins", "Slide position shows not correct");
                AppiumHelper.waitForSeconds(44000);
                AssertKt.assertEquals(AppiumHelper.execToString("adb shell getprop persist.backlight.status").substring(0, 1), "0", "panel not in standby");
             }

        Log.info("-------------Test ASA_05TimerSettingsSetthestandbytimertimetoneverTest End---------------");
    }



}
