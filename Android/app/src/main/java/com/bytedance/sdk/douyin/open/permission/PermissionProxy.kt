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
import android.app.Fragment


import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread



internal class PermissionProxy : Fragment(), ISystemPermission {

    private var mCallback: SystemPermissionRequester.ResultCallback? = null
    private var mRequestPermissions: Array<out String>? = null
    private var mRequestCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(23)
    @UiThread
    override fun requestSystemPermission(
        activity: Activity,
        requestCode: Int,
        permissions: Array<out String>,
        callback: SystemPermissionRequester.ResultCallback
    ) {
        if (activity.isFinishing) {
            return
        }
        mCallback = callback
        mRequestPermissions = permissions
        mRequestCode = requestCode
        bindActivity(activity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (mRequestPermissions != null) {
            requestPermissions(mRequestPermissions!!, mRequestCode!!)
        } else {
            unbindActivity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCallback?.onPermissionResult(permissions, grantResults)
        SystemPermissionRequester.removeProxy(this)
        unbindActivity()
    }

    override fun removePermissionProxy() {
        unbindActivity()
    }

    /**
     * 绑定到Activity上
     *
     * @param activity Activity
     */
    private fun bindActivity(activity: Activity) {
        try {
            val fragmentManager = activity.fragmentManager
            fragmentManager.beginTransaction()
                .add(0, this)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
        }
    }

    /**
     * 解绑Activity
     */
    private fun unbindActivity() {
        try {
            val activity = activity ?: return
            val fragmentManager = activity.fragmentManager
            fragmentManager.beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
        }
    }
}