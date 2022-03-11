/*
 * © 2022 Promethean. All Rights Reserved.
 * Unauthorized copying of this file or any part of this file via any medium is strictly prohibited.
 */

package com.prometheanworld.AP9.Firmware;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyEvent;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.BootSequencePO;
import com.prometheanworld.appium.frame.model.AP9.OOBEPO;
import com.prometheanworld.appium.frame.model.AP9.SourceSwitchPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class BootSequence extends BaseTest {


    private final OOBEPO oobePO = POFactory.getInstance(OOBEPO.class);
    private final BootSequencePO bootSequencePO = POFactory.getInstance(BootSequencePO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    /**
     * C120960 Verify that applications will be opened and closed in the correct order when the last source is home source
     * Steps:
     *  1. Andorid Settinsg app -> Display -> Advanced -> Power on default source, set to Last source
     *  2. Do not switch to external sources
     *  3. Reboot panel => Confirm that panel will enter Android home source, the clock, menu and Welcome app will appear properly
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C120960VerifyThatApplicationsWillBeOpenedAndClosedInTheCorrectOrderWhenTheLastSourceIsHomeSource() {
        TestRail.setTestRailId("120960");

        bootSequencePO.openDefaultSourcePage();
        List<MobileElement> options = ElementHelper.findElements(By.className("android.widget.RadioButton"), 2);
        MobileElement defaultOption = null;
        for (MobileElement ele : options) {
            if ("true".equals(ele.getAttribute("checked"))) {
                defaultOption = ele;
            }
        }
        final String defaultOptionName = defaultOption == null ? "Home" : defaultOption.getText();
        setTestCaseCleaner(() -> {
            if (defaultOptionName != null) {
                bootSequencePO.openDefaultSourcePage();
                ElementHelper.click(Locator.byText(defaultOptionName));
            }
            try {
                systemPO.closeAppOnMenuBar("Settings");
            } catch (Throwable ignore) {}
        });

        TestRail.addStepName("Andorid Settinsg app -> Display -> Advanced -> Power on default source, set to Last source");
        ElementHelper.click(Locator.byText("Last source"));
        Assert.assertTrue(ElementHelper.isChecked(Locator.byText("Last source")),
                "The option 'Last source' should be selected");
        TestRail.addStepName("Do not switch to external sources");
        TestRail.addStepName("Reboot panel");
        AppiumHelper.rebootPanel();
        AppiumHelper.clickAt(0.0, 0.0);
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_time")),
                "The clock should be visible");
    }

    /**
     * C120954 Verify that applications will be opened and closed in the correct order when updating panel with a firmware update and EULA has no updates
     * Steps:
     *  1. Menu -> Applications -> Update app, select local or online update to upgrade panel =>
     *     The panel will automatically restart after the upgrade is complete
     *  2. After reboot => The Boot Manager will bring up the Update Installation Complete message
     *  3. When EULA has no updates, click the Finished button =>
     *     Confirm that panel will enter Android home source, the clock, menu and Welcome app will appear properly
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C120954VerifyThatApplicationsWillBeOpenedAndClosedInTheCorrectOrder() {
        TestRail.setTestRailId("120954");

        setTestCaseCleaner(() -> {
            systemPO.stopUpdateApp();
        });

        TestRail.addStepName("Menu -> Applications -> Update app, select local or online update to upgrade panel");
        systemPO.startAppFromUnifiedLauncher("Update");
        if (ElementHelper.isVisible(Locator.byText("The latest updates have already been installed"), 10)) {
            Log.info("The current version is the latest");
            return;
        }
        Assert.assertTrue(ElementHelper.isEnabled(Locator.byText("Online Update Now")),
                "The button 'Online Update Now' should be enabled");
        ElementHelper.click(Locator.byText("Online Update Now"));
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Install Update"), 5),
                "The dialog 'Install Update' should be visible");
        while (ElementHelper.isVisible(Locator.byText("Install Update"))) {
            AppiumHelper.waitForSeconds(5);
        }
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update Installed Successfully"), 2),
                "Update failed");
        AppiumHelper.waitForSeconds(60);
        AppiumHelper.checkAndConnectDevice();
        TestRail.addStepName("After reboot");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Update Installation Complete"), 10),
                "The dialog that title is 'Update Installation Complete' should be visible");
        TestRail.addStepName("When EULA has no updates, click the Finished button");
        ElementHelper.click(Locator.byText("Finished"));
        AppiumHelper.waitForSeconds(5);
        AppiumHelper.clickAt(0.0, 0.0);
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_time")),
                "The clock should be visible");
    }

    /**
     * C120951 Verify that applications will be opened and closed in the correct order in the first time
     * Steps:
     * 1. Bin flash or factory reset panel
     * 2. Click Skip button to skip the OOBE process
     * 3. Click Accept button to complete the EULA process
     * Author: Ning Lu
     */
    @Test(groups = {"P1", "UserDebug"})
    public void C120951VerifyThatApplicationsWillBeOpenedAndClosedInTheCorrectOrderInTheFirstTime() {
        TestRail.setTestRailId("120951");
        setTestCaseCleaner(() -> {
            oobePO.restoreAfterOOBE();
            ScreenHelper.clickAt(0.1, 0.1);
        });

        TestRail.addStepName("Bin flash or factory reset panel");
        oobePO.startOOBE();
        AppiumHelper.rebootPanel(false);
        // show Welcome page
        ElementHelper.waitUntilPresent(Locator.byText("Welcome to Promethean"));
        ElementHelper.waitUntilPresent(Locator.byText("Skip Set Up"));

        TestRail.addStepName("Click Skip button to skip the OOBE process");
        ElementHelper.clickWhenVisible(Locator.byText("Skip Set Up"));
        // show EULA page
        ElementHelper.waitUntilPresent(Locator.byTextContains("End User License Agreement"));
        ElementHelper.waitUntilPresent(Locator.byText("Accept"));

        TestRail.addStepName("Click Accept button to complete the EULA process");
        ElementHelper.clickWhenVisible(Locator.byText("Accept"));
        // verify the welcome page
        if (!ElementHelper.isPresent(Locator.byResourceId("id/hello"), 5)) {
            ScreenHelper.clickAt(0.5, 0.5);
        }
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/hello"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/welcome"));
        // verify the menu
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        AssertKt.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName(),
                "The default source should be 'Home source'");
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }


    /**
     * C120955 Verify that applications will be opened and closed in the correct order after reboot
     * Steps:
     * 1. Reboot panel
     * Author: Ning Lu
     */
    @Test(groups = {"P1"})
    public void C120955VerifyThatApplicationsWillBeOpenedAndClosedInTheCorrectOrderAfterReboot() {
        TestRail.setTestRailId("120955");
        setTestCaseCleaner(() -> ScreenHelper.clickAt(0.1, 0.1));
        TestRail.addStepName("Reboot panel");
        AppiumHelper.rebootPanel(false);
        // Because the appium settings app will be launched after welcome app, and the welcome app will be disappear
        // verify the menu
        AppiumHelper.sendEvent(PrometheanKeyEvent.Menu, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_bar"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/menu_icon_locker"));
        // verify the source
        AppiumHelper.sendEvent(PrometheanKeyEvent.Sources, PrometheanKeyboard.RemoteControl);
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/source_container_view"));
        AssertKt.assertEquals("Home", POFactory.getInstance(SourceSwitchPO.class).getCurrentSourceName(),
                "The default source should be 'Home source'");
        ScreenHelper.clickAt(0D, 0D);
        // verify the clock app
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_time"));
        ElementHelper.waitUntilPresent(Locator.byResourceId("id/tv_date"));
    }
}
