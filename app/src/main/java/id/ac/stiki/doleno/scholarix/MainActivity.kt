package id.ac.stiki.doleno.scholarix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import id.ac.stiki.doleno.scholarix.ui.theme.ScholarixTheme
import id.ac.stiki.doleno.scholarix.view.LoginScreen
import id.ac.stiki.doleno.scholarix.view.LupaPasswordScreen
import id.ac.stiki.doleno.scholarix.view.SignupScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScholarixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    OnBoardingScreen()
//                    LoginScreen()
//                    SignupScreen()
//                    LupaPasswordScreen()
                    Navigation()
                }
            }
        }
    }
}
