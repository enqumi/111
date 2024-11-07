package com.example.googlemapsexample.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.googlemapsexample.utils.LocationUtils
import com.example.googlemapsexample.viewmodel.LocationViewModel
@Composable
fun MainScreen(
    context: Context,
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navHostController: NavHostController
) {
    val pickedLocation by viewModel.pickedLocationData.collectAsState()
    val pickedLocationAddress by viewModel.pickedLocationAddress.collectAsState()
    val userLocationData by viewModel.userLocationData.collectAsState()
    val userLocationAddress by viewModel.userLocationAddress.collectAsState()

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permission ->
            if (permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                locationUtils.getLocation()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity, Manifest.permission.ACCESS_FINE_LOCATION
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    Toast.makeText(context, "Отказано в доступе", Toast.LENGTH_LONG).show()
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))  // Темный фон
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Блок с информацией о текущем местоположении
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Ваша локация:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (userLocationData != null) {
                    Text(
                        text = if (userLocationAddress.isNotBlank()) userLocationAddress
                        else "Широта: ${userLocationData!!.latitude}, Долгота: ${userLocationData!!.longitude}",
                        color = Color.Green
                    )
                    Text(
                        text = "Широта: ${userLocationData!!.latitude}, Долгота: ${userLocationData!!.longitude}",
                        color = Color.Green
                    )
                } else {
                    Text("Локация не найдена", color = Color.Green)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Блок с информацией о выбранной локации
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Выбранная локация:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (pickedLocation != null) {
                    Text(
                        text = if (pickedLocationAddress.isNotBlank()) pickedLocationAddress
                        else "Широта: ${pickedLocation!!.latitude}, Долгота: ${pickedLocation!!.longitude}",
                        color = Color.Green
                    )
                    Text(
                        text = "Широта: ${pickedLocation!!.latitude}, Долгота: ${pickedLocation!!.longitude}",
                        color = Color.Green
                    )
                } else {
                    Text("Не выбрана локация", color = Color.Gray)
                }
            }
        }

        Spacer(Modifier.height(24.dp))


        Button(
            onClick = {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                navHostController.navigate(route = Graph.MAP_SCREEN)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCFCFC))
        ) {
            Text(
                text = "Выбранная локация",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
