package com.example.masterdetaildmt.views.locations

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.data.viewmodel.LoadingComponentViewModel
import com.example.data.viewmodel.LocationsViewModel
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.permissions.RequestLocationPermission
import com.example.masterdetaildmt.permissions.RequestNotificationsPermission
import com.example.masterdetaildmt.services.LocationBackgroundService
import com.example.masterdetaildmt.utils.Constants.Companion.DENIED_PERMISSIONS_CONTENT
import com.example.masterdetaildmt.utils.Constants.Companion.EMPTY_STRING
import com.example.masterdetaildmt.utils.Constants.Companion.LOCATIONS_CONTENT
import com.example.masterdetaildmt.utils.Constants.Companion.REVOKED_PERMISSIONS_CONTENT
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.inject

class LocationsView {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Composable
    fun getInstance(navController: NavHostController, context: Context) {
        val loadingComponentViewModel: LoadingComponentViewModel by inject()
        val locationsViewModel: LocationsViewModel by inject()
        val currentContent = remember { mutableStateOf(EMPTY_STRING) }

        val currentLocation = locationsViewModel.currentPokePosition.collectAsState().value

        //Initialize it where you need it
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        RequestLocationPermission(
            onPermissionGranted = {
                getCurrentLocation(
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    context = context,
                    onGetCurrentLocationSuccess = { location ->
                        locationsViewModel.setCurrentPokePosition(location.first, location.second)
                    },
                    onGetCurrentLocationFailed = { }
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

        RequestNotificationsPermission(
            onPermissionGranted = {
                currentContent.value = LOCATIONS_CONTENT
                Intent(context, LocationBackgroundService::class.java).also {
                    it.action = LocationBackgroundService.Actions.START.toString()
                    context.startService(it)
                }
            },
            onPermissionDenied = {
            },
            onPermissionsRevoked = {
            },
            context = context
        )
        when (currentContent.value) {
            LOCATIONS_CONTENT -> {
                LocationsContent(locationsViewModel,context,fusedLocationProviderClient)
            }

            DENIED_PERMISSIONS_CONTENT -> {
                DeniedPermissionsContent()
            }

            REVOKED_PERMISSIONS_CONTENT -> {
                RevokedPermissionsContent()
            }

            else -> {
                CircularProgressIndicator()
            }
        }

    }
}

@Composable
fun LocationsContent(locationsViewModel: LocationsViewModel, context: Context, fusedLocationProviderClient: FusedLocationProviderClient) {
    val currentPokePosition = locationsViewModel.currentPokePosition.collectAsState().value
    val pokePositionsList = locationsViewModel.pokePositionsList.collectAsState().value

    val cameraPosition = LatLng(currentPokePosition.latitude, currentPokePosition.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPosition, 15f)
    }

    val mapLoaded = remember{ mutableStateOf(false)}
    LaunchedEffect(Unit){

    }
    LaunchedEffect(mapLoaded.value){
        if (mapLoaded.value){
            getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                context = context,
                onGetCurrentLocationSuccess = { location ->
                    locationsViewModel.setCurrentPokePosition(location.first, location.second)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(cameraPosition, 15f)
                },
                onGetCurrentLocationFailed = { }
            )
            locationsViewModel.getAllPokePosition()
        }
    }


    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            mapLoaded.value = true
        }
    ){
        if (pokePositionsList.isNotEmpty()){
            pokePositionsList.forEach { pokePosition ->
                MarkerComposable(
                    state = MarkerState(position =  LatLng(pokePosition.latitude,pokePosition.longitude)),
                    title = pokePosition.publishDate
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pokeball),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun DeniedPermissionsContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Its necessary grand the location permission")
        Text(text = "You can to this from the app settings")

    }
}

@Composable
fun RevokedPermissionsContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Its necessary grand again the location permission")
        Text(text = "You can to this from the app settings")
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