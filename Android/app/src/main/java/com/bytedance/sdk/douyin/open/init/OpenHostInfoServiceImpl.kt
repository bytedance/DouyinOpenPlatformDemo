package com.bytedance.sdk.douyin.open.init

import android.util.SparseArray
import com.bytedance.sdk.douyin.BuildConfig
import com.bytedance.sdk.open.aweme.core.OpenHostInfoService

class OpenHostInfoServiceImpl : OpenHostInfoService {
    override fun getDeviceId(): String {
        return ""
    }

    override fun getChannel(): String {
        return ""
    }

    override fun getAppId(): String {
        return ""
    }

    override fun getAppName(): String {
        return ""
    }

    override fun getUpdateVersionCode(): String {
        return ""
    }

    override fun getVersionCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    override fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getInstallId(): String {
        return ""
    }

    override fun extraInfo(): SparseArray<String>? {
        return null
    }
}