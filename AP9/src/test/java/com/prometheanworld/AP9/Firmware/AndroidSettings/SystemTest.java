package com.prometheanworld.AP9.Firmware.AndroidSettings;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.util.CommonOperator;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemTest extends BaseTest {

    @Override
    protected String testAppName() {
        return "Settings";
    }

    /**
     * C92387 System interface is displayed normally
     * Steps:
     *  1. On Menu bar click Application icon  -> Settings->System => The System interface will show about, language&input, date&time, reset options, import/export configuration
     * Author: Yifeng Wu
     */
    @Test(groups = "P2")
    public void C92387SystemInterfaceIsDisplayedNormally() {
        TestRail.setTestRailId("92387");

        Set<String> modules = new HashSet<>();
        modules.add("About ActivPanel");
        modules.add("Languages & input");
        modules.add("Date & time");
        modules.add("Reset options");
        modules.add("Import/export configuration");

        TestRail.addStepName("On Menu bar click Application icon  -> Settings->System");
        CommonOperator.scrollAndClick("com.android.settings:id/dashboard_container", "System");
        MobileElement systemEle = ElementHelper.findElement(Locator.byResourceId("com.android.settings:id/list"));
        List<MobileElement> eles =  systemEle.findElements(Locator.byResourceId("android:id/title"));
        Assert.assertEquals(eles.size(), modules.size(), "There should be only " + modules.size() + " modules");
        for (MobileElement ele : eles) {
            String module = ele.getText();
            Assert.assertTrue(modules.remove(module),
                    "The module `" + module + "' should be displayed");
        }
    }
}
