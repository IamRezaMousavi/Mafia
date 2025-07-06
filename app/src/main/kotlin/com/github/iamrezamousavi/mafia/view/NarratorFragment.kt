package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.MainViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.databinding.FragmentNarratorBinding
import com.github.iamrezamousavi.mafia.view.adapter.NarratorAdapter

class NarratorFragment : Fragment() {

    private lateinit var binding: FragmentNarratorBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNarratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val narratorAdapter = NarratorAdapter(requireContext()) {
            mainViewModel.updateNarratorItem(it)
        }

        binding.playerRoleList.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = narratorAdapter
        }

        mainViewModel.narratorList.observe(viewLifecycleOwner) {
            narratorAdapter.submitList(it)
            binding.alivePeople.text =
                getString(R.string.alive_count, it.count { player -> player.isAlive })
            binding.deadPeople.text =
                getString(R.string.dead_count, it.count { player -> player.isAlive.not() })

            binding.deadCitizen.text = getString(
                R.string.citizen_count,
                it.count { player ->
                    player.isAlive.not() && player.role.side == RoleSide.CITIZEN
                }
            )
            binding.deadMafia.text = getString(
                R.string.mafia_count,
                it.count { player ->
                    player.isAlive.not() && player.role.side == RoleSide.MAFIA
                }
            )
            binding.deadIndependent.text = getString(
                R.string.independent_count,
                it.count { player ->
                    player.isAlive.not() && player.role.side == RoleSide.INDEPENDENT
                }
            )

            binding.aliveCitizen.text = getString(
                R.string.citizen_count,
                it.count { player -> player.isAlive && player.role.side == RoleSide.CITIZEN }
            )
            binding.aliveMafia.text = getString(
                R.string.mafia_count,
                it.count { player -> player.isAlive && player.role.side == RoleSide.MAFIA }
            )
            binding.aliveIndependent.text = getString(
                R.string.independent_count,
                it.count { player ->
                    player.isAlive && player.role.side == RoleSide.INDEPENDENT
                }
            )

            when (mainViewModel.checkWin()) {
                RoleSide.CITIZEN -> {
                    binding.narratorCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.citizen_md_theme_primary
                        )
                    )
                    binding.deadPeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.citizen_md_theme_onPrimary
                        )
                    )
                    binding.alivePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.citizen_md_theme_onPrimary
                        )
                    )
                }

                RoleSide.MAFIA -> {
                    binding.narratorCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.mafia_md_theme_primary
                        )
                    )
                    binding.deadPeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.mafia_md_theme_onPrimary
                        )
                    )
                    binding.alivePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.mafia_md_theme_onPrimary
                        )
                    )
                }

                RoleSide.INDEPENDENT -> {
                    binding.narratorCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.independent_md_theme_primary
                        )
                    )
                    binding.deadPeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.independent_md_theme_onPrimary
                        )
                    )
                    binding.alivePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.independent_md_theme_onPrimary
                        )
                    )
                }

                null -> {
                    binding.narratorCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.md_theme_background
                        )
                    )
                    binding.deadPeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.mafia_md_theme_primary
                        )
                    )
                    binding.alivePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.citizen_md_theme_primary
                        )
                    )
                }
            }
        }

        binding.narratorToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    mainViewModel.applyAllPlayerAlive()
                    Toast.makeText(context, R.string.role_refresh, Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menuItemReset -> {
                    mainViewModel.hidePlayerRoles()
                    Toast.makeText(context, R.string.role_hide, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
