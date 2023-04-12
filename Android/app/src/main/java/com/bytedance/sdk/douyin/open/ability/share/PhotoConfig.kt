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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.bytedance.sdk.douyin.open.CustomApplication
import com.bytedance.sdk.douyin.open.utils.FileUtils
import com.bytedance.sdk.douyin.open.utils.MediaUtils

object PhotoConfig {

    fun getImageWidthHeight(path: String?): IntArray? {
        if (!FileUtils.checkFileExists(CustomApplication.context, path)) {
            return intArrayOf(0, 0)
        }
        val options = BitmapFactory.Options()
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true
        decodeBitmap(path, options)
        /**
         * options.outHeight为原始图片的高
         */
        return intArrayOf(options.outWidth, options.outHeight)
    }

    @JvmStatic
    fun decodeBitmap(path: String?, option: BitmapFactory.Options?): Bitmap? {
        var bitmap: Bitmap? = null
        if (MediaUtils.isMediaUriPath(path)) {
            var fd: ParcelFileDescriptor? = null
            try {
                val context = CustomApplication.context
                fd = context.getContentResolver().openFileDescriptor(Uri.parse(path), "r")
                bitmap = BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor, null, option)
            } catch (exception: Exception) {
            } finally {
                fd?.close()
            }
        } else {
            try {
                bitmap = BitmapFactory.decodeFile(path, option)
            } catch (exception: Exception) {
            }
        }
        return bitmap
    }
}