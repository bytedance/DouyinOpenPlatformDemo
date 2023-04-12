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
package com.bytedance.sdk.douyin.open.ability.share

import android.media.MediaMetadataRetriever
import android.util.LruCache
import com.bytedance.sdk.douyin.open.CustomApplication
import com.bytedance.sdk.douyin.open.utils.FileUtils


object VideoConfig {

    private val videoInfosCache = LruCache<String, OpenVideoInfo>(20)

    fun getVideoInfo(path: String): OpenVideoInfo? {
        if (!FileUtils.checkFileExists(CustomApplication.context, path)) {
            return null
        }
        val openVideoInfo = videoInfosCache.get(path)
        if (openVideoInfo != null) {
            return openVideoInfo
        }
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) //宽
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) //高
        val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) //视频的方向角度
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) //视频的长
        val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

        return OpenVideoInfo().apply {
            this.width = width?.toInt() ?: 0
            this.height = height?.toInt() ?: 0
            this.bitrate = bitrate?.toInt() ?: 0
            this.duration = duration?.toInt() ?: 0
            this.rotation = rotation?.toInt() ?: 0
            videoInfosCache.put(path, this)
        }
    }
}