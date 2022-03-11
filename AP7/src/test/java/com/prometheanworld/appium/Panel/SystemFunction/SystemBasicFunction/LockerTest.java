package com.prometheanworld.appium.Panel.SystemFunction.SystemBasicFunction;

import com.nd.automation.core.action.Location;
import com.nd.automation.core.action.element.ElementHelper;
import com.nd.automation.core.action.screen.ScreenHelper;
import com.nd.automation.core.appium.Driver;
import com.nd.automation.core.kotlin.AssertKt;
import com.nd.automation.core.locator.Locator;
import com.nd.automation.core.log.Log;
import com.nd.automation.core.runner.TestStatusListener;
import com.prometheanworld.appium.frame.AppiumHelper;
import com.prometheanworld.appium.frame.BaseTest;
import com.prometheanworld.appium.frame.TestRail;
import com.prometheanworld.appium.frame.model.AP7.SettingsPO;
import com.prometheanworld.appium.frame.model.POFactory;
import com.prometheanworld.appium.frame.util.CommonOperator;
import com.prometheanworld.appium.frame.util.Locker;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static com.prometheanworld.appium.frame.AppiumHelper.startAppFromBottomMenu;
import static com.prometheanworld.appium.frame.AppiumHelper.waitForSeconds;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Listeners({TestStatusListener.class})
public class LockerTest extends BaseTest {

