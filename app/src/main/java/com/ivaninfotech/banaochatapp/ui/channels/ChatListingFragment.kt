package com.ivaninfotech.banaochatapp.ui.channels

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mystudio.ui.base.BaseFragment
import com.ivaninfotech.banaochatapp.R
import com.ivaninfotech.banaochatapp.databinding.FragmentChatListingBinding
import com.ivaninfotech.banaochatapp.utils.collectLatestLifeCycleFlow
import com.ivaninfotech.banaochatapp.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import java.util.logging.Filter

@AndroidEntryPoint
class ChatListingFragment : BaseFragment<FragmentChatListingBinding>() {

    val vm: ChannelViewModel by viewModels()

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_chat_listing
    }

    @OptIn(InternalStreamChatApi::class)
    override fun initView() {
        super.initView()

        val user = vm.getUser()
        if (user == null)
        {
            findNavController().popBackStack()
            return
        }

        val factory = ChannelListViewModelFactory(
            filter = Filters.and(
                Filters.eq("type","messaging"),
                Filters.`in`("members", listOf(user.id)),
            ),
            limit = 100
        )

        val channelViewModel : ChannelListViewModel by viewModels { factory }
        val channelHeaderVm:ChannelListHeaderViewModel by viewModels()

        channelViewModel.bindView(binding.channelsView,viewLifecycleOwner)
        channelHeaderVm.bindView(binding.channelListHeaderView,viewLifecycleOwner)

        binding.channelListHeaderView.setOnUserAvatarLongClickListener {
            vm.logout()
        }

        binding.channelListHeaderView.setOnActionButtonClickListener{
            findNavController().navigate(ChatListingFragmentDirections.actionChatListingFragmentToUserListingFragment())
        }

        binding.channelsView.setChannelItemClickListener {
            findNavController().navigate(ChatListingFragmentDirections.actionChatListingFragmentToChatFragment(it.cid))
        }


    }

    override fun observe() {
        super.observe()
        collectLatestLifeCycleFlow(vm.channelEvents)
        {
            when(it){
                is ChannelViewModel.ChannelEvents.ErrorEvent -> {
                    makeToast(it.message)
                }
                ChannelViewModel.ChannelEvents.LogoutSuccessEvent -> {
                    findNavController().popBackStack()
                }

                ChannelViewModel.ChannelEvents.CreateChannelSuccess -> {

                }
            }
        }
    }


}