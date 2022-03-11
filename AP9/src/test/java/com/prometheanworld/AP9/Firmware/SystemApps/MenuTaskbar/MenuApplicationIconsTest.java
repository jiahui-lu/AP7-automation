package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.model.AP9.SettingsPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class MenuApplicationIconsTest extends BaseTest {
    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C115950 Verify that users can drag and drop icons in Menu bar
     * Open some apps from Application;----ome app icons appear on the menu bar
     * Drag one icon in Menu bar;----The icon can be dargged
     * Drop one icon to another place in Menu bar;----The icon can be dropped to another place
     */
    @Test(groups= "P0")
    private void C115950VerifyThatUsersCanDragAndDropIconsInMenuBar() {
        TestRail.setTestRailId("115950");

        String[] appName = {"Settings", "Files"};
        for (String s : appName) {
            systemPO.startAppFromUnifiedLauncher(s);
        }
        systemPO.startMenuBar();
        String filesXpath = "//*[@text='Files' and @package='com.prometheanworld.unifiedlauncher']";
        MobileElement ele = ElementHelper.findElement(By.xpath(filesXpath));
        Point p1 = ele.getLocation();
        Log.info(p1.toString());
        AppiumHelper.longPressAndMoveTo(filesXpath,p1.x - 150, p1.y);
        ele = ElementHelper.findElement(By.xpath(filesXpath));
        Point p2 = ele.getLocation();
        Log.info(p2.toString());
        Assert.assertTrue(p1.x > p2.x + 30, "Icon x coordinate error");
        Assert.assertEquals(p1.y, p2.y, "Icon Y coordinate error");
        for (String s : appName) {
            systemPO.closeAppOnMenuBar(s);
        }
    }

    /**
     * C113247/115171 Verify that long press app icon in menu bar brings up app menu
     * Click\Press on Application icon;----Applications locker should open
     * Press Adobe app icon to open;----Application should get open and application icon should appear on menu bar(open state i.e. Icon focused with a . below)
     * Long Press the Adobe application icon for 1000 miliseconds;----Long Press menu should appear with options: Keep in Menu, Open at Login, Minimize and Close
     * (115171) When panel is in Guest mode, long press App Icon in taskbar greater than or equal to 1s ;----"Open at Start up" appears on long press menu
     */
    @Test(groups= "P0")
    private void C113247C115171VerifyThatLongPressAppIconInMenuBarBringsUpAppMenu() {
        TestRail.setTestRailId("113247,115171");

        String appName = "Adobe Acrobat";
        systemPO.startAppFromUnifiedLauncher(appName);
        systemPO.startMenuBar();
        ElementHelper.longPress(By.xpath("//*[@text='" + appName + "']"), 1);
        String[] checkEle = {"//*[@text='Keep in Menu']", "//*[@text='Open at start up']", "//*[@text='Minimize']", "//*[@text='Close']"};
        for (String e : checkEle) {
            AssertKt.assertPresent(By.xpath(e), 2);
        }
        ElementHelper.click(By.xpath("//*[@text='Close']"));
    }

    /**
     * Author:lifeifei
     * Date 2021.12.20
     * C115964 Verify that users can drag and drop app icon from applications window to menu bar
     * step1 Open 4 apps from Application and no apps pinned to menu bar
     * step2 drag one app to menubar from locker
     * step3 check just can drag one app to menbar (Max pinned app icons number is 3,there are two app has been pinned in menubar default)
     */
    @Test(groups= "P1")
    private void C115964VerifyThatUsersCanDragAndDropIconsInMenuBar() throws InterruptedException {
        TestRail.setTestRailId("115964");
        Thread.sleep(5000);

        //get all of the apps on the first in screen of locker and add apps name into string[]
        systemPO.startMenuBar();
        systemPO.startLocker();

        String[] AppNames = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.locker:id/app_txt']");
        //launch the  first four apps on the screen
        TestRail.addStepName("step1 Open 4 apps from Application and no apps pinned to menu bar");
        for (int i = 0; i < 4; i++) {
            systemPO.startAppFromUnifiedLauncher(AppNames[i]);
        }
        TestRail.addStepName("step2.drag two app to menubar from locker");
        for (int j = 4; j < 6; j++) {
            systemPO.startLocker();
            Thread.sleep(2000);
            String filesXpath3 = "//*[@text= '" + AppNames[j] + "' and @package='com.prometheanworld.locker']";
            MobileElement ele1 = ElementHelper.findElement(By.xpath(filesXpath3));
            String filesXpath = "//*[@text= '" + AppNames[j - 3] + "' and @package='com.prometheanworld.unifiedlauncher']";
            systemPO.startMenuBar();
            MobileElement ele = ElementHelper.findElement(By.xpath(filesXpath));
            Point p = ele.getLocation();
            systemPO.startLocker();
            Thread.sleep(3000);
            ElementHelper.longPressAndMoveTo(By.xpath(filesXpath3), new Point(p.x + 120, p.y));
        }
        systemPO.startMenuBar();


        TestRail.addStepName("step3 check just can drag one app to menbar");
        List<MobileElement> eles = ElementHelper.findElements(By.id("com.prometheanworld.unifiedlauncher:id/menu_icon_title"), 5);
        //Assert there just seven app in menubar not 8
        Assert.assertEquals(eles.size(), 7);
        for (MobileElement ele : eles) {
            //assert that Max pinned app icons number is 3, the fourth app can't be dragged to menubar successfully
            Assert.assertFalse(AppNames[5].equals(ele.getText()), "Max pinned app icons number is 3");
        }
    }

    @AfterMethod(alwaysRun = true)
//@BeforeMethod
//close and unpin all of the app in the menubar except "Screen Capture" and "Annotate"  before run test cases
    public void after_method() {
        systemPO.startMenuBar();
        String[] AppNamesMenubar = systemPO.getAllElementsText("//*[@resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']");
        System.out.println(Arrays.toString(AppNamesMenubar));
        for (String str : AppNamesMenubar) {
            systemPO.startMenuBar();
            if (str.equals("Screen Capture") || str.equals("Annotate")) {

                continue;
            } else {

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
}

