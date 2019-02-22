LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := encrypt
LOCAL_SRC_FILES +=encrypt.c
include $(BUILD_SHARED_LIBRARY)