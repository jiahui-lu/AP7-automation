package com.prometheanworld.appium.Panel.App;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.LockerPO;
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.AP7.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;



/**
 * @description:
 * @author: EdwardYang
 * @time: 2021/12/3
 */
@Listeners({TestRailListener.class, TestStatusListener.class})
public class GooglePlayStore extends BaseTest{

    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final LockerPO lockerPO = POFactory.getInstance(LockerPO.class);

    @Test
    /** Google Play:Hide and switch user
     * 1. Use NewbieTest app to hide Google Play (https://wiki.prometheanjira.com/display/DEL/How+to+enable+Google+Play)
     * 2. Android Settings --> Apps --> App info, confirm that Google apps are invisible
     * 3. Go to Android Settings page, confirm that the "Google" option is invisible
     * 4. Android Settings --> Security, confirm that the "Google Play Protect" option is invisible
     * 5. Unified Menu --> Locker --> Apps, confirm that Google Play is invisible
     * 6. Sign in as Owner, User and Guest, confirm that Google Play is invisible
     * By yangwenjin
     */
    public void Google123379() throws Exception {
        TestRail.setTestRailId("123379");

        TestRail.addStepName("1.install NewbieTest.apk and hide Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(false);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("false"), "is show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Android Settings --> Apps --> App info, confirm that Google apps are invisible");
        settingsPO.openAppinfo();
        AssertKt.assertFalse(CommonOperator.scrollAndFind("android:id/list", "Google Play Store"), "Google apps are visible");
        TestRail.addStepName("3. Go to Android Settings page, confirm that the \"Google\" option is invisible");
        settingsPO.openLockerSetting();
        AssertKt.assertFalse(CommonOperator.scrollAndFind("android:id/list", "Google"), "Google are visible");
        TestRail.addStepName("4.Android Settings --> Security, confirm that the \"Google Play Protect\" option is invisible");
        settingsPO.openSecurity();
        AssertKt.assertNotPresent(Locator.byTextContains("Google Play Protect"), 5);
        TestRail.addStepName("5.Unified Menu --> Locker --> Apps, confirm that Google Play is invisible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertFalse(CommonOperator.ScrollAndFindApp("Play Store",0),"can find Play Store");
        lockerPO.UninstallAPP("Newbie Test");
    }

    @Test
    /** GOP Control:Hide in owner and switch user and guest
     * 1. Sign in as Owner and Use NewbieTest app to hide Google Play (https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815)
     * 2. Unified Menu --> Locker --> Apps, confirm that Google Play is invisible
     * 3. Sign in as User, confirm that Google Play is invisible
     * 4. Sign in as Guest, confirm that Google Play is invisible
     * By yangwenjin
     */
    public void Google83189() throws Exception {
        TestRail.setTestRailId("83189");

        AppiumHelper.AddNewUserNotsetup();
        TestRail.addStepName("1.install NewbieTest.apk and hide Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(false);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("false"), "is show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Unified Menu --> Locker --> Apps, confirm that Google Play is invisible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertFalse(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        TestRail.addStepName("3.Sign in as User, confirm that Google Play is invisible");
        systemPO.SignoutandSwitch("Owner","New user");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertFalse(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        TestRail.addStepName("4.Sign in as Guest, confirm that Google Play is invisible");
        systemPO.SignoutandSwitch("New user", "Guest");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertFalse(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        systemPO.SignoutandSwitch("Guest","Owner");
        AppiumHelper.waitForSeconds(10);
        lockerPO.UninstallAPP("Newbie Test");

    }

    @Test
    /** GOP Control: Hide in owner and switch user to Enable
     * 1.Sign in as Owner and Use NewbieTest app to hide Google Play (https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815)
     * 2.Sign in as User,and Use NewbieTest app to enable Google Play ,confirm that Google Play is visible
     * By yangwenjin
     */
    public void Google123378() throws Exception {
        TestRail.setTestRailId("123378");

        AppiumHelper.AddNewUserNotsetup();
        TestRail.addStepName("1.install NewbieTest.apk and hide Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(false);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("false"), "is show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Sign in as User,and Use NewbieTest app to enable Google Play ,confirm that Google Play is visible");
        systemPO.SignoutandSwitch("Owner","New user");
        lockerPO.selectgoogleplay(true);
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        systemPO.SignoutandSwitch("New user","Owner");
        lockerPO.UninstallAPP("Newbie Test");

    }

    @Test
    /** GOP Control: Enable in Owner
     * 1.Sign in as Owner and Download NewbieTest apk from:https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815,install by adb command: adb install -r ~/Downloads/NewbieTest.apk
     * 2. Unified menu -> Locker -> Newbie Test -> FUNCTION TEST ->Google Play Settings Test ->SHOW GOOGLE PLAY
     * 3. Android Settings --> Apps --> App info, confirm that the two Google apps(Google Play Store,Google Play services) are still invisible
     * 4. Go to Android Settings page, confirm that the "Google" option is visible
     * 5. Android Settings --> Security, confirm that the "Google Play Protect" option is visible
     * 6. Unified Menu --> Locker --> Apps, confirm that Google Play is visible
     * By yangwenjin
     */
    public void Google83190() throws Exception {
        TestRail.setTestRailId("83190");

        TestRail.addStepName("1.install NewbieTest.apk and Show Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(true);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("true"), "is not show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Android Settings --> Apps --> App info, confirm that the two Google apps(Google Play Store,Google Play services) are still invisible");
        settingsPO.openAppinfo();
        AssertKt.assertFalse(CommonOperator.scrollAndFind("android:id/list", "Google Play Store"), "Google Play Store is visible");
        AssertKt.assertFalse(CommonOperator.scrollAndFind("android:id/list", "Google Play services"), "Google Play services is  visible");
        TestRail.addStepName("3.Go to Android Settings page, confirm that the \"Google\" option is visible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndClickApp("Settings");
        AssertKt.assertFalse(CommonOperator.scrollAndFind("android:id/list", "Google"), "Google are visible");
        TestRail.addStepName("4.Android Settings --> Security, confirm that the \"Google Play Protect\" option is invisible");
        settingsPO.openSecurity();
        AssertKt.assertPresent(Locator.byTextContains("Google Play Protect"), 5);
        TestRail.addStepName("5.Unified Menu --> Locker --> Apps, confirm that Google Play is visible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        lockerPO.UninstallAPP("Newbie Test");
    }

    @Test
    /** GOP Control: Enable in owner and switch user and guest
     * 1. Sign in as Owner and Download NewbieTest apk from:https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815,install by adb command: adb install -r ~/Downloads/NewbieTest.apk
     * 2. Unified menu -> Locker -> Newbie Test -> FUNCTION TEST ->Google Play Settings Test ->SHOW GOOGLE PLAY
     * 3. Sign in as user, confirm that Google Play is visible
     * 4. Sign in as Guest, confirm that Google Play is visible
     * By yangwenjin
     */
    public void Google122380() throws Exception {
        TestRail.setTestRailId("123380");

        AppiumHelper.AddNewUserNotsetup();
        TestRail.addStepName("1.install NewbieTest.apk and Show Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(true);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("true"), "is not show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Sign in as user, confirm that Google Play is visible");
        systemPO.SignoutandSwitch("Owner","New user");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        TestRail.addStepName("3.Sign in as Guest, confirm that Google Play is visible");
        systemPO.SignoutandSwitch("New user","Guest");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        systemPO.SignoutandSwitch("Guest","Owner");
        lockerPO.UninstallAPP("Newbie Test");

    }

    @Test
    /** GOP Control: Enable in owner and switch user to Hide
     * 1.Sign in as Owner and Use NewbieTest app to enable Google Play (https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815)
     * 2.Sign in as User,and Use NewbieTest app to Hide Google Play ,confirm that Google Play is invisible
     * By yangwenjin
     */
    public void Google123377() throws Exception{
        TestRail.setTestRailId("123377");

        AppiumHelper.AddNewUserNotsetup();
        TestRail.addStepName("1.install NewbieTest.apk and Show Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(true);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("true"), "is not show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Sign in as User,and Use NewbieTest app to Hide Google Play ,confirm that Google Play is invisible");
        systemPO.SignoutandSwitch("Owner","New user");
        lockerPO.selectgoogleplay(false);
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertFalse(CommonOperator.ScrollAndFindApp("Play Store",0),"can find Play Store");
        systemPO.SignoutandSwitch("New user","Owner");
        lockerPO.UninstallAPP("Newbie Test");

    }

    @Test
    /** GOP Control: Enable in owner and Reboot
     * 1. Download NewbieTest apk from: https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815,install by adb command: adb install -r ~/Downloads/NewbieTest.apk
     * 2. Unified menu -> Locker -> Newbie Test -> FUNCTION TEST ->Google Play Settings Test ->SHOW GOOGLE PLAY
     * 3. Unified Menu --> Locker --> Apps, confirm that Google Play is visible
     * 4. Reboot panel
     * 5. Confirm that Google Play app and Google option are visible
     * By yangwenjin
     */
    public void Google83191() throws Exception {
        TestRail.setTestRailId("83191");

        TestRail.addStepName("1.install NewbieTest.apk and Show Google Play");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        lockerPO.selectgoogleplay(true);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("true"), "is not show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("2.Unified Menu --> Locker --> Apps, confirm that Google Play is visible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        TestRail.addStepName("3.Reboot panel");
        AppiumHelper.rebootPanel();
        if (ElementHelper.isPresent(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.unifiedlauncher:id/user_top_row']/android.widget.RelativeLayout[1]/android.widget.ImageView[1]"), 10)){
            AppiumHelper.clickOwnerInUserPage();
        }
        TestRail.addStepName("4.Confirm that Google Play app and Google option are visible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndClickApp("Settings");
        AssertKt.assertFalse(CommonOperator.scrollAndFind("android:id/list", "Google"), "Google are visible");
        settingsPO.openSecurity();
        AssertKt.assertPresent(Locator.byTextContains("Google Play Protect"), 5);
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        lockerPO.UninstallAPP("Newbie Test");

    }

    @Test
    /** GOP Control: Enable in user and Reboot
     * 1. Download NewbieTest apk from: https://prometheanworld.atlassian.net/wiki/pages/viewpageattachments.action?pageId=2445182815,install by adb command: adb install -r ~/Downloads/NewbieTest.apk
     * 2. sign as user, Unified menu -> Locker -> Newbie Test -> FUNCTION TEST ->Google Play Settings Test ->SHOW GOOGLE PLAY
     * 3. Unified Menu --> Locker --> Apps, confirm that Google Play is visible
     * 4. Reboot panel
     * 5. Confirm that Google Play app and Google option are visible
     * By yangwenjin
     */
    public void Google123381() throws Exception {
        TestRail.setTestRailId("123381");

        AppiumHelper.AddNewUserNotsetup();
        TestRail.addStepName("1.install NewbieTest.apk ");
        String path = settingsPO.getProjectPath();
        AppiumHelper.execToString("adb install " + path + "updateFile/NewbieTest/NewbieTest-release_prod_signed.apk");
        AppiumHelper.waitForSeconds(10);
        TestRail.addStepName("2.sign as user, Unified menu -> Locker -> Newbie Test -> FUNCTION TEST ->Google Play Settings Test ->SHOW GOOGLE PLAY");
        systemPO.SignoutandSwitch("Owner","New user");
        lockerPO.selectgoogleplay(true);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.nd.ssdktest:id/tv_gp_state")).getText().equals("true"), "is not show");
        systemPO.closeAppOnMenuBar("Newbie Test");
        TestRail.addStepName("3.Unified Menu --> Locker --> Apps, confirm that Google Play is visible");
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        TestRail.addStepName("4.Reboot panel");
        AppiumHelper.rebootPanel();
        TestRail.addStepName("5.Confirm that Google Play app and Google option are visible");
        if (ElementHelper.isPresent(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.unifiedlauncher:id/user_top_row']/android.widget.RelativeLayout[2]/android.widget.ImageView[1]"), 10)){
            ElementHelper.click(By.xpath("//android.widget.LinearLayout[@resource-id='com.prometheanworld.unifiedlauncher:id/user_top_row']/android.widget.RelativeLayout[2]/android.widget.ImageView[1]"));
        }
        AppiumHelper.resetAppiumServices();
        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertTrue(CommonOperator.ScrollAndFindApp("Play Store",0),"can not find Play Store");
        systemPO.SignoutandSwitch("New user","Owner");
        lockerPO.UninstallAPP("Newbie Test");

    }




}
