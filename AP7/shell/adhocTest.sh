#!/usr/bin/env bash

echo "run appium adhoctest script....."
pushd $(dirname $0)/../../ > /dev/null
adb shell am force-stop io.appium.uiautomator2.server
chmod a+x gradlew
testClass=${bamboo_testClass}
echo ${testClass}
if [ "${testClass}" = "" ]; then
        cmd="./gradlew --debug :AP7:test --configure-on-demand -info"
else
        cmd=""
        testlist=(${testClass//,/ })
        for test in ${testlist[@]}
        do
                cmd="${cmd} --tests ${test}"
        done
        cmd="./gradlew --debug :AP7:test${cmd} --configure-on-demand -info"
fi
echo $cmd
$cmd
popd  > /dev/null