package id.ac.stiki.doleno.scholarix.view.main

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import id.ac.stiki.doleno.scholarix.R
import id.ac.stiki.doleno.scholarix.model.google.UserData
import id.ac.stiki.doleno.scholarix.navigation.Screen

@Composable
fun ProfileScreen(
    navController: NavController,
    userData: UserData?,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val versionName = remember { getAppVersionName(context) }

    val db = Firebase.firestore
    val ref = userData?.email?.let { db.collection("users").document(it) }
    val namaLengkap = remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf<String?>(null) }
    ref?.get()?.addOnSuccessListener {
        if (it != null) {
            val retrievedNamaLengkap = it.data?.get("namaLengkap").toString()
            namaLengkap.value = retrievedNamaLengkap
            profilePictureUrl = it.data?.get("profilePictureUrl") as String?
        } else {
            Log.d("Nama Kosong", "Tidak berhasil")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val commonModifier = Modifier
                .size(100.dp)
                .clip(CircleShape) // Membuat gambar menjadi lingkaran
                .border(1.dp, Color.Black, CircleShape)

//            if (userData?.profilePictureUrl != null) {
            if (profilePictureUrl != null) {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = commonModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.avataaars),
                    contentDescription = "Default Profile Picture",
                    modifier = commonModifier
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (userData?.username != null && userData.username != "") {
                Text(text = userData.username, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            } else {
                Text(text = namaLengkap.value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (userData?.email != null) {
                Text(text = userData.email)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 16.dp, top = 16.dp)
                .clickable { navController.navigate(Screen.EditProfileScreen.route) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_edit_24),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Edit Profil")
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_gavel_24),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Syarat dan Ketentuan")
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_key_24),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Kebijakan Privasi")
        }
        Divider()
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                border = BorderStroke(1.dp, Color.Red),
                onClick = { onSignOut() }) {
                Text(
                    text = "Log Out",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Version $versionName",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun getAppVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}