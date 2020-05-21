package com.whx.baselib.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import com.tencent.mars.xlog.Log
import com.whx.baselib.RuntimeInfo
import com.whx.baselib.mmkv.CommonKV
import java.util.*

class DeviceUtils {
    companion object {
        private const val TAG = "DeviceUtils"

        /**
         * 获取当前手机系统地区。
         *
         * @return 返回当前地区。例如：当前设置的是“中文-中国”，则返回“CN”
         */
        fun getSystemCountry(): String {
            var country = ""
            val choice: String = CommonKV.instance.getString("COUNTRY_CHOSE", "")
            if (choice.isNotBlank() && "SYSTEM" != choice) {
                country = choice
            } else {
                country = Locale.getDefault().country
                //高版本Android的语言列表选择了多个语言时，上面这个方法可能返回为""
                if (country.isEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    country = LocaleList.getDefault()[0].country
                    if (country.isEmpty()) {
                        country = "US"
                    }
                }
            }
//            MLog.info(DeviceUtils.TAG, "getSystemCountry country=$country")
            return country
        }

        /**
         * 获取当前手机系统语言。
         *
         * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
         */
        fun getSystemLanguage(): String {
            return try {
                val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    RuntimeInfo.sApplicationContext!!.resources.configuration.locales.get(0)
                } else {
                    RuntimeInfo.sApplicationContext!!.resources.configuration.locale
                }
                locale.language
            } catch (e: Exception) {
                ""
            }
        }

        /**
         * 获取当前手机系统版本号
         *
         * @return 系统版本号
         */
        fun getSystemVersion(): String = Build.VERSION.RELEASE

        /**
         * 获取手机型号
         *
         * @return 手机型号
         */
        fun getSystemModel(): String = Build.MODEL

        /**
         * 获取手机厂商
         *
         * @return 手机厂商
         */
        fun getDeviceBrand(): String = Build.BRAND

        /**
         * 在隐私与权限页，需要传参数，如https://www.noizztv.com/article/declare?lang=en-US
         *
         * @return 如en-US
         */
        fun getLocaleStringForWeb(): String {
            var s =
                if (Locale.getDefault().toString().isEmpty()) "" else Locale.getDefault().toString()
            val groups = s.split("_").toTypedArray()
            if (groups.size > 2) {
                s = groups[0] + "_" + groups[1]
            }
            s = s.replace("_".toRegex(), "-")
            return s
        }

        /**
         * 返回系统语言，如en_US，zh_TW，in_ID
         */
        fun getSystemCountryLanguage(): String {
            var language = Locale.getDefault().language
            var country = Locale.getDefault().country

            //高版本Android的语言列表选择了多个语言时，上面这个方法可能返回为""
            //高版本Android的语言列表选择了多个语言时，上面这个方法可能返回为""
            if (language.isEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                language = LocaleList.getDefault()[0].language
                country = LocaleList.getDefault()[0].country
            }
            if (language.isEmpty()) {
                language = "en"
            }
            if (country.isEmpty()) {
                country = "US"
            }
//            MLog.info(TAG, language + "_" + country)
            return language + "_" + country
        }

        fun getMccMnc(): String {
            try {
                val telManager =
                    RuntimeInfo.sApplicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val sim = telManager.simOperator
                Log.i(TAG, "getMccMnc=$sim")
                return sim ?: ""
            } catch (e: Throwable) {
                Log.e(TAG, "getMccMnc", e)
            }
            return ""
        }
    }
}