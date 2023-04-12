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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bytedance.sdk.douyin.databinding.FragmentShareHtmlBinding
import com.bytedance.sdk.douyin.open.ability.share.DouYinShare
import com.bytedance.sdk.open.douyin.model.ContactHtmlObject

class ShareHtmlFragment : Fragment() {

    private var _binding: FragmentShareHtmlBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShareHtmlBinding.inflate(inflater, container, false)

        val webView = binding.htmlWebview
        initWebView(webView)
        webView.loadUrl("https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/share/android-share-with-douyin")
        binding.shareFb.setOnClickListener {
            val htmlObject = ContactHtmlObject().apply {
                html = webView.url
                title = webView.title
                discription = "分享给抖音好友或群"
                // 你的html的封面图(远程图片) （选填，若不填，则使用开放平台官网申请时上传的图标）
//                thumbUrl = "https://lf3-static.bytednsdoc.com/obj/eden-cn/kuLauvyM-tyvmahsWulwV-upfbvK/ljhwZthlaukjlkulzlp/pc/Logo.png"
            }
            DouYinShare.shareHtmlToIm(requireActivity(), htmlObject)
        }
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(webView: WebView) {
        val settings = webView.settings
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}