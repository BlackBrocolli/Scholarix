package id.ac.stiki.doleno.scholarix.navigation

import androidx.annotation.DrawableRes
import id.ac.stiki.doleno.scholarix.R

sealed class Screen(val route: String, val title: String = "") {
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object LupaPasswordScreen : Screen("lupa_password_screen")
    object OnboardingScreen : Screen("onboarding_screen")
    object SecondOnboarding : Screen("second_onboarding")
    object ThirdOnboarding : Screen("third_onboarding")
    object FourthOnboarding : Screen("fourth_onboarding")
    object MainView : Screen("main_view")
    object EditProfileScreen : Screen("edit_profile_screen")
    object DetailBeasiswaScreen : Screen("detail_beasiswa_screen")

    sealed class BottomScreen(
        val bottomTitle: String,
        val bottomRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(route = bottomRoute, title = bottomTitle) {
        object Home : BottomScreen("Home", "home_screen", R.drawable.baseline_home_24)
        object Favorit : BottomScreen("Favorit", "favorite_screen", R.drawable.baseline_favorite_24)
        object Profil : BottomScreen("Profil", "profile_screen", R.drawable.baseline_account_box_24)
    }
}

val screensInBottomBar = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Favorit,
    Screen.BottomScreen.Profil
)