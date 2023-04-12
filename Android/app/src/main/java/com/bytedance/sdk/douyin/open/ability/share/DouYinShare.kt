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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bytedance.sdk.douyin.open.CustomApplication
import com.bytedance.sdk.douyin.open.ability.auth.DouyinLoginManager
import com.bytedance.sdk.douyin.open.ability.douyinapi.DouYinEntryActivity
import com.bytedance.sdk.douyin.open.utils.FileUtils
import com.bytedance.sdk.open.aweme.CommonConstants
import com.bytedance.sdk.open.aweme.base.*
import com.bytedance.sdk.open.aweme.base.openentity.*
import com.bytedance.sdk.open.aweme.share.Share
import com.bytedance.sdk.open.aweme.utils.ThreadUtils
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import com.bytedance.sdk.open.douyin.ShareToContact
import com.bytedance.sdk.open.douyin.model.ContactHtmlObject
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

object DouYinShare {
    const val IMAGE = 1
    const val ALBUM = 2
    const val VIDEO = 3
    const val MIX = 4

    const val PACKAGE_NAME_DOUYIN = "com.ss.android.ugc.aweme"
    const val PACKAGE_NAME_DYLITE = "com.ss.android.ugc.aweme.lite"

    private val microAppInfo by lazy {
        MicroAppInfo().apply {
            appId = "tt07e3715e98c9aac0" //小程序appid
            appTitle = "小程序能力展示" //小程序title
            appUrl = "pages/component/dy-sticker/dy-sticker" // 小程序pathAndQuery,需要encode
            description = "描述" //小程序描述
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun shareToDouYin(activity: Activity, mediaList: ArrayList<OpenMediaInfo>?, mediaContentType: Int = MIX) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = Share.Request()
            val arrayList = ArrayList<String>()
            request.mMediaContent = MediaContent().apply {
                mMediaObject = when (mediaContentType) {
                    IMAGE -> ImageAlbumObject().apply {
                        mediaList?.forEach {
                            it.takeIf { it.mediaType == OpenMediaInfo.MEDIA_IMAGE }?.let {
                                val path = withContext(Dispatchers.IO) {
                                    copyFile(activity, it.uri)
                                }
                                arrayList.add(path)
                            }
                        }
                        mImagePaths = arrayList
                        isImageAlbum = false
                    }
                    ALBUM -> ImageAlbumObject().apply {
                        mediaList?.forEach {
                            it.takeIf { it.mediaType == OpenMediaInfo.MEDIA_IMAGE }?.let {
                                val path = withContext(Dispatchers.IO) {
                                    copyFile(activity, it.uri)
                                }
                                arrayList.add(path)
                            }
                        }
                        mImagePaths = arrayList
                        isImageAlbum = true //是否开启图集模式,图集下不支持贴纸
                    }
                    VIDEO -> VideoObject().apply {
                        mediaList?.forEach {
                            it.takeIf { it.mediaType == OpenMediaInfo.MEDIA_VIDEO }?.let {
                                val path = withContext(Dispatchers.IO) {
                                    copyFile(activity, it.uri)
                                }
                                arrayList.add(path)
                            }
                        }
                        mVideoPaths = arrayList
                    }
                    else -> MixObject().apply {
                        mediaList?.forEach {
                            val path = withContext(Dispatchers.IO) {
                                copyFile(activity, it.uri)
                            }
                            arrayList.add(path)
                        }
                        mMediaPaths = arrayList
                        isImageAlbum = true  //是否开启图集模式，当全是图片会开启图集模式
                    }
                }
            }
//        request.shareToPublish = true //直接分享到发布页
            request.mState = CustomApplication.hostConfig.hostShareService().getShareId() //填写获取的share_id,用于webhook回调
            // 小程序锚点
            request.mMicroAppInfo = microAppInfo
            // hashtag标签，推荐使用shareParam能精准插入hashtag位置
            request.mHashTagList = ArrayList<String>().apply {
                add("测试hash tag1")
                add(" 测试hash tag2 ")
                add("测试hashtag3")
            }
            request.newShare = true //开启新分享,仅当新分享开启时，shareParam参数才生效
            request.shareParam = ShareParam().apply {
                //标题，总长度有限制，过长会被截断
                titleObject = TitleObject().apply {
                    title = "这  还是新分享的标题 "
                    // 可多次添加
                    addMarker(HashtagTitleMarker().apply {
                        name = "直接插入标题中hashtag"
                        start = 2 //插入标题的位置，对应原始标题索引
                    })
                    addMarker(MentionTitleMarker().apply {
                        openId = DouyinLoginManager.inst().douYinUser?.openId ?: "" // @用户对应的openid
                        start = 3 //插入标题的位置，对应原始标题索引
                    })
                }
                // 贴纸
                stickersObject = StickersObject().apply {
                    addSticker(HashtagSticker().apply {
                        name = "HashtagSticker"
                    })
                    addSticker(MentionSticker().apply {
                        openId = DouyinLoginManager.inst().douYinUser?.openId ?: ""  // @用户对应的openid
                    })
                    addSticker(CustomSticker().apply {
                        mediaList?.forEach() {
                            //本地图片路径，请使用FileProvider
                            if (it.mediaType == OpenMediaInfo.MEDIA_IMAGE) {
                                //此处选择第一张图片做贴纸
                                path = withContext(Dispatchers.IO) {
                                    copyFile(activity, it.uri)
                                }
                                startTime = 0
                                endTime = 1000 * 1000
                                offsetX = 0.4f
                                offsetY = 0.4f
                                normalizedSizeX = 0.2f
                                normalizedSizeY = 0.2f
                                return@apply
                            }
                        }
                    })
                }
            }
            request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
            DouYinOpenApiFactory.create(activity)?.share(request)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun shareDaily(activity: Activity, openMediaInfo: OpenMediaInfo) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = Share.Request()
            request.mMediaContent = MediaContent().apply {
                mMediaObject = if (openMediaInfo.mediaType == OpenMediaInfo.MEDIA_IMAGE) {
                    ImageObject().also {
                        val mImagePaths = ArrayList<String>()
                        val path = withContext(Dispatchers.IO) {
                            copyFile(activity, openMediaInfo.uri)
                        }
                        mImagePaths.add(path) //只支持一张图,图片路径，推荐为FileProvider形式
                        it.mImagePaths = mImagePaths
                    }
                } else {
                    VideoObject().also {
                        val mVideoPaths = ArrayList<String>()
                        val path = withContext(Dispatchers.IO) {
                            copyFile(activity, openMediaInfo.uri)
                        }
                        mVideoPaths.add(path) //只支持单个视频，视频路径，推荐为FileProvider形式
                        it.mVideoPaths = mVideoPaths
                    }
                }
            }
            request.shareToType = 1; // 类型为发日常
            request.mState = CustomApplication.hostConfig.hostShareService().getShareId()
            request.mMicroAppInfo = microAppInfo
            request.newShare = true
            request.shareParam = ShareParam().apply {
                // H5链接，需要在开平aweme.share scope下配置
                shareDailyH5Path = "https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/web-app/h5/share-to-h5/"
                titleObject = TitleObject().apply {
                    title = "title"
                }
                stickersObject = StickersObject().apply {
                    addSticker(HashtagSticker().apply { name = "HashtagSticker" })
                }
            }
            request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName

            val douYinOpenApi = DouYinOpenApiFactory.create(activity)
            //判断是否支持日常，可以根据这个是否隐藏日常图标  当不支持时候走兜底逻辑，正常分享
            if (douYinOpenApi.isSupportApi(CommonConstants.SUPPORT.SHARE, CommonConstants.SUPPORT.SHARE_API.SHARE_DAILY)) {
                douYinOpenApi?.share(request)
            } else {
                Toast.makeText(activity, "当前抖音版本不支持发日常", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareImageToIm(activity: Activity, openMediaInfo: OpenMediaInfo) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = ShareToContact.Request()
            request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
            request.mState = "shareImageToIm"
            request.mMediaContent = MediaContent().apply {
                mMediaObject = ImageObject().apply {
                    val path = withContext(Dispatchers.IO) {
                        copyFile(activity, openMediaInfo.uri)
                    }
                    mImagePaths = arrayListOf(path)
                }
            }
            val douYinOpenApi = DouYinOpenApiFactory.create(activity)
            //判断是否支持，可以根据这个是否隐藏图标
            if (douYinOpenApi.isAppSupportShareToContacts) {
                douYinOpenApi?.shareToContacts(request)
            } else {
                Toast.makeText(activity, "当前抖音版本不支持分享给好友", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 分享Html给好友
     * 如果您想要分享 HTML 链接到抖音联系人，请先到官网管理中心的您应用下的 im.share 权限添加分享链接进行页面链接验证，才可以成功分享
     */
    fun shareHtmlToIm(activity: Activity, contactHtmlObject: ContactHtmlObject) {
        val request = ShareToContact.Request()
        request.callerLocalEntry = DouYinEntryActivity::class.java.canonicalName
        request.mState = "shareImageToIm"
        request.htmlObject = contactHtmlObject
        val douYinOpenApi = DouYinOpenApiFactory.create(activity)
        //判断是否支持，可以根据这个是否隐藏图标
        if (douYinOpenApi.isAppSupportShareToContacts) {
            douYinOpenApi?.shareToContacts(request)
        } else {
            ThreadUtils.postInMain {
                Toast.makeText(activity, "当前抖音版本不支持分享给好友", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * 此处来模拟app内使用FileProvider分享图片、视频到抖音
     */
    fun copyFile(context: Context, uri: Uri): String {
        val path = FileUtils.getPath(context, uri) ?: ""
        val uriParts = path.split('.')
        require(uriParts.isNotEmpty()) { "读取拓展名错误" }
        val suffix = uriParts.last().trim()
        require(suffix.isNotEmpty()) { "无效的拓展名$suffix" }
        val shareDirectory = File(context.getExternalFilesDir(null), "/newMedia")
        if (!shareDirectory.exists()) {
            shareDirectory.mkdir()
        }
        val tempFile = File.createTempFile("share_demo_", ".$suffix", shareDirectory)
        tempFile.deleteOnExit()
        val openInputStream = context.contentResolver.openInputStream(uri)
        openInputStream.use {
            val fileOutputStream = FileOutputStream(tempFile)
            it?.copyTo(fileOutputStream)
        }
        return shareWithProvider(context, tempFile)
    }

    private fun shareWithProvider(context: Context?, file: File): String {
        return context?.let {
            val uri = FileProvider.getUriForFile(
                it,
                "${context.packageName}.fileProvider", file
            )
            //分别给抖音、抖音极速版授权
            context.grantUriPermission(
                PACKAGE_NAME_DYLITE,
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            context.grantUriPermission(
                PACKAGE_NAME_DOUYIN,
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            uri.toString()
        } ?: ""
    }
}