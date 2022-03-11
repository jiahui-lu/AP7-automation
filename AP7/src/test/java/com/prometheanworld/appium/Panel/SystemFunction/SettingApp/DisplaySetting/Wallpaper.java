package com.prometheanworld.appium.Panel.SystemFunction.SettingApp.DisplaySetting;

import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.IdentityPO;
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class Wallpaper extends BaseTest {

    IdentityPO identityPO = POFactory.getInstance(IdentityPO.class);
    SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);

    /** 1.login pannel as a owner
     *  2.setting -> display -> wallpaper -> wallpapers -> select a wallpaper
     *  3.change login to guest
     *  confirm that different wallpaper will display
     */
    @Test
    public void Test_LastSourceInAdvancedSetting(){
        TestRail.setTestRailId("120795");
        boolean bo = settingsPO.selectPaper(2);
        identityPO.changeOwnerToGustFromBottom();
        assertTrue(bo);
        identityPO.changeGustToOwnerFromBottom(false);
        settingsPO.selectPaper(1);
    }

}
