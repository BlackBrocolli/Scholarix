package id.ac.stiki.doleno.scholarix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import id.ac.stiki.doleno.scholarix.auth.sign_in.GoogleAuthUiClient
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.ui.theme.ScholarixTheme
import id.ac.stiki.doleno.scholarix.view.main.DetailBeasiswaScreen
import id.ac.stiki.doleno.scholarix.view.main.MainView

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
}
