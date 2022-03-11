package com.prometheanworld.AP9.Firmware.AndroidSettings.Display;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP9.SourceSwitchPO;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Listeners({TestRailListener.class, TestStatusListener.class})
public class PowerOnDefaultSourceTest extends BaseTest {

    List<String> sourceList = Arrays.asList("Home", "OPS", "HDMI1", "HDMI2", "HDMI3", "DP", "TYPEC", "AV", "VGA");

    private final SourceSwitchPO sourceSwitchPO = POFactory.getInstance(SourceSwitchPO.class);

    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        // Preparing the test environment
        AppiumHelper.switchToHomePage();
        // Tap outside to hide the task bar and locker
        ScreenHelper.clickAt(Location.CENTER);
    }

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
        AppiumHelper.switchToHomePage();
    }

    /**
     * C92358 Verify that users can set Power on default source to different source
     * set "power on default source" to different sources.
     * set the auto-change source setting to off
     * 1. Android Settings --> Display --> Advanced --> Power On Default Source
     * ---- can see power on default source
     * 2. Select option to "*" (PS: "*" includes Home, OPS, HDMI1, HDMI2, HDMI3, DP, TYPEC, AV, VGA, need to verify all of them)
     * ---- can see "*" (PS: "*" includes OPS, HDMI1, HDMI2, HDMI3, DP, TYPEC, AV, VGA, need to verify all of them) is selected in Power on default source
     * 3. Switch the source to an external source other than the option just selected
     * ---- can see the screen of the external source
     * 4. Reboot panel
     * ---- confirm that the panel boots up into the "*" (PS: "*" includes OPS, HDMI1, HDMI2, HDMI3, DP, TYPEC, AV, VGA, need to verify all of them) source
     */
    @Test(groups = {"P0"})
    public void C92358testUsersCanSetPowerOnDefaultSource() {
        TestRail.setTestRailId("92358");

        sourceList.forEach(source -> {
            TestRail.addStepName("Test Source: " + source);
            testDefaultSource(source);
        });
    }

    private void testDefaultSource(String sourceName) {
        AppiumHelper.switchToHomePage();

        TestRail.addStepName("1. Android Settings --> Display --> Advanced --> Power On Default Source");
        openPowerOnDefaultSourcePage();

        TestRail.addStepName("2. Select option to " + sourceName);
        ElementHelper.waitUntilPresent(Locator.byText(sourceName), 3);
        ElementHelper.findElement(Locator.byText(sourceName)).click();

        TestRail.addStepName("3. Switch source to Home");
        AppiumHelper.switchToHomePage();

        TestRail.addStepName("4. Reboot panel");
        AppiumHelper.rebootPanel();
        AppiumHelper.resetAppiumServices();

        TestRail.addStepName("confirm that the panel boots up into the " + sourceName);
        Assert.assertEquals(sourceSwitchPO.getCurrentSourceName(), sourceName);
    }

    /**
     * C92265 Verify that users can switch to last source normally
     * select "Last Source" when attaching different external sources
     * set the auto-change source setting to off
     * 1. Android Settings --> Display --> Advanced --> Power On Default Source
     * ---- can see power on default source
     * 2. Select option to "Last Source"
     * ---- can see "Last source" is selected in Power on default source
     * 3. Switch source to external source "*"
     * ---- can see the screen of external source "*"
     * 4. Reboot panel
     * ---- confirm that the panel boots up into the * source
     * (PS: "*" includes OPS, HDMI1, HDMI2, HDMI3, DP, TYPEC, AV, VGA, need to verify all of them)
     */
    @Test(groups = {"P0"})
    public void C92265testUsersCanSwitchLastSource() {
        TestRail.setTestRailId("92265");

        TestRail.addStepName("1. Android Settings --> Display --> Advanced --> Power On Default Source");
        openPowerOnDefaultSourcePage();

        TestRail.addStepName("2. Select option to Last Source");
        ElementHelper.waitUntilPresent(Locator.byText("Last source"), 3);
        ElementHelper.findElement(Locator.byText("Last source")).click();

        TestRail.addStepName("3.4. Switch source and Reboot panel");
        sourceList.forEach(source -> {
            TestRail.addStepName("Test Last Source: " + source);
            testLastSource(source);
        });
    }

    private void testLastSource(String sourceName) {
        TestRail.addStepName("switch source to " + sourceName + " and reboot panel");
        sourceSwitchPO.switchSource(sourceName);

        TestRail.addStepName("reboot panel");
        AppiumHelper.rebootPanel();
        AppiumHelper.resetAppiumServices();

        TestRail.addStepName("confirm that the panel boots up into the " + sourceName);
        Assert.assertEquals(sourceSwitchPO.getCurrentSourceName(), sourceName);
    }

    /**
     * C92326  Verify that last source is the last source used before shutdown
     * select to "Last Source", reboot panel will always keep you in the last source you stay
     * Set the auto-change source setting to off
     * 1. Android Settings --> Display --> Advanced --> Power On Default Source
     * ---- can see power on default source
     * 2. Select option to "Last Source"
     * ---- can see "Last source" is selected in Power on default source
     * 3. Switch source to external source (like OPS)
     * ---- can see the screen of the external source(like OPS)
     * 4. Switch source to external source (like HDMI1)
     * ---- can see the screen of the external source(like HDMI1)
     * 5. Switch source to external source (like HDMI2)
     * ---- can see the screen of the external source(like HDMI2)
     * 6. Reboot panel
     * ---- confirm that the panel boots up into the HDMI2
     */
    @Test(groups = {"P0"})
    public void C92326testLastSourceIsLastSourceBeforeShutdown() {
        TestRail.setTestRailId("92326");

        TestRail.addStepName("1. Android Settings --> Display --> Advanced --> Power On Default Source");
        openPowerOnDefaultSourcePage();

        TestRail.addStepName("2. Select option to Last Source");
        ElementHelper.waitUntilPresent(Locator.byText("Last source"), 3);
        ElementHelper.findElement(Locator.byText("Last source")).click();

        TestRail.addStepName("3. Switch source to OPS");
        sourceSwitchPO.switchSource("OPS");

        TestRail.addStepName("4. Switch source to HDMI1");
        sourceSwitchPO.switchSource("HDMI1");

        TestRail.addStepName("5.6. Switch source to HDMI2 and Reboot panel");
        testLastSource("HDMI2");
    }

    /**
     * C92268 Verify that last source can be changed when users switch to another source
     * switch to "Last Source" again after switch it to "Home" or other options
     * Set the auto-change source setting to off
     * 1. Android Settings --> Display --> Advanced --> Power On Default Source
     * ---- can see power on default source
     * 2. Select option to "Last Source"
     * ---- can see "Last source" is selected in Power on default source
     * 3. Switch source to external source(like OPS)
     * ---- can see the screen of the external source(like OPS)
     * 4. Reboot panel
     * ---- confirm that the panel boots up into OPS
     * 5. Android Settings --> Display --> Advancecd --> Power On Default Source
     * ---- can see Power on default source
     * 6. Switch back to "Home"(or other sources except the last source)
     * ---- can see "Home"(source you select) is selected in Power on default source
     * 7. Switch to other external sources (not home)
     * ---- can see the screen of the external source
     * 8. Reboot panel
     * ---- confirm that the panel boots up into Home
     */
    @Test(groups = {"P0"})
    public void C92268testLastSourceCanBeChange() {
        TestRail.setTestRailId("92268");

        TestRail.addStepName("1. Android Settings --> Display --> Advanced --> Power On Default Source");
        openPowerOnDefaultSourcePage();

        TestRail.addStepName("2. Select option to Last Source");
        ElementHelper.waitUntilPresent(Locator.byText("Last source"), 3);
        ElementHelper.findElement(Locator.byText("Last source")).click();

        TestRail.addStepName("3.4. Switch source to OPS and Reboot panel");
        testLastSource("OPS");

        TestRail.addStepName("5. Android Settings --> Display --> Advanced --> Power On Default Source");
        AppiumHelper.switchToHomePage();
        openPowerOnDefaultSourcePage();

        TestRail.addStepName("6. Select option to Home");
        ElementHelper.waitUntilPresent(Locator.byText("Home"), 3);
        ElementHelper.findElement(Locator.byText("Home")).click();

        TestRail.addStepName("7.Switch source to HDMI1");
        sourceSwitchPO.switchSource("HDMI1");

        TestRail.addStepName("8. Reboot panel");
        AppiumHelper.rebootPanel();
        AppiumHelper.resetAppiumServices();

        TestRail.addStepName("confirm that the panel boots up into the Home");
        Assert.assertEquals(sourceSwitchPO.getCurrentSourceName(), "Home");


    }

    private void openPowerOnDefaultSourcePage() {
        TestRail.addStepName("open Android Settings");
        POFactory.getInstance(SystemPO.class).findAppFromApplications("Settings").click();
        TestRail.addStepName("click Display");
        ElementHelper.waitUntilPresent(Locator.byText("Display"), 3);
        ElementHelper.findElement(Locator.byText("Display")).click();
        ElementHelper.findElement(Locator.byResourceId("android:id/maximize_window")).click();
        TestRail.addStepName("click Advanced");
        ElementHelper.waitUntilPresent(Locator.byText("Advanced"), 3);
        ElementHelper.findElement(Locator.byText("Advanced")).click();
        TestRail.addStepName("click Power on default source");
        ElementHelper.waitUntilPresent(Locator.byText("Power on default source"), 3);
        ElementHelper.findElement(Locator.byText("Power on default source")).click();
    }

    /**
     * C92266 Verify that panel boots into home source when no external source attached
     * select "Last Source" when no external source is attached
     * with no external source is attached
     * 1. Android Settings --> Display --> Advanced --> Power On Default Source
     * ---- can see power on default source
     * 2. Select option to "Last Source"
     * ---- can see "Last source" is selected in Power on default source
     * 3. Reboot panel
     * ---- confirm that the panel boots up into Home Source
     * Author: Reenu Sandhu
     */
    @Test(groups = {"P1"})
    public void C92266VerifyPanelBootToHomeSource() {
        TestRail.setTestRailId("92266");
        TestRail.addStepName("1. Android Settings --> Display --> Advanced --> Power On Default Source");
        openPowerOnDefaultSourcePage();
        TestRail.addStepName("2. Select option to Last Source");
        ElementHelper.waitUntilPresent(Locator.byText("Last source"), 3);
        ElementHelper.findElement(Locator.byText("Last source")).click();
        TestRail.addStepName("3. Reboot panel");
        AppiumHelper.rebootPanel();
        AppiumHelper.resetAppiumServices();
        TestRail.addStepName("confirm that the panel boots up into the Home");
        Assert.assertEquals(sourceSwitchPO.getCurrentSourceName(), "Home");
    }
}
