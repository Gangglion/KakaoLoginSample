package com.example.kakaologinsample.ui.success

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakaologinsample.data.datastore.repository.DataStoreRepository
import com.example.kakaologinsample.data.kakao.model.UserInfo
import com.example.kakaologinsample.data.kakao.repository.KakaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSuccessViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _userInfoState = MutableLiveData<UserInfoState>()
    val userInfoState: LiveData<UserInfoState> = _userInfoState

    init {
        viewModelScope.launch {
            kakaoRepository.getUserInfo()
                .onStart { _userInfoState.value = UserInfoState.Loading }
                .catch { _userInfoState.value = UserInfoState.Error(UserInfoState.StateType.UserInfo, it) }
                .collect { result ->
                    result.fold(
                        onSuccess = { data ->
                            _userInfoState.value = UserInfoState.Success(UserInfoState.StateType.UserInfo, data)
                        },
                        onFailure = { error ->
                            _userInfoState.value = UserInfoState.Error(UserInfoState.StateType.UserInfo, error)
                        }
                    )
                }
        }
    }

    fun logoutFromKakao() {
        viewModelScope.launch {
            kakaoRepository.logout()
                .catch { _userInfoState.value = UserInfoState.Error(UserInfoState.StateType.Logout, it) }
                .collect { result ->
                    dataStoreRepository.clearAllPrefs()
                    result.fold(
                        onSuccess = {
                            _userInfoState.value = UserInfoState.Success(UserInfoState.StateType.Logout, null)
                        },
                        onFailure = {
                            _userInfoState.value = UserInfoState.Error(UserInfoState.StateType.Logout, it)
                        }
                    )
                }
        }
    }

    fun unlinkFromKakao() {
        viewModelScope.launch {
            kakaoRepository.unlink()
                .catch { UserInfoState.Error(UserInfoState.StateType.Unlink, it) }
                .collect { result ->
                    dataStoreRepository.clearAllPrefs()
                    result.fold(
                        onSuccess = {
                            _userInfoState.value = UserInfoState.Success(UserInfoState.StateType.Unlink, null)
                        },
                        onFailure = {
                            _userInfoState.value = UserInfoState.Error(UserInfoState.StateType.Unlink, it)
                        }
                    )
                }
        }
    }
}

sealed interface UserInfoState {
    enum class StateType {
        UserInfo, Logout, Unlink
    }

    object Loading: UserInfoState
    data class Success(val type: StateType, val userInfo: UserInfo?) : UserInfoState
    data class Error(val type: StateType, val throwable: Throwable) : UserInfoState
}