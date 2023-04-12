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

import android.net.Uri


class OpenMediaInfo(val uri: Uri, val mediaType: String, var finalPath: String, val originIndex: Int) {
    var videoInfo: OpenVideoInfo? = null
    var photoInfo: OpenPhotoInfo? = null


    companion object {
        const val MEDIA_VIDEO = "video"
        const val MEDIA_IMAGE = "image"
    }
}

class OpenVideoInfo {
    var width: Int = 0
    var height: Int = 0
    var rotation: Int = 0
    var duration: Int = 0
    var bitrate: Int = 0
    var fps: Int = 0
    var codec: Int = 0
    var keyFrameCount: Int = 0
    var maxDuration: Int = 0
}

class OpenPhotoInfo(val width: Int, val height: Int)