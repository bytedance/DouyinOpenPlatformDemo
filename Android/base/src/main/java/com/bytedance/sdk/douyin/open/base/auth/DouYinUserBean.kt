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
package com.bytedance.sdk.douyin.open.base.auth

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class DouYinUserBean {
    @SerializedName("avatar")
    var avatar: String? = null

    @SerializedName("client_key")
    var clientKey: String? = null

    @SerializedName("nickname")
    var nickname: String? = null

    @SerializedName("open_id")
    var openId: String? = null

    @SerializedName("union_id")
    var unionId: String? = null
}