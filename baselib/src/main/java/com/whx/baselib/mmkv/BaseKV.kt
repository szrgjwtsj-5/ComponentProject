package com.whx.baselib.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * MMKV 工具，子类可传入自定义MMKV 实例
 */
abstract class BaseKV(private val mmkv: MMKV) {

    fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return try {
            mmkv.decodeInt(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return try {
            mmkv.decodeBool(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return try {
            mmkv.decodeLong(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return try {
            mmkv.decodeFloat(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun putDouble(key: String, value: Double) {
        mmkv.encode(key, value)
    }

    fun getDouble(key: String, defaultValue: Double): Double {
        return try {
            mmkv.decodeDouble(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    fun getString(key: String, defaultValue: String): String {
        return try {
            mmkv.decodeString(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun putBytes(key: String, value: ByteArray) {
        mmkv.encode(key, value)
    }

    fun getBytes(key: String): ByteArray? {
        return try {
            mmkv.decodeBytes(key, null)
        } catch (e: Throwable) {
            null
        }
    }

    fun putParcelable(key: String, value: Parcelable) {
        mmkv.encode(key, value)
    }

    fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T? {
        return try {
            mmkv.decodeParcelable(key, clazz)
        } catch (e: Throwable) {
            null
        }
    }

    fun putStringSet(key: String, stringSet: Set<String>) {
        mmkv.encode(key, stringSet)
    }

    fun getStringSet(key: String): Set<String>? {
        return try {
            mmkv.decodeStringSet(key, null)
        } catch (e: Throwable) {
            null
        }
    }
}