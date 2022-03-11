package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar.MenuApplicationIcons;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.model.AP9.IdentityPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PinApps extends BaseTest {
    private final IdentityPO identityPO = POFactory.getInstance(IdentityPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final String RES_ID_LAUNCHER = "com.prometheanworld.unifiedlauncher:id";
    private final By applicationBy = Locator.byText("Applications");

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        systemPO.resetInitialEnvironment();
    }

    /**
     * C115036 Verify "Keep in menu" selected number limit(up to 3)
     * Application Icons are present in Task Bar
     * presses and holds on an application icon more than 1000ms;----Long press menu appears
     * click "Keep in Menu";----keep in menu is selected;The application icon is kept in the taskbar
     * Repeat step 1-3 to pin three app icons into menu bar;----There are 3 app icons pinned to the menu bar
     * presses and holds on the fourth app icon more than 1000ms
     * click "Keep in Menu";----"Keep in menu" is grey and can't be selected
     * Click on "Close";----The fourth app icon will be deleted from menu bar
     */
    @Test(groups= {"P0"})
    public void C115036VerifyKeepInMenuSelectedNumberLimitUpTo3() {
        TestRail.setTestRailId("115036");

        String[] appName = {"Settings", "Panel Management"};
        for (String a : appName) {
            systemPO.startAppFromUnifiedLauncher(a);
        }
        systemPO.clickLongPressMenu(appName[0], LongPressMenu.KeepInMenu, true);
        systemPO.startMenuBar();
        AppiumHelper.longPressByElement(By.xpath("//*[@text='" + appName[1] + "']"));
        MobileElement ele = ElementHelper.findElement(By.xpath("//*[@text='Keep in Menu']"));
        String clickAble = ele.getAttribute("enabled");
        Assert.assertEquals(clickAble,"false","Keep in menu is grey and can't be selected");
        ScreenHelper.clickAt(0.8,0.8);
        systemPO.clickLongPressMenu(appName[0],LongPressMenu.KeepInMenu,false);
        for (String a : appName) {
            systemPO.closeAppOnMenuBar(a);
        }
    }

    /**
     * 115191 Verify that only 3 apps are able to be pinned to menu bar by selecting ""Open at Start up" when panel is in Guest mode
     * When panel is in Guest mode and no app selects "Open at Start up"
     * long press App Icon in taskbar greater than or equal to 1s
     * Select "Open at Start up"
     * Repeat steps 2-3   three times
     * long press fourth App Icon in taskbar greater than or equal to 1s
     * Select "Open at Start up";----"Open at Start up" can't be selected;Guest can only select 3 apps icon to open at start up
     */
    @Test(groups= {"P0"})
    public void C115191VerifyThanOnly3AppsCanOpenAtStartUpWhenGuestMode() {
        TestRail.setTestRailId("115191");

        String[] appName = {"Settings", "Panel Management", "Chromium", "Adobe Acrobat"};
        for (String a : appName) {
            systemPO.startAppFromUnifiedLauncher(a);
        }
        for (int i=0;i< appName.length-1;i++) {
            systemPO.clickLongPressMenu(appName[i],LongPressMenu.OpenAtStartUp,true);
        }
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='"+appName[3]+"']"),1);
        MobileElement ele = ElementHelper.findElement(By.xpath(systemPO.eleOpenAtStartUp));
        String clickAble = ele.getAttribute("enabled");
        Assert.assertEquals(clickAble,"false","'Open at Start up' can't be selected");
        ScreenHelper.clickAt(0.8,0.8);
        for (int i=0;i< appName.length-1;i++) {
            systemPO.clickLongPressMenu(appName[i],LongPressMenu.OpenAtStartUp,false);
        }
        for (String a : appName) {
            systemPO.closeAppOnMenuBar(a);
        }
    }

    /**
     * C115193/C115187 Verify that only 3 apps are able to be pinned to menu bar by selecting "Open at Log In" when panel is in User mode
     * When panel is in User mode and no app in the task bar selects "Open at Log In"
     * long press App Icon in taskbar greater than or equal to 1s
     * select "Open at Log In"
     * Repeat steps 2-3   three times
     * long press App Icon in taskbar greater than or equal to 1s
     * long press App Icon in taskbar greater than or equal to 1s
     * select "Open at Log In";----"Open at Log In" can't be selected;Guest can only select 3 apps icon to open at start up
     * C115187 Verify that "Open at Login" displays on the Long Press menu when panel is in User mode
     * When panel is in User mode, press and hold a newly opened application icon in the taskbar for greater than or equal to 1s;----"Open at Login In" appears on long press menu
     */
    @Test(groups= {"P0"})
    public void C115193C115187VerifyThanOnly3AppsCanOpenAtLogInWhenUserMode() {
        TestRail.setTestRailId("115193,115187");

        TestRail.addStepName("Change to user mode");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail(true);

        TestRail.addStepName("Open 4 apps and set 3 apps Open at log in");
        String[] appName = {"Settings", "Panel Management", "Identity", "Adobe Acrobat"};
        for (String a : appName) {
            systemPO.startAppFromUnifiedLauncher(a);
        }
        for (int i=0;i< appName.length-1;i++) {
            systemPO.clickLongPressMenu(appName[i], LongPressMenu.OpenAtLogIn,true);
        }

        TestRail.addStepName("Check the 4 app 'Open at log in' can not be selected");
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='"+appName[3]+"']"),1);
        MobileElement ele = ElementHelper.findElement(By.xpath("//*[@text='"+LongPressMenu.OpenAtLogIn.menuName+"']"));
        String clickAble = ele.getAttribute("enabled");
        Assert.assertEquals(clickAble,"false","'Open at log in' can't be selected");

        TestRail.addStepName("Close all apps and change to guest mode");
        ScreenHelper.clickAt(0.8,0.8);
        for (int i=0;i< appName.length-1;i++) {
            systemPO.clickLongPressMenu(appName[i], LongPressMenu.OpenAtLogIn,false);
        }
        for (String a : appName) {
            systemPO.closeAppOnMenuBar(a);
        }
        systemPO.signOut(false,false);
        ScreenHelper.clickAt(0.8,0.8);
        systemPO.setPropMdmEnvByCmd("production");
    }

    /**
     * C112700 Verify that user can unpin the application icons pinned previously
     * As a teacher, I want keep a certain number of application icons in the Menu Bar for quick, easy access
     * Click on Applications icon  ---- Application Locker should open
     * Press and hold "Settings" app icon ---- Icon should be draggable
     * Drag the Settings icon to anywhere on Menu\Task bar ---- Settings App icon should be pinned to task bar and retained
     * Long press pinned Settings app icon, unselect "Keep in menu bar" ---- Settings app icon should get removed from task bar
     */
    @Test(groups= {"P1"})
    public void C112700VerifyThatUseCanUnpinTheApplicationIconsPinnedPreviously() {
        TestRail.setTestRailId("112700");
        TestRail.addStepName("1.Set taskbar to enable and visible.");
        systemPO.startMenuBar();

        TestRail.addStepName("2.Get the Application's location where we can pin.");
        Rect applicationRect = ElementHelper.getRect(applicationBy);

        TestRail.addStepName("3.Open Locker, and find test app in it.");
        String appName = "Settings";
        By appBy = Locator.byText(appName);
        By checkStateBy = Locator.byResourceId(RES_ID_LAUNCHER + "/checked_state");
        MobileElement app = systemPO.findAppFromApplications(appName);

        TestRail.addStepName("4.Drag the test app to the MenuBar.");
        ElementHelper.longPressAndMoveTo(app, new Point(applicationRect.getRight(), applicationRect.getTop()));

        TestRail.addStepName("5.Start the MenuBar and check test app's Keep-In-Menu status.");
        systemPO.startMenuBar();
        ElementHelper.longPress(appBy, 1);
        MobileElement ele = ElementHelper.findElement(checkStateBy);
        Assert.assertTrue(ele.isDisplayed(), "Keep in menu should be selected");
        ScreenHelper.clickAtPoint(applicationRect.getLeft() - 1, applicationRect.getTop());

        TestRail.addStepName("6.Unselect the test app's Keep-In-Menu status.");
        systemPO.clickLongPressMenu(appName,LongPressMenu.KeepInMenu,false);

        TestRail.addStepName("7.Start the MenuBar and check test app's Keep-In-Menu status.");
        systemPO.startMenuBar();
        if (ElementHelper.isPresent(appBy)) {
            ElementHelper.longPress(appBy, 1);
            Assert.expectThrows(NoSuchElementException.class, ()-> {
                ElementHelper.findElement(checkStateBy, 2);
            });
        }
        ScreenHelper.clickAt(0.8,0.5);
    }

    /**
     * C112704 Verify that application locker will close after app icon is pinned to task bar
     * As a teacher, I want keep a certain number of application icons in the Menu Bar for quick, easy access
     * Click on Applications icon  ---- Application Locker should open
     * Press and hold "Settings" app icon ---- Icon should be draggable
     * Drag the Settings icon to anywhere on Menu\Task bar ---- Settings App icon should be pinned to task bar and retained
     * Check Applications locker ---- Application locker closes
     */
    @Test(groups= {"P1"})
    public void C112704VerifyThatApplicationLockerWillCloseAfterAppIconIsPinnedToTaskBar() {
        TestRail.setTestRailId("112704");
        String appName = "Settings";
        By lockerBy = Locator.byResourceId("com.prometheanworld.locker:id/tv_title");

        TestRail.addStepName("1.Set taskbar to enable and visible.");
        systemPO.startMenuBar();

        TestRail.addStepName("2.Get the Application's location where we can pin.");
        Rect applicationRect = ElementHelper.getRect(applicationBy);

        TestRail.addStepName("3.Open Locker, and find test app in it.");
        MobileElement app = systemPO.findAppFromApplications(appName);

        TestRail.addStepName("4.Check Locker is open.");
        ElementHelper.waitUntilPresent(lockerBy);

        TestRail.addStepName("5.Drag the test app to the MenuBar.");
        ElementHelper.longPressAndMoveTo(app, new Point(applicationRect.getRight(), applicationRect.getTop()));

        TestRail.addStepName("6.Check Locker is closed.");
        ElementHelper.waitUntilNotPresent(lockerBy, 2);

        TestRail.addStepName("7.Reset the state: Unpin the test app.");
        systemPO.clickLongPressMenu(appName, LongPressMenu.KeepInMenu, false);
        ScreenHelper.clickAt(0.8,0.5);
    }

    /**
     * C112710 Verify that user can pin an open app icon by pressing "Keep in Menu"
     * Click on Applications icon  ---- Application Locker should open
     * Press Adobe app icon to open ---- Application should get open and application icon should appear on menu bar(open state i.e. Icon focused with a . below)
     * Press and hold an application icon in the Menu bar ---- Long Press dialog should appear above application icon with options Keep in Menu, Open app at log in, Minimize and Close
     * Press "Keep in Menu" option ---- Application icon should be retained in menu task bar in order in which it lies and state should be open
     */
    @Test(groups= {"P1"})
    public void C112710VerifyThatUserCanPinAnAppIconByPressingKeepInMenu() {
        TestRail.setTestRailId("112710");
        String appName = "Adobe Acrobat";
        By appBy = Locator.byText(appName);

        TestRail.addStepName("1.Set taskbar to enable and visible.");
        systemPO.startMenuBar();

        TestRail.addStepName("2.Open Locker, and find test app to open it.");
        systemPO.startAppFromUnifiedLauncher(appName);

        TestRail.addStepName("3.Long press the test app and keep it in Menu.");
        systemPO.clickLongPressMenu(appName, LongPressMenu.KeepInMenu, true);

        TestRail.addStepName("4.Close the test app.");
        systemPO.clickLongPressMenu(appName, LongPressMenu.Close, false);

        TestRail.addStepName("5.Check the test app still in Menu.");
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(appBy);

        TestRail.addStepName("6.Reset the state: Unpin the test app.");
        systemPO.clickLongPressMenu(appName, LongPressMenu.KeepInMenu, false);
        ScreenHelper.clickAt(0.8,0.5);
    }

    /**
     * C112705 Verify that pinned app location is retained for user subsequent logins
     * Click on Applications icon  ---- Application Locker should open
     * Press and hold "Settings" app icon ---- Icon should be draggable
     * Drag the Settings icon to anywhere on Menu\Task bar ---- Settings App icon should be pinned to task bar and retained
     * Sign out ---- User should get sign out from his session
     * Sign in again ---- All the previous apps pinned to menu bar should appear and their location should be retained
     */
    @Test(groups= {"P1"})
    public void C112705VerifyThatUserCanPinAnAppIconByPressingKeepInMenu() {
        TestRail.setTestRailId("112705");
        String appName = "Settings";
        By appBy = Locator.byText(appName);

        TestRail.addStepName("1.Sign in to the Identity.");
        signInToIdentity();

        TestRail.addStepName("2.Get the Application's location where we can pin.");
        systemPO.startMenuBar();
        Rect applicationRect = ElementHelper.getRect(applicationBy);

        TestRail.addStepName("3.Open Locker, and find test app in it.");
        MobileElement app = systemPO.findAppFromApplications(appName);

        TestRail.addStepName("4.Drag the test app to the MenuBar.");
        ElementHelper.longPressAndMoveTo(app, new Point(applicationRect.getRight(), applicationRect.getTop()));

        TestRail.addStepName("5.Check the test app is on the Menu.");
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(appBy);

        TestRail.addStepName("6.Sign out.");
        systemPO.signOut(false, false);

        TestRail.addStepName("7.Sign in again.");
        signInToIdentity();

        TestRail.addStepName("8.Check the test app is still on the Menu.");
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(appBy);

        TestRail.addStepName("9.Reset the state: Unpin the test app and sign out.");
        systemPO.clickLongPressMenu(appName, LongPressMenu.KeepInMenu, false);
        systemPO.signOut(false, false);
        systemPO.setPropMdmEnvByCmd("production");
        ScreenHelper.clickAt(0.8,0.5);
    }

    /**
     * C92406 Verify the default icons in the menu
     * Set menu to enable and visible  ----  There are Applications, Annotate and Screen Capture icons on the taskbar by default
     * There is a  user icon on the left, click on its  ---  Pops up a user settings dialog
     * There is a network icon on the left, click on its  ---  Pops up a network settings dialog
     */
    @Test(groups= {"P0"})
    public void C92406VerifyDefaultIconsInMenuBar(){
        TestRail.setTestRailId("92406");
        TestRail.addStepName("Set menu to enable and visible");
        AppiumHelper.showTaskbar();
        ElementHelper.waitUntilPresent(Locator.byText("Applications"), 3);
        ElementHelper.waitUntilPresent(Locator.byText("Annotate"), 3);
        ElementHelper.waitUntilPresent(Locator.byText("Screen Capture"), 3);

        TestRail.addStepName("There is a user icon on the left, click on its");
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/user_button"), 3);
        ElementHelper.findElement(Locator.byResourceId("id/user_layout")).click();
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/user_root_layout"), 3);

        AppiumHelper.showTaskbar();
        TestRail.addStepName("There is a network icon on the left, click on its");
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/network_button"), 3);
        ElementHelper.findElement(Locator.byResourceId("id/network_layout")).click();
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/network_root_layout"), 3);

        // Tap outside to hide menu
        ScreenHelper.clickAt(Location.CENTER_TOP);
    }

    private void signInToIdentity() {
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
    }

}
