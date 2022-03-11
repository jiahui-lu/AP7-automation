package com.prometheanworld.AP9.Firmware.SystemApps.AVISettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP9.AVIPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EnergyStarTest extends BaseTest {

    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);
    private final AVIPO avipo = POFactory.getInstance(AVIPO.class);

    /**
     * C115235 Verify that the Energy Star logo is displayed except the 86” Low Tier
     * Steps:
     *  1. Open the AVI Settings app -> Visual tab
     *   => The Visual tab page opens and there is a energy star icon on the right
     *  2. Click on the Energy Star icon
     *   => Confirm that Energy Star logo is for display only and is not interactive
     *  3. Swipe the backlight slider
     *   => Confirm that Energy Star logo is for display only and is not interactive
     *  Author: DavidDing
     */
    @Test(groups = {"P0", "UserDebug"})
    public void C115235VerifyThatTheEnergyStarLogoIsDisplayedExceptThe86LowTier() {
        TestRail.setTestRailId("115235");

        try {
            // Open the Update and extra the panel model information.
            systemPO.startAppFromUnifiedLauncher("Update");
            By updateInfoPath = Locator.byResourceId("com.prometheanworld.update:id/tv_system_info");
            ElementHelper.waitUntilVisible(updateInfoPath, 10);
            String modelFromUpdate = null;
            // Verify the content text of Update app system info text view is NOT null.
            String updateInfoText = ElementHelper.findElement(updateInfoPath).getText();
            Assert.assertNotNull(updateInfoText, "Expect the content of the system information in the Update app is available, but is NULL.");
            // Extra the model and firmware version from Update app.
            if (updateInfoText.contains("\n\n")) {
                String[] info = updateInfoText.split("\n\n");
                for (String s : info) {
                    s = s.replace("\n", "");
                    final String prefix_model = "Model:";
                    if (s.contains(prefix_model)) {
                        modelFromUpdate = s.substring(s.indexOf(prefix_model) + prefix_model.length()).trim();
                    }
                }
            }
            // Verify the model from Update app is valid.
            Assert.assertNotNull(modelFromUpdate, "Expect the model from Update app is valid, but is NULL.");
            systemPO.stopUpdateApp();

            boolean supportedEnergyStar = false;
            if ("ActivPanel 9 Premium".equals(modelFromUpdate)) {
                // Energy Star compliant at all series of high tier.
                supportedEnergyStar = true;
            } else {
                AppiumHelper.exeAdbRoot();
                String panelType = Adb.getAndroidProp("ro.product.customer.panel");
                if (panelType == null || panelType.isEmpty()) {
                    // Devkit or the panel information of the device is incomplete.
                    supportedEnergyStar = false;
                } else if (avipo.checkEnergyStarSupportedByPanelType(panelType)) {
                    // Energy Star compliant at all series of low tier except the 86”.
                    supportedEnergyStar = true;
                }
            }

            if (supportedEnergyStar) {
                avipo.openAVISettingPanel();
                By visualSettingsTabPath = Locator.byResourceId("com.prometheanworld.avisettings:id/select_visual_settings");
                ElementHelper.waitUntilVisible(visualSettingsTabPath, 5);
                TestRail.addStepName("Open the AVI Settings app -> Visual tab");
                ElementHelper.click(visualSettingsTabPath);

                ElementHelper.waitUntilVisible(Locator.byResourceId("com.prometheanworld.avisettings:id/backlight_label"), 5);
                By energyStarIconPath = Locator.byResourceId("com.prometheanworld.avisettings:id/energy_star");
                Assert.assertTrue(ElementHelper.findElement(energyStarIconPath).isDisplayed(),
                        "Expect the Energy-Star icon is displayed, but is NOT displayed.");
                TestRail.addStepName("Verify that the Visual tab page opens and there is a energy star icon on the right.");

                Float currentBackLightLevel = avipo.getBackLightFromSeekbar();
                Assert.assertNotNull(currentBackLightLevel, "Expect the back light level from the visual tab is valid, but is NULL.");
                TestRail.addStepName("Click on the Energy Star icon");
                verifyEnergyStarLogoDisplayedAndNotInteractive(energyStarIconPath);
                TestRail.addStepName("Confirm that Energy Star logo is for display only and is not interactive");

                TestRail.addStepName("Swipe the backlight slider");
                avipo.scrollBackLightSeekbar(0.0f);
                verifyEnergyStarLogoDisplayedAndNotInteractive(energyStarIconPath);
                avipo.scrollBackLightSeekbar(100.0f);
                verifyEnergyStarLogoDisplayedAndNotInteractive(energyStarIconPath);
                avipo.scrollBackLightSeekbar(50.0f);
                verifyEnergyStarLogoDisplayedAndNotInteractive(energyStarIconPath);
                TestRail.addStepName("Confirm that Energy Star logo is for display only and is not interactive");
            }
        } finally {
            systemPO.stopUpdateApp();
            avipo.closeAVISettingPanel();
        }
    }

    private void verifyEnergyStarLogoDisplayedAndNotInteractive(By energyStarIconPath) {
        Assert.assertTrue(ElementHelper.findElement(energyStarIconPath).isDisplayed(),
                "Expect the Energy-Star icon is displayed, but is NOT displayed.");
        Float backLight = avipo.getBackLightFromSeekbar();
        ElementHelper.click(energyStarIconPath);
        Assert.assertEquals(avipo.getBackLightFromSeekbar(), backLight,
                "Expect the back light level is not changed after clicked the Energy Star icon, but is changed.");
    }

}
