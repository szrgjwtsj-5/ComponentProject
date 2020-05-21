package com.whx.baselib.network

import com.whx.baselib.RuntimeInfo
import com.whx.baselib.mmkv.CommonKV
import com.whx.baselib.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkManager {
    private const val DEFAULT_SOCKET_TIMEOUT = 15 * 1000L
    private const val DEFAULT_READ_TIMEOUT = 15 * 1000L
    private const val DEFAULT_WRITE_TIMEOUT = 15 * 1000L

    private var currentEnv: Env? = null
    private val retrofitInstances = HashMap<String, Retrofit>()

    fun getRetrofitIns(): Retrofit {
        var ins: Retrofit? = retrofitInstances[getCurrentEnv().url]
        if (ins == null) {
            val builder = Retrofit.Builder()
                .client(initDefaultOkHttpClient())
                .baseUrl(getCurrentEnv().url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())

            ins = builder.build()
            retrofitInstances[getCurrentEnv().url] = ins
        }
        return ins!!
    }

    fun initDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(BaseHeaderInterceptor())
            .build()
    }

    /**
     * 获取当前环境
     */
    fun getCurrentEnv(): Env {
        currentEnv?.let { return it }
        Env.values().forEach { env ->
            if (env.url == CommonKV.instance.getString(AppConstants.ENV_KEY, "")) {
                currentEnv = env
                return env
            }
        }
        return if (RuntimeInfo.isDebug()) Env.TEST else Env.RELEASE
    }

    /**
     * 设置当前环境
     */
    fun setCurrentEnv(env: Env) {
        currentEnv = env
        CommonKV.instance.putString(AppConstants.ENV_KEY, env.url)
    }
}