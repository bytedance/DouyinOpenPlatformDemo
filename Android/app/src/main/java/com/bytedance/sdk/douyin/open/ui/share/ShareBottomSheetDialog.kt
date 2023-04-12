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

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bytedance.sdk.douyin.R
import com.bytedance.sdk.douyin.open.ability.share.DouYinShare
import com.bytedance.sdk.douyin.open.ability.share.OpenMediaInfo
import com.bytedance.sdk.open.aweme.CommonConstants.SUPPORT.*
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import com.google.android.material.R.id.design_bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShareBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    private var mContentView: View? = null

    fun showDialog(activity: Activity, mediaList: ArrayList<OpenMediaInfo>) {
        if (isShowing) {
            return
        }
        mContentView = View.inflate(context, R.layout.share_bottom_sheet_dialog_layout, null)
        initView(mContentView!!, activity, mediaList)
        setContentView(mContentView!!)
        initWindow()
        show()
    }


    private fun initView(rootView: View, activity: Activity, mediaList: ArrayList<OpenMediaInfo>) {
        val douYinOpenApi = DouYinOpenApiFactory.create(activity)


        val supportShare = douYinOpenApi?.isAppSupportShare ?: false
        val shareToDouyinIcon = rootView.findViewById<ImageView>(R.id.share_to_douyin_icon)
        val shareToDouyinTextView = rootView.findViewById<TextView>(R.id.share_to_douyin_text)
        if (mediaList.isEmpty() || !supportShare) {
            shareToDouyinIcon.visibility = View.GONE
            shareToDouyinTextView.visibility = View.GONE
        } else {
            shareToDouyinIcon.setOnClickListener {
                DouYinShare.shareToDouYin(activity, mediaList)
                dismiss()
            }
        }

        val supportDaily = douYinOpenApi?.isSupportApi(SHARE, SHARE_API.SHARE_DAILY) ?: false
        val openMediaInfo = mediaList.firstOrNull()
        val shareDailyIcon = rootView.findViewById<ImageView>(R.id.share_daily_icon)
        val shareDailyTextView = rootView.findViewById<TextView>(R.id.share_to_daily_text)
        if (!supportDaily || openMediaInfo == null) {
            shareDailyIcon.visibility = View.GONE
            shareDailyTextView.visibility = View.GONE
        } else {
            shareDailyIcon.setOnClickListener {
                DouYinShare.shareDaily(activity, openMediaInfo)
                dismiss()
            }
        }

        //只支持图片
        val supportIm = douYinOpenApi?.isSupportApi(SHARE_IM, CONTACT_API.CONTACT_SUPPORT_IMAGE_HTML) ?: false
        val shareImIcon = rootView.findViewById<ImageView>(R.id.share_im_icon)
        val shareImText = rootView.findViewById<TextView>(R.id.share_to_im_text)
        val firstImage = mediaList.firstOrNull { it.mediaType == OpenMediaInfo.MEDIA_IMAGE }
        if (firstImage == null || !supportIm) {
            shareImIcon.visibility = View.GONE
            shareImText.visibility = View.GONE
        } else {
            shareImIcon.setOnClickListener {
                DouYinShare.shareImageToIm(activity, firstImage)
                dismiss()
            }
        }
    }

    private fun initWindow() {
        val bottomSheet = delegate.findViewById<FrameLayout>(design_bottom_sheet)
        bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
    }


}