    private final SettingsPO settingsPO = POFactory.getInstance(SettingsPO.class);

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        ScreenHelper.clickAt(Location.CENTER);
    }
    /**
     Step description
     1.Click app icons on Locker, such as Whiteboard app
     2.Whiteboard should launch properly
     */
    @Test
    public void APP_01Locker(){
        TestRail.setTestRailId("83068");
        TestRail.addStepName("1.Click app icons on Locker, such as Whiteboard app");
        AppiumHelper.StartAppFromUnifiedLauncher("Whiteboard");
        TestRail.addStepName("2.Whiteboard should launch properly");
        AppiumHelper.waitForSeconds(3);
        Assert.assertTrue(Driver.getAndroidDriver().findElementById("android:id/close_window").getAttribute("package").equals("com.prometheanworld.whiteboard"));
        Driver.getAndroidDriver().findElementById("android:id/close_window").click();
    }

    @Test
    public void APP_02Locker(){
        TestRail.setTestRailId("83069");
        ScreenHelper.clickAt(0.5, 0.999);
        AppiumHelper.waitForSeconds(1);
        ElementHelper.click(By.xpath("//android.widget.TextView[@text='Locker']"));
        AppiumHelper.waitForSeconds(2);
        TestRail.addStepName("3.Drag Whiteboard icon on Locker to the adjacent icon location");
        List<AndroidElement> list1 = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_caption"));
        String appName = list1.get(0).getText();
        ElementHelper.longPressAndMoveTo(Locator.byText(appName), new Point(950, 571));
        Assert.assertTrue(list1.get(1).getText().equals(appName));

    }


    @Test
    public void Test_restartenableapp(){
        TestRail.setTestRailId("83608");
        settingsPO.openEnableApps();
        TestRail.addStepName("1.Open Chromium and go to any website");
        TestRail.addStepName("2.Click the three dots in the upper right corner, click (Add to Home screen), save a web shortcut in Locker");
        Locker.addShortcut("www.youku.com","youku");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("youku",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'youku']"));
        Point p = sh1.getCenter();
        TestRail.addStepName("3.long press the shortcut");
        TestRail.addStepName("4.drag icon to empty space in Locker");
        Locker.ScrollAndLongPressmoveShortcut("youku",1181,759);
        AndroidElement sh2 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'youku']"));
        Point s = sh2.getCenter();
        Assert.assertTrue(s.x==p.x);
        Assert.assertTrue(s.y==p.y);
        Locker.testDeleteShortcut("youku");
    }

    @Test
    public void TestDragapemptyspace(){
        TestRail.setTestRailId("83612");
        settingsPO.openEnableApps();
        TestRail.addStepName("1.Open Chromium and go to any website");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("FreeBox",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'FreeBox']"));
        Point p = sh1.getCenter();
        TestRail.addStepName("3.long press an app");
        TestRail.addStepName("drag icon to empty space in Locker");
        Locker.ScrollAndLongPressmoveShortcut("FreeBox",1181,759);
        AndroidElement sh2 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'FreeBox']"));
        Point s = sh2.getCenter();
        Assert.assertTrue(s.x==p.x);
        Assert.assertTrue(s.y==p.y);
    }

    @Test
    public void TestLockerOptimize(){
        TestRail.setTestRailId("83626");
        TestRail.addStepName("Switch \"Enable APPs\" to \"off\"");

        settingsPO.DisableApps();
        AppiumHelper.startAppFromBottomMenu("Locker");
        List<AndroidElement> list1 = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_caption"));
        Assert.assertEquals(list1.size(),3);
        TestRail.addStepName("2.Only three applications are visible in one screen of locker: PanelManagement, Setting and Update");
        CommonOperator.ScrollAndFindApp("PanelManagement",1);
        CommonOperator.ScrollAndFindApp("Settings",1);
        CommonOperator.ScrollAndFindApp("Update",1);
        settingsPO.openEnableApps();
        AppiumHelper.startAppFromBottomMenu("Locker");
        TestRail.addStepName("3.Switch \"Enable APPs\" to \"on\" ");
        CommonOperator.ScrollAndFindApp("Whiteboard",1);
    }

    /**Description: When long pressing shortcut, the icon, menu and lable will change LOI-01
     * 1. Open Chromium and go to any website
     * 2. Click the three dots in the upper right corner, click "Add to Home screen", save a web shortcut in Locker
     * 3. Long press this new shortcut
     * 4. Confirm that Icon flashes white, Menu appears and Lable of the shortcut disappears
     */
    @Test
    public void Test_LongPressShortcut(){
        TestRail.setTestRailId("83595");
        Log.info("-------------Test LongPressShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");

        Locker.ScrollAndLongPressShortcut("优酷");
        AssertKt.assertPresent(By.id("com.prometheanworld.unifiedlauncher:id/content"), 3);
        AssertKt.assertNotPresent(Locator.byTextContains("优酷"), 3);

        Locker.tapByCoordinate(200,200);
        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test LongPressShortcut End---------------");
    }

    /**Description: Tap outside of delete confirmation modal will cancel delete operation LOI-02, LOI-03
     * 1. Open Chromium and go to any website
     * 2. Click the three dots in the upper right corner, click "Add to Home screen", save a web shortcut in Locker
     * 3. Long press this new shortcut
     * 4. tap 'delete shortcut'
     * 5. delete confirmation appears, tap outside of the modal to close the modal/tap 'cancel' to close the modal
     * 6. confirm that the shortcut is still visible and can still access to website by clicking the shortcut
     */
    @Test
    public void Test_CancelDeleteShortcut(){
        TestRail.setTestRailId("83596,83597");
        Log.info("-------------Test CancelDeleteShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/del_item"));
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_negative"));
        AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text= '优酷']"));

        Locker.ScrollAndLongPressShortcut("优酷");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/del_item"));
        Locker.tapByCoordinate(200,200);
        AssertKt.assertPresent(By.xpath("//android.widget.TextView[@text= '优酷']"));

        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test CancelDeleteShortcut End---------------");
    }

    /**Description: Ability to delete browser shortcuts from locker LOI-04
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Open Chromium and go to any website
     * 3. Click the three dots in the upper right corner, click "Add to Home screen", save a web shortcut in Locker
     * 4. Long press this new shortcut
     * 5. tap 'delete shortcut'
     * 6. delete confirmation appears, tap 'delete' to confirm you want to delete shortcut
     * 7. confirm that the shortcut has been deleted. Other apps adjust to fit. Apps in another screen will not fit in this screen.
     * 8. Log out and log back in, make sure the deleted bookmark won't come back
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_DeleteShortcut(){
        TestRail.setTestRailId("83598");
        panelModel=AppiumHelper.getPanelModel();
        Log.info("-------------Test DeleteShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/del_item"));
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        startAppFromBottomMenu("Locker");
        Assert.assertFalse(CommonOperator.ScrollAndFindApp("优酷",1),"shortcut is not deleted");
        AppiumHelper.logOut("save");
        AppiumHelper.logIn("Owner",true,false);
        startAppFromBottomMenu("Locker");
        Assert.assertFalse(CommonOperator.ScrollAndFindApp("优酷",1),"shortcut is not deleted");
        Log.info("-------------Test DeleteShortcut End---------------");
    }

    /**Description: create two shortcuts, delete one of them will show correct name  LOI-05
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Open Chromium and go to any website
     * 3. Click the three dots in the upper right corner, click "Add to Home screen", save a web shortcut in Locker
     * 4. repeat steps 1 and 2 to add another same web shortcut but use a different name
     * 5. long press one of two new shortcuts
     * 6. tap 'delete shortcut', delete confirmation appears
     * 7. confirm that the displayed shortcut name is the same as the name of the shortcut you selected
     * 8. tap 'delete' to confirm you want to delete the shortcut
     * 9. Confirm that the shortcut you selected has been deleted, but the other one is still visible and can be access to websit by clicking it
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_DeleteOneOfTwoShortcut(){
        TestRail.setTestRailId("83599");
        Log.info("-------------Test DeleteOneOfTwoShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.addShortcut("www.youku.com","youku");
        Locker.ScrollAndLongPressShortcut("优酷");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/del_item"));
        AssertKt.assertPresent(Locator.byTextContains("Delete the shortcut 优酷?"), 2);
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        startAppFromBottomMenu("Locker");
        Assert.assertFalse(CommonOperator.ScrollAndFindApp("优酷",1),"shortcut is not deleted");
        Assert.assertTrue(CommonOperator.ScrollAndFindApp("youku",1),"delete wrong shortcut");

        Locker.testDeleteShortcut("youku");
        Log.info("-------------Test DeleteOneOfTwoShortcut End---------------");
    }

    /**Description: Fail to rename shortcuts with more than 30 characters LOI-07
     * 1. Add and save a web shortcut in locker
     * 2. Long press this new shortcut
     * 3. Tap "rename shortcut", confirm that alphanumeric key pad slides up from bottom of panel
     * 4. Type characters through alphanumeric keypad
     * 5. When the number of characters reaches 30, no more characters can be entered
     */
    @Test
    public void Test_RenameMoreThanThirtyCharacters(){
        TestRail.setTestRailId("83601");
        Log.info("-------------Test RenameMoreThanThirtyCharacters Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        Locker.testRenameShortcut("thisisalongnamewithmorethan30character");
        AndroidElement changedET = Driver.getAndroidDriver().findElement(By.id("com.prometheanworld.unifiedlauncher:id/rename_edit"));
        Assert.assertTrue(changedET.getText().length()==30,"length longer than 30");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        AssertKt.assertPresent(Locator.byTextContains("thisisalongnamewithmorethan30c"), 2);

        Locker.longTouch("thisisalongnamewithmorethan30c");
        Locker.testRenameShortcut("thisisalongnamewithmorethan30");
        AndroidElement changed29ET = Driver.getAndroidDriver().findElement(By.id("com.prometheanworld.unifiedlauncher:id/rename_edit"));
        Assert.assertTrue(changed29ET.getText().length()<30,"length longer than 30");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        AssertKt.assertPresent(Locator.byTextContains("thisisalongnamewithmorethan30"), 2);

        Locker.longTouch("thisisalongnamewithmorethan30");
        Locker.testRenameShortcut("thisisalongnamewithmorethan30c");
        AndroidElement changed30ET = Driver.getAndroidDriver().findElement(By.id("com.prometheanworld.unifiedlauncher:id/rename_edit"));
        Assert.assertTrue(changed30ET.getText().length()==30,"length longer than 30");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        AssertKt.assertPresent(Locator.byTextContains("thisisalongnamewithmorethan30c"), 2);

        Locker.testDeleteShortcut("thisisalongnamewithmorethan30c");
        Log.info("-------------Test RenameMoreThanThirtyCharacters End---------------");
    }

    /**Description: Success to rename shortcuts with less than 30 characters LOI-08
     * 1. Add and save a web shortcut in locker
     * 2. Long press this new shortcut
     * 3. Tap "rename shortcut", confirm that alphanumeric key pad slides up from bottom of panel
     * 4. Type new name with less than 30 characters through alphanumeric keypad
     * 5. Click "SAVE", and confirm that the shortcut has been renamed successfully
     */
    @Test
    public void Test_RenameShortcut(){
        TestRail.setTestRailId("83602");
        Log.info("-------------Test RenameShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        Locker.testRenameShortcut("NormalName");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        waitForSeconds(2);
        CommonOperator.ScrollAndFindApp("NormalName",1);
        AssertKt.assertPresent(Locator.byTextContains("NormalName"), 2);

        Locker.testDeleteShortcut("NormalName");
        Log.info("-------------Test RenameShortcut End---------------");
    }

    /**Description: Success to rename shortcuts with less than 30 characters LOI-09
     * 1. Add and save a web shortcut in locker
     * 2. Long press shortcut icon
     * 3. Tap "rename shortcut", confirm that alphanumeric key pad slides up from bottom of panel
     * 4. Type new name with special characters through alphanumeric keypad, such as emoji
     * 5. Click "SAVE", and confirm that the shortcut has been renamed successfully
     */
    @Test
    public void Test_RenameSpecialShortcut(){
        TestRail.setTestRailId("83603");
        Log.info("-------------Test RenameSpecialShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        Locker.testRenameShortcut("\uD83D\uDE01!@#$%");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        waitForSeconds(2);
        CommonOperator.ScrollAndFindApp("\uD83D\uDE01!@#$%",1);
        AssertKt.assertPresent(Locator.byTextContains("\uD83D\uDE01!@#$%"), 2);

        Locker.testDeleteShortcut("\uD83D\uDE01!@#$%");
        Log.info("-------------Test RenameSpecialShortcut End---------------");
    }

    /**Description: Can't rename shortcuts with no character LOI-10
     * 1. Add and save a web shortcut in locker
     * 2. Long press shortcut icon
     * 3. Tap "rename shortcut", confirm that alphanumeric key pad slides up from bottom of panel
     * 4. Rename shortcuts with no character, can't click "SAVE"
     * 5. Fail to rename shortcuts with no character
     */
    @Test
    public void Test_FailToRenameWithNoCharacter(){
        TestRail.setTestRailId("83604");
        Log.info("-------------Test FailToRenameWithNoCharacter Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        Locker.testRenameShortcut("");
        AndroidElement btn_do_update_action = Driver.getAndroidDriver().findElement(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        Assert.assertEquals(btn_do_update_action.getAttribute("enabled"), "false");
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_negative"));
        waitForSeconds(1);
        CommonOperator.ScrollAndFindApp("优酷",1);
        waitForSeconds(1);
        AssertKt.assertPresent(Locator.byTextContains("优酷"), 2);

        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test FailToRenameWithNoCharacter End---------------");
    }

    /**Description: Success to creat same shortcuts name via Chromium LOI-12
     * 1. Open Chromium and go to any website
     * 2. Click the three dots in the upper right corner, click "Add to Home screen"
     * 3. Repeat step 2 to create a same name shortcut
     * 4. Confirm that there are two same name shortcuts in Locker
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_CreateSameNameShortcut(){
        TestRail.setTestRailId("83606");
        Log.info("-------------Test CreateSameNameShortcut Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.addShortcut("www.youku.com","优酷");
        startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("优酷",1);
        Assert.assertTrue(Driver.getAndroidDriver().findElements(By.xpath("//android.widget.TextView[@text= '优酷']")).size()==2,
                "cannot save as same name");

        Locker.testDeleteShortcut("优酷");
        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test CreateSameNameShortcut End---------------");
    }

    /**Description: Tap outside modal or "CANCLE" to close rename modal LOI-13
     * 1. Add and save a web shortcut in locker
     * 2. Long press a shortcut icon
     * 3. Tap "rename shortcut", confirm that alphanumeric key pad slides up from bottom of panel
     * 4. Type new name using alphanumeric keypad
     * 5. Tap outside modal or "CANCLE", confirm that the modal is closed, and the name of shortcut has no change
     */
    @Test
    public void Test_CancelToRename(){
        TestRail.setTestRailId("83607");
        Log.info("-------------Test CancelToRename Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.ScrollAndLongPressShortcut("优酷");
        Locker.testRenameShortcut("xxx");
        waitForSeconds(1);
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_negative"));
        AssertKt.assertPresent(Locator.byTextContains("优酷"), 2);
        Locker.longTouch("优酷");
        Locker.testRenameShortcut("xxx");
        Locker.tapByCoordinate(200,200);
        waitForSeconds(1);
        AssertKt.assertPresent(Locator.byTextContains("优酷"), 2);

        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test CancelToRename End---------------");
    }

    /**Description: Success to modify same name with existing app/shortcut in Locker LOI-11
     * 1. Switch "Enable APPs" to "on", confirm all apps are enable and are visible in the apps locker
     * 2. Add and save a web shortcut in locker
     * 3. Long press a shortcut icon
     * 5. Tap "rename shortcut", confirm that alphanumeric key pad slides up from bottom of panel
     * 6. Type same name with existing app/shortcut in locker
     * 7. There are two same icon names in locker
     */
    @Test
    public void Test_RenameWithExistingName(){
        TestRail.setTestRailId("83605");
        Log.info("-------------Test RenameWithExistingName Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.addShortcut("www.zhihu.com","知乎");
        Locker.ScrollAndLongPressShortcut("知乎");
        Locker.testRenameShortcut("优酷");
        waitForSeconds(1);
        ElementHelper.click(By.id("com.prometheanworld.unifiedlauncher:id/btn_positive"));
        Assert.assertTrue(Driver.getAndroidDriver().findElements(By.xpath("//android.widget.TextView[@text= '优酷']")).size()==2,
                "cannot save as same name");

        Locker.testDeleteShortcut("优酷");
        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test RenameWithExistingName End---------------");
    }

    /** Description: Can't move shortcut icons outside the locker LOI-06
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Unified Launcher --> Locker
     * 3. Long press an app
     * 4. Drag and drop it to the outside the locker
     * 5. Confirm that the locations of all apps won't change
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_MoveOutside(){
        TestRail.setTestRailId("83600");
        Log.info("-------------Test MoveOutside Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("优酷",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= '优酷']"));
        Point p = sh1.getLocation();
        Locker.testMoveOutside(p);
        CommonOperator.ScrollAndFindApp("优酷",1);
        AndroidElement sh2 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= '优酷']"));
        Point s = sh2.getLocation();
        Assert.assertTrue(p.x==s.x);
        Assert.assertTrue(p.y==s.y);

        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test Move End---------------");
    }

    /** Description: Move shorcuts in same screen LOI-15
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Open Chromium and go to any website
     * 3. Can drag and drop an shortcut icon to empty space in a screen
     * 4. Can move shortcut icons in-between two icons in same screen
     * 5. The dragged icon returns to 100% opacity, and label reappears. Other icons will adjust to fit.
     * 6. Log out and log back in, the order of the icons does not revert back
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_MoveShortcutInSameScreen(){
        TestRail.setTestRailId("83609");
        Log.info("-------------Test MoveInSameScreen Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("优酷",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= '优酷']"));
        Point p = sh1.getCenter();
        Locker.testMoveInside(p);
        List<AndroidElement> nameElements = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_caption"));
        Assert.assertTrue(nameElements.get(1).getText().equals("优酷"));

        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test MoveInSameScreen End---------------");
    }

    /**Description: Can't creat a folder when put two shortcut icons together LOI-17
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Open Chromium and go to any website
     * 3. Click the three dots in the upper right corner, click "Add to Home screen", save a web shortcut in Locker
     * 4. long press the shortcut
     * 5. drag one icon and drop it above another icon
     * 6. It will not create a folder. The dragged icon moves to the right side of the app, the following apps move backward one by one
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_MoveShortcutOntoAnothericon(){
        TestRail.setTestRailId("83611");
        Log.info("-------------Test MoveInSameScreen Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        Locker.addShortcut("www.youku.com","youku");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("优酷",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= '优酷']"));
        Point p = sh1.getCenter();

        AndroidElement sh2 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'youku']"));
        Point q = sh2.getCenter();
        int moveToPointX = p.x+50;
        int moveToPointY = p.y;

        Locker.testMove(q,moveToPointX,moveToPointY);

        AndroidElement sh3 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'youku']"));
        Point s = sh3.getCenter();
        Assert.assertTrue(s.x==q.x);
        Assert.assertTrue(s.y==q.y);

        Locker.testDeleteShortcut("优酷");
        Locker.testDeleteShortcut("youku");
        Log.info("-------------Test MoveInSameScreen End---------------");
    }

    /** Description: Move app icons in same screen LOI-19
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Unified Launcher --> Locker. Make sure that there are apps/shortcut icons and empty space in locker screen.
     * 3. Can drag and drop a app icon to empty space in a screen
     * 4. Can move app icons in-between two icons in same screen
     * 5. The dragged icon returns to 100% opacity, and label reappears. Other icons will adjust to fit.
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_MoveAppInSameScreen(){
        TestRail.setTestRailId("83613");
        Log.info("-------------Test MoveInSameScreen Start---------------");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("Settings",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'Settings']"));
        Point p = sh1.getCenter();
        Locker.testMoveInside(p);
        List<AndroidElement> nameElements = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_caption"));
        Assert.assertTrue(nameElements.get(1).getText().equals("Settings"));
        Log.info("-------------Test MoveInSameScreen End---------------");
    }

    /**Description: Can't creat a folder when put two app icons together LOI-21
     * 1. Android Settings --> Display --> Enable Apps --> Switch to "On"
     * 2. Unified Launcher --> Locker
     * 3. long press an app
     * 4. drag icon and drop it above the icon of another app
     * 5. It will not create a folder. The dragged icon moves to the right side of the app, the following apps move backward one by one
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_MoveAppOntoAnothericon(){
        TestRail.setTestRailId("83615");
        Log.info("-------------Test MoveInSameScreen Start---------------");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("Settings",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'Settings']"));
        Point p = sh1.getCenter();
        List<AndroidElement> elements = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_image"));
        Point q = elements.get(0).getCenter();
        int moveToPointX = q.x+50;
        int moveToPointY = q.y+40;
        Locker.testMove(p,moveToPointX,moveToPointY);
        List<AndroidElement> nameElements = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_caption"));
        Assert.assertTrue(nameElements.get(1).getText().equals("Settings"));
        Log.info("-------------Test MoveInSameScreen End---------------");
    }

    /** Description: New added shortcut stays in the last screen of locker LOI-23
     * 1. Unified Launcher -> Setting -> Display
     * 2. Switch "Enable APPs" to "on", confirm all apps are enable and visible in locker
     * 3. Creat shorcuts via Chromium or install new apps with GooglePlay
     * 4. Confirm that the added icon stays in last screen
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_FindShortcutInLastScreen(){
        TestRail.setTestRailId("83617");
        Log.info("-------------Test FindShortcutInLastScreen Start---------------");
        Locker.addShortcut("www.youku.com","优酷");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Locker.ScrollToLastPageAndFindApp("优酷",1);
        List<AndroidElement> nameElements = Driver.getAndroidDriver().findElements(By.id("com.prometheanworld.unifiedlauncher:id/icon_caption"));
        Assert.assertTrue(nameElements.get(nameElements.size()-1).getText().equals("优酷"));

        Locker.testDeleteShortcut("优酷");
        Log.info("-------------Test FindShortcutInLastScreen End---------------");
    }

    /** Description: Reinstalled app icon stays in the original position LOI-24
     * 1. Unified Launcher -> Setting -> Display
     * 2. Switch "Enable APPs" to "on", confirm all shortcuts/apps icons are enable and visible in locker
     * 3. Uninstall an app, and confirm that this app isn't visible in locker
     * 4. Reinstall same app in locker
     * 5. Confirm that the reinstalled icon stays in the original position(if the original position is occupied by other icons, the new added icon stays in the last screen of locker)
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_ReinstallApp(){
        TestRail.setTestRailId("83618");
        Log.info("-------------Test ReinstallApp Start---------------");
        AppiumHelper.startAppFromBottomMenu("Locker");
        CommonOperator.ScrollAndFindApp("Chromium",1);
        AndroidElement sh1 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'Chromium']"));
        Point p = sh1.getCenter();
        AppiumHelper.startAppFromBottomMenu("Home");

        Locker.openAppInfoPage("Chromium");
        Locker.disableApps("Chromium","DISABLE");
        AppiumHelper.startAppFromBottomMenu("Locker");
        assertFalse(CommonOperator.ScrollAndFindApp("Chromium",0),"Chromium still enable");
        ScreenHelper.clickAt(0.999, 0.999);

        Locker.openAppInfoPage("Chromium");
        Locker.disableApps("Chromium","ENABLE");
        AppiumHelper.startAppFromBottomMenu("Locker");
        assertTrue(CommonOperator.ScrollAndFindApp("Chromium",0),"Chromium still disable");

        AndroidElement sh2 = Driver.getAndroidDriver().findElement(By.xpath("//android.widget.TextView[@text= 'Chromium']"));
        Point q = sh2.getCenter();
        Assert.assertTrue(p.x==q.x);
        Assert.assertTrue(p.y==q.y);
        Log.info("-------------Test ReinstallApp End---------------");
    }

    /** Description: Hide icon using remote controller, icon can back to the original locationcan after restoring icon LOI-25
     * 1. Unified Launcher -> Setting -> Display
     * 2. Switch "Enable APPs" to "on", confirm all apps are enable and are visible in locker
     * 3. Perform the following remote controller inputs: Left, Left, Right, Right, Up, Up, Down, Down
     * 4. Click "LOCK ACCESS", confirm that those apps are hidden in Locker and "MORE SETTINGS" is hidden in the Locker Settings
     * 5. Perform step3 again to unlock those apps
     * 6. Hidden apps reappear and visible in locker
     * 7. If the their original positions are occupied by other apps, the locked apps will be added to the end
     * Note: Settings is not available in guest mode
     */
    @Test
    public void Test_ReappearLockedApp(){
        TestRail.setTestRailId("83619");
        Log.info("-------------Test ReappearSettingApp Start---------------");
        CommonOperator.executeShellCommand("setprop persist.sys.settings.remoteSecret true","");
        Locker.rebootPanelAndResetServices();
        waitForSeconds(30);
        //AppiumHelper.startAppFromBottomMenu("Home");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Assert.assertFalse(CommonOperator.ScrollAndFindApp("Settings",1),"found setting app");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Assert.assertTrue(CommonOperator.ScrollAndFindApp("Update",1)==false,"found Update app");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Assert.assertTrue(CommonOperator.ScrollAndFindApp("Panel Management",1)==false,"found Panel Management app");

        CommonOperator.executeShellCommand("setprop persist.sys.settings.remoteSecret false","");
        Locker.rebootPanelAndResetServices();
        waitForSeconds(30);
        //AppiumHelper.startAppFromBottomMenu("Home");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Assert.assertTrue(Locker.ScrollToLastPageAndFindApp("Settings",1)==true,"cannot found setting app");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Assert.assertTrue(Locker.ScrollToLastPageAndFindApp("Update",1)==true,"cannot found Update app");
        AppiumHelper.startAppFromBottomMenu("Locker");
        Assert.assertTrue(Locker.ScrollToLastPageAndFindApp("Panel Management",1)==true,"cannot found Panel Management app");
        Log.info("-------------Test ReappearSettingApp End---------------");
    }
}