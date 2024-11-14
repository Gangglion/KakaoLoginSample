package com.example.kakaologinsample.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private val _toastMsg: MutableLiveData<String> = MutableLiveData("")
    val toastMsg: LiveData<String> = _toastMsg
    private val _isFinish: MutableLiveData<Boolean> = MutableLiveData(false)
    val isFinish: LiveData<Boolean> = _isFinish

    protected fun updateToastMsg(msg: String) {
        _toastMsg.value = msg
    }

    protected fun updateIsFinish(value: Boolean) {
        _isFinish.value = value
    }
}