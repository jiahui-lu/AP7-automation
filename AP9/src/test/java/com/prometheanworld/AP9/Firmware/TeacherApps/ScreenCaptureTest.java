package com.prometheanworld.AP9.Firmware.TeacherApps;

import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.kotlin.AssumeKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP9.FilesPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class ScreenCaptureTest extends BaseTest {
    private String closeXpath = "//android.widget.ImageButton[@content-desc='Close']";
    private String closeScreenCaptureId = "com.prometheanworld.screencapture:id/close";
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final FilesPO filesPO = POFactory.getInstance(FilesPO.class);

    /**
     * C112827 Exit current operation
     * Open Screen Capture app;----Bring current screen into crop window
     * Click "CLOSE" button at the bottom right corner;----Exit the current operation completely
     */
    @Test(groups= "P0")
    public void C112827ExitCurrentOperation() {
        TestRail.setTestRailId("112827");

        TestRail.addStepName("Open Screen Capture app");
        String screenCaptureElementText = "//*[@text=\"Screen Capture\"]";
        systemPO.startAppFromUnifiedLauncher("Screen Capture");
        //Menu bar hides automatically
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Applications']"));
        AssertKt.assertPresent(By.xpath(screenCaptureElementText));

        TestRail.addStepName("Click \"CLOSE\" button at the bottom right corner");
        ElementHelper.click(By.xpath("//*[@text=\"Close\"]"));
        //Screen Capture is invisible on the panel screen
        ElementHelper.waitUntilNotPresent(By.xpath(screenCaptureElementText));
    }

    /**
     * C112819 Cancel & Done buttons displayed after cropping image
     * Steps
     * 1. Open Screen Capture app
     * 2. Drag the bottom right corner to crop image
     * Author:Sita
     */
    @Test(groups= "P1")
    public void C112819CancelAndDoneButtonsDisplayedAfterCroppingImage() {
        TestRail.setTestRailId("112819");
        TestRail.addStepName("Open Screen Capture app");
        AppiumHelper.showTaskbar();
        final By screenCapture = Locator.byText("Screen Capture");
        ElementHelper.click(screenCapture);
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/title"),10));
        TestRail.addStepName("Drag the bottom right corner to crop image");
        AppiumHelper.DragScreenCaptureCorner(By.id("com.prometheanworld.screencapture:id/screen_shot"), "one");
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/cancel"),10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/done"),10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/quick_save"),10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/save_as"),10));
        AppiumHelper.findElementBy(By.id("com.prometheanworld.screencapture:id/cancel")).click();
        AppiumHelper.findElementBy(By.id(closeScreenCaptureId)).click();
    }

    /**
     * C112814 Drag 4 corners of Crop screen
     * Steps
     * 1. Taskbar -> hit "Screen Capture
     * 2. All 4 corners of "Crop" screen can be easily dragged around
     * Author: Sita
     */
    @Test(groups= "P1")
    public void C112814Drag4CornersOfCropScreen() {
        TestRail.setTestRailId("112814");
        TestRail.addStepName("Taskbar -> hit Screen Capture");
        AppiumHelper.showTaskbar();
        final By screenCapture = Locator.byText("Screen Capture");
        ElementHelper.click(screenCapture);
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/title"),10));
        TestRail.addStepName("All 4 corners of \"Crop\" screen can be easily dragged around");
        AppiumHelper.DragScreenCaptureCorner(By.id("com.prometheanworld.screencapture:id/screen_shot"), "all");
        AppiumHelper.findElementBy(By.id("com.prometheanworld.screencapture:id/cancel")).click();
        AppiumHelper.findElementBy(By.id(closeScreenCaptureId)).click();
    }

    /**
     * C120897 Verify that the screen capture will be saved to downloads folder after 'Quick Save' clicked
     * Steps
     * 1. press menu icon->tap screen capture app icon in the menu->Click 'Quick Save'
     * 2. Open my file application->Downloads
     * 3. Open the saved screen capture
     * Author:Sita
     */
    @Test(groups= "P1")
    public void C120897ScreenCaptureSavedToDownloadsAfterQuickSaveClicked() {
        TestRail.setTestRailId("120897");
        TestRail.addStepName("press menu icon->tap screen capture app icon in the menu->Click 'Quick Save'");
        AppiumHelper.showTaskbar();
        final By screenCapture = Locator.byText("Screen Capture");
        ElementHelper.click(screenCapture);
        AppiumHelper.findElementBy(By.id("com.prometheanworld.screencapture:id/quick_save")).click();
        Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/title"),10));
        TestRail.addStepName("Open my files application->Downloads");
        systemPO.startAppFromUnifiedLauncher("Files");
        ElementHelper.waitUntilPresent(Locator.byContentDesc("Show roots"),10);
        AppiumHelper.findElementBy(By.xpath("//android.widget.ImageButton[@content-desc='Show roots']")).click();
        ElementHelper.waitUntilPresent(Locator.byText("Internal Storage"), 10);
        final By internalStorage = Locator.byText("Internal Storage");
        ElementHelper.click(internalStorage);
        AppiumHelper.findElement("//android.widget.ImageButton[@content-desc='Maximize']", 5).click();
        final By download = Locator.byText("Download");
        ElementHelper.click(download);
        AppiumHelper.longPressByElement(By.id("com.android.documentsui:id/icon_thumb"));
        AppiumHelper.findElementBy(By.xpath("(//android.widget.ImageButton[@content-desc=\"More options\"])[1]")).click();
        final By getInfo = Locator.byText("Get info");
        ElementHelper.click(getInfo);
        final MobileElement sizeByText = ElementHelper.findElement(By.className("android.widget.TextView"));
        String sizeText = sizeByText.getText();
        System.out.println(sizeText);
        Assert.assertFalse(sizeText.toLowerCase().contains("0 mb"));
        ElementHelper.findElement(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]")).click();
        TestRail.addStepName("Open the saved screen capture");
        ElementHelper.findElement(By.id("com.android.documentsui:id/icon_thumb")).click();
        CommonOperator.executeShellCommand("rm /sdcard/Download/ScreenCapture*", "");
        AppiumHelper.findElement(closeXpath).click();
    }

    /**
     * C120899 Verify that user can select one folder in the file application to save screen capture after 'save as' clicked
     * Steps
     * 1. Press menu icon->tap screen capture app icon in the menu->Click 'Save As'
     * 2. select one folder and click 'save'
     * 3. Open the capture
     * Author:Sita
     */
    @Test(groups= "P1")
    public void C120899userCanSelectOneFolderInFileApplicationToSaveScreenCapture() {
        TestRail.setTestRailId("120899");
        TestRail.addStepName("Press menu icon->tap screen capture app icon in the menu->Click 'Save As'");
        AppiumHelper.showTaskbar();
        final By screenCapture = Locator.byText("Screen Capture");
        ElementHelper.click(screenCapture);
        AppiumHelper.findElementBy(By.id("com.prometheanworld.screencapture:id/save_as")).click();
        final By download = Locator.byText("Download");
        String downloadText = ElementHelper.getText(download);
        Assert.assertTrue(downloadText.contains("Download"));
        TestRail.addStepName("select one folder and click 'save'");
        ElementHelper.waitUntilPresent(Locator.byContentDesc("Show roots"),10);
        AppiumHelper.findElementBy(By.xpath("//android.widget.ImageButton[@content-desc='Show roots']")).click();
        ElementHelper.waitUntilPresent(Locator.byText("Internal Storage"), 10);
        final By internalStorage = Locator.byText("Internal Storage");
        ElementHelper.click(internalStorage);
        AppiumHelper.findElement("//android.widget.ImageButton[@content-desc='Maximize']", 5).click();
        final By downloads = Locator.byText("Download");
        ElementHelper.click(downloads);
        AppiumHelper.findElementBy(By.id("android:id/button1")).click(); //to save the screenshot
        Assert.assertFalse(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/title"),10));
        CommonOperator.executeShellCommand("rm /sdcard/Download/ScreenCapture*", "");
        TestRail.addStepName("Open the capture");
        AppiumHelper.showTaskbar();
        final By screenCaptures = Locator.byText("Screen Capture");
        ElementHelper.click(screenCaptures);
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.screencapture:id/title"),10));
        AppiumHelper.findElementBy(By.id(closeScreenCaptureId)).click();
    }

    /**
     * C120898 Verify that the saved screen capture's name format is ScreenCapture_[YearMonthDay]_[HourMinuteSecond].png if user selects 'Quick Save'
     * Steps:
     * 1. press menu icon->tap screen capture app icon in the menu->Click 'Quick Save'
     * 2. Open my files application->Downloads->check the saved screen capture's name format
     * Author: Ning Lu
     */
    @Test(groups = "P1")
    public void C120898VerifyThatTheSavedScreenCaptureNameFormatIsScreenCapture_YearMonthDay_HourMinuteSecondPNGIfUserSelectsQuickSave() {
        TestRail.setTestRailId("120898");

        // pre-condition
        boolean isFilesAppExist = false;
        boolean isScreenCaptureExist = false;
        systemPO.startLocker();
        int tabsSize = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab), 5).size();
        for (int i = 1; i <= tabsSize; i++) {
            ElementHelper.click(By.xpath(systemPO.eleApplicationsTab + "[" + i + "]"));
            if (!isFilesAppExist) {
                isFilesAppExist = ElementHelper.isPresent(Locator.byText("Files"), 2);
            }
            if (!isScreenCaptureExist) {
                isScreenCaptureExist = ElementHelper.isPresent(By.xpath("//*[@text='Screen Capture']"), 2);
            }
        }
        AssumeKt.assumeTrue(isFilesAppExist && isScreenCaptureExist,
                "Files & Screen Capture should be visible");
        ScreenHelper.clickAt(0.1, 0.1);
        // set test case cleaner
        setTestCaseCleaner(() -> {
            Adb.adb("shell", "rm /sdcard/Download/ScreenCapture*png");
            systemPO.closeAppOnMenuBar("Screen Capture");
            systemPO.closeAppOnMenuBar("Files");
        });

        TestRail.addStepName("press menu icon->tap screen capture app icon in the menu->Click 'Quick Save'");
        // first of all, clean all ScreenCapture*.png
        Adb.adb("shell", "rm /sdcard/Download/ScreenCapture*png");
        systemPO.startAppFromUnifiedLauncher("Screen Capture");
        ElementHelper.waitUntilPresent(Locator.byTextContains("Quick Save"));
        ElementHelper.click(Locator.byTextContains("Quick Save"));

        TestRail.addStepName("Open my files application->Downloads->check the saved screen capture's name format");
        systemPO.startAppFromUnifiedLauncher("Files");
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/container_directory"));
        filesPO.selectDownloadFiles();
        List<MobileElement> elements = ElementHelper.findElements(Locator.byTextContains("png"));
        AssertKt.assertTrue(!elements.isEmpty(),
                "The screen capture png should be created in the download folder");
        for (MobileElement element : elements) {
            final String text = element.getText();
            AssertKt.assertTrue(text.matches("^ScreenCapture_\\d{8}_\\d{6}\\.png$"),
                    "The png file name should be match ScreenCapture_\\d{8}_\\d{6}\\.png, but actually " + text);
        }
    }


    /**
     * C112831 Drag 4 corners to maximum
     * Steps:
     * 1. Open Screen Capture app
     * 2. Drag 4 corners of the "Crop" screen to the edge of crop window
     * Author: Ning Lu
     */
    @Test(groups = "P1")
    public void C112831Drag4CornersToMaximum() {
        TestRail.setTestRailId("112831");
        // pre-condition
        boolean isFilesAppExist = false;
        boolean isScreenCaptureExist = false;
        systemPO.startLocker();
        int tabsSize = ElementHelper.findElements(By.xpath(systemPO.eleApplicationsTab), 5).size();
        for (int i = 1; i <= tabsSize; i++) {
            ElementHelper.click(By.xpath(systemPO.eleApplicationsTab + "[" + i + "]"));
            if (!isFilesAppExist) {
                isFilesAppExist = ElementHelper.isPresent(Locator.byText("Files"), 2);
            }
            if (!isScreenCaptureExist) {
                isScreenCaptureExist = ElementHelper.isPresent(By.xpath("//*[@text='Screen Capture']"), 2);
            }
        }
        AssumeKt.assumeTrue(isFilesAppExist && isScreenCaptureExist,
                "Files & Screen Capture should be visible");
        ScreenHelper.clickAt(0.1, 0.1);
        // set test case cleaner
        setTestCaseCleaner(() -> {
            Adb.adb("shell", "rm /sdcard/Download/ScreenCapture*png");
            systemPO.closeAppOnMenuBar("Screen Capture");
        });

        TestRail.addStepName("Open Screen Capture app");
        Adb.adb("shell", "rm /sdcard/Download/ScreenCapture*png");
        // secondly, we need to get the maximum dimension of the screenshots that the screen capture app cropped
        systemPO.startAppFromUnifiedLauncher("Screen Capture");
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/screen_shot"));
        final Rect originalRect = ElementHelper.getRect(Locator.byResourceId("id/screen_shot"));

        TestRail.addStepName("Drag 4 corners of the \"Crop\" screen to the edge of crop window");
        // Drag the left-top corner
        Point corner = new Point(originalRect.getLeft() + 10, originalRect.getTop() + 10);
        ScreenHelper.swipe(corner, corner.moveBy(-30, -30));
        // Drag the right-top corner
        corner = new Point(originalRect.getRight() - 10, originalRect.getTop() + 10);
        ScreenHelper.swipe(corner, corner.moveBy(30, -30));
        // Drag the right-bottom corner
        corner = new Point(originalRect.getRight() - 10, originalRect.getBottom() - 10);
        ScreenHelper.swipe(corner, corner.moveBy(30, 30));
        // Drag the left-bottom corner
        corner = new Point(originalRect.getLeft() + 10, originalRect.getBottom() - 10);
        ScreenHelper.swipe(corner, corner.moveBy(-30, 30));
        final Rect transformedRect = ElementHelper.getRect(Locator.byResourceId("id/screen_shot"));
        AssertKt.assertEquals(originalRect, transformedRect, "The cropped area should be equals with each other");
    }
}