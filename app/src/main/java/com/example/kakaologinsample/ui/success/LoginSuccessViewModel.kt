package com.example.kakaologinsample.ui.success

import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.data.local.model.UserToken
import com.example.kakaologinsample.data.repository.UserTokenDbRepository
import com.example.kakaologinsample.domain.model.UserInfo
import com.example.kakaologinsample.domain.usecase.KakaoAuthUseCase
import com.example.kakaologinsample.domain.usecase.UserTokenDbUseCase
import com.example.kakaologinsample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSuccessViewModel @Inject constructor(
    private val kakaoAuthUseCase: KakaoAuthUseCase,
    private val userTokenDbUseCase: UserTokenDbUseCase
) : BaseViewModel() {

    private val _userInfoResult = MutableStateFlow<Result<UserInfo?>?>(null)
    val userInfoResult: StateFlow<Result<UserInfo?>?> = _userInfoResult

    fun getUserInfo() {
        viewModelScope.launch {
            val userInfo = kakaoAuthUseCase.getUserInfo()
            _userInfoResult.value = userInfo
        }
    }

    fun logoutFromKakao() {
        viewModelScope.launch {
            val result = kakaoAuthUseCase.logout().getOrNull()
            if(result != null && result == true) {
                updateToastMsg("로그아웃 성공. SDK 에서 토큰 삭제됨")
                updateIsFinish(true)
            } else {
                updateToastMsg("로그아웃 실패. SDK 에서 토큰 삭제됨")
                updateIsFinish(false)
            }
        }
    }

    fun unlinkFromKakao() {
        viewModelScope.launch {
            val result = kakaoAuthUseCase.unlink().getOrNull()
            if(result != null && result == true) {
                updateToastMsg("연결끊기 성공. SDK 에서 토큰 삭제됨")
                updateIsFinish(true)
            } else {
                updateToastMsg("연결끊기 실패. SDK 에서 토큰 삭제됨")
                updateIsFinish(false)
            }
        }
    }
}