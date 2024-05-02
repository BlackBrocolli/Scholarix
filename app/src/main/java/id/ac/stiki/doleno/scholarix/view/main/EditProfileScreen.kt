package id.ac.stiki.doleno.scholarix.view.main

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun EditProfileScreen(navController: NavController, userData: UserData?) {
    var inputEmail by remember { mutableStateOf("") }
    var inputNamaLengkap by remember { mutableStateOf("") }
    var isInputValueChanged by remember { mutableStateOf(false) }
    var usernameOrNama by remember { mutableStateOf("") }

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<String?>(userData?.profilePictureUrl) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it.toString()
        }
    }

    // Get nama lengkap from firestore
    val db = Firebase.firestore
    val ref = userData?.email?.let { db.collection("users").document(it) }
    val namaLengkap = remember { mutableStateOf("") }
    ref?.get()?.addOnSuccessListener {
        if (it != null) {
            val retrievedNamaLengkap = it.data?.get("namaLengkap").toString()
            namaLengkap.value = retrievedNamaLengkap
        } else {
            Log.d("Nama Kosong", "Tidak berhasil")
        }
    }

    if (userData != null) {
        if (userData.email != null) {
            inputEmail = userData.email.toString()
        }
        if (userData.username != null && userData.username != "") {
            inputNamaLengkap = userData.username.toString()
            usernameOrNama = "Username"
        } else {
            inputNamaLengkap = namaLengkap.toString()
            usernameOrNama = "Nama Lengkap"
        }
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
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
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.avataaars),
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
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                readOnly = true
            )
            OutlinedTextField(
                value = inputNamaLengkap,
                onValueChange = {
                    inputNamaLengkap = it
                    isInputValueChanged = true
                },
                label = { Text(text = usernameOrNama) }, // Ubah label
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text) // Ubah keyboardType
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    // TODO Update profil
                },
                enabled = isInputValueChanged
            ) {
                Text(text = "Simpan")
            }
        }
    }
}