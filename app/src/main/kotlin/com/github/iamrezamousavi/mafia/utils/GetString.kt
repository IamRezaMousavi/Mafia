package com.github.iamrezamousavi.mafia.utils

import android.content.Context
import androidx.annotation.StringRes
import com.github.iamrezamousavi.mafia.R

@StringRes
fun getSide(@StringRes role: Int): Int = when (role) {
    R.string.simple_citizen,
    R.string.doctor,
    R.string.detective,
    R.string.sniper,
    R.string.mayor,
    R.string.guardian,
    R.string.psychologist,
    R.string.professional,
    R.string.gunman,
    R.string.judge,
    R.string.champion,
    R.string.priest,
    R.string.hacker,
    R.string.angel,
    R.string.vigilante,
    R.string.bartender -> R.string.citizen_side

    R.string.simple_mafia,
    R.string.godfather,
    R.string.dr_lecter,
    R.string.silencer,
    R.string.terrorist,
    R.string.negotiator,
    R.string.nato,
    R.string.vandal,
    R.string.magician,
    R.string.hostage_taker,
    R.string.bodyguard,
    R.string.bomber -> R.string.mafia_side

    R.string.unknown,
    R.string.wolfs_rain,
    R.string.killer,
    R.string.thousand_faces,
    R.string.syndicate -> R.string.independent_side

    else -> R.string.citizen_side
}

@StringRes
fun getDescription(@StringRes role: Int): Int = when (role) {
    R.string.simple_citizen -> R.string.simple_citizen_desc
    R.string.doctor -> R.string.doctor_desc
    R.string.detective -> R.string.detective_desc
    R.string.sniper -> R.string.sniper_desc
    R.string.mayor -> R.string.mayor_desc
    R.string.guardian -> R.string.guardian_desc
    R.string.psychologist -> R.string.psychologist_desc
    R.string.professional -> R.string.professional_desc
    R.string.gunman -> R.string.gunman_desc
    R.string.judge -> R.string.judge_desc
    R.string.champion -> R.string.champion_desc
    R.string.priest -> R.string.priest_desc
    R.string.hacker -> R.string.hacker_desc
    R.string.angel -> R.string.angel_desc
    R.string.vigilante -> R.string.vigilante_desc
    R.string.bartender -> R.string.bartender_desc

    R.string.simple_mafia -> R.string.simple_mafia_desc
    R.string.godfather -> R.string.godfather_desc
    R.string.dr_lecter -> R.string.dr_lecter_desc
    R.string.silencer -> R.string.silencer_desc
    R.string.terrorist -> R.string.terrorist_desc
    R.string.negotiator -> R.string.negotiator_desc
    R.string.nato -> R.string.nato_desc
    R.string.vandal -> R.string.vandal_desc
    R.string.magician -> R.string.magician_desc
    R.string.hostage_taker -> R.string.hostage_taker_desc
    R.string.bodyguard -> R.string.bodyguard_desc
    R.string.bomber -> R.string.bomber_desc

    R.string.unknown -> R.string.unknown_desc
    R.string.wolfs_rain -> R.string.wolfs_rain_desc
    R.string.killer -> R.string.killer_desc
    R.string.thousand_faces -> R.string.thousand_faces_desc
    R.string.syndicate -> R.string.syndicate_desc

    else -> R.string.simple_citizen_desc
}

@StringRes
fun getRoleId(context: Context, roleName: String): Int = when (roleName) {
    context.getString(R.string.simple_citizen) -> R.string.simple_citizen
    context.getString(R.string.doctor) -> R.string.doctor
    context.getString(R.string.detective) -> R.string.detective
    context.getString(R.string.sniper) -> R.string.sniper
    context.getString(R.string.mayor) -> R.string.mayor
    context.getString(R.string.guardian) -> R.string.guardian
    context.getString(R.string.psychologist) -> R.string.psychologist
    context.getString(R.string.professional) -> R.string.professional
    context.getString(R.string.gunman) -> R.string.gunman
    context.getString(R.string.judge) -> R.string.judge
    context.getString(R.string.champion) -> R.string.champion
    context.getString(R.string.priest) -> R.string.priest
    context.getString(R.string.hacker) -> R.string.hacker
    context.getString(R.string.angel) -> R.string.angel
    context.getString(R.string.vigilante) -> R.string.vigilante
    context.getString(R.string.bartender) -> R.string.bartender

    context.getString(R.string.simple_mafia) -> R.string.simple_mafia
    context.getString(R.string.godfather) -> R.string.godfather
    context.getString(R.string.dr_lecter) -> R.string.dr_lecter
    context.getString(R.string.silencer) -> R.string.silencer
    context.getString(R.string.terrorist) -> R.string.terrorist
    context.getString(R.string.negotiator) -> R.string.negotiator
    context.getString(R.string.nato) -> R.string.nato
    context.getString(R.string.vandal) -> R.string.vandal
    context.getString(R.string.magician) -> R.string.magician
    context.getString(R.string.hostage_taker) -> R.string.hostage_taker
    context.getString(R.string.bodyguard) -> R.string.bodyguard
    context.getString(R.string.bomber) -> R.string.bomber

    context.getString(R.string.unknown) -> R.string.unknown
    context.getString(R.string.wolfs_rain) -> R.string.wolfs_rain
    context.getString(R.string.killer) -> R.string.killer
    context.getString(R.string.thousand_faces) -> R.string.thousand_faces
    context.getString(R.string.syndicate) -> R.string.syndicate

    else -> R.string.simple_citizen
}
