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
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtils {
    const val HTTP_SCHEMA = "http"
    private const val BYTE_SIZE: Int = 1024 * 2

    fun checkFileExists(context: Context, path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        return if (MediaUtils.isMediaUriPath(path)) {
            MediaUtils.isMediaFileExist(context, Uri.parse(path))
        } else File(path).exists()
    }

    fun getPath(context: Context, uri: Uri): String? {
        val startTime = System.currentTimeMillis()
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            return try {
                val path = convertUriToPath(context, uri)
                if (path != null && checkFileExists(context, path)) {
                    try {
                        //需要检查文件是否可读
                        FileInputStream(path).use {
                            it.close()
                        }
                        return path
                    } catch (e: Exception) {
                    }
                }
                getDataColumn(context, uri, null, null)
            } catch (e: Exception) {
                val shareDir = getMediaCopyDir(context)
                getFilePathFromURI(context, uri, shareDir)
            }
        }
        return uri.path
    }

    private fun getFilePathFromURI(context: Context, contentUri: Uri?, localPath: String): String? {
        val fileName = getFileName(context, contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            if (!checkFileExists(context, localPath)) {
                createFile(localPath, false)
            }
            val copyFile = File("$localPath${File.separator}${fileName}")
            copy(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    @Deprecated("")
    private fun convertUriToPath(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val schema = uri.scheme
        if (TextUtils.isEmpty(schema) || ContentResolver.SCHEME_FILE == schema) {
            return uri.path
        }
        if (HTTP_SCHEMA.equals(schema)) {
            return uri.toString()
        }
        if (ContentResolver.SCHEME_CONTENT == schema) {
            return getDataColumn(context, uri, null, null)
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        if (null != uri) {
            val column = "_data"
            val projection = arrayOf(column)
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                .use { cursor ->
                    cursor?.let {
                        if (cursor.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndexOrThrow(column)
                            val path = cursor.getString(columnIndex)
                            FileInputStream(path).use {}
                            return path
                        }
                    }
                }
        }
        return null
    }

    fun createFile(path: String?, isFile: Boolean): File? {
        if (!TextUtils.isEmpty(path)) {
            val f = File(path)
            if (!f.exists()) {
                if (!isFile) {
                    f.mkdirs()
                } else {
                    try {
                        val parent = f.parentFile
                        if (!parent.exists()) {
                            parent.mkdirs()
                        }
                        f.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return f
        }
        return null
    }

    private fun getDataColumnPath(
        context: Context, uri: Uri, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        try {
            val column = "_data"
            val projection = arrayOf(column)
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                .use { cursor ->
                    cursor?.let {
                        if (cursor.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndexOrThrow(column)
                            val path = cursor.getString(columnIndex)
                            return path
                        }
                    }
                }
        } catch (e: Exception) {
        }
        return null
    }

    fun copy(context: Context, srcUri: Uri?, dstFile: File, isProvider: Boolean = true) {
        try {
            srcUri?.let {
                val inputStream = if (isProvider) {
                    context.contentResolver.openInputStream(it)
                } else {
                    FileInputStream(
                        File(it.path)
                    )
                }
                inputStream ?: return
                val outputStream = FileOutputStream(dstFile)
                copyFile(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyFile(inputStream: InputStream, outputStream: FileOutputStream) {
        val buffer = ByteArray(BYTE_SIZE)
        inputStream.use { input ->
            outputStream.use { fileOut ->

                while (true) {
                    val length = input.read(buffer)
                    if (length <= 0)
                        break
                    fileOut.write(buffer, 0, length)
                }
                fileOut.flush()
                fileOut.close()
            }
        }
        inputStream.close()
    }

    fun getFileName(context: Context, uri: Uri?): String? {
        uri?.let {
            var fileName: String? = null
            var path = getDataColumnPath(context, uri, null, null)
            if (path == null) {
                path = uri.path ?: return null
            }
            val cut = path.lastIndexOf('/')
            if (cut != -1) {
                fileName = path.substring(cut + 1)
            }
            return fileName
        }
        return null
    }


    fun getMediaCopyDir(context: Context): String {
        return context.externalCacheDir?.absolutePath + "/newMedia"
    }


}