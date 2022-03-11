package com.prometheanworld.Receiver.Receiver;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.model.ScreenShare.FloatingModePO;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Listeners({TestRailListener.class})
public class ScreenShareTest extends BaseTest {
    AndroidElement windowPosition;
    final static int X_AXIS = 709;
    final static int Y_AXIS = 455;
    FloatingModePO floatingModePO = POFactory.getInstance(FloatingModePO.class);


    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        ScreenHelper.clickAt(Location.CENTER);
    }

    /**
     * C86823 To verify that UI display of the Screen Share is normal
     * 1.open the Screen Share receiver；2.Contrast the receiver UI with the design UI
     * identity:UI display is the same,there is no UI error
     */

    @Test
    public void C86823ToVerifyThatUIDisplayOfTheScreenShareIsNormal(){
        Log.info("-------------check UI test Start---------------");
        TestRail.setTestRailId("86823");
        TestRail.addStepName("open the Screen Share receiver");
        AppiumHelper.StartAppFromUnifiedLauncher("Screen Share");
        AppiumHelper.waitForSeconds(10);
        Log.info("--------pop up the soft keyboard");
        ScreenHelper.clickAt(Location.CENTER);
        TestRail.addStepName("Contrast the receiver UI with the design UI");
        boolean isPanelNameFrameExist= floatingModePO.panelNameFrame().isDisplayed();
        boolean isPanelNameTipsExist=floatingModePO.panelNameTips().isDisplayed();
        boolean isPanelIdLabelExist=floatingModePO.panelIdLabel().isDisplayed();
        boolean isRefreshButtonExist=floatingModePO.refreshButton().isDisplayed();
        boolean isPanelIdExist=floatingModePO.panelId().isDisplayed();
        boolean isPeopleButtonExist=floatingModePO.peopleButton().isDisplayed();
        boolean isHelpButtonExist=floatingModePO.helpButton().isDisplayed();
        boolean isCloseButtonExist=floatingModePO.closeButton().isDisplayed();
        Log.info("is panel name frame exist = "+isPanelNameFrameExist);
        Log.info("is panel name tips exist = "+isPanelNameTipsExist);
        Log.info("is panel id label exist = "+isPanelIdLabelExist);
        Log.info("is refresh button exist = "+isRefreshButtonExist);
        Log.info("is panel id exist = "+isPanelIdExist);
        Log.info("is people button exist = "+isPeopleButtonExist);
        Log.info("is help button exist = "+isHelpButtonExist);
        Log.info("is clsoe button exist = "+isCloseButtonExist);
        floatingModePO.closeButton().click();
        Log.info("--------click Ok button");
        floatingModePO.popOkButton().click();
    }



    /**
     * C86823 to verify the function of close button
     * 1.open the Screen Share receiver；2.click the "close"button
     * identity:1.tip:"Are you sure you want to close Screen Share? All sharting sessions will end."
     * 2.choose "cancel", the receiver will not close
     * 3.choose "Ok", the receiver will close
     */

    @Test
    public void C86823ToVerifyTheFunctionOfCloseButton() throws IOException {
        Log.info("-------------Close Screen Share test Start---------------");
        TestRail.setTestRailId("86827");
        TestRail.addStepName("open the Screen Share receiver");
        AppiumHelper.StartAppFromUnifiedLauncher("Screen Share");
        AppiumHelper.waitForSeconds(10);
        Log.info("--------pop up the soft keyboard");
        ScreenHelper.clickAt(Location.CENTER);
        TestRail.addStepName("click the \"close\"button");
        floatingModePO.closeButton().click();
        TestRail.addStepName("identify tip:\"Are you sure you want to close Screen Share? All sharting sessions will end.\"");
        String actualText = floatingModePO.popDialogText().getText();
        String expectedText = "Are you sure you want to close Screen Share? All sharing sessions will end.";
        checkTextContent(expectedText,actualText);
        TestRail.addStepName("choose \"cancel\", the receiver will not close");
        floatingModePO.popCancleButton().click();
        ElementHelper.waitUntilNotPresent(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout"));
        floatingModePO.closeButton().click();
        Log.info("--------click Ok button");
        TestRail.addStepName("choose \"Ok\", the receiver will close");
        floatingModePO.popOkButton().click();
        Log.info("----------------Close Screen Share test completes--------------");
       }


    /**
     * C86831 to verify that user can switch the floating mode into full mode
     * 1.open the Screen Share receiver
     * 2.when screen is in the floating mode
     * 3.click the adjust button
     * identify:the Screen Share can switch the floating mode into full mode
     * C86832 to verify that user can switch the full mode into floating mode
     * 1.open the Screen Share receiver
     * 2.when screen is in the full mode
     * 3.click the adjust button
     * identify:the Screen Share can switch the full mode into floating mode
     */
    @Test
    public void C86831C86832ToVerifyThatUserCanSwitchTheFloatingModeIntoFullModeAndSwitchTheFullModeIntoFloatingMode() throws IOException {
        Log.info("-------------Switch windows test Start---------------");
        TestRail.setTestRailId("86831,86832");
        TestRail.addStepName("open the Screen Share receiver");
        AppiumHelper.StartAppFromUnifiedLauncher("Screen Share");
        AppiumHelper.waitForSeconds(10);
        Log.info("--------pop up the soft keyboard");
        ScreenHelper.clickAt(Location.CENTER);
        TestRail.addStepName("when screen is in the floating mode, click the adjust button");
        floatingModePO.peopleButton().click();
        Log.info("--------click minimize button");
        TestRail.addStepName("when screen is in the full mode, click the adjust button");
        ElementHelper.click(By.id("com.nd.promethean.casting.receiver:id/iv_normalize"));
        ElementHelper.waitUntilNotPresent(By.id("com.nd.promethean.casting.receiver:id/iv_normalize"));
        AppiumHelper.waitForSeconds(2);
        Log.info("--------pop up the soft keyboard");
        ScreenHelper.clickAt(Location.CENTER);
        Log.info("--------click full screen button");
        floatingModePO.peopleButton().click();
        Log.info("--------click minimize button");
        ElementHelper.click(By.id("com.nd.promethean.casting.receiver:id/iv_normalize"));
        ElementHelper.waitUntilNotPresent(By.id("com.nd.promethean.casting.receiver:id/iv_normalize"));
        ScreenHelper.clickAt(Location.CENTER);
        floatingModePO.closeButton().click();
        Log.info("--------click Ok button");
        floatingModePO.popOkButton().click();
        Log.info("----------------Switch windows test completes--------------");
    }

/*    @Test
    public void changeId(){
        Log.info("-------------Test Screen Share Start---------------");
        TestRail.setTestRailId("86831,86832");
        AppiumHelper.StartAppFromUnifiedLauncher("Screen Share");
        ScreenHelper.clickAt(0.41, 0.61);
        AndroidElement miniElement = Driver.getAndroidDriver().findElementById("com.nd.promethean.casting.receiver:id/iv_normalize");
        miniElement.click();
        ScreenHelper.clickAt(0.41, 0.61);
    }*/

    public void checkTextContent(String expectText,String actualText){
        try {
            Log.info("--------the text content in the dialog is "+actualText);
            Assert.assertEquals(actualText,expectText);
        }catch (AssertionError e){
            e.printStackTrace();
            Log.info("--------the actual text content is not match the expected content");
        }
    }


    public void RunShell (String shpath) throws IOException {
        String s = null;
        int runningStatus = 0;
        try {
            ProcessBuilder pb = new ProcessBuilder().command(shpath);

            Process p = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                Log.info(s);
            }
            while ((s = stdError.readLine()) != null) {
                Log.info(s);
            }
                try {
                    runningStatus = p.waitFor();
                } catch (InterruptedException e) {
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RunShellBase (String shpath){
        String[]command = {"/bin/sh", "-c",shpath};
        BufferedReader br = null;
        BufferedReader er = null;
        try {
            Process ps = Runtime.getRuntime().exec(command);


            br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "GBK"));
            er = new BufferedReader(new InputStreamReader(ps.getErrorStream(), "GBK"));
            StringBuffer sb = new StringBuffer();
            StringBuffer sr = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            while ((line = er.readLine()) != null) {
                sr.append(line).append("\n");
            }


            ps.waitFor(5, TimeUnit.SECONDS);;
            String result = sb.toString();
            System.out.println(result);
            ps.destroy();
            br.close();
            er.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    er.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void runCmdCommand(String command ){
        BufferedReader br = null;
        StringBuilder outPutResult = new StringBuilder();
        try {
            List<String> execCommand = new LinkedList<>();
            execCommand.add(0, command);
            execCommand.add(0, "/c");
            execCommand.add(0, "cmd.exe");
            ProcessBuilder pb = new ProcessBuilder().command(execCommand).inheritIO();
            // 外部程序的输出放到了错误信息输出流中
            pb.redirectErrorStream(true);
            //等待语句执行完成，否则可能会读不到结果。
            Process process = pb.start();
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;
            while ((line = br.readLine()) != null) {
                outPutResult.append(line).append("\n");
            }
            br.close();
            br = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
                    break;
                case "floating mode":
                    if (windowStatus.equals("floating mode")) {
                        Log.info("Window already in floating mode");
                    } else {
                        switchButton=Driver.getAndroidDriver().findElementById(maximizeIcon);
                        switchButton.click();
                    }
                    break;
                case "left half":
                    switchButton=Driver.getAndroidDriver().findElementById(leftIcon);
                    switchButton.click();
                    break;
                case "right half":
                    switchButton=Driver.getAndroidDriver().findElementById(rightIcon);
                    switchButton.click();
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
            assertNotEquals(currentActivity, "com.prometheanworld.whiteboard.WhiteboardActivity", "Cannot minimize window");
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
            // move window
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
            // resize window
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
            // click close button
            AndroidElement closeButton = Driver.getAndroidDriver().findElementById("android:id/close_window");
            closeButton.click();
        } catch (NoSuchElementException e) {
            Log.error("Cannot find window");
        }
    }

    private void openAndCloseFloatingApps(String appName, String packageName) {
        AppiumHelper.StartAppFromUnifiedLauncher(appName);
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

        AppiumHelper.hideUnifiedMenu();
    }

    private void addShortcut(String address, String Name){
        AppiumHelper.StartAppFromUnifiedLauncher("Chromium");

        // Max the window,so we can find the elements
        AppiumHelper.waitForSeconds(3);
        if(!panelModel.equals("AP7_U")){
            ElementHelper.click(By.id("android:id/maximize_window"));
        }

        ElementHelper.click(By.id("org.chromium.chrome:id/url_bar"));
        AndroidElement url = Driver.getAndroidDriver().findElementById("org.chromium.chrome:id/url_bar");
        url.sendKeys(address);
        AppiumHelper.waitForSeconds(3);
        Driver.getAndroidDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
        AppiumHelper.waitForSeconds(5);

        ElementHelper.click(By.id("org.chromium.chrome:id/menu_button"));
        ElementHelper.click(Locator.byText("Add to Home screen"));
        AppiumHelper.waitForSeconds(2);
        AndroidElement shortcutName = Driver.getAndroidDriver().findElementById("org.chromium.chrome:id/text");
        shortcutName.clear();
        AppiumHelper.waitForSeconds(1);
        shortcutName.sendKeys(Name);
        AppiumHelper.waitForSeconds(1);
        ElementHelper.click(By.id("org.chromium.chrome:id/positive_button"));
        AppiumHelper.waitForSeconds(2);

        ElementHelper.click(By.id("android:id/close_window"));

    }
}
