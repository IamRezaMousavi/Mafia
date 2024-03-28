package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.databinding.ActivitySettingsBinding
import com.github.iamrezamousavi.mafia.utils.changeLanguage
import com.github.iamrezamousavi.mafia.utils.codeToLanguage
import com.github.iamrezamousavi.mafia.utils.getCurrentLocale
import com.github.iamrezamousavi.mafia.utils.nameToLanguage
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var settingsViewModel: SettingsViewModel

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            settingsViewModel = SettingsViewModel(context)
            val code = settingsViewModel.language.value!!.code
            changeLanguage(context, code)
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguageSpinner()

    }

    private fun setupLanguageSpinner() {
        val currentLanguage = codeToLanguage(getCurrentLocale(this).language)
        val items = Language
            .entries
            .map { it.nativeName }
            .sorted()
            .let { languages ->
                listOf(currentLanguage.nativeName) +
                    languages.filter { it != currentLanguage.nativeName }
            }
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.langSpinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val language = nameToLanguage(selectedItem)
                    val currentLocale = getCurrentLocale(this@SettingsActivity)
                    if (language.code == currentLocale.language) return

                    when (selectedItem) {
                        Language.FA.nativeName -> settingsViewModel.setLanguage(Language.FA)
                        Language.EN.nativeName -> settingsViewModel.setLanguage(Language.EN)
                    }
                    restartActivity()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun restartActivity() {
        startActivity(Intent(this, this::class.java))
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
    }
}
