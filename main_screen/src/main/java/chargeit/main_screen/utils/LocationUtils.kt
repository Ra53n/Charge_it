package chargeit.main_screen.utils

import android.content.Context
import android.content.pm.PackageManager
import chargeit.main_screen.settings.COARSE_PERMISSION
import chargeit.main_screen.settings.FINE_PERMISSION

fun isPermissionGranted(context: Context, permission: String) =
    context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun isAtLeastOneGranted(context: Context) =
    isPermissionGranted(context, COARSE_PERMISSION) || isPermissionGranted(context, FINE_PERMISSION)