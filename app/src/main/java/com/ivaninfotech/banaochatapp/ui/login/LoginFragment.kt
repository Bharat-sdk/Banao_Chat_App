package com.ivaninfotech.banaochatapp.ui.login

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mystudio.ui.base.BaseFragment
import com.ivaninfotech.banaochatapp.R
import com.ivaninfotech.banaochatapp.databinding.FragmentLoginBinding
import com.ivaninfotech.banaochatapp.utils.collectLatestLifeCycleFlow
import com.ivaninfotech.banaochatapp.utils.getString
import com.ivaninfotech.banaochatapp.utils.makeToast
import com.ivaninfotech.banaochatapp.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var client:ChatClient

    val vm: LoginViewModel by viewModels()

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        super.initView()
        if (client.getCurrentUser()!=null)
        {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatListingFragment())
        }
        else{
           snackBar("no user")
        }


        binding.btnLogin.setOnClickListener {
            vm.connectUser(
                username = binding.username.getString(),
                userId = binding.userId.getString()
            )
        }

    }

    override fun observe() {
        super.observe()
        collectLatestLifeCycleFlow(vm.loginEvents)
        {
            when(it)
            {
                is LoginViewModel.LoginEvents.ErrorEvent -> {
                    makeToast(it.message)
                }
                is LoginViewModel.LoginEvents.LoginSuccessEvent -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatListingFragment())
                }
            }
        }
    }


}