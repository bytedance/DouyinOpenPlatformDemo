package com.bytedance.sdk.douyin.open.business_module.kvstorage

interface IKVStorage {
    fun storeFloat(key: String, value: Float)

    fun storeBoolean(key: String, value: Boolean)

    fun storeInt(key: String, value: Int)

    fun storeLong(key: String, value: Long)

    fun storeDouble(key: String, value: Double)

    fun storeString(key: String, value: String?)

    fun storeStringSet(key: String, values: Set<String?>?)

    fun erase(key: String)

    fun clear()


    fun count(): Int


    // sp interfaces
    fun getAll(): Map<String?, *>?

    fun getInt(key: String, defValue: Int): Int

    fun getLong(key: String, defValue: Long): Long

    fun getDouble(key: String, defValue: Double): Double

    fun getFloat(key: String, defValue: Float): Float
    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun getStringSet(key: String, defValues: Set<String?>?): Set<String?>?

    fun getString(key: String, defValue: String?): String?


    operator fun contains(key: String): Boolean
}