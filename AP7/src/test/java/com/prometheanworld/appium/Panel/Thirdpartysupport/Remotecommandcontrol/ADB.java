package com.prometheanworld.appium.Panel.Thirdpartysupport.Remotecommandcontrol;

import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.Panel.SystemFunction.SettingApp.DisplaySetting.AppIconDisplaySetting;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class ADB extends BaseTest {

    /** Description:  Change "Unified Menu" switch via API  MOD-07
     * 1. Set "Unified Menu" switch to off, use adb command: "adb shell setprop persist.settings.unified_launcher_visible false"
     * 2. Reboot panel
     * 3. Android Settings -> Display, verify the "Unified Menu" switch is off, the three carrots are not visible and doesn't display when touching the sides of the panel
     * 4. Set "Unified Menu" switch to on, use adb command: "adb shell setprop persist.settings.unified_launcher_visible true"
     * 5. Reboot panel
     * 6. Android Settings -> Display, verify the "Unified Menu" switch is on and the three carrots are visible and function
     */
    @Test
    public void Test_ChangeUnifiedMenuSwitchViaAPI(){
        TestRail.setTestRailId("83231");
        Log.info("-------------Test ChangeUnifiedMenuSwitchViaAPI Start---------------");
        AppIconDisplaySetting.setSwitchStateViaAPI("unified_launcher_visible",false);
        Log.info("Reboot the panel");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppIconDisplaySetting.checkMenuInSpecifySide("Bottom",false);
        AppIconDisplaySetting.setSwitchStateViaAPI("unified_launcher_visible",true);
        Log.info("Reboot the panel again");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppIconDisplaySetting.checkMenuInSpecifySide("Right",true);
        Log.info("-------------Test ChangeUnifiedMenuSwitchViaAPI Start---------------");
    }

    /** Description:  Change "Home Source" switch via API  MOD-15
     * 1. Set "Home Source" switch to off, use adb command: "adb shell setprop persist.settings.home_source false"
     * 2. Reboot panel
     * 3. Android Settings -> Display, verify the "Home Source" switch is off and the input source selection does not show a "Home" icon
     * 4. Set "Home Source" switch to on, use adb command: "adb shell setprop persist.settings.home_source true"
     * 5. Reboot panel
     * 6. Android Settings -> Display, verify the "Home Source" switch is on and the input source selection shows a "Home" icon
     */
    @Test
    public void Test_ChangeHomeSourceSwitchViaAPI(){
        TestRail.setTestRailId("83239");
        Log.info("-------------Test ChangeHomeSourceSwitchViaAPI Start---------------");
        AppIconDisplaySetting.setSwitchStateViaAPI("home_source",false);
        Log.info("Reboot the panel");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppIconDisplaySetting.checkHomeSourceStatus("Bottom",false);
        AppIconDisplaySetting.setSwitchStateViaAPI("home_source",true);
        Log.info("Reboot the panel again");
        AppIconDisplaySetting.rebootPanelAndResetServices();
        AppIconDisplaySetting.checkHomeSourceStatus("Right",true);
        Log.info("-------------Test ChangeHomeSourceSwitchViaAPI Start---------------");
    }
}
