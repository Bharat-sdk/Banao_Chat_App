package com.ivaninfotech.banaochatapp.ui

import com.example.mystudio.ui.base.BaseActivity
import com.ivaninfotech.banaochatapp.R
import com.ivaninfotech.banaochatapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

}