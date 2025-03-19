package com.bytedance.sdk.douyin.open.business_module.kvstorage

import android.content.Context

class SharedPreferencesKvStorage(
    private val context: Context,
    private val name: String
) : IKVStorage {

    private val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    override fun storeFloat(key: String, value: Float) {
        this.sp.edit().putFloat(key, value).apply()
    }

    override fun storeBoolean(key: String, value: Boolean) {
        this.sp.edit().putBoolean(key, value).apply()
    }

    override fun storeInt(key: String, value: Int) {
        this.sp.edit().putInt(key, value).apply()
    }

    override fun storeLong(key: String, value: Long) {
        this.sp.edit().putLong(key, value).apply()
    }

    override fun storeDouble(key: String, value: Double) {
        this.sp.edit().putFloat(key, value.toFloat()).apply()
    }

    override fun storeString(key: String, value: String?) {
        this.sp.edit().putString(key, value).apply()
    }

    override fun storeStringSet(key: String, values: Set<String?>?) {
        this.sp.edit().putStringSet(key, values).apply()
    }

    override fun erase(key: String) {
        this.sp.edit().remove(key).apply()
    }

    override fun clear() {
        this.sp.edit().clear().apply()
    }


    override fun count(): Int {
        return this.sp.all.size
    }

    override fun getAll(): Map<String?, *>? {
        return this.sp.all
    }

    override fun getInt(key: String, defValue: Int): Int {
        return this.sp.getInt(key, defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return this.sp.getLong(key, defValue)
    }

    override fun getDouble(key: String, defValue: Double): Double {
        return this.sp.getFloat(key, defValue.toFloat()).toDouble()
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return this.sp.getFloat(key, defValue)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return this.sp.getBoolean(key, defValue)
    }


    override fun getStringSet(key: String, defValues: Set<String?>?): Set<String?>? {
        return this.sp.getStringSet(key, defValues)
    }


    override fun getString(key: String, defValue: String?): String? {
        return this.sp.getString(key, defValue)
    }


    override fun contains(key: String): Boolean {
        return this.sp.contains(key)
    }


}