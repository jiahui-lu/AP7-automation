package com.prometheanworld.AP9.Firmware.AndroidSettings.System;

import com.nd.automation.core.action.Direction;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.kotlin.AssumeKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.util.FileUtil;
import com.nd.automation.core.util.JsonUtil;
import com.prometheanworld.AP9.Firmware.AndroidSettings.UserAccess;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.hardware.SubSystem;
import com.prometheanworld.appium.frame.model.AP9.*;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ImportExportConfigurationTest extends BaseTest {
    private SettingConfigurationPO configurationPO = POFactory.getInstance(SettingConfigurationPO.class);
    private FilesPO filesPO = POFactory.getInstance(FilesPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final ProximitySensorPO proximitySensorPO = POFactory.getInstance(ProximitySensorPO.class);
    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);

    final By continueButton = Locator.byText("CONTINUE");
    final By settingApp = Locator.byText("Settings");
    final By updateApp = Locator.byText("Update");
    final By panelManagementApp = Locator.byText("Panel Management");
    final By importProgressBar = Locator.byResourceId("id/pb_ipt_cfg_progress");
    String standByTimer = ("/hierarchy/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.SeekBar\n");
    String sleepTimer = ("/hierarchy/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.SeekBar");
    private boolean needUnlock = false;

    @Override
    protected String testAppName() {
        return "Settings";
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
        configurationPO.closeSettingWindow();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        super.afterMethod();
        try {
            if (needUnlock) {
                AppiumHelper.exeAdbRoot();
                Adb.setGlobalSettings("prome_lock_admin_access", "0");
                needUnlock = false;
            }
        } catch (Exception e) {}
    }

    /**
     * C119782 Verify that the information of panel class and version displayed in Import/export configuration is correct.
     * 1. Menu bar -> Application locker -> Update app, get the current class and firmware version
     * 2. Android Settings -> System -> Import/export configuration.
     * 3. Click and pop up a dialog with panel information;----- Confirm that the panel information displayed is the same as step 1.
     */
    @Test(groups = {"P0"})
    public void C119782VerifyPanelClassInfoVersionDisplayedImportExportConfigurationIsCorrect() {
        TestRail.setTestRailId("119782");

        final String prefix_model = "Model:";

        String modelFromUpdate = null;
        String firmwareVersionFromUpdate = null;

        // Launch the Update app in the Locker app.
        String[] apk = {"Update.apk", "Update", "com.prometheanworld.update", "//*[@text='Update ActivPanel']", "//*[@resource-id='com.prometheanworld.update:id/btn_clse']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:" + apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        TestRail.addStepName("Open the Menu bar and launch the Update app in the Locker.");
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"), apk[2]);

            AndroidElement updateInfoTextView = Driver.getAndroidDriver().findElementByXPath("//*[@resource-id='com.prometheanworld.update:id/tv_system_info']");
            // Verify the Update app system info text view is NOT null.
            Assert.assertNotNull(updateInfoTextView);
            // Verify the content text of Update app system info text view is NOT null.
            String updateInfoText = updateInfoTextView.getText();
            Assert.assertNotNull(updateInfoText);
            TestRail.addStepName("Verify the content of the system information in the Update app is available.");

            // Extra the model and firmware version from Update app.
            if (updateInfoText.contains("\n\n")) {
                String[] info = updateInfoText.split("\n\n");
                for (String s : info) {
                    s = s.replace("\n", "");

                    final String prefix_mainboard_firmware = "Mainboard firmware:";
                    if (s.contains(prefix_model)) {
                        modelFromUpdate = s.substring(s.indexOf(prefix_model) + prefix_model.length()).trim();
                    } else if (s.contains(prefix_mainboard_firmware)) {
                        firmwareVersionFromUpdate = s.substring(s.indexOf(prefix_mainboard_firmware) + prefix_mainboard_firmware.length()).trim();
                    }
                }
            }

            // Verify the model from Update app is valid.
            Assert.assertNotNull(modelFromUpdate);
            TestRail.addStepName("Extra the panel model from the Update app.");

            // Verify the firmware version from Update app is valid.
            Assert.assertNotNull(firmwareVersionFromUpdate);
            TestRail.addStepName("Extra the panel firmware version from the Update app.");
        }
        if (apk.length >= 5) {
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"), apk[2]);
            eleClose.click();
            TestRail.addStepName("Close the Update app.");
        }

        configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);
        TestRail.addStepName("Navigate to Android Settings -> System -> Import/export configuration");

        AndroidElement modelTextView = Driver.getAndroidDriver().findElementByXPath("//*[contains(@text,'" + prefix_model + "') and @resource-id='com.prometheanworld.settingsconfiguration:id/class_textView']");
        // Verify the model text view is NOT null.
        Assert.assertNotNull(modelTextView);
        // Verify the content text of model text view is NOT null.
        String modelFromConfiguration = modelTextView.getText();
        Assert.assertNotNull(modelFromConfiguration);
        modelFromConfiguration = modelFromConfiguration.substring(modelFromConfiguration.indexOf(prefix_model) + prefix_model.length()).trim();
        TestRail.addStepName("Extra the panel model from the SettingConfiguration app.");

        final String prefix_firmware_version = "Firmware version:";
        AndroidElement firmwareVersionTextView = Driver.getAndroidDriver().findElementByXPath("//*[contains(@text,'" + prefix_firmware_version + "') and @resource-id='com.prometheanworld.settingsconfiguration:id/firmware_textView']");
        // Verify the firmware version text view is NOT null.
        Assert.assertNotNull(firmwareVersionTextView);
        // Verify the content text of firmware version text view is NOT null.
        String firmwareVersionFromConfiguration = firmwareVersionTextView.getText();
        Assert.assertNotNull(firmwareVersionFromConfiguration);
        firmwareVersionFromConfiguration = firmwareVersionFromConfiguration.substring(firmwareVersionFromConfiguration.indexOf(prefix_firmware_version) + prefix_firmware_version.length()).trim();
        TestRail.addStepName("Extra the panel firmware version from the SettingConfiguration app.");

        // Verify the model from Update and from SettingConfiguration is same.
        Assert.assertEquals(modelFromUpdate, modelFromConfiguration);
        TestRail.addStepName("Verify the panel model from the Update app is the same from the SettingConfiguration app.");

        // Verify the firmware version from Update app and from SettingConfiguration is same.
        Assert.assertEquals(firmwareVersionFromConfiguration, firmwareVersionFromUpdate);
        TestRail.addStepName("Verify the panel firmware version from the Update app is the same from the SettingConfiguration app.");

        configurationPO.closeSettingConfiguration();
    }

    /**
     * C119786 Verify that users can export Settings configuration file to panel local storage.
     * 1. Android Settings -> System -> Import/export configuration
     * 2. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     * 3. Click "CONTINUE" button to display the list of settings to be exported, drag up and down to review them
     * 4. Click "CONTINUE" button and the default Files app opens up
     * 5. Choose "Internal Storage", enter file name and click "EXPORT" button;---- Pops up a dialog with "Export successful"
     * 6. Click "CLOSE" to close it
     * 7. Menu bar -> Application Locker -> Files app;---- Confirm that the .cfg file saved to panel local storage
     */
    @Test(groups = {"P0"})
    public void C119786VerifyUsersCanExportSettingsConfigurationFileToPanelLocalStorage() {
        TestRail.setTestRailId("119786");

        configurationPO.continueExportConfigurationToFilePicker();

        AppiumHelper.switchWindowToFullScreen();

        final String testCaseCfgName = "C119786.cfg";
        filesPO.selectStorage("internal");
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        String cfgFilePath = "/sdcard/" + testCaseCfgName;
        boolean cfgFileExist = configurationPO.checkConfigurationFileExist(cfgFilePath);
        if (cfgFileExist) {
            configurationPO.removeConfigurationFileByPath(cfgFilePath);
        }
        Assert.assertTrue(cfgFileExist);
    }

    /**
     * C119785
     * Verify that users can export Settings configuration file to USB drive
     * 1. Plugged in USB drive
     * 2. Android Settings -> System -> Import/export configuration
     * 3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     * 4. Click "CONTINUE" button to display the list of settings to be exported, drag up and down to review them
     * 5. Click "CONTINUE" button and the default Files app opens up, choose USB folder; ---- The default .cfg file name is "Class_firmware version", for example, "ActivPanel_9_Premium_v1.0.0.x" in 9950 and "ActivPanel_9_v1.0.0.x" in 5680
     * 6. Enter file name and click "EXPORT" button; ---- Pops up a dialog with "Export successful"
     * 7. Click "CLOSE" to close it
     * 8. Menu bar -> Application Locker -> Files app; ---- Confirm that the .cfg file is saved to the USB drive
     */
    @Test(groups = {"P0", "UserDebug"})
    public void C119785VerifyUsersCanExportSettingsConfigurationFileToUsbDrive() {
        TestRail.setTestRailId("119785");

        AppiumHelper.exeAdbRoot();
        final String path_media_rw = "/mnt/media_rw/";
        List<String> rwMedias = Adb.adb("shell ls " + path_media_rw);
        boolean usbPlugged = false;
        if (rwMedias != null && !rwMedias.isEmpty()) {
            for (String s : rwMedias) {
                if (s != null && !s.equals("sdcard")) {
                    usbPlugged = true;
                    break;
                }
            }
        }
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        configurationPO.continueExportConfigurationToFilePicker();

        AppiumHelper.switchWindowToFullScreen();

        final String testCaseCfgName = "C119785.cfg";
        filesPO.selectStorage("usb");
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

        boolean cfgFileExist = false;
        for (String s : rwMedias) {
            String cfgFilePath = path_media_rw + s + "/" + testCaseCfgName;
            boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
            if (exist) {
                configurationPO.removeConfigurationFileByPath(cfgFilePath);
                cfgFileExist = true;
                break;
            }
        }
        Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT FOUND.");
        TestRail.addStepName("Verify the configuration file has been exported to one USB drive that has been plugged.");
    }

    /**
     * C119796 Verify that the format and content of Settings configuration file is correct
     * Steps:
     * 1. Plugged in USB drive to a panel
     * 2. Android Settings -> System -> Import/export configuration
     * 3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     * 4. Check all options, click "CONTINUE" button to display the list of settings to be exported, drag up and down to confirm that these three options are displayed in the list
     * 5. Click "CONTINUE" button, enter file name and export .cfg file to USB drive
     * 6. Plug this USB drive to PC, change the .cfg extension to .zip --> Confirm that a image file of wallpaper is in the zip
     * Confirm that the .cfg file is formatted correctly and the contens is the the same as the panel settings
     * Note: See CFG format in https://wiki.prometheanjira.com/display/AP7/Setttings+Configuration+Overall+Design
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119796VerifyThatTheFormatAndContentOfSettingsConfigurationFileIsCorrect() {
        TestRail.setTestRailId("119796");

        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        configurationPO.continueExportConfigurationWithOptionsToFilePicker(true, true, true);

        AppiumHelper.switchWindowToFullScreen();

        final String TEST_CASE_NAME = "C119796";
        final String CONFIGURATION_FILE_POSTFIX = ".cfg";
        final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

        filesPO.selectStorage("usb");
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

        final String path_media_rw = "/mnt/media_rw/";
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        String cfgFileFolder = "";
        String cfgFilePath = "";
        boolean cfgFileExist = false;
        for (String s : rwMedias) {
            cfgFileFolder = path_media_rw + s;
            cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
            boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
            if (exist) {
                cfgFileExist = true;
                break;
            }
        }
        Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
        TestRail.addStepName("Verify the configuration file has been exported to one USB drive that has been plugged.");

        String cfgFileName = new File(cfgFilePath).getName();
        Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                        && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");

        // Pull the testing exported configuration file from the use device.
        File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);

        // Delete the testing exported configuration file on the usb device.
        configurationPO.removeConfigurationFileByPath(cfgFilePath);

        Assert.assertTrue(unzipCfgFolder != null && unzipCfgFolder.isDirectory(),
                "Expect the unzip configuration folder exists, but NOT exists.");
        TestRail.addStepName("Verify the exported configuration file can be unzipped.");

        File wallpaper = new File(unzipCfgFolder.getAbsoluteFile() + "/wallpaper.png");
        Assert.assertTrue(wallpaper.exists() && wallpaper.length() > 0,
                "Expect the exported wallpaper file exists, but NOT exists.");
        TestRail.addStepName("Verify the exported wallpaper file exists.");

        File configJsonFile = new File(unzipCfgFolder.getAbsoluteFile() + "/settingConfiguration.json");
        Assert.assertTrue(configJsonFile.exists() && configJsonFile.length() > 0,
                "Expect the exported configuration json file exists, but NOT exists.");
        TestRail.addStepName("Verify the exported configuration json file exists.");

        configurationPO.checkConfigurationJsonFileFormatCorrectly(configJsonFile);
        FileUtil.deleteFile(unzipCfgFolder);
    }

    /**
     * C119807 Verify that users can't import a Settings configuration file in an illegal format
     * Steps:
     * 1. Create a new .txt file in PC, change the .txt extension to .cfg
     * 2. Copy this CFG files to USB Drive
     * 3. Plug this USB in same model and same version of panel
     * 4. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button
     * 5. Select .CFG file and then click the Import button --> Confirm that the configuration file cannot be read and get the error message "You can try to import the file again. If you continue to receive this error, the file may be corrupted."
     * 6. Click the TRY AGAIN button --> Confirm that you can re-import this cfg file
     * 7. Click the Cancel button to close the pop-up window --> Confirm that the settings configuration has not changed
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119807VerifyThatUsersCanNotImportSettingsConfigurationFileInAnIllegalFormat() throws IOException {
        TestRail.setTestRailId("119807");

        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        final String TEST_CASE_ID = "C119807";
        final String FAKE_CONFIGURATION_FILE_NAME = TEST_CASE_ID + ".cfg";
        File fakeConfigurationFile = new File(TEST_CASE_ID + ".txt");
        if (fakeConfigurationFile.exists()) {
            fakeConfigurationFile.delete();
        }
        fakeConfigurationFile.createNewFile();
        TestRail.addStepName("Create a new \".txt\" file in PC.");

        fakeConfigurationFile = new File(FAKE_CONFIGURATION_FILE_NAME);
        if (fakeConfigurationFile.exists()) {
            fakeConfigurationFile.delete();
        }
        fakeConfigurationFile = new File(TEST_CASE_ID + ".txt");
        fakeConfigurationFile.renameTo(new File(FAKE_CONFIGURATION_FILE_NAME));
        TestRail.addStepName("Change the .txt extension to \".cfg\".");

        fakeConfigurationFile = new File(FAKE_CONFIGURATION_FILE_NAME);
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        boolean filePushed = configurationPO.pushConfigurationFile(fakeConfigurationFile, "/mnt/media_rw/" + rwMedias.get(0));
        fakeConfigurationFile.delete();
        Assert.assertTrue(filePushed, "Expect the fake configuration file been pushed to the plugged USB drive successful, but FAILURE.");
        TestRail.addStepName("Copy this CFG files to USB Drive.");

        configurationPO.continueImportConfigurationToFilePicker();

        AppiumHelper.switchWindowToFullScreen();

        filesPO.selectStorage("usb");
        filesPO.switchToMenuList();
        boolean foundCfgFile = filesPO.searchFileByNameAndClick(FAKE_CONFIGURATION_FILE_NAME);
        Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
        final String btnImportPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/tv_ipt_start']";
        ElementHelper.waitUntilVisible(By.xpath(btnImportPath), 5);
        Driver.getAndroidDriver().findElementByXPath(btnImportPath).click();
        TestRail.addStepName("Select .CFG file and then click the Import button.");

        final String importErrorMsgPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/tv_ipt_cfg_result_msg']";
        ElementHelper.waitUntilVisible(By.xpath(importErrorMsgPath), 5);
        final String IMPORT_ERROR_MESSAGE = "You can try to import the file again.\n" +
                "If you continue to get this error, the file may be corrupted.";
        String currentDisplayErrorMsg = Driver.getAndroidDriver().findElementByXPath(importErrorMsgPath).getText();
        Assert.assertEquals(IMPORT_ERROR_MESSAGE, currentDisplayErrorMsg, "Expect the import error message display, but NOT match.");
        TestRail.addStepName("Verify that the configuration file cannot be read and get the error message \"You can try to import the file again. If you continue to receive this error, the file may be corrupted.\".");

        final String retryBtnPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/try_again_button']";
        Driver.getAndroidDriver().findElementByXPath(retryBtnPath).click();
        currentDisplayErrorMsg = Driver.getAndroidDriver().findElementByXPath(importErrorMsgPath).getText();
        Assert.assertEquals(IMPORT_ERROR_MESSAGE, currentDisplayErrorMsg, "Expect the import error message display when retry, but NOT match.");
        TestRail.addStepName("Verify that you can re-import this cfg file, and still failure again.");

        final String cancelBtnPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/cancel_button']";
        Driver.getAndroidDriver().findElementByXPath(cancelBtnPath).click();
        configurationPO.removeConfigurationFileByPath("/mnt/media_rw/" + rwMedias.get(0) + "/" + FAKE_CONFIGURATION_FILE_NAME);
    }

    /**
     * C119784 Verify that the checked state of options in export dialog are correct
     * Steps:
     * 1. Plugged in USB stick
     * 2. Android Settings -> System -> Import/export configuration
     * 3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog -->
     * [1] There is a export options, "Lock the Settings, Panel Management, and Update apps" is unchecked by default
     * [2] "Export network settings" and "Include wallpaper file" are checked by default
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119784VerifyThatTheCheckedStateOfOptionsInExportDialogAreCorrect() {
        TestRail.setTestRailId("119784");

        configurationPO.continueExportConfigurationToExportOptions();

        String continueBtnPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/continue_button']";
        ElementHelper.waitUntilVisible(By.xpath(continueBtnPath), 5);

        AndroidElement networkCheckBox = configurationPO.getOptionCheckBox("network_checkBox");
        Boolean networkChecked = configurationPO.isElementIsChecked(networkCheckBox);
        Assert.assertTrue(networkChecked != null && networkChecked,
                "Expect the network export check box is checked, but NOT checked.");
        TestRail.addStepName("Verify the network export check box is checked");

        AndroidElement wallpaperCheckBox = configurationPO.getOptionCheckBox("wallerpaper_checkBox");
        Boolean wallpaperChecked = configurationPO.isElementIsChecked(wallpaperCheckBox);
        Assert.assertTrue(wallpaperChecked != null && wallpaperChecked,
                "Expect the wallpaper export check box is checked, but NOT checked.");
        TestRail.addStepName("Verify the wallpaper export check box is checked");

        AndroidElement lockCheckBox = configurationPO.getOptionCheckBox("lock_checkBox");
        Boolean lockChecked = configurationPO.isElementIsChecked(lockCheckBox);
        Assert.assertTrue(lockChecked != null && !lockChecked,
                "Expect the lock app export check box is not checked, but IS checked.");
        TestRail.addStepName("Verify the lock app export check box is not checked");

        configurationPO.cancelSettingConfiguration();
    }

    /**
     * C119808 Verify that users can't import a Settings configuration file with no wallpaper files
     * Steps:
     * 1. Plug USB in one panel
     * 2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'EXPORT' button, save .CFG file to USB
     * --> Confirm that the export option include "Include wallpaper file" option
     * 3. Plug this USB drive to PC, change the .cfg extension to .zip
     * 4. Delete the image file of wallpaper is in the zip, change the .zip extension back to .cfg
     * 5. Plug this USB in same model and same version of panel
     * 6. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button
     * 7. Select .CFG file and then click the Import button
     * --> Confirm that unable to import this .cfg file, and pops up an error message "The wallpaper file cannot be found. Please import a new configuration file" Confirm that the settings configuration has not changed
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119808VerifyThatUsersCanNotImportSettingsConfigurationFileWithNoWallpaperFiles() {
        TestRail.setTestRailId("119808");

        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        String cfgFilePath = "";
        try {
            configurationPO.continueExportConfigurationToExportOptions();
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/continue_button"), 5);
            final boolean wallpaperCheckboxChecked = configurationPO.hasCheckBoxChecked("wallerpaper_checkBox");
            TestRail.addStepName("Verify that the export option include \"Include wallpaper file\" option.");
            Assert.assertTrue(wallpaperCheckboxChecked, "Expect the wallpaper check box is checked, but NO.");
            configurationPO.continueExportConfigurationToFilePickerFromOptionsPage();

            AppiumHelper.switchWindowToFullScreen();

            final String TEST_CASE_NAME = "C119808";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
            TestRail.addStepName("Verify the configuration file has been exported to one USB drive that has been plugged.");

            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");

            // Pull the testing exported configuration file from the use device, modify it, remove the wallpaper and push back to the usb device.
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            TestRail.addStepName("Plug this USB drive to PC, change the .cfg extension to .zip");
            File wallpaper = new File(unzipCfgFolder.getAbsoluteFile() + "/wallpaper.png");
            if (wallpaper.exists()) {
                wallpaper.delete();
            }
            File newCfgFile = configurationPO.zipConfigurationFileOnlyToGenerateNewCfgFile(unzipCfgFolder, testCaseCfgName);
            Assert.assertTrue(newCfgFile != null && newCfgFile.exists() && newCfgFile.length() > 0,
                    "Expect generate a new valid '.cfg' configuration file without wallpaper, but NOT valid.");
            TestRail.addStepName("Delete the image file of wallpaper is in the zip, change the .zip extension back to .cfg");

            boolean filePushed = configurationPO.pushConfigurationFile(newCfgFile, "/mnt/media_rw/" + rwMedias.get(0));
            newCfgFile.delete();
            Assert.assertTrue(filePushed, "Expect the new configuration file without wallpaper been pushed to the plugged USB drive successful, but FAILURE.");
            TestRail.addStepName("Copy this CFG files to USB Drive.");

            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
            TestRail.addStepName("Select .CFG file and then click the Import button.");

            ElementHelper.waitUntilVisible(Locator.byResourceId("id/no_wallpaper_msg"), 30);
            final String IMPORT_ERROR_MESSAGE = "The wallpaper file cannot be found. Please import a new configuration file.";
            String currentDisplayErrorMsg = ElementHelper.findElement(Locator.byResourceId("id/no_wallpaper_msg")).getText();
            Assert.assertEquals(IMPORT_ERROR_MESSAGE, currentDisplayErrorMsg, "Expect the import error message display, but NOT match.");
            TestRail.addStepName("Verify that unable to import this .cfg file, and pops up an error message \"The wallpaper file cannot be found. Please import a new configuration file\".");
            ElementHelper.click(Locator.byResourceId("id/close_button"));
        } finally {
            // Delete the testing exported configuration file on the usb device.
            configurationPO.removeConfigurationFileByPath(cfgFilePath);
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /**
     * C119832 Verify that users can't import Settings configuration to lower version panel
     * Steps:
     * 1. Plug USB in one panel V1.1.0.x
     * 2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file to USB
     * 3. Plug in this USB in another panel of the same type for version 1.0.0.x
     * 4. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> IMPORT
     * --> Confirm that it fails to import, and get an error message "This file firmware version(1.1.0.x) does not match the fimeware version of the panel(1.0.0.x)"
     * --> Confirm that the background is grayed out at this time and that no other element is clickable except the close button
     * 5. Click on the CLOSE button and the prompt box closes
     * --> Confirm that the settings configuration has not changed
     * Note: You can unzip and modify the firmwareVersion
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119832VerifyThatUsersCanNotImportSettingsConfigurationToLowerVersionPanel() {
        TestRail.setTestRailId("119832");

        TestRail.addStepName("Plug USB in one panel V1.1.0.x.");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        String cfgFilePath = "";
        try {
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
            AppiumHelper.switchWindowToFullScreen();

            final String TEST_CASE_NAME = "C119832";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);
            final String FIRMWARE_VERSION = "firmwareVersion";
            Assert.assertTrue(cfgMap.containsKey(FIRMWARE_VERSION), "Expect the setting configuration file has \"firmwareVersion\", but NOT found.");
            final String firmwareVersion = (String) cfgMap.get(FIRMWARE_VERSION);
            final String newFirmwareVersion = configurationPO.increaseFirmwareVersion(firmwareVersion, 50);
            cfgMap.put(FIRMWARE_VERSION, newFirmwareVersion);
            File newCfgFile = configurationPO.generateNewCfgFileFromJson(JsonUtil.toJson(cfgMap), testCaseCfgName);
            FileUtil.deleteFile(unzipCfgFolder);
            boolean filePushed = configurationPO.pushConfigurationFile(newCfgFile, cfgFileFolder);
            newCfgFile.delete();
            Assert.assertTrue(filePushed, "Expect the new configuration file with increased firmware version been pushed to the plugged USB drive successful, but FAILURE.");
            TestRail.addStepName("Plug in this USB in another panel of the same type for version " + firmwareVersion + ".");

            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
            TestRail.addStepName("Select .CFG file and then click the Import button.");

            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_cfg_result_msg"), 10);
            final String IMPORT_ERROR_MESSAGE = "This file’s firmware version (" + newFirmwareVersion + ") does not match the firmware version of the panel (" + firmwareVersion + ").";
            String currentDisplayErrorMsg = ElementHelper.findElement(Locator.byResourceId("id/tv_ipt_cfg_result_msg")).getText();
            Assert.assertEquals(IMPORT_ERROR_MESSAGE, currentDisplayErrorMsg, "Expect the import error message \"" + IMPORT_ERROR_MESSAGE + "\"display, but NOT match.");
            TestRail.addStepName("Verify that it fails to import, and get an error message \"" + IMPORT_ERROR_MESSAGE + "\"");

            ElementHelper.click(Locator.byResourceId("id/close_button"));
            TestRail.addStepName("Click on the CLOSE button and the prompt box closes");
        } finally {
            // Delete the testing exported configuration file on the usb device.
            configurationPO.removeConfigurationFileByPath(cfgFilePath);
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /**
     * C119833 Verify that users can't import Settings configuration to higher version panel
     * Steps:
     * 1. Plug USB in one panel V1.0.0.x
     * 2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file to USB
     * 3. Plug in this USB in another panel of the same type for version 1.1.0.x
     * 4. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> IMPORT
     * --> Confirm that it fails to import, and get an error message "This file firmware version(1.0.0.x) does not match the fimeware version of the panel(1.1.0.x)"
     * --> Confirm that the background is grayed out at this time and that no other element is clickable except the close button
     * 5. Click on the CLOSE button and the prompt box closes
     * --> Confirm that the settings configuration has not changed
     * Note: You can unzip and modify the firmwareVersion
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119833VerifyThatUsersCanNotImportSettingsConfigurationToHigherVersionPanel() {
        TestRail.setTestRailId("119833");

        TestRail.addStepName("Plug USB in one panel V1.0.0.x.");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        try {
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
            AppiumHelper.switchWindowToFullScreen();

            final String TEST_CASE_NAME = "C119833";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            String cfgFilePath = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);
            final String FIRMWARE_VERSION = "firmwareVersion";
            Assert.assertTrue(cfgMap.containsKey(FIRMWARE_VERSION), "Expect the setting configuration file has \"firmwareVersion\", but NOT found.");
            final String firmwareVersion = (String) cfgMap.get(FIRMWARE_VERSION);
            final String newFirmwareVersion = configurationPO.decreaseFirmwareVersion(firmwareVersion, 5);
            cfgMap.put(FIRMWARE_VERSION, newFirmwareVersion);
            // Simulate the firmware version of the panel is higher than the firmware version of the configuration file.
            File newCfgFile = configurationPO.generateNewCfgFileFromJson(JsonUtil.toJson(cfgMap), testCaseCfgName);
            configurationPO.addLocalCfgPath(cfgPathListForClean, newCfgFile);
            FileUtil.deleteFile(unzipCfgFolder);
            boolean filePushed = configurationPO.pushConfigurationFile(newCfgFile, cfgFileFolder);
            newCfgFile.delete();
            Assert.assertTrue(filePushed, "Expect the new configuration file with increased firmware version been pushed to the plugged USB drive successful, but FAILURE.");
            TestRail.addStepName("Plug in this USB in another panel of the same type for version " + firmwareVersion + ".");

            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
            TestRail.addStepName("Select .CFG file and then click the Import button.");

            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_cfg_result_msg"), 10);
            final String IMPORT_ERROR_MESSAGE = "This file’s firmware version (" + newFirmwareVersion + ") does not match the firmware version of the panel (" + firmwareVersion + ").";
            String currentDisplayErrorMsg = ElementHelper.findElement(Locator.byResourceId("id/tv_ipt_cfg_result_msg")).getText();
            Assert.assertEquals(IMPORT_ERROR_MESSAGE, currentDisplayErrorMsg, "Expect the import error message \"" + IMPORT_ERROR_MESSAGE + "\"display, but NOT match.");
            TestRail.addStepName("Verify that it fails to import, and get an error message \"" + IMPORT_ERROR_MESSAGE + "\"");

            ElementHelper.click(Locator.byResourceId("id/close_button"));
            TestRail.addStepName("Click on the CLOSE button and the prompt box closes");
        } finally {
            // Delete the testing exported configuration file on the usb device and PC.
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /* C122601 Verify that "Lock the Settings, Panel Management, and Updates" option can be selected when exporting Configuration
     * 1. Click Settings > System > Import/Export Configuration->'EXPORT'
     * 2. Check the option 'Lock the Settings, Panel Management, and Updates' and save the configuration
     * 3. Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'
     * Author:Sita */
    @Test(groups = "P1")
    public void C122601LockTheSettingsPanelManagementAndUpdateCanBeSelectedWhenExportingConfiguration() throws InterruptedException, IOException {
        TestRail.setTestRailId("122601");
        TestRail.addStepName("Click Settings > System > Import/Export Configuration->'EXPORT'");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        TestRail.addStepName("Check the option 'Lock the Settings, Panel Management, and Updates' and save the configuration");
        try {
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(true, true, true);
            AppiumHelper.switchWindowToFullScreen();
            final String TEST_CASE_NAME = "C122601";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            String cfgFilePath = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);

            TestRail.addStepName("Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'");
            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
            AppiumHelper.showTaskbar();
            ElementHelper.clickWhenVisible(Locator.byText("Applications"));
            //Verifying that the Settings, Panel Management, and Update app don't appear in the locker
            Assert.assertFalse(AppiumHelper.isElementVisible(null, (settingApp), 10));
            Assert.assertFalse(AppiumHelper.isElementVisible(null, (panelManagementApp), 10));
            ElementHelper.clickWhenVisible(By.xpath("/hierarchy/android.widget.LinearLayout/android.widget.HorizontalScrollView/android.widget.LinearLayout/b.b.k.a.c[2]"));
            Assert.assertFalse(AppiumHelper.isElementVisible(null, (updateApp), 10));
        } finally {
            Runtime.getRuntime().exec("adb shell settings put global prome_lock_admin_access 0");
            // Delete the testing exported configuration file on the usb device and PC.
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /* C122602 Verify that "Lock the Settings, Panel Management, and Updates" option can be unchecked when exporting Configuration
     * 1. Click Settings > System > Import/Export Configuration->'EXPORT'
     * 2. Check the option "Lock the Settings, Panel Management, and Updates"
     * 3. uncheck the option "Lock the Settings, Panel Management, and Updates"
     * 4. save the configuration
     * 5. Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'
     * Author:Sita  */
    @Test(groups = "P1")
    public void C122602LockTheSettingsPanelManagementAndUpdateCanBeUncheckedWhenExportingConfiguration() throws InterruptedException {
        TestRail.setTestRailId("122602");
        TestRail.addStepName("Click Settings > System > Import/Export Configuration->'EXPORT'");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        TestRail.addStepName("Check the option Lock the Settings, Panel Management, and Updates");
        TestRail.addStepName("Uncheck the option Lock the Settings, Panel Management, and Updates");
        try {
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(true, true, false);
            AppiumHelper.switchWindowToFullScreen();
            final String TEST_CASE_NAME = "C122602";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            String cfgFilePath = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);

            TestRail.addStepName("Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'");
            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
            AppiumHelper.showTaskbar();
            ElementHelper.clickWhenVisible(Locator.byText("Applications"));
            //Verifying that Settings, Panel Management, and Update app are displayed in the locker and Network Settings button is displayed in the Network Icon Menu
            Assert.assertTrue(AppiumHelper.isElementVisible(null, (settingApp), 10));
            Assert.assertTrue(AppiumHelper.isElementVisible(null, (panelManagementApp), 10));
            ElementHelper.clickWhenVisible(By.xpath("/hierarchy/android.widget.LinearLayout/android.widget.HorizontalScrollView/android.widget.LinearLayout/b.b.k.a.c[2]"));
            Assert.assertTrue(AppiumHelper.isElementVisible(null, (updateApp), 10));
        } finally {
            // Delete the testing exported configuration file on the usb device and PC.
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
        }
    }


    /*C122608 Verify that Proximity Sensor On/Off setting is part of the export/import panel configurations
     * Steps
     * 1. Click Menu bar -> Application Locker -> Settings -> System -> Import/export configuration ->CONTINUE
     * 2.Check the Review settings list
     * Author:Sita */
    @Test(groups = "P1")
    public void C122608ProximitySensorOnOffSettingIsPartofExportImportPanelConfigurations() {
        TestRail.setTestRailId("122608");
        TestRail.addStepName("Click Menu bar -> Application Locker -> Settings -> System -> Import/export configuration ->CONTINUE");
        configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);
        ElementHelper.waitUntilVisible(By.id("com.prometheanworld.settingsconfiguration:id/export_button"), 10);
        AppiumHelper.findElementBy(By.id("com.prometheanworld.settingsconfiguration:id/export_button")).click();
        ElementHelper.click(continueButton);
        TestRail.addStepName("Check the Review settings list");
        AppiumHelper.swipeByElements(Locator.byText("Ethernet config"), Locator.byText("Wi-Fi(On/Off)"));
        AppiumHelper.swipeByElements(Locator.byText("Menu"), Locator.byText("Ethernet config"));
        AppiumHelper.swipeByElements(Locator.byText("Source detection"), Locator.byText("Menu"));
        systemPO.startAppFromUnifiedLauncher("Update");
        String system_info = ElementHelper.findElement(By.xpath("//*[@resource-id='com.prometheanworld.update:id/tv_system_info']")).getText();
        Log.info(system_info);
        systemPO.closeAppOnMenuBar("Update");
        if (system_info.contains("ActivPanel 9 Premium")) {
            Assert.assertTrue(AppiumHelper.isElementVisible(null, (Locator.byText("Proximity sensor")), 10));//Verifying Proximity Sensor is in the list of Review settings
        }
        AppiumHelper.findElementBy(By.id("com.prometheanworld.settingsconfiguration:id/cancel_button")).click();
    }

    /* C122610 Verify that Proximity sensor's on state can be exported successfully
     * Steps
     * 1. Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn on Proximity Sensor
     * 2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of  Proximity Sensor setting to USB
     * 3. Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn off Proximity Sensor
     * 4. Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'
     * 5. Check the status of Proximity Sensor from  'Settings -> Display -> Proximity Sensor'
     * Author:Sita*/
    @Test(groups = {"P1", "UserDebug"})
    public void C122610ProximitySensorOnStateCanBeExportedSuccessfully() throws InterruptedException {
        TestRail.setTestRailId("122610");

        TestRail.addStepName("Proximity Sensor only supports 9950,Check if the panel is 9950");
        systemPO.startAppFromUnifiedLauncher("Update");
        String system_info = ElementHelper.findElement(By.xpath("//*[@resource-id='com.prometheanworld.update:id/tv_system_info']")).getText();
        systemPO.closeAppOnMenuBar("Update");
        AssumeKt.assumeTrue(system_info.contains("ActivPanel 9 Premium"),"This panel is 5680, it is not support 'Proximity Sensor'. System info: "+system_info);

        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn on Proximity Sensor");
        AppiumHelper.findElementBy(Locator.byText("Display")).click();
        AppiumHelper.findElement("//*[@text='Advanced']").click();
        if (ElementHelper.getText(By.id("com.android.settings:id/switchWidget")).equals("OFF")) {
            AppiumHelper.findElementBy(By.id("com.android.settings:id/switchWidget")).click();}
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of  Proximity Sensor setting to USB");
        try {
            systemPO.startAppFromUnifiedLauncher("Settings");
            configurationPO.continueExportConfigurationToFilePicker();
            AppiumHelper.switchWindowToFullScreen();
            final String TEST_CASE_NAME = "C122610";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            String cfgFilePath = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn off Proximity Sensor");
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.switchWindowToFullScreen();
            AppiumHelper.findElementBy(Locator.byText("Display")).click();
            AppiumHelper.findElement("//*[@text='Advanced']").click();
            if (ElementHelper.getText(By.id("com.android.settings:id/switchWidget")).equals("ON")) {
                AppiumHelper.findElementBy(By.id("com.android.settings:id/switchWidget")).click();}
            ElementHelper.clickWhenVisible(By.id("android:id/close_window"));

            TestRail.addStepName("Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'");
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.switchWindowToFullScreen();
            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));

            TestRail.addStepName("Check the status of Proximity Sensor from  'Settings -> Display -> Proximity Sensor'");
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.findElementBy(Locator.byText("Display")).click();
            AppiumHelper.findElement("//*[@text='Advanced']").click();
            AppiumHelper.findElementBy(Locator.byText("Proximity sensor")).click();
            //Verifying that the status of three 3 switches on Proximity sensor is keep same with step1
            ElementHelper.isVisible(Locator.byText("ON"));
            ElementHelper.isVisible(Locator.byText("ON"));
            ElementHelper.isVisible(Locator.byText("OFF"));
            AppiumHelper.findElementBy(By.id("android:id/close_window")).click();
        } finally {
            // Delete the testing exported configuration file on the usb device and PC.
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /*C119834 Verify that users can import Settings configuration to same type, same version, different size panel
    1.Plug USB in one panel
    2.Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file to USB
    3.Plug in this USB in Same type, same version, different size panel
    4.Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> IMPORT
    5.Choose the saved cfg file in step 2
    6.Click the Import button to display the import progress bar
    7.Click the Reboot button to reboot panel
   Author:Sita */
    @Test(groups = {"P0", "UserDebug"})
    public void C119834UsersCanImportSettingsConfiguration() {
        TestRail.setTestRailId("119834");
        TestRail.addStepName("Plug USB in one panel");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file to USB");
        try {
            configurationPO.continueExportConfigurationToFilePicker();
            AppiumHelper.switchWindowToFullScreen();

            final String TEST_CASE_NAME = "C119834";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            String cfgFilePath = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            TestRail.addStepName("Plug in this USB in Same type, same version, different size panel");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);
            final String PANEL_SIZE = "panelSize";
            final String panelSize  = (String) cfgMap.get(PANEL_SIZE);
            FileUtil.deleteFile(unzipCfgFolder);
            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> IMPORT");
            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            TestRail.addStepName("Choose the saved cfg file in step 2");
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            TestRail.addStepName("Click the Import button to display the import progress bar");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
        } finally {
            // Delete the testing exported configuration file on the usb device and PC.
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
        }
        TestRail.addStepName("Click the Reboot button to reboot panel");
        AppiumHelper.rebootPanel();
    }

    /* C119844 Verify that users can import Standby and Sleep Timers to another panel
    1.Plugged in USB drive to panel #1
    2.Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 3 mins
    3.Android Settings -> System -> Import/export configuration
    4.Export .cfg file to USB drive
    5.Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 1 hour
    6.Plug this USB drive to panel #2, import this .cfg file
     */
