package com.example.masterdetaildmt.views.locations

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.data.viewmodel.LocationsViewModel
import com.example.masterdetaildmt.utils.Constants.Companion.DENIED_PERMISSIONS_CONTENT
import com.example.masterdetaildmt.utils.Constants.Companion.EMPTY_STRING
import com.example.masterdetaildmt.utils.Constants.Companion.LOCATIONS_CONTENT
import com.example.masterdetaildmt.utils.Constants.Companion.REVOKED_PERMISSIONS_CONTENT
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.koin.androidx.compose.inject

class LocationsView {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Composable
    fun getInstance(navController: NavHostController, context: Context) {

        val locationsViewModel: LocationsViewModel by inject()

        val currentContent = remember { mutableStateOf(EMPTY_STRING) }
        //Initialize it where you need it
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        RequestLocationPermission(
            onPermissionGranted = {
                getCurrentLocation(
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    context = context,
                    onGetCurrentLocationSuccess = { location ->
                        Log.d("JEJE", "Locvation")
                        locationsViewModel.setLocation(location.first, location.second)
                    },
                    onGetCurrentLocationFailed = {}
                )
                currentContent.value = LOCATIONS_CONTENT
            },
            onPermissionDenied = {
                currentContent.value = DENIED_PERMISSIONS_CONTENT
            },
            onPermissionsRevoked = {
                currentContent.value = REVOKED_PERMISSIONS_CONTENT
            }
        )

        when (currentContent.value) {
            LOCATIONS_CONTENT -> {
                LocationsContent(locationsViewModel)
            }

            DENIED_PERMISSIONS_CONTENT -> {
                DeniedPermissionsContent()
            }

            REVOKED_PERMISSIONS_CONTENT -> {
                RevokedPermissionsContent()
            }

            else -> {}
        }
    }
}

@Composable
fun LocationsContent(locationsViewModel: LocationsViewModel) {
    val latitude = locationsViewModel.latitude.collectAsState().value
    val longitude = locationsViewModel.longitude.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Latitude: $latitude")
        Text(text = "Longitude: $longitude")
    }
}

@Composable
fun DeniedPermissionsContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Its necessary grand the location permission")
        Text(text = "You can to this from the app settings")

    }
}

@Composable
fun RevokedPermissionsContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Its necessary grand again the location permission")
        Text(text = "You can to this from the app settings")
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(key1 = permissionState) {
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
    }
}


@SuppressLint("MissingPermission")
private fun getLastUserLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context,
    onGetLastLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetLastLocationFailed: (Exception) -> Unit
) {
    if (areLocationPermissionsGranted(context)) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onGetLastLocationSuccess(Pair(it.latitude, it.longitude))
                }
            }
            .addOnFailureListener { exception ->
                onGetLastLocationFailed(exception)
            }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true
) {
    val accuracy = if (priority) {
        Priority.PRIORITY_HIGH_ACCURACY
    } else {
        Priority.PRIORITY_BALANCED_POWER_ACCURACY
    }

    if (areLocationPermissionsGranted(context)) {
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            onGetCurrentLocationFailed(exception)
        }
    }
}

private fun areLocationPermissionsGranted(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}