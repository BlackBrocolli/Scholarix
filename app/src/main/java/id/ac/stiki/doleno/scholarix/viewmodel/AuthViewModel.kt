package id.ac.stiki.doleno.scholarix.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import id.ac.stiki.doleno.scholarix.model.auth.Injection
import id.ac.stiki.doleno.scholarix.model.auth.UserRepository
import id.ac.stiki.doleno.scholarix.model.google.SignInResult
import id.ac.stiki.doleno.scholarix.model.google.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import id.ac.stiki.doleno.scholarix.model.auth.Result
import id.ac.stiki.doleno.scholarix.navigation.Screen
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val userRepository: UserRepository

    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(
        email: String,
        password: String,
        namaLengkap: String,
        noHandphone: String,
        navController: NavController,
        context: Context
    ) {
        viewModelScope.launch {
//            _authResult.value = userRepository.signUp(email, password, namaLengkap, noHandphone)
            try {
                val result = userRepository.signUp(email, password, namaLengkap, noHandphone)
                _authResult.postValue(result)
                if (result is Result.Success) {
                    Log.d("AuthViewModel", "Sign up successful")
                    navController.navigate(Screen.OnboardingScreen.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                    Toast.makeText(
                        context,
                        "Daftar Berhasil.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("AuthViewModel", "Sign up failed: ${(result as Result.Error).exception}")
                    Toast.makeText(
                        context,
                        (result.exception as? FirebaseAuthUserCollisionException)?.message
                            ?: "Sign up failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Exception in sign up", e)
                _authResult.postValue(Result.Error(e))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
        }
    }

    // == GOOGLE SIGN IN ==
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInErrorMessage = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}