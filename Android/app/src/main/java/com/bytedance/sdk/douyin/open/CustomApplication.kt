// Copyright 2023 ByteDance and/or its affiliates.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.bytedance.sdk.douyin.open

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.bytedance.sdk.douyin.BuildConfig
import com.bytedance.sdk.douyin.open.base.config.HostConfig
import com.bytedance.sdk.douyin.open.base.config.HostConfigManager
import com.bytedance.sdk.douyin.open.init.OpenHostInfoServiceImpl
import com.bytedance.sdk.douyin.open.init.OpenHostTicketServiceImpl
import com.bytedance.sdk.douyin.open.utils.ActivityStack
import com.bytedance.sdk.open.aweme.adapter.event.applog.OpenAppLogUtils
import com.bytedance.sdk.open.aweme.adapter.event.applog.OpenEventAppLogServiceImpl
import com.bytedance.sdk.open.aweme.adapter.image.picasso.PicassoOpenImageServiceImpl
import com.bytedance.sdk.open.aweme.adapter.okhttp.OpenNetworkOkHttpServiceImpl
import com.bytedance.sdk.open.aweme.init.DouYinOpenSDKConfig
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import com.facebook.drawee.backends.pipeline.Fresco

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        ActivityStack.init(this)
        initHostConfig()
        initDouYinOpenSDK()
        Fresco.initialize(this)
        Log.e(TAG, "CustomApplication onCreate")
    }

    private fun initDouYinOpenSDK() {
//        DouYinOpenApiFactory.init(DouYinOpenConfig(hostConfig.getClientKey()))
        if (BuildConfig.DEBUG) {
            DouYinOpenApiFactory.setDebuggable(true)
        }
        val douYinOpenSDKConfig = DouYinOpenSDKConfig.Builder()
            .context(this)
            .clientKey(hostConfig.getClientKey())
            .networkService(OpenNetworkOkHttpServiceImpl())
            .imageService(PicassoOpenImageServiceImpl())
            .hostInfoService(OpenHostInfoServiceImpl())
            .hostTicketService(OpenHostTicketServiceImpl())
            .eventService(OpenEventAppLogServiceImpl())
            .build()
        DouYinOpenApiFactory.initConfig(douYinOpenSDKConfig)
        //确保在用户同意隐私协议后调用
        OpenAppLogUtils.init(this, "local_test")
    }

    private fun initHostConfig() {
        //此处注入HostConfig实现
        try {
            Log.i(TAG, "initHostConfig start")
            val hostConfig = Class.forName("com.bytedance.sdk.douyin.open.adapter.HostConfigImpl")
            val instance = hostConfig.newInstance() as HostConfig
            Log.i(TAG, "initHostConfig success")
            HostConfigManager.registerHostConfig(hostConfig = instance)
        } catch (e: Exception) {
        }
    }


    companion object {
        private const val TAG = "CustomApplication"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: CustomApplication

        val hostConfig
            get() = HostConfigManager.getHostConfig()
    }
}