package com.prometheanworld.AP9.Firmware.SystemApps.LanguageInput;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP9.SettingsPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VirtualKeyboard extends BaseTest {
    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C120835 Verify that users can use G-Board normally when in full screen window
     * steps:
     * Step1:Launch Chrome and change window to full screen,the default url is deleted after delete button clicked
     * Step2:the text can be input successfully
     * Step3:page will start to search after search button clicked
     *
     * C120829 Verify that G-Board defaults to small floating keyboard
     * steps:
     * Step1: when Open Chromium home page -> enter text in the navigation bar,G-Board defaults to small floating keyboard
     * Author:lifeifei
     */
    @Test(groups = "P1")
    private void C120835C120829VerifyThatUsersCanUseGBoardNormallyWhenInFullScreenWindow() throws InterruptedException {
        TestRail.setTestRailId("120835,120829");

        TestRail.addStepName("Step1:the default url is deleted when click delete button");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        setTestCaseCleaner(() -> systemPO.closeAppOnMenuBar("Chromium"));
        ElementHelper.click(By.xpath("//android.widget.ImageButton[@content-desc=\"Maximize\"]"));
        AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").click();
        Thread.sleep(1000);
        systemPO.DeleteKeyOnGBoard();
        ElementHelper.waitUntilPresent(By.xpath("//android.widget.FrameLayout[1]/android.widget.EditText"));
        Assert.assertEquals(AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText(),"Search or type web address");

        TestRail.addStepName("Step2:the text is input successfully");
        systemPO.InputTextOnGBoard();
        Assert.assertNotEquals(AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText(),"Search or type web address");

        TestRail.addStepName("Step3:page will start to search when search button clicked");
        systemPO.TapSearchButtonOnGBoard();
        String str = AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText().substring(0,18);
        Assert.assertEquals(str,"https://www.google");
    }

    /**
     * C120836 Verify that users can use G-Board normally when in full screen window
     * steps:
     * Step1:Launch Chrome ,the default url is deleted after delete button clicked
     * Step2:the text can be input successfully
     * Step3:page will start to search after search button clicked
     * Author:lifeifei
     * date 2022/2/21
     */
    @Test(groups = "P1")
    private void C120836VerifyThatUsersCanUseGBoardNormallyWhenInFloatingScreenWindow() throws InterruptedException {
        TestRail.setTestRailId("120836");

        TestRail.addStepName("Step1:the default url is deleted when click delete button");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        setTestCaseCleaner(() -> systemPO.closeAppOnMenuBar("Chromium"));
        Thread.sleep(1000);
        AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").click();
        Thread.sleep(1000);
        systemPO.DeleteKeyOnGBoard();
        ElementHelper.waitUntilPresent(By.xpath("//android.widget.FrameLayout[1]/android.widget.EditText"));
        Assert.assertEquals(AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText(),"Search or type web address");

        TestRail.addStepName("Step2:the text is input successfully");
        systemPO.InputTextOnGBoard();
        Assert.assertNotEquals(AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText(),"Search or type web address");

        TestRail.addStepName("Step3:page will start to search when search button clicked");
        systemPO.TapSearchButtonOnGBoard();
        String str = AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText().substring(0,18);
        Assert.assertEquals(str,"https://www.google");
    }

    /**
     * C120915 Verify that the first preference keyboard language can change with system language
     * steps:
     * Step1:Android Settings > System > Languages & input > Languages->switch Lanaguage
     * Step2:Android Settings > System > Languages & input > Virtual Keyboard > Gboard > Languages,the first preference keyboard language changes with system language
     * Step3:Open Chromium home page -> enter text in the navigation bar, the first preference keyboard language changes with system language
     * Author:lifeifei
     * date 2022/2/23
     */
    @Test(groups = "P1")
    private void C120915VerifyThatTheFirstFreferenceKeyboardLanguageCanChangeWithSystemLanguage() throws InterruptedException {
        TestRail.setTestRailId("C120915");

        TestRail.addStepName("Step1:Android Settings > System > Languages & input > Languages->switch Lanaguage");
        systemPO.SwitchSystemLanguageToChinese();
        setTestCaseCleaner(() -> {
            try {
                systemPO.RemoveAllLanguageExceptEnglish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            systemPO.closeAppOnMenuBar("Chromium");
        });

        TestRail.addStepName("Step2:Android Settings > System > Languages & input > Virtual Keyboard > Gboard > Languages,the first preference keyboard language changes with system language");
        AppiumHelper.findElement("//android.widget.ImageButton[@content-desc=\"向上导航\"]\n").click();
        Thread.sleep(2000);
        AppiumHelper.findElement("//android.widget.LinearLayout[2]/android.widget.RelativeLayout").click();
        Thread.sleep(2000);
        AppiumHelper.findElement("//android.widget.LinearLayout[1]/android.widget.RelativeLayout").click();
        Thread.sleep(2000);
        AppiumHelper.findElement("//android.widget.LinearLayout[2]/android.widget.RelativeLayout").click();
        String attri = AppiumHelper.findElementsByXPath("//android.widget.RelativeLayout[@class='android.widget.RelativeLayout']").get(1).getTagName();
        Assert.assertEquals(attri,"中文（简体）, 拼音");

        TestRail.addStepName("Step3:Open Chromium home page -> enter text in the navigation bar, the first preference keyboard language changes with system language");
        systemPO.startAppFromUnifiedLauncher("Chromium");
        AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").click();
        Thread.sleep(2000);
        systemPO.InputTextOnGBoard();
        Assert.assertNotEquals(AppiumHelper.findElement("//android.widget.FrameLayout[1]/android.widget.EditText").getText(),"b");
    }
}



