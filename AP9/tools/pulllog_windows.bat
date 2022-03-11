adb shell rm -rf ../build/reports/tests/test/com.promethean.certification*
adb pull /sdcard/com.promethean.certification ../build/reports/tests/test
adb pull /sdcard/Download ../build/reports/tests/test
