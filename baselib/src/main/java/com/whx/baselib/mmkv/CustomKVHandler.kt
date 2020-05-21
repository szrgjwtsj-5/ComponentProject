package com.whx.baselib.mmkv

import com.tencent.mars.xlog.Log
import com.tencent.mmkv.MMKVHandler
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.mmkv.MMKVRecoverStrategic


class CustomKVHandler : MMKVHandler {
    private val TAG = "WaveKVHandler"

    override fun wantLogRedirecting(): Boolean {
        return true
    }

    override fun onMMKVFileLengthError(p0: String?): MMKVRecoverStrategic {
        return MMKVRecoverStrategic.OnErrorDiscard
    }

    override fun onMMKVCRCCheckFail(p0: String?): MMKVRecoverStrategic {
        return MMKVRecoverStrategic.OnErrorDiscard
    }

    /**
     * 自定义输出MMKV log
     */
    override fun mmkvLog(
        level: MMKVLogLevel?,
        file: String?,
        line: Int,
        func: String?,
        msg: String?
    ) {
        val log = "< $file : $line :: $func > $msg"
        when (level) {
            MMKVLogLevel.LevelDebug -> {
                Log.d(TAG, log)
            }
            MMKVLogLevel.LevelInfo -> {
                Log.i(TAG, log)
            }
            MMKVLogLevel.LevelWarning -> {
                Log.w(TAG, log)
            }
            MMKVLogLevel.LevelError -> {
                Log.e(TAG, log)
            }
            MMKVLogLevel.LevelNone -> {
                Log.e(TAG, log)
            }
        }
    }
}