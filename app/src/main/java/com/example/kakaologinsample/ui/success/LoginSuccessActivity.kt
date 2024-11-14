package com.example.kakaologinsample.ui.success

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kakaologinsample.R
import com.example.kakaologinsample.data.local.model.UserToken
import com.example.kakaologinsample.domain.model.UserInfo
import com.example.kakaologinsample.ui.base.BaseActivity
import com.example.kakaologinsample.databinding.ActivityLoginSuccessBinding
import com.example.kakaologinsample.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginSuccessActivity : BaseActivity<ActivityLoginSuccessBinding>(R.layout.activity_login_success) {

    private val loginSuccessViewModel: LoginSuccessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(mBinding) {
            btnLogout.setOnClickListener {
                LogUtil.d("로그아웃 클릭")
                loginSuccessViewModel.logoutFromKakao()
            }

            btnUnlink.setOnClickListener {
                LogUtil.d("연결 끊기 클릭")
                loginSuccessViewModel.unlinkFromKakao()
            }
        }
        loginSuccessViewModel.getUserInfo()
        observeUserInfo()
        observeToastMessage(loginSuccessViewModel)
        observeFinish(loginSuccessViewModel)
    }

    private fun observeUserInfo() {
        lifecycleScope.launch {
            loginSuccessViewModel.userInfoResult.collect { result ->
                LogUtil.v(result.toString())
                when (result?.isSuccess) {
                    true -> {
                        result.getOrNull()?.let { userInfo ->
                            LogUtil.d("사용자 정보 가져오기 성공!!\n" +
                                    "$userInfo")
                            setData(userInfo)
                            showEmptyDataScreen(false)
                        } ?: run {
                            showEmptyDataScreen(true)
                        }
                    }
                    false -> {
                        showEmptyDataScreen(true)
                    }
                    else -> {
                        showEmptyDataScreen(true)
                    }
                }
            }
        }
    }

    private fun setData(userInfo: UserInfo) {
        with(mBinding) {
            Glide.with(mContext).load(userInfo.profileImage).override(300, 300).into(ivProfile)
            tvUserNumber.text = userInfo.id.toString()
            tvUserNick.text = userInfo.nickName ?: "닉네임을 가져오지 못함"
            tvUserEmail.text = userInfo.email
        }
    }

    private fun showEmptyDataScreen(isShow: Boolean) {
        with(mBinding) {
            llUserInfo.visibility = if (isShow) View.GONE else View.VISIBLE
            tvEmptyData.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }
}