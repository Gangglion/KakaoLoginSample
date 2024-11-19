package com.example.kakaologinsample.ui.success

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.kakaologinsample.R
import com.example.kakaologinsample.data.repository.kakao.model.UserInfo
import com.example.kakaologinsample.databinding.ActivityLoginSuccessBinding
import com.example.kakaologinsample.ui.base.BaseActivity
import com.example.kakaologinsample.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint

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
        observeUserInfo()
    }

    private fun observeUserInfo() {
        loginSuccessViewModel.userInfoState.observe(this) { uiState ->
            when(uiState) {
                is UserInfoState.Loading -> {
                    showEmptyDataScreen(true)
                }
                is UserInfoState.Error -> {
                    LogUtil.e("Error", uiState.throwable)
                    when(uiState.type) {
                        UserInfoState.StateType.UserInfo -> {
                            showToast("정보 가져오기 실패")
                        }
                        UserInfoState.StateType.Logout -> {
                            showToast("로그아웃 실패. SDK 에서 토큰 삭제됨")
                            finish()
                        }
                        UserInfoState.StateType.Unlink -> {
                            showToast("연결 끊기 실패. SDK 에서 토큰 삭제됨")
                            finish()
                        }
                    }
                }
                is UserInfoState.Success -> {
                    when(uiState.type) {
                        UserInfoState.StateType.UserInfo -> {
                            if(uiState.userInfo != null) {
                                showToast("정보 가져오기 성공")
                                showEmptyDataScreen(false)
                                setData(uiState.userInfo)
                            } else {
                                showToast("정보 가져오기 실패")
                                showEmptyDataScreen(true)
                            }
                        }
                        UserInfoState.StateType.Logout -> {
                            showToast("로그아웃 성공. SDK 에서 토큰 삭제됨")
                            finish()
                        }
                        UserInfoState.StateType.Unlink -> {
                            showToast("연결 끊기 성공. SDK 에서 토큰 삭제됨")
                            finish()
                        }
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