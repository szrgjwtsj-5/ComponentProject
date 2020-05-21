package com.whx.baselib.utils.sp

import android.content.Context
import com.whx.baselib.RuntimeInfo

class CommonPref private constructor(spFileName: String) : YSharedPref(
    RuntimeInfo.sApplicationContext!!.getSharedPreferences(
        spFileName,
        Context.MODE_PRIVATE
    )
) {

    companion object {
        private const val COMMON_PREF_NAME = "CommonPref"

//        @Volatile private var ins: CommonPref? = null
//        fun instance(): CommonPref {
//            return ins ?: synchronized(this) {
//                ins ?: CommonPref(COMMON_PREF_NAME).also { ins = it }
//            }
//        }

        // 使用lazy 实现的单例
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { CommonPref(COMMON_PREF_NAME) }
    }
}