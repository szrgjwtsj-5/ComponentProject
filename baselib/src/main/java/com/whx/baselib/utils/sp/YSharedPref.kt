package com.whx.baselib.utils.sp

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import java.util.*

abstract class YSharedPref(pref: SharedPreferences) {
    private val TAG = "YSharedPref"

    private val DELIMITER = ","

    private val mPref: SharedPreferences = pref

    fun putString(key: String, value: String?) {
        mPref.edit().putString(key, value).apply()
    }

    fun getString(key: String?): String? {
        return getString(key, null)
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return mPref.getString(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        mPref.edit().putInt(key, value).apply()
    }

    fun putFloat(key: String, value: Float) {
        mPref.edit().putFloat(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        mPref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        key ?: return defaultValue
        return mPref.getBoolean(key, defaultValue)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        key ?: return defaultValue
        return mPref.getInt(key, defaultValue)
    }

    fun getInt(key: String?): Int {
        return getInt(key, -1)
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        key ?: return defaultValue
        return mPref.getFloat(key, defaultValue)
    }

    fun getFloat(key: String?): Float {
        return getFloat(key, -1f)
    }

    fun putLong(key: String, value: Long) {
        mPref.edit().putLong(key, value).apply()
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        key ?: return defaultValue
        return mPref.getLong(key, defaultValue)
    }

    fun getLong(key: String?): Long {
        return getLong(key, -1L)
    }

    fun putIntArray(key: String, values: Array<Int?>) {
        putIntList(key, listOf(*values))
    }

    fun getIntArray(key: String?): IntArray? {
        return getIntArray(key, null)
    }

    /**
     * @param key
     * @param outValues For memory reuse, if the result is no greater than this space,
     * will fill into this, the redundant elements won't be touched.
     * If it is null, a new int array will be created if result is
     * not empty.
     * @return The result list, null if no correlated.
     */
    fun getIntArray(key: String?, outValues: IntArray?): IntArray? {
        val list = getIntList(key)
        if (list == null || list.isEmpty()) {
            return null
        }
        val ret = if (list.size <= outValues!!.size) outValues else IntArray(list.size)
        var i = 0
        for (e in list) {
            ret[i++] = e
        }
        return ret
    }

    fun putIntList(key: String, values: List<Int?>) {
        if (values.isEmpty()) {
            return
        }
        val value = TextUtils.join(DELIMITER, values)
        putString(key, value)
    }

    fun getIntList(key: String?): List<Int>? {
        val value = getString(key)
        if (value.isNullOrEmpty()) {
            return null
        }
        val values = TextUtils.split(value, DELIMITER)
        if (values.isNullOrEmpty()) {
            return null
        }
        val list = ArrayList<Int>()
        for (e in values) {
            try {
                list.add(e.toInt())
            } catch (ex: NumberFormatException) {
//                MLog.error(
//                    TAG,
//                    "lcy failed to parse value for key: %s, value: %s, exception: %s",
//                    key, e, ex
//                )
                continue
            }
        }
        return list
    }

    fun remove(key: String?) {
        mPref.edit().remove(key).apply()
    }

    fun clear() {
        mPref.edit().clear().apply()
    }

    fun getAll(): Map<String?, *>? {
        return mPref.all
    }

    fun getAndIncrement(key: String, defVal: Int): Int {
        val value = getInt(key, defVal)
        putInt(key, value + 1)
        return value
    }

    fun compareAndSet(key: String, expect: Int, update: Int): Boolean {
        val value = getInt(key, 0)
        if (value == expect) {
            putInt(key, update)
            return true
        }
        return false
    }

    fun contains(key: String?): Boolean {
        return if (key == null || key.isEmpty()) {
            false
        } else mPref.contains(key)
    }

    fun putObject(key: String, obj: Any?) {
        val gson = Gson()
        val json = gson.toJson(obj)
        putString(key, json)
    }

    fun getObj(key: String?, className: Class<*>?): Any? {
        val gson = Gson()
        val json = getString(key, "")
        return gson.fromJson(json, className)
    }

}