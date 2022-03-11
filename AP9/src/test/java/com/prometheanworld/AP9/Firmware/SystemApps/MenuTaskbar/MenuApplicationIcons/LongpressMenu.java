package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar.MenuApplicationIcons;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.model.AP9.IdentityPO;
import com.prometheanworld.appium.frame.model.AP9.SettingsPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

public class LongpressMenu extends BaseTest {
    private SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final IdentityPO identityPO = POFactory.getInstance(IdentityPO.class);

    /**
     * Author:lifeifei
     * Date 2021.12.24
     * C115041 Verify that clicking on "Restore" on the Long Press menu can show app window
     * step1 Launch one app from locker
     * step2 long press App Icon in taskbar greater than or equal to 1s and select click on "Minimize"
     * step3 Long press the icon again for more than 1s and click on "Restore"
     */
    @Test(groups= {"P1"})
    private void C115041VerifyMinimizeAndRestoreWorkNormally() throws InterruptedException {
        TestRail.setTestRailId("115041");

        TestRail.addStepName("step1.Launch one app from locker");
        String app = "Gallery";
        systemPO.startAppFromUnifiedLauncher(app);

        TestRail.addStepName("long press App Icon in taskbar greater than or equal to 1s and select click on 'Minimize'");
        systemPO.startMenuBar();
        systemPO.clickLongPressMenu(app, LongPressMenu.Minimize, true);
        AssertKt.assertNotPresent(By.id("android:id/maximize_window"));

        TestRail.addStepName("step3 Long press the icon again for more than 1s and click on \"Restore\"");
        systemPO.clickLongPressMenu(app, LongPressMenu.Restore, true);
        AssertKt.assertPresent(By.id("android:id/maximize_window"));
    }

    /**
     * Author:lifeifei
     * Date 2021.12.28
     * C115039 Verify that clicking on "Minimize" on the Long Press menu can hide app window
     * step1 Launch one app from locker
     * step2 long press App Icon in taskbar greater than or equal to 1s and select click on "Minimize"
     * step3 Check that The app moves to the bottom to hide, the app is not displayed in panel, and there is still an app icon in taskbar
     */
    @Test(groups= {"P1"})
    private void C115039VerifyappWindowHidderAfterMinimizeClicked() throws InterruptedException {
        TestRail.setTestRailId("115039");

        TestRail.addStepName("step1.Launch one app from locker");
        systemPO.startLocker();
        String[] AppNamesInLocker = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.locker:id/app_txt']");
        systemPO.startAppFromUnifiedLauncher(AppNamesInLocker[6]);

        TestRail.addStepName("long press App Icon in taskbar greater than or equal to 1s and select click on \"Minimize\"");
        systemPO.startMenuBar();
        systemPO.clickLongPressMenu(AppNamesInLocker[6], LongPressMenu.Minimize, true);

        TestRail.addStepName("Check that The app moves to the bottom to hide, the app is not displayed in panel, and there is still an app icon in taskbar");
        AssertKt.assertNotPresent(By.id("android:id/maximize_window"));
        systemPO.startMenuBar();
        String[] AppNamesInMenu = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        Assert.assertTrue(Arrays.asList(AppNamesInMenu).contains(AppNamesInLocker[6]),"The Minimized app is closed");
    }

    @AfterMethod(alwaysRun = true)
    //close and unpin all of the app in the menubar except "Screen Capture" and "Annotate"  before run test cases
    public void after_method() {
        systemPO.startMenuBar();
        String[] AppNamesMenubar = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        System.out.println(Arrays.toString(AppNamesMenubar));
        for (String str : AppNamesMenubar) {
            systemPO.startMenuBar();
            if (str.equals("Screen Capture")||str.equals("Annotate")){
                continue;
            }
            else {
                systemPO.clickLongPressMenu(str, LongPressMenu.KeepInMenu, false);
            }
        }
        systemPO.startMenuBar();
        AppNamesMenubar = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        for (String str : AppNamesMenubar) {
            if (str.equals("Screen Capture") || str.equals("Annotate")) {
                continue;
            } else {
                systemPO.closeAppOnMenuBar(str);
            }
        }
    }
		
