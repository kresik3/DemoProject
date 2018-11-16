package com.krasovsky.dima.demoproject.main.list.datasource.model.list

import android.arch.lifecycle.MutableLiveData

class RecyclerViewStateModel {

    var stateLoading: MutableLiveData<Boolean>? = null
    var stateEmpty: MutableLiveData<Boolean>? = null
}