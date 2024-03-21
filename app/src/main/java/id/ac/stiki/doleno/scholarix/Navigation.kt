package id.ac.stiki.doleno.scholarix

import androidx.compose.runtime.Composable
import id.ac.stiki.doleno.scholarix.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.view.LoginScreen
import id.ac.stiki.doleno.scholarix.view.LupaPasswordScreen
import id.ac.stiki.doleno.scholarix.view.SignupScreen

@Composable
fun Navigation(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignupScreen.route) {
            SignupScreen(navController = navController)
        }
        composable(Screen.LupaPasswordScreen.route) {
            LupaPasswordScreen(navController = navController)
        }
    }
}