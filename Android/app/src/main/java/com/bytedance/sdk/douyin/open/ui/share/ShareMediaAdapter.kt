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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sdk.douyin.R
import com.bytedance.sdk.douyin.open.ext.setWidth
import com.bytedance.sdk.douyin.open.ability.share.OpenMediaInfo
import com.bytedance.sdk.douyin.open.utils.FrescoUtils
import com.bytedance.sdk.douyin.open.utils.MediaUtils
import com.bytedance.sdk.open.aweme.utils.UIUtils
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import kotlin.math.max
import kotlin.math.min

class ShareMediaAdapter() : RecyclerView.Adapter<ShareMediaAdapter.ShareMediaViewHolder>() {

    private val mediaList: ArrayList<OpenMediaInfo> = ArrayList<OpenMediaInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareMediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.openplatform_share_media_item, parent, false)
        return ShareMediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareMediaViewHolder, position: Int) {
        holder.bind(
            mediaList[position],
            holder.adapterPosition == 0,
            holder.adapterPosition == mediaList.size - 1
        )
    }

    override fun getItemCount() = mediaList.size

    fun setData(openMediaInfos: ArrayList<OpenMediaInfo>) {
        mediaList.clear()
        mediaList.addAll(openMediaInfos)
        notifyDataSetChanged()
    }

    inner class ShareMediaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val cardMaxWidth = UIUtils.dip2Px(view.context, 266f)
        private val cardMinWidth = UIUtils.dip2Px(view.context, 80f)

        fun bind(openMediaInfo: OpenMediaInfo, isFirst: Boolean, isLast: Boolean) {
            val sivSystemShareThing = view.findViewById<SimpleDraweeView>(R.id.siv_share_thing)
            var ivWidth = cardMaxWidth
            sivSystemShareThing?.hierarchy?.roundingParams =
                RoundingParams.fromCornersRadius(UIUtils.dip2Px(view.context, 12f))
            if (openMediaInfo.mediaType == OpenMediaInfo.MEDIA_VIDEO) {
                val tvVideoDuration = view.findViewById<TextView>(R.id.tv_video_duration)
                openMediaInfo.videoInfo?.let {
                    tvVideoDuration.text = getDuration(it.duration)
                    tvVideoDuration.visibility = View.VISIBLE
                    ivWidth = UIUtils.dip2Px(view.context, 200F) * it.width / it.height
                }
            } else {
                openMediaInfo.photoInfo?.let {
                    ivWidth = UIUtils.dip2Px(view.context, 200F) * it.width / it.height
                }
            }
            sivSystemShareThing.setWidth(max(min(ivWidth, cardMaxWidth), cardMinWidth).toInt())
            FrescoUtils.bindImage(
                sivSystemShareThing,
                MediaUtils.getFileUri(openMediaInfo.finalPath).toString()
            )

            view.layoutParams = ViewGroup.MarginLayoutParams(view.layoutParams).apply {
                marginStart = if (isFirst) {
                    UIUtils.dip2Px(view.context, 16f).toInt()
                } else {
                    UIUtils.dip2Px(view.context, 6f).toInt()
                }
                marginEnd = if (isLast) {
                    UIUtils.dip2Px(view.context, 16f).toInt()
                } else {
                    UIUtils.dip2Px(view.context, 6f).toInt()
                }
            }
        }


    }

    companion object {

        private const val MS = 1000
        private const val HOUR_OF_SECOND = 3600  // 秒
        private const val MINUTE_OF_SECOND = 60

        fun getDuration(duration: Int): String {
            if (duration <= 0) {
                return ""
            }

            var duration = duration / MS
            val hour = duration / HOUR_OF_SECOND
            duration -= hour * HOUR_OF_SECOND
            val minute = duration / MINUTE_OF_SECOND
            duration -= minute * MINUTE_OF_SECOND
            val second = if (duration > 0) duration else 1
            var durationText = ""
            if (hour > 0) {
                durationText += "${appendZero(hour)}:"
            }
            durationText += "${appendZero(minute)}:"
            durationText += appendZero(second)
            return durationText
        }

        private fun appendZero(time: Int) = if (time > 9) "$time" else "0$time"
    }
}