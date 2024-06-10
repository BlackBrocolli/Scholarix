package id.ac.stiki.doleno.scholarix

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.ac.stiki.doleno.scholarix.auth.google.GoogleAuthUiClient
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.view.auth.LoginScreen
import id.ac.stiki.doleno.scholarix.view.auth.LupaPasswordScreen
import id.ac.stiki.doleno.scholarix.view.auth.SignupScreen
import id.ac.stiki.doleno.scholarix.view.main.DetailBeasiswaScreen
import id.ac.stiki.doleno.scholarix.view.main.EditProfileScreen
import id.ac.stiki.doleno.scholarix.view.main.FavoritScreen
import id.ac.stiki.doleno.scholarix.view.main.HomeScreen
import id.ac.stiki.doleno.scholarix.view.main.KalenderBeasiswaScreen
import id.ac.stiki.doleno.scholarix.view.main.MainView
import id.ac.stiki.doleno.scholarix.view.main.PreferensiUser
import id.ac.stiki.doleno.scholarix.view.main.ProfileScreen
import id.ac.stiki.doleno.scholarix.view.onboarding.FourthOnboarding
import id.ac.stiki.doleno.scholarix.view.onboarding.OnBoardingScreen
import id.ac.stiki.doleno.scholarix.view.onboarding.SecondOnboarding
import id.ac.stiki.doleno.scholarix.view.onboarding.ThirdOnboarding
import id.ac.stiki.doleno.scholarix.viewmodel.AuthViewModel
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(
    navController: NavController,
    mainViewModel: MainViewModel,
    pd: PaddingValues,
    googleAuthUiClient: GoogleAuthUiClient,
    shouldShowBottomBar: Boolean
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.Home.route,
        modifier = Modifier.padding(pd)
    ) {
        composable(Screen.BottomScreen.Home.route) {
            val userEmail = googleAuthUiClient.getSignInUser()?.email
            if (userEmail != null) {
                HomeScreen(
                    navController = navController,
                    viewModel = mainViewModel,
                    userEmail = userEmail
                )
            }
        }
        composable(Screen.BottomScreen.Favorit.route) {
            googleAuthUiClient.getSignInUser()
                ?.let { it1 ->
                    FavoritScreen(
                        viewModel = mainViewModel,
                        userData = it1,
                        navController = navController
                    )
                }
        }
        composable(Screen.BottomScreen.Profil.route) {
            ProfileScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.BottomScreen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
        composable(Screen.EditProfileScreen.route) {
            val authViewModel: AuthViewModel = viewModel()
            EditProfileScreen(
                viewModel = authViewModel,
                navController = navController,
                userData = googleAuthUiClient.getSignInUser()
            )
        }
        composable(Screen.PreferensiUser.route) {
            PreferensiUser(navController = navController, viewModel = mainViewModel)
        }
        composable(
            route = "${Screen.DetailBeasiswaScreen.route}/{id}/{type}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val type = backStackEntry.arguments?.getString("type")
            if (id != null && type != null) {
                val encodedId = Uri.encode(id) // Melakukan encoding terhadap ID dengan spasi
                googleAuthUiClient.getSignInUser()?.let {
                    DetailBeasiswaScreen(
                        id = encodedId,
                        type = type,
                        viewModel = mainViewModel,
                        navController = navController,
                        userData = it
                    )
                }
            }
        }
        composable(Screen.KalenderBeasiswaScreen.route) {
            KalenderBeasiswaScreen(viewModel = mainViewModel, navController = navController)
        }

        composable(Screen.LoginScreen.route) {
            val authViewModel: AuthViewModel = viewModel()
            val state by authViewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignInUser() != null) {
                    navController.navigate(Screen.MainView.route)
                }
            }

            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                authViewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    navController.navigate(Screen.MainView.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                    authViewModel.resetState()
                }
            }

            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                state = state,
                onSignInGoogleClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }

        composable(Screen.SignupScreen.route) {
            val authViewModel: AuthViewModel = viewModel()
            val state by authViewModel.state.collectAsStateWithLifecycle()

            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                authViewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    navController.navigate(Screen.MainView.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                    authViewModel.resetState()
                }
            }

            SignupScreen(
                navController = navController,
                authViewModel = authViewModel,
                state = state,
                onSignInGoogleClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
        composable(Screen.OnboardingScreen.route) {
            OnBoardingScreen(navController = navController)
        }
        composable(Screen.SecondOnboarding.route) {
            SecondOnboarding(navController = navController, viewModel = mainViewModel)
        }
        composable(Screen.ThirdOnboarding.route) {
            ThirdOnboarding(navController = navController, viewModel = mainViewModel)
        }
        composable(Screen.FourthOnboarding.route) {
            googleAuthUiClient.getSignInUser()
                ?.let { userData ->
                    FourthOnboarding(
                        navController = navController,
                        viewModel = mainViewModel,
                        userData = userData
                    )

                }
        }
        composable(Screen.MainView.route) {
            MainView(googleAuthUiClient)
        }
        composable(Screen.LupaPasswordScreen.route) {
            LupaPasswordScreen(navController = navController)
        }
    }
}