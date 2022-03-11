package com.prometheanworld.appium.Panel.SystemFunction.SystemBasicFunction;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.LockerPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Listeners({TestRailListener.class})
public class MultiWindows extends BaseTest {
    AndroidElement windowPosition;

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        ScreenHelper.clickAt(Location.CENTER);
    }

    /** Description: Use AP7_A or AP7_B panel Open a full screen app can run normal */
    @Test
    public void Open_a_full_screen_app_can_run_normal_test() {
        TestRail.setTestRailId("83292");

        AppiumHelper.StartAppFromUnifiedLauncher("Files");
        confirmWindowStates("full screen");
        confirmWindowbottomcolumn("multi window");
        closeWindow();
    }

    /** Description: Open a floating window app, app displays correctly as a floating window app*/
    @Test
    public void open_popup_window_app_can_run_normal_test() {
        TestRail.setTestRailId("83293, 83314");

        openAndCloseFloatingApps("Update",null);
        openAndCloseFloatingApps("Spinner","com.prometheanworld.spinner");
        openAndCloseFloatingApps("Timer","com.prometheanworld.timer");
        openAndCloseFloatingApps("Annotate","com.prometheanworld.annotate");
        openAndCloseFloatingApps("Screen Share","com.nd.promethean.casting.receiver");
        openAndCloseFloatingApps("Activities",null);
        openAndCloseFloatingApps("FreeBox",null);
        openAndCloseFloatingApps("Cloud Connect","com.prometheanworld.cloudcoupler");
        openAndCloseFloatingApps("Help",null);
        AndroidElement closeIcon;
        AppiumHelper.StartAppFromUnifiedLauncher("Panel Management");
        AssertKt.assertNotPresent(By.id("android:id/caption"), 3);
        try {
            closeIcon = Driver.getAndroidDriver().findElementById("com.nd.promethean.mdmagent:id/close_icon");
            closeIcon.click();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.info("Cannot find close button of Panel Management");
        }
        AppiumHelper.hideUnifiedMenu();
    }

    /** Description: Click "Half Screen - left" button and "Half Screen - right" button*/
    @Test
    public void click_Half_Screen_button_to_adjust_the_window_test() {
        TestRail.setTestRailId("83308, 83309, 83310");

        AppiumHelper.StartAppFromUnifiedLauncher("Whiteboard");
        for (int i=1;i<11 ;i++) {
            switchWindowPosition("left half");
            switchWindowPosition("right half");
            Log.info("Cycle number: " + i);
        }
        closeWindow();
    }

    /*** Description: Use AP7_A or AP7_B panel Click_the_zoomed_button_to_adjust_the_window */
    @Test
    public void Click_the_zoomed_button_to_adjust_the_window_test() {
        TestRail.setTestRailId("83300");

        AppiumHelper.StartAppFromUnifiedLauncher("Music Player");
        confirmWindowStates("full screen");
        switchWindowPosition("floating mode");
        confirmWindowStates("floating mode");
        switchWindowPosition("full screen");
        confirmWindowStates("full screen");
        AppiumHelper.StartAppFromUnifiedLauncher("Chromium");
        confirmWindowStates("floating mode");
        switchWindowPosition("full screen");
        confirmWindowStates("full screen");
        switchWindowPosition("floating mode");
        confirmWindowStates("floating mode");
        confirmWindowbottomcolumn("multi window");
        AppiumHelper.CloseAppFromBottomRecentApps("Chromium");
        ScreenHelper.clickAt(0.8,0.8);
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.CloseAppFromBottomRecentApps("Music Player");
        ScreenHelper.clickAt(0.8,0.8);
    }

    /** Description: Use AP7_A or AP7_B panel Click the minimize button to minimize window when in full screen,then on Recent Apps open Adobe Acrobat app */
    @Test
    public void Click_the_minimize_button_to_minimize_window_when_in_full_screen_test() {
        TestRail.setTestRailId("83304");

        AppiumHelper.StartAppFromUnifiedLauncher("Adobe Acrobat");
        confirmWindowStates("full screen");
        minimizeWindow();
        confirmAppOnRecentApps("openapps","Adobe Acrobat");
        AppiumHelper.openAppInRecentAppsPrivate("Adobe Acrobat");
        confirmWindowStates("full screen");
        ScreenHelper.clickAt(0.8,0.8);
        closeWindow();
    }

    /**Description: Click the minimize button to minimize window when in floating window mode,then on Recent Apps open Whiteboard app
     */
    @Test
    public void Click_the_minimize_button_to_minimize_window_when_in_floating_window_mode_Test() {
        TestRail.setTestRailId("83305");

        AppiumHelper.StartAppFromUnifiedLauncher("Whiteboard");
        switchWindowPosition("floating mode");
        minimizeWindow();
        confirmAppOnRecentApps("openapps","Whiteboard");
        AppiumHelper.openAppInRecentAppsPrivate("Whiteboard");
        confirmWindowStates("floating mode");
        ScreenHelper.clickAt(0.8,0.8);
        closeWindow();
    }

    /** Description: Click the minimize button to minimize window after resizing it*/
    @Test
    public void Click_the_minimize_button_to_minimize_window_after_resizing_it_Test() {
        TestRail.setTestRailId("83306");

        AppiumHelper.StartAppFromUnifiedLauncher("Gallery");
        switchWindowPosition("floating mode");
        resizeWindow();
        Dimension beforeSize=getWindowSize("android:id/content");
        minimizeWindow();
        confirmAppOnRecentApps("openapps","Gallery");
        AppiumHelper.openAppInRecentAppsPrivate("Gallery");
        ScreenHelper.clickAt(0.8,0.8);
        Dimension afterSize = getWindowSize("android:id/content");
        assertEquals(afterSize, beforeSize,"The size of the window changes");
        closeWindow();
    }

    /** Description: Click the minimize button to minimize window after moving it*/
    @Test
    public void Click_the_minimize_button_to_minimize_window_after_moving_it_Test(){
        TestRail.setTestRailId("83307");

        AppiumHelper.StartAppFromUnifiedLauncher("Music Player");
        switchWindowPosition("floating mode");
        moveWindow(1000,1000);
        Point beforePosition = getWindowPosition("android:id/caption");
        minimizeWindow();
        confirmAppOnRecentApps("openapps","Music Player");
        AppiumHelper.openAppInRecentAppsPrivate("Music Player");
        ScreenHelper.clickAt(0.8,0.8);
        Point afterPosition = getWindowPosition("android:id/caption");
        assertEquals(afterPosition, beforePosition,"The position of the window changes");
        closeWindow();
    }

    /** Description: Use AP7_A or AP7_B panel Click the minimize button to minimize window when in full screen,then on Recent Apps open Adobe Acrobat app */
    @Test
    public void Click_the_close_button_to_close_apps_test() {
        TestRail.setTestRailId("83303");

        AppiumHelper.StartAppFromUnifiedLauncher("Files");
        confirmWindowStates("full screen");
        closeWindow();
        confirmAppOnRecentApps("closeapps","Files");
        ScreenHelper.clickAt(0.8,0.8);
    }

    /** Description: Drag up a full screen window and switch it to floating mode */
    @Test
    public void Drag_up_a_full_screen_window_can_switch_to_floating_mode_Test() {
        TestRail.setTestRailId("83297");

        AppiumHelper.StartAppFromUnifiedLauncher("Adobe Acrobat");
        confirmWindowStates("full screen");
        AppiumHelper.waitForSeconds(3);
        swipeUpWindow();
        confirmWindowStates("floating mode");
        closeWindow();
    }

    /** Description: Drag up a half screen window and switch it to floating mode */
    @Test
    public void Drag_up_a_half_screen_window_can_switch_to_floating_mode_Test() {
        TestRail.setTestRailId("83298");

        AppiumHelper.StartAppFromUnifiedLauncher("Music Player");
        AppiumHelper.waitForSeconds(3);
        switchWindowPosition("left half");
        swipeUpWindow();
        confirmWindowStates("floating mode");
        switchWindowPosition("right half");
        swipeUpWindow();
        confirmWindowStates("floating mode");
        closeWindow();
    }

    /** Description: app in multi-window mode cannot be dragged around when in full screen*/
    @Test
    public void App_in_full_screen_window_unable_to_move_left_and_right_horizontally_Test() {
        TestRail.setTestRailId("83295");

        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        switchWindowPosition("full screen");
        AppiumHelper.waitForSeconds(3);
        moveWindow(1920, 1053);
        confirmWindowStates("full screen");
        AppiumHelper.waitForSeconds(1);
        moveWindow(0, 1053);
        confirmWindowStates("full screen");
        closeWindow();
    }

    /** Description: Switch to external source and external sources displayed in window*/
    @Test
    public void Switch_to_external_source_and_open_apps_window_Test() {
        TestRail.setTestRailId("83317");

        CommonOperator.executeShellCommand("setprop persist.settings.home_source true", "");
        AppiumHelper.startAppFromBottomMenu("Source");
        if (ElementHelper.isPresent(By.id("com.prometheanworld.unifiedlauncher:id/middle_column"), 3)) {
            AndroidElement sourceElement = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/middle_column");
            sourceElement.click();
            AssertKt.assertNotPresent(By.id("android:id/caption"), 3);
            AppiumHelper.waitForSeconds(5);
            AppiumHelper.StartAppFromUnifiedLauncher("Files");
            confirmWindowStates("full screen");
            confirmWindowbottomcolumn("multi window");
            closeWindow();
        } else {
            Log.info("No external sources");
            ScreenHelper.clickAt(0.8,0.8);
        }
    }

    /** Description: Chromium and Settings apps should open in a floating window state by default*/
    @Test
    public void open_Settings_And_Chromium_apps_should_open_in_a_floating_window_state_by_default_Test() {
        TestRail.setTestRailId("83321");

        AppiumHelper.StartAppFromUnifiedLauncher("Settings");
        confirmWindowStates("floating mode");
        closeWindow();
        AppiumHelper.StartAppFromUnifiedLauncher("Chromium");
        confirmWindowStates("floating mode");
        closeWindow();
    }

    /** Description: Open Settings app from "More settings"*/
    @Test
    public void open_Settings_app_From_More_Settings_Test() {
        TestRail.setTestRailId("83322");

        AppiumHelper.startAppFromBottomMenu("Locker");
        try {
            AndroidElement cogIcon = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/settings_drawer_tab");
            cogIcon.click();
            AndroidElement moreButton = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/more_setting_button");
            moreButton.click();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.info("Cannot go to Locker Settings tab");
        }
        AppiumHelper.waitForSeconds(2);
        confirmWindowStates("floating mode");
        closeWindow();
    }

    /**  Description: Open Settings app from User*/
    @Test
    public void open_Settings_app_From_User_Test() {
        TestRail.setTestRailId("83323");

        AppiumHelper.startAppFromBottomMenu("Switch User");
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getAndroidDriver(), 3);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.prometheanworld.unifiedlauncher:id/UserSettingBtn"))).click();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.info("Cannot find User config icon");
        }
        AppiumHelper.waitForSeconds(2);
        confirmWindowStates("floating mode");
        closeWindow();
    }

    /** Description: Open Chromium app from the web shortcut*/
    @Test
    public void Open_Chromium_app_From_Web_Shortcut_Test() {
        TestRail.setTestRailId("83326");

        POFactory.getInstance(LockerPO.class).addShortcut("www.baidu.com","baidu");
        AppiumHelper.waitForSeconds(3);
        AppiumHelper.StartAppFromUnifiedLauncher("baidu");
        confirmWindowStates("floating mode");
        closeWindow();
        POFactory.getInstance(LockerPO.class).deleteAddShortcut("baidu");
        ScreenHelper.clickAt(0.8,0.8);
    }

    private Dimension getWindowSize(String elementID) {
        windowPosition=Driver.getAndroidDriver().findElementById(elementID);
        Dimension dimension=windowPosition.getSize();
        return dimension;
    }

    private Point getWindowPosition(String elementID) {
        windowPosition=Driver.getAndroidDriver().findElementById(elementID);
        Point point=windowPosition.getLocation();
        return point;
    }

    private void switchWindowPosition(String position) {
        String leftIcon = "android:id/left_side_window";
        String rightIcon = "android:id/right_side_window";
        String maximizeIcon = "android:id/maximize_window";
        String windowStatus=getWindowStatus();
        try {
            AndroidElement switchButton;
            switch (position) {
                case "full screen":
                    if (windowStatus.equals("full screen")) {
                        Log.info("Window already in full screen");
                    } else {
                        switchButton=Driver.getAndroidDriver().findElementById(maximizeIcon);
                        switchButton.click();
                    }
                    confirmWindowbottomcolumn("multi window");
                    break;
                case "floating mode":
                    if (windowStatus.equals("floating mode")) {
                        Log.info("Window already in floating mode");
                    } else {
                        switchButton=Driver.getAndroidDriver().findElementById(maximizeIcon);
                        switchButton.click();
                    }
                    confirmWindowbottomcolumn("multi window");
                    break;
                case "left half":
                    switchButton=Driver.getAndroidDriver().findElementById(leftIcon);
                    switchButton.click();
                    confirmWindowbottomcolumn("multi window");
                    break;
                case "right half":
                    switchButton=Driver.getAndroidDriver().findElementById(rightIcon);
                    switchButton.click();
                    confirmWindowbottomcolumn("multi window");
                    break;
            }
            Thread.sleep(3000);
            confirmWindowStates(position);
        } catch (NoSuchElementException | InterruptedException e) {
            Log.error("Cannot find " +  position + " button");
        }
    }

    private void confirmWindowStates(String windowState) {
        try {
            Point point = getWindowPosition("android:id/caption");
            Dimension dimension =getWindowSize("android:id/caption");
            int first_width = point.x;
            int second_width = point.x+dimension.width;
            switch (windowState) {
                case "full screen":
                    assertEquals(first_width, 0,"Window is not in full screen");
                    assertEquals(second_width, 1920, "Window is not in full screen");
                    break;
                case "floating mode":
                    assertNotEquals(first_width, 0,"Window not in floating mode");
                    assertNotEquals(second_width, 1920, "Window not in floating mode");
                    break;
                case "left half":
                    assertEquals(first_width, 0,"Window not in left half");
                    assertEquals(second_width, 960, "Window not in left half");
                    break;
                case "right half":
                    assertEquals(first_width, 960,"Window not in right half");
                    assertEquals(second_width, 1920, "Window not in right half");
                    break;
            }
            Log.info("Window is in " + windowState);
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }

    private void minimizeWindow() {
        try {
            AndroidElement minimizeButton=Driver.getAndroidDriver().findElementById("android:id/minimize_window");
            minimizeButton.click();
            AppiumHelper.waitForSeconds(2);
            String currentActivity = Driver.getAndroidDriver().currentActivity();
            assertNotEquals(currentActivity, "com.prometheanworld.Whiteboard.WhiteboardActivity", "Cannot minimize window");
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }

    private void swipeUpWindow() {
        try {
            Point point = getWindowPosition("android:id/caption");
            Dimension dimension =getWindowSize("android:id/caption");
            int pressPointX=point.x + dimension.width / 3;
            int pressPointY=point.y + dimension.height / 2;
            int moveToPointY=point.y / 4;
            // when window in full screen and drag it to up, switch to floating without a release
            new TouchAction(Driver.getAndroidDriver())
                    .press(PointOption.point(pressPointX, pressPointY))
                    .moveTo(PointOption.point(pressPointX, moveToPointY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1)))
                    .perform();
            Log.info("Swipe end, the press position is: " + pressPointX + ", " + pressPointY);
        } catch (NoSuchElementException e) {
            Log.info("Cannot find window");
        }
    }

    private void moveWindow(int moveToPointX, int moveToPointY) {
        try {
            Point point = getWindowPosition("android:id/caption");
            Dimension dimension =getWindowSize("android:id/caption");
            int pressPointX=point.x + dimension.width / 3;
            int pressPointY=point.y + dimension.height / 2;
            Log.info("The start position is: " + pressPointX + ", " + pressPointY);
            CommonOperator.executeShellCommand("input swipe", " " + pressPointX + " " + pressPointY + " " + moveToPointX + " " + moveToPointY + " 1000");
            Log.info("Move end, the end position is: " + moveToPointX + ", " + moveToPointY);
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }

    private String getWindowStatus() {
        String windowStatus;
        Point point = getWindowPosition("android:id/caption");
        Dimension dimension =getWindowSize("android:id/caption");
        int first_width = point.x;
        int second_width = point.x+dimension.width;
        if (first_width==0&&second_width==1920) {
            windowStatus = "full screen";
        } else if (first_width!=0&&second_width!=1920){
            windowStatus = "floating mode";
        } else if (first_width==0&&second_width==960){
            windowStatus = "left half";
        } else {
            windowStatus = "right half";
        }
        return windowStatus;
    }

    private void resizeWindow() {
        try {
            AndroidElement resizeIcon=Driver.getAndroidDriver().findElementById("android:id/resize_window");
            int first_width = resizeIcon.getLocation().getX();
            int first_height = resizeIcon.getLocation().getY();
            Log.info("The start position is: " + first_width + ", " + first_height);
            new TouchAction(Driver.getAndroidDriver())
                    .press(PointOption.point(first_width, first_height))
                    .moveTo(PointOption.point(first_width-100, first_height-100))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1)))
                    .release()
                    .perform();
            Log.info("Resize end, the end position is: " + resizeIcon.getLocation().getX() + ", " + resizeIcon.getLocation().getY());
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }

    private void closeWindow() {
        try {
            AndroidElement closeButton = Driver.getAndroidDriver().findElementById("android:id/close_window");
            closeButton.click();
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }

    private void openAndCloseFloatingApps(String appName, String packageName) {
        AppiumHelper.startAppFromBottomMenu("Locker");
        AppiumHelper.waitForSeconds(1);
        CommonOperator.ScrollAndClickApp(appName);
        AppiumHelper.waitForSeconds(2);
        confirmWindowbottomcolumn("popup-window");
        AppiumHelper.waitForSeconds(1);
        if (appName.equals("Screen Share")) {
            CommonOperator.executeShellCommand("am force-stop ", "org.chromium.chrome");
        }
        AssertKt.assertNotPresent(By.id("android:id/caption"), 3);
        if (appName.equals("Spinner") || appName.equals("Timer") || appName.equals("Annotate") || appName.equals("Screen Share") || appName.equals("Cloud Connect")) {
            CommonOperator.executeShellCommand("am force-stop ", packageName);
        } else {
            AppiumHelper.CloseAppFromBottomRecentApps(appName);
        }
        Log.info("-------------Close " + appName +"---------------");
        ScreenHelper.clickAt(0.8,0.8);
        AppiumHelper.waitForSeconds(3);
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

    private void confirmAppOnRecentApps(String onrecentapps,String appName) {
        switch (onrecentapps){
            case "openapps":
                AppiumHelper.startAppFromBottomMenu("RecentApps");
                AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/runningappsnapshot"), 3);
                break;
            case "closeapps":
                AppiumHelper.startAppFromBottomMenu("RecentApps");
                AppiumHelper.waitForSeconds(5);
                AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/runningappsnapshot"), 3);
                break;
        }
    }
}
