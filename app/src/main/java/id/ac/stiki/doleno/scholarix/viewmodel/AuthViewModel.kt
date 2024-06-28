package id.ac.stiki.doleno.scholarix.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    // == Variable edit profile ==
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _inputNamaLengkap = MutableLiveData<String>()
    val inputNamaLengkap: LiveData<String> = _inputNamaLengkap

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    // == End of Variable edit profile ==

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    // == Variable Login dan Register ==
    private val _isLoadingLoginButton = MutableLiveData(false)
    val isLoadingLoginButton: LiveData<Boolean> = _isLoadingLoginButton

    private val _isLoadingRegisterButton = MutableLiveData(false)
    val isLoadingRegisterButton: LiveData<Boolean> = _isLoadingRegisterButton
    // == End of Variable Login dan Register ==

    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }

    fun signUp(
        email: String,
        password: String,
        namaLengkap: String,
        noHandphone: String,
        navController: NavController,
        context: Context
    ) {
        _isLoadingRegisterButton.value = true
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
            } finally {
                _isLoadingRegisterButton.value =
                    false  // Tambahkan untuk menandai bahwa proses pendaftaran telah selesai
            }
        }
    }

    fun login(email: String, password: String) {
        _isLoadingLoginButton.value = true
        viewModelScope.launch {
            try {
                val result = userRepository.login(email, password)
                _authResult.value = result
            } finally {
                _isLoadingLoginButton.value = false
            }
        }
    }


    // == GOOGLE SIGN IN ==
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

    // -------- Start of EDIT PROFILE --------
    fun setInputNamaLengkap(nama: String) {
        _inputNamaLengkap.value = nama
    }

    fun updateUserProfile(
        email: String,
        namaLengkap: String,
        profilePictureUri: Uri?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        _isLoading.value = true
        val userMap = mutableMapOf<String, Any>()
        userMap["namaLengkap"] = namaLengkap

        if (profilePictureUri != null) {
            val profilePicRef = storage.reference.child("profile_pictures/$email")
            profilePicRef.putFile(profilePictureUri)
                .addOnSuccessListener {
                    profilePicRef.downloadUrl.addOnSuccessListener { uri ->
                        userMap["profilePictureUrl"] = uri.toString()
                        db.collection("users").document(email)
                            .update(userMap)
                            .addOnSuccessListener {
                                _isLoading.value = false
                                onSuccess()
                            }
                            .addOnFailureListener { exception ->
                                _isLoading.value = false
                                onFailure(exception)
                            }
                    }.addOnFailureListener { exception ->
                        _isLoading.value = false
                        onFailure(exception)
                    }
                }.addOnFailureListener { exception ->
                    _isLoading.value = false
                    onFailure(exception)
                }
        } else {
            db.collection("users").document(email)
                .update(userMap)
                .addOnSuccessListener {
                    _isLoading.value = false
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _isLoading.value = false
                    onFailure(exception)
                }
        }
    }
    // -------- End of EDIT PROFILE --------
}