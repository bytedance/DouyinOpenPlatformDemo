package com.bytedance.sdk.douyin.open.business_module.kvstorage

import com.bytedance.sdk.douyin.open.CustomApplication

object LocalDataUtils {


    private val localData: IKVStorage by lazy {
        SharedPreferencesKvStorage(CustomApplication.context, "douyin_open_demo_local_data")
    }


    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return localData.getBoolean(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        localData.storeBoolean(key, value)
    }
}