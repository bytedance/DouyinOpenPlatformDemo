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
package com.bytedance.sdk.douyin.open.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytedance.sdk.douyin.R
import com.bytedance.sdk.douyin.databinding.FragmentShareBinding
import com.bytedance.sdk.douyin.open.ability.share.DouYinShare
import com.bytedance.sdk.douyin.open.ability.share.OpenMediaInfo

class ShareFragment : Fragment() {

    private var _binding: FragmentShareBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val shareViewModel by lazy {
        ViewModelProvider(this).get(ShareViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initMediaView()
        binding.chooseImageBt.setOnClickListener {
            activity?.let {
                shareViewModel.chooseMedia(this, OpenMediaInfo.MEDIA_IMAGE)
            }
        }
        binding.chooseVideoBt.setOnClickListener {
            activity?.let {
                shareViewModel.chooseMedia(this, OpenMediaInfo.MEDIA_VIDEO)
            }
        }
        binding.clearAllMediaBt.setOnClickListener {
            shareViewModel.clearMedia()
        }
        binding.shareBt.setOnClickListener {
            val openMediaInfos = shareViewModel.chooseMediaList.value
            if (openMediaInfos.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "请选择文件", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            activity?.let {
                val shareBottomSheetDialog = ShareBottomSheetDialog(it)
                shareBottomSheetDialog.showDialog(it, openMediaInfos)
            }
        }

        val shareHtmlToImButton = binding.shareHtmlToIm
        shareHtmlToImButton.setOnClickListener {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.fragment_container, ShareHtmlFragment())
            fragmentTransaction.commitAllowingStateLoss()
        }
        binding.shareOpenRecord.setOnClickListener {
            DouYinShare.openRecord(requireActivity())
        }
        return root
    }


    private fun initMediaView() {
        val shareMediaRv = binding.shareMediaRv
        val shareMediaAdapter = ShareMediaAdapter()
        shareMediaRv.adapter = shareMediaAdapter
        shareMediaRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        shareViewModel.chooseMediaList.observe(viewLifecycleOwner) {
            shareMediaAdapter.setData(it)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        shareViewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}