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
package com.bytedance.sdk.douyin.open.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bytedance.sdk.douyin.databinding.FragmentCommonBinding
import com.bytedance.sdk.douyin.open.ability.auth.DouyinLoginManager
import com.bytedance.sdk.douyin.open.ability.common.DouYinCommonAbility

class CommonFragment : Fragment() {

    private var _binding: FragmentCommonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.jumpDouyinUserProfile.setOnClickListener {
            val host = DouyinLoginManager.inst().douYinUser?.openId
            val client = DouyinLoginManager.inst().clientDouYinUserLiveData.value?.openId
            if (host.isNullOrEmpty() || client.isNullOrEmpty()) {
                Toast.makeText(context, "请在授权页进行主客态授权登陆", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            DouYinCommonAbility.jumpDouYinUserProfile(requireActivity(), host, client)
        }
        binding.jumpDouyinUserContact.setOnClickListener {
            val host = DouyinLoginManager.inst().douYinUser?.openId
            val client = DouyinLoginManager.inst().clientDouYinUserLiveData.value?.openId
            if (host.isNullOrEmpty() || client.isNullOrEmpty()) {
                Toast.makeText(context, "请在授权页进行主客态授权登陆", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            DouYinCommonAbility.jumpDouYinUserContact(requireActivity(), host, client)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}