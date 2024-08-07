package id.ac.stiki.doleno.scholarix.view.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.R
import id.ac.stiki.doleno.scholarix.model.google.SignInState
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    state: SignInState,
    onSignInGoogleClick: () -> Unit
) {
    // TODO: MEMINDAHKAN VARIABEL DAN FUNCTION KE VIEWMODEL: AUTH
    var inputNamaLengkap by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputNomorHP by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputKonfirmasiPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmasiPasswordVisible by remember { mutableStateOf(false) }
    var namaError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var nomorHPError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var konfirmasiPasswordError by remember { mutableStateOf("") }
    val isLoadingRegisterButton by authViewModel.isLoadingRegisterButton.observeAsState(initial = false)

    fun validateNama(nama: String) {
        namaError = if (nama.isBlank()) {
            "Nama tidak boleh kosong"
        } else if (!nama.matches(".*[a-zA-Z]+.*".toRegex())) {
            "Nama harus mengandung minimal satu huruf"
        } else {
            ""
        }
    }

    fun validateEmail(email: String) {
        emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) ""
        else "Alamat email tidak valid"
    }

    fun validateNomorHP(nomorHP: String) {
        nomorHPError = if (!nomorHP.startsWith("08")) {
            "Nomor HP harus dimulai dari 08"
        } else if (!nomorHP.matches("^08[0-9]{8,11}\$".toRegex())) {
            "Nomor HP tidak valid"
        } else {
            ""
        }
    }

    fun validatePassword(password: String) {
        passwordError = if (password.length < 6) {
            "Password harus terdiri dari minimal 6 karakter"
        } else {
            ""
        }
    }

    fun validateKonfirmasiPassword(password: String, konfirmasiPassword: String) {
        konfirmasiPasswordError = when {
            konfirmasiPassword.length < 6 -> "Konfirmasi password harus terdiri dari minimal 6 karakter"
            password != konfirmasiPassword -> "Password dan konfirmasi password tidak sama"
            else -> ""
        }
    }

    fun isFormValid(): Boolean {
        return inputNamaLengkap.isNotBlank() && namaError.isEmpty() &&
                inputEmail.isNotBlank() && emailError.isEmpty() &&
                inputNomorHP.isNotBlank() && nomorHPError.isEmpty() &&
                inputPassword.length >= 6 && passwordError.isEmpty() &&
                inputKonfirmasiPassword.length >= 6 && konfirmasiPasswordError.isEmpty() &&
                inputPassword == inputKonfirmasiPassword
    }

    // ERROR HANDLING
    // Bisa dihapus saja nanti
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInErrorMessage) {
        state.signInErrorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(title = "Daftar Akun") {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = inputNamaLengkap,
                    onValueChange = {
                        inputNamaLengkap = it
                        validateNama(it)
                    },
                    label = { Text(text = "Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = namaError.isNotEmpty(),
                    trailingIcon = {
                        if (namaError.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF8F79E8),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8F79E8),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF8F79E8),
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Black
                    )
                )
                if (namaError.isNotEmpty()) {
                    Text(
                        text = namaError,
                        color = Color.Red,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = {
                        inputEmail = it
                        validateEmail(it)
                    },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    isError = emailError.isNotEmpty(),
                    trailingIcon = {
                        if (emailError.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF8F79E8),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8F79E8),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF8F79E8),
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Black
                    )
                )
                if (emailError.isNotEmpty()) {
                    Text(
                        text = emailError,
                        color = Color.Red,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                OutlinedTextField(
                    value = inputNomorHP,
                    onValueChange = {
                        inputNomorHP = it
                        validateNomorHP(it)
                    },
                    label = { Text(text = "No Handphone") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nomorHPError.isNotEmpty(),
                    trailingIcon = {
                        if (nomorHPError.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF8F79E8),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8F79E8),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF8F79E8),
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Black
                    )
                )
                if (nomorHPError.isNotEmpty()) {
                    Text(
                        text = nomorHPError,
                        color = Color.Red,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                OutlinedTextField(
                    value = inputPassword,
                    onValueChange = {
                        inputPassword = it
                        validatePassword(it)
                        validateKonfirmasiPassword(it, inputKonfirmasiPassword)
                    },
                    label = { Text(text = "Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordError.isNotEmpty(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF8F79E8),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8F79E8),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF8F79E8),
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Black
                    ),
                )
                if (passwordError.isNotEmpty()) {
                    Text(
                        text = passwordError,
                        color = Color.Red,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                OutlinedTextField(
                    value = inputKonfirmasiPassword,
                    onValueChange = {
                        inputKonfirmasiPassword = it
                        validateKonfirmasiPassword(inputPassword, it)
                    },
                    label = { Text(text = "Konfirmasi Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (konfirmasiPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = konfirmasiPasswordError.isNotEmpty(),
                    trailingIcon = {
                        IconButton(
                            onClick = { konfirmasiPasswordVisible = !konfirmasiPasswordVisible }
                        ) {
                            Icon(
                                imageVector = if (konfirmasiPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (konfirmasiPasswordVisible) "Hide password" else "Show password",
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF8F79E8),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8F79E8),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF8F79E8),
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Black
                    ),
                )
                if (konfirmasiPasswordError.isNotEmpty()) {
                    Text(
                        text = konfirmasiPasswordError,
                        color = Color.Red,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Spacer(modifier = Modifier.height(2.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        authViewModel.signUp(
                            inputEmail,
                            inputPassword,
                            inputNamaLengkap,
                            inputNomorHP,
                            navController,
                            context
                        )
//                    inputEmail = ""
//                    inputPassword = ""
//                    inputNamaLengkap = ""
//                    inputNomorHP = ""
//                    Toast.makeText(
//                        context,
//                        "Daftar berhasil!",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.LightGray,
                        containerColor = Color(0xFF8F79E8),
                    ),
                    enabled = isFormValid() // Memeriksa apakah formulir valid
                ) {
                    if (isLoadingRegisterButton) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(text = "Daftar", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Atau daftar dengan akun",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onSignInGoogleClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(20.dp) // Sesuaikan ukuran ikon sesuai kebutuhan
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sudah punya akun?",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Masuk",
                        fontSize = 12.sp,
                        color = Color(0xFF8F79E8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.LoginScreen.route)
                        }
                    )
                }
            }

        }
    }
}