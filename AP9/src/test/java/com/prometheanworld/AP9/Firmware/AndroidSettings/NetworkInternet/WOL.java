package com.prometheanworld.AP9.Firmware.AndroidSettings.NetworkInternet;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;

public class WOL extends BaseTest {
    @Override
    protected String testAppName() {
        return "Settings";
    }

    private static final String WOL_PROPERTY = "persist.xbh.wol.enable";

    private String originWolValue;

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        AppiumHelper.exeAdbRoot();
        originWolValue = Adb.getAndroidProp(WOL_PROPERTY);
    }

    @BeforeMethod(alwaysRun = true)
    @Override
    public void beforeMethod() {
        super.beforeMethod();
        AppiumHelper.exeAdbRoot();
        Adb.setAndroidProp(WOL_PROPERTY, "");
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
        AppiumHelper.exeAdbRoot();
        if (originWolValue != null && !originWolValue.equals(Adb.getAndroidProp(WOL_PROPERTY))) {
            Log.debug("reset wol status to:" + originWolValue);
            Adb.setAndroidProp(WOL_PROPERTY, originWolValue);
            if ("true".equals(originWolValue)) {
                // reboot to make wol take effect
                AppiumHelper.rebootPanel();
            }
            originWolValue = null;
        }
    }

    /**
     * C113429 Wake On Lan can be enabled via Android settings
     *
     * Find a panel with fresh build: either bin flash it or do a factory reset after update
     * Open Settings -> Network => "Wake on network" can be turned on and off by user
     *
     */
    @Test(groups = {"P1"})
    public void C113429WakeOnNetworkCanBeSwitched() {
        TestRail.setTestRailId("113429");

        final String switchPath = "//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='Wake on network']]//*[ends-with(@resource-id, 'id/switch_widget')]";
        final String wakePath = "//android.widget.TextView[@text='Wake on network']";
        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='Network & internet']"));
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(switchPath)));
        TestRail.addStepName("Open Settings -> Network");
        ElementHelper.click(By.xpath(wakePath));
        Assert.assertTrue(ElementHelper.isChecked(By.xpath(switchPath)), "\"Wake on network\" should be turned on by user");
        ElementHelper.click(By.xpath(wakePath));
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(switchPath)), "\"Wake on network\" should be turned off by user");
    }

    /**
     * C113428 Wake On Lan should be off by default
     *
     * Find a panel with fresh build: either bin flash it or do a factory reset after update
     * Open Settings -> Network => "Wake on network" should be off by default
     *
     */
    @Test(groups = {"P1"})
    public void C113428WakeOnNetworkDefaultOff() {
        TestRail.setTestRailId("113428");

        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='Network & internet']"));
        TestRail.addStepName("Open Settings -> Network");
        String path = "//*[ends-with(@resource-id, 'id/list')]/*[descendant::*[@text='Wake on network']]//*[ends-with(@resource-id, 'id/switch_widget')]";
        Assert.assertFalse(ElementHelper.isChecked(By.xpath(path)), "\"Wake on network\" should be off by default");
    }
}
