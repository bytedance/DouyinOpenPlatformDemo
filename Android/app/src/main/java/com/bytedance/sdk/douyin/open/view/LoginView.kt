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
package com.bytedance.sdk.douyin.open.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bytedance.sdk.douyin.R
import com.bytedance.sdk.douyin.open.ability.auth.DouyinLoginManager
import com.bytedance.sdk.douyin.open.utils.FrescoUtils
import com.bytedance.sdk.open.aweme.utils.NoDoubleClickUtils
import com.facebook.drawee.view.SimpleDraweeView

/**
 * @author:zhanglei 2022/7/21
 */
class LoginView constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    LifecycleFrameLayout(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    private val view by lazy {
        LayoutInflater.from(context).inflate(R.layout.login_view_layout, this, true)
    }
    private val userIcon by lazy { view.findViewById<SimpleDraweeView>(R.id.user_icon) }
    private val nickname by lazy { view.findViewById<TextView>(R.id.user_nick_name) }
    private val loginButton by lazy { view.findViewById<Button>(R.id.login_button) }

    init {
        initView()
        DouyinLoginManager.inst().isLogin.observe(this) {
            refresh(it)
        }
    }


    private fun initView() {
        loginButton.setOnClickListener {
            if (NoDoubleClickUtils.isDoubleClick(view)) {
                Toast.makeText(context, "点击太频繁", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (DouyinLoginManager.inst().isLogin()) {
                DouyinLoginManager.inst().logout()
            } else {
                DouyinLoginManager.inst().login()
            }
        }
        refresh(DouyinLoginManager.inst().isLogin())
    }

    private var lastLoginState: Boolean? = null
    private fun refresh(isLogin: Boolean) {
        if (lastLoginState == isLogin) {
            return
        }
        lastLoginState = isLogin
        if (isLogin) {
            DouyinLoginManager.inst().douYinUser?.avatar?.let {
                FrescoUtils.bindImage(userIcon, it)
            }
            nickname.text = DouyinLoginManager.inst().douYinUser?.nickname ?: ""
            loginButton.text = "登出"
        } else {
            nickname.text = "未登录"
            loginButton.text = "登陆"
            userIcon.setImageResource(R.drawable.douyin_openplatform_icon)
        }
    }
}