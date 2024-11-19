package com.example.kakaologinsample.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.data.repository.kakao.model.Token
import com.example.kakaologinsample.data.repository.kakao.repository.KakaoRepository
import com.example.kakaologinsample.domain.usecase.UserPrefUseCase
import com.example.kakaologinsample.ui.auth.LoginUiState.Error
import com.example.kakaologinsample.ui.auth.LoginUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val userPrefUseCase: UserPrefUseCase
) : ViewModel() {
    private val _uiState = MutableLiveData<LoginUiState>()
    val uiState: LiveData<LoginUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val accessToken = userPrefUseCase.getAccessToken().first()
            val refreshToken = userPrefUseCase.getRefreshToken().first()
            if(accessToken != null && refreshToken != null) {
                _uiState.value = Success(Token(accessToken, refreshToken))
            } else {
                _uiState.value = Success(null)
            }
        }
    }

    fun loginWithKakao(activityContext: Context) {
        viewModelScope.launch {
            kakaoRepository.loginUseKakao(activityContext)
                .onStart { _uiState.value = LoginUiState.Loading }
                .collect { result ->
                    result.fold(
                        onSuccess = { token ->
                            _uiState.value = Success(token)
                            if(token != null)
                                userPrefUseCase.saveUserToken(token.accessToken, token.refreshToken)
                        },
                        onFailure = { error ->
                            _uiState.value = Error(error)
                        }
                    )
                }
        }
    }
}

sealed interface LoginUiState {
    object Loading: LoginUiState
    data class Error(val throwable: Throwable) : LoginUiState
    data class Success(val token: Token?) : LoginUiState
}