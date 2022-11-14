package chargeit.main_screen.utils

import android.content.Context
import android.content.pm.PackageManager

fun isPermissionGranted(context: Context, permission: String) =
    context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED