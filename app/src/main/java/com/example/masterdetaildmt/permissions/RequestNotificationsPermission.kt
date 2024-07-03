package com.example.masterdetaildmt.permissions

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationsPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf()
        }
    )

    LaunchedEffect(key1 = permissionState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val allPermissionsRevoked =
                permissionState.permissions.size == permissionState.revokedPermissions.size

            val permissionsToRequest = permissionState.permissions.filter {
                !it.status.isGranted
            }

            if (permissionsToRequest.isNotEmpty()) {
                permissionState.launchMultiplePermissionRequest()
            }

            if (allPermissionsRevoked) {
                onPermissionsRevoked()
            } else {
                if (permissionState.allPermissionsGranted) {
                    onPermissionGranted()
                } else {
                    onPermissionDenied()
                }
            }
        } else {
            onPermissionGranted()
        }
    }
}
