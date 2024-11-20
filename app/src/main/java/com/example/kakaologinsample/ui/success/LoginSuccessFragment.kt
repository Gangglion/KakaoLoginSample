package com.example.kakaologinsample.ui.success

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kakaologinsample.R
import com.example.kakaologinsample.data.kakao.model.UserInfo
import com.example.kakaologinsample.databinding.FragmentLoginSuccessBinding
import com.example.kakaologinsample.ui.base.BaseFragment
import com.example.kakaologinsample.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSuccessFragment : BaseFragment<FragmentLoginSuccessBinding>(R.layout.fragment_login_success) {

    private val loginSuccessViewModel: LoginSuccessViewModel by viewModels()

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(findNavController().previousBackStackEntry?.destination?.id == R.id.loginFragment)
                requireActivity().finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            LogUtil.d("${it.getString("test")}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressed)
        with(binding) {
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
        loginSuccessViewModel.userInfoState.observe(viewLifecycleOwner) { uiState ->
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
                            findNavController().navigateUp()
                        }
                        UserInfoState.StateType.Unlink -> {
                            showToast("연결 끊기 실패. SDK 에서 토큰 삭제됨")
                            findNavController().navigateUp()
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
                            findNavController().navigateUp()
                        }
                        UserInfoState.StateType.Unlink -> {
                            showToast("연결 끊기 성공. SDK 에서 토큰 삭제됨")
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun setData(userInfo: UserInfo) {
        with(binding) {
            Glide.with(mContext).load(userInfo.profileImage).override(300, 300).into(ivProfile)
            tvUserNumber.text = userInfo.id.toString()
            tvUserNick.text = userInfo.nickName ?: "닉네임을 가져오지 못함"
            tvUserEmail.text = userInfo.email
        }
    }

    private fun showEmptyDataScreen(isShow: Boolean) {
        with(binding) {
            llUserInfo.visibility = if (isShow) View.GONE else View.VISIBLE
            tvEmptyData.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }
}