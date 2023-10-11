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
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytedance.sdk.douyin.open.CustomApplication
import com.bytedance.sdk.douyin.open.ability.share.OpenMediaInfo
import com.bytedance.sdk.douyin.open.ability.share.OpenPhotoInfo
import com.bytedance.sdk.douyin.open.ability.share.PhotoConfig
import com.bytedance.sdk.douyin.open.ability.share.VideoConfig
import com.bytedance.sdk.douyin.open.utils.FileUtils
import com.bytedance.sdk.douyin.open.utils.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    private val _chooseMediaList = MutableLiveData<ArrayList<OpenMediaInfo>>()
    val chooseMediaList: LiveData<ArrayList<OpenMediaInfo>> = _chooseMediaList


    fun clearMedia() {
        _chooseMediaList.postValue(ArrayList<OpenMediaInfo>())
    }

    fun chooseMedia(fragment: ShareFragment, selectType: String) {
        realChooseMedia(fragment, selectType)
    }

    private fun realChooseMedia(fragment: ShareFragment, selectType: String) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        // 支持多选
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        if (selectType == OpenMediaInfo.MEDIA_VIDEO) {
            intent.type = "video/*"
        } else {
            intent.type = "image/*"
        }
        //需要用fragment启动
        fragment.startActivityForResult(intent, CHOOSE_MEDIA_REQUEST_CODE)
        return
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CHOOSE_MEDIA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val chooseUris = ArrayList<Uri>()
            data.data?.let {
                chooseUris.add(it)
            }
            data.clipData?.items()?.forEach {
                chooseUris.add(it.uri)
            }
            viewModelScope.launch(Dispatchers.IO) {
                // 处理uri
                val openMediaInfos = ArrayList<OpenMediaInfo>()
                chooseUris.forEachIndexed { index, uri ->
                    val contentType = MediaUtils.getContentTypeLocalFile(CustomApplication.context, uri)
                    val context = CustomApplication.context
                    val path = FileUtils.getPath(context, uri) ?: return@forEachIndexed
                    if (contentType.startsWith("video/")) {
                        val videoInfo = VideoConfig.getVideoInfo(path) ?: return@forEachIndexed
                        openMediaInfos.add(OpenMediaInfo(uri, OpenMediaInfo.MEDIA_VIDEO, path, 0).apply {
                            this.videoInfo = videoInfo
                        })
                    } else if (contentType.startsWith("image/")) {
                        val imageWidthHeight = PhotoConfig.getImageWidthHeight(uri.toString()) ?: return@forEachIndexed
                        val info = OpenMediaInfo(uri, OpenMediaInfo.MEDIA_IMAGE, path, 0).apply {
                            photoInfo = OpenPhotoInfo(imageWidthHeight[0], imageWidthHeight[1])
                        }
                        openMediaInfos.add(info)
                    }
                }
                val arrayList = _chooseMediaList.value ?: ArrayList()
                arrayList.addAll(openMediaInfos)
                _chooseMediaList.postValue(arrayList)
            }
        }
    }


    private fun ClipData.items(): List<ClipData.Item> {
        val itemList = mutableListOf<ClipData.Item>()
        for (i in 0 until itemCount) {
            itemList.add(getItemAt(i))
        }
        return itemList
    }

    companion object {
        const val CHOOSE_MEDIA_REQUEST_CODE = 10011;
    }
}