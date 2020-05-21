package com.whx.baselib.network

import androidx.annotation.Keep

@Keep
class BaseData<D>(val code: Int?, val message: String?, val data: D?)