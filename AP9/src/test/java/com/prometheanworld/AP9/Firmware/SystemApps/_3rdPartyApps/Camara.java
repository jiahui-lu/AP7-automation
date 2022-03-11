package com.prometheanworld.AP9.Firmware.SystemApps._3rdPartyApps;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.command.Adb;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class Camara extends BaseTest {
    /**
     * C91399 Camera app shows up when no camera connected
     * Steps:
     *  1. Does not connect to a camera
     *  2. Click Application icon on taskbar => Application locker should appear and the Camera app is in the applications list
     *  3. Touch the Camera icon on the menu => The camera app opens up and pops up a message "No camera detcted" and " Please plug in the camera and restart the app."
     * Author: Yifeng Wu
     *
     */
    @Test(groups = "P2")
    public void C91399CameraAppShowsUpWhenNoCameraConnected() {
        TestRail.setTestRailId("91399");

        try {
            TestRail.addStepName("Does not connect to a camera");
            List<String> list = Adb.adb("shell \"ls dev\"");
            boolean haveCamera = false;
            for (String s : list) {
                if (s.startsWith("video")) {
                    haveCamera = true;
                    break;
                }
            }
            if (haveCamera) {
                Log.info("This panel has camera module");
                return;
            }
            TestRail.addStepName("Click Application icon on taskbar");
            systemPO.startLocker();
            MobileElement cameraEle = null;
            try {
                cameraEle = systemPO.scrollAndFindApp("Camera");
            } catch (Throwable ignored) {
            }
            Assert.assertNotNull(cameraEle, "Camera app should be visible");
            TestRail.addStepName("Touch the Camera icon on the menu");
            cameraEle.click();
            Assert.assertTrue(ElementHelper.isVisible(Locator.byText("No camera detected"), 2),
                    "The text 'No camera detected' should be visible");
            Assert.assertTrue(ElementHelper.isVisible(Locator.byText("Please plug in the camera and restart the app.")),
                    "The text 'Please plug in the camera and restart the app.' should be visible");
        } catch (Throwable e) {
            AppiumHelper.captureScreenshot("C91399-failure-" + System.currentTimeMillis());
            throw e;
        } finally {
            systemPO.hideMenuBar();
            Adb.forceStop("com.prometheanworld.locker");
            Adb.forceStop("com.android.camera2");
        }
    }
}