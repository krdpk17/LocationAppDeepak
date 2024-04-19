package com.example.locationappdeepak

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.locationappdeepak.ui.theme.LocationAppDeepakTheme
import android.Manifest
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel : LocationViewModel = viewModel()
            LocationAppDeepakTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    MyLocationApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun MyLocationApp(viewModel: LocationViewModel){
    val context = LocalContext.current
    val myLocationUtils = MyLocationUtils(context)
    DisplayLocation(myLocationUtils = myLocationUtils, viewModel,
                    context = context)
}

@Composable
fun DisplayLocation(
    myLocationUtils: MyLocationUtils,
    viewModel: LocationViewModel,
    context: Context
){
    val location = viewModel.location.value

    val address = location?.let{
        myLocationUtils.requestGeocodeLocation(location)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                ){
                //ok can access location
                myLocationUtils.requestLocationUpdates(viewModel=viewModel)
            }else{
                //ask for permission
                val rationalRequired =  ActivityCompat.shouldShowRequestPermissionRationale(
                                        context as MainActivity,
                                        Manifest.permission.ACCESS_FINE_LOCATION) ||
                                        ActivityCompat.shouldShowRequestPermissionRationale(
                                        context as MainActivity,
                                        Manifest.permission.ACCESS_COARSE_LOCATION)

                if(rationalRequired){
                    Toast.makeText(context, "This feature requires location permission", Toast.LENGTH_LONG).show()
                }else{
                    //Need to set the permission from setting
                    Toast.makeText(context, "Please enable location permission from Android setting", Toast.LENGTH_LONG).show()
                }
            }
        })

    Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ){

        if(location != null){
            Text("location, \n lat: ${location.latitude} & long: ${location.longitude} \n $address")
        }else{
            Text("location not available")
        }
        Button(onClick = {
            if(myLocationUtils.hasLocationPermission(context)){
                // permission granted -> Update the location
                myLocationUtils.requestLocationUpdates(viewModel)
            }else{
                //Request location permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {
            Text("Get Location")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LocationAppDeepakTheme {
        Greeting("Android")
    }
}