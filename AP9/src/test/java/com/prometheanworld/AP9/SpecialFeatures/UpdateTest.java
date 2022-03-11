package com.prometheanworld.AP9.SpecialFeatures;

import com.nd.automation.core.command.Adb;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSystemPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseUpdatePO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UpdateTest extends BaseTest {
    private BaseUpdatePO updatePO = null;
    private static BaseSystemPO systemPO = null;;
    private String testRailID = null;

//    @Override
//    protected String testAppName() {
//        return "Update";
//    }

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        updatePO = POFactory.getInstance(BaseUpdatePO.class);
        systemPO = POFactory.getInstance(BaseSystemPO.class);
        //check is there a U disk
//        String usbPath = updatePO.getUpdateFilePathForPanel();
//        assertTrue(usbPath != null);
    }

    @Test
    public void UpdateTest() {
        //1. Perform OTA update
        Integer appiumTimes = 1;
        String bamboo_appiumTimes = System.getenv("bamboo_AppiumTimes");
        if (bamboo_appiumTimes != null && !bamboo_appiumTimes.equals("")) {
            appiumTimes = Integer.parseInt(bamboo_appiumTimes);
        }
        String updateURLs = System.getenv("bamboo_updateURL");
 //       String updateURLs = "http://172.24.140.120:545/AP9/master.1.0.0.294/userdebug/ota-mt9950.1.0.0.294.userdebug.zip";
        if (updateURLs == null) {
            updateURLs = "";
        }
        String[] updateURLList = updateURLs.split(",");
        for (int i=1;i<=appiumTimes;i++) {
            for (String updateURL : updateURLList){
                if (updateURL == null || updateURL.trim().equals("")) {
                    continue;
                }
                Log.info("["+i+"/"+appiumTimes+"] Update file:"+updateURL+";panel date:"+ Adb.adb("shell date"));
                systemPO.startAppFromUnifiedLauncher("Update");
                Boolean updateResult = updatePO.upgradeDowngradeThroughOTA(updateURL);
                Assert.assertTrue(updateResult);
            }
        }
    }
}
