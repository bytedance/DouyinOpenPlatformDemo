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

import android.graphics.Bitmap
import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder

object FrescoUtils {

    fun bindImage(draweeView: SimpleDraweeView?, url: String?) {
        bindImage(draweeView, url, -1, -1)
    }


    fun bindImage(draweeView: SimpleDraweeView?, url: String?, resizeWidth: Int, resizeHeight: Int) {
        bindImage(draweeView, url, resizeWidth, resizeHeight, Bitmap.Config.ARGB_8888)
    }

    fun bindImage(draweeView: SimpleDraweeView?, url: String?, resizeWidth: Int, resizeHeight: Int, config: Bitmap.Config?) {
        bindImage(draweeView, url, resizeWidth, resizeHeight, config, null)
    }

    fun bindImage(
        draweeView: SimpleDraweeView?, url: String?, resizeWidth: Int, resizeHeight: Int,
        config: Bitmap.Config?, controllerListener: ControllerListener<ImageInfo?>?
    ) {
        if (draweeView == null) {
            return
        }
        var resizeOptions: ResizeOptions? = null
        if (resizeWidth > 0 && resizeHeight > 0) {
            resizeOptions = ResizeOptions(resizeWidth, resizeHeight)
        }
        val uri = Uri.parse(url)
        val builder = ImageRequestBuilder.newBuilderWithSource(uri)
        if (null != resizeOptions) {
            builder.resizeOptions = resizeOptions
        }
        if (null != config) {
            val decodeOptionsBuilder = ImageDecodeOptions.newBuilder()
            decodeOptionsBuilder.bitmapConfig = config
            val imageDecodeOptions = ImageDecodeOptions(decodeOptionsBuilder)
            builder.imageDecodeOptions = imageDecodeOptions
        }
        val imageRequest = builder.build()
        val controller: DraweeController = Fresco.newDraweeControllerBuilder().setOldController(draweeView.controller)
            .setControllerListener(controllerListener)
            .setImageRequest(imageRequest).build()
        draweeView.controller = controller
    }

}