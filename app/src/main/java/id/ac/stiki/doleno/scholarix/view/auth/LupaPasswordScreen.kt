package id.ac.stiki.doleno.scholarix.view.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import id.ac.stiki.doleno.scholarix.R

@Composable
fun LupaPasswordScreen(navController: NavController) {

    var inputEmail by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun validateEmail(email: String) {
        emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) ""
        else "Alamat email tidak valid"
    }

    fun isFormValid(): Boolean {
        return inputEmail.isNotBlank() && emailError.isEmpty()
    }

    fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Email reset password telah dikirim.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Gagal mengirim email reset password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(title = "Lupa Password") {
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
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.lupa_password_text),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colors.onSurface
                )
                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = {
                        inputEmail = it
                        validateEmail(it)
                    },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
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
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        sendPasswordResetEmail(inputEmail)
                    },
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.LightGray,
                        containerColor = Color(0xFF8F79E8),
                    ),
                    enabled = isFormValid() // Memeriksa apakah formulir valid
                ) {
                    Text(
                        text = "Reset Password",
                        color = Color.White
                    )
                }
            }
        }
    }
}