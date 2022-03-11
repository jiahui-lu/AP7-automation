package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar;

import com.nd.automation.core.action.Direction;
import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.AVIPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MenuFocus extends BaseTest {

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final AVIPO aviPO = POFactory.getInstance(AVIPO.class);

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        systemPO.resetInitialEnvironment();
    }

    /**
     * C113248 Verify that application menu disappears if user click on anywhere outside of app menu
     * Click\Press on Application icon;----Applications locker should open
     * Press Adobe app icon to open;----Application should get open and application icon should appear on menu bar(open state i.e. Icon focused with a . below)
     * Long Press  Adobe application icon;----Long Press menu appears
     * Press anywhere outside of Long Press menu but in menu bar;----Long Press menu should disappear
     * Press anywhere outside of Long Press menu and menu bar;----Long Press menu and menu bar should disappear together
     */
    @Test(groups= "P0")
    public void C113248VerifyThatApplicationMenuDisappearsIfUserClickOnAnywhereOutsideOfAppMenu() {
        TestRail.setTestRailId("113248");

        String appName = "Adobe Acrobat";
        TestRail.addStepName("Open " + appName);
        systemPO.startAppFromUnifiedLauncher(appName);

        TestRail.addStepName("Long Press Adobe application icon,Long Press menu appears");
        systemPO.startMenuBar();
        MobileElement eleApp = ElementHelper.findElement(By.xpath("//*[@text='" + appName + "']"));
        Point pApp = eleApp.getLocation();
        ElementHelper.longPress(By.xpath("//*[@text='"+appName+"']"),1);
        AssertKt.assertPresent(By.xpath("//*[@text='Close']"));

        TestRail.addStepName("Press anywhere outside of Long Press menu but in menu bar,Long Press menu should disappear");
        ScreenHelper.clickAtPoint(pApp.x-5, pApp.y);
        AssertKt.assertNotPresent(Locator.byText("Close"));

        TestRail.addStepName("Press anywhere outside of Long Press menu and menu bar,Long Press menu and menu bar should disappear together");
        ElementHelper.longPress(By.xpath("//*[@text='"+appName+"']"),1);
        Rect pClose = ElementHelper.getRect(By.xpath("//*[@text='Close']"));
        ScreenHelper.clickAtPoint(pClose.getLeft() - 50, pClose.getTop());
        AssertKt.assertNotPresent(Locator.byText( appName ));
        AssertKt.assertNotPresent(Locator.byText("Close"));

        systemPO.closeAppOnMenuBar(appName);
    }

    /**
     * C114952 Verify that users open one system app at one time by pressing Settings button on the remote control
     * Application Locker window opens/Source Switch window opens/Menu bar highlights Applications
     * Press on Settings button on remote;----Application Locker window closes/Source Switch window closes/Menu bar unhighlights Applications，AVI Settings window opens
     * Users press Settings button on remote again;----AVI Settings window closes
     */
    @Test(groups= "P1")
    public void C114952VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingSettingsButtonOnTheRemoteControl() {
        TestRail.setTestRailId("114952");

        TestRail.addStepName("Application Locker window opens");
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleApplicationsTab));

        TestRail.addStepName("Press on Settings button on remote.Application Locker window closes,AVI Settings window opens");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleApplicationsTab));
        String aviCheckEle = "//*[@text='Panel Settings' and @resource-id='com.prometheanworld.avisettings:id/app_label']";
        ElementHelper.waitUntilPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Users press Settings button on remote again.AVI Settings window closes");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleApplicationsTab));

        TestRail.addStepName("Source Switch window opens");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        String sourceCheckEle = "//*[@resource-id='com.prometheanworld.sources:id/source_list_item']";
        ElementHelper.waitUntilPresent(By.xpath(sourceCheckEle));

        TestRail.addStepName("Press on Settings button on remote.Source Switch window closes,AVI Settings window opens");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));
        ElementHelper.waitUntilPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Users press Settings button on remote again.AVI Settings window closes");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Menu bar highlights Applications");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        String menuCheckEle = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_locker']";
        ElementHelper.waitUntilPresent(By.xpath(menuCheckEle));

        TestRail.addStepName("Press on Settings button on remote.Menu bar unhighlights Applications,AVI Settings window opens");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));
        ElementHelper.waitUntilPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Users press Settings button on remote again.AVI Settings window closes");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));
    }

    /**
     * C114964 Verify that users open one system app at one time by pressing Menu button on the remote control
     * Application Locker window opens/Source Switch window opens/Menu bar highlights Applications
     * Press on Settings button on remote;----Application Locker window closes/Source Switch window closes/Menu bar unhighlights Applications，AVI Settings window opens
     * Users press Settings button on remote again;----AVI Settings window closes
     */
    @Test(groups= "P1")
    public void C114964VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingMenuButtonOnTheRemoteControl() {
        TestRail.setTestRailId("114964");

        TestRail.addStepName("AVI Settings window opens");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        String aviCheckEle = "//*[@text='Panel Settings' and @resource-id='com.prometheanworld.avisettings:id/app_label']";
        ElementHelper.waitUntilPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote.AVI Settings window closes,Menu activates, Menu highlights Application Locker");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        String menuCheckEle = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_locker']";
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));
        ElementHelper.waitUntilPresent(By.xpath(menuCheckEle));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote again.The Menu (task bar) disappears");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Source Switch window opens");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        String sourceCheckEle = "//*[@resource-id='com.prometheanworld.sources:id/source_list_item']";
        ElementHelper.waitUntilPresent(By.xpath(sourceCheckEle));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote.Source Switch window closes,Menu activates, Menu highlights Application Locker");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));
        ElementHelper.waitUntilPresent(By.xpath(menuCheckEle));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote again.The Menu (task bar) disappears");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));

        TestRail.addStepName("Application Locker window opens");
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleApplicationsTab));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote.Application Locker window closes,Menu activates, Menu highlights Application Locker");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleApplicationsTab));
        ElementHelper.waitUntilPresent(By.xpath(menuCheckEle));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote again.The Menu (task bar) disappears");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleApplicationsTab));
    }

    /**
     * C114967 Verify that users open one system app at one time by pressing Source button on the remote control
     * AVI Settings window opens/Application Locker window opens/Menu bar highlights Applications
     * Press Source Switch button on remote;----AVI Settings window closes/Application Locker window closes/Menu bar unhighlights Applications,Source Switch window opens
     * Press Source Switch button on remote again;----Source Switch window closed
     */
    @Test(groups= "P1")
    public void C114967VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingSourceButtonOnTheRemoteControl() {
        TestRail.setTestRailId("114964");

        TestRail.addStepName("AVI Settings window opens");
        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        String aviCheckEle = "//*[@text='Panel Settings' and @resource-id='com.prometheanworld.avisettings:id/app_label']";
        ElementHelper.waitUntilPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Press Source Switch button on remote.AVI Settings window closes,Source Switch window opens");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        String sourceCheckEle = "//*[@resource-id='com.prometheanworld.sources:id/source_list_item']";
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));
        ElementHelper.waitUntilPresent(By.xpath(sourceCheckEle));

        TestRail.addStepName("Press Source Switch button on remote again.Source Switch window closed");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(aviCheckEle));

        TestRail.addStepName("Application Locker window opens");
        systemPO.startLocker();
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleApplicationsTab));

        TestRail.addStepName("Press Source Switch button on remote.Application Locker window closes, Source Switch window opens");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleApplicationsTab));
        ElementHelper.waitUntilPresent(By.xpath(sourceCheckEle));

        TestRail.addStepName("Press Source Switch button on remote again.Source Switch window closed");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleApplicationsTab));
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));

        TestRail.addStepName("Menu bar highlights Applications");
        systemPO.startMenuBar();
        String menuCheckEle = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_locker']";
        ElementHelper.waitUntilPresent(By.xpath(menuCheckEle));

        TestRail.addStepName("Press Source Switch button on remote.Menu bar unhighlights Applications, Source Switch window opens");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));
        ElementHelper.waitUntilPresent(By.xpath(sourceCheckEle));

        TestRail.addStepName("Press Source Switch button on remote again.Source Switch window closed");
        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilNotPresent(By.xpath(menuCheckEle));
        ElementHelper.waitUntilNotPresent(By.xpath(sourceCheckEle));
    }

    /**
     * C113255 Verify that menu bar and user profile menu will disappear together if clicking on anywhere outside of them
     * Press the menu button on the panel;----Menu bar should appear
     * Click on anywhere outside of menu bar;----Menu bar will disappear
     * Press the menu button again;----Menu bar should reappear
     * Click on user profile button;----user profile menu will appear
     * Click on anywhere outside of menu bar and user profile menu;----Menu bar and user profile button will disappear together
     */
    @Test(groups= "P1")
    public void C113255VerifyThatMenuBarAndUserProfileMenuWillDisappearTogetherIfClickingOnAnywhereOutsideOfThem() {
        TestRail.setTestRailId("113255");

        TestRail.addStepName("Press the menu button on the panel;Menu bar should appear");
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleMenuApplications));

        TestRail.addStepName("Click on anywhere outside of menu bar;Menu bar will disappear");
        ScreenHelper.clickAt(0.8, 0.5);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleMenuApplications));

        TestRail.addStepName("Press the menu button again;Menu bar should reappear");
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleMenuApplications));

        TestRail.addStepName("Click on user profile button;user profile menu will appear");
        ElementHelper.click(By.xpath( systemPO.eleMenuUser));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Sign in to myPromethean']"));

        TestRail.addStepName("Click on anywhere outside of menu bar and user profile menu;Menu bar and user profile button will disappear together");
        ScreenHelper.clickAt(0.8, 0.5);
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Sign in to myPromethean']"));
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleMenuApplications));
    }

    /**
     * C115141 Verify that App list will close if users open one system app with remote buttons
     * 1 Open 5 apps;----The Open Apps icon appears on the task bar
     * 2 Press remote menu button;----The task bar appears and focus is on Application icon
     * 3 Press the right arrow to go to Open Apps icon;----Focus should be on Open Apps icon
     * 4 Press center button;----Apps list opens,Focus switches to Apps list
     * 5 Press remote settings button;----Apps list closed and AV Settings window opens,Focus switches to AV Settings app
     * 6 Repeat steps 2-4, press remote source button;----Apps list closed and Source window opens,Focus switches to Source window
     * 7 Repeat steps 2-4, press remote menu button;----Apps list closed and focus switches to Application icon
     */
    @Test(groups= "P1")
    public void C115141VerifyThatAppListWillCloseIfUsersOpenOneSystemAppWithRemoteButtons() {
        TestRail.setTestRailId("115141");

        TestRail.addStepName("Open 5 apps;The Open Apps icon appears on the task bar");
        String[] appNames = {"Settings", "Adobe Acrobat", "Files", "Panel Management", "Update"};
        for (String s : appNames) {
            systemPO.startAppFromUnifiedLauncher(s);
        }

        for (int i = 1; i < 4; i++) {
            TestRail.addStepName("Press remote menu button,Press the right arrow to go to Open Apps icon,Press center button;Apps list opens,Focus switches to Apps list");
            AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            systemPO.inputKeyevent(PrometheanKey.RemoteRight);
            systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
            String focusedXpath = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/apps_grid_container']/*[1]/*[@focused='true']";
            ElementHelper.waitUntilPresent(By.xpath(focusedXpath));

            if (i == 1) {
                TestRail.addStepName("Press remote settings button;User sign in menu closed and AV Settings window opens,Focus switches to AV Settings app");
                AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@resource-id='com.prometheanworld.avisettings:id/backlight_adjust' and @focused='true']"));
            } else if (i == 2) {
                TestRail.addStepName("Press remote source button;User sign in menu closed and Source window opens,Focus switches to Source window");
                AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Home']/..[@focused='true']"));
            } else {
                TestRail.addStepName("Press remote menu button;User sign in menu closed,Focus switches to Application icon");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));
            }
            ElementHelper.waitUntilNotPresent(By.xpath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/apps_grid_container']"));
        }
        for (int i = appNames.length; i > 0; i--) {
            systemPO.closeAppOnMenuBar(appNames[i - 1]);
        }
    }

    /**
     * C115147 Verify that User Profile will close if users open one system app with remote buttons
     * 1 Press remote menu button;----The task bar appears and focus is on Application icon
     * 2 Press the left arrow to go to User icon;----Focus should be on User icon
     * 3 Press center button;----User sign in menu should open,Focus switches to User sign in menu
     * 4 Press remote settings button;----User sign in menu closed and AV Settings window opens,Focus switches to AV Settings app
     * 5 Repeat steps 1-3, press remote source button;----User sign in menu closed and Source window opens,Focus switches to Source window
     * 6 Repeat steps 1-3, press remote menu button;----User sign in menu closed,Focus switches to Application icon
     */
    @Test(groups= "P1")
    public void C115147VerifyThatUserProfileWillCloseIfUsersOpenOneSystemAppWithRemoteButtons() {
        TestRail.setTestRailId("115147");

        for (int i = 1; i < 4; i++) {
            TestRail.addStepName("1.Press remote menu button,2.Press the left arrow to go to User icon,3.Press center button;User sign in menu should open,Focus switches to User sign in menu");
            AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            systemPO.inputKeyevent(PrometheanKey.RemoteLeft, 2);
            systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
            String signInTomyPrometheanXpath = "//*[@text='Sign in to myPromethean']";
            String focusedXpath = "//*[@text='Sign in to myPromethean']/..[@focused='true']";
            ElementHelper.waitUntilPresent(By.xpath(signInTomyPrometheanXpath));
            ElementHelper.waitUntilPresent(By.xpath(focusedXpath));

            if (i == 1) {
                TestRail.addStepName("Press remote settings button;User sign in menu closed and AV Settings window opens,Focus switches to AV Settings app");
                AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@resource-id='com.prometheanworld.avisettings:id/backlight_adjust' and @focused='true']"));
            } else if (i == 2) {
                TestRail.addStepName("Press remote source button;User sign in menu closed and Source window opens,Focus switches to Source window");
                AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Home']/..[@focused='true']"));
            } else {
                TestRail.addStepName("Press remote menu button;User sign in menu closed,Focus switches to Application icon");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));
            }
            ElementHelper.waitUntilNotPresent(By.xpath(signInTomyPrometheanXpath));
        }
        ScreenHelper.clickAt(0.8, 0.5);
    }

    /**
     * C115230 Verify that App icon menu will close if users open one system app with remote buttons
     * 1 Task bar -> click Applications icon to open locker, open apps from locker, such as open Settings app
     * 2 Press remote menu button;----The task bar appears and focus is on Application icon
     * 3 Press the right arrow to go to Settings icon;----Focus should be on Settings icon
     * 4 Long press the remote center button;----Pops up app icon menu and focus switches to app icon menu
     * 5 Press remote settings button;----App icon menu closed and AV Settings window opens,Focus switches to AV Settings app
     * 6 Repeat steps 2-4, press remote source button;----App icon menu closed and Source window opens,Focus switches to Source window
     * 7 Repeat steps 2-4, press remote menu button;----App icon menu closed,Focus switches to Application icon
     */
    @Test(groups= "P1")
    public void C115230VerifyThatAppIconWillCloseIfUsersOpenOneSystemAppWithRemoteButtons() {
        TestRail.setTestRailId("115230");

        TestRail.addStepName("Click Applications icon to open locker, open apps from locker, such as open Settings app");
        String appName = "Settings";
        systemPO.startAppFromUnifiedLauncher(appName);

        for (int i = 1; i < 4; i++) {
            TestRail.addStepName("Press remote menu button,Press the right arrow to go to Settings icon,Long press the remote center button;Pops up app icon menu and focus switches to app icon menu");
            AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            systemPO.inputKeyevent(PrometheanKey.RemoteRight, 3);
            systemPO.longPressRemoteCenter();
            String focusedXpath = "//*[@text='Keep in Menu']/../..[@focused='true']";
            ElementHelper.waitUntilPresent(By.xpath(focusedXpath));

            if (i == 1) {
                TestRail.addStepName("Press remote settings button;User sign in menu closed and AV Settings window opens,Focus switches to AV Settings app");
                AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@resource-id='com.prometheanworld.avisettings:id/backlight_adjust' and @focused='true']"));
            } else if (i == 2) {
                TestRail.addStepName("Press remote source button;User sign in menu closed and Source window opens,Focus switches to Source window");
                AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Home']/..[@focused='true']"));
            } else {
                TestRail.addStepName("Press remote menu button;User sign in menu closed,Focus switches to Application icon");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));
            }
            ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Keep in Menu']"));
        }
        systemPO.closeAppOnMenuBar(appName);
    }

    /**
     * C114966 Verify that users open one system app at one time by pressing Center Select button on the remote control
     * Steps:
     * 1 Press Menu (Promethean Flame) button on remote;----Menu activates, Menu highlights Application Locker
     * 2 Press Center Select button on remote;----Menu unhighlights Application Locker,Application Locker window opens and focus switches to locker
     * 3 Press Menu (Promethean Flame) button on remote again;----Locker disappears, and menu highlights to Application icon again
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C114966VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingCenterSelectButtonOnTheRemoteControl() {
        TestRail.setTestRailId("114966");

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote;Menu activates, Menu highlights Application Locker");
        AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));

        TestRail.addStepName("Press Center Select button on remote;Menu unhighlights Application Locker,Application Locker window opens and focus switches to locker");
        systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
        ElementHelper.waitUntilPresent(By.xpath("//*[@resource-id='com.prometheanworld.locker:id/apps_grid_container']/*[1][@focused='true']"));

        TestRail.addStepName("Press Menu (Promethean Flame) button on remote again;Locker disappears, and menu highlights to Application icon again");
        AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));
    }

    /**
     * C115415 Verify that menu app icon will close if users open one system app with remote buttons
     * Steps:
     * 1. Press remote menu button;----The task bar appears and focus is on Application icon
     * 2. Press the left arrow to go to User icon or other app icons;----Focus should be on User icon or other app icons
     * 3. Press remote settings button;----Menu unhighlights User icon or other app icons,AV Settings window opens,Focus switches to AV Settings app
     * 4. Repeat steps 1-2, press remote source button;----Menu unhighlights User icon or other app icons,Source window opens,Focus switches to Source window
     * 5. Repeat steps 1-2, press remote menu button;----Menu unhighlights User icon or other app icons,Menu bar hides
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115415VerifyThatMenuAppIconWillCloseIfUsersOpenOneSystemAppWithRemoteButtons(){
        TestRail.setTestRailId("115415");

        for (int i=1;i<=3;i++) {
            TestRail.addStepName("Press remote menu button;The task bar appears and focus is on Application icon");
            AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));

            TestRail.addStepName("Press the left arrow to go to User icon or other app icons;Focus should be on User icon or other app icons");
            systemPO.inputKeyevent(PrometheanKey.RemoteLeft, 2);
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Guest']/..[@focused='true']"));

            if(i==1) {
                TestRail.addStepName("Press remote settings button;Menu unhighlights User icon or other app icons,AV Settings window opens,Focus switches to AV Settings app");
                AppiumHelper.clickKey(PrometheanKey.Settings,PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@resource-id='com.prometheanworld.avisettings:id/backlight_adjust' and @focused='true']"));
            } else if(i==2) {
                TestRail.addStepName("Repeat steps 1-2, press remote source button;Menu unhighlights User icon or other app icons,Source window opens,Focus switches to Source window");
                AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilPresent(By.xpath("//*[@text='Home']/..[@focused='true']"));
            } else {
                TestRail.addStepName("Repeat steps 1-2, press remote menu button;Menu unhighlights User icon or other app icons,Menu bar hides");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Applications']"));
            }
        }
    }

    /**
     * C115793 Verify that users open one system app at one time by pressing Menu button on the panel
     * Steps:
     * 1 AVI Settings window opens/Source Switch window opens/Application Locker window opens and menu bar is invisible
     * 2 Press Menu (Promethean Flame) button on panel;----AVI Settings window closes/Source Switch window closes/Application Locker window closes,menu bar appears at the buttom
     * 3 Press Menu (Promethean Flame) button on panel again ;----AVI Settings window closes/Source Switch window closes/Application Locker window closes,menu bar disappears at the buttom
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115793VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingMenuButtonOnThePanel() {
        TestRail.setTestRailId("115793");

        String eleMenu = "//*[@text='Applications' and @package='com.prometheanworld.unifiedlauncher']";
        for (int i=1;i<=3;i++) {
            String eleCheck = "";
            if (i == 1) {
                TestRail.addStepName("AVI Settings window opens and menu bar is invisible");
                AppiumHelper.clickKey(PrometheanKey.Settings,PrometheanKeyboard.RemoteControl);
                eleCheck = aviPO.eleIdInteractionTab;
            } else if (i == 2) {
                TestRail.addStepName("Source Switch window opens and menu bar is invisible");
                AppiumHelper.clickKey(PrometheanKey.Sources,PrometheanKeyboard.ActivPanel);
                eleCheck = "//*[@resource-id='com.prometheanworld.sources:id/source_view_pager']";
            } else {
                TestRail.addStepName("Application Locker window opens and menu bar is invisible");
                systemPO.startLocker();
                eleCheck = "//*[@text='Applications' and @resource-id='com.prometheanworld.locker:id/tv_title']";
            }
            ElementHelper.waitUntilPresent(By.xpath(eleCheck));
            ElementHelper.waitUntilNotPresent(By.xpath(eleMenu));

            TestRail.addStepName("Press Menu (Promethean Flame) button on panel;menu bar appears at the buttom");
            AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
            ElementHelper.waitUntilPresent(By.xpath(eleMenu));

            TestRail.addStepName("Press Menu (Promethean Flame) button on panel again;AVI Settings window closes/Source Switch window closes/Application Locker window closes,menu bar disappears at the buttom");
            AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
            ElementHelper.waitUntilNotPresent(By.xpath(eleMenu));
            ElementHelper.waitUntilNotPresent(By.xpath(eleCheck));
        }
    }

    /**
     * C115794 Verify that users open one system app at one time by pressing Source button on the panel
     * Steps:
     * 1 AVI Settings window opens/Application Locker window opens/Menu bar is visible
     * 2 Press Source Switch button on the panel;----AVI Settings window closes/Application Locker window closes/Menu bar closed,Source Switch window opens
     * 3 Press Source Switch button on the panel again;----Source Switch window closed
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115794VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingSourceButtonOnThePanel() {
        TestRail.setTestRailId("115794");

        String eleSource = "//*[@resource-id='com.prometheanworld.sources:id/source_view_pager']";
        for (int i=1;i<=3;i++) {
            String eleCheck = "";
            if (i == 1) {
                TestRail.addStepName("AVI Settings window opens is visible");
                AppiumHelper.clickKey(PrometheanKey.Settings,PrometheanKeyboard.RemoteControl);
                eleCheck = aviPO.eleIdInteractionTab;
            } else if (i == 2) {
                TestRail.addStepName("Application Locker window opens is visible");
                systemPO.startLocker();
                eleCheck = "//*[@text='Applications' and @resource-id='com.prometheanworld.locker:id/tv_title']";
            } else {
                TestRail.addStepName("Menu bar is visible");
                AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
                eleCheck = "//*[@text='Applications' and @package='com.prometheanworld.unifiedlauncher']";
            }
            ElementHelper.waitUntilPresent(By.xpath(eleCheck));

            TestRail.addStepName("Press Source Switch button on the panel;Source Switch window opens");
            AppiumHelper.clickKey(PrometheanKey.Sources,PrometheanKeyboard.ActivPanel);
            ElementHelper.waitUntilPresent(By.xpath(eleSource));

            TestRail.addStepName("Press Source Switch button on the panel again;Source Switch window closed");
            AppiumHelper.clickKey(PrometheanKey.Sources,PrometheanKeyboard.ActivPanel);
            ElementHelper.waitUntilNotPresent(By.xpath(eleSource));
            ElementHelper.waitUntilNotPresent(By.xpath(eleCheck));
        }
    }

    /**
     * C115795 Verify that users open one system app at one time by pressing Settings button on the volume bar
     * Steps:
     * 1. Application Locker window opens/Source Switch window opens/Menu bar is visible
     * 2. Press on volume up/down button on the button;----Application Locker window closes/Source Switch window closes/Menu bar is invisible,Volume UI displays with AVI settings button
     * 3. User presses AVI settings button on screen;----Application Locker window closes/Source Switch window closes/Menu bar is still visible,Volume UI closes,AVI settings opens
     * 4. Press on volume up/down button on the button;----Volume UI displays with AVI settings button,AVI settings closes
     * 5. User presses AVI settings button on screen;----Volume UI closes,AVI settings closes
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115795VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingSettingsButtonOnTheVolumeBar() {
        TestRail.setTestRailId("115795");

        for (int i=1;i<=3;i++) {
            String eleCheck = "";
            if (i == 1) {
                TestRail.addStepName("Application Locker window opens");
                systemPO.startLocker();
                eleCheck = "//*[@text='Applications' and @resource-id='com.prometheanworld.locker:id/tv_title']";
            } else if (i == 2) {
                TestRail.addStepName("Source Switch window opens");
                AppiumHelper.clickKey(PrometheanKey.Sources,PrometheanKeyboard.ActivPanel);
                eleCheck = "//*[@resource-id='com.prometheanworld.sources:id/source_view_pager']";
            } else {
                TestRail.addStepName("Menu bar is visible");
                systemPO.startMenuBar();
                eleCheck = "//*[@text='Applications' and @package='com.prometheanworld.unifiedlauncher']";
            }
            ElementHelper.waitUntilPresent(By.xpath(eleCheck));

            for (int j=0;j<2;j++) {
                TestRail.addStepName("Press on volume up/down button on the button;Application Locker window closes/Source Switch window closes/Menu bar is invisible,Volume UI displays with AVI settings button");
                if (j == 0) {
                    AppiumHelper.clickKey(PrometheanKey.VolumeUp, PrometheanKeyboard.ActivPanel);
                } else {
                    AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.ActivPanel);
                }
                ElementHelper.waitUntilPresent(By.xpath(aviPO.eleVolumeUILaunchAVI));
                ElementHelper.waitUntilNotPresent(By.xpath(eleCheck));
                ElementHelper.waitUntilNotPresent(By.xpath(aviPO.eleIdInteractionTab));

                TestRail.addStepName("User presses AVI settings button on screen;Application Locker window closes/Source Switch window closes/Menu bar is still visible,Volume UI closes,AVI settings opens");
                ElementHelper.click(By.xpath(aviPO.eleVolumeUILaunchAVI));
                ElementHelper.waitUntilNotPresent(By.xpath(aviPO.eleVolumeUILaunchAVI));
                ElementHelper.waitUntilNotPresent(By.xpath(eleCheck));
                ElementHelper.waitUntilPresent(By.xpath(aviPO.eleIdInteractionTab));
            }
        }
        AppiumHelper.clickAt(0.8,0.8);
    }

    /**
     * C115916 Verify that users open one system app at one time by pressing Application icon on the menu bar
     * Steps:
     * 1 AVI Settings window opens/Source Switch window opens/Application Locker window opens
     * 2 User presses Application icon on Menu;----AVI Settings window closes/Source Switch window closes/Application Locker window closes
     * 3 User presses Application icon on Menu again;----Application Locker window opens
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115916VerifyThatUsersOpenOneSystemAppAtOneTimeByPressingApplicationIconOnTheMenuBar() {
        TestRail.setTestRailId("115916");

        for (int i=1;i<=3;i++) {
            String eleCheck = "";
            if (i == 1) {
                TestRail.addStepName("AVI Settings window opens opens");
                AppiumHelper.clickKey(PrometheanKey.Settings,PrometheanKeyboard.RemoteControl);
                eleCheck = aviPO.eleIdInteractionTab;
            } else if (i == 2) {
                TestRail.addStepName("Source Switch window opens");
                AppiumHelper.clickKey(PrometheanKey.Sources,PrometheanKeyboard.ActivPanel);
                eleCheck = "//*[@resource-id='com.prometheanworld.sources:id/source_view_pager']";
            } else {
                TestRail.addStepName("Application Locker window opens");
                systemPO.startLocker();
                eleCheck = "//*[@text='Applications' and @resource-id='com.prometheanworld.locker:id/tv_title']";
            }
            ElementHelper.waitUntilPresent(By.xpath(eleCheck));

            TestRail.addStepName("User presses Application icon on Menu;");
            AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications' and @package='com.prometheanworld.unifiedlauncher']"));

            TestRail.addStepName("User presses Application icon on Menu again;Application Locker window opens");
            AppiumHelper.findElementAndClick("Xpath","//*[@text='Applications']");
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications' and @resource-id='com.prometheanworld.locker:id/tv_title']"));
        }
    }

    /**
     * C115916 Verify that Menu sub layout will close if users open one system app with panel buttons
     * Steps:
     * 1. User presses Profile Icon on the menu bar;----Profile layout appears
     * 2. Click on menu button on the panel;----Profile layout and menu bar disappear together
     * 3. Click on menu button on the panel, long press app icons on the menu bar;----Long press app menu appears
     * 4. Click on menu button on the panel;----Long press app menu and menu bar disappear together
     * 5. Click on menu button on the panel, open more than 5 apps, click on open apps;----open apps menu opens
     * 6. Click on menu button on the panel;----Open apps menu and menu bar disappear together
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115933VerifyThatMenuSubLayoutWillCloseIfUsersOpenOneSystemAppWithPanelButtons() {
        TestRail.setTestRailId("115933");

        TestRail.addStepName("User presses Profile Icon on the menu bar;Profile layout appears");
        String tvTitle = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/tv_title']";
        systemPO.startMenuBar();
        ElementHelper.click(By.xpath(systemPO.eleMenuUser));
        ElementHelper.waitUntilPresent(By.xpath(tvTitle));

        TestRail.addStepName("Click on menu button on the panel;Profile layout and menu bar disappear together");
        AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilNotPresent(By.xpath(tvTitle));
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleMenuApplications));

        TestRail.addStepName("Click on menu button on the panel, long press app icons on the menu bar;Long press app menu appears");
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='Annotate']"),1);
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleOpenAtStartUp));

        TestRail.addStepName("Click on menu button on the panel;Long press app menu and menu bar disappear together");
        AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleOpenAtStartUp));
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleMenuApplications));

        TestRail.addStepName("Click on menu button on the panel, open more than 5 apps, click on open apps;open apps menu opens");
        String[] apps = {"Gallery","Settings","Chromium","Adobe Acrobat","Files"};
        for (String i : apps) {
            systemPO.startAppFromUnifiedLauncher(i);
        }
        systemPO.startOpenApps();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+apps[0]+"']"));

        TestRail.addStepName("Click on menu button on the panel;Open apps menu and menu bar disappear together");
        AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleOpenApps));
        ElementHelper.waitUntilNotPresent(By.xpath(systemPO.eleMenuApplications));

        for (int i=4;i>=0;i--) {
            systemPO.closeAppOnMenuBar(apps[i]);
        }
    }

    /**
     * C115140 Verify that the menu popup can only have one focus at a time
     * Steps:
     * 1. Open 5 apps, including Settings app;----The Open Apps icon appears
     * 2. Press remote menu button;----The task bar appears and focus is on Application icon
     * 3. Press the left arrow to go to User icon;----Focus should be on User icon
     * 4. Press center button;---User sign in menu should open,Focus switches to User sign in menu
     * 5. Press remote menu button;----User sign in menu closed,Focus should be on Applications icon
     * 6. Press center button;----Locker opens,Focus switches to locker, menu unhighlights Application icon
     * 7. Press remote menu button then press the right arrow to go to Open Apps icon;----Locker closed,Focus should be on Open Apps icon
     * 8. Press center button;----Apps list opens,Focus switches to Apps list
     * 9. Press remote menu button then press the left/right arrow to go to Settings app icon;----Apps list closed,Focus switches to Settings icon icon
     * 10. Long press the remote center button;----Pops up app icon menu,Focus switches to app icon menu
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115140VerifyThatTheMenuPopupCanOnlyHaveOneFocusAtaTime() {
        TestRail.setTestRailId("115140");

        TestRail.addStepName("1. Open 5 apps, including Settings app;The Open Apps icon appears");
        String[] apps = {"Update","Chromium","Settings","Adobe Acrobat","Files"};
        for (String s : apps) {
            systemPO.startAppFromUnifiedLauncher(s);
        }

        TestRail.addStepName("2. Press remote menu button;The task bar appears and focus is on Application icon");
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));

        TestRail.addStepName("3. Press the left arrow to go to User icon;Focus should be on User icon");
        systemPO.inputKeyevent(PrometheanKey.RemoteLeft, 2);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Guest']/..[@focused='true']"));

        TestRail.addStepName("4. Press center button;User sign in menu should open,Focus switches to User sign in menu");
        systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Sign in to myPromethean']/..[@focused='true']"));

        TestRail.addStepName("5. Press remote menu button;User sign in menu closed,Focus should be on Applications icon");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Sign in to myPromethean']"));

        TestRail.addStepName("6. Press center button;Locker opens,Focus switches to locker, menu unhighlights Application icon");
        systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Applications']/..[@focused='true']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@resource-id='com.prometheanworld.locker:id/apps_grid_container']/*[1][@focused='true']"));

        TestRail.addStepName("7. Press remote menu button then press the right arrow to go to Open Apps icon;Locker closed,Focus should be on Open Apps icon");
        AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
        systemPO.inputKeyevent(PrometheanKey.RemoteRight);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Open Apps']/..[@focused='true']"));

        TestRail.addStepName("8. Press center button;Apps list opens,Focus switches to Apps list");
        systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
        AndroidElement ele = AppiumHelper.findElement("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_layout'][1]");
        Assert.assertEquals(ele.getAttribute("focused"),"true","Apps list opens,Focus switches to Apps list");

        TestRail.addStepName("9. Press remote menu button then press the left/right arrow to go to Settings app icon;Apps list closed,Focus switches to Settings icon icon");
        AppiumHelper.clickKey(PrometheanKey.Menu,PrometheanKeyboard.RemoteControl);
        systemPO.inputKeyevent(PrometheanKey.RemoteRight,4);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Settings']/..[@focused='true']"));
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/apps_grid_container']"));

        TestRail.addStepName("10. Long press the remote center button;Pops up app icon menu,Focus switches to app icon menu");
        systemPO.longPressRemoteCenter();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Keep in Menu']/../..[@focused='true']"));

        for (int i=4;i>=0;i--) {
            systemPO.closeAppOnMenuBar(apps[i]);
        }
    }

    /**
     * C115426 Verify that the menu will lose focus (highlight) if users touches on the menu bar
     * Steps:
     * 1. Click remote menu button, menu highlights on Applications icon, touch anywhere on the menu bar;----lose foucs
     * 2. Click remote menu button, menu highlights on Application icon, use directional pad to focus on other app icons on menu, touch anywhere on the menu bar;----lose foucs
     * 3. Long press on an app icon to bring up app menu, touch anywhere on the menu bar;----lose foucs
     * 4. Click the user icon to bring up user profile menu, touch anywhere on the menu button;----lose foucs
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C115426VerifyThatTheMenuWillCloseFocusHighlightIfUsersTouchesOnTheMenuBar() {
        TestRail.setTestRailId("115426");

        for (int i=1;i<=4;i++) {
            String eleFocused = "";
            if (i == 1) {
                TestRail.addStepName("Click remote menu button, menu highlights on Applications icon, touch anywhere on the menu bar;lose foucs");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                eleFocused = "//*[@text='Applications']/..[@focused='true']";
            } else if (i == 2) {
                TestRail.addStepName("Click remote menu button, menu highlights on Application icon, use directional pad to focus on other app icons on menu, touch anywhere on the menu bar;lose foucs");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                systemPO.inputKeyevent(PrometheanKey.RemoteRight);
                eleFocused = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/toolbar']/*[2]/*[@focused='true']";
            } else if (i == 3) {
                TestRail.addStepName("Long press on an app icon to bring up app menu, touch anywhere on the menu bar;lose foucs");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                systemPO.inputKeyevent(PrometheanKey.RemoteRight);
                systemPO.longPressRemoteCenter();
                eleFocused = "//*[@text='Keep in Menu']/../..[@focused='true']";
            } else {
                TestRail.addStepName("Click the user icon to bring up user profile menu, touch anywhere on the menu button;lose foucs");
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                systemPO.inputKeyevent(PrometheanKey.RemoteLeft, 2);
                systemPO.inputKeyevent(PrometheanKey.RemoteCenter);
                eleFocused = "//*[@text='Sign in to myPromethean']/..[@focused='true']";
            }
            ElementHelper.waitUntilPresent(By.xpath(eleFocused));
            AppiumHelper.clickAt(0.4, 0.99);
            ElementHelper.waitUntilPresent(By.xpath(systemPO.eleMenuApplications));
            ElementHelper.waitUntilNotPresent(By.xpath(eleFocused));
        }
    }

    /**
     * C113249 Verify that long press menu stays active until users don't click outside the menu
     * Steps:
     * 1. Click\Press on Application icon;----Applications locker should open
     * 2. Press Adobe app icon to open;----Application should get open and application icon should appear on menu bar(open state i.e. Icon focused with a . below)
     * 3. Set menu to visible, long press on the app icon;----Long Press menu should appear
     * 4. Don't interact for 120s;----Menu bar and Long Press menu will hide automatically
     * Author: zhang kaichun
     */
    @Test(groups = "P2")
    public void C113249VerifyThatLongPressMenuStaysActiveUntilUsersDontClickOutsideTheMenu() {
        TestRail.setTestRailId("113249");

        TestRail.addStepName("Click Press on Application icon,Press Adobe app icon to open;Application should get open and application icon should appear on menu bar");
        String app = "Adobe Acrobat";
        systemPO.startAppFromUnifiedLauncher(app);

        TestRail.addStepName("Set menu to visible, long press on the app icon;Long Press menu should appear");
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='"+app+"']"),1);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+ LongPressMenu.OpenAtStartUp.menuName +"']"));

        TestRail.addStepName("Don't interact for 120s;Menu bar and Long Press menu will hide automatically");
        AppiumHelper.waitForSeconds(120);
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='"+ LongPressMenu.OpenAtStartUp.menuName +"']"));
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Applications' and @package='com.prometheanworld.unifiedlauncher']"));

        systemPO.closeAppOnMenuBar(app);
    }

}
