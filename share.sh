#! /bin/bash  
gradle installDebug
adb shell am start -n com.hengye.share/.Launcher