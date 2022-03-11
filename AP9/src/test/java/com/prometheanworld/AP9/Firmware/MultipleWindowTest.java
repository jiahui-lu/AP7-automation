package com.prometheanworld.AP9.Firmware;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.SettingConfigurationPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.Assert;
import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.TestRailListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.nd.automation.core.kotlin.AssertKt.*;
@Listeners({TestRailListener.class, TestStatusListener.class})
public class MultipleWindowTest extends BaseTest {
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    SettingConfigurationPO configurationPO = POFactory.getInstance(SettingConfigurationPO.class);
    String ProjectPath=configurationPO.getProjectPath();
    String installTrapBallApk =ProjectPath + "\\updateFile\\apkFile\\TrapBall.apk";
    String installAppSearchApk =ProjectPath + "\\updateFile\\apkFile\\AppSearch.apk";
    String installLimitApk =ProjectPath + "\\updateFile\\apkFile\\limit.apk";

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        // Tap outside to hide the system UI
        ScreenHelper.clickAt(Location.CENTER_TOP);
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
    }

    /**C115434 Click back button to go back to the previous page
     * 1. Open windowed app, such as Chromium app
     * 2. Search and go to any website
     * 3. Click the back button in the window menu bar
     * 4. Open another windowed app, such as Settings app, click any option to go to another page
     * 5. Click the back button in the window menu bar
     * Author:Sita */
    @Test(groups = "P1")
    public void C115434ClickBackButtonToGoBackToPreviousPage(){
        TestRail.setTestRailId("115434");
        TestRail.addStepName("Open windowed app, such as Chromium app");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        TestRail.addStepName("Search and go to any website");
        ElementHelper.clickWhenVisible(By.id("org.chromium.chrome:id/url_bar"));
        AndroidElement url = Driver.getAndroidDriver().findElementById("org.chromium.chrome:id/url_bar");
        url.sendKeys("www.google.com");
        AppiumHelper.waitForSeconds(2);
        Driver.getAndroidDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
        TestRail.addStepName("Click the back button in the window menu bar");
        AppiumHelper.waitForSeconds(5);
        ElementHelper.clickWhenVisible(By.id("android:id/back_window"));
        //Chromium app back to the previous page properly
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("https://portal.mypromethean.com")));
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
        TestRail.addStepName("Open another windowed app, such as Settings app, click any option to go to another page");
        systemPO.startAppFromUnifiedLauncher("Settings");
        ElementHelper.clickWhenVisible(Locator.byText("Display"));
        TestRail.addStepName("Click the back button in the window menu bar");
        AppiumHelper.waitForSeconds(5);
        ElementHelper.clickWhenVisible(By.id("android:id/back_window"));
        //Settings app back to the previous page properly
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
    }

    /**
     * C114919 Switch window from full screen to floating mode
     * Steps:
     * 1. Open windowed app and it is on full screen
     * 2. Click Reduce button ---- Switch to floating mode successfully
     * 3. Repeat above steps 10 times ---- Panel should not crash
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114919SwitchFullscreenWindowToFloatingMode() {
        TestRail.setTestRailId("114919");

        TestRail.addStepName("Open windowed app and it is on full screen");
        systemPO.startAppFromUnifiedLauncher("Whiteboard");
        AppiumHelper.waitForSeconds(2);
        systemPO.confirmWindowStates("full screen");

        TestRail.addStepName("Repeat above steps 10 times");
        for(int i=1;i<11;i++) {
            TestRail.addStepName("Click Reduce button");
            Log.info("------The " + i + " time------");
            ElementHelper.click(By.xpath(systemPO.eleMaximizeApp));
            AppiumHelper.waitForSeconds(1);
            systemPO.confirmWindowStates("floating mode");
            //Click Full Screen button
            ElementHelper.click(By.xpath(systemPO.eleMaximizeApp));
            AppiumHelper.waitForSeconds(1);
            systemPO.confirmWindowStates("full screen");
        }

        systemPO.closeAppOnMenuBar("Whiteboard");
    }

    /**
     * C114920 Click the close button to close windowed apps
     * Steps:
     * 1. Open multiple windowed apps, such as Settings and Chromium app
     * 2. Click the Close button to close Settings app ---- Settings app closed and no Settings app icon in taskbar
     *  ---- Chromium app still opens and there is a Chromium app icon in taskbar
     * 3. Click the Close button to close Chromium app ---- Chromium app closed and no Chromium app icon in taskbar
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114920ClickTheCloseButtonToCloseWindowedApps() {
        TestRail.setTestRailId("114920");

        TestRail.addStepName("Open multiple windowed apps, such as Settings and Chromium app");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.showTaskbar();
        ElementHelper.findElement(Locator.byText("Chromium"));
        ElementHelper.findElement(Locator.byText("Settings"));
        // Tap outside to hide the system UI
        ScreenHelper.clickAt(Location.CENTER_TOP);

        TestRail.addStepName("Click the Close button to close Settings app");
        ElementHelper.click(By.xpath(systemPO.eleCloseApp));
        AppiumHelper.showTaskbar();
        ElementHelper.findElement(Locator.byText("Chromium"));
        ElementHelper.waitUntilNotPresent(Locator.byText("Settings"));
        // Tap outside to hide the system UI
        ScreenHelper.clickAt(Location.CENTER_TOP);

        TestRail.addStepName("Click the Close button to close Chromium app");
        ElementHelper.click(By.xpath(systemPO.eleCloseApp));
        AppiumHelper.showTaskbar();
        ElementHelper.waitUntilNotPresent(Locator.byText("Chromium"));
        // Tap outside to hide the system UI
        ScreenHelper.clickAt(Location.CENTER_TOP);
    }

    /**
     * C114921 Click the minimize button to minimize window when in full screen
     * Steps:
     * 1. Open a windowed app and switch to full screen
     * 2. Click the minimize button to minimize it ---- The app moves to the bottom to hide
     *  ---- The app is not displayed in panel, and there is still an app icon in taskbar
     * 3. Click the app icon in taskbar  ---- The app moves from the button to the top to display
     *   ---- This app opens in full screen and runs properly
     * 4. Repeat above steps 10 times ---- Panel should not crash
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114921ClickMinimizeButtonToMinimizeWindowWhenInFullScreen() {
        TestRail.setTestRailId("114921");

        TestRail.addStepName("Open a windowed app and switch to full screen");
        systemPO.startAppFromUnifiedLauncher("Whiteboard");
        AppiumHelper.waitForSeconds(1);
        systemPO.confirmWindowStates("full screen");

        TestRail.addStepName("Repeat above steps 10 times");
        for(int i=1;i<11;i++) {
            Log.info("------The " + i + " time------");
            TestRail.addStepName("Click the minimize button to minimize it");
            ElementHelper.click(By.xpath(systemPO.eleMinimizeApp));
            AppiumHelper.waitForSeconds(1);
            // The app moves to the bottom to hide
            assertNotEquals(Driver.getAndroidDriver().getCurrentPackage(), "com.prometheanworld.whiteboard", "Minimize window failed");
            // The app is not displayed in panel, and there is still an app icon in taskbar
            AppiumHelper.showTaskbar();
            ElementHelper.findElement(Locator.byText("Whiteboard"));

            TestRail.addStepName("Click the app icon in taskbar");
            systemPO.startAppFromUnifiedLauncher("Whiteboard",true);
            // The app moves from the button to the top to display
            assertEquals(Driver.getAndroidDriver().getCurrentPackage(), "com.prometheanworld.whiteboard", "Maximize window failed");
            // This app opens in full screen and runs properly
            systemPO.confirmWindowStates("full screen");
        }

        systemPO.closeAppOnMenuBar("Whiteboard");
    }

    /**
     * C114925 Set a windowed application to left half screen
     * Steps:
     * 1. Open a windowed app, such as Chromium app
     * 2. Click "Half Screen - left" button ---- The app scales to half the screen and is on the left side, and there is still a bottom bar on it with different buttons available without the resize icon
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114925SetWindowedApplicationToLeftHalfScreen() {
        TestRail.setTestRailId("114925");

        TestRail.addStepName("Open a windowed app, such as Chromium app");
        systemPO.startAppFromUnifiedLauncher("Chromium");

        TestRail.addStepName("Click \"Half Screen - left\" button");
        ElementHelper.click(By.xpath(systemPO.eleLeftSideApp));
        systemPO.confirmWindowStates("left half");

        systemPO.closeAppOnMenuBar("Chromium");
    }

    /**
     * C114926 Set a windowed application to right half screen
     * Steps:
     * 1. Open a windowed app, such as Chromium app
     * 2. Click "Half Screen - right" button ---- The app scales to half the screen and is on the right side, and there is still a bottom bar on it with different buttons available without the resize icon
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114926SetWindowedApplicationToRightHalfScreen() {
        TestRail.setTestRailId("114926");

        TestRail.addStepName("Open a windowed app, such as Chromium app");
        systemPO.startAppFromUnifiedLauncher("Chromium");

        TestRail.addStepName("Click \"Half Screen - right\" button");
        ElementHelper.click(By.xpath(systemPO.eleRightSideApp));
        systemPO.confirmWindowStates("right half");

        systemPO.closeAppOnMenuBar("Chromium");
    }

    /**
     * C114928 Verify that users can resize the window
     * Steps:
     * 1. Open a windowed app and switch to floating mode
     * 2. Drag the icon in the lower right corner of the window to resize it ---- Confirm that window is resized to where your finger stops，displays and runs properly
     * 3. Drag window to minimum size ---- App displays and runs properly
     * 4. Drag window to maximum size ---- App displays and runs properly
     * 5. Repeat above steps 10 times  ---- Panel should not crash
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114928UsersCanResizeTheWindow() {
        TestRail.setTestRailId("114928");

        TestRail.addStepName("Open a windowed app and switch to floating mode");
        systemPO.startAppFromUnifiedLauncher("Chromium");

        AppiumHelper.waitForSeconds(1);
        TestRail.addStepName("Drag the icon in the lower right corner of the window to resize it");
        Dimension first_dimension =systemPO.getWindowSize();
        // Drag window to minimum size
        systemPO.resizeWindow(100,100);
        Dimension second_dimension =systemPO.getWindowSize();
        assertNotEquals(first_dimension,second_dimension,"Resize window failed");
        assertEquals(Driver.getAndroidDriver().getCurrentPackage(), "org.chromium.chrome", "Chromium app does not opened");

        // move window to the top left corner
        Point first_point=systemPO.getWindowPosition();
        systemPO.moveWindow(200,0);
        AppiumHelper.waitForSeconds(2);
        Point second_point=systemPO.getWindowPosition();
        assertNotEquals(first_point,second_point,"Move window failed");

        TestRail.addStepName("Repeat above steps 10 times");
        for(int i=1;i<11;i++){
            Log.info("------The " + i + " time------");
            TestRail.addStepName("Drag window to maximum size");
            systemPO.resizeWindow(1920,1080);
            // App displays and runs properly
            assertEquals(Driver.getAndroidDriver().getCurrentPackage(), "org.chromium.chrome", "Chromium app does not opened");
            TestRail.addStepName("Drag window to minimum size");
            systemPO.resizeWindow(100,0);
            // App displays and runs properly
            assertEquals(Driver.getAndroidDriver().getCurrentPackage(), "org.chromium.chrome", "Chromium app does not opened");
        }

        systemPO.closeAppOnMenuBar("Chromium");
    }

    /**
     * C114929 Verify that users can resize multiple windows separately
     * Steps:
     * 1. Open multiple windowed apps and switch them to floating mode
     * 2. Drag the icon in the lower right corner of the windows to resize them ---- Confirm that apps can be resized separately, display and run properly
     * Author: huiqian.chen
     */
    @Test(groups = "P1")
    public void C114929UsersCanResizeMultipleWindowsSeparately() {
        TestRail.setTestRailId("114929");

        TestRail.addStepName("Open multiple windowed apps and switch them to floating mode");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        systemPO.startAppFromUnifiedLauncher("Settings");

        TestRail.addStepName("Drag the icon in the lower right corner of the windows to resize them");
        // Resize Settings app window
        AppiumHelper.waitForSeconds(1);
        systemPO.resizeWindow(900,900);
        Dimension first_dimension = systemPO.getWindowSize();

        // Resize Chromium app window
        systemPO.startAppFromUnifiedLauncher("Chromium",true);
        AppiumHelper.waitForSeconds(1);
        systemPO.resizeWindow(800,800);
        Dimension second_dimension = systemPO.getWindowSize();
        assertNotEquals(first_dimension,second_dimension,"Resize windows failed");

        systemPO.closeAppOnMenuBar("Chromium");
        systemPO.closeAppOnMenuBar("Settings");
    }

    /**C114763 Apps open in Flexible Window
     * Steps
     * 1.Open Android Settings app from Locker
     * 2.Open Chromium app from Locker
     * 3.Open Files app from Locker
     * Author:Sita */
    @Test(groups = "P1")
    public void C114763AppsOpenInFlexibleWindow() {
        TestRail.setTestRailId("114763");
        TestRail.addStepName("Open Android Settings app from Locker");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("floating mode");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        TestRail.addStepName("Open Chromium app from Locker");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("floating mode");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        TestRail.addStepName("Open Files app from Locker");
        systemPO.startAppFromUnifiedLauncher("Files");
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("floating mode");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));    }

    /**C114909 Apps open in Full Screen with Window Bar
     * Steps
     * 1.Open Whiteboard app from Locker
     * 2.Open VLC app from Locker
     * 3.Open Camera app from Locker
     * 4.Open Adobe Acrobat app from Locker
     * 5.Open Radix(Viso) app from Locker
     * 6.Open Promethean Blockly app from Locker
     * Author:Sita */
    @Test(groups = "P1")
    public void C114909AppsOpenInFullScreenWithWindowBar() {
        TestRail.setTestRailId("114909");
        TestRail.addStepName("Open Whiteboard app from Locker");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Whiteboard");
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("full screen");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        TestRail.addStepName("Open VLC app from Locker");
        systemPO.startAppFromUnifiedLauncher("VLC");
        if(ElementHelper.isVisible(Locator.byText("Yes"))==true) {
            ElementHelper.clickWhenVisible(Locator.byText("YES"));}
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("full screen");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        TestRail.addStepName("Open Camera app from Locker");
        systemPO.startAppFromUnifiedLauncher("Camera");
        if(ElementHelper.isVisible(Locator.byText("No camera detected"))==false) {
            //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
            CheckWindowBar();
            ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));}
        if (ElementHelper.isVisible(Locator.byText("No camera detected"))==true) {
            ElementHelper.clickWhenVisible(Locator.byText("CLOSE"));}

        TestRail.addStepName("Open Adobe Acrobat app from Locker");
        systemPO.startAppFromUnifiedLauncher("Adobe Acrobat");
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("full screen");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        TestRail.addStepName("Open Radix(Viso) app from Locker");
        systemPO.startAppFromUnifiedLauncher("Viso");
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("full screen");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        TestRail.addStepName("Open Promethean Blockly app from Locker");
        systemPO.startAppFromUnifiedLauncher("Promethean Blockly");
        if(ElementHelper.isVisible(Locator.byText("DENY"))==true) {
            ElementHelper.clickWhenVisible(Locator.byText("DENY"));}
        //App opens in Full Screen with a window bar, including Back, Half-screen left, Half-screen right, Full screen, Minimize, Close icons
        systemPO.confirmWindowStates("full screen");
        CheckWindowBar();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));    }

    /**C114910 Apps open in non-window state
     * Steps
     * 1.Apps open in non-window state: Annotate, Activities, Screen Capture, Screen share, Spinner, Timer, Panel Management, Update, AVI Settings, Source Switch, Locker
     * 2.Open the above apps
     * Author:Sita */
    @Test(groups = "P1")
    public void C114910AppsOpeninNonWindowState() {
        TestRail.setTestRailId("114910");
        TestRail.addStepName("Apps open in non-window state: Annotate, Activities, Screen Capture, Screenshare, Spinner, Timer, Panel Management, Update, AVI Settings, Source Switch, Locker");
        EnableWindowIsOn();
        TestRail.addStepName("Open the above apps");
        //Confirm that these apps are displayed in a non-windowed state with no window bar, and they all run and close properly
        systemPO.startAppFromUnifiedLauncher("Annotate");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        Adb.forceStop("com.prometheanworld.annotate");

        systemPO.startAppFromUnifiedLauncher("Activities");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        ElementHelper.clickWhenVisible(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[1]/android.view.View[2]/android.widget.ListView/android.view.View[3]/android.view.View"));

        systemPO.startAppFromUnifiedLauncher("Screen Capture");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        ElementHelper.clickWhenVisible(Locator.byText("Close"));

        systemPO.startAppFromUnifiedLauncher("Spinner");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        Adb.forceStop("com.prometheanworld.spinner");

        systemPO.startAppFromUnifiedLauncher("Timer");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        Adb.forceStop("com.prometheanworld.timer");

        systemPO.startAppFromUnifiedLauncher("Panel Management");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        ElementHelper.clickWhenVisible(By.id("com.nd.promethean.mdmagent:id/close_icon"));

        systemPO.startAppFromUnifiedLauncher("Update");
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        ElementHelper.clickWhenVisible(By.id("com.prometheanworld.update:id/btn_clse"));

        AppiumHelper.clickKey(PrometheanKey.Settings, PrometheanKeyboard.RemoteControl);
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));

        AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));

        systemPO.startLocker();
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));

        systemPO.startAppFromUnifiedLauncher("Screen Share");
        if(ElementHelper.isVisible(Locator.byText("DENY"))==true) {
            ElementHelper.clickWhenVisible(Locator.byText("DENY"));
            ElementHelper.clickWhenVisible(Locator.byText("DENY"));}
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        CloseApps("Screen Share","com.nd.promethean.casting.receiver");
    }

    /**C114911 Mix multi-window apps and non-window apps
     * Steps
     * 1.Open Chromium app, then open Update app
     * 2.Open Update app, then open Chromium app
     * Author:Sita */
    @Test(groups = "P1")
    public void C114911MixMultiWindowAppsAndNonWindowApps() {
        TestRail.setTestRailId("114911");
        TestRail.addStepName("Open Chromium app, then open Update app");
        EnableWindowIsOn();
        //All the apps display correctly and run, close properly
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(Locator.byText("https://portal.mypromethean.com"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("https://portal.mypromethean.com")));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        systemPO.startAppFromUnifiedLauncher("Update");
        ElementHelper.waitUntilPresent(Locator.byText("Update ActivPanel"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update ActivPanel")));
        ElementHelper.clickWhenVisible(By.id("com.prometheanworld.update:id/btn_clse"));

        TestRail.addStepName("Open Update app, then open Chromium app");
        //All the apps display correctly and run, close properly
        systemPO.startAppFromUnifiedLauncher("Update");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update ActivPanel")));
        ElementHelper.clickWhenVisible(By.id("com.prometheanworld.update:id/btn_clse"));

        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.waitUntilPresent(Locator.byText("https://portal.mypromethean.com"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("https://portal.mypromethean.com")));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));    }

    /**C114912 Verify that app in multi-window mode cannot be dragged around when in full screen
     * Steps
     * 1.Open windowed app and switch to full screen
     * 2.Drag the bottom of the window to move
     * Author:Sita
     */
    @Test(groups = "P1")
    public void C114912VerifyAppInMultiWindowModeCannotBeDragged() {
        TestRail.setTestRailId("114912");
        EnableWindowIsOn();
        TestRail.addStepName("Open windowed app and switch to full screen");
        systemPO.startAppFromUnifiedLauncher("Settings");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMaximizeApp));
        TestRail.addStepName("Drag the bottom of the window to move");
        confirmWindowbottomcolumn("multi window");
        systemPO.moveWindow(1002,1056);
        //Window cannot move left and right
        systemPO.confirmWindowStates("full screen");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));    }

    /**C114913 Verify that app in multi-window mode can be dragged around when in floating mode
     * Steps
     * 1.Open windowed app and switch to floating mode
     * 2.Drag the bottom of the window to move
     * 3.Move window to the top/bottom
     * 4.Drag the bottom of the window to move to the far left/right
     * 5.Move window 10 times
     * Author:Sita */
    @Test(groups = "P1")
    public void C114913VerifyAppInMultiWindowModeCanBeDragged() {
        TestRail.setTestRailId("114913");
        EnableWindowIsOn();
        TestRail.addStepName("Open windowed app and switch to floating mode");
        systemPO.startAppFromUnifiedLauncher("Settings");
        TestRail.addStepName("Drag the bottom of the window to move");
        systemPO.moveWindow(234, 933);
        TestRail.addStepName("Move window to the top/bottom");
        systemPO.moveWindow(800, 600);
        systemPO.moveWindow(800,1400);
        //Window movement has upper and lower limits
        TestRail.addStepName("Drag the bottom of the window to move to the far left/right");
        systemPO.moveWindow(0,920);
        systemPO.moveWindow(1500, 0);
        TestRail.addStepName("Move window 10 times");
        int i = 0;
        while (i < 11) {
            systemPO.moveWindow(800, 600);
            systemPO.moveWindow(800,1400);
            systemPO.moveWindow(0,920);
            systemPO.moveWindow(1500, 0);
            i++;
        }
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMaximizeApp));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));    }

    /**C114914 Drag up a full screen window and switch it to floating mode
     * Steps
     * 1.Open windowed app and switch to full screen
     * 2.Touch the bottom bar within the app and drag the app up
     * Author:Sita */
    @Test(groups = "P1")
    public void C114914DragUpFullscreenWindow() {
        TestRail.setTestRailId("114914");
        EnableWindowIsOn();
        TestRail.addStepName("Open windowed app and switch to full screen");
        systemPO.startAppFromUnifiedLauncher("Files");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMaximizeApp));
        TestRail.addStepName("Touch the bottom bar within the app and drag the app up");
        //The full screen app should unlock and switch to a windowed state
        Dimension first_dimension = systemPO.getWindowSize();
        systemPO.moveWindow(234, 933);
        Dimension second_dimension = systemPO.getWindowSize();
        assertNotEquals(first_dimension,second_dimension,"The full screen app didn't unlock and couldn't switch to a windowed state");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114916 Open multiple windowed apps and move them separately
     * Steps
     * 1.Open multiple windowed apps and switch them to floating mode
     * 2.Drag the bottom of the windows to move
     * Author: Sita */
    @Test(groups = "P1")
    public void C114916OpenMultipleWindowedAppsAndMoveThemSeparately() {
        TestRail.setTestRailId("114916");
        TestRail.addStepName("Open multiple windowed apps and switch them to floating mode");
        EnableWindowIsOn();
        TestRail.addStepName("Drag the bottom of the windows to move");
        //Windows move to where your finger stops separately and can be overlapped
        systemPO.startAppFromUnifiedLauncher("Chromium");
        systemPO.startAppFromUnifiedLauncher("Settings");

        TestRail.addStepName("Drag the bottom of the windows to move");
        // Drag the bottom of the windows to move
        AppiumHelper.waitForSeconds(1);
        systemPO.moveWindow(900,900);
        Dimension first_dimension = systemPO.getWindowSize();

        // Drag the bottom of the windows to move
        systemPO.startAppFromUnifiedLauncher("Chromium",true);
        AppiumHelper.waitForSeconds(1);
        systemPO.moveWindow(800,800);
        Dimension second_dimension = systemPO.getWindowSize();
        assertNotEquals(first_dimension,second_dimension,"Drag windows failed");

        systemPO.closeAppOnMenuBar("Chromium");
        systemPO.closeAppOnMenuBar("Settings");
    }

    /**C114917 Switch window from floating mode to full screen
     * Steps
     * 1.Open a windowed application in floating mode
     * 2.Click the Full Screen button to make it full-screened
     * Author:Sita */
    @Test(groups = "P1")
    public void C114917SwitchWindowFromFloatingModeToFullScreen() {
        TestRail.setTestRailId("114917");
        EnableWindowIsOn();
        TestRail.addStepName("Open a windowed application in floating mode");
        systemPO.startAppFromUnifiedLauncher("Files");
        TestRail.addStepName("Click the Full Screen button to make it full-screened");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMaximizeApp));
        //App displays in full screen properly and no resize icon in the lower right corner
        Assert.assertFalse(ElementHelper.isVisible(By.xpath(systemPO.eleResizeApp)));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114915 Drag up a half screen window and switch it to floating mode
     * Step
     * 1.Open a windowed app
     * 2.Touch one of the half screen buttons on the app bar
     * 3.Touch the bottom bar within the app and drag the app up
     * Author:Sita */
    @Test(groups = "P1")
    public void C114915DragUpHalfScreenWindow(){
        TestRail.setTestRailId("114915");
        TestRail.addStepName("Open an windowed app");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");

        TestRail.addStepName("Touch one of the half screen buttons on the app bar");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleLeftSideApp));

        TestRail.addStepName("Touch the bottom bar within the app and drag the app up");
        //The half screen app should unlock and switch to a windowed state
        systemPO.moveWindow(600,100);
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114918 Switch multiple windows from floating mode to full screen
     *Steps
     * 1.Open multiple windowed apps, such as Settings and Chromium app
     * 2.Click the Full Screen button of Settings app to switch to full screen
     * 3.Click the Full Screen button of Chromium app to switch to full screen
     * Author:Sita */
    @Test(groups = "P2")
    public void C114918SwitchMultipleWindows(){
        TestRail.setTestRailId("114918");
        TestRail.addStepName("Open multiple windowed apps, such as Settings and Chromium app");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");
        systemPO.startAppFromUnifiedLauncher("Chromium");

        TestRail.addStepName("Click the Full Screen button of Settings app to switch to full screen");
        AppiumHelper.openAppsFromTaskBar("Settings");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMaximizeApp));
        //Confirm that Settings app is displayed in full screen and Chromium app is still displayed in floating mode
        systemPO.confirmWindowStates("full screen");
        AppiumHelper.openAppsFromTaskBar("Chromium");
        systemPO.confirmWindowStates("floating mode");

        TestRail.addStepName("Click the Full Screen button of Chromium app to switch to full screen");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMaximizeApp));
        //Confirm that both Settings and Chromium app are displayed in full screen
        systemPO.confirmWindowStates("full screen");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
        systemPO.confirmWindowStates("full screen");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114922 Click the minimize button to minimize window when in floating mode
     *Steps
     * 1.Open a windowed app and switch to floating mode
     * 2.Click the minimize button to minimize it
     * 3.Click the app icon in taskbar
     * Author:Sita */
    @Test(groups = "P2")
    public void C114922ClickMinimizeButtonToMinimize(){
        TestRail.setTestRailId("114922");
        TestRail.addStepName("Open a windowed app and switch to floating mode");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");

        TestRail.addStepName("Click the minimize button to minimize it");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMinimizeApp));
        //The app is not displayed in panel, and there is still an app icon in taskbar
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("Settings")));
        AppiumHelper.showTaskbar();
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));

        TestRail.addStepName("Click the app icon in taskbar");
        AppiumHelper.openAppsFromTaskBar("Settings");
        //It opens in floating mode and runs properly
        systemPO.confirmWindowStates("floating mode");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C115791 Click the minimize button to minimize window when in left/right half screen
     * Steps
     * 1.Open a windowed app and switch to left/right screen
     * 2.Click the minimize button to minimize it
     * 3.Click the app icon in taskbar
     * Author:Sita */
    @Test(groups = "P2")
    public void C115791ClickMinimizeButton(){
        TestRail.setTestRailId("115791");
        TestRail.addStepName("Open a windowed app and switch to left/right screen");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleLeftSideApp));

        TestRail.addStepName("Click the minimize button to minimize it");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMinimizeApp));
        //The app is not displayed in panel, and there is still an app icon in taskbar
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("Settings")));
        AppiumHelper.showTaskbar();
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));

        TestRail.addStepName("Click the app icon in taskbar");
        AppiumHelper.openAppsFromTaskBar("Settings");
        //This app opens in left/right screen and runs properly
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));
        systemPO.confirmWindowStates("left half");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114923 Click the minimize button to minimize window after resizing it
     * Steps
     * 1.Open a windowed app and switch to floating mode
     * 2.Drag the resize icon in the lower right corner of the window to resize it
     * 3.Click the minimize button to minimize it
     * 4.Click the app icon in taskbar
     * Author:Sita */
    @Test(groups = "P2")
    public void C114923ClickMinimizeButtonToMinimizeWindowAfterResizing(){
        TestRail.setTestRailId("114923");
        TestRail.addStepName("Open a windowed app and switch to floating mode");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");
        TestRail.addStepName("Drag the resize icon in the lower right corner of the window to resize it");
        systemPO.resizeWindow(800,800);
        Dimension first_dimension = systemPO.getWindowSize();

        TestRail.addStepName("Click the minimize button to minimize it");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMinimizeApp));
        //The app is not displayed in panel, and there is still an app icon in taskbar
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("Settings")));
        AppiumHelper.showTaskbar();
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));

        TestRail.addStepName("Click the app icon in taskbar");
        AppiumHelper.openAppsFromTaskBar("Settings");
        //The app moves from the bottom to top to display and open in resized window and run properly
        Dimension second_dimension = systemPO.getWindowSize();
        assertEquals(first_dimension,second_dimension,"Resize windows failed");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114924 Click the minimize button to minimize window after moving it
     * Steps
     * 1.Open a windowed app and switch to floating mode
     * 2.Drag the bottom of the window to move it
     * 3.Click the minimize button to minimize it
     * 4.Click the app icon in taskbar
     * Author:Sita */
    @Test(groups = "P2")
    public void C114924ClickMinimizeButtonToMinimizeWindowAfterMoving(){
        TestRail.setTestRailId("114924");
        TestRail.addStepName("Open a windowed app and switch to floating mode");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Settings");

        TestRail.addStepName("Drag the bottom of the window to move it");
        systemPO.moveWindow(600,100);
        Dimension first_dimension = systemPO.getWindowSize();

        TestRail.addStepName("Click the minimize button to minimize it");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleMinimizeApp));
        //The app is not displayed in panel, and there is still an app icon in taskbar
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("Settings")));
        AppiumHelper.showTaskbar();
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));

        TestRail.addStepName("Click the app icon in taskbar");
        AppiumHelper.openAppsFromTaskBar("Settings");
        //The app moves from the bottom to the top to display open in the moved position and run properly
        Dimension second_dimension = systemPO.getWindowSize();
        assertEquals(first_dimension,second_dimension,"Resize windows failed");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Settings")));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114927 Switching windows in the left and right half screens
     * Steps
     * 1.Open a windowed app, such as Chromium app
     * 2.Click "Half Screen - left" button
     * 3.Click "Half Screen - right" button
     * 4.Repeat above steps 10 times
     * author:Sita */
    @Test(groups = "P2")
    public void C114927SwitchingWindowInHalfScreens(){
        TestRail.setTestRailId("114927");
        TestRail.addStepName("Open a windowed app, such as Chromium app");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Chromium");

        TestRail.setTestRailId("Click Half Screen - left button");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleLeftSideApp));
        //The app scales to half the screen and is on the left side
        systemPO.confirmWindowStates("left half");

        TestRail.setTestRailId("Click Half Screen - right button");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleRightSideApp));
        //The app scales to half the screen and is on the right side
        systemPO.confirmWindowStates("right half");

        TestRail.addStepName("Repeat above steps 10 times");
        //Panel should not crash
        int i = 0;
        while (i < 11) {
            ElementHelper.clickWhenVisible(By.xpath(systemPO.eleLeftSideApp));
            ElementHelper.clickWhenVisible(By.xpath(systemPO.eleRightSideApp));
            i++;
        }
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    /**C114932 Open the same app multiple times
     * Steps
     * 1.Open a windowed app, such as Chromium app
     * 2.Click the app icon again
     * Author:Sita */
    @Test(groups = "P2")
    public void C114932OpenSameAppMultipleTimes(){
        TestRail.setTestRailId("114932");
        TestRail.addStepName("Open a windowed app, such as Chromium app");
        EnableWindowIsOn();
        systemPO.startAppFromUnifiedLauncher("Chromium");

        TestRail.addStepName("Click the app icon again");
        AppiumHelper.openAppsFromTaskBar("Chromium");
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
        //Confirm that cannot open a second instance of the app
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("Chromium")));
    }

    /**C114936 Open on-screen keyboard when in full screen window
     *Steps
     * 1.Open on-screen keyboard in these pages(all these pages are in full screen):
     * a. Settings -> Wifi -> enter Wifi password page
     * b. Settings -> Security -> Screen Lock -> set PIN page
     * c. Chromium home page -> enter text in the navigation bar
     * d. www.Youtube.com page -> enter text in the search box
     * Author:Sita */
    @Test(groups = "P2")
    public void C114936OpenKeyboardWhenInFullScreenWindow() {
        TestRail.setTestRailId("114936");
        TestRail.addStepName("Open on-screen keyboard in these pages(all these pages are in full screen)");
        EnableWindowIsOn();
        //a. Settings -> Wifi -> enter Wifi password page
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        ElementHelper.clickWhenVisible(Locator.byText("Network & internet"));
        ElementHelper.clickWhenVisible(By.xpath("//android.widget.TextView[@content-desc=\"Search settings\"]"));
        AppiumHelper.findElementBy(Locator.byResourceId("android:id/search_src_text")).sendKeys("Guest Wireless");
        AppiumHelper.waitForSeconds(5);
        //Confirm that the on-screen keyboard cannot mess up maximized window
        systemPO.confirmWindowStates("full screen");
        systemPO.closeAppOnMenuBar("Settings");

        //b.Chromium home page -> enter text in the navigation bar
        systemPO.startAppFromUnifiedLauncher("Chromium");
        AppiumHelper.switchWindowToFullScreen();
        ElementHelper.clickWhenVisible(By.id("org.chromium.chrome:id/url_bar"));
        AppiumHelper.findElementBy(Locator.byResourceId("org.chromium.chrome:id/url_bar")).sendKeys("www.youtube.com");
        AppiumHelper.waitForSeconds(5);
        ElementHelper.clickWhenVisible(Locator.byText("www.youtube.com/"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("search"));
        //Confirm that the on-screen keyboard cannot mess up maximized window
        systemPO.confirmWindowStates("full screen");

        //c. www.Youtube.com page -> enter text in the search box
        ElementHelper.clickWhenVisible(By.xpath("/hierarchy/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView/android.view.View[1]/android.view.View[2]/android.view.View[1]/android.view.View/android.view.View/android.view.View[2]/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText"));
        AndroidElement url = Driver.getAndroidDriver().findElementByXPath("/hierarchy/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView/android.view.View[1]/android.view.View[2]/android.view.View[1]/android.view.View/android.view.View/android.view.View[2]/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText");
        url.sendKeys("Coco-Melon");
        AppiumHelper.waitForSeconds(5);
        ElementHelper.clickWhenVisible(Locator.byText("Search"));
        //Confirm that the on-screen keyboard cannot mess up maximized window
        systemPO.confirmWindowStates("full screen");
        systemPO.closeAppOnMenuBar("Chromium");
    }

    /**C115787 Verify that 3rd party apps open in full screen with Minimize, Back and Close buttons
     * Step
     * 1.Sideload 3rd party apps, such as PowerPoint, Word or Excel app
     * 2.Open 3rd party apps
     * Author:Sita */
    @Test(groups = {"P2", "UserDebug"})
    public void C115787Verify3rdPartyAppsOpenInFullScreen() throws IOException, IOException {
        TestRail.setTestRailId("115787");
        TestRail.addStepName("Sideload 3rd party apps, such as PowerPoint, Word or Excel app");
        AppiumHelper.exeAdbRoot();
        Runtime.getRuntime().exec("adb shell setprop persist.apkinstall.enable true");

        TestRail.addStepName("Open 3rd party apps");
        Adb.installApk(installTrapBallApk);
        systemPO.startAppFromUnifiedLauncher("Trapball");
        if(ElementHelper.isVisible(Locator.byText("OK"))==true){
            ElementHelper.clickWhenVisible(Locator.byText("OK"));}
        ElementHelper.waitUntilVisible(By.xpath(systemPO.eleMinimizeApp));
        //1.Display all applications open as full-screen applications and display the bar
        //2.Only Minimize, Back and Close buttons are displayed on the bar
        systemPO.confirmWindowStates("full screen");
        confirmWindowDisplay();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        Adb.installApk(installAppSearchApk);
        systemPO.startAppFromUnifiedLauncher("FAST App Search Tool");
        ElementHelper.waitUntilVisible(By.xpath(systemPO.eleBackApp));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleBackApp));
        confirmWindowDisplay();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        Adb.installApk(installLimitApk);
        systemPO.startAppFromUnifiedLauncher("limit");
        ElementHelper.waitUntilVisible(By.xpath(systemPO.eleBackApp));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleBackApp));
        confirmWindowDisplay();
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));

        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        ElementHelper.clickWhenVisible(Locator.byText("Apps"));
        ElementHelper.clickWhenVisible(Locator.byText("limit"));
        ElementHelper.clickWhenVisible(Locator.byText("UNINSTALL"));
        ElementHelper.clickWhenVisible(Locator.byText("OK"));

        ElementHelper.clickWhenVisible(Locator.byText("FAST App Search Tool"),10);
        ElementHelper.clickWhenVisible(Locator.byText("UNINSTALL"));
        ElementHelper.clickWhenVisible(Locator.byText("OK"));

        ElementHelper.clickWhenVisible(Locator.byText("Trapball"),10);
        ElementHelper.clickWhenVisible(Locator.byText("UNINSTALL"));
        ElementHelper.clickWhenVisible(Locator.byText("OK"));
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }

    public void confirmWindowDisplay(){
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleBackApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleMinimizeApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleCloseApp)));
    }

    private void CloseApps(String appName, String packageName) {
        if (appName.equals("Spinner") || appName.equals("Timer") || appName.equals("Annotate") || appName.equals("Screen Share") || appName.equals("Cloud Connect")) {
            CommonOperator.executeShellCommand("am force-stop ", packageName);
        } else {
            AppiumHelper.CloseAppFromBottomRecentApps(appName);
        }
        Log.info("-------------Close " + appName +"---------------");
        AppiumHelper.hideUnifiedMenu();
    }

    private void confirmWindowbottomcolumn(String windowStyle) {
        switch (windowStyle) {
            case "multi window":
                AssertKt.assertPresent(By.id("android:id/caption"), 3);
                AssertKt.assertPresent(By.id("android:id/right_side_window"), 3);
                break;
            case "popup window":
                AssertKt.assertNotPresent(By.id("android:id/caption"), 3);
                AssertKt.assertNotPresent(By.id("android:id/right_side_window"), 3);
                break;
        }
    }

    private void CheckWindowBar(){
        ElementHelper.waitUntilPresent(By.xpath(systemPO.eleBackApp));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleBackApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleLeftSideApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleRightSideApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleMaximizeApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleMinimizeApp)));
        Assert.assertTrue(ElementHelper.isVisible(By.xpath(systemPO.eleCloseApp)));
    }

    private String getEnableWindowingSwitchWidget() {
        return "//*[ends-with(@resource-id, 'id/list')]" +
                "/*[descendant::*[@text='Enable windowing']]//*[ends-with(@resource-id, 'id/switch_widget')]";
    }

    private void EnableWindowIsOn() {
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        ElementHelper.clickWhenVisible(Locator.byText("Display"));
        if (!ElementHelper.isChecked(By.xpath(getEnableWindowingSwitchWidget()))) {
            ElementHelper.click(Locator.byText("Enable windowing"));
        }
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));
    }
}