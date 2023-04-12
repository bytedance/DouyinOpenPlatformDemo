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
package com.bytedance.sdk.douyin.open.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.URLConnection

object MediaUtils {

    const val SCHEMA_CONTENT = "content"

    @JvmStatic
    fun isMediaUriPath(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }
        val uri: Uri?
        try {
            uri = Uri.parse(path)
            if (SCHEMA_CONTENT == uri?.scheme) {
                return true
            }
        } catch (exception: Exception) {
        }
        return false
    }

    @JvmStatic
    fun getFileUri(path: String?): Uri {
        if (isMediaUriPath(path)) {
            return Uri.parse(path)
        }
        return Uri.parse("file://$path")
    }

    @JvmStatic
    fun isMediaFileExist(context: Context, uri: Uri): Boolean {
        try {
            if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                context.contentResolver?.openInputStream(uri).use {
                    it?.close()
                    return true
                }
            }
            FileInputStream(uri.toString()).use {
                return true
            }
        } catch (e: FileNotFoundException) {
            return false
        }
    }

    fun getContentTypeLocalFile(context: Context, uri: Uri): String {
        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            return context.contentResolver.getType(uri) ?: ""
        }
        val fileMaps = URLConnection.getFileNameMap()
        return fileMaps.getContentTypeFor(uri.toString())
    }

    fun isMediaVideo(context: Context, uri: Uri): Boolean {
        return getContentTypeLocalFile(context, uri).startsWith("video/") ?: false
    }

    fun isMediaImage(context: Context, uri: Uri): Boolean {
        return getContentTypeLocalFile(context, uri).startsWith("image/") ?: false
    }


}