//    @Test(groups = {"P0", "UserDebug"})
    public void C119844UsersCanImportStandby() {
        TestRail.setTestRailId("119844");
        TestRail.addStepName("Plugged in USB drive to panel #1");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        TestRail.addStepName("Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 3 mins");
        AppiumHelper.findElementBy(Locator.byText("Display")).click();
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        AppiumHelper.setSeekBar(0,standByTimer);
        AppiumHelper.setSeekBar(0,sleepTimer);
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        TestRail.addStepName("Export .cfg file to USB drive");
        try {
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.switchWindowToFullScreen();
            configurationPO.continueExportConfigurationToFilePicker();
            AppiumHelper.switchWindowToFullScreen();

            final String TEST_CASE_NAME = "C119844";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = "";
            String cfgFilePath = "";
            boolean cfgFileExist = false;
            for (String s : rwMedias) {
                cfgFileFolder = path_media_rw + s;
                cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
                boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
                if (exist) {
                    cfgFileExist = true;
                    break;
                }
            }
            Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);
            String cfgFileName = new File(cfgFilePath).getName();
            Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                            && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                    "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
            File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
            configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
            File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
            Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                    "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
            Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);
            final String FIRMWARE_VERSION = "firmwareVersion";
            Assert.assertTrue(cfgMap.containsKey(FIRMWARE_VERSION), "Expect the setting configuration file has \"firmwareVersion\", but NOT found.");
            final String firmwareVersion = (String) cfgMap.get(FIRMWARE_VERSION);
            FileUtil.deleteFile(unzipCfgFolder);
            TestRail.addStepName("Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 1 hour");
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.switchWindowToFullScreen();
            AppiumHelper.findElementBy(Locator.byText("Display")).click();
            ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
            AppiumHelper.setSeekBar(280,standByTimer);
            AppiumHelper.setSeekBar(280,sleepTimer);

            TestRail.addStepName("Plug this USB drive to panel #2, import this .cfg file");
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.switchWindowToFullScreen();
            configurationPO.navigateToImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            AppiumHelper.switchWindowToFullScreen();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            boolean foundCfgFile = filesPO.searchFileByNameAndClick(testCaseCfgName);
            Assert.assertTrue(foundCfgFile, "Expect found the specified configuration file in the file app, but NOT found.");
            ElementHelper.waitUntilVisible(Locator.byResourceId("id/tv_ipt_start"), 5);
            ElementHelper.click(Locator.byResourceId("id/tv_ipt_start"));
            //Confirming that standby and sleep timer are 3 mins in panel
            systemPO.startAppFromUnifiedLauncher("Settings");
            AppiumHelper.switchWindowToFullScreen();
            AppiumHelper.findElementBy(Locator.byText("Display")).click();
            ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        } finally {
            // Delete the testing exported configuration file on the usb device and PC.
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
        }
        TestRail.addStepName("Click the Reboot button to reboot panel");
        AppiumHelper.rebootPanel();
    }

    /**
     * C119804 Verify that other types files can't be selected when users try to import Settings configuration from no cfg files panel local storage
     * Steps
     *  1. No USB drive inserted and no cfg files in Downloads folder
     *  2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button
     *   => 1. Confirm that it will open the Files app, which by default is displayed on the Recent selection page
     *   => 2. Confirm that all files are grayed out and cannot be choosed
     * Author: DavidDing
     */
    @Test(groups = "P1")
    public void C119804VerifyThatOtherTypesFilesCanNotBeSelectedWhenUsersTryToImportSettingsConfigurationFromNoCfgFilesPanelLocalStorage() throws IOException {
        TestRail.setTestRailId("119804");

        setTestCaseCleaner(new Runnable() {
            @Override
            public void run() {
                configurationPO.forceStopFilPicker();
                configurationPO.forceStopSettingConfiguration();
            }
        });

        TestRail.addStepName("No USB drive inserted and no cfg files in Downloads folder");
        final String downloadFolderPath = "/sdcard/Download";
        boolean downloadFolderExist = configurationPO.checkPathExistOnPanel(downloadFolderPath);
        if (downloadFolderExist) {
            // Remove all unnecessary test configuration files under download folder to simulate no cfg files in Downloads folder.
            configurationPO.removeAllConfigurationFilesUnderPathOnPanel(downloadFolderPath);
        } else {
            configurationPO.createDirOnPanel(downloadFolderPath);
        }

        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button");
        configurationPO.continueImportConfigurationToFilePicker();
        AppiumHelper.switchWindowToFullScreen();

        TestRail.addStepName("Confirm that it will open the Files app, which by default is displayed on the Recent selection page");
        boolean filePickerRunning = configurationPO.isFilePickerRunning();
        Assert.assertTrue(filePickerRunning, "Expect the Files apps will opened, but is NOT opened");
        configurationPO.waitForFilePickerReady();

        TestRail.addStepName("Confirm that all files are grayed out and cannot be choosed");
        filesPO.selectDownloadFiles();
        filesPO.searchFileByName("*.cfg");
        String filePickerDisplayMsg = filesPO.getDisplayMessage();
        Assert.assertTrue(filePickerDisplayMsg != null && "No matches in Files".equals(filePickerDisplayMsg),
                "Expect none configuration file can be chosen, but is NOT.");
    }

    /**
     * C119805 Verify that users can import .cfg type Settings configuration file from panel local storage
     * Steps
     *  1. No USB drive inserted, panel local storage has one or more .CFG files
     *  2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button
     *   => Confirm that it will open the Files app, all other file types are disabled except CFG format files
     *  3. Choose and click one .cfg file, pops up a confirmation dialog with two options: Import and Cancel
     *  4. Click "IMPORT" button
     *   => Confirm that import success
     * Author: DavidDing
     */
    @Test(groups = "P1")
    public void C119805VerifyThatUsersCanImportCfgTypeSettingsConfigurationFileFromPanelLocalStorage() {
        TestRail.setTestRailId("119805");

        String testCfgFilePath = null;
        boolean panelHasBeenReboot = false;
        try {
            TestRail.addStepName("No USB drive inserted, panel local storage has one or more .CFG files");
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
            AppiumHelper.switchWindowToFullScreen();
            final String TEST_CASE_NAME = "C119805";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;
            filesPO.selectDownloadFiles();
            // We export the configuration file to the panel local storage to simulate there has one or more .CFG files.
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
            final String exportedFilePath = "/sdcard/Download/" + testCaseCfgName;
            final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(exportedFilePath);
            Assert.assertTrue(exportedSuccessful,
                    "Expect the configuration has been exported to local storage successfully, but is NOT");
            testCfgFilePath = exportedFilePath;

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button");
            configurationPO.clickImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();

            TestRail.addStepName("Confirm that it will open the Files app, all other file types are disabled except CFG format files");
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            final boolean filePickerRunning = configurationPO.isFilePickerRunning();
            Assert.assertTrue(filePickerRunning, "Expect the Files apps will opened, but is NOT opened");
            filesPO.selectDownloadFiles();
            filesPO.switchToMenuList();
            filesPO.searchFileByName(testCaseCfgName);
            String cfgItemPath = "//*[contains(@text,'" + testCaseCfgName + "') and @resource-id='android:id/title']";
            ElementHelper.waitUntilVisible(By.xpath(cfgItemPath), 5);
            AndroidElement dirList = (AndroidElement) ElementHelper.findElement(By.xpath("//*[@resource-id='com.android.documentsui:id/dir_list']"));
            AndroidElement matchedListItem = (AndroidElement) dirList.findElementByXPath(cfgItemPath);
            Assert.assertTrue(matchedListItem.isDisplayed(), "Expect the CFG format file is displayed, but is NOT");

            TestRail.addStepName("Choose and click one .cfg file, pops up a confirmation dialog with two options: Import and Cancel");
            matchedListItem.click();
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/tv_ipt_msg_confirm"));
            AndroidElement textViewCancel = (AndroidElement) ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/tv_ipt_cancel"));
            Assert.assertTrue(textViewCancel.getAttribute("text").equals("CANCEL"),
                    "Expect the confirmation dialog with option: 'Cancel', but is NOT");
            AndroidElement textViewImport = (AndroidElement) ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/tv_ipt_start"));
            Assert.assertTrue(textViewImport.getAttribute("text").equals("IMPORT"),
                    "Expect the confirmation dialog with option: 'Import', but is NOT");

            TestRail.addStepName("Click \"IMPORT\" button");
            textViewImport.click();

            TestRail.addStepName("Confirm that import success");
            By contentFragmentPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/content_fragment");
            ElementHelper.waitUntilVisible(contentFragmentPath, 30);
            AndroidElement textImportSuccessful = (AndroidElement) ElementHelper.findElement(Locator.byText("Import successful"));
            Assert.assertTrue(textImportSuccessful != null && textImportSuccessful.isDisplayed(),
                    "Confirm the import process was successful and with a message, but is NOT");

            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
                testCfgFilePath = null;
            }
            // Click the 'REBOOT' button to reboot the panel so that we can apply the imported configuration.
            ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/reboot_button")).click();
            panelHasBeenReboot = true;
            // We need to make sure that the panel has rebooted successfully and reconnect the ADB reconnection.
            AppiumHelper.checkAndConnectDevice();
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("C119805_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            if (!panelHasBeenReboot) {
                configurationPO.forceStopFilPicker();
                configurationPO.forceStopSettingConfiguration();
            }
        }
    }

    /**
     * C119806 Verify that other types files can't be selected when users try to import Settings configuration from no cfg files USB folder
     * Steps
     *  1. Insert USB drive, there is no .CFG file on the USB flash drive.
     *  2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button.
     *   => Confirm that it will open the Files app, which by default is displayed on the Recent selection page.
     *  3. Choose USB folder.
     *   => Confirm that all files are grayed and disabled.
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119806VerifyThatOtherTypesFilesCanNotBeSelectedWhenUsersTryToImportSettingsConfigurationFromNoCfgFilesUSBFolder() {
        TestRail.setTestRailId("119806");

        String cfgFileFolder = null;
        try {
            TestRail.addStepName("Insert USB drive, there is no .CFG file on the USB flash drive.");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            final String cfgFileFolderName = "C119806";
            cfgFileFolder = path_media_rw + rwMedias.get(0) + "/" + cfgFileFolderName;
            // We create a new empty folder in the USB drive to simulate the scenario that no .CFG file in the USB drive.
            configurationPO.createDirOnPanel(cfgFileFolder);

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration, click 'IMPORT' button.");
            configurationPO.continueImportConfigurationToFilePicker();
            AppiumHelper.switchWindowToFullScreen();

            TestRail.addStepName("Confirm that it will open the Files app, which by default is displayed on the Recent selection page.");
            boolean filePickerRunning = configurationPO.isFilePickerRunning();
            Assert.assertTrue(filePickerRunning, "Expect the Files apps will opened, but is NOT opened");
            configurationPO.waitForFilePickerReady();

            TestRail.addStepName("Choose USB folder.");
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();

            TestRail.addStepName("Confirm that all files are grayed and disabled.");
            filesPO.searchFileByNameAndClick(cfgFileFolderName);
            // Wait for the directory get clicked and enter.
            AppiumHelper.waitForSeconds(2);
            filesPO.searchFileByName("*.cfg");
            String filePickerDisplayMsg = filesPO.getDisplayMessage();
            Assert.assertTrue(filePickerDisplayMsg != null && filePickerDisplayMsg.contains("No matches in"),
                    "Expect none configuration file can be chosen, but is NOT.");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("C119806_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (cfgFileFolder != null && !cfgFileFolder.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + cfgFileFolder);
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /**
     * C120776 Verify that unchecked options on the “Select your export options” page can't appear in the export settings list
     * Steps
     *  1. Android Settings -> System -> Import/export configuration
     *  2. Uncheck all options
     *  3. Click "CONTINUE" button to display the list of settings to be exported, scroll down to the end
     *   => Confirm that unchecked options on the “Select your export options” page can't appear in the export settings list
     *  4. Click on Previous button
     *   => Back to the previous page
     *  5. Check all options
     *  6. Click "CONTINUE" button to display the list of settings to be exported, scroll down to the end
     *   => Confirm that unchecked options on the “Select your export options” page can appear in the export settings list
     * Author: DavidDing
     */
    @Test(groups = "P2")
    public void C120776VerifyThatUncheckedOptionsOnTheSelectYourExportOptionsPageCaNotAppearInTheExportSettingsList() {
        TestRail.setTestRailId("120776");

        setTestCaseCleaner(new Runnable() {
            @Override
            public void run() {
                configurationPO.forceStopSettingConfiguration();
            }
        });

        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);

        TestRail.addStepName("Uncheck all options");
        By exportBtnPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_button");
        ElementHelper.waitUntilVisible(exportBtnPath, 5);
        ElementHelper.findElement(exportBtnPath).click();
        By continueBtnPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/continue_button");
        ElementHelper.waitUntilVisible(continueBtnPath, 5);
        configurationPO.dispatchCheckBox("network_checkBox", false);
        configurationPO.dispatchCheckBox("wallerpaper_checkBox", false);
        configurationPO.dispatchCheckBox("lock_checkBox", false);

        TestRail.addStepName("Click \"CONTINUE\" button to display the list of settings to be exported, scroll down to the end");
        ElementHelper.findElement(continueBtnPath).click();
        By exportListViewPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_listView");
        ElementHelper.waitUntilVisible(exportListViewPath, 3);
        AndroidElement listView = (AndroidElement) ElementHelper.findElement(exportListViewPath);
        configurationPO.scrollViewToBottom(listView);

        TestRail.addStepName("Confirm that unchecked options on the “Select your export options” page can't appear in the export settings list");
        AndroidElement textViewSavedNetworks = configurationPO.findElementByTextSafely("Saved networks");
        Assert.assertTrue(textViewSavedNetworks == null || !textViewSavedNetworks.isDisplayed(),
                "Expect the unchecked option \"Saved networks\" doesn't appear in the export settings list, but is NOT");
        AndroidElement textViewWallpaper = configurationPO.findElementByTextSafely("Wallpaper");
        Assert.assertTrue(textViewWallpaper == null || !textViewWallpaper.isDisplayed(),
                "Expect the unchecked option \"Wallpaper\" doesn't appear in the export settings list, but is NOT");
        AndroidElement textViewLockOption = configurationPO.findElementByTextSafely("Lock option");
        Assert.assertTrue(textViewLockOption == null || !textViewLockOption.isDisplayed(),
                "Expect the unchecked option \"Lock option\" doesn't appear in the export settings list, but is NOT");

        TestRail.addStepName("Click on Previous button");
        ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/previous_button")).click();

        TestRail.addStepName("Back to the previous page");
        ElementHelper.waitUntilVisible(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/network_checkBox"), 3);
        AndroidElement textViewSelectExportOptions = configurationPO.findElementByTextSafely("Select your export options:");
        /**
         * If the hint "Select your export options:" is displayed on the page,
         * we can confirm that we are back to the export options select page.
         */
        Assert.assertTrue(textViewSelectExportOptions != null && textViewSelectExportOptions.isDisplayed(),
                "Expect the hit \"Select your export options:\" is displayed on the page, but is NOT");

        TestRail.addStepName("Check all options");
        configurationPO.dispatchCheckBox("network_checkBox", true);
        configurationPO.dispatchCheckBox("wallerpaper_checkBox", true);
        configurationPO.dispatchCheckBox("lock_checkBox", true);

        TestRail.addStepName("Click \"CONTINUE\" button to display the list of settings to be exported, scroll down to the end");
        ElementHelper.findElement(continueBtnPath).click();
        exportListViewPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_listView");
        ElementHelper.waitUntilVisible(exportListViewPath, 3);
        listView = (AndroidElement) ElementHelper.findElement(exportListViewPath);
        configurationPO.scrollViewToBottom(listView);

        TestRail.addStepName("Confirm that unchecked options on the “Select your export options” page can appear in the export settings list");
        textViewSavedNetworks = configurationPO.findElementByTextSafely("Saved networks");
        Assert.assertTrue(textViewSavedNetworks != null && textViewSavedNetworks.isDisplayed(),
                "Expect the unchecked option \"Saved networks\" to appear in the export settings list, but is NOT");
        textViewWallpaper = configurationPO.findElementByTextSafely("Wallpaper");
        Assert.assertTrue(textViewWallpaper != null && textViewWallpaper.isDisplayed(),
                "Expect the unchecked option \"Wallpaper\" to appear in the export settings list, but is NOT");
        textViewLockOption = configurationPO.findElementByTextSafely("Lock option");
        Assert.assertTrue(textViewLockOption != null && textViewLockOption.isDisplayed(),
                "Expect the unchecked option \"Lock option\" to appear in the export settings list, but is NOT");
    }

    /**
     * C119783 Verify that nothing occurs when users click on “Cancel” button in Import/export configuration
     * Steps:
     *  1. Plugged in an USB stick
     *  2. Android Settings -> System -> Import/export configuration
     *  3. Click and pop up a dialog
     *  4. Click "CANCEL" button => Confirm that nothing occurs
     * Author: Yifeng Wu
     *
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119783VerifyThatNothingOccursWhenUsersClickOnCancelButtonInImportExportConfiguration() {
        TestRail.setTestRailId("119783");

        setTestCaseCleaner(() -> {
            configurationPO.forceStopSettingConfiguration();
        });

        TestRail.addStepName("Plugged in an USB stick");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        AssumeKt.assumeTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        TestRail.addStepName("Click and pop up a dialog");
        configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/cancel_button")),
                "The dialog 'Import/export configuration' should be visible");
        TestRail.addStepName("Click \"CANCEL\" button");
        ElementHelper.click(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/cancel_button"));
        Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/cancel_button")),
                "The dialog 'Import/export configuration' should not be visible");
        Assert.assertTrue(ElementHelper.isVisible(By.xpath("//*[@text='Import/export configuration' and @resource-id='android:id/title']")),
                "The Settings app should be focused");
    }

    /**
     * C119803 Verify that Settings configuration file with a long string with many characters can be saved
     * Steps
     *  1. Plugged in USB drive to the panel
     *  2. Android Settings -> System -> Import/export configuration
     *  3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     *  4. Click "CONTINUE" button to display the list of settings to be exported
     *  5. Click "CONTINUE" button and the default Files app opens up, choose USB folder
     *  6. Enter a long string with many characters, click "EXPORT" button
     *  7. Pops up a dialog with "Export successful" and click Close button to close it
     *  8. Menu bar -> Application Locker -> Files app
     *   => Confirm that the .cfg file with "..." is saved to the USB drive
     * Author: DavidDing
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119803VerifyThatSettingsConfigurationFileWithALongStringWithManyCharactersCanBeSaved() {
        TestRail.setTestRailId("119803");

        String testCfgFilePath = null;
        try {
            TestRail.addStepName("Plugged in USB drive to the panel");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";

            TestRail.addStepName("Android Settings -> System -> Import/export configuration");
            configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);

            TestRail.addStepName("Click and pop up a dialog, choose \"EXPORT\" and jump to the export dialog");
            By exportButtonPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_button");
            ElementHelper.waitUntilVisible(exportButtonPath, 3);
            ElementHelper.findElement(exportButtonPath).click();

            TestRail.addStepName("Click \"CONTINUE\" button to display the list of settings to be exported");
            ElementHelper.waitUntilVisible(continueButton, 3);
            ElementHelper.findElement(continueButton).click();

            TestRail.addStepName("Click \"CONTINUE\" button and the default Files app opens up, choose USB folder");
            ElementHelper.waitUntilVisible(continueButton, 3);
            ElementHelper.findElement(continueButton).click();
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();

            TestRail.addStepName("Enter a long string with many characters, click \"EXPORT\" button");
            final String testCaseCfgNameWithManyCharacters = "C119803_the_quick_brown_fox_jumps_over_a_lazy_dog" +
                    "_THE_QUICK_BROWN_FOX_JUMPS_OVER_A_LAZY_DOG" +
                    "_the_quick_brown_fox_jumps_over_a_lazy_dog" +
                    "_THE_QUICK_BROWN_FOX_JUMPS_OVER_A_LAZY_DOG" +
                    ".cfg";
            configurationPO.exportConfigurationWithSpecifiedNameAndClickSaveButton(testCaseCfgNameWithManyCharacters);

            TestRail.addStepName("Pops up a dialog with \"Export successful\" and click Close button to close it");
            By closeBtnPath = By.xpath("//*[@text='CLOSE' and @resource-id='com.prometheanworld.settingsconfiguration:id/close_button']");
            ElementHelper.waitUntilVisible(closeBtnPath, 15);
            AndroidElement textViewExportSuccessful = (AndroidElement) ElementHelper.findElement(Locator.byText("Export successful"));
            Assert.assertTrue(textViewExportSuccessful != null && textViewExportSuccessful.isDisplayed(),
                    "Expect the message \"Export successful\" is displayed on the page, but is NOT");
            ElementHelper.findElement(closeBtnPath).click();
            testCfgFilePath = cfgFileFolder + testCaseCfgNameWithManyCharacters;

            TestRail.addStepName("Menu bar -> Application Locker -> Files app");
            systemPO.startAppFromUnifiedLauncher("Files");

            TestRail.addStepName("Confirm that the .cfg file with \"...\" is saved to the USB drive");
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            filesPO.searchFileByName(testCaseCfgNameWithManyCharacters);
            String cfgItemPath = "//*[contains(@text,'" + testCaseCfgNameWithManyCharacters + "') and @resource-id='android:id/title']";
            ElementHelper.waitUntilVisible(By.xpath(cfgItemPath), 5);
            AndroidElement dirList = (AndroidElement) ElementHelper.findElement(By.xpath("//*[@resource-id='com.android.documentsui:id/dir_list']"));
            AndroidElement matchedListItem = (AndroidElement) dirList.findElementByXPath(cfgItemPath);
            Assert.assertTrue(matchedListItem.isDisplayed(), "Expect the specified CFG file is displayed, but is NOT");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("C119803_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /**
     * C119793 Verify that users can import Lock Apps setting to another pannel
     * 1. Plugged in USB drive to panel #1
     * 2. Android Settings -> System -> Import/export configuration
     * 3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     * 4. Check only the "Lock Settings, Panel Management, and Update app" option, export .cfg file to USB drive
     * 5. Plug this USB drive to panel #2 of the same class and firmware version, import this .cfg file
     *  => Confirm that those apps in panel #2 are hidden in Locker and no other apps are hidden
     * 6. Menu bar -> Application Locker -> Locker Settings -> Volume, reduce the volume down to zero
     * 7. Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down
     * 8. Pops up a unlock confirmation message with two options: Yes and Cancel, select "YES"
     *  => Confirm that these 3 apps are visible in Locker and work properly
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119793VerifyThatUsersCanImportLockAppsSettingToAnotherPanel() {
        TestRail.setTestRailId("119793");

        needUnlock = true;
        String testCfgFilePath = null;
        boolean panelHasBeenReboot = false;
        try {
            TestRail.addStepName("Plugged in USB drive to panel #1");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";

            TestRail.addStepName("Android Settings -> System -> Import/export configuration");
            configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);

            TestRail.addStepName("Click and pop up a dialog, choose \"EXPORT\" and jump to the export dialog");
            By exportBtnPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_button");
            ElementHelper.waitUntilVisible(exportBtnPath, 5);
            ElementHelper.findElement(exportBtnPath).click();

            TestRail.addStepName("Check only the \"Lock Settings, Panel Management, and Update app\" option, export .cfg file to USB drive");
            By continueBtnPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/continue_button");
            ElementHelper.waitUntilVisible(continueBtnPath, 5);
            configurationPO.dispatchCheckBox("network_checkBox", false);
            configurationPO.dispatchCheckBox("wallerpaper_checkBox", false);
            configurationPO.dispatchCheckBox("lock_checkBox", true);
            ElementHelper.click(continueBtnPath);
            ElementHelper.waitUntilVisible(continueBtnPath, 5);
            ElementHelper.click(continueBtnPath);
            AppiumHelper.switchWindowToFullScreen();
            final String TEST_CASE_NAME = "C119793";
            final String CONFIGURATION_FILE_POSTFIX = ".cfg";
            final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
            testCfgFilePath = cfgFileFolder + testCaseCfgName;
            final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(testCfgFilePath);
            Assert.assertTrue(exportedSuccessful,
                    "Expect the configuration has been exported to the USB drive, but is NOT");

            TestRail.addStepName("Plug this USB drive to panel #2 of the same class and firmware version, import this .cfg file");
            // We considered importing on the same panel to simulate another panel that has the same class and firmware version.
            configurationPO.clickImportExportConfiguration();
            configurationPO.chooseImportToImportDialog();
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            filesPO.searchFileByNameAndClick(testCaseCfgName);
            configurationPO.confirmImportAndWaitForFinished();
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
                testCfgFilePath = null;
            }
            panelHasBeenReboot = true;
            AppiumHelper.rebootPanel();

            TestRail.addStepName("Confirm that those apps in panel #2 are hidden in Locker and no other apps are hidden");
            AppiumHelper.exeAdbRoot();
            String lockAdminAccess = Adb.getGlobalSettings(UserAccess.SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
            Assert.assertTrue("1".equals(lockAdminAccess),"Expect the admin access is lock, but is NOT");
            final Set<String> appsListForAdminAccessCheck = new HashSet();
            appsListForAdminAccessCheck.add("Update");
            appsListForAdminAccessCheck.add("Settings");
            appsListForAdminAccessCheck.add("Panel Management");
            systemPO.startLocker();
            Assert.assertFalse(configurationPO.foundAppsInLocker(systemPO, appsListForAdminAccessCheck),
                    "Confirm that \"Update\", \"Settings\", and \"Panel Management\" are hidden in Locker, but is NOT");

            TestRail.addStepName("Menu bar -> Application Locker -> Locker Settings -> Volume, reduce the volume down to zero");
            AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.RemoteControl);
            By volumeBarPath = Locator.byResourceId("com.prometheanworld.avisettings:id/volume_bar");
            ElementHelper.waitUntilVisible(volumeBarPath, 3);
            AndroidElement volumeBar = (AndroidElement) ElementHelper.findElement(volumeBarPath);
            volumeBar.click();
            final Float fromValue = Float.parseFloat(volumeBar.getAttribute("text"));
            ElementHelper.scrollProgressBar(volumeBar, fromValue, 0.0f, 100.0f);

            TestRail.addStepName("Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down");
            configurationPO.pressKonamiCode();

            TestRail.addStepName("Pops up a unlock confirmation message with two options: Yes and Cancel, select \"YES\"");
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.prometheanworld.panelbuttonservice:id/header"), 5);
            ElementHelper.findElement(By.xpath("//*[@text='UNLOCK ACCESS' and @resource-id='com.prometheanworld.panelbuttonservice:id/primary_button']")).click();

            TestRail.addStepName("Confirm that these 3 apps are visible in Locker and work properly");
            lockAdminAccess = Adb.getGlobalSettings(UserAccess.SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS);
            Assert.assertTrue("0".equals(lockAdminAccess),"Expect the admin access is unlock, but is NOT");
            systemPO.startLocker();
            Assert.assertTrue(configurationPO.foundAppsInLocker(systemPO, appsListForAdminAccessCheck),
                    "Confirm that \"Update\", \"Settings\", and \"Panel Management\" are visible in Locker, but is NOT");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot("C119793_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            if (!panelHasBeenReboot) {
                configurationPO.forceStopFilPicker();
                configurationPO.forceStopSettingConfiguration();
            }
            Adb.setGlobalSettings(UserAccess.SETTINGS_GLOBAL_ADMIN_ACCESS_LOCK_STATUS, "0");
        }
    }

    /**
     * C119800 Verify that overwritten Settings configuration file can work on another panel
     * Steps:
     * 1.Plug USB in one panel
     * 2.Verify at least one USB drive has been plugged.
     * 3.Android Settings -> System -> Import/export configuration
     * 4.exported configuration file which named C119800.cfg -> check the C119800.cfg is produced in usb
     * 5.exported configuration file as same name C119800.cfg again and check it will produced a new file C119800 (1).cfg
     * 6.import this C119800 (1).cfg file
     * 7.check it can be imported successful
     * Author: guangxi.chen
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119800VerifyThatOverwrittenSettingsConfigurationFileCanWorkOnAnotherPanel() {
        TestRail.setTestRailId("119800");
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        setTestCaseCleaner(() -> {
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
            configurationPO.forceStopFilPicker();
        });
        TestRail.addStepName("Plug USB in one panel");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.continueExportConfigurationToFilePicker();
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("exported configuration file which named C119800.cfg");
        final String TEST_CASE_NAME = "C119800";
        final String CONFIGURATION_FILE_POSTFIX = ".cfg";
        final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        TestRail.addStepName("check the C119800.cfg is produced in usb");
        final String path_media_rw = "/mnt/media_rw/";
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        boolean cfgFileExist = false;
        String cfgRootPath = "";
        String cfgFileFolder = "";
        for (String s : rwMedias) {
            cfgFileFolder = path_media_rw + s;
            boolean exist = configurationPO.checkConfigurationFileExist(cfgFileFolder + "/" + testCaseCfgName);
            if (exist) {
                cfgFileExist = true;
                cfgRootPath = cfgFileFolder;
                break;
            }
        }
        if (cfgFileExist) {
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgRootPath + "/" + testCaseCfgName);
        }
        Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
        TestRail.addStepName("exported configuration file as same name again and check it will produced a new file C119800 (1).cfg");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.continueExportConfigurationToFilePicker();
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        final String recoverCfgName = "C119800 (1).cfg";
        AppiumHelper.openAppsFromTaskBar("Applications"); //Open Locker
        systemPO.scrollAndClickApp("Files");
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();
        boolean foundRecoverCfgFile = filesPO.searchFileByNameAndClick(recoverCfgName);
        Assert.assertTrue(foundRecoverCfgFile, "Expect the recover configuration file has been exported to one USB drive that has been plugged, but NOT found.");
        systemPO.closeAppOnMenuBar("Files");
        configurationPO.addPanelCfgPath(cfgPathListForClean, cfgRootPath + "/" + recoverCfgName);
        TestRail.addStepName("import this C119800 (1).cfg file");
        ElementHelper.click(Locator.byText("Import/export configuration"));
        configurationPO.chooseImportToImportDialog();
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();
        filesPO.searchFileByNameAndClick(recoverCfgName);
        final String btnImportPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/tv_ipt_start']";
        ElementHelper.waitUntilVisible(By.xpath(btnImportPath), 5);
        Driver.getAndroidDriver().findElementByXPath(btnImportPath).click();
        TestRail.addStepName("check it can be imported successful");
        Assert.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Import successful"), 40));
        AppiumHelper.rebootPanel();
    }

    /**
     * C122610 Verify that Proximity sensor's on state can be exported successfully
     * Steps
     * 1. Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn on Proximity Sensor
     *  => Proximity Sensor can be turned on
     * 2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of  Proximity Sensor setting to USB
     *  => The configuration can be exported successfully
     * 3. Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn off Proximity Sensor
     *  => Proximity Sensor is turned off
     * 4. Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'
     *  => The configuration can be imported successfully
     * 5. Check the status of Proximity Sensor from  'Settings -> Display -> Proximity Sensor'
     *  => Proximity Sensor is turnned on,and the status of three 3 switches on Proximity sensor is keep same with step1
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C122610VerifyThatProximitySensorOnStateCanBeExportedSuccessfully() {
        TestRail.setTestRailId("122610");

        AssumeKt.assumeTrue(SubSystem.isMT9950(), "Current device is NOT MT9950, skip the test");
        final String TEST_CASE_NAME = "C122610";
        Boolean originalProximitySensorState = null;
        String testCfgFilePath = null;
        try {
            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn on Proximity Sensor");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AndroidElement proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();

            TestRail.addStepName("Proximity Sensor can be turned on");
            if ("OFF".equals(proximitySensorSwitcher.getAttribute("text"))) {
                originalProximitySensorState = false;
                proximitySensorSwitcher.click();
                Assert.assertTrue("ON".equals(proximitySensorSwitcher.getAttribute("text")),
                        "Expect the switcher of proximity sensor has been turn ON, but is OFF.");
            } else if ("ON".equals(proximitySensorSwitcher.getAttribute("text"))) {
                originalProximitySensorState = true;
            }
            By proximitySensorSettingItemPath = By.xpath("//*[@text='Proximity sensor' and @resource-id='android:id/title']");
            ElementHelper.click(proximitySensorSettingItemPath);
            AppiumHelper.waitForSeconds(1);
            Boolean resetStandbyAndSleepTimerSwitchState = proximitySensorPO.getResetStandbyAndSleepTimerSwitchState();
            Boolean illuminateScreenSwitchState = proximitySensorPO.getIlluminateScreenSwitchState();
            configurationPO.navigateUp();
            configurationPO.navigateUp();

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of  Proximity Sensor setting to USB");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
            final String testCaseCfgName = TEST_CASE_NAME + ".cfg";
            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            TestRail.addStepName("The configuration can be exported successfully");
            final String exportedFilePath = cfgFileFolder + testCaseCfgName;
            final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(exportedFilePath);
            Assert.assertTrue(exportedSuccessful,
                    "Expect the configuration has been exported to the usb device successfully, but is NOT");
            testCfgFilePath = exportedFilePath;

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn off Proximity Sensor");
            configurationPO.navigateUp();
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();
            if ("ON".equals(proximitySensorSwitcher.getAttribute("text"))) {
                TestRail.addStepName("Proximity Sensor is turned off");
                proximitySensorSwitcher.click();
                Assert.assertTrue("OFF".equals(proximitySensorSwitcher.getAttribute("text")),
                        "Expect the switcher of proximity sensor has been turn OFF, but is ON.");
            }

            TestRail.addStepName("Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'");
            configurationPO.navigateUp();
            configurationPO.continueImportConfigurationToFilePicker();
            configurationPO.waitForFilePickerReady();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            filesPO.searchFileByNameAndClick(testCaseCfgName);
            configurationPO.confirmImportAndWaitForFinished();

            TestRail.addStepName("The configuration can be imported successfully");
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
                testCfgFilePath = null;
            }
            AppiumHelper.rebootPanel();

            TestRail.addStepName("Check the status of Proximity Sensor from  'Settings -> Display -> Proximity Sensor'");
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();

            TestRail.addStepName("Proximity Sensor is turnned on,and the status of three 3 switches on Proximity sensor is keep same with step1");
            Assert.assertTrue("ON".equals(proximitySensorSwitcher.getAttribute("text")),
                    "Except the proximity sensor is turnned on, but is OFF");
            ElementHelper.click(proximitySensorSettingItemPath);
            AppiumHelper.waitForSeconds(1);
            Assert.assertEquals(proximitySensorPO.getResetStandbyAndSleepTimerSwitchState(), resetStandbyAndSleepTimerSwitchState,
                    "Except the reset standby and sleep timer switch's switch is same as step1, but is NOT");
            Assert.assertEquals(proximitySensorPO.getIlluminateScreenSwitchState(), illuminateScreenSwitchState,
                    "Except the illuminate screen when motion is detected switch's switch is same as step1, but is NOT");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot(TEST_CASE_NAME + "_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
            // Restore the panel's proximity sensor state.
            if (originalProximitySensorState != null) {
                systemPO.startAppFromUnifiedLauncher(testAppName());
                CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
                AndroidElement proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();
                if ((originalProximitySensorState && "OFF".equals(proximitySensorSwitcher.getAttribute("text")))
                        || (!originalProximitySensorState && "ON".equals(proximitySensorSwitcher.getAttribute("text")))) {
                    proximitySensorSwitcher.click();
                }
                configurationPO.forceStopSettingConfiguration();
            }
        }
    }

    /**
     * C122609 Verify that Proximity sensor's off state can be exported successfully
     * Steps
     * 1. Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn off Proximity Sensor
     *  => Proximity Sensor can be turned off
     * 2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of  Proximity Sensor setting to USB
     *  => The configuration can be exported successfully
     * 3. Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn on Proximity Sensor
     *  => Proximity Sensor is turned on
     * 4. Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'
     *  => The configuration can be imported successfully
     * 5. Check the status of Proximity Sensor from  'Settings -> Display -> Proximity Sensor'
     *  => Proximity Sensor is turnned off, and the status of three 3 switches on Proximity sensor is keep same with step1
     * Author: DavidDing
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C122609VerifyThatProximitySensorOffStateCanBeExportedSuccessfully() {
        TestRail.setTestRailId("122609");

        AssumeKt.assumeTrue(SubSystem.isMT9950(), "Current device is NOT MT9950, skip the test");
        final String TEST_CASE_NAME = "C122609";
        Boolean originalProximitySensorState = null;
        String testCfgFilePath = null;
        try {
            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn off Proximity Sensor");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AndroidElement proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();

            TestRail.addStepName("Proximity Sensor can be turned off");
            if ("ON".equals(proximitySensorSwitcher.getAttribute("text"))) {
                originalProximitySensorState = true;
                proximitySensorSwitcher.click();
                Assert.assertTrue("OFF".equals(proximitySensorSwitcher.getAttribute("text")),
                        "Expect the switcher of proximity sensor has been turn OFF, but is ON.");
            } else if ("OFF".equals(proximitySensorSwitcher.getAttribute("text"))) {
                originalProximitySensorState = false;
            }
            By proximitySensorSettingItemPath = By.xpath("//*[@text='Proximity sensor' and @resource-id='android:id/title']");
            ElementHelper.click(proximitySensorSettingItemPath);
            AppiumHelper.waitForSeconds(1);
            Boolean resetStandbyAndSleepTimerSwitchState = proximitySensorPO.getResetStandbyAndSleepTimerSwitchState();
            Boolean illuminateScreenSwitchState = proximitySensorPO.getIlluminateScreenSwitchState();
            configurationPO.navigateUp();
            configurationPO.navigateUp();

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of  Proximity Sensor setting to USB");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
            final String testCaseCfgName = TEST_CASE_NAME + ".cfg";
            filesPO.selectStorage("usb");
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);

            TestRail.addStepName("The configuration can be exported successfully");
            final String exportedFilePath = cfgFileFolder + testCaseCfgName;
            final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(exportedFilePath);
            Assert.assertTrue(exportedSuccessful,
                    "Expect the configuration has been exported to the usb device successfully, but is NOT");
            testCfgFilePath = exportedFilePath;

            TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Display -> Proximity Sensor, turn on Proximity Sensor");
            configurationPO.navigateUp();
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();
            if ("OFF".equals(proximitySensorSwitcher.getAttribute("text"))) {
                TestRail.addStepName("Proximity Sensor is turned on");
                proximitySensorSwitcher.click();
                Assert.assertTrue("ON".equals(proximitySensorSwitcher.getAttribute("text")),
                        "Expect the switcher of proximity sensor has been turn ON, but is OFF.");
            }

            TestRail.addStepName("Reimport the configuration which exported at step 2 by clicking Settings > System > Import/Export Configuration->'IMPORT'");
            configurationPO.navigateUp();
            configurationPO.continueImportConfigurationToFilePicker();
            configurationPO.waitForFilePickerReady();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            filesPO.searchFileByNameAndClick(testCaseCfgName);
            configurationPO.confirmImportAndWaitForFinished();

            TestRail.addStepName("The configuration can be imported successfully");
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
                testCfgFilePath = null;
            }
            AppiumHelper.rebootPanel();

            TestRail.addStepName("Check the status of Proximity Sensor from  'Settings -> Display -> Proximity Sensor'");
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();

            TestRail.addStepName("Proximity Sensor is turnned off,and the status of three 3 switches on Proximity sensor is keep same with step1");
            Assert.assertTrue("OFF".equals(proximitySensorSwitcher.getAttribute("text")),
                    "Except the proximity sensor is turnned OFF, but is ON");
            ElementHelper.click(proximitySensorSettingItemPath);
            AppiumHelper.waitForSeconds(1);
            Assert.assertEquals(proximitySensorPO.getResetStandbyAndSleepTimerSwitchState(), resetStandbyAndSleepTimerSwitchState,
                    "Except the reset standby and sleep timer switch's switch is same as step1, but is NOT");
            Assert.assertEquals(proximitySensorPO.getIlluminateScreenSwitchState(), illuminateScreenSwitchState,
                    "Except the illuminate screen when motion is detected switch's switch is same as step1, but is NOT");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot(TEST_CASE_NAME + "_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
            // Restore the panel's proximity sensor state.
            if (originalProximitySensorState != null) {
                systemPO.startAppFromUnifiedLauncher(testAppName());
                CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
                AndroidElement proximitySensorSwitcher = proximitySensorPO.findProximitySensorSwitcher();
                if ((originalProximitySensorState && "OFF".equals(proximitySensorSwitcher.getAttribute("text")))
                        || (!originalProximitySensorState && "ON".equals(proximitySensorSwitcher.getAttribute("text")))) {
                    proximitySensorSwitcher.click();
                }
                configurationPO.forceStopSettingConfiguration();
            }
        }
    }

    /**
     * C119797 Verify that Lock Apps setting doesn't work on another panel if unchecking it when exporting
     * 1. Plugged in USB drive to panel #1
     * 2. Android Settings -> System -> Import/export configuration
     * 3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     * 4. Check "Export network settings" and "Include wallpaper file", uncheck "Lock the Settings, Panel Management, and Update apps"
     * 5. Click "CONTINUE" button to display the list of settings to be exported, drag up and down to confirm that the chose options are displayed in the list
     * 6. Click "CONTINUE" button and enter file name and export .cfg file to USB drive
     * 7. Plug this USB drive to another panel of the same class and firmware version, import this .cfg file
     *  => Confirm that the panel #2 has the same network configuration as the panel #1
     *  => Confirm that the wallpaper of panel #2 is the same with the panel #1
     *  => Confirm that those apps in panel #2 are still visible in Locker
     * Author: Rock Shi
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119797VerifyThatLockAppsSettingDoesNotWorkOnAnotherPanelIfUncheckingItWhenExporting() {
        TestRail.setTestRailId("119797");

        AtomicBoolean isReboot = new AtomicBoolean(false);
        AtomicReference<String> deleteFile = new AtomicReference<>();
        setTestCaseCleaner(() -> {
            String cfgFile = deleteFile.get();
            if (cfgFile != null && !cfgFile.isEmpty()) {
                Adb.adb("shell", "rm -rf " + cfgFile);
            }
            if (!isReboot.get()) {
                configurationPO.forceStopFilPicker();
                configurationPO.forceStopSettingConfiguration();
            }
        });


        TestRail.addStepName("Plugged in USB drive to panel #1");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        AssumeKt.assumeTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        final String path_media_rw = "/mnt/media_rw/";
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";

        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);

        TestRail.addStepName("Click and pop up a dialog, choose \"EXPORT\" and jump to the export dialog");
        By exportBtnPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_button");
        ElementHelper.waitUntilVisible(exportBtnPath, 5);
        ElementHelper.findElement(exportBtnPath).click();

        TestRail.addStepName("\"Export network settings\" and \"Include wallpaper file\", uncheck \"Lock the Settings, Panel Management, and Update apps\"");
        By continueBtnPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/continue_button");
        ElementHelper.waitUntilVisible(continueBtnPath, 5);
        configurationPO.dispatchCheckBox("network_checkBox", true);
        configurationPO.dispatchCheckBox("wallerpaper_checkBox", true);
        configurationPO.dispatchCheckBox("lock_checkBox", false);
        ElementHelper.click(continueBtnPath);
        ElementHelper.waitUntilVisible(continueBtnPath, 5);
        ElementHelper.click(continueBtnPath);
        AppiumHelper.switchWindowToFullScreen();
        final String TEST_CASE_NAME = "C119797";
        final String CONFIGURATION_FILE_POSTFIX = ".cfg";
        final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;
        String testCfgFilePath = cfgFileFolder + testCaseCfgName;
        // try to delete the file, before save it
        Adb.adb("shell", "rm -rf " + testCfgFilePath);

        ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
        filesPO.selectStorage("usb");
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(testCfgFilePath);
        Assert.assertTrue(exportedSuccessful,
                "Expect the configuration has been exported to the USB drive, but is NOT");
        deleteFile.set(testCfgFilePath);

        // we can not verify wallpaper and network, simple to verify source detection, get source detection and change it
        configurationPO.closeSettingWindow();
        systemPO.startAppFromUnifiedLauncher(testAppName());
        settingsPO.navToNetworkAndInternet();
        boolean previousStatus = settingsPO.getWiFiStatus();
        configurationPO.closeSettingWindow();

        TestRail.addStepName("Plug this USB drive to another panel of the same class and firmware version, import this .cfg file");

        systemPO.startAppFromUnifiedLauncher(testAppName());
        configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);
        // We considered importing on the same panel to simulate another panel that has the same class and firmware version.
        configurationPO.clickImportExportConfiguration();
        configurationPO.chooseImportToImportDialog();
        ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
        filesPO.selectStorage("usb");
        filesPO.switchToMenuList();
        filesPO.searchFileByNameAndClick(testCaseCfgName);
        configurationPO.confirmImportAndWaitForFinished();

        ElementHelper.waitUntilNotPresent(importProgressBar);
        isReboot.set(true);
        AppiumHelper.rebootPanel();

        TestRail.addStepName("Confirm that those apps in panel #2 are still visible in Locker");
        final Set<String> appsListForAdminAccessCheck = new HashSet();
        appsListForAdminAccessCheck.add("Update");
        appsListForAdminAccessCheck.add("Settings");
        appsListForAdminAccessCheck.add("Panel Management");
        systemPO.startLocker();
        Assert.assertTrue(configurationPO.foundAppsInLocker(systemPO, appsListForAdminAccessCheck),
                "Confirm that those apps in panel #2 are still visible in Locker");

        systemPO.startAppFromUnifiedLauncher(testAppName());
        settingsPO.navToNetworkAndInternet();
        boolean currentStatus = settingsPO.getWiFiStatus();
        Assert.assertEquals(currentStatus, previousStatus, "Confirm that the panel #2 has the same network configuration as the panel #1");
    }

    /**
     * C119801 Verify that renamed Settings configuration file can work on another panel
     * Steps:
     * 1.Plug USB in one panel
     * 2.Verify at least one USB drive has been plugged.
     * 3.Android Settings -> System -> Import/export configuration
     * 4.exported configuration file which named C119801.cfg -> check the C119801.cfg is produced in usb
     * 5.rename this C119801.cfg file name to rename.cfg
     * 6.import this rename.cfg file
     * 7.check it can be imported successful
     * Author: guangxi.chen
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119801VerifyThatRenamedSettingsConfigurationFileCanWorkOnAnotherPanel() {
        TestRail.setTestRailId("119801");
        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        setTestCaseCleaner(() -> {
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
            configurationPO.forceStopFilPicker();
        });
        TestRail.addStepName("Plug USB in one panel");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        TestRail.addStepName("Verify at least one USB drive has been plugged.");
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.continueExportConfigurationToFilePicker();
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();
        TestRail.addStepName("exported configuration file which named C119801.cfg");
        final String TEST_CASE_NAME = "C119801";
        final String CONFIGURATION_FILE_POSTFIX = ".cfg";
        final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;

        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        TestRail.addStepName("check the C119801.cfg is produced in usb");
        final String path_media_rw = "/mnt/media_rw/";
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        boolean cfgFileExist = false;
        String cfgRootPath = "";
        String cfgFileFolder = "";
        for (String s : rwMedias) {
            cfgFileFolder = path_media_rw + s;
            boolean exist = configurationPO.checkConfigurationFileExist(cfgFileFolder + "/" + testCaseCfgName);
            if (exist) {
                cfgFileExist = true;
                cfgRootPath = cfgFileFolder;
                break;
            }
        }
        if (cfgFileExist) {
            configurationPO.addPanelCfgPath(cfgPathListForClean, cfgRootPath + "/" + testCaseCfgName);
        }
        Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
        TestRail.addStepName("rename this C119801.cfg file name to rename.cfg");
        AppiumHelper.openAppsFromTaskBar("Applications"); //Open Locker
        systemPO.scrollAndClickApp("Files");
        filesPO.selectFileByName("usb", testCaseCfgName);
        String renameCfgFileName = "rename.cfg";
        ElementHelper.findElement(By.xpath("//android.widget.ImageButton[@content-desc=\"More options\"]")).click();
        ElementHelper.findElement(Locator.byText("Rename")).click();
        MobileElement editElement = ElementHelper.findElement(By.id("android:id/text1"));
        editElement.clear();
        editElement.sendKeys(renameCfgFileName);
        ElementHelper.findElement(By.id("android:id/button1")).click();
        systemPO.closeAppOnMenuBar("Files");
        configurationPO.addPanelCfgPath(cfgPathListForClean, cfgRootPath + "/" + renameCfgFileName);
        TestRail.addStepName("import this rename.cfg file");
        ElementHelper.click(Locator.byText("Import/export configuration"));
        configurationPO.chooseImportToImportDialog();
        filesPO.searchFileByNameAndClick(renameCfgFileName);
        final String btnImportPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/tv_ipt_start']";
        ElementHelper.waitUntilVisible(By.xpath(btnImportPath), 5);
        Driver.getAndroidDriver().findElementByXPath(btnImportPath).click();
        TestRail.addStepName("check it can be imported successful");
        Assert.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Import successful"), 40));
        AppiumHelper.rebootPanel();
    }

    /**
     * C119802 Verify that Settings configuration file with special characters can be saved
     * Steps
     *  1. Plugged in USB drive to the panel
     *  2. Android Settings -> System -> Import/export configuration
     *  3. Click and pop up a dialog, choose "EXPORT" and jump to the export dialog
     *  4. Click "CONTINUE" button to display the list of settings to be exported
     *  5. Click "CONTINUE" button and the default Files app opens up, choose USB folder
     *  6. Delete the default filename to make the filename empty, click "EXPORT" button, export fails
     *  7. Type in special characters, click "EXPORT" button
     *  8. Pops up a dialog with "Export successful" and click Close button to close it
     *  9. Menu bar -> Application Locker -> Files app
     *   => Confirm that the .cfg file with new name is saved to the USB drive
     * Author: DavidDing
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119802VerifyThatSettingsConfigurationFileWithSpecialCharactersCanBeSaved() {
        TestRail.setTestRailId("119802");

        final String TEST_CASE_NAME = "C119802";
        String testCfgFilePath = null;
        try {
            TestRail.addStepName("Plugged in USB drive to the panel");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";

            TestRail.addStepName("Android Settings -> System -> Import/export configuration");
            configurationPO.navigateToSettingConfigurationWithScroll(Direction.UP);

            TestRail.addStepName("Click and pop up a dialog, choose \"EXPORT\" and jump to the export dialog");
            By exportButtonPath = Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/export_button");
            ElementHelper.waitUntilVisible(exportButtonPath, 3);
            ElementHelper.findElement(exportButtonPath).click();

            TestRail.addStepName("Click \"CONTINUE\" button to display the list of settings to be exported");
            ElementHelper.waitUntilVisible(continueButton, 3);
            ElementHelper.findElement(continueButton).click();

            TestRail.addStepName("Click \"CONTINUE\" button and the default Files app opens up, choose USB folder");
            ElementHelper.waitUntilVisible(continueButton, 3);
            ElementHelper.findElement(continueButton).click();
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();

            TestRail.addStepName("Delete the default filename to make the filename empty, click \"EXPORT\" button, export fails");
            configurationPO.exportConfigurationWithSpecifiedNameAndClickSaveButton(null);
            AppiumHelper.waitForSeconds(1);
            Assert.assertTrue(configurationPO.isFilePickerWaitingForInput(),
                    "Expect the Files app has not been closed and waiting for input, but is NOT.");

            TestRail.addStepName("Type in special characters, click \"EXPORT\" button");
            final String testCaseCfgNameWithSpecialCharacters = TEST_CASE_NAME + "-*+/\\!@#$%^&?[]{}()<>.cfg";
            configurationPO.exportConfigurationWithSpecifiedNameAndClickSaveButton(testCaseCfgNameWithSpecialCharacters);

            TestRail.addStepName("Pops up a dialog with \"Export successful\" and click Close button to close it");
            By closeBtnPath = By.xpath("//*[@text='CLOSE' and @resource-id='com.prometheanworld.settingsconfiguration:id/close_button']");
            ElementHelper.waitUntilVisible(closeBtnPath, 15);
            AndroidElement textViewExportSuccessful = (AndroidElement) ElementHelper.findElement(Locator.byText("Export successful"));
            Assert.assertTrue(textViewExportSuccessful != null && textViewExportSuccessful.isDisplayed(),
                    "Expect the message \"Export successful\" is displayed on the page, but is NOT");
            ElementHelper.findElement(closeBtnPath).click();
            testCfgFilePath = cfgFileFolder + testCaseCfgNameWithSpecialCharacters;

            TestRail.addStepName("Menu bar -> Application Locker -> Files app");
            systemPO.startAppFromUnifiedLauncher("Files");

            TestRail.addStepName("Confirm that the .cfg file with new name is saved to the USB drive");
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            final String searchFileName = TEST_CASE_NAME;
            filesPO.searchFileByName(searchFileName);
            String cfgItemPath = "//*[contains(@text,'" + searchFileName + "') and @resource-id='android:id/title']";
            ElementHelper.waitUntilVisible(By.xpath(cfgItemPath), 5);
            AndroidElement dirList = (AndroidElement) ElementHelper.findElement(By.xpath("//*[@resource-id='com.android.documentsui:id/dir_list']"));
            AndroidElement matchedListItem = (AndroidElement) dirList.findElementByXPath(cfgItemPath);
            Assert.assertTrue(matchedListItem.isDisplayed(), "Expect the specified CFG file is displayed, but is NOT");
        } catch (Exception e) {
            AppiumHelper.captureScreenshot(TEST_CASE_NAME + "_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null && !testCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + TEST_CASE_NAME + "*.cfg");
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
        }
    }

    /**
     * C119813 Verify that users can import off-state of "Menu", "Home Source" and "Enable Apps" to another panel
     * Steps:
     *  1. Plugged in USB drive to panel #1
     *  2. Android Settings -> Display, set the "Menu", "Home Source" and "Enable Apps" to off
     *  3. Android Settings -> System -> Import/export configuration, export .cfg file to USB drive
     *  4. Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display, set the "Menu", "Home Source" and "Enable Apps" to on
     *  5. Plug this USB drive to panel #2, import this .cfg file
     *  6. In panel #2, Android Settings -> Display => Confirm that the "Menu", "Home Source" and "Enable Apps" are off
     *  7. Press menu button on the panel or remote control => Confirm that menu bar is not visible
     *  8. Press the Source button => Confirm that the input source selection does not show a "Home" icon
     *  9. Menu bar -> Application Locker => Confirm that only Update, Settings, and Panel Management are enabled and visible in Locker
     * Author: Yifeng Wu
     *
     *
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119813VerifyThatUsersCanImportOffStateOfMenuHomeSourceAndEnableAppsToAnotherPanel() {
        TestRail.setTestRailId("119813");

        String testCfgFilePath = null;
        Boolean defaultMenu = null;
        Boolean defaultHomeSource = null;
        Boolean defaultEnableApps = null;
        final String TEST_CASE_NAME = "119813";
        try {
            TestRail.addStepName("Plugged in USB drive to panel #1");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            AssumeKt.assumeTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            TestRail.addStepName("Android Settings -> Display, set the \"Menu\", \"Home Source\" and \"Enable Apps\" to off");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            defaultMenu = configurationPO.isMenuSwitchOn();
            if (defaultMenu) {
                ElementHelper.click(Locator.byText("Menu"));
            }
            Assert.assertFalse(configurationPO.isMenuSwitchOn(),
                    "The switch 'Menu' should be off");
            defaultHomeSource = configurationPO.isHomeSourceSwitchOn();
            if (defaultHomeSource) {
                ElementHelper.click(Locator.byText("Home source"));
            }
            Assert.assertFalse(configurationPO.isHomeSourceSwitchOn(),
                    "The switch 'Home source' should be off");
            defaultEnableApps = configurationPO.isEnableAppsSwitchOn();
            if (defaultEnableApps) {
                ElementHelper.click(Locator.byText("Enable apps"));
            }
            Assert.assertFalse(configurationPO.isEnableAppsSwitchOn(),
                    "The switch 'Enable apps' should be off");
            TestRail.addStepName("Android Settings -> System -> Import/export configuration, export .cfg file to USB drive");
            Driver.pressKey(AndroidKey.BACK);
            AppiumHelper.waitForSeconds(2);
            configurationPO.continueExportConfigurationToFilePicker();
            AppiumHelper.waitForSeconds(2);
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";
            final String testCaseCfgName = TEST_CASE_NAME + ".cfg";
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
            final String exportedFilePath = cfgFileFolder + testCaseCfgName;
            final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(exportedFilePath);
            Assert.assertTrue(exportedSuccessful,
                    "Expect the configuration has been exported to the usb device successfully, but is NOT");
            testCfgFilePath = exportedFilePath;
            TestRail.addStepName("Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display, set the \"Menu\", \"Home Source\" and \"Enable Apps\" to on");
            systemPO.stopSettingsApp();
            Adb.adb("shell am start com.android.settings/com.android.settings.Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            if (!configurationPO.isMenuSwitchOn()) {
                ElementHelper.click(Locator.byText("Menu"));
                AppiumHelper.waitForSeconds(2);
                AppiumHelper.clickAt(0.0, 0.0);
                AppiumHelper.waitForSeconds(2);
            }
            Assert.assertTrue(configurationPO.isMenuSwitchOn(),
                    "The switch 'Menu' should be on");
            if (!configurationPO.isHomeSourceSwitchOn()) {
                ElementHelper.click(Locator.byText("Home source"));
            }
            Assert.assertTrue(configurationPO.isHomeSourceSwitchOn(),
                    "The switch 'Home source' should be on");
            if (!configurationPO.isEnableAppsSwitchOn()) {
                ElementHelper.click(Locator.byText("Enable apps"));
            }
            Assert.assertTrue(configurationPO.isEnableAppsSwitchOn(),
                    "The switch 'Enable apps' should be on");
            Driver.pressKey(AndroidKey.BACK);
            AppiumHelper.waitForSeconds(2);
            TestRail.addStepName("Plug this USB drive to panel #2, import this .cfg file");
            configurationPO.continueImportConfigurationToFilePicker();
            configurationPO.waitForFilePickerReady();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            filesPO.searchFileByNameAndClick(testCaseCfgName);
            configurationPO.confirmImportAndWaitForFinished();
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.prometheanworld.settingsconfiguration:id/reboot_button"), 30);
            Adb.adb("shell", "rm -rf " + testCfgFilePath);
            testCfgFilePath = null;
            AppiumHelper.rebootPanel();
            TestRail.addStepName("In panel #2, Android Settings -> Display");
            Adb.adb("shell am start com.android.settings/com.android.settings.Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            Assert.assertFalse(configurationPO.isMenuSwitchOn(),
                    "The switch 'Menu' should be off");
            Assert.assertFalse(configurationPO.isHomeSourceSwitchOn(),
                    "The switch 'Home source' should be off");
            // The Settings Configuration app doesn't support importing/exporting the 'Enable apps' yet, so "//" these codes.
//            Assert.assertFalse(configurationPO.isEnableAppsSwitchOn(),
//                    "The switch 'Enable apps' should be off");
            systemPO.stopSettingsApp();
            TestRail.addStepName("Press menu button on the panel or remote control");
            boolean isMenuShowing = false;
            for (int i = 0; i < 4; i++) {
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                if (ElementHelper.isVisible(Locator.byResourceId("id/menu_bar"), 5)) {
                    isMenuShowing = true;
                    break;
                }
            }
            Assert.assertFalse(isMenuShowing, "The menu bar should not be visible");
            TestRail.addStepName("Press the Source button");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.sources:id/source_container_view")),
                    "The dialog 'Sources' should be visible");
            Assert.assertFalse(ElementHelper.isVisible(By.xpath("//*[@text='Home' and @resource-id='com.prometheanworld.sources:id/source_item_name']")),
                    "The 'Home' source should not be visible");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.sources:id/source_container_view")),
                    "The dialog 'Sources' should not be visible");
            // The Settings Configuration app doesn't support importing/exporting the 'Enable apps' yet, so "//" these codes.
//            TestRail.addStepName("Menu bar -> Application Locker");
//            boolean isLockerShowing = false;
//            for (int i = 0; i < 4; i++) {
//                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
//                if (ElementHelper.isVisible(Locator.byResourceId("id/locker_container"), 5)) {
//                    isLockerShowing = true;
//                }
//            }
//            Assert.assertTrue(isLockerShowing, "The locker should be visible");
//            final Set<String> appsListForAdminAccessCheck = new HashSet();
//            appsListForAdminAccessCheck.add("Update");
//            appsListForAdminAccessCheck.add("Settings");
//            appsListForAdminAccessCheck.add("Panel Management");
//            Assert.assertTrue(configurationPO.foundAppsInLocker(systemPO, appsListForAdminAccessCheck),
//                    "Only Update, Settings, and Panel Management should be enabled and visible in Locker");
        } catch (Throwable e) {
            AppiumHelper.captureScreenshot("C119813_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null) {
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            configurationPO.forceStopSettingConfiguration();
            configurationPO.forceStopFilPicker();
            systemPO.stopSettingsApp();
            if (ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.sources:id/source_container_view"))) {
                AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            }
            Adb.adb("shell am start com.android.settings/com.android.settings.Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            if (defaultMenu != null && configurationPO.isMenuSwitchOn() != defaultMenu) {
                ElementHelper.click(Locator.byText("Menu"));
                AppiumHelper.waitForSeconds(2);
                AppiumHelper.clickAt(0.0, 0.0);
                AppiumHelper.waitForSeconds(2);
            }
            if (defaultHomeSource != null && configurationPO.isHomeSourceSwitchOn() != defaultHomeSource) {
                ElementHelper.click(Locator.byText("Home source"));
            }
            if (defaultEnableApps != null && configurationPO.isEnableAppsSwitchOn() != defaultEnableApps) {
                ElementHelper.click(Locator.byText("Enable apps"));
            }
        }
    }
    /**
     * C119846 Verify that users can't modify standby and sleep timer less than 3min through cfg file
     * Steps:
     * 1.Plugged in USB drive to panel #1
     * 2.Android Settings -> System -> Import/export configuration
     * 3.Export .cfg file to USB drive
     * 4.Plug this USB drive to PC, unzip and modify the settingConfiguration.json, change the SleepStandBy to "SleepStandBy":{"standbyTime":150.0,"sleepTime":150.0}
     * 5.Zip to cfg format again
     * 6.Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 1 hour
     * 7.Plug this USB drive to panel #2, import this .cfg file
     * 8.Confirm that import fails and the standby and sleep timer in panel #2 are still 1 hour
     * Author: guangxi.chen
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119846VerifyThatUsersCantModifyStandbyAndSleepTimerLessThan3MinThroughCfgFile() {
        TestRail.setTestRailId("119846");

        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();

        StandbySleepTimerPO standbySleepTimerPO = POFactory.getInstance(StandbySleepTimerPO.class);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        StandbySleepTimerPO.SleepStandByTime originStandTime = standbySleepTimerPO.getCurrentStandByTime();
        StandbySleepTimerPO.SleepStandByTime originSleepTime = standbySleepTimerPO.getCurrentSleepTime();

        setTestCaseCleaner(() -> {
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
            if (originStandTime != null) {
                standbySleepTimerPO.scrollStandByTimeSeekbar(originStandTime);
            }
            if (originSleepTime != null) {
                standbySleepTimerPO.scrollSleepTimeSeekbar(originSleepTime);
            }
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
            configurationPO.forceStopFilPicker();
        });

        TestRail.addStepName("Plugged in USB drive to panel #1");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();

        TestRail.addStepName("exported configuration file which named C119846.cfg");
        final String TEST_CASE_NAME = "C119846";
        final String CONFIGURATION_FILE_POSTFIX = ".cfg";
        final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        final String path_media_rw = "/mnt/media_rw/";
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        String cfgFileFolder = "";
        String cfgFilePath = "";
        boolean cfgFileExist = false;
        for (String s : rwMedias) {
            cfgFileFolder = path_media_rw + s;
            cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
            boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
            if (exist) {
                cfgFileExist = true;
                break;
            }
        }
        Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
        configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);

        TestRail.addStepName("Plug this USB drive to PC, unzip and modify the settingConfiguration.json, change the SleepStandBy to \"SleepStandBy\":{\"standbyTime\":150.0,\"sleepTime\":150.0}");
        String cfgFileName = new File(cfgFilePath).getName();
        Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                        && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
        File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
        configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
        File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
        Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
        configurationPO.addLocalCfgPath(cfgPathListForClean, cfgJsonFile);
        Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);
        LinkedHashMap configurations = (LinkedHashMap) cfgMap.get("configurations");
        LinkedHashMap sleepStandBy = (LinkedHashMap) configurations.get("SleepStandBy");
        sleepStandBy.put("standbyTime", 150.0);
        sleepStandBy.put("sleepTime", 150.0);
        configurations.put("SleepStandBy", sleepStandBy);
        cfgMap.put("configurations", configurations);

        TestRail.addStepName("Zip to cfg format again");
        File newCfgFile = configurationPO.generateNewCfgFileFromJson(JsonUtil.toJson(cfgMap), testCaseCfgName);
        configurationPO.addLocalCfgPath(cfgPathListForClean, newCfgFile);
        FileUtil.deleteFile(unzipCfgFolder);
        boolean filePushed = configurationPO.pushConfigurationFile(newCfgFile, cfgFileFolder);
        newCfgFile.delete();
        Assert.assertTrue(filePushed, "Expect the new configuration file with change SleepStandBy value been pushed to the plugged USB drive successful, but FAILURE.");

        TestRail.addStepName("Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 1 hour");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        StandbySleepTimerPO.SleepStandByTime expectTime = StandbySleepTimerPO.SleepStandByTime._1hrs;
        standbySleepTimerPO.scrollStandByTimeSeekbar(expectTime);
        standbySleepTimerPO.scrollSleepTimeSeekbar(expectTime);
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));

        TestRail.addStepName("Plug this USB drive to panel #2, import this .cfg file");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.navigateToImportExportConfiguration();
        configurationPO.chooseImportToImportDialog();
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();
        filesPO.searchFileByNameAndClick(testCaseCfgName);
        final String btnImportPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/tv_ipt_start']";
        ElementHelper.waitUntilVisible(By.xpath(btnImportPath), 5);
        Driver.getAndroidDriver().findElementByXPath(btnImportPath).click();

        TestRail.addStepName("Confirm that import fails and the standby and sleep timer in panel #2 are still 1 hour");
        Assert.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Unable to apply configuration"), 40));
        ElementHelper.findElement(By.id("com.prometheanworld.settingsconfiguration:id/close_button")).click();
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        Assert.assertEquals(standbySleepTimerPO.getCurrentStandByTime().getDurationSecond(), 3600);
        Assert.assertEquals(standbySleepTimerPO.getCurrentSleepTime().getDurationSecond(), 3600);
    }

    /**
     * C119812 Verify that users can import on-state of "Menu", "Home Source" to another panel
     * Steps:
     *  1. Plugged in USB drive to panel #1
     *  2. Android Settings -> Display, set the "Menu", "Home Source" to on
     *  3. Android Settings -> System -> Import/export configuration
     *  4. Export .cfg file to USB drive
     *  5. Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display, set the "Menu", "Home Source" to off
     *  6. Plug this USB drive to panel #2, import this .cfg file
     *  7. In panel #2, Android Settings -> Display => Confirm that the "Menu", "Home Source" is on
     *  8. Press menu button on the panel or remote control => Confirm that menu bar visible and function
     *  9. Press source button => Confirm that the input source selection shows a "Home" icon that represents the android home screen
     * Author: Yifeng Wu
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119812VerifyThatUsersCanImportOnStateOfMenuHomeSourceToAnotherPanel() {
        TestRail.setTestRailId("119812");

        String testCfgFilePath = null;
        Boolean defaultMenu = null;
        Boolean defaultHomeSource = null;
        final String TEST_CASE_NAME = "119812";
        try {
            TestRail.addStepName("Plugged in USB drive to panel #1");
            Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
            final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
            AssumeKt.assumeTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");
            TestRail.addStepName("Android Settings -> Display, set the \"Menu\", \"Home Source\" to on");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            defaultMenu = configurationPO.isMenuSwitchOn();
            if (!defaultMenu) {
                ElementHelper.click(Locator.byText("Menu"));
                AppiumHelper.waitForSeconds(2);
                AppiumHelper.clickAt(0.0, 0.0);
                AppiumHelper.waitForSeconds(2);
            }
            Assert.assertTrue(configurationPO.isMenuSwitchOn(),
                    "The switch 'Menu' should be on");
            defaultHomeSource = configurationPO.isHomeSourceSwitchOn();
            if (!defaultHomeSource) {
                ElementHelper.click(Locator.byText("Home source"));
            }
            Assert.assertTrue(configurationPO.isHomeSourceSwitchOn(),
                    "The switch 'Home source' should be on");
            TestRail.addStepName("Android Settings -> System -> Import/export configuration");
            Driver.pressKey(AndroidKey.BACK);
            AppiumHelper.waitForSeconds(2);
            configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
            TestRail.addStepName("Export .cfg file to USB drive");
            AppiumHelper.waitForSeconds(2);
            ElementHelper.waitUntilVisible(Locator.byResourceId("com.android.documentsui:id/toolbar"), 5);
            filesPO.selectStorage("usb");
            final String path_media_rw = "/mnt/media_rw/";
            final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
            String cfgFileFolder = path_media_rw + rwMedias.get(0) + "/";
            final String testCaseCfgName = TEST_CASE_NAME + ".cfg";
            configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
            final String exportedFilePath = cfgFileFolder + testCaseCfgName;
            final boolean exportedSuccessful = configurationPO.checkConfigurationFileExist(exportedFilePath);
            Assert.assertTrue(exportedSuccessful,
                    "Expect the configuration has been exported to the usb device successfully, but is NOT");
            testCfgFilePath = exportedFilePath;
            TestRail.addStepName("Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display, set the \"Menu\", \"Home Source\" to off");
            systemPO.stopSettingsApp();
            Adb.adb("shell am start com.android.settings/com.android.settings.Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            if (configurationPO.isMenuSwitchOn()) {
                ElementHelper.click(Locator.byText("Menu"));
            }
            Assert.assertFalse(configurationPO.isMenuSwitchOn(),
                    "The switch 'Menu' should be on");
            if (configurationPO.isHomeSourceSwitchOn()) {
                ElementHelper.click(Locator.byText("Home source"));
            }
            Assert.assertFalse(configurationPO.isHomeSourceSwitchOn(),
                    "The switch 'Home source' should be on");
            TestRail.addStepName("Plug this USB drive to panel #2, import this .cfg file");
            Driver.pressKey(AndroidKey.BACK);
            AppiumHelper.waitForSeconds(2);
            configurationPO.continueImportConfigurationToFilePicker();
            configurationPO.waitForFilePickerReady();
            filesPO.selectStorage("usb");
            filesPO.switchToMenuList();
            filesPO.searchFileByNameAndClick(testCaseCfgName);
            configurationPO.confirmImportAndWaitForFinished();
            AppiumHelper.waitForSeconds(20);
            Adb.adb("shell", "rm -rf " + testCfgFilePath);
            testCfgFilePath = null;
            AppiumHelper.rebootPanel();
            TestRail.addStepName("In panel #2, Android Settings -> Display");
            systemPO.startAppFromUnifiedLauncher("Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            Assert.assertTrue(configurationPO.isMenuSwitchOn(),
                    "The switch 'Menu' should be on");
            Assert.assertTrue(configurationPO.isHomeSourceSwitchOn(),
                    "The switch 'Home source' should be on");
            systemPO.stopSettingsApp();
            TestRail.addStepName("Press menu button on the panel or remote control");
            boolean isMenuShowing = false;
            for (int i = 0; i < 4; i++) {
                AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
                if (ElementHelper.isVisible(Locator.byResourceId("id/menu_bar"), 5)) {
                    isMenuShowing = true;
                    break;
                }
            }
            Assert.assertTrue(isMenuShowing, "The menu bar should be visible after pressing the menu button");
            AppiumHelper.clickKey(PrometheanKey.Menu, PrometheanKeyboard.RemoteControl);
            AppiumHelper.waitForSeconds(2);
            TestRail.addStepName("Press the Source button");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.sources:id/source_container_view")),
                    "The dialog 'Sources' should be visible");
            Assert.assertTrue(ElementHelper.isVisible(By.xpath("//*[@text='Home' and @resource-id='com.prometheanworld.sources:id/source_item_name']")),
                    "The 'Home' source should be visible");
            AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            Assert.assertFalse(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.sources:id/source_container_view")),
                    "The dialog 'Sources' should not be visible");
        } catch (Throwable e) {
            AppiumHelper.captureScreenshot("C119812_screenshot_" + System.currentTimeMillis());
            throw e;
        } finally {
            if (testCfgFilePath != null) {
                Adb.adb("shell", "rm -rf " + testCfgFilePath);
            }
            configurationPO.forceStopSettingConfiguration();
            configurationPO.forceStopFilPicker();
            systemPO.stopSettingsApp();
            if (ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.sources:id/source_container_view"))) {
                AppiumHelper.clickKey(PrometheanKey.Sources, PrometheanKeyboard.RemoteControl);
            }
            Adb.adb("shell am start com.android.settings/com.android.settings.Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            AppiumHelper.waitForSeconds(2);
            if (defaultMenu != null && configurationPO.isMenuSwitchOn() != defaultMenu) {
                ElementHelper.click(Locator.byText("Menu"));
                AppiumHelper.waitForSeconds(2);
                AppiumHelper.clickAt(0.0, 0.0);
                AppiumHelper.waitForSeconds(2);
            }
            if (defaultHomeSource != null && configurationPO.isHomeSourceSwitchOn() != defaultHomeSource) {
                ElementHelper.click(Locator.byText("Home source"));
            }
        }
    }

    /**
     * C119827 Verify that users can import on-state of Bluetooth setting to another panel
     * Steps:
     *  1. Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn on Bluetooth
     *  2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of Bluetooth setting to File
     *  3. Panel #2 is the same class and firmware version as panel #1, Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn off Bluetooth
     *  4. Menu bar -> Application Locker -> Open "Settings" -> System -> Import/export configuration -> IMPORT
     *  => Comfirm that import successfully, and the Bluetooth in the second panel still stay on and panel connects to bluetooth devices successfully
     * Author: Luna He
     *
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119827VerifyThatUsersCanImportOnStateOfBluetoothSettingToAnotherPanel() {
        TestRail.setTestRailId("119827");

        String cfgFilePath = null;
        String finalCfgFilePath = cfgFilePath;
        setTestCaseCleaner(() -> {
            if (finalCfgFilePath != null && !finalCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + finalCfgFilePath);
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
        });

        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn on Bluetooth");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        CommonOperator.scrollAndClick("android:id/title", "Connection preferences");
        CommonOperator.scrollAndClick("android:id/title", "Bluetooth");
        if(!configurationPO.isBluetoothSwitchOn()) {
            CommonOperator.scrollAndClick("com.android.settings:id/switch_text","Off");
        }

        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of Bluetooth setting to File");
        //Back to the root page of Settings
        systemPO.startAppFromUnifiedLauncher(testAppName());
        //Save to Download path
        configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
        AppiumHelper.switchWindowToFullScreen();
        final String testCaseCfgName = "C119827.cfg";
        filesPO.selectDownloadFiles();
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        cfgFilePath = "/sdcard/Download/" + testCaseCfgName;
        boolean cfgFileExist = configurationPO.checkConfigurationFileExist(cfgFilePath);
        Assert.assertTrue(cfgFileExist);

        TestRail.addStepName("Panel #2 is the same class and firmware version as panel #1, Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn off Bluetooth");
        Driver.pressKey(AndroidKey.BACK);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        CommonOperator.scrollAndClick("android:id/title", "Connection preferences");
        CommonOperator.scrollAndClick("android:id/title", "Bluetooth");
        ElementHelper.waitUntilVisible(By.xpath("//*[@text='On' and @resource-id='com.android.settings:id/switch_text']"), 5);
        CommonOperator.scrollAndClick("com.android.settings:id/switch_text","On");

        TestRail.addStepName("Menu bar -> Application Locker -> Open ‘Settings’ -> System -> Import/export configuration -> IMPORT ");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        configurationPO.continueImportConfigurationToFilePicker();
        configurationPO.waitForFilePickerReady();
        filesPO.selectDownloadFiles();
        filesPO.switchToMenuList();
        filesPO.searchFileByNameAndClick(testCaseCfgName);
        configurationPO.confirmImportAndWaitForFinished();
        if (cfgFilePath != null && !cfgFilePath.isEmpty()) {
            // At the end of the test, we should delete the test folder we created.
            Adb.adb("shell", "rm -rf " + cfgFilePath);
        }
        AppiumHelper.rebootPanel();
        TestRail.addStepName("Comfirm that import successfully, and the Bluetooth in the second panel still stay on and panel connects to bluetooth devices successfully");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        CommonOperator.scrollAndClick("android:id/title", "Connection preferences");
        CommonOperator.scrollAndClick("android:id/title", "Bluetooth");
        ElementHelper.waitUntilVisible(By.xpath("//*[@text='On' and @resource-id='com.android.settings:id/switch_text']"), 5);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.android.settings:id/switch_text")).getText().equals("On"),"Import Fail");
    }

    /**
     * C119828 Verify that users can import off-state of Bluetooth setting to another panel
     * Steps:
     *  1. Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn off Bluetooth
     *  2. Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of Bluetooth setting to File
     *  3. Panel #2 is the same class and firmware version as panel #1, Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn on Bluetooth
     *  4. Menu bar -> Application Locker -> Open "Settings" -> System -> Import/export configuration -> IMPORT
     *  => Comfirm that import successfully, and the Bluetooth in the second panel still stay off and panel connects to bluetooth devices successfully
     * Author: Luna He
     *
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C119828VerifyThatUsersCanImportOffStateOfBluetoothSettingToAnotherPanel() {
        TestRail.setTestRailId("119828");

        String cfgFilePath = null;
        String finalCfgFilePath = cfgFilePath;
        setTestCaseCleaner(() -> {
            if (finalCfgFilePath != null && !finalCfgFilePath.isEmpty()) {
                // At the end of the test, we should delete the test folder we created.
                Adb.adb("shell", "rm -rf " + finalCfgFilePath);
            }
            configurationPO.forceStopFilPicker();
            configurationPO.forceStopSettingConfiguration();
        });

        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn off Bluetooth");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        CommonOperator.scrollAndClick("android:id/title", "Connection preferences");
        CommonOperator.scrollAndClick("android:id/title", "Bluetooth");
        if(!configurationPO.isBluetoothSwitchOff()) {
            CommonOperator.scrollAndClick("com.android.settings:id/switch_text","On");
        }

        TestRail.addStepName("Menu bar -> Application Locker -> Settings -> System -> Import/export configuration -> EXPORT, save .CFG file of Bluetooth setting to File");
        //Back to the root page of Settings
        systemPO.startAppFromUnifiedLauncher(testAppName());
        //Save to Download path
        configurationPO.continueExportConfigurationWithOptionsToFilePicker(false, false, false);
        AppiumHelper.switchWindowToFullScreen();
        final String testCaseCfgName = "C119828.cfg";
        filesPO.selectDownloadFiles();
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        cfgFilePath = "/sdcard/Download/" + testCaseCfgName;
        boolean cfgFileExist = configurationPO.checkConfigurationFileExist(cfgFilePath);
        Assert.assertTrue(cfgFileExist);

        TestRail.addStepName("Panel #2 is the same class and firmware version as panel #1, Menu bar -> Application Locker -> Settings -> Connected Devices -> Bluetooth, turn on Bluetooth");
        Driver.pressKey(AndroidKey.BACK);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        CommonOperator.scrollAndClick("android:id/title", "Connection preferences");
        CommonOperator.scrollAndClick("android:id/title", "Bluetooth");
        ElementHelper.waitUntilVisible(By.xpath("//*[@text='Off' and @resource-id='com.android.settings:id/switch_text']"), 5);
        CommonOperator.scrollAndClick("com.android.settings:id/switch_text","Off");

        TestRail.addStepName("Menu bar -> Application Locker -> Open ‘Settings’ -> System -> Import/export configuration -> IMPORT ");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        configurationPO.continueImportConfigurationToFilePicker();
        configurationPO.waitForFilePickerReady();
        filesPO.selectDownloadFiles();
        filesPO.switchToMenuList();
        filesPO.searchFileByNameAndClick(testCaseCfgName);
        configurationPO.confirmImportAndWaitForFinished();
        if (cfgFilePath != null && !cfgFilePath.isEmpty()) {
            // At the end of the test, we should delete the test folder we created.
            Adb.adb("shell", "rm -rf " + cfgFilePath);
        }
        AppiumHelper.rebootPanel();

        TestRail.addStepName("Comfirm that import successfully, and the Bluetooth in the second panel still stay off and panel connects to bluetooth devices successfully");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        CommonOperator.scrollAndClick("android:id/title", "Connection preferences");
        CommonOperator.scrollAndClick("android:id/title", "Bluetooth");
        ElementHelper.waitUntilVisible(By.xpath("//*[@text='Off' and @resource-id='com.android.settings:id/switch_text']"), 5);
        AssertKt.assertTrue(Driver.getAndroidDriver().findElement(By.id("com.android.settings:id/switch_text")).getText().equals("Off"),"Import Fail");
    }


    /**
     * C119845 Verify that users can modify standby and sleep timer to Never through cfg file
     * Steps:
     * 1.Plugged in USB drive to panel #1
     * 2.Android Settings -> System -> Import/export configuration
     * 3.Export .cfg file to USB drive
     * 4.Plug this USB drive to PC, unzip and modify the settingConfiguration.json, change the SleepStandBy to "SleepStandBy":{"standbyTime":0.0,"sleepTime":0.0}
     * 5.Zip to cfg format again
     * 6.Panel #2 is the same class and firmware version as panel #1, Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 1 hour
     * 7.Plug this USB drive to panel #2, import this .cfg file
     * 8.Confirm that standby and sleep timer are Never in panel #2
     * Author: guangxi.chen
     */
    @Test(groups = {"P2", "UserDebug"})
    public void C119845VerifyThatUsersCanModifyStandbyAndSleepTimerToNeverThroughCfgFile(){
        TestRail.setTestRailId("119845");

        List<SettingConfigurationPO.CfgPath> cfgPathListForClean = new ArrayList();
        StandbySleepTimerPO standbySleepTimerPO = POFactory.getInstance(StandbySleepTimerPO.class);
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        StandbySleepTimerPO.SleepStandByTime originStandTime = standbySleepTimerPO.getCurrentStandByTime();
        StandbySleepTimerPO.SleepStandByTime originSleepTime = standbySleepTimerPO.getCurrentSleepTime();

        setTestCaseCleaner(() -> {
            systemPO.startAppFromUnifiedLauncher(testAppName());
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
            ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
            if (originStandTime != null) {
                standbySleepTimerPO.scrollStandByTimeSeekbar(originStandTime);
            }
            if (originSleepTime != null) {
                standbySleepTimerPO.scrollSleepTimeSeekbar(originSleepTime);
            }
            configurationPO.removeMultiConfigurationFile(cfgPathListForClean);
            configurationPO.forceStopSettingConfiguration();
            configurationPO.forceStopFilPicker();
        });

        TestRail.addStepName("Plugged in USB drive to panel #1");
        Map<String, Object> pluggedDevicesMap = configurationPO.getAvailablePluggedUseDevices();
        final boolean usbPlugged = (boolean) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.USB_PLUGGED, false);
        Assert.assertTrue(usbPlugged, "Expect at least one USB drive has been plugged, but found ZERO.");

        TestRail.addStepName("Android Settings -> System -> Import/export configuration");
        systemPO.startAppFromUnifiedLauncher("Settings");
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.continueExportConfigurationWithOptionsToFilePicker(false,false,false);
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();

        TestRail.addStepName("exported configuration file which named 119845.cfg");
        final String TEST_CASE_NAME = "C119845";
        final String CONFIGURATION_FILE_POSTFIX = ".cfg";
        final String testCaseCfgName = TEST_CASE_NAME + CONFIGURATION_FILE_POSTFIX;
        configurationPO.exportConfigurationWithSpecifiedNameAndWaitForFinished(testCaseCfgName);
        final String path_media_rw = "/mnt/media_rw/";
        final List<String> rwMedias = (List<String>) pluggedDevicesMap.getOrDefault(SettingConfigurationPO.PARAM_KEY.RW_MEDIAS, new ArrayList<String>());
        String cfgFileFolder = "";
        String cfgFilePath = "";
        boolean cfgFileExist = false;
        for (String s : rwMedias) {
            cfgFileFolder = path_media_rw + s;
            cfgFilePath = cfgFileFolder + "/" + testCaseCfgName;
            boolean exist = configurationPO.checkConfigurationFileExist(cfgFilePath);
            if (exist) {
                cfgFileExist = true;
                break;
            }
        }
        Assert.assertTrue(cfgFileExist, "Expect the configuration file has been exported to one USB drive that has been plugged, but NOT found.");
        configurationPO.addPanelCfgPath(cfgPathListForClean, cfgFilePath);

        TestRail.addStepName("Plug this USB drive to PC, unzip and modify the settingConfiguration.json, change the SleepStandBy to \"SleepStandBy\":{\"standbyTime\":0.0,\"sleepTime\":0.0}");
        String cfgFileName = new File(cfgFilePath).getName();
        Assert.assertTrue(cfgFileName.contains(CONFIGURATION_FILE_POSTFIX)
                        && CONFIGURATION_FILE_POSTFIX.equals(cfgFileName.substring(cfgFileName.lastIndexOf("."))),
                "Expect the exported configuration file has the '.cfg' postfix, but NOT found.");
        File unzipCfgFolder = configurationPO.pullAndUnzipConfigurationFile(cfgFilePath);
        configurationPO.addLocalCfgPath(cfgPathListForClean, unzipCfgFolder);
        File cfgJsonFile = new File(unzipCfgFolder.getAbsolutePath() + File.separator + "settingConfiguration.json");
        Assert.assertTrue(cfgJsonFile.exists() && cfgJsonFile.length() > 0,
                "Expect the unzip \"settingConfiguration.json\" file is valid, but NOT valid.");
        configurationPO.addLocalCfgPath(cfgPathListForClean, cfgJsonFile);
        Map<String, Object> cfgMap = JsonUtil.fromJsonFile(cfgJsonFile, Map.class);
        LinkedHashMap configurations = (LinkedHashMap) cfgMap.get("configurations");
        LinkedHashMap sleepStandBy = (LinkedHashMap) configurations.get("SleepStandBy");
        sleepStandBy.put("standbyTime", 0.0);
        sleepStandBy.put("sleepTime", 0.0);
        configurations.put("SleepStandBy", sleepStandBy);
        cfgMap.put("configurations", configurations);

        TestRail.addStepName("Zip to cfg format again");
        File newCfgFile = configurationPO.generateNewCfgFileFromJson(JsonUtil.toJson(cfgMap), testCaseCfgName);
        configurationPO.addLocalCfgPath(cfgPathListForClean, newCfgFile);
        FileUtil.deleteFile(unzipCfgFolder);
        boolean filePushed = configurationPO.pushConfigurationFile(newCfgFile, cfgFileFolder);
        newCfgFile.delete();
        Assert.assertTrue(filePushed, "Expect the new configuration file with change SleepStandBy value been pushed to the plugged USB drive successful, but FAILURE.");

        TestRail.addStepName("Android Settings -> Display -> Advanced -> Standby and Sleep Times, set standby and sleep timer to 1 hour");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        StandbySleepTimerPO.SleepStandByTime expectTime = StandbySleepTimerPO.SleepStandByTime._1hrs;
        standbySleepTimerPO.scrollStandByTimeSeekbar(expectTime);
        standbySleepTimerPO.scrollSleepTimeSeekbar(expectTime);
        ElementHelper.clickWhenVisible(By.id("android:id/close_window"));

        TestRail.addStepName("Plug this USB drive to panel #2, import this .cfg file");
        systemPO.startAppFromUnifiedLauncher(testAppName());
        AppiumHelper.switchWindowToFullScreen();
        configurationPO.navigateToImportExportConfiguration();
        configurationPO.chooseImportToImportDialog();
        filesPO.selectStorage("usb");
        AppiumHelper.switchWindowToFullScreen();
        filesPO.searchFileByNameAndClick(testCaseCfgName);
        final String btnImportPath = "//*[@resource-id='com.prometheanworld.settingsconfiguration:id/tv_ipt_start']";
        ElementHelper.waitUntilVisible(By.xpath(btnImportPath), 10);
        ElementHelper.click(By.xpath(btnImportPath));
        Assert.assertTrue(ElementHelper.isPresent(Locator.byTextContains("Import successful"), 40));
        AppiumHelper.rebootPanel();

        TestRail.addStepName("Confirm that standby and sleep timer are Never in panel #2");
        AppiumHelper.waitForSeconds(2);
        systemPO.startAppFromUnifiedLauncher(testAppName());
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Display");
        ElementHelper.click(Locator.byText("Standby and sleep timeouts"));
        Assert.assertEquals(standbySleepTimerPO.getCurrentStandByTime().getDurationSecond(), 0);
        Assert.assertEquals(standbySleepTimerPO.getCurrentSleepTime().getDurationSecond(), 0);
    }

}