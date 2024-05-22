package id.ac.stiki.doleno.scholarix.view.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
                    Toast.makeText(context, "Email reset password telah dikirim.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal mengirim email reset password.", Toast.LENGTH_SHORT).show()
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.lupa_password_text),
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
            OutlinedTextField(
                value = inputEmail,
                onValueChange = {
                    inputEmail = it
                    validateEmail(it)
                },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    sendPasswordResetEmail(inputEmail)
                },
                enabled = isFormValid() // Memeriksa apakah formulir valid
            ) {
                Text(text = "ResetPassword")
            }
        }
    }
}