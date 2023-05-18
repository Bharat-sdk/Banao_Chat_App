package com.ivaninfotech.banaochatapp.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaninfotech.banaochatapp.ui.channels.ChannelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class UserViewModel @Inject constructor(
    val client: ChatClient
) : ViewModel() {

    init {
        queryAllUsers()
    }

    private val _userEvent = MutableSharedFlow<UserEvents>()
    val userEvents = _userEvent.asSharedFlow()

    fun createChannel(selectedUser: String) {
        viewModelScope.launch {
            client.createChannel(
                channelType = "messaging",
                memberIds = listOf(client.getCurrentUser()?.id?:"", selectedUser),
                channelId = Random.nextLong(10000000, 50000000).toString(),
                extraData = emptyMap()
            ).enqueue { result ->
                viewModelScope.launch {
                    if (result.isSuccess) {
                        _userEvent.emit(
                            UserEvents.CreateChannelSuccess(
                                result.data().cid
                            )
                        )
                    } else {
                        _userEvent.emit(
                            UserEvents.ErrorEvent(
                                result.error().message ?: "Unknown Error Occurred"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun queryAllUsers() {
        val request = QueryUsersRequest(
            filter = Filters.ne("id", client.getCurrentUser()?.id?:""),
            offset = 0,
            limit = 100
        )
        client.queryUsers(request).enqueue { result ->
            viewModelScope.launch {
                if (result.isSuccess) {
                    _userEvent.emit(
                        UserEvents.UserListSuccess(
                            result.data()
                        )
                    )
                } else {
                    _userEvent.emit(
                        UserEvents.ErrorEvent(
                            result.error().message ?: "Unknown Error Occurred"
                        )
                    )
                }
            }

        }
    }


}

sealed class UserEvents() {
    class ErrorEvent(val message: String) : UserEvents()
    class CreateChannelSuccess(val channelId: String) : UserEvents()
    class UserListSuccess(val list: List<User>) : UserEvents()
}