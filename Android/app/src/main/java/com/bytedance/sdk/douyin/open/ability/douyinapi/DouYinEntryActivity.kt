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
package com.bytedance.sdk.douyin.open.ability.douyinapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.sdk.douyin.open.ability.auth.DouyinLoginManager
import com.bytedance.sdk.douyin.open.ability.common.DouYinCommonAbility
import com.bytedance.sdk.open.aweme.authorize.model.Authorization
import com.bytedance.sdk.open.aweme.common.handler.IApiEventHandler
import com.bytedance.sdk.open.aweme.common.model.BaseReq
import com.bytedance.sdk.open.aweme.common.model.BaseResp
import com.bytedance.sdk.open.aweme.commonability.CommonAbility
import com.bytedance.sdk.open.aweme.share.Share
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import com.bytedance.sdk.open.douyin.ShareToContact
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi

class DouYinEntryActivity : AppCompatActivity(), IApiEventHandler {
    private var douYinOpenApi: DouYinOpenApi? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        douYinOpenApi = DouYinOpenApiFactory.create(this)
        douYinOpenApi?.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {

    }

    override fun onResp(resp: BaseResp?) {
        if (resp is Authorization.Response) {
            //处理授权结果
            DouyinLoginManager.inst().onAuthResponse(resp)
        } else if (resp is Share.Response) {
            // 处理分享结果

        } else if (resp is ShareToContact.Response) {
            //处理分享到好友结果
        } else if (resp is CommonAbility.Response) {
            DouYinCommonAbility.onResponse(resp)
        }
        finish()
    }

    override fun onErrorIntent(p0: Intent?) {

    }
}