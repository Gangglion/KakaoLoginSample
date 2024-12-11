package com.example.kakaologinsample.ui.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.kakaologinsample.R
import com.example.kakaologinsample.databinding.FragmentLoginBinding
import com.example.kakaologinsample.ui.base.BaseFragment
import com.example.kakaologinsample.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val loginViewModel: LoginViewModel by viewModels()

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(!findNavController().navigateUp())
                requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressed)
        setClickEvent()
        observeLoginUiState()
        loginViewModel.checkTokens()
    }

    /**
     * uiState 관찰
     */
    private fun observeLoginUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { uiState ->
                    when(uiState) {
                        is LoginUiState.Loading -> {
                            LogUtil.d("로딩 중")
                        }
                        is LoginUiState.Success -> {
                            LogUtil.d("Success :: ${uiState.token}")
                            if(uiState.token != null) {
                                val action = LoginFragmentDirections.actionLoginFragmentToLoginSuccessFragment("프레그먼트 네비게이션 사용하여 값 전달 예시")
                                findNavController().navigate(action)
                            }
                        }
                        is LoginUiState.Error -> {
                            LogUtil.e("Error", uiState.throwable)
                        }
                    }
                }

            }
        }
    }

    /**
     * 클릭이벤트 정의
     */
    private fun setClickEvent() {
        binding.btnLogin.setOnClickListener {
            loginViewModel.loginWithKakao(mContext)
        }
    }
}