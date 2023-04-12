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
package com.bytedance.sdk.douyin.open.ability.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.douyin.open.CustomApplication
import com.bytedance.sdk.douyin.open.CustomApplication.Companion.hostConfig
import com.bytedance.sdk.douyin.open.ability.douyinapi.DouYinEntryActivity
import com.bytedance.sdk.douyin.open.base.auth.DouYinUserBean
import com.bytedance.sdk.douyin.open.utils.ActivityStack
import com.bytedance.sdk.open.aweme.authorize.model.Authorization
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author:zhanglei 2022/7/21
 */
class DouyinLoginManager private constructor() {

    companion object {
        private const val SP_AUTH_FILE = "douyin_login_info_file"
        private const val KEY_HAS_LOGIN = "has_login"
        private const val KEY_DOU_YIN_USER = "dou_yin_user"
        private const val KEY_CLIENT_DOU_YIN_USER = "client_dou_yin_user"
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DouyinLoginManager()
        }

        fun inst() = instance
    }

    private val sp = CustomApplication.context.getSharedPreferences(SP_AUTH_FILE, Context.MODE_PRIVATE)


    val isLogin = MutableLiveData<Boolean>().apply {
        value = false
    }

    var douYinUser: DouYinUserBean? = null
        private set


    private val _scopes = MutableLiveData<String>().apply {
        value = "user_info"
    }
    var scopes: LiveData<String> = _scopes


    private val gson = Gson()

    var clientDouYinUserLiveData: MutableLiveData<DouYinUserBean> = MutableLiveData() // 存放客态用户信息，主要用来测试


    init {
        isLogin.postValue(sp.getBoolean(KEY_HAS_LOGIN, false))
        val userStr = sp.getString(KEY_DOU_YIN_USER, "")
        val douYinUserBean = gson.fromJson<DouYinUserBean>(userStr, DouYinUserBean::class.java)
        val clientUserBeanStr = sp.getString(KEY_CLIENT_DOU_YIN_USER, "")
        clientDouYinUserLiveData.postValue(gson.fromJson(clientUserBeanStr, DouYinUserBean::class.java))
        douYinUser = douYinUserBean
        isLogin.observeForever {
            saveData()
        }
    }

    private fun saveData() {
        try {
            sp.edit()
                .putBoolean(KEY_HAS_LOGIN, isLogin())
                .putString(KEY_DOU_YIN_USER, gson.toJson(douYinUser))
                .putString(KEY_CLIENT_DOU_YIN_USER, gson.toJson(clientDouYinUserLiveData.value))
                .apply()
        } catch (e: Exception) {

        }
    }

    fun isLogin(): Boolean = isLogin.value == true

    fun login() {
        tryDouYinAuth(AuthState.Host)
    }

    fun logout() {
        isLogin.postValue(false)
    }

    fun tryDouYinAuth(state: AuthState) {
        val randomUUID = UUID.randomUUID().toString()
        val request = Authorization.Request()
        request.scope = scopes.value ?: ""
        request.state = "${state.name}=$randomUUID"
        request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
        val activity = ActivityStack.topActivity
        val douYinOpenApi = DouYinOpenApiFactory.create(activity)
        douYinOpenApi?.authorize(request)
    }

    fun onAuthResponse(resp: Authorization.Response) {
        if (resp.isSuccess) {
            val authCode = resp.authCode
            val grantedPermissions = resp.grantedPermissions
            val state = resp.state ?: ""
            //上传到服务端换取access_token
            GlobalScope.launch(Dispatchers.Main) {
                val douYinUserBean = hostConfig.hostAuthService().getUserInfoByCode(hostConfig.getClientKey(), authCode, grantedPermissions)
                if (douYinUserBean.openId == null) {
                    Toast.makeText(CustomApplication.context, "获取用户信息失败", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                if (state.startsWith(AuthState.Host.name)) {
                    //主态授权
                    douYinUser = douYinUserBean

                    isLogin.postValue(true)
                }
                if (state.startsWith(AuthState.Client.name)) {
                    clientDouYinUserLiveData.postValue(douYinUserBean)
                }
            }
        }
    }
}

enum class AuthState {
    Host, Client
}