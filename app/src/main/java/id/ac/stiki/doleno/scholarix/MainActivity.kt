package id.ac.stiki.doleno.scholarix

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import id.ac.stiki.doleno.scholarix.auth.google.GoogleAuthUiClient
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.ui.theme.ScholarixTheme

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScholarixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val startDestination = if (googleAuthUiClient.getSignInUser() != null) {
                        Screen.MainView.route  // Pengguna sudah login, langsung ke halaman utama
                    } else {
                        Screen.LoginScreen.route // Pengguna belum login, mulai dari halaman login
                    }

                    SetupSystemBarsColor()

                    Navigation(
                        googleAuthUiClient = googleAuthUiClient,
                        navController = navController,
                        startDestination = startDestination
                    )
                    // MainView()
                }
            }
        }
    }

    @Composable
    private fun SetupSystemBarsColor() {
        fun isColorLight(color: Color): Boolean {
            return color.luminance() > 0.5f
        }

        val systemUiController = rememberSystemUiController()
        val useDarkIcons = isColorLight(Color(0xFF733CEF))  // Ganti dengan warna yang sesuai

        SideEffect {
            systemUiController.setStatusBarColor(
                color = Color(0xFF733CEF), // Ganti dengan warna yang Anda inginkan
                darkIcons = useDarkIcons
            )
            systemUiController.setNavigationBarColor(
                color = Color.Black
            )
        }
    }
}
