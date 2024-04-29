package id.ac.stiki.doleno.scholarix

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import id.ac.stiki.doleno.scholarix.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import id.ac.stiki.doleno.scholarix.auth.google.GoogleAuthUiClient
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.view.onboarding.FourthOnboarding
import id.ac.stiki.doleno.scholarix.view.auth.LoginScreen
import id.ac.stiki.doleno.scholarix.view.auth.LupaPasswordScreen
import id.ac.stiki.doleno.scholarix.view.onboarding.OnBoardingScreen
import id.ac.stiki.doleno.scholarix.view.onboarding.SecondOnboarding
import id.ac.stiki.doleno.scholarix.view.auth.SignupScreen
import id.ac.stiki.doleno.scholarix.view.main.MainView
import id.ac.stiki.doleno.scholarix.view.onboarding.ThirdOnboarding
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    startDestination: String
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.LoginScreen.route) {
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignInUser() != null) {
                    navController.navigate(Screen.MainView.route)
                }
            }

            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
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
                    viewModel.resetState()
                }
            }

            LoginScreen(
                navController = navController,
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
            val state by viewModel.state.collectAsStateWithLifecycle()

            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
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
                    viewModel.resetState()
                }
            }

            SignupScreen(
                navController = navController,
                authViewModel = viewModel,
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

        composable(Screen.LupaPasswordScreen.route) {
            LupaPasswordScreen(navController = navController)
        }
        composable(Screen.OnboardingScreen.route) {
            OnBoardingScreen(navController = navController)
        }
        composable(Screen.SecondOnboarding.route) {
            SecondOnboarding(navController = navController)
        }
        composable(Screen.ThirdOnboarding.route) {
            ThirdOnboarding(navController = navController)
        }
        composable(Screen.FourthOnboarding.route) {
            FourthOnboarding(navController = navController)
        }
        composable(Screen.MainView.route) {
            MainView(googleAuthUiClient)
        }
    }
}