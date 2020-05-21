package com.whx.baselib.network

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class StateLiveData<T> : MediatorLiveData<T>() {
    enum class State {
        Idle, Loading, Success,Error
    }
    val state = MutableLiveData<State>()

    init {
        clearState()
    }

    fun postValueAndSuccess(value: T) {
        super.postValue(value)
        postSuccess()
    }

    fun clearState() {
        state.postValue(State.Idle)
    }

    fun postLoading() {
        state.postValue(State.Loading)
    }

    fun postSuccess() {
        state.postValue(State.Success)
    }

    fun postError() {
        state.postValue(State.Error)
    }

    fun changeState(s: State) {
        state.postValue(s)
    }
}