package com.prometheanworld.AP9.Firmware.SystemApps;

import com.nd.automation.core.action.Rect;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class DateAndTime extends BaseTest {
    /**
     * C120830 Verify that the user can see the time and date on the home source screen
     * Steps:
     *  1. Flash the panel with the latest package => After the panel is turned on, you can see time and date on the home source screen.
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P1")
    public void C120830VerifyThatTheUserCanSeeTheTimeAndDateOnTheHomeSourceScreen() {
        TestRail.setTestRailId("120830");

        TestRail.addStepName("Flash the panel with the latest package");
        List<String> list = Adb.adb("shell dumpsys activity | grep mResume");
        Assert.assertTrue(!list.isEmpty() && list.get(0).contains("com.prometheanworld.bootmanagement"), "The home source screen should be visible");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_time")), "Time should be visible");
        Assert.assertTrue(ElementHelper.isVisible(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_date")), "Date should be visible");
    }

    /**
     * C120859 Verify that time and date format When system language is English
     * Steps:
     *  1. Open Settings->System->Languages & input->Languages
     *  2. Select Language: English(United States) => System language Switch to English
     *  3. return to the home source screen to view time and log format =>
     *     Time and log formats are shown as:
     *     Time: xx:xx
     *     Date: week month day
     *
     *     For example：
     *     00:25
     *     TUE NOVEMBER 02
     *
     *     Note: The abbreviation of the week can only display the first 3 letters
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P1")
    public void C120859VerifyThatTimeAndDateFormatWhenSystemLanguageIsEnglish() {
        TestRail.setTestRailId("120859");

        String originOption = null;
        try {
            final String language = "English";
            final String country = "United States";
            TestRail.addStepName("Open Settings->System->Languages & input->Languages");
            TestRail.addStepName("Select Language: English(United States)");
            addLanguage(language, country);
            originOption = switchLanguage(language + " (" + country + ")");
            TestRail.addStepName("Return to the home source screen to view time and log format");
            Adb.forceStop("com.android.settings");
            String time = ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_time")).getText();
            String date = ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_date")).getText();
            Assert.assertTrue(time.matches("\\d{2}:\\d{2}"), "Time should be like '08:30'");
            Assert.assertTrue(isEnglishDate(date), "Date should be like 'TUE FEBRUARY 08'");
        } finally {
            if (originOption != null) {
                switchLanguage(originOption);
            }
            Adb.forceStop("com.android.settings");
        }
    }

    /**
     * C120868 Verify that time and date format When time use 12-hour format and language is English
     * Steps:
     *  1. Open Settings->System->Date & time
     *  2. Switch the Use 24-hour format button to close => The time and date of the system are switched to 12 hours format
     *  3. Return home source screen view time and date =>
     *     Time and log formats are shown as:
     *     Time: xx:xx AM
     *     Date: week month day
     *
     *     For example：
     *     12:25 AM
     *     TUE NOVEMBER 02
     * Author: Yifeng Wu
     *
     */
    @Test(groups = {"P1", "User"})
    public void C120868VerifyThatTimeAndDateFormatWhenTimeUse12HourFormatAndLanguageIsEnglish() {
        TestRail.setTestRailId("120868");

        String defaultFormat = null;
        List<String> list = Adb.adb("shell settings get system time_12_24");
        if (!list.isEmpty()) {
            defaultFormat = list.get(0);
        }
        try {
            TestRail.addStepName("Open Settings->System->Date & time");
            systemPO.startAppFromUnifiedLauncher("Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "System");
            CommonOperator.scrollAndClick("com.android.settings:id/list", "Date & time");
            TestRail.addStepName("Switch the Use 24-hour format button to close");
            String path = "//*[@text='Use 24-hour format']/../../android.widget.LinearLayout[@resource-id='android:id/widget_frame']/android.widget.Switch[@resource-id='android:id/switch_widget']";
            if (ElementHelper.isChecked(By.xpath(path))) {
                ElementHelper.click(Locator.byText("Use 24-hour format"));
            }
            Assert.assertFalse(ElementHelper.isChecked(By.xpath(path)), "The 'Use 24-hour format' switch should be off");
            Adb.forceStop("com.android.settings");
            String time = ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_time")).getText();
            String date = ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_date")).getText();
            Assert.assertTrue(time.matches("\\d{1,2}:\\d{2} [AP]M"), "Time should be like '08:30 AM'");
            Assert.assertTrue(isEnglishDate(date), "Date should be like 'TUE FEBRUARY 08'");
        } finally {
            if (defaultFormat == null) {
                Adb.adb("shell settings delete system time_12_24");
            } else {
                Adb.adb("shell settings put system time_12_24 " + defaultFormat);
            }
            Adb.forceStop("com.android.settings");
        }
    }

    /**
     * C120863 Verify that time and date format When time use 24-hour format and language is English
     * Steps:
     *  1. Open Settings->System->Date & time
     *  2. Switch the Use 24-hour format button to open => The time and date of the system are switched to 24 hours format
     *  3. Return home source screen view time and date =>
     *     Time and log formats are shown as:
     *     Time: xx:xx
     *     Date: week month day
     *
     *     For example：
     *     00:25
     *     TUE NOVEMBER 02
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P1")
    public void C120863VerifyThatTimeAndDateFormatWhenTimeUse24HourFormatAndLanguageIsEnglish() {
        TestRail.setTestRailId("120863");

        String defaultFormat = null;
        List<String> list = Adb.adb("shell settings get system time_12_24");
        if (list != null && !list.isEmpty()) {
            defaultFormat = list.get(0);
        }
        try {
            TestRail.addStepName("Open Settings->System->Date & time");
            systemPO.startAppFromUnifiedLauncher("Settings");
            CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "System");
            CommonOperator.scrollAndClick("com.android.settings:id/list", "Date & time");
            TestRail.addStepName("Switch the Use 24-hour format button to open");
            String path = "//*[@text='Use 24-hour format']/../../android.widget.LinearLayout[@resource-id='android:id/widget_frame']/android.widget.Switch[@resource-id='android:id/switch_widget']";
            if (!ElementHelper.isChecked(By.xpath(path))) {
                ElementHelper.click(Locator.byText("Use 24-hour format"));
            }
            Assert.assertTrue(ElementHelper.isChecked(By.xpath(path)), "The 'Use 24-hour format' switch should be on");
            Adb.forceStop("com.android.settings");
            String time = ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_time")).getText();
            String date = ElementHelper.findElement(Locator.byResourceId("com.prometheanworld.bootmanagement:id/tv_date")).getText();
            Assert.assertTrue(time.matches("\\d{2}:\\d{2}"), "Time should be like '08:30'");
            Assert.assertTrue(isEnglishDate(date), "Date should be like 'TUE FEBRUARY 08'");
        } finally {
            if (defaultFormat == null) {
                Adb.adb("shell settings delete system time_12_24");
            } else {
                Adb.adb("shell settings put system time_12_24 " + defaultFormat);
            }
            Adb.forceStop("com.android.settings");
        }
    }

    private String switchLanguage(String key) {
        Adb.forceStop("com.android.settings");
        Adb.adb("shell am start -a android.settings.LOCALE_SETTINGS");
        AppiumHelper.waitForSeconds(2);
        MobileElement defaultEle = ElementHelper.findElement(Locator.byResourceId("com.android.settings:id/label"));
        String defaultLanguageKey = defaultEle.getText();
        if (defaultLanguageKey.equals(key)) {
            return key;
        }
        Assert.assertTrue(ElementHelper.isVisible(Locator.byText(key)), "The option '" + key + "' should be visible");
        Point start = new Rect(ElementHelper.findElement(Locator.byText(key)).getRect()).getPoint(0.0f, 0.5f);
        Point end = new Rect(defaultEle.getRect()).getPoint(0.0f, 0.0f);
        TouchAction act = new TouchAction(Driver.getAndroidDriver());
        act.longPress(PointOption.point(start))
                .moveTo(PointOption.point(end))
                .release()
                .perform();
        AppiumHelper.waitForSeconds(4);
        Assert.assertEquals(ElementHelper.findElement(Locator.byResourceId("com.android.settings:id/label")).getText(), key);
        return defaultLanguageKey;
    }

    private void addLanguage(String language, String country) {
        Adb.forceStop("com.android.settings");
        Adb.adb("shell am start -a android.settings.LOCALE_SETTINGS");
        AppiumHelper.waitForSeconds(2);
        List<MobileElement> eles = ElementHelper.findElements(Locator.byResourceId("com.android.settings:id/label"));
        boolean containLanguage = false;
        for (MobileElement ele : eles) {
            String key = ele.getText();
            if (key.startsWith(language) && (country == null || key.contains(country))) {
                containLanguage = true;
                break;
            }
        }
        if (containLanguage) {
            return;
        }
        final By byAddLanguage = Locator.byResourceId("com.android.settings:id/add_language");
        ElementHelper.click(byAddLanguage);
        CommonOperator.scrollAndClick("id/list", language);
        if (country != null) {
            CommonOperator.scrollAndClick("id/list", country);
        }
        AppiumHelper.waitForSeconds(4);
        eles = ElementHelper.findElements(Locator.byResourceId("com.android.settings:id/label"));
        for (MobileElement ele : eles) {
            String key = ele.getText();
            if (key.startsWith(language) && (country == null || key.contains(country))) {
                containLanguage = true;
                break;
            }
        }
        Assert.assertTrue(containLanguage, "The language '" + language + " " + country + "' should be visible");
    }

    private boolean isEnglishDate(String s) {
        String[] arr = s.split(" ");
        final List<String> dayOfWeekList = Arrays.asList(
                "SUN",
                "MON",
                "TUE",
                "WED",
                "THU",
                "FRI",
                "SAT"
        );
        final List<String> monthList = Arrays.asList(
                "JANUARY",
                "FEBRUARY",
                "MARCH",
                "APRIL",
                "MAY",
                "JUNE",
                "JULY",
                "AUGUST",
                "SEPTEMBER",
                "OCTOBER",
                "NOVEMBER",
                "DECEMBER"
        );
        return arr.length == 3 && dayOfWeekList.contains(arr[0]) && monthList.contains(arr[1]) && arr[2].matches("\\d{2}");
    }
}
