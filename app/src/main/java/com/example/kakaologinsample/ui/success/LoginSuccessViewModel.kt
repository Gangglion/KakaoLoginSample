package com.example.kakaologinsample.ui.success

import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.domain.model.UserInfo
import com.example.kakaologinsample.domain.usecase.KakaoAuthUseCase
import com.example.kakaologinsample.domain.usecase.UserPrefUseCase
import com.example.kakaologinsample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSuccessViewModel @Inject constructor(
    private val kakaoAuthUseCase: KakaoAuthUseCase,
    private val userPrefUseCase: UserPrefUseCase
) : BaseViewModel() {

    private val _userInfoResult = MutableStateFlow<UserInfo?>(null)
    val userInfoResult: StateFlow<UserInfo?> = _userInfoResult

    fun getUserInfo() {
        viewModelScope.launch {
            val result = kakaoAuthUseCase.getUserInfo()
            when(result.isSuccess) {
                true -> {
                    val userInfo = result.getOrNull()
                    if(userInfo != null) {
                        _userInfoResult.value = userInfo
                        // 가져온 사용자 Id DataStore 에 저장
                        userPrefUseCase.saveUserId(_userInfoResult.value!!.id!!)
                    } else {
                        updateToastMsg("사용자 정보 가져오기 실패")
                        // 사용자 정보 가져오기 실패 시, DataStore 에 저장된 값 전부 제거
                        userPrefUseCase.clearAllPrefs()
                        updateIsFinish(true)
                    }
                }
                false -> {
                    updateToastMsg("사용자 정보 가져오기 실패")
                    // 사용자 정보 가져오기 실패 시, DataStore 에 저장된 값 전부 제거
                    userPrefUseCase.clearAllPrefs()
                    updateIsFinish(true)
                }
            }
        }
    }

    fun logoutFromKakao() {
        viewModelScope.launch {
            val result = kakaoAuthUseCase.logout().getOrNull()
            if(result != null && result == true) {
                updateToastMsg("로그아웃 성공. SDK 에서 토큰 삭제됨")
                userPrefUseCase.clearAllPrefs()
                updateIsFinish(true)
            } else {
                updateToastMsg("로그아웃 실패. SDK 에서 토큰 삭제됨")
                updateIsFinish(true)
            }
        }
    }

    fun unlinkFromKakao() {
        viewModelScope.launch {
            val result = kakaoAuthUseCase.unlink().getOrNull()
            if(result != null && result == true) {
                updateToastMsg("연결끊기 성공. SDK 에서 토큰 삭제됨")
                userPrefUseCase.clearAllPrefs()
                updateIsFinish(true)
            } else {
                updateToastMsg("연결끊기 실패. SDK 에서 토큰 삭제됨")
                updateIsFinish(true)
            }
        }
    }
}