package com.whx.baselib.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseDataRepository<IAbsRequestMethod> {
    protected var api: IAbsRequestMethod? = null

    init {
        createApi()
    }

    public fun getAPI(): IAbsRequestMethod? {
        return api
    }

    abstract fun getType(): Class<IAbsRequestMethod>

    protected fun getCustomDomain(): String? {
        return null
    }

    protected fun createApi() {
        api = if (getCustomDomain() == null) {
            NetworkManager.getRetrofitIns().create(getType())
        } else {
            Retrofit.Builder()
                .client(NetworkManager.initDefaultOkHttpClient())
                .baseUrl(NetworkManager.getCurrentEnv().url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(getType())
        }
    }
}