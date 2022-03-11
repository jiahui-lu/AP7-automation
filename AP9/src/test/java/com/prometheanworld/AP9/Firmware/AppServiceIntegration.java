package com.prometheanworld.AP9.Firmware;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.ApkPackageName;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class AppServiceIntegration extends BaseTest {
    public static final By SETTINGS_DASHBOARD = Locator.byResourceId("id/dashboard_container");
    private static BaseSystemPO systemPO = null;

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        systemPO = POFactory.getInstance(BaseSystemPO.class);
    }

    @Test(groups= "P0")
    public void C120175VerifyUpdateAPK() {
        TestRail.setTestRailId("120175");

        //{{"apk name","app icon name","package name","select one element to check app has open","close the app"}}
        String[] apk = {"Update.apk","Update","com.prometheanworld.update","//*[@text='Update ActivPanel']","//*[@resource-id='com.prometheanworld.update:id/btn_clse']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void VerifyCameraAPK() {
//        TestRail.setTestRailId("");

        String[] apk = {"Camera.apk","Camera","com.android.camera2","//*[@text='No camera detected']","//*[@text='CLOSE']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120162VerifyActivityAPK() {
        TestRail.setTestRailId("120162");

        String[] apk = {"Activity.apk","Activities","com.prometheanworld.activity","//*[@text='Activity Player']","//*[@class='android.widget.ListView']/android.view.View[3]"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120163VerifyScreenCaptureAPK() {
        TestRail.setTestRailId("120163");

        String[] apk = {"ScreenCapture.apk", "Screen Capture", "com.prometheanworld.screencapture", "//*[@text='Screen Capture']", "//*[@text='Close']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120167VerifyWhiteboardAPK() {
        TestRail.setTestRailId("120167");

        String[] apk = {"Whiteboard.apk","Whiteboard","com.prometheanworld.whiteboard","","//*[@resource-id='android:id/close_window']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120168VerifyWelcomeScreenAPK() {
        TestRail.setTestRailId("120168");

        String[] apk = {"WelcomeScreen.apk","Welcome Screen","com.prometheanworld.welcomescreen","//*[@text='Hello']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120174VerifyAdobeAcrobatAPK() {
        TestRail.setTestRailId("120174");

        String[] apk = {"AdobeAcrobat.apk","Adobe Acrobat","com.adobe.reader","//*[@text='Welcome to Acrobat Reader']","//*[@resource-id='android:id/close_window']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120173VerifyVLCAPK() {
        TestRail.setTestRailId("120173");

        String[] apk = {"VLC.apk","VLC","org.videolan.vlc","","//*[@resource-id='android:id/close_window']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120172VerifyChromiumAPK() {
        TestRail.setTestRailId("120172");

        String[] apk = {"Chromium.apk", "Chromium", "org.chromium.chrome", "//*[@resource-id='org.chromium.chrome:id/url_bar']", "//*[@resource-id='android:id/close_window']"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[2].trim()) >= 0);
        systemPO.startAppFromUnifiedLauncher(apk[1]);
        if (!apk[3].equals("")) {
            MobileElement ele = ElementHelper.findElement(By.xpath(apk[3]));
            Assert.assertEquals(ele.getAttribute("package"),apk[2]);
        }
        if (apk.length >= 5){
            MobileElement eleClose = ElementHelper.findElement(By.xpath(apk[4]));
            Assert.assertEquals(eleClose.getAttribute("package"),apk[2]);
            eleClose.click();
        }
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120178VerifyLockerTest() {
        TestRail.setTestRailId("120178");

        String[] apk = {"Locker.apk","com.prometheanworld.locker"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);

        systemPO.startMenuBar();
        ElementHelper.click(By.xpath("//*[@text='Applications']"));
        List<MobileElement> eles = ElementHelper.findElements(Locator.byResourceIdAndText("com.prometheanworld.locker:id/tv_title", "Applications"), 5);
        Assert.assertEquals(eles.size(),1);
        Assert.assertEquals(eles.get(0).getAttribute("package"),apk[1]);
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120183VerifyLauncherTest() {
        TestRail.setTestRailId("120183");

        String[] apk = {"Launcher.apk","com.prometheanworld.unifiedlauncher"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);

        systemPO.startMenuBar();
        List<MobileElement> eles = ElementHelper.findElements(Locator.byText("Applications"), 5);
        Assert.assertEquals(eles.size(),1);
        Assert.assertEquals(eles.get(0).getAttribute("package"),apk[1]);
        ScreenHelper.clickAt(0.8,0.8);//close unified launcher
    }

    @Test(groups= "P0")
    public void C120176VerifySourcesTest() {
        TestRail.setTestRailId("120176");

        String[] apk = {"Sources.apk","com.prometheanworld.sources"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);

        systemPO.startSourceApp();
        List<MobileElement> eles = ElementHelper.findElements(Locator.byText("Sources"), 5);
        Assert.assertEquals(eles.size(),1);
        Assert.assertEquals(eles.get(0).getAttribute("package"),apk[1]);
        ScreenHelper.clickAt(0.8,0.8);
    }

    @Test(groups= "P0")
    public void C120177VerifyAVISettingsTest() {
        TestRail.setTestRailId("120177");

        String[] apk = {"AVISettings.apk", "com.prometheanworld.avisettings"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:" + apk[1].trim()) >= 0);

        List<MobileElement> eles = null;
        for (int i = 0; i < 5; i++) {
            systemPO.startVolumeApp(true);
            eles = ElementHelper.findElements(By.id("com.prometheanworld.avisettings:id/volume_bar"), 5);
            if (eles.size() == 0) {
                Log.info("Start volume error " + i);
                AppiumHelper.waitForSeconds(2);
            } else {
                break;
            }
        }
        Assert.assertEquals(eles.size(), 1);
        Assert.assertEquals(eles.get(0).getAttribute("package"), apk[1]);
        systemPO.startVolumeApp(false);
    }

    @Test(groups="P0")
    public void C120159VerifySchemaSettingsAPK() {
        TestRail.setTestRailId("120159");

        String[] apk = {"SchemaSettings.apk","com.prometheanworld.schemasettings"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups="P0")
    public void C120161VerifyAnnotateAPK() {
        TestRail.setTestRailId("120161");

        String[] apk = {"Annotate.apk","com.prometheanworld.annotate"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups="P0")
    public void C120164VerifyScreenShareReceiverAPK() {
        TestRail.setTestRailId("120164");

        String[] apk = {"ScreenShareReceiver.apk","com.nd.promethean.casting.receiver"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120165VerifySpinnerAPK() {
        TestRail.setTestRailId("120165");

        String[] apk = {"Spinner.apk","com.prometheanworld.spinner"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120166VerifyTimerAPK() {
        TestRail.setTestRailId("120166");

        String[] apk = {"Timer.apk","com.prometheanworld.timer"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120182VerifyAP9ButtonServicesAPK() {
        TestRail.setTestRailId("120182");

        String[] apk = {"AP9ButtonServices.apk","com.prometheanworld.panelbuttonservice"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120181VerifySGboardAPK() {
        TestRail.setTestRailId("120181");

        String[] apk = {"SGboard.apk", "com.google.android.inputmethod.latin"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120169VerifyPanelManagementIoTAgentAPK() {
        TestRail.setTestRailId("120169");

        String[] apk = {"PanelManagementIoTAgent.apk","com.nd.promethean.mdmagent"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120179VerifyUSBRelayAPK() {
        TestRail.setTestRailId("120179");

        String[] apk = {"USBRelay.apk","com.prometheanworld.usbrelay"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120180VerifyUSBRelayProviderAPK() {
        TestRail.setTestRailId("120180");

        String[] apk = {"USBRelayProvider.apk","com.prometheanworld.usbrelayprovider"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120160VerifyCloudCouplerAPK() {
        TestRail.setTestRailId("120160");

        String[] apk = {"CloudCoupler.apk","com.prometheanworld.cloudcoupler"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void C120170VerifyIdentityAPK() {
        TestRail.setTestRailId("120170");

        String[] apk = {"Identity.apk","com.prometheanworld.identity"};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    @Test(groups= "P0")
    public void VerifyPanelManagementAPK() {
//        TestRail.setTestRailId("");

        String[] apk = {"PanelManagement.apk", ApkPackageName.PanelManagementIoTAgent.packageName};
        String packages = systemPO.listPackages();
        Assert.assertTrue(packages.indexOf("package:"+apk[1].trim()) >= 0);
    }

    /**C120086 Verify that system integration with OOBE app
     * Steps
     * 1. Find a panel with fresh build: either bin flash it or do a factory reset after update
     * 2. After panel is powered on
     * 3. Click "Set Up ActivePanel" button
     * Author:Sita
     */
    //@Test(groups= "P0")
    public void C120086SystemIntegrationWithOOBEApp() throws InterruptedException {
        TestRail.setTestRailId("120086");
        TestRail.addStepName("Find a panel with fresh build: either bin flash it or do a factory reset after update");
        systemPO.startAppFromUnifiedLauncher("Settings");
        ElementHelper.click(By.xpath("//android.widget.ImageButton[@content-desc='Maximize']"));
        final By system = Locator.byText("System");
        ElementHelper.scrollUntilPresent(SETTINGS_DASHBOARD, system);
        ElementHelper.click(system);
        final By resetOptions = Locator.byText("Reset options");
        ElementHelper.click(resetOptions);
        final By factoryReset = Locator.byText("Erase all data (factory reset)");
        ElementHelper.click(factoryReset);
        AppiumHelper.findElementBy(By.id("com.android.settings:id/initiate_master_clear")).click();
        AppiumHelper.findElementBy(By.id("com.android.settings:id/execute_master_clear")).click();
        Thread.sleep(180000);
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"),500);

        TestRail.addStepName("After panel is powered on"); //Verifying that out of box instructions displays with two buttons: "Skip Set Up" and "Set Up ActivPanel"
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.oobe:id/btnSkip"),10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.oobe:id/btnSetUp"),10));

        TestRail.addStepName("Click \"Set Up ActivePanel\" button");//Starting the OOBE flow
        AppiumHelper.findElementBy(By.id("com.prometheanworld.oobe:id/btnSetUp")).click();
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.oobe:id/tvLanguage"),10));
        final By Continue = Locator.byText("Continue");
        ElementHelper.click(Continue);
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.oobe:id/btn_back"),10));
        ElementHelper.click(Continue);
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.update:id/btn_skip"),10));
        AppiumHelper.findElementBy(By.id("com.prometheanworld.update:id/btn_skip")).click();
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.eula:id/btnAccept"),10));
        AppiumHelper.findElementBy(By.id("com.prometheanworld.eula:id/btnAccept")).click();
    }

    /**C120184 Verify that system integration with SettingsConfiguration app
     * Steps
     * 1. Open Settings->System->Advanced
     * 2. Click on Import/export configuration
     * Author:Sita
     */
    @Test(groups= "P0")
    public void C120184SystemIntegrationWithSettingsConfigurationApp(){
        TestRail.setTestRailId("120184");
        TestRail.addStepName("Open Settings->System->Advanced");
        systemPO.startAppFromUnifiedLauncher("Settings");
        ElementHelper.click(By.xpath("//android.widget.ImageButton[@content-desc='Maximize']"));
        final By system = Locator.byText("System");
        ElementHelper.scrollUntilPresent(SETTINGS_DASHBOARD, system);
        ElementHelper.click(system);
        TestRail.addStepName("Click on Import/export configuration");
        final By importExport = Locator.byText("Import/export configuration");
        if (ElementHelper.isPresent(Locator.byText("Advanced"),3)) {
            ElementHelper.click(Locator.byText("Advanced"));
        }
        ElementHelper.click(importExport);
        //Verifying that Import/ Export Configuration window is displayed
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.settingsconfiguration:id/import_button"),10));
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.prometheanworld.settingsconfiguration:id/export_button"),10));
        AppiumHelper.findElementBy(By.id("com.prometheanworld.settingsconfiguration:id/cancel_button")).click();
        systemPO.closeAppOnMenuBar("Settings");
    }

    /**C120189 Verify that PrebuiltGmsCore.apk is removed
     * Steps
     * 1. Use adb command to connect panel：adb connect <IPAddress>
     * 2. Use the ADB command to see if the PrebuiltGmsCore package is included in the list:
     *    adb shell pm list packages | grep com.google.android.gms
     * Author:Sita
     */
    @Test(groups= "P0")
    public void C120189PrebuiltGmsCoreApkIsRemoved(){
        TestRail.setTestRailId("120189");
        TestRail.addStepName("Use adb command to connect panel：adb connect <IPAddress>");
        TestRail.addStepName("Use the ADB command to see if the PrebuiltGmsCore package is included in the list:\n"+
                "adb shell pm list packages | grep com.google.android.gms");
        String packageName = CommonOperator.executeShellCommand("pm list packages","");
        System.out.println(packageName);
        Assert.assertFalse(packageName.contains("package:com.google.android.gms"));
    }

    /**C120191 Verify that DosBox.apk is removed
     * Steps
     * 1. Use adb command to connect panel：adb connect <IPAddress>
     * 2. Use the ADB command to see if the DosBox package is included in the list:
     *    adb shell pm list packages | grep com.fishstix.dosboxfree
     * Author:Sita
     */
    @Test(groups= "P0")
    public void C120191DosBoxApkIsRemoved(){
        TestRail.setTestRailId("120191");
        TestRail.addStepName("Use adb command to connect panel：adb connect <IPAddress>");
        TestRail.addStepName("Use the ADB command to see if the DosBox package is included in the list:\n" +
                "adb shell pm list packages | grep com.fishstix.dosboxfree");
        String packageName = CommonOperator.executeShellCommand("pm list packages","");
        System.out.println(packageName);
        Assert.assertFalse(packageName.contains("package:com.fishstix.dosboxfree"));
    }

    /**C120190 Verify that GoogleServicesFramework.apk is removed
     * Steps
     * 1. Use adb command to connect panel：adb connect <IPAddress>
     * 2. Use the ADB command to see if the GoogleServicesFramework package is included in the list:
     *    adb shell pm list packages | grep com.google.android.gsf
     * Author:Sita
     */
    @Test(groups= "P0")
    public void C120190GoogleServicesFrameworkApkIsRemoved(){
        TestRail.setTestRailId("120190");
        TestRail.addStepName("Use adb command to connect panel：adb connect <IPAddress>");
        TestRail.addStepName("Use the ADB command to see if the GoogleServicesFramework package is included in the list:\n" +
                "adb shell pm list packages | grep com.google.android.gsf");
        String packageName = CommonOperator.executeShellCommand("pm list packages","");
        System.out.println(packageName);
        Assert.assertFalse(packageName.contains("package:com.google.android.gsf"));
    }

    /**C120330 Verify that system integration with Gallery app
     *Steps
     * 1. Open Locker
     * 2. Click on Gallery
     * Author:Sita
     */
    @Test(groups= "P0")
    public void C120330SystemIntegrationWithGalleryApp (){
        TestRail.setTestRailId("120330");
        TestRail.addStepName("Open Locker");
        AppiumHelper.showTaskbar();
        AppiumHelper.findElementBy(By.xpath("//android.widget.Button[@content-desc=\"Apps\"]")).click();
        TestRail.addStepName("Click on Gallery");
        final By gallery = Locator.byText("Gallery");
        ElementHelper.click(gallery);
        Assert.assertTrue(AppiumHelper.isElementVisible(null,By.id("com.android.gallery3d:id/gl_root_view"),10));
        systemPO.closeAppOnMenuBar("Gallery");
    }
}