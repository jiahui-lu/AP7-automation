package com.prometheanworld.appium.Panel.VersionUpdate;

import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.ApiBase.BaseLockerPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseUpdatePO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.Map;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class OTAUpdate extends BaseTest {
    private BaseUpdatePO updatePO = null;
    private BaseLockerPO lockerPO = null;

    @Override
    protected String testAppName() {
        return "Update";
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        updatePO = POFactory.getInstance(BaseUpdatePO.class);
        lockerPO = POFactory.getInstance(BaseLockerPO.class);
        //check is there a U disk
//        String usbPath = updatePO.getUpdateFilePathForPanel();
//        assertTrue(usbPath != null,"Lack of U disk, skip the upgrade case.");
    }

    /*With USB stick plugged in AP7 and the current version is latest OTA build
    Unified Launcher --> Open "Locker" --> Open "Update" app --> "Local Update"
    Choose the OTA-1 .zip
    Downgraded to OTA-1  (this version don't change bezel version)
     */
    @Test
    public void OTAU_03DowngradeBezelVersionNotChange() {
        TestRail.setTestRailId("83509");

        Map<String,String> beforVersion = null;
        TestRail.addStepName("Perform OTA update With USB stick");
        String updateURLs = "http://172.24.140.120:545/Lango/releasebuild.RB_5.1.200_future.5.1.200.15/user/ClassB-ota-RB_5.1.200_future-Scaler5.1.200.15-Bezel6.4.73.42-8.13.74.42-9.13.74.42-user.zip";
        String[] updateURLList = updateURLs.split(",");
        for (int i=0;i<updateURLList.length;i++) {
            if (i == updateURLList.length-1) {
                beforVersion = updatePO.getVersionInfo();
            }
            Log.info("Update file:"+updateURLList[i]);
            AppiumHelper.StartAppFromUnifiedLauncher(testAppName());
            updatePO.upgradeDowngradeThroughOTA(updateURLList[i]);
        }
        Map<String,String> afterVersion = updatePO.getVersionInfo();
        Map<String,String> lockerVersion = lockerPO.getVersionInfo();
        TestRail.addStepName("Verify mainboard version and bezel version on Locker and Update app");
        Assert.assertNotEquals(beforVersion.get("Mainboard"),afterVersion.get("Mainboard"));
        Assert.assertEquals(beforVersion.get("Bezel"),afterVersion.get("Bezel"));
        Assert.assertNotEquals(beforVersion.get("Mainboard"),lockerVersion.get("Mainboard"));
        Assert.assertEquals(beforVersion.get("Bezel"),lockerVersion.get("Bezel"));
    }

    @Test
    public void OTAU_04DowngradeBezelVersionChange() {
        TestRail.setTestRailId("116227");

        Map<String,String> beforVersion = null;
        TestRail.addStepName("Perform OTA update With USB stick");
        String updateURLs = "";
        String[] updateURLList = updateURLs.split(",");
        for (int i=0;i<updateURLList.length;i++) {
            if (i == updateURLList.length-1) {
                beforVersion = updatePO.getVersionInfo();
            }
            Log.info("Update file:"+updateURLList[i]);
            AppiumHelper.StartAppFromUnifiedLauncher(testAppName());
            updatePO.upgradeDowngradeThroughOTA(updateURLList[i]);
        }
        Map<String,String> afterVersion = updatePO.getVersionInfo();
        Map<String,String> lockerVersion = lockerPO.getVersionInfo();
        TestRail.addStepName("Verify mainboard version and bezel version on Locker and Update app");
        Assert.assertNotEquals(beforVersion.get("Mainboard"),afterVersion.get("Mainboard"));
        Assert.assertNotEquals(beforVersion.get("Bezel"),afterVersion.get("Bezel"));
        Assert.assertNotEquals(beforVersion.get("Mainboard"),lockerVersion.get("Mainboard"));
        Assert.assertNotEquals(beforVersion.get("Bezel"),lockerVersion.get("Bezel"));
    }

    @Test
    public void OTAU_05UpdateBezelVersionChange() {
        TestRail.setTestRailId("116226");

        Map<String,String> beforVersion = null;
        TestRail.addStepName("Perform OTA update With USB stick");
        String updateURLs = "";
        String[] updateURLList = updateURLs.split(",");
        for (int i=0;i<updateURLList.length;i++) {
            if (i == updateURLList.length-1) {
                beforVersion = updatePO.getVersionInfo();
            }
            Log.info("Update file:"+updateURLList[i]);
            AppiumHelper.StartAppFromUnifiedLauncher(testAppName());
            updatePO.upgradeDowngradeThroughOTA(updateURLList[i]);
        }
        Map<String,String> afterVersion = updatePO.getVersionInfo();
        Map<String,String> lockerVersion = lockerPO.getVersionInfo();
        TestRail.addStepName("Verify mainboard version and bezel version on Locker and Update app");
        Assert.assertNotEquals(beforVersion.get("Mainboard"),afterVersion.get("Mainboard"));
        Assert.assertNotEquals(beforVersion.get("Bezel"),afterVersion.get("Bezel"));
        Assert.assertNotEquals(beforVersion.get("Mainboard"),lockerVersion.get("Mainboard"));
        Assert.assertNotEquals(beforVersion.get("Bezel"),lockerVersion.get("Bezel"));
    }

    @Test
    public void OTAU_06UpdateBezelVersionChange() {
        TestRail.setTestRailId("116231");

        Map<String,String> beforVersion = null;
        TestRail.addStepName("Perform OTA update With USB stick");
        String updateURLs = "";
        String[] updateURLList = updateURLs.split(",");
        for (int i=0;i<updateURLList.length;i++) {
            if (i == updateURLList.length-1) {
                beforVersion = updatePO.getVersionInfo();
            }
            Log.info("Update file:"+updateURLList[i]);
            AppiumHelper.StartAppFromUnifiedLauncher(testAppName());
            updatePO.upgradeDowngradeThroughOTA(updateURLList[i]);
        }
        Map<String,String> afterVersion = updatePO.getVersionInfo();
        Map<String,String> lockerVersion = lockerPO.getVersionInfo();
        TestRail.addStepName("Verify mainboard version and bezel version on Locker and Update app");
        Assert.assertNotEquals(beforVersion.get("Mainboard"),afterVersion.get("Mainboard"));
        Assert.assertNotEquals(beforVersion.get("Bezel"),afterVersion.get("Bezel"));
        Assert.assertNotEquals(beforVersion.get("Mainboard"),lockerVersion.get("Mainboard"));
        Assert.assertNotEquals(beforVersion.get("Bezel"),lockerVersion.get("Bezel"));
    }

    @Test
    public void OTAU_07UpdateBezelVersionNotChange() {
        TestRail.setTestRailId("116232");

        Map<String,String> beforVersion = null;
        TestRail.addStepName("Perform OTA update With USB stick");
        String updateURLs = "";
        String[] updateURLList = updateURLs.split(",");
        for (int i=0;i<updateURLList.length;i++) {
            if (i == updateURLList.length-1) {
                beforVersion = updatePO.getVersionInfo();
            }
            Log.info("Update file:"+updateURLList[i]);
            AppiumHelper.StartAppFromUnifiedLauncher(testAppName());
            updatePO.upgradeDowngradeThroughOTA(updateURLList[i]);
        }
        Map<String,String> afterVersion = updatePO.getVersionInfo();
        Map<String,String> lockerVersion = lockerPO.getVersionInfo();
        TestRail.addStepName("Verify mainboard version and bezel version on Locker and Update app");
        Assert.assertNotEquals(beforVersion.get("Mainboard"),afterVersion.get("Mainboard"));
        Assert.assertEquals(beforVersion.get("Bezel"),afterVersion.get("Bezel"));
        Assert.assertNotEquals(beforVersion.get("Mainboard"),lockerVersion.get("Mainboard"));
        Assert.assertEquals(beforVersion.get("Bezel"),lockerVersion.get("Bezel"));
    }

    @Test
    public void OTAU_08UpdateVersionFromCurrentToCurrent() {
        TestRail.setTestRailId("83510");

        Map<String,String> beforVersion = null;
        TestRail.addStepName("Perform OTA update With USB stick");
        String updateURLs = "";
        String[] updateURLList = updateURLs.split(",");
        for (int i=0;i<updateURLList.length;i++) {
            if (i == updateURLList.length-1) {
                beforVersion = updatePO.getVersionInfo();
            }
            Log.info("Update file:"+updateURLList[i]);
            AppiumHelper.StartAppFromUnifiedLauncher(testAppName());
            updatePO.upgradeDowngradeThroughOTA(updateURLList[i]);
        }
        Map<String,String> afterVersion = updatePO.getVersionInfo();
        Map<String,String> lockerVersion = lockerPO.getVersionInfo();
        TestRail.addStepName("Verify mainboard version and bezel version on Locker and Update app");
        Assert.assertEquals(beforVersion.get("Mainboard"),afterVersion.get("Mainboard"));
        Assert.assertEquals(beforVersion.get("Bezel"),afterVersion.get("Bezel"));
        Assert.assertEquals(beforVersion.get("Mainboard"),lockerVersion.get("Mainboard"));
        Assert.assertEquals(beforVersion.get("Bezel"),lockerVersion.get("Bezel"));
    }

}
