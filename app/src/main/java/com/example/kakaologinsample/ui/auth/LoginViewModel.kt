package com.example.kakaologinsample.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.domain.model.Token
import com.example.kakaologinsample.domain.usecase.KakaoAuthUseCase
import com.example.kakaologinsample.domain.usecase.UserPrefUseCase
import com.example.kakaologinsample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoAuthUseCase: KakaoAuthUseCase,
    private val userPrefUseCase: UserPrefUseCase
) : BaseViewModel() {
    private val _isExistToken: MutableLiveData<Boolean> = MutableLiveData(false)
    val isExistToken: LiveData<Boolean> = _isExistToken

    private val _loginResult = MutableStateFlow<Token?>(null)
    val loginResult: StateFlow<Token?> = _loginResult

    fun loginWithKakao(activityContext: Context) {
        viewModelScope.launch {
            val token = kakaoAuthUseCase.login(activityContext)
            if(token != null) {
                _loginResult.value = token
                // 로그인 성공 시 가져온 토큰 정보 DataStore 저장
                userPrefUseCase.saveUserToken(_loginResult.value!!.accessToken, _loginResult.value!!.refreshToken)
                _isExistToken.value = true // 토큰 존재 여부 갱신
                updateToastMsg("로그인 성공!!")
            } else {
                updateToastMsg("로그인 실패!!")
                _isExistToken.value = false // 토큰 존재 여부 갱신
            }
        }
    }

    /**
     * 저장된 토큰 여부 관찰
     */
    fun checkSavedTokens() {
        viewModelScope.launch {
            val isExistAccessToken = userPrefUseCase.getAccessToken().first() != null
            val isExistRefreshToken = userPrefUseCase.getRefreshToken().first() != null
            _isExistToken.value = isExistAccessToken && isExistRefreshToken
            if(_isExistToken.value == true) {
                updateToastMsg("자동로그인")
            } else {
                updateToastMsg("저장된 토큰이 존재하지 않음. 로그인 필요")
            }
        }
    }
}