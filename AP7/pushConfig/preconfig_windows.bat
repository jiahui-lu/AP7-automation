adb shell am force-stop io.appium.uiautomator2.server
adb shell rm -rf /sdcard/Download/*
adb shell mkdir /sdcard/com.promethean.certification
adb push exclude.json /sdcard/com.promethean.certification