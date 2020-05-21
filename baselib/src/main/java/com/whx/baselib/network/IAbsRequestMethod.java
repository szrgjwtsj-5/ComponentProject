package com.whx.baselib.network;

import androidx.lifecycle.LiveData;

import retrofit2.Call;

/**
 * Created by zdx on 2020/4/30.
 * Description:
 * Update:
 **/
public interface IAbsRequestMethod<D> {
    LiveData<BaseData<D>> getData();

    Call<BaseData<D>> loadData();
}
