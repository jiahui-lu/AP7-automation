package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.SubSystem;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.AP9.UpdatePO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class Update extends BaseTest {

    private static final String PROP_LOCAL_UPDATE = "persist.update.local";
    private static final String PROP_OTA_UPDATE = "persist.update.ota";
    private static final String UPDATE_PACKAGE = "com.prometheanworld.update";
    private final UpdatePO updatePO = POFactory.getInstance(UpdatePO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private static final By FILES = By.id("com.prometheanworld.update:id/tv_file_name");

    /**
     * C128869 Verify that local update is disabled after executed the command 'adb shell setprop persist.update.local false'
     * Steps:
     *  1. Execute the command 'adb shell setprop persist.update.local false'
     *  2. Launch 'Update' app and check the status
     */
    @Test(groups = {"P1","UserDebug"})
    public void C128869VerifyThatLocalUpdateIsDisabledAfterExecutedTheADBCommand() {
        TestRail.setTestRailId("128869");
        setTestCaseCleaner(() -> {
            resetStatus();
            Adb.forceStop(UPDATE_PACKAGE);
        });
        AppiumHelper.exeAdbRoot();
        resetStatus();

        TestRail.addStepName("Execute the command 'adb shell setprop persist.update.local false'");
        Adb.setAndroidProp(PROP_LOCAL_UPDATE, "false");

        TestRail.addStepName("Launch 'Update' app and check the status");
        systemPO.startAppFromUnifiedLauncher("Update");
        AppiumHelper.waitForSeconds(3);

        // The Local Update button is grayed out, user can't update the panel by local update
        assertEnableStateEquals(updatePO.eleIdUpdateLocalUpdate, false);

        // The Online Update button is not grayed out
        assertEnableStateEquals(updatePO.eleIdOTAUpdate,
                !ElementHelper.isPresent(By.xpath("//*[@text='The latest updates have already been installed']"), 3));

        // there is a message displayed ‘Local firmware updates have been disabled on this panel.’
        assertDisplayedMessageEquals(updatePO.eleIdFirmwareDisabled, "Local firmware updates have been disabled on this panel.");

    }

    /**
     * C128870 Verify that online update is disabled after executed the command 'adb shell setprop persist.update.ota false'
     * Steps:
     *  1. Execute the command 'adb shell setprop persist.update.ota false'
     *  2. Launch 'Update' app and check the status
     */
    @Test(groups = {"P1","UserDebug"})
    public void C128870VerifyThatOnlineUpdateIsDisabledAfterExecutedTheADBCommand() {
        TestRail.setTestRailId("128870");
        setTestCaseCleaner(() -> {
            resetStatus();
            Adb.forceStop(UPDATE_PACKAGE);
        });
        AppiumHelper.exeAdbRoot();
        resetStatus();

        TestRail.addStepName("Execute the command 'adb shell setprop persist.update.ota false'");
        Adb.setAndroidProp(PROP_OTA_UPDATE, "false");

        TestRail.addStepName("Launch 'Update' app and check the status");
        systemPO.startAppFromUnifiedLauncher("Update");
        AppiumHelper.waitForSeconds(3);

        // The online Update button is grayed out, user can't update the panel by online update
        assertEnableStateEquals(updatePO.eleIdOTAUpdate, false);

        // The local Update button is not grayed out
        assertEnableStateEquals(updatePO.eleIdUpdateLocalUpdate, true);

        // there is a message displayed ‘Online firmware updates have been disabled on this panel.’
        assertDisplayedMessageEquals(updatePO.eleIdFirmwareDisabled, "Online firmware updates have been disabled on this panel.");
    }

    /**
     * C128871 Verify that both local update and online update are disabled
     * after executed the command 'adb shell setprop persist.update.local false' and 'adb shell setprop persist.update.ota false'
     * Steps:
     *  1. Execute the command 'adb shell setprop persist.update.local false' and 'adb shell setprop persist.update.ota false'
     *  2. Launch 'Update' app and check the status
     */
    @Test(groups = {"P1","UserDebug"})
    public void C128871VerifyThatBothLocalUpdateAndOnlineUpdateAreDisabledAfterExecutedTheADBCommand() {
        TestRail.setTestRailId("128871");
        setTestCaseCleaner(() -> {
            resetStatus();
            Adb.forceStop(UPDATE_PACKAGE);
        });
        AppiumHelper.exeAdbRoot();
        resetStatus();

        TestRail.addStepName("Execute the command 'adb shell setprop persist.update.local false' and 'adb shell setprop persist.update.ota false'");
        Adb.setAndroidProp(PROP_OTA_UPDATE, "false");
        Adb.setAndroidProp(PROP_LOCAL_UPDATE, "false");

        TestRail.addStepName("Launch 'Update' app and check the status");
        systemPO.startAppFromUnifiedLauncher("Update");
        AppiumHelper.waitForSeconds(3);

        // The Local Update button is grayed out, user can't update the panel by local update
        assertEnableStateEquals(updatePO.eleIdUpdateLocalUpdate, false);

        // The Online Update button is grayed out,user can't update the panel by oneline update
        assertEnableStateEquals(updatePO.eleIdOTAUpdate, false);

        // there is a message displayed ‘Firmware updates have been disabled on this panel.’ (localized, eventually)
        assertDisplayedMessageEquals(updatePO.eleIdFirmwareDisabled, "Firmware updates have been disabled on this panel.");
    }

    /**
     * C119951 Verify that user can close the update application by clicking 'close' button
     * Steps:
     * 1. Launch 'Update' app->click 'close' button
     */
    @Test(groups= "P1")
    public void C119951VerifyThatUserCanCloseTheUpdateApplicationByClickingCloseButton() {
        TestRail.setTestRailId("119951");
        setTestCaseCleaner(() -> Adb.forceStop(UPDATE_PACKAGE));
        TestRail.addStepName("Launch 'Update' app->click 'close' button");
        systemPO.startAppFromUnifiedLauncher("Update");

        ElementHelper.click(Locator.byResourceId("id/btn_clse"));

        AppiumHelper.waitForSeconds(3);
        String currentPackageName = Driver.getAndroidDriver().getCurrentPackage();
        assertNotEquals(UPDATE_PACKAGE, currentPackageName);
    }

    /**
     * C116713 Verify that online checking start automatically after 'Update' app launched
     * Steps:
     * 1. Launch 'Update' app
     * 2. wait the checking to be ended
     */
    @Test(groups= "P1")
    public void C116713VerifyThatOnlineCheckingStartAutomaticallyAfterUpdateAppLaunched() {
        TestRail.setTestRailId("116713");
        setTestCaseCleaner(() -> Adb.forceStop(UPDATE_PACKAGE));

        TestRail.addStepName("Launch 'Update' app");
        systemPO.startAppFromUnifiedLauncher("Update");

        //Checking for update will start automatically, and 'online update now' is grey and unclickable. When the check time is too short, the verify will fail
//            ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_checking_tip"), 3);

        TestRail.addStepName("wait the checking to be ended");
        // if there is no update, the words should be 'The latest updates have already been installed', 'online update now' is grey and unclickable
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/tv_checking_tip"), 3);

        MobileElement checkResultElement = ElementHelper.findElement(By.id(updatePO.eleIdCheckingResult), 10);
        String result = checkResultElement.getText();
        if (result.equals("ActivPanel Up-to-date")) {
            assertDisplayedMessageEquals(updatePO.eleIdLatestVersionDesc, "The latest updates have already been installed");
            assertEnableStateEquals(updatePO.eleIdOTAUpdate, false);
        }

        // if there is update, the words should be 'AP9 version ... is ready to download and install', 'online update now' is clickable
        if (result.equals("Update Available")) {
            assertDisplayedMessageContains(updatePO.eleIdLatestVersionDesc, "is ready to download and install");
            assertEnableStateEquals(updatePO.eleIdOTAUpdate, true);
        }
    }

    /**
     * C128872 Verify that users can update via OTA in panel's default state
     * Steps:
     * 1. Launch 'Update' app and check the status
     * 2. Click 'Online update' to update the panel
     * Author: Lin Shengjie
     */
    @Test(groups = "P0")
    public void C128872VerifyThatUsersCanUpdateViaOtaInPanelsDefaultState() {
        TestRail.setTestRailId("128872");
        assert updatePO != null;
        setTestCaseCleaner(() -> Adb.forceStop(UPDATE_PACKAGE));

        TestRail.addStepName("Launch 'Update' app and check the status");
        systemPO.startAppFromUnifiedLauncher("Update");

        // if Online Update button is grayed out, we don't do anything
        if (ElementHelper.isPresent(Locator.byTextContains("The latest updates have already been installed"), 5)) {
            assertEnableStateEquals(updatePO.eleIdOTAUpdate, false);
            return;
        }

        TestRail.addStepName("Click 'Online update' to update the panel");
        final String curVersion = Adb.getAndroidProp("ro.product.version");
        ElementHelper.click(Locator.byResourceId("id/btn_ota"));
        while (ElementHelper.isPresent(Locator.byResourceId("id/current_progress"))) {
            AppiumHelper.waitForSeconds(5);
        }
        assertTrue(ElementHelper.isPresent(Locator.byResourceId("id/iv_update_success"), 3), "online update fails");
        AppiumHelper.rebootPanel();
        assertFalse(curVersion.equals(Adb.getAndroidProp("ro.product.version")));
    }

    /**
     * C115946 Online update panel via ethernet
     * Steps:
     * 1. In Android Settings app -> disable WIFI
     * 2. Make sure ethernet cable is plugged in
     * 3. Go to Locker -> Launch Update app
     * 4. Click "Online Update Now" button
     * 5. When the upgrade is complete, restart the panel
     * 6. Confirm that panel upgrade success
     * Author: Lin Shengjie
     */
    @Test(groups = "P1")
    public void C115946OnlineUpdatePanelViaEthernet() {
        Boolean bDefaultWifiEnableStatus = null;
        try {
            TestRail.setTestRailId("115946");
            //disable wifi
            TestRail.addStepName("In Android Settings app -> disable WIFI");
            systemPO.stopSettingsApp();
            systemPO.startAppFromUnifiedLauncher("Settings");
            ElementHelper.click(Locator.byText("Network & internet"));
            ElementHelper.click(Locator.byText("Wi\u2011Fi"));
            bDefaultWifiEnableStatus = ElementHelper.isChecked(By.id("com.android.settings:id/switch_widget"));
            if(bDefaultWifiEnableStatus){
                ElementHelper.click(By.id("com.android.settings:id/switch_widget"));
            }

            //make sure to use ethernet
            TestRail.addStepName("Make sure ethernet cable is plugged in");
            Driver.getAndroidDriver().pressKey(new KeyEvent(AndroidKey.BACK));
            ElementHelper.click(Locator.byText("Ethernet settings"));
            ElementHelper.click(Locator.byText("Network status"));
            assertTrue(ElementHelper.isEnabled(Locator.byResourceId("id/ipaddress")));
            String regEx="((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
            assertTrue(ElementHelper.getText(Locator.byResourceId("id/ipaddress")).matches(regEx));
            systemPO.stopSettingsApp();

            //open update app
            TestRail.addStepName("Go to Locker -> Launch Update app");
            systemPO.startAppFromUnifiedLauncher("Update");
            if (ElementHelper.isPresent(Locator.byTextContains("The latest updates have already been installed"),5)) {
                assertEnableStateEquals(updatePO.eleIdOTAUpdate, false);
                return;
            }

            //online update
            TestRail.addStepName("Click \"Online Update Now\" button");
            final String curVersion = Adb.getAndroidProp("ro.product.version");
            updatePO.OnlineUpdateAndReboot();
            AppiumHelper.findElementAndClick("Xpath","//*[@text='Finished']");
            AndroidElement ele = AppiumHelper.findElement("//*[@text='Accept']",5);
            if (ele != null) {
                ele.click();
            }

            //confirm success
            TestRail.addStepName("Confirm that panel upgrade success");
            assertFalse(curVersion.equals(Adb.getAndroidProp("ro.product.version")));
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("01");
            throw e;
        }finally {
            systemPO.stopUpdateApp();
            systemPO.stopSettingsApp();

            if(null != bDefaultWifiEnableStatus){
                systemPO.startAppFromUnifiedLauncher("Settings");
                ElementHelper.click(Locator.byText("Network & internet"));
                ElementHelper.click(Locator.byText("Wi\u2011Fi"));
                if(bDefaultWifiEnableStatus !=
                        ElementHelper.isChecked(By.id("com.android.settings:id/switch_widget"))){
                    ElementHelper.click(By.id("com.android.settings:id/switch_widget"));
                }
                systemPO.stopSettingsApp();
            }
        }
    }

    /**
     * C116766 Verify that unzipped updated file should be supported when local update
     * Steps:
     * 1. Launch 'Update' app->click 'local updte'
     * 2. select the unzipped update package>click 'Update' icon
     * 3. local update with this unzipped folder
     * 3. When the upgrade is complete, restart the panel
     * 4. Confirm that panel upgrade success
     * Author: Lin Shengjie
     */
    @Test(groups = "P1")
    public void C116766VerifyThatUnzippedUpdateFileShouldBeSupportedWhenLocalUpdate() {
        TestRail.setTestRailId("116766");
        assert updatePO != null;
        setTestCaseCleaner(() -> Adb.forceStop(UPDATE_PACKAGE));

        TestRail.addStepName("Launch 'Update' app->click 'local updte'");
        systemPO.startAppFromUnifiedLauncher("Update");
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        TestRail.addStepName("select the unzipped update package>click 'Update' icon");
        List<MobileElement> elementList = ElementHelper.findElements(Locator.byResourceId("id/tv_file_name"));
        MobileElement meTarget = null;
        for (MobileElement me : elementList) {
            if (!me.getText().contains(".zip")) {
                meTarget = me;
                meTarget.click();
                break;
            }
        }
        if (null == meTarget) {
            //no update-able unzip folder
            assertFalse(ElementHelper.findElement(Locator.byResourceId("id/btn_local_update")).isEnabled(),
                    "none selected, but btn enabled");
            return;
        }

        TestRail.addStepName("local update with this unzipped folder");
        final String curVersion = Adb.getAndroidProp("ro.product.version");
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        while (ElementHelper.isPresent(Locator.byResourceId("id/current_progress"))) {
            AppiumHelper.waitForSeconds(5);
        }
        assertTrue(ElementHelper.isPresent(Locator.byResourceId("id/iv_update_success"), 3), "online update fails");

        TestRail.addStepName("When the upgrade is complete, restart the panel");
        AppiumHelper.rebootPanel();

        assertFalse(curVersion.equals(Adb.getAndroidProp("ro.product.version")));
    }

    /**
     * C119907 Verify that 'Update Install successfully' page will always pops up after 'Update' app launched if the Panel has not been restarted after ‘close’ button which is at the right bottom of 'Update Install successfully' clicked
     * Steps:
     * 1. Launch Update App and Successfully update
     * 2. Click 'close' button, Do any operation on the panel, such as open app
     * 3. Launch 'Update' app again,verify 'Update Install successfully' page pops up, and then close it again
     * 4. Launch 'Update' app again,and then reboot panel,verify 'Update Installation Complete' page
     *      will pop up rather than 'Update Install successfully' page
     * Author: Lin Shengjie
     */
    @Test(groups = "P1")
    public void C119907VerifyThatUpdateInstallSuccessfullyPageWillAlwaysPopupUntilReboot() {
        TestRail.setTestRailId("119907");
        assert updatePO != null;
        setTestCaseCleaner(() -> {
            systemPO.stopUpdateApp();
            systemPO.stopChromiumApp();
        });

        TestRail.addStepName("Launch Update App and Successfully update");
        systemPO.startAppFromUnifiedLauncher("Update");
        if (ElementHelper.isPresent(Locator.byTextContains("The latest updates have already been installed"), 5)) {
            assertEnableStateEquals(updatePO.eleIdOTAUpdate, false);
            return;
        }
        updatePO.OnlineUpdateWithoutReboot();

        TestRail.addStepName("Click 'close' button, Do any operation on the panel, such as open app");
        ElementHelper.click(Locator.byResourceId("id/btn_clse"));
        doAnythingWithChromium();

        TestRail.addStepName("Launch 'Update' app again,verify 'Update Install successfully' page pops up, and then close it again");
        systemPO.startAppFromUnifiedLauncher("Update");
        assertTrue(ElementHelper.isPresent(Locator.byTextContains("Update Installed Successfully"), 5));
        ElementHelper.click(Locator.byResourceId("id/btn_complete_later"));
        AppiumHelper.waitForSeconds(2);

        TestRail.addStepName("Launch 'Update' app again,and then reboot panel,verify");
        systemPO.startAppFromUnifiedLauncher("Update");
        assertTrue(ElementHelper.isPresent(Locator.byTextContains("Update Installed Successfully"), 5));

        final String curVersion = Adb.getAndroidProp("ro.product.version");
        String ip = AppiumHelper.getDeviceStatus();
        ElementHelper.click(Locator.byResourceId("id/btn_restart_now"));
        AppiumHelper.waitForSeconds(5);
        if (!(ip.equals(AppiumHelper.getDeviceStatus()))) {
            Log.info("Reboot not complete,wait for more 30s");
            AppiumHelper.waitForSeconds(30);
        }
        AppiumHelper.checkAndConnectDevice();
        assertTrue(ElementHelper.isPresent(Locator.byTextContains("Update Installation Complete"), 10));
        ElementHelper.click(Locator.byResourceId("id/finish"));
        assertFalse(curVersion.equals(Adb.getAndroidProp("ro.product.version")));

        //do anything on panel,launch update and verify  'Update Install successfully' page does not pop up
        doAnythingWithChromium();
        systemPO.startAppFromUnifiedLauncher("Update");
        assertTrue(!ElementHelper.isPresent(Locator.byTextContains("Update Installed Successfully"), 5));
    }

    /**
     * C113273 Verify that file with invalid title is not identified in update app
     * Steps:
     * 1. Insure the plugged usb contains a zip named  "Invalid_lowtier.zip" that is packed with a few files
     * 2. Launch 'Update' app and click local update button
     * 3. verify "Invalid_lowtier.zip" file name is not in available list
     * Author: Lin Shengjie
     */
    @Test(groups = "P1")
    public void C113273VerifyThatFileWithInvalidTitleIsNotIdentifiedInUpdateApp() throws Exception {
        TestRail.setTestRailId("113273");
        final String INVALID_ZIP_NAME = "Invalid_lowtier.zip";
        assert updatePO != null;

        TestRail.addStepName("Insure the plugged usb contains a zip named  \"Invalid_lowtier.zip\" that is packed with a few files");
        assertTrue(updatePO.copyFalseRomToUsbRootDir(INVALID_ZIP_NAME));
        setTestCaseCleaner(() -> {
            systemPO.stopUpdateApp();
            try {
                updatePO.deleteFileFromRootUsbDir(INVALID_ZIP_NAME);
            } catch (Exception e) {
                assertTrue(false);
            }
        });

        TestRail.addStepName("Launch 'Update' app and click local update button");
        systemPO.startAppFromUnifiedLauncher("Update");
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));

        TestRail.addStepName("verify \"Invalid_lowtier.zip\" file name is not in available list");
        assertTrue(!ElementHelper.isPresent(Locator.byText("Invalid_lowtier.zip")));
    }

    /* C116690 Verify that the installation can be cancelled during the package installing
     * 1. Launch 'Update' app, Local update for a while and then cancel
     * 2. Check Update shows the main page
     * 3. Online update for a while and then cancel
     * 4. Check Update shows the main page
     * 5. Verify local update can succeed
     * Author:Lin Shengjie
     */
    @Test(groups = "P2")
    public void C116690VerifyThatTheInstallationCanBeCancelledDuringThePackageInstalling() {
        TestRail.setTestRailId("116690");
        TestRail.addStepName("Launch 'Update' app, Local update for a while and then cancel");
        systemPO.startAppFromUnifiedLauncher("Update");
        setTestCaseCleaner(() -> {
            systemPO.stopUpdateApp();
        });
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        List<MobileElement> elementList = ElementHelper.findElements(Locator.byResourceId("id/tv_file_name"), 5);
        assertTrue(null != elementList && !elementList.isEmpty());
        elementList.get(0).click();
        assertTrue(ElementHelper.findElement(Locator.byResourceId("id/btn_local_update")).isEnabled());
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        AppiumHelper.waitForSeconds(10);
        assertTrue(!ElementHelper.isPresent(Locator.byResourceId("id/tv_error_title")));
        ElementHelper.click(Locator.byResourceId("id/cancel"));

        TestRail.addStepName("Check Update shows the main page");
        assertTrue(ElementHelper.isPresent(Locator.byResourceId("id/tv_system_info"), 3));

        TestRail.addStepName("Online update for a while and then cancel");
        if (!ElementHelper.isPresent(Locator.byTextContains("The latest updates have already been installed"),5)) {
            assertEnableStateEquals(updatePO.eleIdOTAUpdate, true);
            ElementHelper.findElement(Locator.byResourceId("id/btn_ota")).click();
            AppiumHelper.waitForSeconds(10);
            assertTrue(!ElementHelper.isPresent(Locator.byResourceId("id/tv_error_title")));
            ElementHelper.click(Locator.byResourceId("id/cancel"));

            TestRail.addStepName("Check Update shows the main page");
            assertTrue(ElementHelper.isPresent(Locator.byResourceId("id/tv_system_info"), 3));
        }

        TestRail.addStepName("Verify local update can succeed");
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        elementList = ElementHelper.findElements(Locator.byResourceId("id/tv_file_name"), 5);
        assertTrue(null != elementList && !elementList.isEmpty());
        elementList.get(0).click();
        assertTrue(ElementHelper.findElement(Locator.byResourceId("id/btn_local_update")).isEnabled());
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        while (ElementHelper.isPresent(Locator.byResourceId("id/current_progress"))){
            AppiumHelper.waitForSeconds(5);
        }
        assertTrue(ElementHelper.isPresent(Locator.byResourceId("id/iv_update_success"), 3), "online update fails");
        AppiumHelper.rebootPanel();
        if (ElementHelper.isPresent(Locator.byTextContains("Update Installation Complete"), 10)) {
            ElementHelper.click(Locator.byResourceId("id/finish"));
        }
        AndroidElement ele = AppiumHelper.findElement("//*[@text='Accept']",5);
        if (ele != null) {
            ele.click();
        }
    }

    /* C176785 Verify that users can update via Local in panel's default state
     * 1. Launch 'Update' app and check the status
     * 2.Click 'local update' to update the panel
     * Author:Sita
     */
    @Test(groups = "P0")
    public void C119861PanelsSystemInformationDisplayedCorrectly() {
        TestRail.setTestRailId("119861");
        setTestCaseCleaner(() -> {
            systemPO.stopUpdateApp();
        });
        TestRail.addStepName("Launch 'Update' app and check the status");
        systemPO.startAppFromUnifiedLauncher("Update");
        //1.Online update and local update are available
        //2.no message is displayed stating that updating has been disabled
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Online Update Now"),10));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Local Update"),10));
        Assert.assertFalse(ElementHelper.isVisible(Locator.byText("updating has been disabled"),10));
        TestRail.addStepName("Click 'local update' to update the panel");
        ElementHelper.click(Locator.byText("Local Update"));
        ElementHelper.clickWhenVisible(FILES);
        ElementHelper.waitUntilVisible(By.id("com.prometheanworld.update:id/btn_local_update"),10);
        ElementHelper.click(By.id("com.prometheanworld.update:id/btn_local_update"));
        //Panel can be updated successfully
        ElementHelper.waitUntilVisible(Locator.byText("Update Installed Successfully"),1800);
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update Installed Successfully"),10));
        try {
            ElementHelper.clickWhenEnabled(Locator.byText("Restart Now"));
        } catch (Exception e) {}
        AppiumHelper.waitForSeconds(30);
        AppiumHelper.checkAndConnectDevice();
        if (ElementHelper.isVisible(Locator.byText("Update Installation Complete"),30)) {
            ElementHelper.clickWhenEnabled(Locator.byText("Finished"));
        }
    }

    /**
     * C116705 Verify that user can download error log to local if error occurs when installing
     * Steps:
     * 1. Plug in a USB that contains a false Update zip
     * 2. Launch 'Update' app and update with this false update zip
     * 3. verify error occurs
     * 4. click Download Error Log button and save.
     * 5. open files app and check this log exists.
     * Author: Lin Shengjie
     */
    @Test(groups = "P1")
    public void C116705VerifyThatUserCanDownloadErrorLogToLocalIfErrorOccursWhenInstalling() throws Exception {
        TestRail.setTestRailId("116705");
        final String FALSE_ROM_NAME = getFalseRomFileName();
        TestRail.addStepName("Plug in a USB that contains a false Update zip");
        setTestCaseCleaner(() -> {
            systemPO.stopUpdateApp();
            systemPO.stopFilesApp();
            try {
                updatePO.deleteFileFromRootUsbDir(FALSE_ROM_NAME);
            } catch (Exception e) {
                assertTrue(false);
            }
        });
        assertTrue(updatePO.copyFalseRomToUsbRootDir(FALSE_ROM_NAME));
        TestRail.addStepName("Launch 'Update' app and update with this false update zip");
        systemPO.startAppFromUnifiedLauncher("Update");
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        ElementHelper.click(Locator.byText(FALSE_ROM_NAME));
        ElementHelper.click(Locator.byResourceId("id/btn_local_update"));
        AppiumHelper.waitForSeconds(3);

        TestRail.addStepName("verify error occurs");
        ElementHelper.isPresent(Locator.byResourceId("id/tv_error_title"), 10);

        TestRail.addStepName("click Download Error Log button and save");
        ElementHelper.click(Locator.byResourceId("id/btn_download_error_log"));
        AppiumHelper.waitForSeconds(2);
        String logName = Driver.getAndroidDriver().findElement(Locator.byResourceId("id/title")).getText();
        ElementHelper.click(Locator.byText("SAVE"));

        TestRail.addStepName("open files app and check this log exists");
        systemPO.startAppFromUnifiedLauncher("Files");
        AppiumHelper.waitForSeconds(2);
        AndroidElement eleLog = Driver.getAndroidDriver().findElement(Locator.byTextContains(logName));
        assertNotNull(eleLog);
    }


    private void resetStatus() {
        Adb.setAndroidProp(PROP_LOCAL_UPDATE, "true");
        Adb.setAndroidProp(PROP_OTA_UPDATE, "true");
    }

    private void assertEnableStateEquals(String id, boolean state) {
        assertEquals(ElementHelper.isEnabled(By.id(id)), state);
    }

    private void assertDisplayedMessageEquals(String id, String message) {
        ElementHelper.waitUntilTextToBe(By.id(id), message, 10);
    }

    private void assertDisplayedMessageContains(String id, String message) {
        assertTrue(ElementHelper.getText(By.id(id)).contains(message));
    }

    private String getFalseRomFileName() {
        String strSrcZip = "false_mt5680_update.zip";
        if (SubSystem.isMT9950()) {
            strSrcZip = "false_mt9950_update.zip";
        }
        return strSrcZip;
    }

    private void doAnythingWithChromium(){
        //open Chromium and then close
        systemPO.startAppFromUnifiedLauncher("Chromium");
        AppiumHelper.waitForSeconds(3);
        AppiumHelper.findElementAndClick("ID", "org.chromium.chrome:id/url_bar");
        AndroidElement url = Driver.getAndroidDriver().findElementById("org.chromium.chrome:id/url_bar");
        url.sendKeys("www.youku.com");
        AppiumHelper.waitForSeconds(2);
        Driver.getAndroidDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
        AppiumHelper.waitForSeconds(5);
        systemPO.stopChromiumApp();
    }
}