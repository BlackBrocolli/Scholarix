package id.ac.stiki.doleno.scholarix.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object LupaPasswordScreen : Screen("lupa_password_screen")
    object OnboardingScreen : Screen("onboarding_screen")
    object SecondOnboarding : Screen("second_onboarding")
    object ThirdOnboarding : Screen("third_onboarding")
    object FourthOnboarding : Screen("fourth_onboarding")
}