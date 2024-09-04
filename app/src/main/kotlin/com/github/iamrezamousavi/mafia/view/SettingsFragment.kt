package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.databinding.FragmentSettingsBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingsViewModel = SettingsViewModel(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLanguageSpinner()
    }

    private fun setupLanguageSpinner() {
        val currentLanguage = LangData.getCurrentLanguage(requireContext())
        val items = Language
            .entries
            .map { it.nativeName }
            .sorted()
            .let { languages ->
                listOf(currentLanguage.nativeName) +
                    languages.filter { it != currentLanguage.nativeName }
            }
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.langSpinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val language = LangData.languageFromName(selectedItem)
                    val currentLang = LangData.getCurrentLanguage(context)
                    if (language == currentLang) return

                    when (selectedItem) {
                        Language.FA.nativeName -> settingsViewModel.setLanguage(Language.FA)
                        Language.EN.nativeName -> settingsViewModel.setLanguage(Language.EN)
                    }
                }

                @Suppress("EmptyFunctionBlock")
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
}
