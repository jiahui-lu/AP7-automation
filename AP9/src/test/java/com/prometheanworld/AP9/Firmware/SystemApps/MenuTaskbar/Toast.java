package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.model.AP9.SettingsPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class Toast extends BaseTest {
    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C119985 Verify that toast pops up when clicking build number
     * steps:
     * 1.when clicking build number toast 'No need, you are already a developer.' will pops up
     * Author:lifeifei
     * date 2022/2/16
     */
    @Test(groups = "P2")
    private void C119985VerifyThatToastPopsUpWhenClickingBuildNumber() {
        TestRail.setTestRailId("119985");

        TestRail.addStepName("Step1:launch Settings->System->About ActivPanel");
        systemPO.EnterAboutActivPanelPage();

        TestRail.addStepName("Step2:Click on the build number ");
        ElementHelper.click(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[8]"));
        AppiumHelper.findElement("//*[contains(@text,'No need, you are already a developer.')]").getText();
        Assert.assertEquals(AppiumHelper.findElement("//*[contains(@text,'No need, you are already a developer.')]").getText(), "No need, you are already a developer.");
        systemPO.closeAppOnMenuBar("Settings");
    }

}

