package com.prometheanworld.appium.Panel.SystemFunction.SystemBasicFunction;

import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.log.Log;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.TestRailListener;
import com.prometheanworld.appium.frame.model.AP7.IdentityPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Listeners({TestRailListener.class})
public class UnifiedMenu extends BaseTest {

    IdentityPO identityPO = POFactory.getInstance(IdentityPO.class);
    List<String> list=new ArrayList<>();

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        list.add("Bottom");
        list.add("Right");
        list.add("Left");
    }


    /**
     * C83355 Verify that owner can change to New user
     * Verify in all three directions
     */
    @Test
    public void c83355ChangeUserOwnerToNewUser() {
        TestRail.setTestRailId("83355");
        if(!identityPO.hasNewUser()){
            identityPO.addUser(false);
        }

        TestRail.addStepName("try 3 side Unified Menu in the pannel");
        for(int i=0;i<list.size();i++){
            TestRail.addStepName("Owner change to New user");
            identityPO.changeUser("Owner","New User", list.get(i), false);
            try {
                AppiumHelper.showBottomMenu();
            } catch (Exception e) {
                Log.error("Show bottom menu failed.");
            }
            AppiumHelper.waitForSeconds(5);
            AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text='New user']"));
            identityPO.changeUser("New User", "Owner", "Bottom", false);
        }
        TestRail.addStepName("Clean up the environment");
        identityPO.deleteUser();
    }

    /**
     * C123320 Verify that owner can change to guest
     * Verify in all three directions
     */
    @Test
    public void c123320ChangeUserOwnerToGuest() {
        TestRail.setTestRailId("123320");
        if(!identityPO.hasNewUser()){
            identityPO.addUser(false);
        }
        TestRail.addStepName("try 3 side Unified Menu in the pannel");
        for(int i=0;i<list.size();i++){
            TestRail.addStepName("Owner change to guest");
            identityPO.changeUser("Owner","Guest", list.get(i), false);
            try {
                AppiumHelper.showBottomMenu();
            } catch (Exception e) {
                Log.error("Show bottom menu failed.");
            }
            AppiumHelper.waitForSeconds(5);
            AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text='Guest']"));
            identityPO.changeUser("Guest", "Owner", "Bottom", false);
        }
        TestRail.addStepName("Clean up the environment");
        identityPO.deleteUser();
    }

    /**
     * C123322 Verify that new user can change to owner
     * Verify in all three directions
     */
    @Test
    public void c123322ChangeUserNewUserToGuest() {
        TestRail.setTestRailId("123322");
        if(!identityPO.hasNewUser()){
            identityPO.addUser(false);
        }
        identityPO.changeUser("Owner","New user", "Bottom", false);
        TestRail.addStepName("try 3 side Unified Menu in the pannel");
        for(int i=0;i<list.size();i++){
            TestRail.addStepName("new user change to guest");
            identityPO.changeUser("New user","Guest", list.get(i), false);
            try {
                AppiumHelper.showBottomMenu();
            } catch (Exception e) {
                Log.error("Show bottom menu failed.");
            }
            AppiumHelper.waitForSeconds(5);
            AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text='Guest']"));
            identityPO.changeUser("Guest", "New user", "Bottom", false);
        }
        TestRail.addStepName("Clean up the environment");
        identityPO.changeUser("New user","Owner", "Bottom", false);
        identityPO.deleteUser();
    }

    /**
     * C123323 Verify that guest can change to other user with save session
     * Verify in all three directions
     */
    @Test
    public void c123322ChangeUserGuestToOtherUserWithSaveSession() {
        TestRail.setTestRailId("123322");
        if(!identityPO.hasNewUser()){
            identityPO.addUser(false);
        }
        identityPO.changeUser("Owner","Guest", "Bottom", false);
        TestRail.addStepName("try 3 side Unified Menu in the pannel");
        for(int i=0;i<list.size();i++){
            TestRail.addStepName("guest change to owner");
            identityPO.changeUser("Guest","Owner", list.get(i), true);
            try {
                AppiumHelper.showBottomMenu();
            } catch (Exception e) {
                Log.error("Show bottom menu failed.");
            }
            AppiumHelper.waitForSeconds(5);
            AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text='Owner']"));
            identityPO.changeUser("Owner", "Guest", "Bottom", false);
            AssertKt.assertNotPresent(By.xpath("//android.widget.TextView[@text='Owner']"));
        }
        TestRail.addStepName("Clean up the environment");
        identityPO.deleteUser();
    }
}
