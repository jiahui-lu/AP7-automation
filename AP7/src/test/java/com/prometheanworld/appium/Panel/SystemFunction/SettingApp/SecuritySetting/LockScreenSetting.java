package com.prometheanworld.appium.Panel.SystemFunction.SettingApp.SecuritySetting;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.AP7.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.By;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @description:
 * @author: yangwenjin(Edward)
 * @time: 2021/12/15
 */
@Listeners({TestRailListener.class, TestStatusListener.class})
public class LockScreenSetting extends BaseTest {

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);

    /** Password:Mode and Default
     * Unified Launcher -> locker -> Settings -> Security & Location -> Screen lock
     * Jump directly to "Choose screen lock" page and the default current screen lock was on "None"
     * There are three ways to lock screen on "Choose screen lock" page, including "None", "PIN" and "Password"
     * By yangwenjin
     */
    @Test
    public void Password83661(){
        TestRail.setTestRailId("83661");

        TestRail.addStepName("1.Unified Launcher -> locker -> Settings -> Security & Location -> Screen lock");
        settingsPO.openSecurity();
        TestRail.addStepName("2.Jump directly to \"Choose screen lock\" page and the default current screen lock was on \"None\"");
        AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text='None']"));
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        TestRail.addStepName("3.There are three ways to lock screen on \"Choose screen lock\" page, including \"None\", \"PIN\" and \"Password\"");
        AssertKt.assertPresent(Locator.byText("None"),3);
        AssertKt.assertPresent(Locator.byText("PIN"),3);
        AssertKt.assertPresent(Locator.byText("Password"),3);

        systemPO.closeAppOnMenuBar("Settings");

    }

    /** Password:Normal Create
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password
     * Confirm that panel goes into Password screen with message "Choose your password" and the alphanumeric keyboard would pop up from bottom.
     * Type at least 4 characters and less than 17 characters as password
     * Click "CONTINUE"
     * Type same characters again to confirm your password, then click "OK"
     * Back to "Secrity & Location" page
     * Confirm that the current screen lock is on "Password"
     * By yangwenjin
     */
    @Test
    public void Password83662(){
        TestRail.setTestRailId("83662");

        TestRail.addStepName("1.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Password']"));
        TestRail.addStepName("2.Confirm that panel goes into Password screen with message \"Choose your password\" and the alphanumeric keyboard would pop up from bottom.");
        AssertKt.assertPresent(Locator.byResourceIdAndText("com.android.settings:id/suw_layout_title", "Choose your password"));
        TestRail.addStepName("3.Type at least 4 characters and less than 17 characters as password");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        TestRail.addStepName("4.Click \"CONTINUE\"");
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("5.Type same characters again to confirm your password, then click \"OK\"");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("6.Back to \"Secrity & Location\" page");
        TestRail.addStepName("7.Confirm that the current screen lock is on \"Password\"");
        AssertKt.assertPresent(Locator.byText("Security & Location"));
        AssertKt.assertPresent(Locator.byText("Password"));

        settingsPO.Restoreemptypassword("1111");
    }

    /**Password:Abnormal Create
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password
     * Confirm that panel goes into Password screen with info "Choose your password" and the alphanumeric keyboard would pop up from bottom.
     * When type fewer than 4 characters as a new password
     * Confirm that the "CONTINUE" option is unselectable, and info tip "Must be at least 4 characters"
     * When type more than 17 characters as a new password
     * Confirm that the "CONTINUE" option is unselectable, and info tip "Must be fewer than 17 characters"
     * by yangwenjin
     */
    @Test
    public void Password83663(){
        TestRail.setTestRailId("83663");

        TestRail.addStepName("1.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Password']"));
        TestRail.addStepName("2.Confirm that panel goes into Password screen with message \"Choose your password\" and the alphanumeric keyboard would pop up from bottom.");
        AssertKt.assertPresent(Locator.byResourceIdAndText("com.android.settings:id/suw_layout_title", "Choose your password"));
        TestRail.addStepName("3.When type fewer than 4 characters as a new password");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        TestRail.addStepName("4.Confirm that the \"CONTINUE\" option is unselectable, and info tip \"Must be at least 4 characters\"");
        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.android.settings:id/description_text']").getText(),"Must be at least 4 characters","show not correct");
        AssertKt.assertFalse(Driver.getAndroidDriver().findElementByXPath("//android.widget.Button[@resource-id='com.android.settings:id/next_button']").isEnabled(),"CONTINUE is enable");
        TestRail.addStepName("5.When type more than 17 characters as a new password");
        for (int i = 0; i < 15; i++){
            Driver.pressKey(AndroidKey.DIGIT_1);
        }
        TestRail.addStepName("6.Confirm that the \"CONTINUE\" option is unselectable, and info tip \"Must be fewer than 17 characters\"");
        AssertKt.assertEquals(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.android.settings:id/description_text']").getText(),"Must be fewer than 17 characters","show not correct");
        AssertKt.assertFalse(Driver.getAndroidDriver().findElementByXPath("//android.widget.Button[@resource-id='com.android.settings:id/next_button']").isEnabled(),"CONTINUE is enable");

        systemPO.closeAppOnMenuBar("Settings");

    }

    /**	Password:Enter a different password for confirmation
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password
     * Confirm that panel goes into password screen with info "Choose your password" and the alphanumeric keyboard would pot up from bottom.
     * Type at least 4 characters as password
     * Click "CONTINUE"
     * Type different characters from step3 to confirm your password
     * Creation fails with info "Password don't match"
     * by yangwenjin
     */
    @Test
    public void Password83665(){
        TestRail.setTestRailId("83665");

        TestRail.addStepName("1.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Password']"));
        TestRail.addStepName("2.Confirm that panel goes into Password screen with message \"Choose your password\" and the alphanumeric keyboard would pop up from bottom.");
        AssertKt.assertPresent(Locator.byResourceIdAndText("com.android.settings:id/suw_layout_title", "Choose your password"));
        TestRail.addStepName("3.Type at least 4 characters as password");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        TestRail.addStepName("4.Click \"CONTINUE\"");
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("5.Type different characters from step3 to confirm your password");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("6.Creation fails with info \"Password don't match\"");
        AssertKt.assertEquals(Driver.getAndroidDriver().findElementById("com.android.settings:id/headerText").getText(),"Passwords don’t match","not correct");

        systemPO.closeAppOnMenuBar("Settings");
    }

    /**	Password:Enter the password the second time to select Cancel
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock -> Password
     * Confirm that panel goes into Password screen with message "Choose your password" and the alphanumeric keyboard Would pop up from bottom.
     * Type at least 4 characters as password, then click "CONTINUE"
     * Click "CANCEL"
     * Confirm that you back to "Choose screen lock" page
     * If you are confirming your password, then click "CANCEL"
     * You can also back to "Choose screen lock" page again
     * by yangwenjin
     */
    @Test
    public void Password83666(){
        TestRail.setTestRailId("83666");

        TestRail.addStepName("1.Unified Launcher -> locker -> Settings -> Security & Location -> Screen lock-> Password");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Password']"));
        TestRail.addStepName("2.Confirm that panel goes into Password screen with message \"Choose your password\" and the alphanumeric keyboard Would pop up from bottom.");
        AssertKt.assertPresent(Locator.byResourceIdAndText("com.android.settings:id/suw_layout_title", "Choose your password"));
        TestRail.addStepName("3.Type at least 4 characters as password, then click \"CONTINUE\"");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("4.Click \"CANCEL\"");
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='com.android.settings:id/cancel_button']"));
        TestRail.addStepName("5.Confirm that you back to \"Choose screen lock\" page");
        AssertKt.assertPresent(Locator.byText("Choose screen lock"));

        systemPO.closeAppOnMenuBar("Settings");
    }

    /**	Password：Change Currently password
     * Creat alphanumeric password for panel with at least 4 characters
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock
     * Type correct password and jump to "Choose screen lock" page
     * Click "Password", and type at least 4 characters as a new password
     * Confirm that the label "Must be at least 4 characters" disappear and "CONTINUE" is selectable state
     * Click "CONTINUE"
     * Type same characters again to confirm your password, then click "OK"
     * Confirm that no notification pop up
     * by yangwenjin
     */
    @Test
    public void Password83667(){
        TestRail.setTestRailId("83667");

        TestRail.addStepName("1.Creat alphanumeric password for panel with at least 4 characters");
        settingsPO.Creatpassword("password");
        TestRail.addStepName("2.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock");
        settingsPO.openSecurity();
        TestRail.addStepName("3.Type correct password and jump to \"Choose screen lock\" page");
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("4.Click \"Password\", and type at least 4 characters as a new password");
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Password']"));
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        TestRail.addStepName("5.Confirm that the label \"Must be at least 4 characters\" disappear and \"CONTINUE\" is selectable state");
        AssertKt.assertNotPresent(Locator.byResourceId("com.android.settings:id/description_text"));
        AssertKt.assertTrue(ElementHelper.isEnabled(Locator.byResourceId("com.android.settings:id/next_button")),"is not enable");
        TestRail.addStepName("6.Click \"CONTINUE\"");
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("7.Type same characters again to confirm your password, then click \"OK\"");
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("8.Confirm that no notification pop up");
        AssertKt.assertPresent(Locator.byText("Choose screen lock"));

        settingsPO.Restoreemptypassword("2222");

    }

    /**	Password:Change password to PIN
     * Creat alphanumeric password for panel with at least 4 characters
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock
     * Type correct password and jump to "Choose screen lock" page
     * Click "PIN", and type at least 4 digits as your PIN
     * Click "CONTINUE"
     * Type same digits again to confirm your PIN, then click "OK"
     * Confirm that no notification pop up
     */
    @Test
    public void Password83668(){
        TestRail.setTestRailId("83668");

        TestRail.addStepName("1.Creat alphanumeric password for panel with at least 4 characters");
        settingsPO.Creatpassword("password");
        TestRail.addStepName("2.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock");
        settingsPO.openSecurity();
        TestRail.addStepName("3.Type correct password and jump to \"Choose screen lock\" page");
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("4.Click \"PIN\", and type at least 4 digits as your PIN");
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='PIN']"));
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        TestRail.addStepName("5.Click \"CONTINUE\"");
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("6.Type same digits again to confirm your PIN, then click \"OK\"");
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("7.Confirm that no notification pop up");
        AssertKt.assertPresent(Locator.byText("Choose screen lock"));

        settingsPO.Restoreemptypassword("2222");
    }

    /** Password:Change PIN to password
     * Creat PIN for panel with at least 4 digits
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock
     * Type correct password and jump to "Choose screen lock" page
     * Click "Password", and type at least 4 confirming as your new password
     * Click "CONTINUE"
     * Type same characters again to confirm your password, then click "OK"
     * Confirm that no notifications pop up
     * by yangwenjin
     */
    @Test
    public void Password83669(){
        TestRail.setTestRailId("83669");

        TestRail.addStepName("1.Creat PIN for panel with at least 4 digits");
        settingsPO.Creatpassword("pin");
        TestRail.addStepName("2.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        TestRail.addStepName("3.Type correct password and jump to \"Choose screen lock\" page");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("4.Click \"Password\", and type at least 4 confirming as your new password");
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Password']"));
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        TestRail.addStepName("5.Click \"CONTINUE\"");
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("6.Type same characters again to confirm your password, then click \"OK\"");
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.DIGIT_2);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("7.Confirm that no notifications pop up");
        AssertKt.assertPresent(Locator.byText("Choose screen lock"));

        settingsPO.Restoreemptypassword("2222");
    }

    /**	Password:Remove the existing Password
     * Creat alphanumeric password for panel with at least 4 characters
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock
     * Type correct password and jump to "Choose screen lock" page
     * Click "None"
     * Select "YES, REMOVE" to remove existing password
     * Confirm that the current screen lock is "None"
     * by yangwenjin
     */
    @Test
    public void Password83670(){
        TestRail.setTestRailId("83670");

        TestRail.addStepName("1.Creat alphanumeric password for panel with at least 4 characters");
        settingsPO.Creatpassword("password");
        TestRail.addStepName("2.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        TestRail.addStepName("3.Type correct password and jump to \"Choose screen lock\" page");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("4.Click \"None\"");
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='None']"));
        TestRail.addStepName("5.Select \"YES, REMOVE\" to remove existing password");
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='android:id/button1']"));
        TestRail.addStepName("6.Confirm that the current screen lock is \"None\"");
        AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[2]/android.support.v4.widget.DrawerLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.s" +
                "upport.v7.widget.RecyclerView[1]/android.widget.LinearLayout[1]/android.widget.LinearL" +
                "ayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[2]").getText().equals("None"),"not be None");

    }

    /** Password: Cancel remove the existing Password
     * Creat alphanumeric password for panel with at least 4 characters
     * Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock
     * Type correct password and jump to "Choose screen lock" page
     * Click "None"
     * Select "CANCEL" and back to "Choose screen lock" page
     * Confirm that the existing lock is not changed
     */
    @Test
    public void Password83671(){
        TestRail.setTestRailId("83671");

        TestRail.addStepName("1.Creat alphanumeric password for panel with at least 4 characters");
        settingsPO.Creatpassword("password");
        TestRail.addStepName("2.Unified Launchr -> locker -> Settings -> Secrity & Location -> Screen lock");
        settingsPO.openSecurity();
        ElementHelper.click(By.xpath("//android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]"));
        TestRail.addStepName("3.Type correct password and jump to \"Choose screen lock\" page");
        AppiumHelper.waitForSeconds(2);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("4.Click \"None\"");
        ElementHelper.click(By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='None']"));
        TestRail.addStepName("5.Select \"CANCEL\" and back to \"Choose screen lock\" page");
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='android:id/button2']"));
        TestRail.addStepName("6.Confirm that the existing lock is not changed");
        settingsPO.openSecurity();
        AssertKt.assertPresent(Locator.byText("Password"));

        settingsPO.Restoreemptypassword("1111");
    }

    /**Password:Correct and incorrect passwords will respond properly
     * Creat alphanumeric password for panel with at least 4 characters
     * Unified Launcher -> Users -> "Lock",
     * Confirm the screen can be locked successfully
     * When you type correct password to unlock screen
     * Confirm that the screen can be unlocked successfully
     * When you type wrong password to unlock screen
     * Fail to unlock screen, and message tip "Wrong password, please try again"
     */
    @Test
    public void Password83672(){
        TestRail.setTestRailId("83672");

        TestRail.addStepName("1.Creat alphanumeric password for panel with at least 4 characters");
        settingsPO.Creatpassword("password");
        TestRail.addStepName("2.Unified Launcher -> Users -> \"Lock\",");
        ScreenHelper.clickAt(0.5, 0.999);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text= 'Owner']"));
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='com.prometheanworld.unifiedlauncher:id/UserLockBtn']"));
        TestRail.addStepName("3.Confirm the screen can be locked successfully");
        AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanwo" +
                "rld.unifiedlauncher:id/pin_message']").getText().equals("Please enter your password"),"lock failed");
        TestRail.addStepName("4.When you type correct password to unlock screen");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("5.Confirm that the screen can be unlocked successfully");
        ScreenHelper.clickAt(0.5, 0.999);
        AssertKt.assertPresent(Locator.byText("Home"));
        TestRail.addStepName("6.When you type wrong password to unlock screen");
        ScreenHelper.clickAt(0.5, 0.999);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text= 'Owner']"));
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='com.prometheanworld.unifiedlauncher:id/UserLockBtn']"));
        AppiumHelper.waitForSeconds(2);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        TestRail.addStepName("7.Fail to unlock screen, and message tip \"Wrong password, please try again\"");
        AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanwo" +
                "rld.unifiedlauncher:id/pin_message']").getText().equals("Incorrect password. Please try again."),"enter failed");

        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        settingsPO.Restoreemptypassword("1111");
    }

    /**Password: Different accounts correspond to different passwords
     * Sign as Owner/User
     * Creat passcode A with PIN/Password for current account
     * Confirm that the passcode can lock or unlock panel successfully
     * Unified Launcher -> Owner/User profile -> SIGN OUT
     * Select different account to login, not Guest
     * Passcode B can be created for current account
     * Confirm that the passcode can lock or unlock panel successfully
     */
    @Test
    public void Password83675(){
        TestRail.setTestRailId("83675");

        TestRail.addStepName("1.Sign as Owner/User");
        TestRail.addStepName("2.Creat passcode A with PIN/Password for current account");
        settingsPO.Creatpassword("password");
        TestRail.addStepName("3.Confirm that the passcode can lock or unlock panel successfully");
        ScreenHelper.clickAt(0.5, 0.999);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text= 'Owner']"));
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='com.prometheanworld.unifiedlauncher:id/UserLockBtn']"));
        TestRail.addStepName("3.Confirm the screen can be locked successfully");
        AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanwo" +
                "rld.unifiedlauncher:id/pin_message']").getText().equals("Please enter your password"),"lock failed");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);
        ScreenHelper.clickAt(0.5, 0.999);
        AssertKt.assertPresent(By.xpath("//android.widget.Button[@resource-id='com.prometheanworld.unifiedlauncher:id/homeBtn']"));
        TestRail.addStepName("5.Unified Launcher -> Owner/User profile -> SIGN OUT");
        TestRail.addStepName("6.Select different account to login, not Guest");
        AppiumHelper.setUpNewUser();
        TestRail.addStepName("7.Passcode B can be created for current account");
        settingsPO.Creatpassword("pin");
        TestRail.addStepName("8.Confirm that the passcode can lock or unlock panel successfully");
        ScreenHelper.clickAt(0.5, 0.999);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text= 'New user']"));
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='com.prometheanworld.unifiedlauncher:id/UserLockBtn']"));
        AssertKt.assertTrue(Driver.getAndroidDriver().findElementByXPath("//android.widget.TextView[@resource-id='com.prometheanwo" +
                "rld.unifiedlauncher:id/pin_message']").getText().equals("Please enter your PIN"),"select wrong");
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.DIGIT_1);
        Driver.pressKey(AndroidKey.ENTER);

        systemPO.SignoutandSwitch("New user","Owner");
        settingsPO.Restoreemptypassword("1111");
        AppiumHelper.enterUsersSetting();
        AppiumHelper.deleteAllOtherUsers();
        systemPO.closeAppOnMenuBar("Users");


    }
}
