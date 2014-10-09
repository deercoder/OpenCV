#!/bin/bash
# A simple script to test the SIFT detector based on OpenCV4Android and nonfree package
# Created by Guohui Wang
# Email: robertwgh_at_gmail_com
# Data: 5/22/2013

echo "|======================|"

echo "push the executable into the device"
#adb shell "rm -r /data/local/sift"
#adb shell "mkdir -p /data/local/sift"      // mkdir /sdcard/test
adb push ./libs/armeabi /sdcard/test
adb push ./img/img1.jpg /sdcard/test

echo "start SIFT computation"
echo "                        "
adb shell "cd /sdcard/test && LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/sdcard/test&& chmod 777 ./test_sift &&./test_sift img1.jpg img1_result_output.jpg"
echo "                        "

echo "fetch the result image back."
adb pull /data/local/sift/img1_result_output.jpg ./img

echo "|======================|"

