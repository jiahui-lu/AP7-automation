package com.prometheanworld.AP9.Firmware.AndroidSettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class Apps extends BaseTest {

    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
    }

    /**
     * C92381 Apps interface is displayed normally
     * Steps:
     *  1. On Menu bar click Application icon -> Settings->Apps => the app interface will show Recently opened apps,
     *  and have 4 options, which are See all xx apps,Default apps, App permissions,Special app access
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C92381AppsInterfaceIsDisplayedNormally() {
        TestRail.setTestRailId("92381");

        final String[] modules = {
            "Default apps",
            "App permissions",
            "Special app access"
        };
        TestRail.addStepName("On Menu bar click Application icon -> Settings->Apps");
        Adb.forceStop(ANDROID_SETTINGS_PACKAGE);
        systemPO.startAppFromUnifiedLauncher("Settings");
        By path = Locator.byText("Apps");
        Assert.assertTrue(
                ElementHelper.isVisible(path, 2),
                "Settings app should be opened"
        );
        ElementHelper.click(path);
        boolean appExist = CommonOperator.scrollAndFind("com.android.settings:id/list", "App info");
        if (!appExist) {
            appExist = CommonOperator.scrollAndFind("com.android.settings:id/list",
                    "Recently opened apps");
        }
        Assert.assertTrue(appExist, "'App info' or 'See all xx apps' should be displayed");
        for (String module : modules) {
            Assert.assertTrue(CommonOperator.scrollAndFind("com.android.settings:id/list", module),
                    "The module '" + module + "' should be displayed");
        }
    }
}
