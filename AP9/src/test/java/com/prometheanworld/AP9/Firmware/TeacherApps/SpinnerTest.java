package com.prometheanworld.AP9.Firmware.TeacherApps;

import com.nd.automation.core.command.Adb;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import org.testng.annotations.Test;

public class SpinnerTest extends BaseTest {

    /**C112853 Spinner App should launch and run properly
     * Steps
     * 1. Menu -> Applications -> open Spinner app
     * Author:Sita */
    @Test(groups = "P1")
    public void C112853SpinnerAppShouldLaunchAndRunProperly(){
        TestRail.setTestRailId("112853");
        TestRail.addStepName(" Menu -> Applications -> open Spinner app ");
        systemPO.startAppFromUnifiedLauncher("Spinner");
        AppiumHelper.waitForSeconds(5);
        Adb.forceStop("com.prometheanworld.spinner");
    }
}