package com.example.kakaologinsample.ui.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T: ViewDataBinding>(private val layoutResId: Int): AppCompatActivity() {
    protected lateinit var mBinding: T
    protected lateinit var mContext: Context
    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, layoutResId)
        mBinding.lifecycleOwner = this
        mContext = this
    }

    private fun makeToast(msg: String) {
        mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT)
    }

    /**
     * 토스트 메시지 관찰
     */
    protected fun observeToastMessage(viewModel: BaseViewModel) {
        viewModel.toastMsg.observe(this) { msg ->
            if(msg.isNotBlank()) {
                mToast?.cancel()
                makeToast(msg)
                mToast?.show()
            }
        }
    }

    /**
     * 종료 플래그 관찰
     */
    protected fun observeFinish(viewModel: BaseViewModel) {
        viewModel.isFinish.observe(this) { isFinish ->
            if(isFinish) finish()
        }
    }
}