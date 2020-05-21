package com.whx.baselib.network

import androidx.lifecycle.*
import com.tencent.mars.xlog.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val state = MutableLiveData<State>()

    fun <T> launchOnUI(block: suspend CoroutineScope.() -> T): LiveData<T> {
        val res = StateLiveData<T>()
        Log.e("baseviewmodel", Thread.currentThread().name)
        viewModelScope.launch(Dispatchers.IO) {
            res.postLoading()
            try {
                res.postValueAndSuccess(block())
            } catch (e: Throwable) {
                e.printStackTrace()
                res.postError()
            }
        }
        return res
    }

    fun <T> loadInIO(block: suspend CoroutineScope.() -> T): LiveData<T> {
        return liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            state.postValue(State.Loading)
            try {
                Log.e("BaseViewModel", Thread.currentThread().name)
                emit(block(viewModelScope))
                state.postValue(State.Success)
            } catch (e: Throwable) {
                e.printStackTrace()
                state.postValue(State.Error)
            }
        }
    }

}