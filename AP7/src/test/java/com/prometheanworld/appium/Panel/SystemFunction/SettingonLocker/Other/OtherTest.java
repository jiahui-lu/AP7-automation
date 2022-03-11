package com.prometheanworld.appium.Panel.SystemFunction.SettingonLocker.Other;


import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.LockerPO;
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @description:
 * @author: yangwenjin(edward)
 * @time: 2021/11/15
 */
@Listeners({TestRailListener.class, TestStatusListener.class})
public class OtherTest extends BaseTest {

    private final LockerPO lockerPO = POFactory.getInstance(LockerPO.class);

    @Test
    public void Test_RemoveUnifiedLauncherFiles() {
        /** ADH-01 Description:Make sure UnifiedLauncher - Files is not displayed
         * 1. UnifiedLauncher --> Locker
         * 2.Confirm that "Files" option is not displayed on the left, removed since ota4
         * By Huangjie
         */
        TestRail.setTestRailId("83014");
        Log.info("-------------Test RemoveUnifiedLauncherFiles Start---------------");
        TestRail.addStepName("1. UnifiedLauncher --> Locker");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AppiumHelper.waitForSeconds(1);
        TestRail.addStepName("2.Confirm that 'Files' option is not displayed on the left, removed since ota4");
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/apps_drawer_tab"), 3);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/settings_drawer_tab"), 3);
        AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/files_drawer_tab"), 3);

        //click the lower-right corner point, use to hide bottom menu
        Log.info("click the lower-right corner point, use to hide bottom menu");
        AppiumHelper.waitForSeconds(2);

        ScreenHelper.clickAt(0.999, 0.999);
        Log.info("-------------Test RemoveUnifiedLauncherFiles End---------------");
    }

    @Test
    public void Test_RemoveUnifiedLauncherLock() {
        /** ADH-02 Description:Make sure UnifiedLauncher - Lock/Unlock is not displayed
         * 1. UnifiedLauncher --> Locker
         * 2.Confirm that "Lock/Unlock"  option is not displayed on the left,removed since ota4
         * By Huangjie
         */
        TestRail.setTestRailId("83015");
        Log.info("-------------Test RemoveUnifiedLauncherLock Start---------------");
        TestRail.addStepName("1. UnifiedLauncher --> Locker");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AppiumHelper.waitForSeconds(1);
        TestRail.addStepName(" 2.Confirm that 'Lock/Unlock'  option is not displayed on the left,removed since ota4");
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/apps_drawer_tab"), 3);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/settings_drawer_tab"), 3);
        AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/panel_locking_drawer_tab"), 3);
        //click the lower-right corner point, use to hide bottom menu
        Log.info("click the lower-right corner point, use to hide bottom menu");
        AppiumHelper.waitForSeconds(2);
        ScreenHelper.clickAt(0.999, 0.999);

        Log.info("-------------Test RemoveUnifiedLauncherLock End---------------");
    }

    /**
     * ADH-05 Description: AP7-4421 Click the info button, confirm that the information of panel displays correctly
     * 1. Unified Mune -> Locker-> System -> About
     * 2. Confirm that the information of panel displays correctly
     * Note: You can check the information in Update app
     */
    @Test
    public void Test_InfoButton() {
        TestRail.setTestRailId("83017");
        Log.info("-------------Test InfoButton Start---------------");
        Log.info("Open Update app and get panel information");
        TestRail.addStepName("1.check the information in Update app");
        CommonOperator.executeShellCommand("am start com.prometheanworld.otaupdateapp/com.prometheanworld.otaupdateapp.OTAActivity", "");
        AppiumHelper.waitForSeconds(5);
        String updateModel = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_model").getText();
        String updateMainboardFirmware = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_scaler_firmware").getText();
        String updateBezelFirmware = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_bezel_fw").getText();
        String updateAndroidOperatingSystem = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_android_os_ver").getText();
        String updateSerialNumber = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_serial_num").getText();
        CommonOperator.executeShellCommand("am force-stop com.prometheanworld.otaupdateapp", "");

    }

    @Test
    public void UserAccess1920() throws Exception {
        TestRail.setTestRailId("83546,83547");
        TestRail.addStepName("1.install NewbieTest.apk");
        String path = POFactory.getInstance(SettingsPO.class).getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        TestRail.addStepName("2.Unified menu -> Locker -> Newbie Test -> FUNCTION TEST -> Remote Controller Test -> Lock Access");
        lockerPO.selectLock();
        TestRail.addStepName("3.Unified menu -> Locker, confirm that Settings, Panel Management, and Update apps are invisible");
        TestRail.addStepName("4.Unified menu -> Locker -> Locker Settings, \"MORE SETTINGS\" button is invisible");
        TestRail.addStepName("5.Unified menu -> User icon, confirm that the setting icon is invisible");
        lockerPO.Confirmlock();
        TestRail.addStepName("6.Unified menu -> Locker -> Newbie Test -> FUNCTION TEST -> Remote Controller Test -> unLock ");
        lockerPO.selectUnlock();
        TestRail.addStepName("7.Unified menu -> Locker, confirm that Settings, Panel Management, and Update apps are visible");
        TestRail.addStepName("8.Unified menu -> Locker -> Locker Settings, \"MORE SETTINGS\" button is visible");
        TestRail.addStepName("9.Unified menu -> User icon, confirm that the setting icon is visible");
        lockerPO.Confirmunlock();
        lockerPO.UninstallAPP("Newbie Test");

    }

    @Test
    public void  UserAccess22() throws Exception {
        TestRail.setTestRailId("83549");
        TestRail.addStepName("1.install NewbieTest.apk");
        String path = POFactory.getInstance(SettingsPO.class).getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        TestRail.addStepName("2.Unified menu -> Locker -> Newbie Test -> FUNCTION TEST -> Remote Controller Test -> Lock Access");
        lockerPO.selectLock();
        TestRail.addStepName("3.Confirm that those apps are hidden in Locker and \"MORE SETTINGS\" is hidden in the Locker Settings");
        lockerPO.Confirmlock();
        TestRail.addStepName("4.Reboot panel");
        AppiumHelper.rebootPanel();
        TestRail.addStepName("5.Confirm that those apps are still hidden in Locker and \"MORE SETTINGS\" is hidden in the Locker Settings");
        ElementHelper.click(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.unifiedlauncher:id/user_top_row']/android.widget.RelativeLayout[1]/android.widget.ImageView[1]"));
        AppiumHelper.waitForSeconds(10);
        lockerPO.Confirmlock();
        lockerPO.UninstallAPP("Newbie Test");
    }

    @Test
    public void  UserAccess23() throws Exception {
        TestRail.setTestRailId("83550");
        TestRail.addStepName("1.install NewbieTest.apk");
        String path = POFactory.getInstance(SettingsPO.class).getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        AppiumHelper.AddNewUserNotsetup();
        TestRail.addStepName("2.Unified menu -> Locker -> Newbie Test -> FUNCTION TEST -> Remote Controller Test -> Lock Access");
        lockerPO.selectLock();
        TestRail.addStepName("3.Confirm that those apps are hidden in Locker and \"MORE SETTINGS\" is hidden in the Locker Settings");
        lockerPO.Confirmlock();
        try {
            AppiumHelper.showBottomMenu();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ElementHelper.click(By.xpath("//android.widget.TextView[@text= 'Owner']"));
        ElementHelper.click(By.xpath("//android.widget.Button[@text= 'SIGN OUT']"));
        TestRail.addStepName("4.Switch another user accounts");
        ElementHelper.click(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.unifiedlauncher:id/user_top_row']/android.widget.RelativeLayout[2]/android.widget.ImageView[1]"));
        AppiumHelper.waitForSeconds(10);
        Adb.uninstallApk("io.appium.settings");
        AppiumHelper.resetAppiumServices();
        TestRail.addStepName("5.Confirm that those apps are hidden in Locker and \"MORE SETTINGS\" is hidden in the Locker Settings");
        lockerPO.Confirmlock();
        try {
            AppiumHelper.showBottomMenu();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ElementHelper.click(By.xpath("//android.widget.TextView[@text= 'New user']"));
        ElementHelper.click(By.xpath("//android.widget.Button[@text= 'SIGN OUT']"));
        ElementHelper.click(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.unifiedlauncher:id/user_top_row']/android.widget.RelativeLayout[1]/android.widget.ImageView[1]"));
        AppiumHelper.waitForSeconds(13);
        AppiumHelper.resetAppiumServices();
    }

    @Test
    public void LargeModeTest(){
        TestRail.setTestRailId("122549");
        TestRail.addStepName("1.Unified menu -> Locker -> large mode");
        AppiumHelper.startAppFromBottomMenu("Locker");
        ElementHelper.click(By.xpath("/hierarchy/android.widget.LinearLayout/android.widget.RelativeLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.Button"));
        ElementHelper.click(By.xpath("//android.widget.Button[@text= 'LARGE MODE']"));
        AppiumHelper.waitForSeconds(10);
        TestRail.addStepName("2.confirm it has been large mode");
        AppiumHelper.startAppFromBottomMenu("Locker");
        ElementHelper.click(By.xpath("/hierarchy/android.widget.LinearLayout/android.widget.RelativeLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.Button"));
        AssertKt.assertPresent(By.xpath("//android.widget.Button[@text= 'NORMAL MODE']"), 3);
        ElementHelper.click(By.xpath("//android.widget.Button[@text= 'NORMAL MODE']"));
        AppiumHelper.waitForSeconds(10);
    }

    @Test
    public void MoreSettingTest(){
        TestRail.setTestRailId("122550");
        TestRail.addStepName("1.Unified menu -> Locker -> More setting");
        AppiumHelper.startAppFromBottomMenu("Locker");
        ElementHelper.click(By.xpath("/hierarchy/android.widget.LinearLayout/android.widget.RelativeLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.Button"));
        ElementHelper.click(By.xpath("//android.widget.Button[@resource-id='com.prometheanworld.unifiedlauncher:id/more_setting_button']"));
        TestRail.addStepName("2.confirm setting is opened");
        AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text= 'Users']"), 3);
        Driver.getAndroidDriver().findElementById("android:id/close_window").click();
    }

    @AfterMethod
    public void endtset(){
        AppiumHelper.startAppFromBottomMenu("Locker");
        if (CommonOperator.ScrollAndFindApp("Newbie Test",1)){
            lockerPO.UninstallAPP("Newbie Test");
        }
        ScreenHelper.clickAt(0.4,0.6);

    }

}


