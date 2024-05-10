package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.databinding.ActivityNarratorBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.view.adapter.NarratorAdapter
import com.github.iamrezamousavi.mafia.viewmodel.NarratorViewModel
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class NarratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNarratorBinding

    private val narratorViewModel: NarratorViewModel by viewModels()

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            val settingsViewModel = SettingsViewModel(context)
            LangData.getContextWrapper(context, settingsViewModel.language.code)
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNarratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val narratorAdapter = NarratorAdapter(this) {
            narratorViewModel.updateItem(it)
        }

        binding.playerRoleList.apply {
            layoutManager = LinearLayoutManager(this@NarratorActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = narratorAdapter
        }

        narratorViewModel.playerRoleList.observe(this) {
            narratorAdapter.submitList(it)
        }
    }
}
