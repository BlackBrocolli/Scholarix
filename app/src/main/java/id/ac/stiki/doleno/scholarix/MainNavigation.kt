package id.ac.stiki.doleno.scholarix

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.view.main.DetailBeasiswaScreen
import id.ac.stiki.doleno.scholarix.view.main.EditProfileScreen
import id.ac.stiki.doleno.scholarix.view.main.FavoritScreen
import id.ac.stiki.doleno.scholarix.view.main.HomeScreen
import id.ac.stiki.doleno.scholarix.view.main.KalenderBeasiswaScreen
import id.ac.stiki.doleno.scholarix.view.main.MainView
import id.ac.stiki.doleno.scholarix.view.main.ProfileScreen
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun MainNavigation(
    navController: NavController,
    viewModel: MainViewModel,
    pd: PaddingValues,
    shouldShowBottomBar: Boolean
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.Home.route,
        modifier = Modifier.padding(pd)
    ) {
        composable(Screen.BottomScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.BottomScreen.Favorit.route) {
            FavoritScreen()
        }
        composable(Screen.BottomScreen.Profil.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController = navController)
        }
        composable(
            route = "${Screen.DetailBeasiswaScreen.route}/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
                nullable = false
            })
        ) { entry ->
            val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else -1L
            DetailBeasiswaScreen(id = id, viewModel = viewModel, navController = navController)
        }
        composable(Screen.KalenderBeasiswaScreen.route) {
            KalenderBeasiswaScreen(viewModel = viewModel, navController = navController)
        }
    }
}