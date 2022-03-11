package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar.MenuApplicationIcons;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class OpenApps extends BaseTest {

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        systemPO.resetInitialEnvironment();
    }

    /**
     * C120350 Verify that Open Apps window should never have less than two apps in it
     * When menu bar has opened 4 non-pinned apps
     * User taps "Applications" icon
     * User taps an application icon in the Application window to open it
     * User taps Open Apps icon on the menu bar;----3 open app icons appear on the menu bar;2 open app icons appear on the Open Apps
     */
    @Test(groups= {"P0"})
    public void C120350VerifyThanOpenAppsShouldHaveMoreThanTwoAppsInIt() {
        TestRail.setTestRailId("120350,120352");

        TestRail.addStepName("Open 5 apps");
        String[] appName = {"Settings", "Chromium", "Adobe Acrobat", "Files", "Update"};
        for (String a : appName) {
            systemPO.startAppFromUnifiedLauncher(a);
        }

        TestRail.addStepName("Check there are 3 apps on the muenu bar");
        String checkAppsXpath = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']";
        systemPO.startMenuBar();
        int menuAppsCount = ElementHelper.findElements(By.xpath(checkAppsXpath), 5).size();
        Assert.assertEquals(menuAppsCount, 5, "Apps count error on the menu bar");
        ScreenHelper.clickAt(0.8, 0.8);

        TestRail.addStepName("Check there are 2 apps on the Open Apps");
        systemPO.startOpenApps();
        int openAppsCount = ElementHelper.findElements(By.xpath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/apps_grid_container']" + checkAppsXpath), 5).size();
        Assert.assertEquals(openAppsCount, 2, "Apps count error on the Open Apps");
        ScreenHelper.clickAt(0.8, 0.8);

        TestRail.addStepName("Check the Open Apps window disappears after close one app");
        systemPO.closeAppOnMenuBar(appName[appName.length - 1]);
        systemPO.startMenuBar();
        AssertKt.assertNotPresent(Locator.byText("Open Apps"));

        TestRail.addStepName("Check there are 4 apps on the muenu bar");
        menuAppsCount = ElementHelper.findElements(By.xpath(checkAppsXpath), 5).size();
        Assert.assertEquals(menuAppsCount, 6, "Apps count error on the menu bar");
        ScreenHelper.clickAt(0.8,0.8);

        for (int i=appName.length-2;i>-1;i--) {
            systemPO.closeAppOnMenuBar(appName[i]);
        }
    }

    /**
     * C120352 Verify than Open Apps window will disappear if users reduce opened app number to 4
     * When menu bar has opened 5 non-pinned apps
     * Close an app;----Open Apps window diappears;4 app icons appears on the menu bar
     */
    @Test(groups= {"P0"})
    public void C120352VerifyThanOpenAppsWindowWillDisappearIfUsersReduceOpenedAppNumberTo4() {
        TestRail.setTestRailId("");
        Log.info("This case same as 'com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar.MenuApplicationIcons.OpenApps.C120350C120352VerifyThanOpenAppsShouldHaveMoreThanTwoAppsInIt'");
    }

    /**
     * C112625 Verify that Open Apps icon appears on menu bar if opening more than 4 non-pinned apps
     * When menu bar has opened 4 non-pinned apps
     * User taps "Applications" icon;----Application Locker opens
     * User taps an application icon in the Application window to open it;----App opens,Open Apps icon appears in the right of Application icon,The latest opened non-pinned app icon appears on the end of app icon list
     * User taps Open Apps icon;----Open Apps window opens,The first opened non-pinned app icon appears on the Open Apps window
     */
    @Test(groups= {"P1"})
    public void C112625VerifyThatOpenAppsIconAppearOnMenuBarIfOpeningMoreThan4NonpinnedApps() {
        TestRail.setTestRailId("112625");

        TestRail.addStepName("menu bar has opened 5 non-pinned apps");
        String[] appNames = {"Settings", "Adobe Acrobat", "Files", "Panel Management", "Update"};
        for (String s : appNames) {
            systemPO.startAppFromUnifiedLauncher(s);
        }

        TestRail.addStepName("App opens,The latest opened non-pinned app icon appears on the end of app icon list,Open Apps icon doesn't appear on menu bar if opening less than 4 non-pinned apps");
        systemPO.startMenuBar();
        List<MobileElement> eles = ElementHelper.findElements(By.xpath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"), 5);
        Assert.assertEquals(eles.size(), 5, "All app open");
        Assert.assertEquals(eles.get(4).getText(), appNames[4], "The latest opened non-pinned app icon appears on the end of app icon list");
        String openAppsXpath = "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/toolbar']/*[2]/*[2][@text='Open Apps']";
        MobileElement openAppsEle = ElementHelper.findElement(By.xpath(openAppsXpath));

        TestRail.addStepName("User taps Open Apps icon;Open Apps window opens,The first opened non-pinned app icon appears on the Open Apps window");
        openAppsEle.click();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + appNames[0] + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));

        TestRail.addStepName("Clear environment, close all apps.");
        ScreenHelper.clickAt(0.8, 0.8);
        for (int i = 4; i >= 0; i--) {
            systemPO.closeAppOnMenuBar(appNames[i]);
        }
    }

    /**
     * C112627 Verify that Open Apps icon doesn't appear on menu bar if opening less than 4 non-pinned apps
     * When menu bar has opened 3 non-pinned apps
     * User taps "Applications" icon;----Application Locker opens
     * User taps an application icon in the Application window to open it;----App opens,The latest opened non-pinned app icon appears on the end of app icon list,Open Apps icon doesn't appear on menu bar if opening less than 4 non-pinned apps
     */
    @Test(groups= {"P1"})
    public void C112627VerifyThatOpenAppsIconDoesnotAppearOnMenuBarIfOpeningLessThan4NonpinnedApps() {
        TestRail.setTestRailId("112627");

        TestRail.addStepName("menu bar has opened 4 non-pinned apps");
        String[] appNames = {"Settings", "Adobe Acrobat", "Files", "Panel Management"};
        for (String s : appNames) {
            systemPO.startAppFromUnifiedLauncher(s);
        }

        TestRail.addStepName("App opens,The latest opened non-pinned app icon appears on the end of app icon list,Open Apps icon doesn't appear on menu bar if opening less than 4 non-pinned apps");
        systemPO.startMenuBar();
        List<MobileElement> eles = ElementHelper.findElements(By.xpath("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"), 5);
        Assert.assertEquals(eles.size(), 6, "All app open");
        Assert.assertEquals(eles.get(5).getText(), appNames[3], "The latest opened non-pinned app icon appears on the end of app icon list");
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Open Apps']"));

        TestRail.addStepName("Clear environment, close all apps.");
        for (String s : appNames) {
            systemPO.closeAppOnMenuBar(s);
        }
    }
}
