package chargeit.main_screen.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import chargeit.core.view.CoreFragment
import chargeit.main_screen.data.MapsFragmentViewModelContract

private const val COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
private const val FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

fun isPermissionGranted(context: Context, permission: String) =
    context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun getPermissionRequest(
    activity: Activity,
    fragment: CoreFragment,
    viewModel: MapsFragmentViewModelContract
) = fragment.registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { status -> processActivityResult(activity, viewModel, status) }

fun launchPermissionRequest(
    permissionRequest: ActivityResultLauncher<Array<String>>,
    permissions: Array<String>
) {
    permissionRequest.launch(permissions)
}

fun shouldShowAtLeastOneRationale(activity: Activity) =
    shouldShowRequestPermissionRationale(activity, COARSE_PERMISSION)
            || shouldShowRequestPermissionRationale(activity, FINE_PERMISSION)

fun processActivityResult(
    activity: Activity,
    viewModel: MapsFragmentViewModelContract,
    status: Map<String, Boolean>
) {
    val noRationale = shouldShowAtLeastOneRationale(activity).not()
    val isGranted = (status[COARSE_PERMISSION] == true || status[FINE_PERMISSION] == true)
    when {
        isGranted -> viewModel.startLocationUpdates()
        noRationale -> viewModel.requestNotGrantedNoAskDialog()
        else -> viewModel.requestRationaleDialog()
    }
}

fun isAtLeastOnePermissionGranted(context: Context) =
    isPermissionGranted(context, COARSE_PERMISSION) || isPermissionGranted(context, FINE_PERMISSION)

fun startAccessToLocation(
    activity: Activity,
    fragment: CoreFragment,
    viewModel: MapsFragmentViewModelContract
) {
    if (isAtLeastOnePermissionGranted(activity)) {
        viewModel.startLocationUpdates()
    } else {
        launchPermissionRequest(
            getPermissionRequest(activity, fragment, viewModel),
            arrayOf(COARSE_PERMISSION, FINE_PERMISSION)
        )
    }
}