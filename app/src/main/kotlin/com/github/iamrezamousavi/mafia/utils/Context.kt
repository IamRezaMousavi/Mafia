package com.github.iamrezamousavi.mafia.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.getCurrentVersion(): String? = packageManager
    .getPackageInfo(packageName, 0)
    .versionName

fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
}
