package com.ivaninfotech.banaochatapp.ui.user

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystudio.ui.base.BaseFragment
import com.ivaninfotech.banaochatapp.R
import com.ivaninfotech.banaochatapp.databinding.FragmentLoginBinding
import com.ivaninfotech.banaochatapp.databinding.FragmentUsersBinding
import com.ivaninfotech.banaochatapp.ui.adapter.UserListAdapter
import com.ivaninfotech.banaochatapp.utils.collectLatestLifeCycleFlow
import com.ivaninfotech.banaochatapp.utils.getString
import com.ivaninfotech.banaochatapp.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import javax.inject.Inject

@AndroidEntryPoint
class UserListingFragment : BaseFragment<FragmentUsersBinding>() {

    @Inject
    lateinit var client:ChatClient

    @Inject
    lateinit var userAdapter: UserListAdapter

    val vm: UserViewModel by viewModels()

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_users
    }

    override fun initView() {
        super.initView()

        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }

        userAdapter.setOnUserClickListener {
            if (it.banned)
            {
                makeToast("This user in Banned ")
            }else{
                vm.createChannel(it.id)
            }
        }

    }

    override fun observe() {
        super.observe()
        collectLatestLifeCycleFlow(vm.userEvents)
        {
            when(it)
            {
                is UserEvents.CreateChannelSuccess -> {
                    findNavController().navigate(UserListingFragmentDirections.actionUserListingFragmentToChatFragment(
                        it.channelId
                    ))
                }
                is UserEvents.ErrorEvent -> {
                    Log.d("TAG", "observe: "+it.message)
                    makeToast(it.message)
                }
                is UserEvents.UserListSuccess -> {
                    userAdapter.differ.submitList(
                        it.list
                    )
                }
            }
        }
    }


}