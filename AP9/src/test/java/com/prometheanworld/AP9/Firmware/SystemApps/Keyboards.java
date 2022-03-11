package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Keyboards extends BaseTest {
    private static final By SETTINGS_DASHBOARD = Locator.byResourceId("id/dashboard_container");

    /**
     * C116891 Verify that Gboard is default virtual keyboard
     * Steps:
     * 1. Settings> System> Languages & Input> Virtual Keyboard - Verify Gboard is default
     * Author:Sita
     */
    @Test(groups= "P2")
    public void C116891VerifyThatGboardIsDefaultVirtualKeyboard() {
        TestRail.setTestRailId("116891");
        TestRail.addStepName("Settings> System> Languages & Input> Virtual Keyboard - Verify Gboard is default");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.findElement("//android.widget.ImageButton[@content-desc='Maximize']", 5).click();
        final By system = Locator.byText("System");
        ElementHelper.scrollUntilPresent(SETTINGS_DASHBOARD, system);
        ElementHelper.click(system);
        ElementHelper.clickWhenVisible(Locator.byText("Gboard"));
        ElementHelper.clickWhenVisible(Locator.byText("Virtual keyboard"));
        //verifying Gboard is selected by clicking on the <String - Gboard> which is present by default.
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Gboard")));
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
    }

    /**C112899 verify Gboard keyboard open successful
     * Steps
     * 1.Settings->System->Languages & input->Virtual keyboard->Manage Keyboards->Enabled the switch of Gboard
     * 2.open the chromium->type in the search bar
     * Author:Sita
     * */
    @Test(groups = "P2")
    //Precondition: Need to side-load a keyboard(Microsoft Swiftkey Keyboard), before executing the test case.
    public void C112899GboardKeyboardOpenSuccessful(){
        TestRail.setTestRailId("112899");
        TestRail.addStepName("Settings->System->Languages & input->Virtual keyboard->Manage Keyboards->Enabled the switch of Gboard");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.findElement("//android.widget.ImageButton[@content-desc='Maximize']", 5).click();
        final By system = Locator.byText("System");
        ElementHelper.scrollUntilPresent(SETTINGS_DASHBOARD, system);
        ElementHelper.click(system);
        ElementHelper.clickWhenVisible(Locator.byText("Gboard"));
        ElementHelper.clickWhenVisible(Locator.byText("Virtual keyboard"));
        ElementHelper.clickWhenVisible(Locator.byText("Manage keyboards"));
        ElementHelper.clickWhenVisible(Locator.byResourceId("android:id/switch_widget"));
        ElementHelper.waitUntilPresent(Locator.byText("Microsoft SwiftKey Keyboard"));
        ElementHelper.clickWhenVisible(Locator.byText("Microsoft SwiftKey Keyboard"));
        ElementHelper.clickWhenVisible(Locator.byText("OK"));
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
        TestRail.addStepName("open the chromium->type in the search bar");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        ElementHelper.clickWhenVisible(By.id("org.chromium.chrome:id/url_bar"));
        AndroidElement url = Driver.getAndroidDriver().findElementById("org.chromium.chrome:id/url_bar");
        url.sendKeys("www.google.com");
        AppiumHelper.waitForSeconds(10);
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
    }

    /**C122598 Verify that adb key events are detected by panel
     *     1. adb connect <IPaddress>
     *     2. scrcpy.exe
     *     3. In another cmd, adb shell input keyevent 82
     *     4. adb shell input keyevent 26
     *     5. 1.adb shell input keyevent 25 2.adb shell input keyevent 25
     *     6. 1.adb shell input keyevent 24 2.adb shell input keyevent 24
     *     Author:Sita*/
    @Test(groups = "P1")
    public void C122598AdbKeyEventsAreDetectedByPanel() {
        TestRail.setTestRailId("122598");
        TestRail.addStepName("1. adb connect <IPaddress>"); //Skipping the step
        TestRail.addStepName(" scrcpy.exe "); //Skipping the step
        TestRail.addStepName(" In another cmd, adb shell input keyevent 82");
        systemPO.inputKeyevent(PrometheanKey.Menu);
        //Menu bar should be invoked
        Assert.assertTrue(ElementHelper.isVisible(By.id("com.prometheanworld.unifiedlauncher:id/menu_icon_locker")));

        TestRail.addStepName("adb shell input keyevent 26"); //Skipping the step
        TestRail.addStepName(" 1.adb shell input keyevent 25" + "2.adb shell input keyevent 25");
        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.ActivPanel);
        //Volume bar slider should appear
        Assert.assertTrue(ElementHelper.isVisible(By.id("com.prometheanworld.avisettings:id/volume_bar")));
        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.ActivPanel);
        AppiumHelper.waitForSeconds(60);

        TestRail.addStepName(" 1. adb shell input keyevent 24" + "adb shell input keyevent 24");
        AppiumHelper.clickKey(PrometheanKey.VolumeUp, PrometheanKeyboard.ActivPanel);
        //Volume bar slider should appear
        Assert.assertTrue(ElementHelper.isVisible(By.id("com.prometheanworld.avisettings:id/volume_bar")));
        AppiumHelper.clickKey(PrometheanKey.VolumeUp, PrometheanKeyboard.ActivPanel);
    }
}