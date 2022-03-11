#!/usr/bin/env bash
echo "run appium AP9 automation script....."
pushd $(dirname $0)/../../ > /dev/null
adb shell am force-stop io.appium.uiautomator2.server
chmod a+x gradlew
testClass=${bamboo_testClass}
testPriority=${bamboo_testPriority}
excludeGroups=${bamboo_excludeGroups}
echo "testClass: "${testClass}
echo "testPriority: "${testPriority}
echo "excludeGroups: "${excludeGroups}

if [ "${testPriority}" != "" ]; then
        sed -i "/suites \"testng.xml\"/a\        includeGroups \"${testPriority}\"" testng.gradle
fi
if [ "${excludeGroups}" != "" ]; then
        sed -i "/suites \"testng.xml\"/a\        excludeGroups \"${excludeGroups}\"" testng.gradle
fi
cat testng.gradle

if [ "${testClass}" = "" ]; then
        cmd="./gradlew --debug :AP9:test --configure-on-demand -info"
else
        cmd=""
        testlist=(${testClass//,/ })
        for test in ${testlist[@]}
        do
                cmd="${cmd} --tests ${test}"
        done
        cmd="./gradlew --debug :AP9:test${cmd} --configure-on-demand -info"
fi
echo $cmd
$cmd

if [ "${testPriority}" != "" ]; then
        sed -i "/        includeGroups \"${testPriority}\"/d" testng.gradle
fi
if [ "${excludeGroups}" != "" ]; then
        sed -i "/        excludeGroups \"${excludeGroups}\"/d" testng.gradle
fi
popd  > /dev/null