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

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.util.*

object ActivityStack {
    /**
     * Activity栈
     */
    private val sActivityStack = LinkedList<Activity>()
    var mForegroundActivityNum = 0

    /**
     * 初始化
     *
     * @param application 当前的Application对象
     */
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                sActivityStack.remove(activity)
                sActivityStack.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                mForegroundActivityNum++
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                mForegroundActivityNum--
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                sActivityStack.remove(activity)
            }
        })
    }

    /**
     * 得到指定activity前一个active activity的实例
     *
     * @param curActivity 当前activity
     * @return 可能为null
     */
    fun getPreviousActivity(curActivity: Activity): Activity? {
        val activities = sActivityStack
        var index = activities.size - 1
        var findCurActivity = false
        while (index >= 0) {
            if (findCurActivity) {
                val preActiveActivity = activities[index]
                if (preActiveActivity != null && !preActiveActivity.isFinishing) {
                    return preActiveActivity
                }
            } else if (activities[index] === curActivity) {
                findCurActivity = true
            }
            index--
        }
        return null
    }

    /**
     * 获得栈顶的Activity
     *
     * @return
     */
    val topActivity: Activity?
        get() = if (sActivityStack.isEmpty()) null else sActivityStack.last
}