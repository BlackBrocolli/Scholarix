package id.ac.stiki.doleno.scholarix.view.main

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import id.ac.stiki.doleno.scholarix.R
import id.ac.stiki.doleno.scholarix.model.google.UserData
import id.ac.stiki.doleno.scholarix.view.auth.MyTopAppBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import id.ac.stiki.doleno.scholarix.viewmodel.AuthViewModel

@Composable
fun EditProfileScreen(viewModel: AuthViewModel, navController: NavController, userData: UserData?) {
    var inputEmail by remember { mutableStateOf("") }
    val inputNamaLengkap by viewModel.inputNamaLengkap.observeAsState(initial = "")
    var isInputValueChanged by remember { mutableStateOf(false) }
    var usernameOrNama by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var profilePictureUrl by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            isInputValueChanged = true
        }
    }

    // Get data from Firestore
    val db = Firebase.firestore
    val ref = userData?.email?.let { db.collection("users").document(it) }
    val namaLengkap = remember { mutableStateOf("") }
    ref?.get()?.addOnSuccessListener {
        if (it != null) {
            val retrievedNamaLengkap = it.data?.get("namaLengkap").toString()
            namaLengkap.value = retrievedNamaLengkap
            viewModel.setInputNamaLengkap(namaLengkap.value)
            profilePictureUrl = it.data?.get("profilePictureUrl") as String?
        } else {
            Log.d("Nama Kosong", "Tidak berhasil")
        }
    }

    if (userData != null) {
        if (userData.email != null) {
            inputEmail = userData.email.toString()
        }
//        usernameOrNama = if (userData.username != null && userData.username != "") {
//            viewModel.setInputNamaLengkap(userData.username.toString())
//            "Username"
//        } else {
//            viewModel.setInputNamaLengkap(namaLengkap.value)
//            "Nama Lengkap"
//        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(title = "Edit Profil") {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        launcher.launch("image/*") // This will now open the gallery and handle the result
                    }
            ) {
                val commonModifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape) // Membuat gambar menjadi lingkaran
                    .border(1.dp, Color.Black, CircleShape)
                    .align(Alignment.Center)
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Profile Picture",
                        modifier = commonModifier,
                        contentScale = ContentScale.Crop
                    )
                } else if (profilePictureUrl != null && profilePictureUrl!!.isNotEmpty()) {
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = "Profile Picture",
                        modifier = commonModifier,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = null,
                        modifier = commonModifier
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_camera_alt_24),
                        contentDescription = "Edit",
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = inputEmail,
                onValueChange = { inputEmail = it },
                label = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray,
                    focusedBorderColor = Color(0xFF8F79E8),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8F79E8),
                    unfocusedLabelColor = Color.Gray
                )
            )
            OutlinedTextField(
                value = inputNamaLengkap,
                onValueChange = {
                    viewModel.setInputNamaLengkap(it)
                    isInputValueChanged = true
                },
                label = { Text(text = "Nama Lengkap") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text), // Ubah keyboardType
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray,
                    focusedBorderColor = Color(0xFF8F79E8),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8F79E8),
                    unfocusedLabelColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        val email = inputEmail
                        val inputanNamaLengkap = inputNamaLengkap
                        val profilePictureUri = selectedImageUri

                        viewModel.updateUserProfile(email, inputanNamaLengkap, profilePictureUri,
                            onSuccess = {
                                // Refresh the profile picture URL
                                ref?.get()?.addOnSuccessListener { document ->
                                    if (document != null) {
                                        profilePictureUrl =
                                            document.data?.get("profilePictureUrl") as String?
                                        selectedImageUri = null
                                        navController.navigateUp()
                                    }
                                }
                            },
                            onFailure = { exception ->
                                // Handle failure
                                Log.e(
                                    "EditProfileScreen",
                                    "Failed to update profile: ${exception.message}"
                                )
                            }
                        )
                    },
                    enabled = isInputValueChanged,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.LightGray,
                        containerColor = Color(0xFF8F79E8),
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp),
                            color = Color.White
                        )
                    } else {
                        val textColor = if (isInputValueChanged) Color.White else Color.Black
                        Text(text = "Simpan", color = textColor)
                    }
                }
            }
        }
    }
}
