package com.whx.baselib

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

object RuntimeInfo {
    private var isDebug: Boolean? = null

    var sIsMainProcess = false
    lateinit var sPackageName: String
    lateinit var sCurProcessName: String
    lateinit var sApplicationContext: Context
    var sVersion: String? = null

    fun init(ctx: Application) {
        sApplicationContext = ctx
        sPackageName = ctx.packageName
        sCurProcessName = getProcessName()
        if (sPackageName == sCurProcessName) {
            sIsMainProcess = true
        }

        sVersion = getVersionName(ctx)
    }

    private fun getProcessName(): String {
        var reader: BufferedReader? = null
        try {
            reader =
                BufferedReader(FileReader("/proc/" + Process.myPid() + "/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ""
    }

    private fun getVersionName(ctx: Context): String {
        val packageInfo: PackageInfo? = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        return if (packageInfo == null) "" else packageInfo.versionName
    }

    fun isDebug(): Boolean {
        if (isDebug == null) {
            var appInfo: ApplicationInfo? = null
            val packMgmr: PackageManager? = sApplicationContext.packageManager
            try {
                appInfo = packMgmr?.getApplicationInfo(sPackageName, PackageManager.GET_META_DATA)
            } catch (e: PackageManager.NameNotFoundException) {
                // log
            }

            isDebug = if (appInfo != null) {
                (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
            } else {
                false
            }
        }
        return isDebug!!
    }
}