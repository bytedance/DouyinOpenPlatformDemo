package com.bytedance.sdk.douyin.open.init

import com.bytedance.sdk.douyin.open.base.config.HostConfigManager
import com.bytedance.sdk.open.aweme.CommonConstants
import com.bytedance.sdk.open.aweme.core.OpenCallback
import com.bytedance.sdk.open.aweme.core.OpenHostTicketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OpenHostTicketServiceImpl : OpenHostTicketService {

    override fun requestClientCode(p0: String?, p1: OpenCallback<String>?) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val hostTickerService = HostConfigManager.getHostConfig().hostTickerService()
                if (hostTickerService == null) {
                    p1?.onFail(CommonConstants.SDKErrorCode.CLIENT_CODE_ERROR, "没有实现")
                    return@launch
                }
                val result = try {
                    hostTickerService.getClientCode(p0 ?: "")
                } catch (e: Exception) {
                    p1?.onFail(-1, e.message)
                    return@launch
                }
                if (result.isSuccess && result.getData()?.isNotEmpty() == true) {
                    p1?.onSuccess(result.getData())
                    return@launch
                } else {
                    p1?.onFail(result.errCode, result.msg)
                }
            } catch (e: Exception) {
                p1?.onFail(-1, e.message)
            }

        }

    }

    override fun requestAccessCode(p0: String?, p1: String?, p2: OpenCallback<String>?) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val hostTickerService = HostConfigManager.getHostConfig().hostTickerService()
                if (hostTickerService == null) {
                    p2?.onFail(CommonConstants.SDKErrorCode.ACCESS_CODE_ERROR, "没有实现")
                    return@launch
                }
                if (p0.isNullOrEmpty() || p1.isNullOrEmpty()) {
                    p2?.onFail(CommonConstants.SDKErrorCode.ACCESS_CODE_ERROR, "client_key or openid is empty")
                    return@launch
                }
                val result =
                    try {
                        hostTickerService.getAccessCode(p0, p1)
                    } catch (e: Exception) {
                        p2?.onFail(-1, e.message)
                        return@launch
                    }
                if (result.isSuccess && result.getData()?.isNotEmpty() == true) {
                    p2?.onSuccess(result.getData())
                    return@launch
                } else {
                    p2?.onFail(result.errCode, result.msg)
                }
            } catch (e: Exception) {
                p2?.onFail(-1, e.message)
            }

        }
    }
}