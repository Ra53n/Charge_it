package chargeit.main_screen.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import chargeit.core.view.CoreFragment
import chargeit.main_screen.data.MapsFragmentViewModelContract

class PermissionHelper(private val activity: Activity, private val fragment: CoreFragment) {
    private fun isPermissionGranted(permission: String) =
        activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun getPermissionRequest(
        viewModel: MapsFragmentViewModelContract
    ) = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { status -> processActivityResult(viewModel, status) }

    private fun launchPermissionRequest(
        permissionRequest: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        permissionRequest.launch(permissions)
    }

    private fun shouldShowAtLeastOneRationale() =
        shouldShowRequestPermissionRationale(activity, COARSE_PERMISSION)
                || shouldShowRequestPermissionRationale(activity, FINE_PERMISSION)

    private fun processActivityResult(
        viewModel: MapsFragmentViewModelContract,
        status: Map<String, Boolean>
    ) {
        val noRationale = shouldShowAtLeastOneRationale().not()
        val isGranted = (status[COARSE_PERMISSION] == true || status[FINE_PERMISSION] == true)
        when {
            isGranted -> viewModel.startLocationUpdates()
            noRationale -> viewModel.requestNotGrantedNoAskDialog()
            else -> viewModel.requestRationaleDialog()
        }
    }

    private fun isAtLeastOnePermissionGranted() =
        isPermissionGranted(COARSE_PERMISSION) || isPermissionGranted(FINE_PERMISSION)

    fun startAccessToLocation(
        viewModel: MapsFragmentViewModelContract
    ) {
        if (isAtLeastOnePermissionGranted()) {
            viewModel.startLocationUpdates()
        } else {
            launchPermissionRequest(
                getPermissionRequest(viewModel),
                arrayOf(COARSE_PERMISSION, FINE_PERMISSION)
            )
        }
    }

    companion object {
        private const val COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}
