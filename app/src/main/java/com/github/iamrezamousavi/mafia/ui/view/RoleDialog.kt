package com.github.iamrezamousavi.mafia.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.DialogRoleBinding

class RoleDialog(context: Context, private val role: String) : AlertDialog(context) {
    private lateinit var binding: DialogRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.titleText.text = role

        val sideText = when (role) {
            context.getString(R.string.simple_citizen),
            context.getString(R.string.doctor),
            context.getString(R.string.detective),
            context.getString(R.string.sniper),
            context.getString(R.string.mayor),
            context.getString(R.string.guardian),
            context.getString(R.string.psychologist),
            context.getString(R.string.professional),
            context.getString(R.string.gunman),
            context.getString(R.string.judge),
            context.getString(R.string.champion),
            context.getString(R.string.priest),
            context.getString(R.string.hacker),
            context.getString(R.string.angel),
            context.getString(R.string.vigilante),
            context.getString(R.string.bartender) -> context.getString(R.string.citizen_side)

            context.getString(R.string.simple_mafia),
            context.getString(R.string.godfather),
            context.getString(R.string.dr_lecter),
            context.getString(R.string.silencer),
            context.getString(R.string.terrorist),
            context.getString(R.string.negotiator),
            context.getString(R.string.nato),
            context.getString(R.string.vandal),
            context.getString(R.string.magician),
            context.getString(R.string.hostage_taker),
            context.getString(R.string.bodyguard),
            context.getString(R.string.bomber) -> context.getString(R.string.mafia_side)

            context.getString(R.string.unknown),
            context.getString(R.string.wolfs_rain),
            context.getString(R.string.killer),
            context.getString(R.string.thousand_faces),
            context.getString(R.string.syndicate) -> context.getString(R.string.independent_side)

            else -> ""
        }
        binding.sideText.text = sideText

        val descText = when (role) {
            context.getString(R.string.simple_citizen) -> context.getString(R.string.simple_citizen_desc)
            context.getString(R.string.doctor) -> context.getString(R.string.doctor_desc)
            context.getString(R.string.detective) -> context.getString(R.string.detective_desc)
            context.getString(R.string.sniper) -> context.getString(R.string.sniper_desc)
            context.getString(R.string.mayor) -> context.getString(R.string.mayor_desc)
            context.getString(R.string.guardian) -> context.getString(R.string.guardian_desc)
            context.getString(R.string.psychologist) -> context.getString(R.string.psychologist_desc)
            context.getString(R.string.professional) -> context.getString(R.string.professional_desc)
            context.getString(R.string.gunman) -> context.getString(R.string.gunman_desc)
            context.getString(R.string.judge) -> context.getString(R.string.judge_desc)
            context.getString(R.string.champion) -> context.getString(R.string.champion_desc)
            context.getString(R.string.priest) -> context.getString(R.string.priest_desc)
            context.getString(R.string.hacker) -> context.getString(R.string.hacker_desc)
            context.getString(R.string.angel) -> context.getString(R.string.angel_desc)
            context.getString(R.string.vigilante) -> context.getString(R.string.vigilante_desc)
            context.getString(R.string.bartender) -> context.getString(R.string.bartender_desc)

            context.getString(R.string.simple_mafia) -> context.getString(R.string.simple_mafia_desc)
            context.getString(R.string.godfather) -> context.getString(R.string.godfather_desc)
            context.getString(R.string.dr_lecter) -> context.getString(R.string.dr_lecter_desc)
            context.getString(R.string.silencer) -> context.getString(R.string.silencer_desc)
            context.getString(R.string.terrorist) -> context.getString(R.string.terrorist_desc)
            context.getString(R.string.negotiator) -> context.getString(R.string.negotiator_desc)
            context.getString(R.string.nato) -> context.getString(R.string.nato_desc)
            context.getString(R.string.vandal) -> context.getString(R.string.vandal_desc)
            context.getString(R.string.magician) -> context.getString(R.string.magician_desc)
            context.getString(R.string.hostage_taker) -> context.getString(R.string.hostage_taker_desc)
            context.getString(R.string.bodyguard) -> context.getString(R.string.bodyguard_desc)
            context.getString(R.string.bomber) -> context.getString(R.string.bomber_desc)

            context.getString(R.string.unknown) -> context.getString(R.string.unknown_desc)
            context.getString(R.string.wolfs_rain) -> context.getString(R.string.wolfs_rain_desc)
            context.getString(R.string.killer) -> context.getString(R.string.killer_desc)
            context.getString(R.string.thousand_faces) -> context.getString(R.string.thousand_faces_desc)
            context.getString(R.string.syndicate) -> context.getString(R.string.syndicate_desc)

            else -> ""
        }
        binding.descriptionText.text = descText

        binding.okButton.setOnClickListener {
            dismiss()
        }

    }
}