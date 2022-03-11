package com.prometheanworld.AP9.Firmware;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseAVIPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseAbstractionTestPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class AbstractionTest extends BaseTest {
    private BaseAbstractionTestPO abstractionPO = null;
    private BaseAVIPO aviPO = null;
    private BaseSettingsPO settingsPO = null;

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    protected String testAppName() {
        return "Abstraction Test";
    }

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        abstractionPO = POFactory.getInstance(BaseAbstractionTestPO.class);
        aviPO = POFactory.getInstance(BaseAVIPO.class);
        settingsPO = POFactory.getInstance(BaseSettingsPO.class);
    }

    @Test(groups = {"P2","UserDebug"})
    public void C115338VerifyTheResetAppsAndDataAPI() {
        TestRail.setTestRailId("115338");

        AppiumHelper.exeAdbRoot();
        String fileName = "updateFile.txt";
        String pushFile = abstractionPO.getProjectPath()+"updateFile"+File.separator+fileName;
        String pushFolder = "/data/data/com.nd.abstractiontest";
        Log.info("adb push "+pushFile+" "+pushFolder);
        Adb.pushFile(pushFile, pushFolder);
        String files = String.join(" ", Adb.adb("shell ls "+pushFolder));
        Log.info(files);
        Assert.assertTrue(files.indexOf(fileName)>-1,"push file to panel failed."+files);

        abstractionPO.testAppAndDataRest();
        AppiumHelper.exeAdbRoot();
        files = String.join(" ", Adb.adb("shell ls "+pushFolder));
        Log.info(files);
        Assert.assertTrue(files.indexOf(fileName) == -1,"testAppAndDataRest failed."+files);
    }

//    @Test(groups = {"P2"})
    public void C115345VerifyTheSetWOLStatusAPI() {
        TestRail.setTestRailId("115345");

        abstractionPO.setWOLStatus(false);
        systemPO.startAppFromUnifiedLauncher("Settings");
        String wolStatus = settingsPO.getWOLStatus();
        ElementHelper.click(By.xpath("//*[@resource-id='android:id/close_window']"));
        Assert.assertEquals(wolStatus,"OFF","Get WOL Status failed.");

        abstractionPO.setWOLStatus(true);
        systemPO.startAppFromUnifiedLauncher("Settings");
        wolStatus = settingsPO.getWOLStatus();
        ElementHelper.click(By.xpath("//*[@resource-id='android:id/close_window']"));
        Assert.assertEquals(wolStatus,"ON","Get WOL Status failed.");
    }

//    @Test(groups = {"P2"})
    public void C115341VerifyTheGetWOLStatusAPI() {
        TestRail.setTestRailId("115341");

        systemPO.startAppFromUnifiedLauncher("Settings");
        settingsPO.setWOLStatus(true);
        ElementHelper.click(By.xpath("//*[@resource-id='android:id/close_window']"));
        String wolStatus = abstractionPO.getWOLStatus();
        Log.info("Get wolStatus result:"+wolStatus);
        Assert.assertEquals(wolStatus,"WOLStatus: true","Get WOL Status failed.");

        systemPO.startAppFromUnifiedLauncher("Settings");
        settingsPO.setWOLStatus(false);
        ElementHelper.click(By.xpath("//*[@resource-id='android:id/close_window']"));
        wolStatus = abstractionPO.getWOLStatus();
        Log.info("Get wolStatus result:"+wolStatus);
        Assert.assertEquals(wolStatus,"WOLStatus: false","Get WOL Status failed.");
    }

    @Test(groups = {"P2"})
    public void C120777VerifyTheGetBezelDataInfoAPI() {
        TestRail.setTestRailId("120777");

        String bezelData = abstractionPO.getBezelDataInfo();
        Log.info("Get bezel data result:"+bezelData);
        String[] checkStr = {"VendorId:","ProductId:","FirmwareVersion:","HardwareRevision:","SerialNumber:","description:"};
        for (String s : checkStr) {
            Assert.assertTrue(bezelData.indexOf(s) > -1, "Get Eraser/Pen Count failed,lack:" + s);
        }
    }

    @Test(groups = {"P2"})
    public void C120778VerifyTheGetEraserPenCountAPI() {
        TestRail.setTestRailId("120778");

        String eraserPenCount = abstractionPO.getEraserPenCount();
        Log.info("Get eraser pen count result:"+eraserPenCount);
        String[] checkStr = {"PenTypes:","touchCount:","MaxPenCount:","EraseCount:","blockEraseCount:"};
        for (String s : checkStr) {
            Assert.assertTrue(eraserPenCount.indexOf(s) > -1, "Get Eraser/Pen Count failed,lack:" + s);
        }
    }

    @Test(groups = {"P2"})
    public void C120781VerifyTheGetPalmRejectAPI() {
        TestRail.setTestRailId("120781");

        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.RemoteControl);
        aviPO.navToPanelSettings();
        aviPO.navToInteractionTab();
        aviPO.setPalmMode(BaseAVIPO.PalmMode.RejectPalm);
        AppiumHelper.clickAt(0.8, 0.8);
        String palmReject = abstractionPO.getPalmReject();
        Log.info("Get PalmReject result:"+palmReject);
        Assert.assertEquals(palmReject,"PalmReject: true");
        AppiumHelper.clickKey(PrometheanKey.VolumeDown, PrometheanKeyboard.RemoteControl);
        aviPO.navToPanelSettings();
        aviPO.navToInteractionTab();
        aviPO.setPalmMode(BaseAVIPO.PalmMode.Eraser);
        AppiumHelper.clickAt(0.8,0.8);
        palmReject = abstractionPO.getPalmReject();
        Log.info("Get PalmReject result:"+palmReject);
        Assert.assertEquals(palmReject,"PalmReject: false");
    }

    @Test(groups = {"P2"})
    public void C120782VerifyTheGetTouchLockedAPI() {
        TestRail.setTestRailId("120782");

        AppiumHelper.clickKey(PrometheanKey.Touch, PrometheanKeyboard.RemoteControl);
        String touchLocked = abstractionPO.getTouchLocked();
        Log.info("Get touch locked result:"+touchLocked);
        Assert.assertEquals(touchLocked,"TouchLock: true");
        AppiumHelper.clickKey(PrometheanKey.Touch, PrometheanKeyboard.RemoteControl);
        touchLocked = abstractionPO.getTouchLocked();
        Log.info("Get touch locked result:"+touchLocked);
        Assert.assertEquals(touchLocked,"TouchLock: false");
    }

    @Test(groups = {"P2"})
    public void C120783VerifyTheGetSizeAPI() {
        TestRail.setTestRailId("120783");

        String sz = abstractionPO.getSize();
        Log.info("Get size result:" + sz);
        Assert.assertTrue((sz.equals("Size: 65") || sz.equals("Size: 0")));
    }
}
