package com.example.kakaologinsample.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.data.datastore.repository.DataStoreRepository
import com.example.kakaologinsample.data.kakao.model.Token
import com.example.kakaologinsample.data.kakao.repository.KakaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun checkTokens() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val accessToken = dataStoreRepository.accessToken.first()
            val refreshToken = dataStoreRepository.refreshToken.first()
            if(accessToken != null && refreshToken != null) {
                _uiState.value = LoginUiState.Success(Token(accessToken, refreshToken))
            } else {
                _uiState.value = LoginUiState.Success(null)
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
                            _uiState.value = LoginUiState.Success(token)
                            if(token != null)
                                dataStoreRepository.saveTokens(token.accessToken, token.refreshToken)
                        },
                        onFailure = { error ->
                            _uiState.value = LoginUiState.Error(error)
                        }
                    )
                }
        }
    }
}

sealed interface LoginUiState {
    data object Loading: LoginUiState
    data class Error(val throwable: Throwable) : LoginUiState
    data class Success(val token: Token?) : LoginUiState
}