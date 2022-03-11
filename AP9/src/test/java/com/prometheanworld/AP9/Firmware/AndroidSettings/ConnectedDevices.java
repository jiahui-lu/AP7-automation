package com.prometheanworld.AP9.Firmware.AndroidSettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.SubSystem;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConnectedDevices extends BaseTest {

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C92380 Connected Devices interface is displayed normally
     * Steps:
     *  1. On Menu bar click Application icon-> Settings->Connected Devices =>
     *     1. the Connected deivces interface have 2 options, which are Pair new devices, Connection preferences
     *     2. if the devices is 5680,the Connected connection preferences will have 1 options, which is Bluetooth.
     *        if the devices is 9950,the Connected connection will have 2 options, which are Bluetooth, NFC
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C92380ConnectedDevicesInterfaceIsDisplayedNormally() {
        TestRail.setTestRailId("92380");

        setTestCaseCleaner(() -> {
            systemPO.stopSettingsApp();
        });

        TestRail.addStepName("On Menu bar click Application icon-> Settings->Connected Devices");
        systemPO.startAppFromUnifiedLauncher("Settings");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "Connected devices");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Pair new device")),
                "The 'Pair new device' module should be visible");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Connection preferences")),
                "The 'Connection preferences' module should be visible");
        final String connectionSummaryPath = "//*[@text='Connection preferences']/../*[@resource-id='android:id/summary']";
        final String connectionSummaryText = ElementHelper.findElement(By.xpath(connectionSummaryPath)).getText();
        boolean is9950 = SubSystem.isMT9950();
        if (is9950) {
            Assert.assertEquals(connectionSummaryText, "Bluetooth, NFC");
        } else {
            Assert.assertEquals(connectionSummaryText, "Bluetooth");
        }
    }
}
