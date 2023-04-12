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
package com.bytedance.sdk.douyin.open.base.config

import com.bytedance.sdk.douyin.open.base.auth.DouYinUserBean

/**
 * 宿主实现相关逻辑
 */
interface IHostAuthService : IHostService {

    /**
     * 使用授权返回的code获取用户信息
     *  实现可以参考：
     *  1. code换取token  https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/account-permission/get-access-token
     *  2. token获取用户信息  https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/account-management/get-account-open-info
     */
    suspend fun getUserInfoByCode(clientKey: String, code: String, grantPermission: String): DouYinUserBean
}