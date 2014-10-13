# Android.mk for compiling sift_test using OpenCV4Android
# Created by Guohui Wang
# Email: robertwgh_at_gmail_com
# Data: 02/25/2014

LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := nonfree_prebuilt
LOCAL_SRC_FILES := libnonfree.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := opencv_java_prebuilt
LOCAL_SRC_FILES := libopencv_java.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
# Modify LOCAL_C_INCLUDES with your path to OpenCV for Android.
LOCAL_C_INCLUDES:= /home/changliu/OpenCV-2.4.9-android-sdk/sdk/native/jni/include
LOCAL_C_INCLUDES += /home/changliu/android-ndk-r10b/platforms/android-15/arch-arm/usr/include
LOCAL_MODULE    := nonfree_jni
LOCAL_CFLAGS    := -Werror -O3 -ffast-math
LOCAL_LDLIBS    += -llog -ldl -L/home/changliu/android-ndk-r10b/platforms/android-15/arch-arm/usr/lib/ -landroid -lz  -L$(LOCAL_PATH) -lzip
LOCAL_SHARED_LIBRARIES := nonfree_prebuilt opencv_java_prebuilt
LOCAL_SRC_FILES := nonfree_jni.cpp
include $(BUILD_SHARED_LIBRARY)
