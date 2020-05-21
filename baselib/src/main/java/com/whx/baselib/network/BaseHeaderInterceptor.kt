package com.whx.baselib.network

import com.whx.baselib.utils.AppConstants
import com.whx.baselib.utils.DeviceUtils
import okhttp3.Interceptor
import okhttp3.Response

class BaseHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("X-version", HttpParamHelper.getVersionName() ?: "")
        requestBuilder.addHeader("X-compAppid", AppConstants.WAVE_APP_ID)
//        requestBuilder.addHeader("X-hdid", HiidoManager.getHiidoId())
        requestBuilder.addHeader("X-country", HttpParamHelper.getSysCountry() ?: "")
        requestBuilder.addHeader("X-server-country", HttpParamHelper.getCountry())
        requestBuilder.addHeader("X-language", HttpParamHelper.getSysLanguage() ?: "")
        requestBuilder.addHeader("X-traceid", HttpParamHelper.getCommonTraceId())
//        requestBuilder.addHeader("X-osVersion", DeviceUtils.getSystemVersion())
        requestBuilder.addHeader("X-sign", "token")
        requestBuilder.addHeader("X-ab-test", "ab-test")
        requestBuilder.addHeader("X-machine", DeviceUtils.getSystemModel())
//        requestBuilder.addHeader("X-uid", "${LoginUtil.getUid()}")            //todo 实现获取登录用户ID
        requestBuilder.addHeader("X-testonly", "true/false")
        requestBuilder.addHeader("X-trg-idc", "")

        return chain.proceed(requestBuilder.build())
    }
}