    /** zhangkaichun
     * C115183 Verify that app can be opened automatically after starting up when panel is in Guest mode if selecting "Open at Start up"
     * When panel is in Guest mode, long press App Icon in taskbar greater than or equal to 1s
     * select "Open  at start up"
     * restart panel;----Open at strat up is checked,any application marked as Open at start up automatically open
     */
    @Test(groups= {"P1"})
    private void C115183VerifyThatAppCanBeOpenedAutomaticallyAfterAtartingUpWhenPanelIsInGuestModeIfSelectingOpenatStartup() {
        TestRail.setTestRailId("115183");

        TestRail.addStepName("hen panel is in Guest mode, long press App Icon in taskbar greater than or equal to 1s,select 'Open  at start up'");
        String app = "Settings";
        systemPO.startAppFromUnifiedLauncher(app);
        systemPO.clickLongPressMenu(app, LongPressMenu.OpenAtStartUp,true);
        AppiumHelper.waitForSeconds(5);

        TestRail.addStepName("restart panel;Open at strat up is checked,any application marked as Open at start up automatically open");
        AppiumHelper.rebootPanel();
        AppiumHelper.clickAt(0.8,0.8);
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+app+"' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        ElementHelper.longPress(By.xpath("//*[@text='"+app+"']"),1);
        Boolean checkedEle = ElementHelper.isPresent(By.xpath("//*[@text='" + LongPressMenu.OpenAtStartUp.menuName + "']/../android.widget.ImageView"));
        Assert.assertTrue(checkedEle,"Open at strat up is checked");
        AppiumHelper.clickAt(0.8,0.8);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Settings' and @package='com.android.settings']"));

        systemPO.clickLongPressMenu(app, LongPressMenu.OpenAtStartUp,false);
        systemPO.closeAppOnMenuBar(app);
    }

    /** zhangkaichun
     * C115189 Verify that app can be opened automatically after user signs in of panel if selecting "Open at Log In"
     * When panel is in User mode, long press App Icon in taskbar greater than or equal to 1s
     * select "Open  at log in"
     * user signs out panel
     * user signs in panel;----any application marked as Open at Login automatically open,any application unmarked as Open at login not automatically open
     */
    @Test(groups= {"P1"})
    private void C115189VerifyThatAppCanBeOpenedAutomaticallyAfterUserSignsInOfPanelIfSelectingOpenatLogIn() {
        TestRail.setTestRailId("115189");

        TestRail.addStepName("When panel is in User mode, long press App Icon in taskbar greater than or equal to 1s,select 'Open  at log in'");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
        String app = "Settings";
        systemPO.startAppFromUnifiedLauncher(app);
        systemPO.clickLongPressMenu(app, LongPressMenu.OpenAtLogIn,true);
        systemPO.closeAppOnMenuBar(app);

        TestRail.addStepName("user signs out panel");
        systemPO.signOut(false,false);

        TestRail.addStepName("user signs in panel;any application marked as Open at Login automatically open,any application unmarked as Open at login not automatically open");
        systemPO.signIn();
        identityPO.signInWithEmail();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Settings' and @package='com.android.settings']"));
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+app+"' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        String[] titles = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        Assert.assertTrue(titles.length == 1 || titles.length == 3,"any application unmarked as Open at login not automatically open");

        systemPO.clickLongPressMenu(app, LongPressMenu.OpenAtLogIn,false);
        systemPO.closeAppOnMenuBar(app);
        systemPO.signOut(false,false);
    }

    /**
     * Author:lifeifei
     * Date 2021.12.24
     * C115043 Verify that pinned app icon still appears on the menu bar if clicking on "close" on the Long Press menu
     * step1.Launch one app from locker
     * stet2.Check keep in menu
     * step3.Close one apps at step1
     * step4:Check app won't disapper from menubar
     */
    @Test(groups= "P1")
    private void C115043VerifyPinnedAPPStillInMenubarAfterClosed() throws InterruptedException {
        TestRail.setTestRailId("115043");
        systemPO.startMenuBar();
        systemPO.startLocker();
        TestRail.addStepName("step1.Launch one app from locker");
        String[] AppNamesInLocker = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.locker:id/app_txt']");
        for (int i = 0; i < 1; i++) {
            systemPO.startAppFromUnifiedLauncher(AppNamesInLocker[i]);
            systemPO.startMenuBar();
            TestRail.addStepName("stet2.Check keep in menu");

            systemPO.clickLongPressMenu(AppNamesInLocker[i], LongPressMenu.KeepInMenu, true);
            Thread.sleep(3000);
            //Close both two apps at step1
            TestRail.addStepName("step3.Close one apps at step1");
            systemPO.closeAppOnMenuBar(AppNamesInLocker[i]);
        }
        TestRail.addStepName("step4:Check app won't disapper from menubar");
        systemPO.startMenuBar();
        String[] AppNamesInMenuBar = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        Assert.assertTrue(Arrays.asList(AppNamesInMenuBar).contains(AppNamesInLocker[0]), "The pinned app is closed");
    }

