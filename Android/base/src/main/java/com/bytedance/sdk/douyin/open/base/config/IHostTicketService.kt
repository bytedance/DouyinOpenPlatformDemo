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

import com.bytedance.sdk.douyin.open.base.base.OpenResult

/**
 * 宿主实现相关逻辑
 * 主要用来请求服务端获取相关数据
 */
interface IHostTicketService : IHostService {

    suspend fun getClientCode(clientKey: String): OpenResult<String>

    suspend fun getAccessCode(clientKey: String, openid: String): OpenResult<String>
}