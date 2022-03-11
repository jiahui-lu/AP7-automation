package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.Direction;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;

public class ChromiumTest extends BaseTest {

    final By MY_PROMETHEAN = Locator.byText("myPromethean");
    final By desktopSite = Locator.byText("Desktop site");
    final By CHROMIUM_ROOT = By.id("org.chromium.chrome:id/menu_button");
    final By CHROMIUM_SETTINGS = Locator.byText("Settings");
    final By ABOUT_CHROME = Locator.byText("About Chrome");
    final By ADD_HOME_SCREEN = By.xpath("//android.widget.TextView[@content-desc=\"Add to Home screen\"]");
    final By DELETE_SHORTCUT = Locator.byText("Delete shortcut");
    final By RENAME_SHORTCUT = Locator.byText("Rename shortcut");
    final By NAVIGATE_BUTTON = By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]");
    final By MAXIMIZE = By.xpath("//android.widget.ImageButton[@content-desc=\"Maximize\"]");
    final By SECOND_TAB = By.xpath("/hierarchy/android.widget.LinearLayout/android.widget.HorizontalScrollView/android.widget.LinearLayout/b.b.k.a.c[2]");

    /* C90131 Chromium default to desktop mode
    1. launch Chromium app, go to a webpage and expand the "..." sign at the top right corner
    Author:Sita */
    @Test(groups = "P2")
    public void C90131ChromiumDefaultToDesktopMode() {
        TestRail.setTestRailId("90131");
        TestRail.addStepName("launch Chromium app, go to a webpage and expand the \"...\" sign at the top right corner");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(MAXIMIZE);
        AppiumHelper.switchWindowToFullScreen();
        ElementHelper.click(CHROMIUM_ROOT);
        AppiumHelper.swipeByElements(Locator.byText("View source"), Locator.byText("Bookmarks"));
        //Chrome's desktop site is selected by default
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Desktop site"),10));
        ElementHelper.clickWhenVisible(By.xpath("//android.widget.TextView[@content-desc=\"Exit\"]"));
    }

    /*C112864 Verify Update Chromium Browser
    1.open the chromium
    2.go to a webpage and expand the "..." sign at the top right corner->click the settings
    3.click the About Chrome
    Author:Sita */
    @Test(groups = "P2")
    public void C112864UpdateChromiumBrowser() {
        TestRail.setTestRailId("112864");
        TestRail.addStepName("open the chromium");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        AppiumHelper.waitForSeconds(10);
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("go to a webpage and expand the \"...\" sign at the top right corner->click the settings");
        ElementHelper.click(CHROMIUM_ROOT);
        AppiumHelper.swipeByElements(Locator.byText("View source"), Locator.byText("Bookmarks"));
        ElementHelper.click(CHROMIUM_SETTINGS);
        TestRail.addStepName("click the About Chrome");
        AppiumHelper.swipeByElements(Locator.byText("Advanced"), Locator.byText("Search engine"));
        ElementHelper.click(ABOUT_CHROME);
        AppiumHelper.waitForSeconds(10);
        closeWindow();
    }

    /*C119970 Verify that "Add to Home screen" option in the chromium custom list is visible and functional
    1.Open the chromium
    2.Enter a URL and visit
    3.Click the three-dots button in the upper right corner of the browser
    4.Click on "Add to Home screen" button
    Author:Sita */
    @Test(groups = "P2")
    public void C119970AddToHomeScreenOptionInTheChromium() throws AWTException, InterruptedException {
        TestRail.setTestRailId("119970");
        TestRail.addStepName("Open the chromium");
        TestRail.addStepName("Enter a URL and visit");
        TestRail.addStepName("Click the three-dots button in the upper right corner of the browser");
        TestRail.addStepName("Click on \"Add to Home screen\" button");
        navigateChromiumBookMark();
        //Url can be added to locker
        Assert.assertTrue(ElementHelper.isVisible(MY_PROMETHEAN,10));
        deleteBookmark();
    }

    /*C120424 Verify that when Chromium is opened, the home page displays MyPromethean.com by default
    1.Open locker
    2.Click on Chromium and open it
    Author:Sita */
    @Test(groups = "P2")
    public void C120424WhenChromiumIsOpened() {
        TestRail.setTestRailId("120424");
        TestRail.addStepName("Open locker");
        TestRail.addStepName("Click on Chromium and open it");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        AppiumHelper.switchWindowToFullScreen();
        //The home page displays MyPromethean.com by default
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("https://portal.mypromethean.com"), 10));
        closeWindow();
    }

    /*C120427 Verify that when Chromium opens a new tab, the home page displays MyPromethean.com by default
    1.Open Chromium
    2.Click to open a new tab button at the top
    Author:Sita */
    @Test(groups = "P2")
    public void C120427ChromiumOpensNewTab() {
        TestRail.setTestRailId("120427");
        TestRail.addStepName("Open Chromium");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("Click to open a new tab button at the top");
        ElementHelper.click(CHROMIUM_ROOT);
        ElementHelper.click(By.xpath("//android.widget.TextView[@content-desc=\"New tab\"]"));
        //Open a new tab, the homepage will show MyPromethean.com by default
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("https://portal.mypromethean.com"), 10));
        closeWindow();
    }

    /* C120805 Verify that the user can delete my Chromium bookmark from my locker
    1. Open the chromium
    2.Enter a URL and visit
    3.Click the three-dots button in the upper right corner of the browser
    4.Click on "Add to Home screen" button
    5.Click the menu button on the panel->Click on Applications
    6.Long press Chromium bookmark
    7.Click Delete shortcut
    8.Click on Delete
    Author:Sita */
    @Test(groups = "P1")
    public void C120805CanDeleteMyChromiumBookmark() throws InterruptedException {
        TestRail.setTestRailId("120805");
        TestRail.addStepName("");
        TestRail.addStepName("Open the chromium");
        TestRail.addStepName("Enter a URL and visit");
        TestRail.addStepName("Click the three-dots button in the upper right corner of the browser");
        TestRail.addStepName("Click on \"Add to Home screen\" button");
        TestRail.addStepName("Click the menu button on the panel->Click on Applications");
        navigateChromiumBookMark();
        TestRail.addStepName("Long press Chromium bookmark");
        ElementHelper.longPress(MY_PROMETHEAN);
        TestRail.addStepName("Click Delete shortcut");
        ElementHelper.click(DELETE_SHORTCUT);
        TestRail.addStepName("Click on Delete");
        ElementHelper.click(Locator.byText("Delete"));
        //The bookmark is deleted successfully, and the bookmark is not displayed in the locker
        Assert.assertFalse(ElementHelper.isVisible(MY_PROMETHEAN,10));
    }

    /*C120807 Verify that the user can cancel deleting my Chromium bookmark from my locker
    1.Open the chromium
    2.Click the three-dots button in the upper right corner of the browser
    3.Click on "Add to Home screen" button
    4.Click the menu button on the panel->Click on Applications
    5.Long press Chromium bookmark
    6.Click Delete shortcut
    7.Click on Cancel
    Author:Sita */
    @Test(groups = "P1")
    public void C120807UserCanCancelDeletingMyChromiumBookmark() throws InterruptedException {
        TestRail.setTestRailId("120807");
        TestRail.addStepName("Open the chromium");
        TestRail.addStepName("Click the three-dots button in the upper right corner of the browser");
        TestRail.addStepName("Click on \"Add to Home screen\" button");
        TestRail.addStepName("Click the menu button on the panel->Click on Applications");
        navigateChromiumBookMark();
        TestRail.addStepName("Long press Chromium bookmark");
        ElementHelper.longPress(MY_PROMETHEAN);
        TestRail.addStepName("Click Delete shortcut");
        ElementHelper.click(DELETE_SHORTCUT);
        TestRail.addStepName("Click on Cancel");
        ElementHelper.click(Locator.byText("Cancel"));
        //Cancel delete bookmark and return to locker
        Assert.assertTrue(ElementHelper.isVisible(MY_PROMETHEAN,10));
        deleteBookmark();
    }

    /*C120816 Verify that the user can rename my Chromium bookmark from my locker
    1. Open the chromium
    2.Enter a URL and visit
    3.Click the three-dots button in the upper right corner of the browser
    4.Click on "Add to Home screen" button
    5.Click the menu button on the panel->Click on Applications
    6.Long press Chromium bookmark
    7.Click Rename shortcut
    8.Click the input box and enter text
    9.Click the Save button
    Author:Sita */
    @Test(groups = "P1")
    public void C1208016UserCanRenameDeletingMyChromiumBookmark() throws InterruptedException {
        TestRail.setTestRailId("1208016");
        TestRail.addStepName("Open the chromium");
        TestRail.addStepName("Enter a URL and visit");
        TestRail.addStepName("Click the three-dots button in the upper right corner of the browser");
        TestRail.addStepName("Click on \"Add to Home screen\" button");
        TestRail.addStepName("Click the menu button on the panel->Click on Applications");
        navigateChromiumBookMark();
        TestRail.addStepName("Long press Chromium bookmark");
        ElementHelper.longPress(MY_PROMETHEAN);
        TestRail.addStepName("Click Rename shortcut");
        ElementHelper.click(RENAME_SHORTCUT);
        TestRail.addStepName("Click the input box and enter text");
        AppiumHelper.findElementBy(By.id("com.prometheanworld.locker:id/rename_edit")).clear();
        AppiumHelper.findElementBy(By.id("com.prometheanworld.locker:id/rename_edit")).sendKeys("Google");
        TestRail.addStepName("Click the Save button");
        ElementHelper.click(Locator.byText("Save"));
        //Rename the Chromium bookmark successfully, return to the locker to display the modified name
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Google"),10));
        //Deleting the Bookmark
        systemPO.startLocker();
        ElementHelper.clickWhenVisible(SECOND_TAB);
        ElementHelper.longPress(Locator.byText("Google"));
        ElementHelper.clickWhenVisible(DELETE_SHORTCUT);
        ElementHelper.clickWhenVisible(Locator.byText("Delete"));
    }

    /*C120818 Verify that the user can cancel renaming Chromium bookmark in my locker
    1. Open the chromium
    2.Enter a URL and visit
    3.Click the three-dots button in the upper right corner of the browser
    4.Click on "Add to Home screen" button
    5.Click the menu button on the panel->Click on Applications
    6.Long press Chromium bookmark
    7.Click Rename shortcut
    8.Click on Cancel
    Author:Sita */
    @Test(groups = "P2")
    public void C120818UserCanCancelRenamingMyChromiumBookmark() throws InterruptedException {
        TestRail.setTestRailId("1208016");
        TestRail.addStepName("Open the chromium");
        TestRail.addStepName("Enter a URL and visit");
        TestRail.addStepName("Click the three-dots button in the upper right corner of the browser");
        TestRail.addStepName("Click on \"Add to Home screen\" button");
        TestRail.addStepName("Click the menu button on the panel->Click on Applications");
        navigateChromiumBookMark();
        TestRail.addStepName("Long press Chromium bookmark");
        ElementHelper.longPress(MY_PROMETHEAN);
        TestRail.addStepName("Click Rename shortcut");
        ElementHelper.click(RENAME_SHORTCUT);
        TestRail.addStepName("Click on Cancel");
        ElementHelper.click(Locator.byText("Cancel"));
        //Cancel renaming Chromium bookmarks, return to locker Chromium bookmarks will not change
        Assert.assertTrue(ElementHelper.isVisible(MY_PROMETHEAN,10));
        deleteBookmark();
    }

    /*C122589 Verify that user can modify the size of text scaling in chromium settings
    1.Open chromium in locker
    2.Click the three dots in the upper right corner of the chromium page
    3.Click on Settings->Accessibility
    4.Drag the slider to increase the value of Text Scaling
    5.Return to the Chromium homepage to view the text on the page
    6.Drag the slider to decrease the value of Text Scaling
    7.Return to the Chromium homepage to view the text on the page
    Author:Sita */
    @Test(groups = "P2")
    public void C122589CanModifyTheSize(){
        TestRail.setTestRailId("122589");
        TestRail.addStepName("Open chromium in locker");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(MAXIMIZE);
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("Click the three dots in the upper right corner of the chromium page");
        TestRail.addStepName("Click on Settings->Accessibility");
        TestRail.addStepName("Drag the slider to increase the value of Text Scaling");
        increaseTextScalingValue();
        TestRail.addStepName("Return to the Chromium homepage to view the text on the page");
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        //All text on the page becomes larger
        ElementHelper.waitUntilPresent(NAVIGATE_BUTTON);
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        TestRail.addStepName("Drag the slider to decrease the value of Text Scaling");
        decreaseTextScalingValue();
        TestRail.addStepName("Return to the Chromium homepage to view the text on the page");
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        //All text on the page will become smaller
        ElementHelper.waitUntilPresent(NAVIGATE_BUTTON);
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        closeWindow();
    }

    /*C122591 Verify that the user can modify the size of text scaling and switch to Mobile view
    1.Open chromium in locker
    2.Click the three dots in the upper right corner of the chromium page
    3.Uncheck on Desktop site
    4.Click on Settings->Accessibility
    5.Drag the slider to increase the value of Text Scaling
    6.Return to the Chromium homepage to view the text on the page
    7.Drag the slider to decrease the value of Text Scaling
    8.Return to the Chromium homepage to view the text on the page
    9.Switch to mobile site mode，and return to the Chromium homepage to view the text on the page
     Author:Sita */
    @Test(groups = "P1")
    public void C122591UserCanModifySize(){
        TestRail.setTestRailId("122591");
        TestRail.addStepName("Open chromium in locker");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(MAXIMIZE);
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("Click the three dots in the upper right corner of the chromium page");
        ElementHelper.click(CHROMIUM_ROOT);
        TestRail.addStepName("Uncheck on Desktop site");
        ElementHelper.swipe(desktopSite, Direction.UP);
        AppiumHelper.findElementBy(By.id("org.chromium.chrome:id/checkbox")).click();
        TestRail.addStepName("Click on Settings->Accessibility");
        TestRail.addStepName("Drag the slider to increase the value of Text Scaling");
        increaseTextScalingValue();
        TestRail.addStepName("Return to the Chromium homepage to view the text on the page");
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        //All text on the page becomes larger
        ElementHelper.waitUntilPresent(NAVIGATE_BUTTON);
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        TestRail.addStepName("Drag the slider to decrease the value of Text Scaling");
        decreaseTextScalingValue();
        TestRail.addStepName("Return to the Chromium homepage to view the text on the page");
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        //All text on the page becomes smaller
        ElementHelper.waitUntilPresent(NAVIGATE_BUTTON);
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        TestRail.addStepName("Switch to desktop site mode, and return to the chromium homepage to view the text");
        ElementHelper.click(CHROMIUM_ROOT);
        ElementHelper.swipe(desktopSite, Direction.UP);//7.Switch to desktop site mode
        AppiumHelper.findElementBy(By.id("org.chromium.chrome:id/checkbox")).click();
        closeWindow();
    }

    /*C122590 Verify that the user can modify the size of text scaling and switch to Desktop view
   1.Open chromium in locker
   2.Click the three dots in the upper right corner of the chromium page
   3.Check on Desktop site
   4.Click on Settings->Accessibility
   5.Drag the slider to increase the value of Text Scaling
   6.Return to the Chromium homepage to view the text on the page
   7.Drag the slider to decrease the value of Text Scaling
   8.Return to the Chromium homepage to view the text on the page
   9.Switch to mobile site mode，and return to the Chromium homepage to view the text on the page
    Author:Sita */
    @Test(groups = "P1")
    public void C122590UserCanModifySize(){
        TestRail.setTestRailId("122590");
        TestRail.addStepName("Open chromium in locker");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(MAXIMIZE);
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("Click the three dots in the upper right corner of the chromium page");
        TestRail.addStepName("Check on Desktop site");
        TestRail.addStepName("Click on Settings->Accessibility");
        TestRail.addStepName("Drag the slider to increase the value of Text Scaling");
        increaseTextScalingValue();
        TestRail.addStepName("Return to the Chromium homepage to view the text on the page");
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        //All text on the page will become larger
        ElementHelper.waitUntilPresent(NAVIGATE_BUTTON);
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        TestRail.addStepName("Drag the slider to decrease the value of Text Scaling");
        decreaseTextScalingValue();
        TestRail.addStepName("Return to the Chromium homepage to view the text on the page");
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        //All text on the page will become smaller
        ElementHelper.waitUntilPresent(NAVIGATE_BUTTON);
        ElementHelper.clickWhenVisible(NAVIGATE_BUTTON);
        TestRail.addStepName("Switch to mobile site mode，and return to the Chromium homepage to view the text on the page");
        ElementHelper.click(CHROMIUM_ROOT);
        ElementHelper.swipe(desktopSite, Direction.UP);//Switch to mobile site mode
        AppiumHelper.findElementBy(By.id("org.chromium.chrome:id/checkbox")).click();
        closeWindow();
    }

    private void navigateChromiumBookMark() throws InterruptedException {
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(MAXIMIZE);
        AppiumHelper.switchWindowToFullScreen();
        AppiumHelper.findElementBy(By.id("org.chromium.chrome:id/url_bar")).sendKeys("https://portal.mypromethean.com");
        AppiumHelper.findElementBy(Locator.byText("https://portal.mypromethean.com")).click();
        Thread.sleep(3000);
        AppiumHelper.findElementBy(Locator.byText("myPromethean")).click();
        ElementHelper.click(CHROMIUM_ROOT);
        ElementHelper.clickWhenVisible(ADD_HOME_SCREEN);
        ElementHelper.click(Locator.byText("Add"));
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
        systemPO.startLocker();
        ElementHelper.clickWhenVisible(SECOND_TAB);
    }

    private void increaseTextScalingValue() {
        ElementHelper.clickWhenVisible(CHROMIUM_ROOT);
        AppiumHelper.swipeByElements(Locator.byText("View source"), Locator.byText("Bookmarks"));
        ElementHelper.click(CHROMIUM_SETTINGS);
        ElementHelper.waitUntilPresent(Locator.byText("Theme"));
        AppiumHelper.swipeByElements(Locator.byText("Theme"), Locator.byText("Search engine"));
        AppiumHelper.swipeByElements(Locator.byText("User Scripts"), Locator.byText("Advanced"));
        ElementHelper.click(Locator.byText("Accessibility"));
        ElementHelper.waitUntilPresent(By.xpath("//android.widget.LinearLayout/android.widget.SeekBar"), 10);
        AppiumHelper.setSeekBar(1500, "//android.widget.LinearLayout/android.widget.SeekBar");
    }

    private void decreaseTextScalingValue() {
        ElementHelper.clickWhenVisible(CHROMIUM_ROOT);
        AppiumHelper.swipeByElements(Locator.byText("View source"), Locator.byText("Bookmarks"));
        ElementHelper.click(CHROMIUM_SETTINGS);
        ElementHelper.waitUntilPresent(Locator.byText("Theme"));
        AppiumHelper.swipeByElements(Locator.byText("Theme"), Locator.byText("Search engine"));
        AppiumHelper.swipeByElements(Locator.byText("User Scripts"), Locator.byText("Advanced"));
        ElementHelper.click(Locator.byText("Accessibility"));
        ElementHelper.waitUntilPresent(By.xpath("//android.widget.LinearLayout/android.widget.SeekBar"), 10);
        AppiumHelper.setSeekBar(200, "//android.widget.LinearLayout/android.widget.SeekBar");
    }

    private void deleteBookmark() {
        systemPO.startLocker();
        ElementHelper.clickWhenVisible(SECOND_TAB);
        ElementHelper.longPress(MY_PROMETHEAN);
        ElementHelper.clickWhenVisible(DELETE_SHORTCUT);
        ElementHelper.clickWhenVisible(Locator.byText("Delete"));
    }

    private void closeWindow() {
        try {
            AndroidElement closeButton = Driver.getAndroidDriver().findElementById("android:id/close_window");
            ElementHelper.waitUntilPresent(By.id("android:id/close_window"));
            closeButton.click();
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }
}
