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
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationAppDeepakTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    MyLocationApp()
                }
            }
        }
    }
}

@Composable
fun MyLocationApp(){
    val context = LocalContext.current
    val myLocationUtils = MyLocationUtils(context)
    DisplayLocation(myLocationUtils = myLocationUtils, context = context)
}

@Composable
fun DisplayLocation(
    myLocationUtils: MyLocationUtils,
    context: Context
){
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                ){
                //ok can access location
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
        Text("location not available")
        Button(onClick = {
            if(myLocationUtils.hasLocationPermission(context)){
                // permission granted -> Update the location
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