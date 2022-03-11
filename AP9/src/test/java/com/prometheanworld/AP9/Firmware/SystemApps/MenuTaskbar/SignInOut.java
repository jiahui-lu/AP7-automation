package com.prometheanworld.AP9.Firmware.SystemApps.MenuTaskbar;

import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.locator.Locator;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.configurations.ConfigurationReader;
import com.prometheanworld.appium.frame.enumdata.LongPressMenu;
import com.prometheanworld.appium.frame.hardware.PrometheanKey;
import com.prometheanworld.appium.frame.hardware.PrometheanKeyboard;
import com.prometheanworld.appium.frame.model.AP9.IdentityPO;
import com.prometheanworld.appium.frame.model.AP9.SettingsPO;
import com.prometheanworld.appium.frame.model.AP9.WelcomeScreenPO;
import com.prometheanworld.appium.frame.model.POFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SignInOut extends BaseTest {
    public IdentityPO identityPO = POFactory.getInstance(IdentityPO.class);
    public SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);
    public WelcomeScreenPO welcomeScreenPO = POFactory.getInstance(WelcomeScreenPO.class);

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        super.beforeMethod();
        systemPO.resetInitialEnvironment();
    }

    /**
     * C115923 Verify that guest can click option "Sign in" in avatar menu to sign in
     * When panel is in Guest(Sign-out) mode and online(connect to network), click on user icon;----User layout appears
     * Click on option "Sign in";----Identity app should open,User layout disappears
     * C115689 Verify that user icon shows the correct state of the Identity signed in/out status
     * When panel is in Guest(Sign-out) mode, oberve user icon on the left of the menu bar;----User icon should show the Guest gray generic avatar and name
     * When panel is in user(Sign-in) mode, observe user icon on the left of the menu bar;---- User icon should show the corresponding user  profile avatar and name
     * C115774 Verify that User Layout shows the correct options when panel is in user mode
     * When panel is in user(sign-in) mode, click on user icon, observe options in user layout;----'My account','Lock screen','Sign out'
     */
    @Test(groups= "P0")
    public void C115923C115689C115774VerifyThatGuestCanClickOptionToSignIn() {
        TestRail.setTestRailId("115923,115689,115774");

        TestRail.addStepName("When panel is in Guest(Sign-out) mode,User icon should show the Guest gray generic avatar and name");
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Guest']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@content-desc='Switch User']"));
        systemPO.hideMenuBar();

        TestRail.addStepName("Sign in panel,When panel is in user(Sign-in) mode,User icon should show the corresponding user  profile avatar and name");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail(true);
        Assert.assertEquals(systemPO.getUserName(),ConfigurationReader.promeUsername,"User name error");

        TestRail.addStepName("When panel is in user(sign-in) mode, click on user icon, observe options in user layout;----'My account','Lock screen','Sign out'");
        systemPO.startMenuBar();
        AppiumHelper.findElementAndClick("Xpath",systemPO.eleMenuUser);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='My account']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Lock Screen']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Sign out']"));
        systemPO.hideMenuBar();

        systemPO.signOut(false,false);
        AppiumHelper.clickAt(0.8,0.8);
        systemPO.setPropMdmEnvByCmd("production");
    }

    /** zhangkaichun
     * C115465 Verify that Welcome modal appears on the screen when panel has either ethernet or wifi detected
     * Panel has either ethernet or wifi detected, whether or not there is a net
     * When user is in Guest mode;
     * Wake up or reboot panel
     * Observe the main screen;----Users can see both the welcome mode and the menu displayed on the home screen,Two access options:Sign in, Continue as Guest
     * Turn panel to standby, press power button to wake up;----Users can see both the welcome mode and the menu displayed on the home screen
     * Sign in an account;----Users can't see welcome modal displayed on the main screen
     * Wake up or reboot panel;----Panel turns into Guest mode,Users can see both the welcome mode and the menu displayed on the home screen
     */
    //@Test(groups= "P1")
    public void C115465VerifyThatWelcomeModalAppearsOnTheScreenWhenPanelHasEitherEthernetOrWifiDetected() {
        TestRail.setTestRailId("115465");

        TestRail.addStepName("Wake up or reboot panel;Users can see both the welcome mode and the menu displayed on the home screen,Two access options:Sign in, Continue as Guest");
        AppiumHelper.rebootPanel();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.unifiedlauncher:id/toolbar_layout"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Welcome Screen' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));

        TestRail.addStepName("Turn panel to standby, press power button to wake up;Users can see both the welcome mode and the menu displayed on the home screen");
        AppiumHelper.clickKey(PrometheanKey.Power, PrometheanKeyboard.ActivPanel);
        AppiumHelper.waitForSeconds(2);
        AppiumHelper.clickKey(PrometheanKey.Power, PrometheanKeyboard.ActivPanel);
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.unifiedlauncher:id/toolbar_layout"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Welcome Screen' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        AppiumHelper.clickAt(0.5,0.5);
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Sign In' and @resource-id='com.prometheanworld.welcomescreen:id/sign_in']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Continue as Guest' and @resource-id='com.prometheanworld.welcomescreen:id/continue_as_guest']"));

        TestRail.addStepName("Sign in an account,Users can't see welcome modal displayed on the main screen");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
        Assert.assertEquals(systemPO.getUserName(),ConfigurationReader.promeUsername,"User name error");
        systemPO.startMenuBar();
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Welcome Screen' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));

        TestRail.addStepName("Wake up or reboot panel;Panel turns into Guest mode,Users can see both the welcome mode and the menu displayed on the home screen");
        AppiumHelper.rebootPanel();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.unifiedlauncher:id/toolbar_layout"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Welcome Screen' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        AppiumHelper.clickAt(0.8,0.8);
        String userName = systemPO.getUserName();
        Assert.assertEquals(userName,"Guest","Panel turns into Guest mode");

        systemPO.setPropMdmEnvByCmd("production");
    }

    /**
     * C115926 Verify that user can click option "My account" to launch their myPromethean account service app
     * When panel is in user(sign-in) mode, click on user icon
     * Click on option "My account";----User layout disappears，User can launch their myPromethean account service app
     */
    @Test(groups= "P1")
    public void C115926VerifyThatUserCanClickOptionMyAccountToLaunchTheirMyPrometheanAccountServiceApp() {
        TestRail.setTestRailId("115926");

        TestRail.addStepName("Sign in panel");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail(true);
        Assert.assertEquals(systemPO.getUserName(), ConfigurationReader.promeUsername, "User name error");

        TestRail.addStepName("Click on option \"My account\";User layout disappears,User can launch their myPromethean account service app");
        systemPO.startMenuBar();
        AppiumHelper.findElementAndClick("Xpath", systemPO.eleMenuUser);
        AppiumHelper.findElementAndClick("Xpath", "//*[@text='My account']");
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Lock Screen']"));
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Applications']"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + ConfigurationReader.promeUsername + "' and @resource-id='com.prometheanworld.identity:id/tv_display_name']"));

        systemPO.closeAppOnMenuBar("Identity");
        systemPO.signOut(false, false);
        AppiumHelper.clickAt(0.8, 0.8);
        systemPO.setPropMdmEnvByCmd("production");
    }

    /**
     * C119988 Verify that guest can restore pinned app after an authenticated user logs out
     * When guest has pinned 3 apps, ex. File, Chromium, Settings
     * Click on avatar icon on the menu bar to log in a user account that has no pinned apps
     * Observe menu bar;----Confirm that no apps pinned to menu bar
     * Log out user account to return guest mode, observe menu bar;----Confirm that 3 previous pinned apps appear on the menu bar
     * Click on avatar icon on the menu bar to log in a user account that has 3 pinned apps, ex. Dosbox, File, screen share
     * Observe menu bar;----Confirm that 3 apps pinned to menu bar, Dosbox, File, screen share
     */
    @Test(groups= "P1")
    public void C119988VerifyThatGuestCanRestorePinnedAppAfterAnAuthenticatedUserLogsOut() {
        TestRail.setTestRailId("119988");

        TestRail.addStepName("When guest has pinned 1 apps, ex. Files");
        String guestApp = "Panel Management";
        systemPO.startAppFromUnifiedLauncher(guestApp);
        systemPO.clickLongPressMenu(guestApp, LongPressMenu.KeepInMenu, true);
        systemPO.closeAppOnMenuBar(guestApp);

        TestRail.addStepName("Click on avatar icon on the menu bar to log in a user account that has no pinned apps,Confirm that Files apps not pinned to menu bar");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
        Assert.assertEquals(systemPO.getUserName(), ConfigurationReader.promeUsername, "User name error");
        systemPO.startMenuBar();
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='" + guestApp + "']"));

        TestRail.addStepName("Set 'Chromium' app 'keep in menu' for user");
        String userApp = "Chromium";
        systemPO.startAppFromUnifiedLauncher(userApp);
        systemPO.clickLongPressMenu(userApp, LongPressMenu.KeepInMenu, true);
        systemPO.closeAppOnMenuBar(userApp);

        TestRail.addStepName("Log out user account to return guest mode, observe menu bar;----Confirm that previous pinned apps appear on the menu bar");
        systemPO.signOut(false, false);
        systemPO.startMenuBar();
        AppiumHelper.clickAt(0.8, 0.8);
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + guestApp + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        systemPO.clickLongPressMenu(guestApp, LongPressMenu.KeepInMenu, false);

        TestRail.addStepName("Click on avatar icon on the menu bar to log in a user account that has 1 pinned apps, observe menu bar;Confirm that Chromium app pinned appear on the menu bar");
        systemPO.signIn();
        identityPO.signInWithEmail();
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + userApp + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        systemPO.clickLongPressMenu(userApp, LongPressMenu.KeepInMenu, false);

        systemPO.signOut(false, false);
        systemPO.setPropMdmEnvByCmd("production");
    }

    /**
     * C119989 Verify that guest can restore boot-up app after an authenticated user logs out
     * When panel is in guest mode, long press app icons on the menu
     * Click on "Open at Start up" to set 3 boot-up apps, ex. File, Chromium, Settings
     * Click on avatar icon on the menu bar to log in a user account that has no boot-up apps
     * Observe the panel screen;----Confirm that no apps boot up
     * Log out user account to return guest mode, Observe the panel screen;----Confirm that 3 previous boot-up apps boot up
     * Click on avatar icon on the menu bar to log in a user account that has 3 boot-up apps, ex. Dosbox, File, screen share
     * Observe the panel screen;----Confirm that 3 boot-up apps boot up, Dosbox, File, screen share
     */
    @Test(groups= "P1")
    public void C119989VerifyThatGuestCanRestoreBootupAppsAfterAnAuthenticatedUserLogsOut() {
        TestRail.setTestRailId("119989");

        TestRail.addStepName("When panel is in guest mode, long press app icons on the menu, Click on 'Open at log in' to set 3 boot-up apps, ex. File, Chromium, Settings");
        String[] guestApp = {"Panel Management", "Settings", "Chromium"};
        for (String s : guestApp) {
            systemPO.startAppFromUnifiedLauncher(s);
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtStartUp, true);
            systemPO.closeAppOnMenuBar(s);
        }

        TestRail.addStepName("Click on avatar icon on the menu bar to log in a user account that has no boot-up apps,Confirm that no apps boot up");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail();
        systemPO.startMenuBar();
        for (String s : guestApp) {
            ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='" + s + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        }

        TestRail.addStepName("Set Adobe Acrobat,Update,EULA apps 'Open at log in' for user");
        String[] userApp = {"Adobe Acrobat", "Update", "VLC"};
        for (String s : userApp) {
            systemPO.startAppFromUnifiedLauncher(s);
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtLogIn, true);
            systemPO.closeAppOnMenuBar(s);
        }

        TestRail.addStepName("Log out user account to return guest mode, Observe the panel screen,Confirm that 3 previous boot-up apps boot up");
        systemPO.signOut(false, false);
        for (String s : guestApp) {
            systemPO.startMenuBar();
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + s + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtStartUp, false);
            systemPO.closeAppOnMenuBar(s);
        }

        TestRail.addStepName("Click on avatar icon on the menu bar to log in a user account that has 3 boot-up apps, ex. Dosbox, File, screen share;Confirm that 3 boot-up apps boot up," + userApp);
        systemPO.signIn();
        identityPO.signInWithEmail();
        for (String s : userApp) {
            systemPO.startMenuBar();
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + s + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtLogIn, false);
            systemPO.clickLongPressMenu(s, LongPressMenu.Close, false);
        }

        systemPO.signOut(false, false);
        systemPO.setPropMdmEnvByCmd("production");
    }

    /**
     * C119990 Verify that user can restore pinned app after loging in
     * When user has pinned 3 apps, ex. File, Chromium, Settings
     * Click on avatar icon on the menu bar to log out to guest that has no pinned apps
     * Observe menu bar;----Confirm that no apps pinned to menu bar
     * Click on avatar icon on the menu bar to log in user again, observe menu bar;----Confirm that 3 previous pinned apps appear on the menu bar
     * Click on avatar icon on the menu bar to log out to guest that has 3 pinned apps, ex. Dosbox, File, screen share
     * Observe menu bar;----Confirm that 3 apps pinned to menu bar, Dosbox, File, screen share
     */
    @Test(groups= "P1")
    public void C119990VerifyThatUserCanRestorePinnedAppAfterLogingIn() {
        TestRail.setTestRailId("119990");

        TestRail.addStepName("Sign in panel");
        systemPO.setPropMdmEnvByCmd("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail(true);
        Assert.assertEquals(systemPO.getUserName(), ConfigurationReader.promeUsername, "User name error");

        TestRail.addStepName("user has pinned 1 apps, ex. Files");
        String userApp = "Panel Management";
        systemPO.startAppFromUnifiedLauncher(userApp);
        systemPO.clickLongPressMenu(userApp, LongPressMenu.KeepInMenu, true);
        systemPO.closeAppOnMenuBar(userApp);

        TestRail.addStepName("Click on avatar icon on the menu bar to log out to guest that has no pinned apps,Confirm that Files app not pinned to menu bar");
        systemPO.signOut(false, false);
        systemPO.startMenuBar();
        AppiumHelper.clickAt(0.8, 0.8);
        systemPO.startMenuBar();
        ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='" + userApp + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));

        TestRail.addStepName("Set 'Chromium' app 'keep in menu' for guest");
        String guestApp = "Chromium";
        systemPO.startAppFromUnifiedLauncher(guestApp);
        systemPO.clickLongPressMenu(guestApp, LongPressMenu.KeepInMenu, true);
        systemPO.closeAppOnMenuBar(guestApp);

        TestRail.addStepName("Click on avatar icon on the menu bar to log in user again, observe menu bar;Confirm that Files app pinned appear on the menu bar");
        systemPO.signIn();
        identityPO.signInWithEmail();
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + userApp + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        systemPO.clickLongPressMenu(userApp, LongPressMenu.KeepInMenu, false);

        TestRail.addStepName("Click on avatar icon on the menu bar to log out to guest that has 1 pinned apps,Confirm that Chromium apps pinned to menu bar");
        systemPO.signOut(false, false);
        systemPO.startMenuBar();
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + guestApp + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        systemPO.clickLongPressMenu(guestApp, LongPressMenu.KeepInMenu, false);
        systemPO.setPropMdmEnvByCmd("production");
    }

    /**
     * C119991 Verify that user can restore boot-up apps after loging in
     * When panel is in user mode, long press app icons on the menu
     * Click on "Open at Start up" to set 3 boot-up apps, ex. File, Chromium, Settings
     * Click on avatar icon on the menu bar to log out to guest that has no boot-up apps
     * Observe the panel screen;----Confirm that no apps boot up
     * Log in user account, observe the panel screen;----Confirm that 3 previous boot-up apps boot up
     * Click on avatar icon on the menu bar to log out to guest that has 3 boot-up apps, ex. Dosbox, File, screen share
     * Observe the panel screen;----Confirm that 3 apps boot-up to menu bar, Dosbox, File, screen share
     */
    @Test(groups= "P1")
    public void C119991VerifyThatUserCanRestoreBootupAppsAfterLogingIn() {
        TestRail.setTestRailId("119991");

        TestRail.addStepName("Sign in panel");
        systemPO.setPropMdmEnvByFile("sandbox");
        systemPO.signIn();
        identityPO.signInWithEmail(true);
        Assert.assertEquals(systemPO.getUserName(), ConfigurationReader.promeUsername, "User name error");

        TestRail.addStepName("Click on 'Open at log in' to set 3 boot-up apps, ex. File, Chromium, Settings");
        String[] userApp = {"Cloud Connect", "Chromium", "Settings"};
        for (String s : userApp) {
            systemPO.startAppFromUnifiedLauncher(s);
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtLogIn, true);
            systemPO.closeAppOnMenuBar(s);
        }

        TestRail.addStepName("Click on avatar icon on the menu bar to log out to guest that has no boot up apps,Confirm that Files,Chromium,Settings app not boot up");
        systemPO.signOut(false, false);
        systemPO.startMenuBar();
        AppiumHelper.clickAt(0.8, 0.8);
        systemPO.startMenuBar();
        for (String s : userApp) {
            ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='" + s + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        }

        TestRail.addStepName("Set Panel Management,Update,EULA apps 'Open at start up' for guest");
        String[] guestApp = {"Panel Management", "Update", "VLC"};//"Adobe Acrobat"
        for (String s : guestApp) {
            systemPO.startAppFromUnifiedLauncher(s);
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtStartUp, true);
            systemPO.closeAppOnMenuBar(s);
        }

        TestRail.addStepName("Log in user account, observe the panel screen;Confirm that 3 previous boot-up apps boot up");
        systemPO.signIn();
        identityPO.signInWithEmail();
        for (String s : userApp) {
            systemPO.startMenuBar();
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + s + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtLogIn, false);
            systemPO.clickLongPressMenu(s, LongPressMenu.Close, false);
        }

        TestRail.addStepName("Click on avatar icon on the menu bar to log out to guest that has 3 boot-up apps,Confirm that 3 apps boot-up, Adobe Acrobat,Update,EULA");
        systemPO.signOut(false, false);
        for (String s : guestApp) {
            systemPO.startMenuBar();
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='" + s + "' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
            systemPO.clickLongPressMenu(s, LongPressMenu.OpenAtStartUp, false);
            systemPO.clickLongPressMenu(s, LongPressMenu.Close, false);
        }
        systemPO.setPropMdmEnvByCmd("production");
    }

    /** zhangkaichun
     * C115691 Verify that say-hello title in Guest Layout can display different prompts according to the local time
     * Panel is in Guest mode
     * Set local time to morning, click on user icon, observe say-hello title in the user layout;----say-hello title: Good morning
     * Set local time to afternoon, click on user icon, observe say-hello title in the user layout;----say-hello title: Good afternoon
     * Set local time to evening, click on user icon, observe say-hello title in the user layout;----say-hello title: Good evening
     */
    @Test(groups= "P2")
    public void C115691VerifyThatSayhelloTitleInGuestLayoutCanDisplayDifferentPromptsAccordingToTheLocalTime() {
        TestRail.setTestRailId("115691");

        String app = "Settings";
        systemPO.startAppFromUnifiedLauncher(app);
        settingsPO.navToDateTime();
        Map<String,String> checkTime = new HashMap<>();
        checkTime.put("6","Good morning");
        checkTime.put("14","Good afternoon");
        checkTime.put("1","Good evening");

        for (String t : checkTime.keySet()) {
            TestRail.addStepName("Set local time to "+checkTime.get(t).split(" ")[1]+", click on user icon, observe say-hello title in the user layout;say-hello title: "+checkTime.get(t));
            settingsPO.setTime(t, "30");
            systemPO.startMenuBar();
            AppiumHelper.findElementAndClick("Xpath", "//*[@resource-id='com.prometheanworld.unifiedlauncher:id/user_text']");
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='"+checkTime.get(t)+"' and @resource-id='com.prometheanworld.unifiedlauncher:id/tv_title']"));
            AppiumHelper.clickAt(0.8,0.8);
        }

        AppiumHelper.setSwitchWidget(settingsPO.eleAutomaticDateTime,true);
        systemPO.closeAppOnMenuBar(app);
    }

    /** zhangkaichun
     * C115467 Verify that Welcome modal should disappear when users touch outside the modal
     * When user is in Guest mode and panel is online, wake up or reboot panel;----Users can see both the welcome mode and the menu displayed on the home screen,Two access options:Sign in, Continue as Guest
     * Touch outside the welcome modal;----Welcome modal should disappear and the user should remain in Guest mode
     * Repeat step1, use panel button or remote control to open Source window, AVISettings and highlight Application icon;----Welcome modal should disappear and the user should remain in Guest mode
     */
    //@Test(groups= "P2")
    public void C115467VerifyThatWelcomeModalShouldDisappearWhenUsersTouchOutsideTheModal() {
        TestRail.setTestRailId("115467");

        for (int i=1; i<=3; i++) {
            TestRail.addStepName("When user is in Guest mode and panel is online, wake up or reboot panel;Users can see both the welcome mode and the menu displayed on the home screen,Two access options:Sign in, Continue as Guest");
            AppiumHelper.rebootPanel();
            ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.unifiedlauncher:id/toolbar_layout"));
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Welcome Screen' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
            AppiumHelper.clickAt(0.5, 0.5);
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Sign In' and @resource-id='com.prometheanworld.welcomescreen:id/sign_in']"));
            ElementHelper.waitUntilPresent(By.xpath("//*[@text='Continue as Guest' and @resource-id='com.prometheanworld.welcomescreen:id/continue_as_guest']"));

            switch (i) {
                case 1:
                    TestRail.addStepName("Touch outside the welcome modal;Welcome modal should disappear and the user should remain in Guest mode");
                    AppiumHelper.clickAt(0.8, 0.8);
                    break;
                case 2:
                    TestRail.addStepName("use panel button or remote control to open Source window;Welcome modal should disappear and the user should remain in Guest mode");
                    AppiumHelper.clickKey(PrometheanKey.Sources,PrometheanKeyboard.ActivPanel);
                    break;
                case 3:
                    TestRail.addStepName("use panel button or remote control to open AVISettings;Welcome modal should disappear and the user should remain in Guest mode");
                    AppiumHelper.clickKey(PrometheanKey.VolumeDown,PrometheanKeyboard.RemoteControl);
                    break;
            }
            ElementHelper.waitUntilNotPresent(By.xpath("//*[@text='Sign In' and @resource-id='com.prometheanworld.welcomescreen:id/sign_in']"));
            String userName = systemPO.getUserName();
            Assert.assertEquals(userName, "Guest", "the user should remain in Guest mode");
        }
    }

    /** zhangkaichun
     * C115466 Verify that Welcome modal can be used normally
     * When user is in Guest mode and panel is online, wake up or reboot panel;----Users can see both the welcome mode and the menu displayed on the home screen,Two access options:Sign in, Continue as Guest
     * Select "Sign in" from the Welcome modal;----it should launch the Identity app on the AP9
     * Close the Identity app;----Welcome modal should disappear and the user should remain in Guest mode
     * Reboot the panel
     * Select "Continue as Guest";----Welcome modal should disappear and the user should remain in Guest mode
     */
    //@Test(groups= "P2")
    public void C115466VerifyThatWelcomeModalCanBeUsedNormally() {
        TestRail.setTestRailId("115466");

        TestRail.addStepName("When user is in Guest mode and panel is online, wake up or reboot panel;Users can see both the welcome mode and the menu displayed on the home screen,Two access options:Sign in, Continue as Guest");
        AppiumHelper.rebootPanel();
        ElementHelper.waitUntilPresent(Locator.byResourceId("com.prometheanworld.unifiedlauncher:id/toolbar_layout"));
        ElementHelper.waitUntilPresent(By.xpath("//*[@text='Welcome Screen' and @resource-id='com.prometheanworld.unifiedlauncher:id/menu_icon_title']"));
        AppiumHelper.clickAt(0.5,0.5);
        ElementHelper.waitUntilPresent(By.xpath(welcomeScreenPO.eleSignIn));
        ElementHelper.waitUntilPresent(By.xpath(welcomeScreenPO.eleContinueAsGuest));

        TestRail.addStepName("Select 'Sign in' from the Welcome modal;it should launch the Identity app on the AP9");
        AppiumHelper.findElementAndClick("Xpath",welcomeScreenPO.eleSignIn);
        ElementHelper.waitUntilPresent(By.xpath(identityPO.eleSignInWithEmail));

        TestRail.addStepName("Close the Identity app;Welcome modal should disappear and the user should remain in Guest mode");
        AppiumHelper.findElementAndClick("Xpath",identityPO.eleCloseApp);
        ElementHelper.waitUntilNotPresent(By.xpath(identityPO.eleSignInWithEmail));
        ElementHelper.waitUntilNotPresent(By.xpath(welcomeScreenPO.eleSignIn));
        ElementHelper.waitUntilNotPresent(By.xpath(welcomeScreenPO.eleContinueAsGuest));
        String userName = systemPO.getUserName();
        Assert.assertEquals(userName,"Guest","the user should remain in Guest mode");

        TestRail.addStepName("Reboot the panel,Select 'Continue as Guest';Welcome modal should disappear and the user should remain in Guest mode");
        AppiumHelper.rebootPanel();
        AppiumHelper.clickAt(0.5,0.5);
        AppiumHelper.findElementAndClick("Xpath",welcomeScreenPO.eleContinueAsGuest);
        ElementHelper.waitUntilNotPresent(By.xpath(welcomeScreenPO.eleSignIn));
        ElementHelper.waitUntilNotPresent(By.xpath(welcomeScreenPO.eleContinueAsGuest));
        userName = systemPO.getUserName();
        Assert.assertEquals(userName,"Guest","the user should remain in Guest mode");
    }

}
