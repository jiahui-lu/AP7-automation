package com.prometheanworld.AP9.Firmware.AndroidSettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.enumdata.OptionsUnderAccessibility;
import com.prometheanworld.appium.frame.model.AP9.SettingsPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;

public class Accessibility extends BaseTest {
    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C92386 Accessibilty interface is displayed normally
     * steps:
     * Step1:On Menu bar click Application icon -> Settings->Accessibilty
     * Step2:Check the options under accessibilty, text-to-speech output, font size, display size, magnification, dwell timing,touch&hold delay, mono audio, captions, high contrast text, color correction, color inversion are existed
     * Author:lifeifei
     */
    @Test(groups = "P2")
    private void C92386VerifyThatAccessibilityInterfaceIsDisplayedNormally() {
        TestRail.setTestRailId("C92386");

        TestRail.addStepName("On Menu bar click Application icon -> Settings->Accessibilty");
        systemPO.SetFontSize("Small");
        systemPO.startAppFromUnifiedLauncher("Settings");
        setTestCaseCleaner(() -> {
            systemPO.closeAppOnMenuBar("Settings");
            systemPO.SetFontSize("Default");
        });
        ElementHelper.click(By.xpath("//android.widget.ImageButton[@content-desc=\"Maximize\"]"));

        AppiumHelper.findElement("//android.widget.LinearLayout[8]/android.widget.LinearLayout").click();

        TestRail.addStepName("step2: Check the options under accessibilty");
        String[] str = systemPO.getAllElementsText("//android.widget.RelativeLayout/android.widget.TextView");
        Assert.assertEquals(20, str.length);
        List<String> list = Arrays.asList(str);
        int flag = list.indexOf(OptionsUnderAccessibility.TextToSpeechOutput.options);
        int flag1 = list.indexOf(OptionsUnderAccessibility.FontSize.options);
        int flag2 = list.indexOf(OptionsUnderAccessibility.DisplaySize.options);
        int flag3 = list.indexOf(OptionsUnderAccessibility.Magnification.options);
        int flag4 = list.indexOf(OptionsUnderAccessibility.DwellTiming.options);
        int flag5 = list.indexOf(OptionsUnderAccessibility.TouchHoldDelay.options);
        int flag6 = list.indexOf(OptionsUnderAccessibility.MonoAudio.options);
        int flag7 = list.indexOf(OptionsUnderAccessibility.Captions.options);
        int flag8 = list.indexOf(OptionsUnderAccessibility.HighContrastText.options);
        int flag9 = list.indexOf(OptionsUnderAccessibility.ColorCorrection.options);
        int flag10 = list.indexOf(OptionsUnderAccessibility.ColorInversion.options);
        boolean bool = true;
        if (flag == -1 || flag1 == -1 || flag2 == -1 || flag3 == -1 | flag4 == -1 || flag5 == -1 || flag6 == -1||flag7==-1||flag8==-1||flag9==-1||flag10==-1) {
            bool = false;
            Assert.assertTrue(bool, "the Options under Accessibility should be text-to-speech output, font size, display size, magnification, dwell timing,touch&hold delay, " +
                    "mono audio, captions, high contrast text, color correction, color inversion");
        }
        else {
            Assert.assertTrue(bool);
        }
    }

}












