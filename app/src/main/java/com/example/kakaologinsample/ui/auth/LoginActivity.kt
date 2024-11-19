package com.example.kakaologinsample.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.kakaologinsample.R
import com.example.kakaologinsample.databinding.ActivityLoginBinding
import com.example.kakaologinsample.ui.base.BaseActivity
import com.example.kakaologinsample.ui.success.LoginSuccessActivity
import com.example.kakaologinsample.util.LogUtil
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setClickEvent()
        observeLoginUiState()
    }

    /**
     * uiState 관찰
     */
    private fun observeLoginUiState() {
        loginViewModel.uiState.observe(this) { uiState ->
            when(uiState) {
                is LoginUiState.Loading -> {
                    LogUtil.d("로딩 중")
                }
                is LoginUiState.Success -> {
                    LogUtil.d("Success :: ${uiState.token}")
                    if(uiState.token != null) {
                        startActivity(Intent(mContext, LoginSuccessActivity::class.java))
                    }
                }
                is LoginUiState.Error -> {
                    LogUtil.e("Error", uiState.throwable)
                }
            }
        }
    }

    /**
     * 클릭이벤트 정의
     */
    private fun setClickEvent() {
        mBinding.btnLogin.setOnClickListener {
            loginViewModel.loginWithKakao(mContext)
        }
    }
}