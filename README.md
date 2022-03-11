## Usage

This is a Java Appium client. It needs an Appium Server to cooperate. 

* Connect to an AP7 panel or emulator.

* Install node.js :  https://nodejs.org/en/

* Install Appium Server :  http://appium.io/docs/en/about-appium/getting-started/#getting-started.

* Start Appium Server by ```appium --relaxed-security```

* Open this Java project by IntelliJ IDEA or other IDE. Make sure it can be built successfully.


## Reboot Test
Follow the steps below to run reboot test.

* Run by ```gradlew :reboottest:test --tests RebootRecycleTest.startRebootRecycleTest```

* Wait for a while of time

* Run by ```gradlew :reboottest:test --tests RebootRecycleTest.checkSystemStateAfterRebootTest```


## Telemetry Test
Telemetry Test should run in userdebug build.

* Run by ```gradlew --debug :telemetrytest:test```


## Notice

1. An odd exception when you connect Appium client to Appium server.

With the above usage, the Appium test can be used normally. We have run the Appium test successfully in Mac and Linux environment.

But maybe you will get an odd exception when you connect Appium client to Appium server in Docker environment, like following:
```
io.appium.java_client.service.local.InvalidServerInstanceException: Invalid server instance exception has occured: There is no installed nodes! Please install node via NPM (https://www.npmjs.com/package/appium#using-node-js) or download and install Appium app (http://appium.io/downloads.html)
    at io.appium.java_client.service.local.AppiumServiceBuilder.findNodeInCurrentFileSystem(AppiumServiceBuilder.java:156)
```
And you certainly has installed NPM.
The issue is like this one:
https://github.com/appium/appium/issues/6197

Then you can add a system environment variable in your Docker environment:
```APPIUM_BINARY_PATH=$YOUR_APPIUM_MAIN_JS_PATH```
such as:
```APPIUM_BINARY_PATH=/usr/local/lib/node_modules/appium/build/lib/main.js```
## Feedback testrail results
1. Add listening "@Listeners({TestRailListener.class})" before the suit, 
and call the APIClient package provided by testrail (if you are interested in it, you can take a look at it yourself)
2. Add the testcase id of the testrail above use case to each automated use case step,such as TestRail.setTestRailId("83014");
3. Modify the configuration file default_config.properties UPDATE_TESTRAIL_RESULTS=true //If true, will the automated
   execution result be fed back to testrail TESTRAIL_USERNAME=suiwenjing@nd.com.cn //The user account that filled in the
   result TESTRAIL_PASSWORD_KEY=E3vFUVAQKPPnzpQSDCD4-V7F7wLCaGp0Dtj4C11DM //Fill in the user password of the result (set
   for testrail users -> api keys)
   TESTRAIL_RUN_ID=3143 //test run id on testrail
4. After matching the relevant modifications, you can choose to execute a test case or a module test case on the idea
5. The above values can be read from environment variables for the java environment (docker container) when they are not
   defined in the properties file

### Note:

The expected flow of the values for TestRail from source to this code is for the UPDATE_TESTRAIL_RESULTS flag to remain
in the properties file while TESTRAIL_RUN_ID as a Bamboo plan variable, TESTRAIL_USER and TESTRAIL_PASSWORD_KEY are
available as global variables in Bamboo; these Bamboo variables can be passed in to the test runner (this code) as
environment variables.

## Wiki:

https://prometheanworld.atlassian.net/wiki/spaces/AP9/pages/13912703014/Promethean+Automation+Development
