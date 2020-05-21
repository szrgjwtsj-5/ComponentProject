package com.whx.baselib.mmkv

import com.tencent.mmkv.MMKV

/**
 * 通用MMKV 工具
 */
class CommonKV private constructor(id: String) : BaseKV(MMKV.mmkvWithID(id)) {
    companion object {
        private const val COMMON_ID = "common_kv"

        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CommonKV(COMMON_ID)
        }
    }
}