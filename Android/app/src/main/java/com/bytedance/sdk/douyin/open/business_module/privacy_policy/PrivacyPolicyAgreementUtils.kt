package com.bytedance.sdk.douyin.open.business_module.privacy_policy

import android.app.Activity
import android.app.Dialog
import android.os.Process
import androidx.appcompat.app.AlertDialog
import com.bytedance.sdk.douyin.open.business_module.kvstorage.LocalDataUtils

object PrivacyPolicyAgreementUtils {

    private const val SHOW_SHOW_DIALOG_KEY = "shouldShowPrivacyPolicyDialog"

    private val listenerList = mutableListOf<OnPrivacyPolicyListener>()

    @JvmStatic
    fun isUserAgreePrivacyPolicy(): Boolean {
        return !LocalDataUtils.getBoolean(SHOW_SHOW_DIALOG_KEY, true)
    }

    @JvmStatic
    private fun setUserAgreePrivacyPolicy() {
        LocalDataUtils.putBoolean(SHOW_SHOW_DIALOG_KEY, false)
        listenerList.forEach {
            it.onAgree()
        }
    }

    fun registerPrivacyPolicyListener(listener: OnPrivacyPolicyListener) {
        listenerList.add(listener)
    }

    fun showPrivacyPolicyDialog(activity: Activity): Dialog {
        val dialog = AlertDialog.Builder(activity)
            .setTitle("隐私政策")
            .setMessage("请阅读并同意隐私政策  \nxxx\n模拟隐私政策\nxxx")
            .setPositiveButton("同意") { dialog, which ->
                setUserAgreePrivacyPolicy()
                dialog.dismiss()
            }.setNegativeButton("暂不使用") { dialog, which ->
                Process.killProcess(Process.myPid())
            }
            .setCancelable(false)
        return dialog.show()
    }

    interface OnPrivacyPolicyListener {
        fun onAgree()
    }
}