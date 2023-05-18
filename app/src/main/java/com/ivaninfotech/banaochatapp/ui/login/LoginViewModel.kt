package com.ivaninfotech.banaochatapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val client:ChatClient
):ViewModel(){

    private val _loginEvent = MutableSharedFlow<LoginEvents>()
    val loginEvents = _loginEvent.asSharedFlow()

    fun connectUser(username:String, userId:String)
    {
        viewModelScope.launch {
            val result = client.connectGuestUser(
                userId = userId,
                username = username,
                timeoutMilliseconds = 100000000000
            ).await()
            if (result.isError)
            {
                _loginEvent.emit(
                    LoginEvents.ErrorEvent(
                        result.error().message ?: "Unknown Error Occurred"
                    )
                )
                return@launch
            }
            _loginEvent.emit(
                LoginEvents.LoginSuccessEvent(
                    result.data().connectionId ?: "Unknown Error Occurred"
                )
            )
        }
    }

    sealed class LoginEvents()
    {
        class ErrorEvent(val message:String): LoginEvents()
        class LoginSuccessEvent(val message:String): LoginEvents()

    }
}