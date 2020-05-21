package com.whx.baselib.network

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.whx.baselib.RuntimeInfo
import com.whx.baselib.utils.DeviceUtils
import java.util.*

object HttpParamHelper {
    private const val HEAD_KEY_UA = "Dw-Ua"
    //http://dev.yypm.com/web/?post=posts/standard/interfaces/biugo/interface_contract.md
    private const val HEADER_CONSISTENT_LEVEL = "x-consistent-level"

    private var sVersionName: String? = null
    private var sVersionCode = -1
    private var sChannel: String? = null
    private var sUa: String? = null
    private val deeplinkUri = ""
    private val shareLink = ""
    private var forceRefresh = 0


    fun getVersionName(): String? {
        if (sVersionName != null) {
            return sVersionName
        }
        sVersionName = RuntimeInfo.sVersion
        if (!isHeaderParamValid(sVersionName)) {
            sVersionName = "0.0.0"
        }
        return sVersionName
    }

    private fun isHeaderParamValid(value: String?): Boolean {
        var i = 0
        val length = value!!.length
        while (i < length) {
            val c = value[i]
            if (c <= '\u001f' && c != '\t' || c >= '\u007f') {
//                KLog.w(
//                    "Header", Util.format(
//                        "Unexpected char %#04x at %d value: %s", c.toInt(), i,
//                        value
//                    )
//                )
                return false
            }
            i++
        }
        return true
    }

    fun getVersionCode(): Int {
        if (sVersionCode > 0) {
            return sVersionCode
        }
        sVersionCode = try {
            val packageManager: PackageManager = RuntimeInfo.sApplicationContext!!.packageManager
            val pkg: PackageInfo =
                packageManager.getPackageInfo(RuntimeInfo.sPackageName, 0)
            pkg.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            1
        }
        return sVersionCode
    }

    fun getChannel(): String? {
        if (sChannel != null) {
            return sChannel
        }
        try {
            val packageManager: PackageManager = RuntimeInfo.sApplicationContext!!.packageManager
            val appInfo = packageManager
                .getApplicationInfo(RuntimeInfo.sPackageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                sChannel = appInfo.metaData.getString("UMENG_CHANNEL")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (sChannel == null) {
            sChannel = ""
        }
        return sChannel
    }

    /**
     * 获取ua
     *
     * @return
     */
    fun getUA(): String? {
        if (sUa != null) {
            return sUa
        }
        try {
            val packageManager: PackageManager = RuntimeInfo.sApplicationContext!!.packageManager
            val sb = StringBuilder()
            sVersionName = getVersionName()
            sVersionCode = getVersionCode()
            sb.append("Biugo&").append(sVersionName).append('-').append(sVersionCode)
            sb.append('&').append("adr")
            val appInfo = packageManager
                .getApplicationInfo(RuntimeInfo.sPackageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                val channel = appInfo.metaData.getString("UMENG_CHANNEL")
                sChannel = channel
                if (!TextUtils.isEmpty(channel)) {
                    sb.append('&').append(channel)
                }
            }
            sUa = sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            sUa = ""
        }
        return sUa
    }

    /**
     * 获取当前国家，优先服务端返回
     *
     * @return
     */
    fun getCountry(): String {
        // todo ip所在国家
        return "" //CommonUtils.getServerCountry()
    }

    private var sDwState: String? = null

    fun getStateStr(): String? {
        if (TextUtils.isEmpty(sDwState)) {
            sDwState = "&" + getCountry() + "&" + HttpParamHelper.getSysLanguage()
        }
        return sDwState
    }

    private var sSysLanguage: String? = null
    private var sSysCountry: String? = null

    fun getSysLanguage(): String? {
        return if (sSysLanguage != null) {
            sSysLanguage
        } else {
            DeviceUtils.getSystemLanguage()
        }
    }

    fun getSysCountry(): String? {
        return if (sSysCountry != null) {
            sSysCountry
        } else {
            DeviceUtils.getSystemCountry()
        }
    }

    /**
     * 请求唯一id，组成规则:毫秒时间戳 + 6 位随机数
     */
    fun getCommonTraceId(): String {
        return StringBuilder(System.currentTimeMillis().toString())
            .append(Random().nextInt(1000000)).toString()
    }
}