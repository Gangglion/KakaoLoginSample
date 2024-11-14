package com.example.kakaologinsample.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.domain.usecase.KakaoAuthUseCase
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoAuthUseCase: KakaoAuthUseCase
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<OAuthToken?>?>(null)
    val loginResult: StateFlow<Result<OAuthToken?>?> = _loginResult

    fun loginWithKakao() {
        viewModelScope.launch {
            val result = kakaoAuthUseCase.login()
            _loginResult.value = result
        }
    }
}