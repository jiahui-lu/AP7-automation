package com.prometheanworld.appium.Panel.SystemFunction.SettingonLocker.Brightness;

import com.nd.automation.core.action.Direction;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @description:
 * @author: yangwenjin
 * @time: 2021/11/15
 */
@Listeners({TestRailListener.class, TestStatusListener.class})
public class BrightnessTest extends BaseTest {

    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);

    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        super.afterClass();
        ScreenHelper.clickAt(0.999, 0.001);
    }

    /**
     * ENS-01 Energy Star 8 logo will be displayed in energy save mode
     * 1. Open the Locker and select the settings tab
     * 2. Click the leaf icon of Brightness
     * 3. The Energy Star icon is displayed in the upper right corner
     * Note: 86 panel produced after 4.1.0 can display energy star icon, so if the tested panel was produced before 4.1.0, it cannot display it
     */
    @Test
    public void clickEnergySaverIconTest() {
        TestRail.setTestRailId("83170");
        TestRail.addStepName("1.Open the Locker and select the settings tab");
        settingsPO.openLockerSetting();
        //openLockerSetting();
        TestRail.addStepName("2.Click the leaf icon of Brightness");
        settingsPO.switchBrightnessMode("energy saver mode");
        //switchBrightnessMode("energy saver mode");
        TestRail.addStepName("3.The Energy Star icon is displayed in the upper right corner");
        settingsPO.confirmEnergyStarIcon();
        //confirmEnergyStarIcon();
    }

    /**
     * ENS-02 Energy Star 8 logo will disappear when clicking "A" icon
     * 1. Open the Locker and select the settings tab
     * 2. Click the leaf icon of Brightness
     * 3. The Energy Star icon is displayed in the upper right corner
     * 4. Click the "A" icon of Brightness
     * 5. The Energy Star icon will disappear
     * Note: 86 panel produced after 4.1.0 can display energy star icon, so if the tested panel was produced before 4.1.0, it cannot display it
     */
    @Test
    public void clickAmbientIconTest() {
        TestRail.setTestRailId("83171");
        TestRail.addStepName("1.Open the Locker and select the settings tab");
        settingsPO.openLockerSetting();
        TestRail.addStepName("2.Click the leaf icon of Brightness");
        settingsPO.switchBrightnessMode("energy saver mode");
        TestRail.addStepName("3.The Energy Star icon is displayed in the upper right corner");
        settingsPO.confirmEnergyStarIcon();
        TestRail.addStepName("4.Click the 'A' icon of Brightness");
        settingsPO.switchBrightnessMode("ambient mode");
        TestRail.addStepName("5.The Energy Star icon will disappear");
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/energy_star_compliance"), 3);
    }

    /**
     * ENS-03 Energy Star 8 logo will disappear when clicking sun icon
     * 1. Open the Locker and select the settings tab
     * 2. Click the leaf icon of Brightness
     * 3. The Energy Star icon is displayed in the upper right corner
     * 4. Click the sun icon of Brightness
     * 5. The Energy Star icon will disappear
     * Note: 86 panel produced after 4.1.0 can display energy star icon, so if the tested panel was produced before 4.1.0, it cannot display it
     */
    @Test
    public void clickManualIconTest() {
        TestRail.setTestRailId("83172");
        TestRail.addStepName("1.Open the Locker and select the settings tab");
        settingsPO.openLockerSetting();
        TestRail.addStepName("2.Click the leaf icon of Brightness");
        settingsPO.switchBrightnessMode("energy saver mode");
        TestRail.addStepName("3.The Energy Star icon is displayed in the upper right corner");
        settingsPO.confirmEnergyStarIcon();
        TestRail.addStepName("4.Click the sun icon of Brightness");
        settingsPO.switchBrightnessMode("manual mode");
        TestRail.addStepName("5.The Energy Star icon will disappear");
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/energy_star_compliance"), 3);
    }

    /**
     * ENS-04 Energy Star 8 logo will disappear when swiping the brightness slider
     * 1. Open the Locker and select the settings tab
     * 2. Click the leaf icon of Brightness
     * 3. The Energy Star icon is displayed in the upper right corner
     * 4. Swipe the brightness slider
     * 5. The Energy Star icon will disappear and switch to Manual mode (sun icon)
     * Note: 86 panel produced after 4.1.0 can display energy star icon, so if the tested panel was produced before 4.1.0, it cannot display it
     */
    @Test
    public void swipeBrightnessTest() {
        TestRail.setTestRailId("83173");
        TestRail.addStepName("1.Open the Locker and select the settings tab");
        settingsPO.openLockerSetting();
        TestRail.addStepName("2.Click the leaf icon of Brightness");
        settingsPO.switchBrightnessMode("energy saver mode");
        TestRail.addStepName("3.The Energy Star icon is displayed in the upper right corner");
        settingsPO.confirmEnergyStarIcon();
        TestRail.addStepName("4.Swipe the brightness slider");
        // swipe from 0.5 to 0.7
        ElementHelper.swipe(Locator.byResourceId("id/brightness_seek_bar"), Direction.RIGHT, 0.2, 0.6);
        TestRail.addStepName("5.The Energy Star icon will disappear");
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/energy_star_compliance"), 3);
        // to do: confirm that the current brightness mode is manual mode
    }

    /**
     * ENS-05 Energy Star 8 logo will disappear when swiping the contrast slider
     * 1. Open the Locker and select the settings tab
     * 2. Click the leaf icon of Brightness
     * 3. The Energy Star icon is displayed in the upper right corner
     * 4. Swipe the contrast slider
     * 5. The Energy Star icon will disappear and switch to Manual mode (sun icon)
     * Note: 86 panel produced after 4.1.0 can display energy star icon, so if the tested panel was produced before 4.1.0, it cannot display it
     */
    @Test
    public void swipeContrastTest() {
        TestRail.setTestRailId("83174");
        TestRail.addStepName("1.Open the Locker and select the settings tab");
        settingsPO.openLockerSetting();
        TestRail.addStepName("2.Click the leaf icon of Brightness");
        settingsPO.switchBrightnessMode("energy saver mode");
        TestRail.addStepName("3.The Energy Star icon is displayed in the upper right corner");
        settingsPO.confirmEnergyStarIcon();
        TestRail.addStepName("4.Swipe the contrast slider");
        // swipe from 0.5 to 0.7
        ElementHelper.swipe(Locator.byResourceId("id/contrast_seek_bar"), Direction.RIGHT, 0.2, 0.6);
        TestRail.addStepName("5.The Energy Star icon will disappear");
        ElementHelper.waitUntilNotPresent(Locator.byResourceId("id/energy_star_compliance"), 3);
        // to do: confirm that the current brightness mode is manual mode
    }
}


