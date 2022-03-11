package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP9.OOBEPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OOBE extends BaseTest {

    private static final String BOOTMANAGER_ID_PREFIX = "com.prometheanworld.bootmanagement:id/%s";

    private static final By SET_UP_ACTIVPANEL = Locator.byText("Set Up ActivPanel");
    private static final By CONTINUE = Locator.byText("Continue");
    private static final By SKIP = Locator.byText("Skip");
    private static final By SKIP_SET_UP = Locator.byText("Skip Set Up");
    private static final By BACK = Locator.byText("Back");
    private static final By ACCEPT = Locator.byText("Accept");
    private static final By FINISHED = Locator.byText("Finished");

    private final OOBEPO oobePO = POFactory.getInstance(OOBEPO.class);

    /**
     * C116717 Verify that the Network Connect page does not appear when ethernet is used
     * Steps:
     *  1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     *  2. After panel is powered on
     *  3. Click "Set Up ActivPanel" button
     *  4. Click Continue button to skip the Select Language and Select TimeZone page
     *  5. Click Skip button in the Update ActivPanel page
     *  6. Click the Accept button
     *  7. Open Chromium app and go to any website
     *
     */
    @Test(groups = {"P1","UserDebug"})
    public void C116717VerifyThatTheNetworkConnectPageDoesNotAppearWhenEthernetIsUsed() {
        TestRail.setTestRailId("116717");
        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        oobePO.startOOBE();
        TestRail.addStepName("After panel is powered on");
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SET_UP_ACTIVPANEL);
        TestRail.addStepName("Click \"Set Up ActivPanel\" button");
        ElementHelper.clickWhenEnabled(SET_UP_ACTIVPANEL);
        TestRail.addStepName("Click Continue button to skip the Select Language and Select TimeZone page");
        // show Select Language page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Language"));
        ElementHelper.waitUntilPresent(CONTINUE);
        ElementHelper.clickWhenEnabled(CONTINUE);
        // show Select Timezone page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Select Timezone"));
        ElementHelper.waitUntilPresent(CONTINUE);
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.clickWhenEnabled(CONTINUE);
        // show Update ActivPanel page
        ElementHelper.waitUntilPresent(Locator.byTextContains("Update ActivPanel"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(SKIP);
        TestRail.addStepName("Click Skip button in the Update ActivPanel page");
        ElementHelper.clickWhenEnabled(SKIP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(ACCEPT);
        TestRail.addStepName("Click the Accept button");
        ElementHelper.clickWhenEnabled(ACCEPT);
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_time")));
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_date")));
        // because this step just to verify whether or not the device already connected to the internet
        // but this step is hard to implement, also this step is limited by whether or not the Chromium app is installed
        // so we use ping command instead of the original step.
        TestRail.addStepName("Open Chromium app and go to any website");
        Assert.assertTrue(oobePO.isNetworkConnected(), "This device should already be connected to the Internet, but actually not");
    }

    /**
     * C116668 Verify that users can skip the OOBE set up process
     * Steps:
     *  1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     *  2. After panel is powered on
     *  3. Click Skip Set Up button
     *  4. Click the Accept button
     *
     */
    @Test(groups = {"P0","UserDebug"})
    public void C116668VerifyThatUsersCanSkipTheOOBESetUpProcess() {
        TestRail.setTestRailId("116668");
        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        oobePO.startOOBE();
        TestRail.addStepName("After panel is powered on");
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(SET_UP_ACTIVPANEL);
        ElementHelper.waitUntilPresent(SKIP_SET_UP);
        TestRail.addStepName("Click Skip Set Up button");
        ElementHelper.clickWhenEnabled(SKIP_SET_UP);
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(BACK);
        ElementHelper.waitUntilPresent(ACCEPT);
        TestRail.addStepName("Click the Accept button");
        ElementHelper.clickWhenEnabled(ACCEPT);
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_time")));
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_date")));
    }

    /**C122095 Verify that user can upgrade panel online while connected to the network
     * Steps
     * 1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     * 2. After panel is powered on
     * 3. Click "Set Up ActivPanel" button
     * 4. Click Continue button in the Select Language and Select TimeZone page
     * 5. Click on the drop down, select and connect to a WIFI
     * 6.Click Continue button to enter Update ActivPanel page
     * 7.Click "Online Update Now" button to start the online update
     * 8. After reboot
     * 9. Click the Finished button
     * 10.Click Accept button
     * Author:Sita
     */
    @Test(groups = {"P1","UserDebug"})
    public void C122095VerifyThatUserCanUpgradePanelOnline(){
        TestRail.setTestRailId("122095");

        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        oobePO.startOOBE();
        TestRail.addStepName("After panel is powered on");
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        TestRail.addStepName("Click \"Set Up ActivPanel\" button");
        ElementHelper.clickWhenEnabled(SET_UP_ACTIVPANEL);
        // show EULA page
        TestRail.addStepName("Click Continue button in the Select Language and Select TimeZone page");
        ElementHelper.clickWhenEnabled(CONTINUE);
        ElementHelper.isPresent(CONTINUE,10);
        ElementHelper.clickWhenEnabled(CONTINUE);
        //Skipping step 5 and 6 because the network connect page does not appear when ethernet is used
        TestRail.addStepName("Click on the drop down, select and connect to a WIFI");
        TestRail.addStepName("Click Continue button to enter Update ActivPanel page");
        TestRail.addStepName("Click \"Online Update Now\" button to start the online update");
        AndroidElement ele = AppiumHelper.findElement("//*[@text='Online Update Now']");
        if (ElementHelper.isPresent(By.xpath("//*[@text='The latest updates have already been installed']"),10)) {
            Log.info("The panel is latest version.");
            ElementHelper.clickWhenEnabled(Locator.byText("Skip"));
            ElementHelper.clickWhenEnabled(ACCEPT);
            return;
        }
        ElementHelper.clickWhenEnabled(Locator.byText("Online Update Now"));
        TestRail.addStepName("After reboot");
        //Update Installation Complete message
        AppiumHelper.findElement("//*[@text='Update Installed Successfully']",3600);
        try {
            ElementHelper.clickWhenEnabled(Locator.byText("Restart Now"));
        } catch (Exception e) {}
        AppiumHelper.waitForSeconds(60);
        AppiumHelper.checkAndConnectDevice();
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update Installation Complete"),150));
        TestRail.addStepName("Click the Finished button");
        ElementHelper.clickWhenEnabled(FINISHED);
        TestRail.addStepName("Click Accept button");
        //The Update Installation Complete message closes, EULA appears with only Accept button
        ElementHelper.waitUntilPresent(ACCEPT);
        ElementHelper.clickWhenEnabled(ACCEPT);
        ElementHelper.waitUntilPresent(Locator.byTextContains("Welcome"),10);
        AppiumHelper.clickAt(0.8,0.8);
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_time")));
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_date")));
    }

    /**C122096 Verify that users can upgrade the panel locally
     * Steps
     * 1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     * 2. After panel is powered on
     * 3. Click Continue button in the Select Language and Select TimeZone page, click Skip button in Network Connection page
     * 4. Click the Local Update button, choose a upgrade package then click Update button
     * 5.After reboot
     * 6. Click the Finished button
     * 7. Click Accept button
     * Author:Sita
     */
    @Test(groups = {"P1","UserDebug"})
    public void C122096VerifyThatUsersCanUpgradePanelLocally(){
        TestRail.setTestRailId("122096,120953");
        setTestCaseCleaner(oobePO::restoreAfterOOBE);
        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        oobePO.startOOBE();
        TestRail.addStepName("After panel is powered on");
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        //Skipping step 5 last line because the network connect page does not appear when ethernet is used
        TestRail.addStepName("Click Continue button in the Select Language and Select TimeZone page, click Skip button in Network Connection page");
        ElementHelper.clickWhenEnabled(SET_UP_ACTIVPANEL);
        // show EULA page
        ElementHelper.clickWhenEnabled(CONTINUE);
        ElementHelper.isPresent(CONTINUE,10);
        ElementHelper.clickWhenEnabled(CONTINUE);
        TestRail.addStepName("Click the Local Update button, choose a upgrade package then click Update button");
        ElementHelper.clickWhenEnabled(Locator.byText("Local Update"));
        if (!ElementHelper.isPresent(By.id("com.prometheanworld.update:id/tv_file_name"),2)) {
            Log.info("There is no ZIP file for update in USB");
            return;
        }
        ElementHelper.clickWhenEnabled(By.id("com.prometheanworld.update:id/tv_file_name"));
        ElementHelper.clickWhenEnabled(By.id("com.prometheanworld.update:id/btn_local_update"));
        TestRail.addStepName("After reboot");
        //Update Installation Complete message
        AppiumHelper.findElement("//*[@text='Update Installed Successfully']",3600);
        try {
            ElementHelper.clickWhenEnabled(Locator.byText("Restart Now"));
        } catch (Exception e) {}
        AppiumHelper.waitForSeconds(60);
        AppiumHelper.checkAndConnectDevice();
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update Installation Complete"),150));
        if (ElementHelper.isPresent(FINISHED)) {
            TestRail.addStepName("Click the Finished button");
            ElementHelper.clickWhenEnabled(FINISHED);
        }
        TestRail.addStepName("Click Accept button");
        //The Update Installation Complete message closes, EULA appears with only Accept button
        Assert.assertTrue(ElementHelper.isVisible(ACCEPT,10),"After the update installation complete message closes, EULA doesn't appears with only Accept Button");
        ElementHelper.clickWhenEnabled(ACCEPT);
        ElementHelper.waitUntilPresent(Locator.byTextContains("Welcome"),10);
        AppiumHelper.clickAt(0.8,0.8);
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_time")));
        ElementHelper.waitUntilPresent(By.id(String.format(BOOTMANAGER_ID_PREFIX, "tv_date")));
    }
}
