package com.whx.baselib.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.whx.baselib.network.StateLiveData

fun <X, Y> LiveData<X>.switchMapState(switchMapFunction: (X)-> LiveData<Y>?): StateLiveData<Y> {
    val res = StateLiveData<Y>()
    res.addSource(this, object : Observer<X> {
        var mSource: LiveData<Y>? = null
        override fun onChanged(t: X) {
            val newLiveData = switchMapFunction(t)
            if (mSource == newLiveData) {
                return
            }
            if (mSource != null) {
                res.removeSource(mSource!!)
            }
            mSource = newLiveData
            if (mSource != null) {
                res.addSource(mSource!!) {
                    res.value = it
                }
            }
        }
    })
    return res
}