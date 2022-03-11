package com.prometheanworld.AP9.adhoc;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.prometheanworld.appium.frame.AppiumHelper.waitForSeconds;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by yaohuangjie on 2020/5/30.
 * AdHoc Test Group is used to test the UI changes/function adjustment for unifiedLauncher and locker page.
 * Test Module Name(AdHoc) is Consistent with test case name now
 */
@Listeners({TestRailListener.class})
public class AdHocTest extends BaseTest{

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        ScreenHelper.clickAt(Location.CENTER);
    }

    @Test
    public void Test_RemoveUnifiedLauncherFiles(){
        /** ADH-01 Description:Make sure UnifiedLauncher - Files is not displayed
         * 1. UnifiedLauncher --> Locker
         * 2.Confirm that "Files" option is not displayed on the left, removed since ota4
         * By Huangjie
         */
        TestRail.setTestRailId("83014");
        Log.info("-------------Test RemoveUnifiedLauncherFiles Start---------------");
        AppiumHelper.startAppFromBottomMenu("Locker");
        waitForSeconds(1);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/apps_drawer_tab"), 3);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/settings_drawer_tab"), 3);
        AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/files_drawer_tab"), 3);

        //click the lower-right corner point, use to hide bottom menu
        Log.info("click the lower-right corner point, use to hide bottom menu");
        waitForSeconds(2);

        ScreenHelper.clickAt(0.999, 0.999);
        Log.info("-------------Test RemoveUnifiedLauncherFiles End---------------");
    }

    @Test
    public void Test_RemoveUnifiedLauncherLock(){
        /** ADH-02 Description:Make sure UnifiedLauncher - Lock/Unlock is not displayed
         * 1. UnifiedLauncher --> Locker
         * 2.Confirm that "Lock/Unlock"  option is not displayed on the left,removed since ota4
         * By Huangjie
         */
        TestRail.setTestRailId("83015");
        Log.info("-------------Test RemoveUnifiedLauncherLock Start---------------");
        AppiumHelper.startAppFromBottomMenu("Locker");
        waitForSeconds(1);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/apps_drawer_tab"), 3);
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/settings_drawer_tab"), 3);
        AssertKt.assertNotPresent(By.id("com.prometheanworld.unifiedlauncher:id/panel_locking_drawer_tab"), 3);

        //click the lower-right corner point, use to hide bottom menu
        Log.info("click the lower-right corner point, use to hide bottom menu");
        waitForSeconds(2);
        ScreenHelper.clickAt(0.999, 0.999);

        Log.info("-------------Test RemoveUnifiedLauncherLock End---------------");
    }

    /** ADH-05 Description: AP7-4421 Click the info button, confirm that the information of panel displays correctly
     * 1. Unified Mune -> Locker, click the info button under the locker settings button
     * 2. Confirm that the information of panel displays correctly
     * Note: You can check the information in Update app
     */
    @Test
    public void Test_InfoButton() {
        TestRail.setTestRailId("83017");
        Log.info("-------------Test InfoButton Start---------------");
        Log.info("Open Update app and get panel information");
        CommonOperator.executeShellCommand("am start com.prometheanworld.otaupdateapp/com.prometheanworld.otaupdateapp.OTAActivity","");
        waitForSeconds(3);
        String updateModel = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_model").getText();
        String updateMainboardFirmware = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_scaler_firmware").getText();
        String updateBezelFirmware = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_bezel_fw").getText();
        String updateAndroidOperatingSystem = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_android_os_ver").getText();
        String updateSerialNumber = Driver.getAndroidDriver().findElementById("com.prometheanworld.otaupdateapp:id/text_serial_num").getText();
        CommonOperator.executeShellCommand("am force-stop com.prometheanworld.otaupdateapp","");

        AppiumHelper.startAppFromBottomMenu("Locker");
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/version_drawer_tab"), 3);
        AndroidElement infoButton = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/version_drawer_tab");
        infoButton.click();
        String infoModel = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/text_model").getText();
        String infoMainboardFirmware = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/text_scaler_firmware").getText();
        String infoBezelFirmware = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/text_bezel_fw").getText();
        String infoAndroidOperatingSystem = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/text_android_os_ver").getText();
        String infoSerialNumber = Driver.getAndroidDriver().findElementById("com.prometheanworld.unifiedlauncher:id/text_serial_num").getText();
        AssertKt.assertPresent(Locator.byTextContains("About this panel"), 3);
        assertEquals(updateModel,infoModel,"Model is different from Update");
        assertEquals(updateMainboardFirmware,infoMainboardFirmware,"Mainboard Firmware is different from Update");
        assertEquals(updateBezelFirmware,infoBezelFirmware,"Bezel Firmware is different from Update");
        assertEquals(updateAndroidOperatingSystem,infoAndroidOperatingSystem,"Android Operating System is different from Update");
        assertEquals(updateSerialNumber,infoSerialNumber,"Serial Number is different from Update");
        ScreenHelper.clickAt(0.999, 0.999);
        Log.info("-------------Test InfoButton End---------------");
    }

    @Test
    public void Test_106587() {
        BaseSettingsPO setPO = POFactory.getInstance(BaseSettingsPO.class);
        setPO.setStandbyTimer("106587 test");
    }
}
