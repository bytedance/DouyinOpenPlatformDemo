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
package com.bytedance.sdk.douyin.open.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.bytedance.sdk.douyin.R
import com.bytedance.sdk.douyin.open.ability.auth.AuthState
import com.bytedance.sdk.douyin.open.ability.auth.DouyinLoginManager
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_auth, container, false)
        root.findViewById<Button>(R.id.client_auth_bt)?.apply {
            DouyinLoginManager.inst().clientDouYinUserLiveData.observe(viewLifecycleOwner) { user ->
                if (user == null) {
                    text = "未授权"
                } else {
                    text = user.nickname ?: ""
                }
            }
            setOnClickListener {
                DouyinLoginManager.inst().tryDouYinAuth(AuthState.Client)
            }
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}