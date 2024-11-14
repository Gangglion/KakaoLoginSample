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

        observeResult()

        mBinding.btnLogin.setOnClickListener { loginViewModel.loginWithKakao() }
    }

    private fun observeResult() {
        lifecycleScope.launch {
            loginViewModel.loginResult.collect { result ->
                when (result?.isSuccess) {
                    true -> {
                        val token = result.getOrNull()
                        if(token != null) {
                            LogUtil.d("로그인 성공")
                            LogUtil.i("Get Token ::\n" +
                                    "accessToken : ${token.accessToken}\n" +
                                    "refreshToken : ${token.refreshToken}")
                            startActivity(Intent(mContext, LoginSuccessActivity::class.java))
                        } else {
                            LogUtil.w("로그인 실패")
                        }
                    }
                    else -> {
                        val exception = result?.exceptionOrNull()
                        exception?.let {
                            LogUtil.e("로그인 실패!!", it)
                        }
                    }
                }
            }
        }
    }
}