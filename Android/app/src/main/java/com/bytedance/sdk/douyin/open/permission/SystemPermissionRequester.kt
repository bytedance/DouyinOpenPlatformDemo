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
package com.bytedance.sdk.douyin.open.permission

import android.annotation.TargetApi
import android.app.Activity
import androidx.annotation.UiThread
import java.util.concurrent.CopyOnWriteArrayList

object SystemPermissionRequester {

    const val REQUEST_CODE = Short.MAX_VALUE - 2

    private val sPendingProxy: CopyOnWriteArrayList<ISystemPermission> = CopyOnWriteArrayList()

    @TargetApi(23)
    @UiThread
    fun requestPermissions(activity: Activity, permissions: Array<String>, callback: ResultCallback) {
        val proxy = createProxy(activity)
        sPendingProxy.add(proxy)
        proxy.requestSystemPermission(activity, REQUEST_CODE, permissions, callback)
    }

    fun removeProxy(proxy: ISystemPermission) {
        sPendingProxy.remove(proxy)
    }

    fun removeAllProxy() {
        if (sPendingProxy.isNotEmpty()) {
            sPendingProxy.forEach {
                it.removePermissionProxy()
            }
        }
    }

    private fun createProxy(activity: Activity): ISystemPermission {
        return PermissionProxy()

    }

    /**
     * 系统授权结果监听器
     */
    abstract class ResultCallback() {

        /**
         * 系统权限授权结果回调方法
         *
         * @param permissions 申请权限列表
         * @param grantResults 授权结果
         */
        abstract fun onPermissionResult(permissions: Array<out String>, grantResults: IntArray)
    }
}