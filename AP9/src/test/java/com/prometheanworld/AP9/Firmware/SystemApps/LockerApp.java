package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.Direction;
import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.command.Cmd;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.model.AP9.IdentityPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseIdentityPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class LockerApp extends BaseTest {
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C113246 Verify that locker opens pressing applications icon on task bar
     * check locker app is installed
     * pressing applications icon on task bar
     * after hide taskbar find Applications element in screen
     * Verify that locker app opens
     */
    @Test(groups= "P0")
    public void C113246VerifyThatLockerOpensPressingApplicationsIconOnTaskbar(){
        TestRail.setTestRailId("113246");
        TestRail.addStepName("check locker app is installed");
        String lockerPackageName = "com.prometheanworld.locker";
        String lockerElementText = "//*[@text=\"Applications\"]";
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:" + lockerPackageName) >= 0);
        TestRail.addStepName("pressing applications icon on task bar");
        AppiumHelper.showTaskbar();
        ElementHelper.click(By.xpath(lockerElementText));
        TestRail.addStepName("after hide taskbar find Applications element in screen");
        AppiumHelper.hideTaskbar();
        MobileElement ele = ElementHelper.findElement(By.xpath(lockerElementText));
        TestRail.addStepName("Verify that locker app opens");
        Assert.assertEquals(ele.getAttribute("package"), lockerPackageName);
        Adb.forceStop(lockerPackageName);
    }

    /**
     * C112697: Verify that application icon can be dragged from Application locker into taskbar
     * Click on Applications icon -> Application Locker should open
     * Press and hold an application icon eG: Settings or Spinner or Camera -> Icon should become draggable
     *
     */
    @Test(groups= "P0")
    public void C112697verifyDragAppIconToTaskBar() {
        TestRail.setTestRailId("112697");

        systemPO.startMenuBar();
        String testDragAppName = "Chromium";
        TestRail.addStepName("open locker");
        Rect testAppRect = ElementHelper.getRect(Locator.byText("Applications"));
        Log.info("; testAppRect: x=" + testAppRect.getLeft() + ",y=" + testAppRect.getTop() + ",width=" + testAppRect.getWidth() + ",height=" + testAppRect.getHeight());
        ElementHelper.click(By.xpath(systemPO.eleMenuApplications));
        String path = "//android.widget.TextView[@text='" + testDragAppName + "']";
        List<MobileElement> tabs = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab), 5);
        for(MobileElement tabEle:tabs) {
            tabEle.click();
            if (ElementHelper.isPresent(By.xpath(path), 2)) {
                TestRail.addStepName("drag Chromium to menu bar");
                Rect eleAppRect = ElementHelper.getRect(By.xpath(path));
                TouchAction act = new TouchAction(Driver.getAndroidDriver());
                act.press(PointOption.point(eleAppRect.getLeft(), eleAppRect.getTop()))
                        .moveTo(PointOption.point(eleAppRect.getLeft(), testAppRect.getTop()))
                        .moveTo(PointOption.point(testAppRect.getRight(), testAppRect.getTop()))
                        .release()
                        .perform();
                systemPO.startMenuBar();
                AssertKt.assertPresent(By.xpath(path));
                try {
                    systemPO.clickLongPressMenu(testDragAppName, LongPressMenu.KeepInMenu,false);
                } catch (Throwable e) {}
                act.press(PointOption.point(0, 0)).perform();
                return;
            }
        }
        throw new NoSuchElementException(testDragAppName + ": Can't find app in Applications");
    }

    /**
     * C92392 Verify that user can swipe left and right to open apps in applications locker
     * Click on Application icon in menu  ---  Application locker should appear
     * Click to swipe right or on .  ---  Application on 2nd page should appear
     * Click to swipe left or on .  ---  15 max applications should appear on this page
     */
    @Test(groups= "P2")
    public void C92392VerifyUserCanSwipeLockerLeftAndRight(){
        TestRail.setTestRailId("92392");

        TestRail.addStepName("Click on Application icon in menu");
        // open locker
        AppiumHelper.openAppsFromTaskBar("Applications");
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/app_grid"), 5);

        int currentPage = 1;
        int lockerPage = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab)).size();
        int iconNumber;
        TestRail.addStepName("Click to swipe right or on .");
        while(currentPage<=lockerPage){
            iconNumber = ElementHelper.findElements(Locator.view("android.widget.ImageView").toBy()).size();
            if (iconNumber > 15) {
                throw new NoSuchElementException("App icons in Locker are more than 15");
            }
            systemPO.swipePageByTab(currentPage);
            AppiumHelper.waitForSeconds(2);
            currentPage++;
        }

        TestRail.addStepName("Click to swipe left or on .");
        while(currentPage>0){
            iconNumber = ElementHelper.findElements(Locator.view("android.widget.ImageView").toBy()).size();
            if (iconNumber > 15) {
                throw new NoSuchElementException("App icons in Locker are more than 15");
            }
            ElementHelper.swipe(Locator.byResourceId("id/apps_grid_container"), Direction.RIGHT);
            AppiumHelper.waitForSeconds(2);
            currentPage--;
        }

        // Tap outside to hide the locker
        ScreenHelper.clickAt(Location.CENTER_TOP);
    }

    /**
     * C116955 Verify that users can move app icon between 2 icons in the application locker
     * Steps:
     * 1.open locker
     * 2.get moveElement and other two elements
     * 3.move app icon between other two icons
     * 4.check the position of app icons will change
     * 5.check and restore the location
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C116955VerifyThatUsersCanMoveAppIconBetweenTwoIconsInTheApplicationLocker(){
        Point moveLocation = null,frontLocation = null,behindLocation = null,currentLocation = null;
        try {
            TestRail.setTestRailId("116955");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("get moveElement and other two elements");
            List<MobileElement> elements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            MobileElement moveElement = elements.get(0);
            MobileElement frontElement = elements.get(1);
            MobileElement behindElement = elements.get(2);
            moveLocation = moveElement.getLocation();
            frontLocation = frontElement.getLocation();
            behindLocation = behindElement.getLocation();
            String moveAppName = moveElement.getText();
            TestRail.addStepName("move app icon between other two icons");
            TouchAction act = new TouchAction(Driver.getAndroidDriver());
            act.press(PointOption.point(moveLocation.getX(),moveLocation.getY())).moveTo(PointOption.point((frontLocation.getX()+behindLocation.getX())/2,frontLocation.getY()));
            act.release().perform();
            TestRail.addStepName("check the position of app icons will change");
            MobileElement currentElement = AppiumHelper.findElement("//android.widget.TextView[@text='" + moveAppName + "']");
            currentLocation = currentElement.getLocation();
            Assert.assertNotEquals(moveLocation.x,currentLocation.x);
            Assert.assertEquals(moveLocation.y,currentLocation.y);
        }finally {
            TestRail.addStepName("check and restore the location");
            if (moveLocation != null && frontLocation != null && behindLocation != null && currentLocation != null && moveLocation.getX() != currentLocation.getX()) {
                TouchAction act = new TouchAction(Driver.getAndroidDriver());
                act.press(PointOption.point(moveLocation.getX(), moveLocation.getY())).moveTo(PointOption.point((frontLocation.getX() + behindLocation.getX()) / 2, frontLocation.getY()));
                act.release().perform();
            }
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C115129 Verify that locker will auto-hides after 30 seconds of non-interactivity
     * Steps:
     * 1.open locker
     * 2.click on any balck space in the locker
     * 3.Confirm that locker still displays
     * 4.Wait for 30 seconds of non-interactivity
     * 5.Confirm that locker will disappear automatically
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C115129VerifyThatLockerWillAutoHidesAfter30SecondsOfNonInteractivity() {
        try {
            TestRail.setTestRailId("115129");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("Wait for 10 seconds of non-interactivity");
            AppiumHelper.waitForSeconds(10);
            TestRail.addStepName("click on any balck space in the locker");
            AndroidElement ele = AppiumHelper.findElement("//*[@text=\"Applications\"]");
            Assert.assertNotNull(ele);
            ele.click();
            TestRail.addStepName("Confirm that locker still displays");
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("id/locker_container"), 1));
            TestRail.addStepName("Wait for 30 seconds of non-interactivity");
            AppiumHelper.waitForSeconds(30);
            TestRail.addStepName("Confirm that locker will disappear automatically");
            Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("id/locker_container"), 1));
        } finally {
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C115209 Verify reset the auto-hide timer by controlling the locker with the remote
     * Steps:
     * 1.open locker
     * 2.send remote command of right
     * 3.test locker is still show after 31 seconds
     * 4.test locker is hidden after new 30 seconds
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C115209ResetTheAutoHideTimerByControllingTheLockerWithTheRemote(){
        try {
            TestRail.setTestRailId("115209");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            AppiumHelper.waitForSeconds(5);
            TestRail.addStepName("send remote command of right");
            systemPO.inputKeyevent(PrometheanKey.RemoteRight, 2);
            AppiumHelper.waitForSeconds(26);
            TestRail.addStepName("test locker is still show after 31 seconds");
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("id/locker_container"), 1));
            AppiumHelper.waitForSeconds(4);
            TestRail.addStepName("test locker is hidden after new 30 seconds");
            Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("id/locker_container"), 1));
        }finally {
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C115208 Verify reset the auto-hide timer by dragging the locker app icon
     * Steps:
     * 1.open locker
     * 2.get other element which chromium element to move to
     * 3.Drag Chromium app icon for 5 seconds and check Chromium icon can be dragged and move around
     * 4.Release dragging and check Chromium icon can be dragged and move around
     * 5.wait for 26 seconds of non-interactivity check locker still open
     * 6.check and restore the location
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C115208ResetTheAutoHideTimerByDraggingTheLockerAppIcon() {
        Point chromeOriginLocation = null,otherLocation = null,chromeNewLocation = null;
        Rectangle otherAppRect = null;
        try {
            TestRail.setTestRailId("115208");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            String testDragAppName = "Chromium";
            String chromePath = "//android.widget.TextView[@text='" + testDragAppName + "']";
            MobileElement chromeElement = systemPO.scrollAndFindApp(testDragAppName);
            chromeOriginLocation = chromeElement.getLocation();
            int posBeforeX = chromeElement.getLocation().x;
            TestRail.addStepName("get other element which chromium element to move to");
            List<MobileElement> elements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            MobileElement otherElement = null;
            for (int i = 0; i < elements.size(); i++) {
                String elementName = elements.get(i).getText();
                if(!elementName.equals(testDragAppName)){
                    otherElement = elements.get(i);
                    break;
                }
            }
            Assert.assertNotNull(otherElement);
            otherAppRect = otherElement.getRect();
            otherLocation = otherElement.getLocation();
            TestRail.addStepName("Drag Chromium app icon for 5 seconds and check Chromium icon can be dragged and move around");
            TouchAction act = new TouchAction(Driver.getAndroidDriver());
            act = act.press(PointOption.point(chromeOriginLocation.getX(), chromeOriginLocation.getY()))
                    .moveTo(PointOption.point(otherLocation.getX(), otherLocation.getY()))
                    .moveTo(PointOption.point(otherLocation.getX() + otherAppRect.width, otherLocation.getY()));
            AppiumHelper.waitForSeconds(5);
            TestRail.addStepName("Release dragging and check Chromium icon can be dragged and move around");
            act.release().perform();
            chromeElement = AppiumHelper.findElement(chromePath);
            chromeNewLocation = chromeElement.getLocation();
            Assert.assertFalse(chromeNewLocation.x == posBeforeX);
            TestRail.addStepName("wait for 26 seconds of non-interactivity check locker still open");
            AppiumHelper.waitForSeconds(26);
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("id/locker_container"), 1));
        }finally {
            TestRail.addStepName("check and restore the location");
            if (chromeOriginLocation != null && chromeNewLocation != null && otherLocation != null && otherAppRect != null && chromeOriginLocation.getX() != chromeNewLocation.getX()) {
                TouchAction act = new TouchAction(Driver.getAndroidDriver());
                act = act.press(PointOption.point(otherLocation.getX(), otherLocation.getY()))
                        .moveTo(PointOption.point(chromeOriginLocation.getX(), chromeOriginLocation.getY()))
                        .moveTo(PointOption.point(chromeOriginLocation.getX() + otherAppRect.width, chromeOriginLocation.getY()));
                act.release().perform();
            }
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C116953 Verify that users can't drag app icon to empty space in the application locker
     * Steps:
     * 1.open locker
     * 2.swipe to last page of locker
     * 3.get last two element location and rect
     * 4.drag app icon to empty space in application locker
     * 5.check the position of app icons will not change
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C116953VerifyThatUsersCanNotDragAppIconToEmptySpaceInTheApplicationLocker(){
        try {
            TestRail.setTestRailId("116953");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("swipe to last page of locker");
            int lockerPage = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab)).size();
            systemPO.swipePageByTab(lockerPage);
            TestRail.addStepName("get last two element location and rect");
            List<MobileElement> elements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            Assert.assertTrue(elements.size() >= 2);
            MobileElement moveElement = elements.get(elements.size() - 2);
            MobileElement lastElement = elements.get(elements.size() - 1);
            Rectangle rect = lastElement.getRect();
            Point moveLocation = moveElement.getLocation();
            Point lastLocation = lastElement.getLocation();
            String moveAppName = moveElement.getText();
            TestRail.addStepName("drag app icon to empty space in application locker");
            TouchAction act = new TouchAction(Driver.getAndroidDriver());
            act.press(PointOption.point(moveLocation.getX(),moveLocation.getY())).moveTo(PointOption.point(lastLocation.getX()+rect.width*2,lastLocation.getY()));
            act.release().perform();
            TestRail.addStepName("check the position of app icons will not change");
            MobileElement currentElement = AppiumHelper.findElement("//android.widget.TextView[@text='" + moveAppName + "']");
            Assert.assertEquals(moveLocation.x,currentElement.getLocation().x);
            Assert.assertEquals(moveLocation.y,currentElement.getLocation().y);
        }finally {
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C116956 Verify that users can move app icon across screen in the application locker
     * Steps:
     * 1.open locker
     * 2.swipe to second page and get a secondPageElement location
     * 3.swipe to first page and move element to second page location where is get before
     * 4.check second page element will adjust to fit it
     * 5.swipe to first page and get a firstPageElement location
     * 6.swipe to second page and move element to first page location where is get before
     * 7.check first page element will adjust to fit it
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C116956VerifyThatUsersCanMoveAppIconAcrossScreenInTheApplicationLocker() {
        try {
            TestRail.setTestRailId("116956");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            List<MobileElement> elements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            MobileElement containerElement = ElementHelper.findElement(Locator.byResourceId("id/locker_container"));
            MobileElement moveElement = elements.get(0);
            MobileElement endElement = elements.get(4);
            Point moveLocation = moveElement.getLocation();
            Point endLocation = endElement.getLocation();
            Point containerElementLocation = containerElement.getLocation();
            Rectangle rect = containerElement.getRect();
            TestRail.addStepName("swipe to second page and get a secondPageElement location");
            systemPO.swipePageByTab(2);
            List<MobileElement> secondPageElements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            MobileElement secondPageElement = secondPageElements.get(3);
            String elementText = secondPageElement.getText();
            Point secondPageElementLocation = secondPageElement.getLocation();
            TestRail.addStepName("swipe to first page and move element to second page location where is get before");
            systemPO.swipePageByTab(1);
            AppiumHelper.waitForSeconds(1);
            TouchAction act = new TouchAction(Driver.getAndroidDriver());
            act.longPress(PointOption.point(moveLocation.getX(), moveLocation.getY())).moveTo(PointOption.point(endLocation.getX(), endLocation.getY()))
                    .moveTo(PointOption.point(containerElementLocation.getX() + rect.width, endLocation.getY())).moveTo(PointOption.point(secondPageElementLocation.getX(), endLocation.getY()));
            act.release().perform();
            TestRail.addStepName("check second page element will adjust to fit it");
            MobileElement currentElement = AppiumHelper.findElement("//android.widget.TextView[@text='" + elementText + "']");
            Assert.assertNotEquals(secondPageElementLocation.getX(), currentElement.getLocation().getX());
            TestRail.addStepName("swipe to first page and get a firstPageElement location");
            systemPO.swipePageByTab(1);
            List<MobileElement> firstPageElements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            MobileElement firstPageElement = firstPageElements.get(2);
            elementText = firstPageElement.getText();
            Point firstPageElementLocation = firstPageElement.getLocation();
            TestRail.addStepName("swipe to second page and move element to first page location where is get before");
            systemPO.swipePageByTab(2);
            AppiumHelper.waitForSeconds(1);
            act = new TouchAction(Driver.getAndroidDriver());
            act.longPress(PointOption.point(currentElement.getLocation().getX(), currentElement.getLocation().getY())).moveTo(PointOption.point(moveLocation.getX(), moveLocation.getY()))
                    .moveTo(PointOption.point(containerElementLocation.getX(), endLocation.getY())).moveTo(PointOption.point(firstPageElementLocation.getX(), firstPageElementLocation.getY()));
            act.release().perform();
            TestRail.addStepName("check first page element will adjust to fit it");
            currentElement = AppiumHelper.findElement("//android.widget.TextView[@text='" + elementText + "']");
            Assert.assertNotEquals(firstPageElementLocation.getX(), currentElement.getLocation().getX());
        } finally {
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**C112703 Verify that application locker closes when user click else where on screen
     *Steps
     * 1. Click on Applications icon
     * 2. Click on Task bar or else where on screen
     * Author:Sita
     */
    @Test(groups= "P1")
    public void C112703ApplicationLockerCloses(){
        TestRail.setTestRailId("112703");
        TestRail.addStepName("Click on Applications icon");
        AppiumHelper.openAppsFromTaskBar("Applications"); //Open Locker
        Assert.assertTrue(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.locker:id/tv_title"),10));
        TestRail.addStepName("Click on Task bar or else where on screen");
        AppiumHelper.showTaskbar();
        Assert.assertFalse(AppiumHelper.isElementVisible(null, By.id("com.prometheanworld.locker:id/tv_title"),10));
    }

    /**
     * C116957 Verify that users can't create a folder in the application locker when put two app icons together
     * Steps:
     * 1.open locker
     * 2.get two app element to move together
     * 3.Drag icon and drop it above the icon of another app
     * 4.check users can't create a folder in the application locker when put two app icons together
     * 5.restore the location
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C116957VerifyThatUsersCanNotCreateAfolderInTheApplicationLockerWhenPutTwoAppIconsTogether() {
        Point moveAppLocation = null, secondAppLocation = null;
        try {
            TestRail.setTestRailId("116957");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("get two app element to move together");
            List<MobileElement> elements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            MobileElement firstElement = elements.get(0);
            MobileElement secondElement = elements.get(1);
            String firstAppName = firstElement.getText();
            String secondAppName = secondElement.getText();
            moveAppLocation = firstElement.getLocation();
            secondAppLocation = secondElement.getLocation();
            TestRail.addStepName("Drag icon and drop it above the icon of another app");
            TouchAction act = new TouchAction(Driver.getAndroidDriver());
            act.press(PointOption.point(moveAppLocation.getX(), moveAppLocation.getY())).moveTo(PointOption.point(secondAppLocation.getX(), secondAppLocation.getY()));
            act.release().perform();
            TestRail.addStepName("check users can't create a folder in the application locker when put two app icons together");
            firstElement = AppiumHelper.findElement("//android.widget.TextView[@text='" + firstAppName + "']");
            secondElement = AppiumHelper.findElement("//android.widget.TextView[@text='" + secondAppName + "']");
            Assert.assertNotNull(firstElement);
            Assert.assertNotNull(secondElement);
        } finally {
            if (moveAppLocation != null && secondAppLocation != null) {
                TouchAction act = new TouchAction(Driver.getAndroidDriver());
                act.press(PointOption.point(moveAppLocation.getX(), moveAppLocation.getY())).moveTo(PointOption.point(secondAppLocation.getX(), secondAppLocation.getY()));
                act.release().perform();
            }
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C116958 Verify that the newest installed app is on the end of app icon list in the application locker
     * Steps:
     * 1.open locker
     * 2.check the newest app is not install before
     * 3.install the newest app
     * 4.swipe to last page of locker
     * 5.check the newest installed app is on the end of app icon list in the application locker
     * 6.uninstall the newest app
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C116958VerifyThatTheNewestInstalledAppIsOnTheEndOfAppIconListInTheApplicationLocker() {
        try {
            TestRail.setTestRailId("116958");
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("check the newest app is not install before");
            String newestAppName = "My Application";
            MobileElement eleApp;
            try {
                eleApp = systemPO.scrollAndFindApp(newestAppName);
            } catch (NoSuchElementException e) {
                eleApp = null;
            }
            Assert.assertNull(eleApp);
            TestRail.addStepName("install the newest app");
            String installNewApp = "adb install " + systemPO.getProjectPath() + "updateFile" + File.separator + "locker" + File.separator + "newestInstall.apk";
            Cmd.exec(installNewApp);
            AppiumHelper.waitForSeconds(2);
            TestRail.addStepName("swipe to last page of locker");
            int lockerPage = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab)).size();
            systemPO.swipePageByTab(lockerPage);
            TestRail.addStepName("check the newest installed app is on the end of app icon list in the application locker");
            List<MobileElement> elements = ElementHelper.findElements(Locator.byResourceId("id/app_txt"));
            Assert.assertEquals(elements.get(elements.size() - 1).getText(), newestAppName);
        } finally {
            TestRail.addStepName("uninstall the newest app");
            Adb.adb("adb uninstall com.promappium.locker.test");
            Adb.forceStop("com.prometheanworld.locker");
        }
    }


    /**
     * C176775 Verify that Cloud Connect app is hidden when sign in as guest
     * Steps:
     * 1.show taskbar and check current user is a guest
     * 2.open locker
     * 3.Confirm that the Cloud Connect Application is not visible in Locker
     * Author: guangxi.chen
     */
    @Test(groups = "P2")
    public void C176775VerifyThatCloudConnectAppIsHiddenWhenSignInAsGuest() {
        try {
            TestRail.setTestRailId("176775");
            TestRail.addStepName("show taskbar and check current user is a guest");
            AppiumHelper.showTaskbar();
            Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Guest")));
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("Confirm that the Cloud Connect Application is not visible in Locker");
            String cloudAppName = "Cloud Connect";
            MobileElement eleApp;
            try {
                eleApp = systemPO.scrollAndFindApp(cloudAppName);
            } catch (NoSuchElementException e) {
                eleApp = null;
            }
            Assert.assertNull(eleApp);
        } finally {
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

    /**
     * C176776 Verify that Cloud Connect app is visible when sign in as user
     * Steps:
     * 1.adb root and change to sandbox env
     * 2.login in to identity and check success
     * 3.open locker
     * 4.Confirm that the Cloud Connect Application is visible in Locker
     * 5.check and signOut
     * 6.reset env to production
     * Author: guangxi.chen
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C176776VerifyThatCloudConnectAppIsVisibleWhenSignInAsUser() {
        try {
            TestRail.setTestRailId("176776");
            TestRail.addStepName("adb root and change to sandbox env");
            AppiumHelper.exeAdbRoot();
            systemPO.setPropMdmEnvByCmd("sandbox");
            TestRail.addStepName("login in to identity and check success");
            systemPO.signIn();
            BaseIdentityPO identityPO = POFactory.getInstance(IdentityPO.class);
            Assert.assertTrue(identityPO.signInWithEmail());
            TestRail.addStepName("open locker");
            AppiumHelper.openAppsFromTaskBar("Applications");
            TestRail.addStepName("Confirm that the Cloud Connect Application is visible in Locker");
            String cloudAppName = "Cloud Connect";
            MobileElement eleApp;
            try {
                eleApp = systemPO.scrollAndFindApp(cloudAppName);
            } catch (NoSuchElementException e) {
                eleApp = null;
            }
            Assert.assertNotNull(eleApp);
        } finally {
            TestRail.addStepName("check and signOut");
            systemPO.startMenuBar();
            if(!ElementHelper.isVisible(Locator.byText("Guest"))){
                systemPO.signOut(false,false);
            }
            TestRail.addStepName("reset env to production");
            systemPO.setPropMdmEnvByCmd("production");
            Adb.forceStop("com.prometheanworld.locker");
        }
    }

}