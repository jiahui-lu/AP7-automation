package com.prometheanworld.AP9.Firmware.TeacherApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.util.CommonOperator;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class ScreenShareTest extends BaseTest {

    /**C112854 Screen Share should launch and run properly
     * Steps
     * 1. Taskbar -> Applications -> Open Screen Share app
     * 2. You can share your screen via myPromethean app or other means
     * Author:Sita */
    @Test(groups = "P1")
    public void C112854ScreenShareShouldLaunchAndRunProperly(){
        TestRail.setTestRailId("112854");
        TestRail.addStepName(" Taskbar -> Applications -> Open Screen Share app");
        systemPO.startAppFromUnifiedLauncher("Screen Share");

        TestRail.addStepName(" You can share your screen via myPromethean app or other means");
        if(ElementHelper.isVisible(Locator.byText("ALLOW"))==true){
            ElementHelper.clickWhenVisible(Locator.byText("ALLOW"));
            ElementHelper.clickWhenVisible(Locator.byText("ALLOW"));}
        AppiumHelper.waitForSeconds(5);
        CloseApps("Screen Share","com.nd.promethean.casting.receiver");
        if(ElementHelper.isVisible(Locator.byText("CLOSE"))==true){
        ElementHelper.clickWhenVisible(By.xpath(systemPO.eleCloseApp));}
    }

    private void CloseApps(String appName, String packageName) {
        if (appName.equals("Spinner") || appName.equals("Timer") || appName.equals("Annotate") || appName.equals("Screen Share") || appName.equals("Cloud Connect")) {
            CommonOperator.executeShellCommand("am force-stop ", packageName);
        } else {
            systemPO.closeAppOnMenuBar(appName);
        }
        Log.info("-------------Close " + appName +"---------------");
        AppiumHelper.hideUnifiedMenu();
    }
}