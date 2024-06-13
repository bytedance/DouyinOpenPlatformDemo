package com.bytedance.sdk.douyin.open.base.base

class OpenResult<T> {
    var errCode: Int
        private set
    var msg: String
        private set
    private var data: T? = null

    constructor(errCode: Int, msg: String, var3: T) {
        this.errCode = errCode
        this.msg = msg
        data = var3
    }

    constructor(errCode: Int, msg: String) {
        this.errCode = errCode
        this.msg = msg
    }

    fun getData(): T? {
        return data
    }

    val isSuccess: Boolean
        get() = errCode == 0
}