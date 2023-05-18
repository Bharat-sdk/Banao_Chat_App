package com.ivaninfotech.banaochatapp.ui.channels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChannelViewModel @Inject constructor(
    val client: ChatClient
) : ViewModel() {

    private val _channelEvent = MutableSharedFlow<ChannelEvents>()
    val channelEvents = _channelEvent.asSharedFlow()

    fun logout() {
        client.disconnect(flushPersistence = false).enqueue { disconnectResult ->
            viewModelScope.launch {
                if (disconnectResult.isSuccess) {
                    _channelEvent.emit(
                        ChannelEvents.LogoutSuccessEvent
                    )
                } else {
                    _channelEvent.emit(
                        ChannelEvents.ErrorEvent(
                            disconnectResult.error().message ?: "Unknown Error Occurred"
                        )
                    )
                }
            }
        }

    }

    fun getUser():User?{
        return client.getCurrentUser()
    }



    sealed class ChannelEvents() {
        class ErrorEvent(val message: String) : ChannelEvents()
        object LogoutSuccessEvent : ChannelEvents()
        object CreateChannelSuccess : ChannelEvents()

    }
}