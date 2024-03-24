package id.ac.stiki.doleno.scholarix.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.navigation.Screen

@Composable
fun SignupScreen(navController: NavController) {
    var inputNamaLengkap by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputNomorHP by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputKonfirmasiPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmasiPasswordVisible by remember { mutableStateOf(false) }

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
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = inputNamaLengkap,
                onValueChange = { inputNamaLengkap = it },
                label = { Text(text = "Nama Lengkap") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inputEmail,
                onValueChange = { inputEmail = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = inputNomorHP,
                onValueChange = { inputNomorHP = it },
                label = { Text(text = "No Handphone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                value = inputPassword,
                onValueChange = { inputPassword = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            OutlinedTextField(
                value = inputKonfirmasiPassword,
                onValueChange = { inputKonfirmasiPassword = it },
                label = { Text(text = "Konfirmasi Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (konfirmasiPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(
                        onClick = { konfirmasiPasswordVisible = !konfirmasiPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (konfirmasiPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (konfirmasiPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(2.dp))
            /* TODO: ubah button jadi yang primary jika semua field sudah diisi*/
            FilledTonalButton(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), onClick = { /*TODO*/ }) {
                Text(text = "Daftar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Atau daftar dengan akun",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    /* TODO: Ubah jadi iconnya Google */
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp) // Sesuaikan ukuran ikon sesuai kebutuhan
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Spacer untuk jarak antara ikon dan teks
                    Text(
                        text = "Google",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Sudah punya akun?",
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Masuk",
                    fontSize = 12.sp,
                    color = Color(android.graphics.Color.parseColor("#007FFF")),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.LoginScreen.route)
                    }
                )
            }

        }
    }
}