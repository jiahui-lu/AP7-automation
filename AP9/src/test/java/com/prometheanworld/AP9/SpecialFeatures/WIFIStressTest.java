package com.prometheanworld.AP9.SpecialFeatures;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.model.AP9.SystemPO;
import com.prometheanworld.appium.frame.model.ApiBase.BaseSettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.UtilHelper;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class WIFIStressTest extends BaseTest {
    private BaseSettingsPO settingsPO = POFactory.getInstance(BaseSettingsPO.class);
    private final SystemPO systemPO = POFactory.getInstance(SystemPO.class);

    @Override
    protected String testAppName() {
        return "Settings";
    }

    @Test
    public void WIFIConnectDisconnectTest() {
        String bamboo_appiumTimes = System.getenv("bamboo_AppiumTimes");
        String bamboo_wifiName = System.getenv("bamboo_wifiName");
        String bamboo_wifiPassword = System.getenv("bamboo_wifiPassword");

        Integer testNumber = 1;
        if (bamboo_appiumTimes != null && !bamboo_appiumTimes.equals("")) {
            testNumber = Integer.parseInt(bamboo_appiumTimes);
        }
        settingsPO.navToNetworkAndInternet();
        settingsPO.setWiFiStatus(true);
        AppiumHelper.waitForSeconds(10);
        String wifiName = settingsPO.getWiFiName();
        if (wifiName.equals("Not connected")) {
            wifiName = settingsPO.connectToWifi(bamboo_wifiName,bamboo_wifiPassword);
        }
        Assert.assertNotEquals(wifiName, "Not connected");
        ElementHelper.click(By.xpath(settingsPO.eleIdBackWindow));
        for (int i=0;i<testNumber;i++) {
            settingsPO.navToNetworkAndInternet();
            settingsPO.setWiFiStatus(false);
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Off']"),10);
            String ifconfigStr = systemPO.getIpInfo();
            Map<String, List<String>> parseInfo = UtilHelper.getIfconfig(ifconfigStr);
            int offIpSize = parseInfo.get("inet addr").size();
            settingsPO.setWiFiStatus(true);
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+wifiName+"']"),60);
            ElementHelper.click(By.xpath(settingsPO.eleIdBackWindow));
            int onIpSize = 0 ;
            for (int j=0;j<20;j++) {
                ifconfigStr = systemPO.getIpInfo();
                parseInfo = UtilHelper.getIfconfig(ifconfigStr);
                onIpSize = parseInfo.get("inet addr").size();
                if (offIpSize + 1 == onIpSize) {
                    break;
                } else {
                    AppiumHelper.waitForSeconds(3);
                }
            }
            Log.info("["+(i+1)+"/"+testNumber+"] Get ip address when wifi is on:"+parseInfo);
            Assert.assertEquals(onIpSize,offIpSize+1,"Can not get IP address.Current Ip is:"+parseInfo);
        }
    }
}