    /**
     * Author:lifeifei
     * Date 2021.12.24
     * C115044 Verify that unpinned app icon disappears on the menu bar if clicking on "close" on the Long Press menu
     * step1.Launch one app from locker
     * stet2.uncheck 'keep in menu'
     * step3.Close the apps at step1
     * step4:Check app  is disapper from menubar
     */
    @Test(groups= "P1")
    private void C115044VerifyUnPinnedAPPDisappearFromMenubarAfterClosed() throws InterruptedException {
        TestRail.setTestRailId("115044");
        systemPO.startMenuBar();
        systemPO.startLocker();
        TestRail.addStepName("step1.Launch one app from locker");
        String[] AppNamesInLocker = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.locker:id/app_txt']");
        systemPO.startAppFromUnifiedLauncher(AppNamesInLocker[0]);
        systemPO.startMenuBar();
        TestRail.addStepName("stet2.uncheck 'keep in menu'");
        systemPO.clickLongPressMenu(AppNamesInLocker[0], LongPressMenu.KeepInMenu, false);
        Thread.sleep(3000);
        //Close both two apps at step1
        TestRail.addStepName("step3.Close the apps at step1");
        systemPO.closeAppOnMenuBar(AppNamesInLocker[0]);
        TestRail.addStepName("step4:Check app  is disapper from menubar");
        systemPO.startMenuBar();
        String[] AppNamesInMenuBar = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        Assert.assertFalse(Arrays.asList(AppNamesInMenuBar).contains(AppNamesInLocker[0]), "The unpinned app should be disappear from menubar after closed");
    }

