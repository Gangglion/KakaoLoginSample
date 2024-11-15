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
        observeResult()
        observeIsTokenExist()
        observeToastMessage(loginViewModel)

        loginViewModel.checkSavedTokens() // 앱 시작 시 토큰 존재 여부 확인
    }

    /**
     * 로그인 결과값 관찰
     */
    private fun observeResult() {
        lifecycleScope.launch {
            loginViewModel.loginResult.collect { token ->
                if(token != null) {
                    LogUtil.i("Get Token ::\n" +
                            "accessToken : ${token.accessToken}\n" +
                            "refreshToken : ${token.refreshToken}")
                }
            }
        }
    }

    /**
     * 저장된 토큰값 있는지 확인한 결과 관찰
     */
    private fun observeIsTokenExist() {
        loginViewModel.isExistToken.observe(this) {
            if(it) { // 저장된 토큰값이 존재할 경우
                startActivity(Intent(mContext, LoginSuccessActivity::class.java))
            }
        }
    }

    /**
     * 클릭이벤트 정의
     */
    private fun setClickEvent() {
        mBinding.btnLogin.setOnClickListener { loginViewModel.loginWithKakao() }
    }
}