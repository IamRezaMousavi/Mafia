package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.local.LanguageKey
import com.github.iamrezamousavi.mafia.data.local.SettingsSP
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class MainActivity :
    AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var sharedPreferences: SharedPreferences

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            settingsViewModel = SettingsViewModel(context)
            LangData.getContextWrapper(context, settingsViewModel.language.code)
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        sharedPreferences = getSharedPreferences(SettingsSP, Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == LanguageKey) {
            recreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
