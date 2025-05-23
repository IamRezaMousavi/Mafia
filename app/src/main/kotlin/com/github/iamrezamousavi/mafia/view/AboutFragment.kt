package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.iamrezamousavi.mafia.databinding.FragmentAboutBinding
import com.github.iamrezamousavi.mafia.utils.getCurrentVersion
import com.github.iamrezamousavi.mafia.utils.openUrl

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().getCurrentVersion().let { currentVersion ->
            binding.aboutAppVersion.text = currentVersion
            binding.aboutAppVersion.visibility = View.VISIBLE
        }

        binding.aboutSourceCodeButton.setOnClickListener {
            requireContext().openUrl("https://github.com/IamRezaMousavi/Mafia")
        }

        binding.aboutBugReportButton.setOnClickListener {
            requireContext().openUrl("https://github.com/IamRezaMousavi/Mafia/issues")
        }

        binding.aboutUserGuideButton.setOnClickListener {
            requireContext().openUrl(
                "https://github.com/IamRezaMousavi/Mafia/blob/master/docs%2Fmafia-en.md"
            )
        }
    }
}
