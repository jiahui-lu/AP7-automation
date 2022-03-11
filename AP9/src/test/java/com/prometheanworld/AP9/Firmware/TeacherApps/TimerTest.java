package com.prometheanworld.AP9.Firmware.TeacherApps;

import com.nd.automation.core.command.Adb;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import org.testng.annotations.Test;

public class TimerTest extends BaseTest {
    /**C112852 Timer should launch and run properly
     * Steps
     * 1.Timer should launch and run properly
     * Author:Sita */
    @Test(groups = "P1")
    public void C112852TimerShouldLaunchAndRunProperly(){
        TestRail.setTestRailId("112852");
        TestRail.addStepName("Menu -> Applications -> Open Timer app");
        systemPO.startAppFromUnifiedLauncher("Timer");
        AppiumHelper.waitForSeconds(5);
        Adb.forceStop("com.prometheanworld.timer");
    }
}