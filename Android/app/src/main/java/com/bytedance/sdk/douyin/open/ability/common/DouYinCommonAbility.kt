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
package com.bytedance.sdk.douyin.open.ability.common

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.bytedance.sdk.douyin.open.CustomApplication
import com.bytedance.sdk.douyin.open.ability.douyinapi.DouYinEntryActivity
import com.bytedance.sdk.open.aweme.CommonConstants
import com.bytedance.sdk.open.aweme.CommonConstants.SUPPORT
import com.bytedance.sdk.open.aweme.commonability.CommonAbility
import com.bytedance.sdk.open.aweme.utils.ThreadUtils
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import org.json.JSONException
import org.json.JSONObject

object DouYinCommonAbility {

    /**
     * 跳转抖音个人主页
     * 需要申请 内部权限下-抖音名片跳转 jump.profile 能力
     *
     */
    fun jumpDouYinUserProfile(activity: Activity, targetOpenId: String): Boolean {
        //方式一 使用封装好的方法
//        JumpUtils.jumpToDouyinProfile(activity, sourceOpenId, targetOpenId, "demo", DouYinEntryActivity::class.java.canonicalName)
        //方式二
        val douYinOpenApi = DouYinOpenApiFactory.create(activity)
        if (douYinOpenApi == null || !douYinOpenApi.isSupportCommonAbility(CommonAbility.COMMON_TYPE_JUMP_PROFILE)) {
            return false
        }
        val request = CommonAbility.Request()
        request.commonType = CommonAbility.COMMON_TYPE_JUMP_PROFILE
        request.mState = "state"
        request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
        val data = JSONObject()
        try {
            data.put("target_open_id", targetOpenId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        request.data = data.toString()
        request.extras = Bundle()
        request.extras.putInt("custom_max_video_count", 2) // 2或3
        request.extras.putString("opensdk_type", "card")
        return douYinOpenApi.openCommon(request)
    }

    /**
     * 跳转抖音会话页
     * 需要申请 内部权限下-会话页跳转 jump.im.conversation 能力
     */
    fun jumpDouYinUserContact(activity: Activity, targetOpenId: String): Boolean {
        //方式一
//        JumpUtils.jumpToDouyinIM(activity, sourceOpenId, targetOpenId, "demo", DouYinEntryActivity::class.java.canonicalName)
        // 方式二
        val douYinOpenApi = DouYinOpenApiFactory.create(activity)
        if (douYinOpenApi == null || !douYinOpenApi.isSupportCommonAbility(CommonAbility.COMMON_TYPE_JUMP_CONTACT)) {
            return false
        }
        val request = CommonAbility.Request()
        request.commonType = CommonAbility.COMMON_TYPE_JUMP_CONTACT
        request.mState = "mState"
        request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
        val data = JSONObject()
        try {
            data.put("target_open_id", targetOpenId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        request.data = data.toString()
        return douYinOpenApi.openCommon(request)
    }

    /**
     * 跳转作品管理页
     * 需要申请 内部权限下-跳转抖音名片自定义视频管理页 jump.profile.video_selection 能力
     */
    fun jumpVideoManager(activity: Activity, sourceOpenId: String): Boolean {
        val douYinOpenApi = DouYinOpenApiFactory.create(activity) ?: return false
        val supportApi = douYinOpenApi.isSupportApi(
            SUPPORT.COMMON_ABILITY,
            CommonConstants.SUPPORT.COMMON_API.COMMON_TYPE_JUMP_NEW_PRODUCTION
        )
        if (!supportApi) {
            return false
        }
        val request = CommonAbility.Request()
        request.commonType = CommonAbility.COMMON_TYPE_JUMP_PRODUCTION
        request.mState = "state"
        request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
        val data = JSONObject()
        try {
            data.put("from_open_id", sourceOpenId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        request.data = data.toString()
        request.extras = Bundle()
        request.extras.putInt("custom_max_video_count", 2) // 2或3
        request.extras.putString("opensdk_type", "card")
        return douYinOpenApi.openCommon(request)
    }


    fun onResponse(response: CommonAbility.Response) {
        //注意，跳转成功无回调，此处处理失败回调
        ThreadUtils.postInMain {
            val errorCode = response.errorCode
            Toast.makeText(
                CustomApplication.context,
                "跳转失败${response.commonType} errCode=${errorCode} msg=${response.errorMsg}", Toast.LENGTH_SHORT
            ).show()
        }
    }
}