    /** zhangkaichun
     * C115040 Verify that "Minimize" option on the Long Press menu is replaced with "Restore" after minimizing app window
     * long press App Icon in taskbar greater than or equal to 1s;----Long press menu appears
     * click "Minimize";----The app moves to the bottom to hide,The app is not displayed in panel, and there is still an app icon in taskbar
     * Long press the icon again for more than 1s;----The Minimize button is displayed as "Restore" button
     */
    @Test(groups= {"P2"})
    private void C115040VerifyThatMinimizeOptionOnTheLongPressMenuIsReplacedWithRestoreAfterMinimizingAppWindow() {
        TestRail.setTestRailId("115040");

        TestRail.addStepName("long press App Icon in taskbar greater than or equal to 1s;Long press menu appears");
        String app = "Settings";
        systemPO.startAppFromUnifiedLauncher(app);
        ElementHelper.waitUntilPresent(By.xpath(settingsPO.eleSettingsTitle));

        TestRail.addStepName("click 'Minimize';The app moves to the bottom to hide,The app is not displayed in panel, and there is still an app icon in taskbar");
        systemPO.clickLongPressMenu(app,LongPressMenu.Minimize,true);
        ElementHelper.waitUntilNotPresent(By.xpath(settingsPO.eleSettingsTitle));
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+app+"' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));

        TestRail.addStepName("Long press the icon again for more than 1s;The Minimize button is displayed as 'Restore' button");
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='"+app+"']"),1);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+LongPressMenu.Restore.menuName+"']"));
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='"+LongPressMenu.Minimize.menuName+"']"));

        systemPO.closeAppOnMenuBar(app);
    }

    /** zhangkaichun
     * C115045/C115047 Verify that "close" doesn't display on the Long Press menu of pinned app icon any more
     * long press App Icon in taskbar greater than or equal to 1s
     * click "Keep in Menu";
     * click "close";
     * long press App Icon in taskbar greater than or equal to 1s;----"Close" is not displayed on the menu bar,"Keep in Menu" is checked
     * (C115047) Uncheck "Keep in Menu";----The application icon is removed from the taskbar
     */
    @Test(groups= {"P2"})
    private void C115045C115047VerifyThatCloseDoesntDisplayOnTheLongPressMenuOfPinnedAppIconAnyMore() {
        TestRail.setTestRailId("115045,115047");

        TestRail.addStepName("long press App Icon in taskbar greater than or equal to 1s,click 'Keep in Menu',click 'close'");
        String app = "Settings";
        systemPO.startAppFromUnifiedLauncher(app);
        systemPO.clickLongPressMenu(app,LongPressMenu.KeepInMenu,true);
        systemPO.clickLongPressMenu(app,LongPressMenu.Close,true);

        TestRail.addStepName("long press App Icon in taskbar greater than or equal to 1s;'Close' is not displayed on the menu bar,'Keep in Menu' is checked");
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='"+app+"']"),1);
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='"+LongPressMenu.Close.menuName+"']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + LongPressMenu.KeepInMenu.menuName + "']/../android.widget.ImageView"));

        systemPO.clickLongPressMenu(app,LongPressMenu.KeepInMenu,false);
        systemPO.startMenuBar();
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='"+app+"' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
    }

    /**
     * C115186 Verify that app can't be opened automatically after starting up when panel is in Guest mode if unselecting "Open at Start up"
     * Steps:
     * 1. When panel is in Guest mode, long press App Icon in taskbar greater than or equal to 1s
     * 2. unselect "Open  at start up"
     * 3. restart panel;----Open at strat up is unchecked,any application unmarked as Open at start up not automatically open
     * Author: zhang kaichun
     */
    @Test(groups= {"P2"})
    private void C115186VerifyThatAppCanotBeOpenedAutomaticallyAfterStartingUpWhenPanelIsInGuestModeIfUnselectingOpenatStartup() {
        TestRail.setTestRailId("115186");

        TestRail.addStepName("restart panel;Open at strat up is unchecked,any application unmarked as Open at start up not automatically open");
        AppiumHelper.rebootPanel();
        String[] titles = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        Assert.assertTrue(titles.length == 2 ,"any application unmarked as Open at start up not automatically open");
        for (String s : titles) {
            systemPO.startMenuBar();
            AppiumHelper.longPressByElement(By.xpath("//*[@text='"+s+"']"));
            boolean isChecked = ElementHelper.isPresent(By.xpath("//*[@text='"+LongPressMenu.OpenAtStartUp.menuName+"']/../android.widget.ImageView"),1);
            Assert.assertFalse(isChecked,"Open at strat up is unchecked");
        }
    }

    /**
     * C115188 Verify that app icon will not appear on the menu bar if user signs out panel
     * Steps:
     * 1. When panel is in User mode, press and hold a newly opened application icon in the taskbar for greater than or equal to 1s
     * 2. select "Open  at login";----Open at login is checked
     * 3. user signs out of panel;----applications close
     * Author: zhang kaichun
     */
    @Test(groups= {"P2"})
    private void C115188VerifyThatAppIconWillNotAppearOnTheMenuBarIfUserSignsOutPanel() {
        TestRail.setTestRailId("115188");

        TestRail.addStepName("When panel is in User mode, press and hold a newly opened application icon in the taskbar for greater than or equal to 1s,select 'Open  at login'");
        systemPO.setPropMdmEnvByFile("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
        String app = "Chromium";
        systemPO.startAppFromUnifiedLauncher(app);
        systemPO.clickLongPressMenu(app, LongPressMenu.OpenAtLogIn,true);

        TestRail.addStepName("user signs out of panel;applications close");
        systemPO.signOut(false,false);
        systemPO.startMenuBar();
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='"+app+"']"));

        systemPO.signIn();
        identityPO.signInWithEmail();
        systemPO.clickLongPressMenu(app, LongPressMenu.OpenAtLogIn,false);
        systemPO.signOut(false,false);
        systemPO.startMenuBar();
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='"+app+"']"));
    }

    /**
     * C113250 Verify that long press menu appears for pinned closed apps
     * Steps:
     * 1. Click on Applications icon;----Application Locker should open
     * 2. Press and hold "Settings" app icon;----Icon should be draggable
     * 3. Drag the Settings icon to anywhere on Menu\Task bar;----Settings App icon should be pinned to task bar and retained
     * 4. Long press settings app icon for 1 second;----Application menu should appear with "Keep in Menu" (ticked), "Open app at log in"
     * Author: zhang kaichun
     */
    @Test(groups= {"P2"})
    private void C113250VerifyThatLongPressMenuAppearsForPinnedClosedApps() {
        TestRail.setTestRailId("113250");

        TestRail.addStepName("Drag the Settings icon to anywhere on Menu Task bar;Settings App icon should be pinned to task bar and retained");
        String app = "Settings";
        systemPO.startMenuBar();
        AndroidElement ele = AppiumHelper.findElementsByXPath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']").get(1);
        Point p = ele.getLocation();
        systemPO.startLocker();
        ElementHelper.longPressAndMoveTo(By.xpath("//*[@text='"+app+"']"),p);

        TestRail.addStepName("Long press settings app icon for 1 second;Application menu should appear with 'Keep in Menu' (ticked), 'Open app at log in'");
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='"+app+"']"),1);;
        int longPressMenuSize = AppiumHelper.findElementsByXPath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/title']").size();
        Assert.assertEquals(longPressMenuSize,2,"Application menu should appear with 'Keep in Menu' (ticked), 'Open app at log in'");
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + LongPressMenu.OpenAtStartUp.menuName + "']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + LongPressMenu.KeepInMenu.menuName + "']/../android.widget.ImageView"));
        ElementHelper.click(By.xpath("//*[@text='" + LongPressMenu.KeepInMenu.menuName + "']/../android.widget.ImageView"));
    }
}
