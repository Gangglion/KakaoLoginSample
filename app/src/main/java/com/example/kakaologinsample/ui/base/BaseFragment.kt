package com.example.kakaologinsample.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T: ViewDataBinding>(private val layoutResId: Int): Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!
    lateinit var mContext: Context
    private var mToast: Toast? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun showToast(msg: String) {
        mToast?.cancel()
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        mToast?.show()
    }
}