package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel
import android.content.Intent
import android.net.Uri

@Composable
fun DetailBeasiswaScreen(id: String, viewModel: MainViewModel, navController: NavController) {

    val scholarship = viewModel.scholarship.observeAsState(initial = Beasiswa())
    val isLoading = viewModel.isLoadingScholarship.observeAsState(initial = false)
    val isError = viewModel.isErrorScholarship.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.fetchScholarshipById(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 1.5.dp,
                title = { },
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back Icon"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val url = scholarship.value.link
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    navController.context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Kunjungi Website")
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading.value -> {
                    // Display a loading animation or indicator
                    CircularProgressIndicator()
                }

                isError.value -> {
                    // Show an error message and possibly a retry button
                    Text("Failed to load scholarship details. Tap to retry.",
                        modifier = Modifier
                            .clickable { viewModel.fetchScholarshipById(id) }
                            .padding(16.dp)
                    )
                }

                else -> {
                    DetailBeasiswaContent(scholarship.value)
                }
            }
        }
    }
}

@Composable
private fun DetailBeasiswaContent(beasiswa: Beasiswa) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = beasiswa.name,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
        beasiswa.fundingStatus?.let { Text(text = it, fontSize = 14.sp) }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            // Menampilkan setiap derajat dalam kartu terpisah
            beasiswa.degrees.forEach { degree ->
                val containerColor = when (degree) {
                    "S1" -> Color(0xFFD9FAE7)
                    "S2" -> Color(0x401B73B3)
                    "S3" -> Color(0x40C77738)
                    else -> Color.Gray // Warna default jika derajat tidak dikenali
                }
                val textColor = when (degree) {
                    "S1" -> Color(0xFF21764C) // Warna teks untuk S1
                    "S2" -> Color(0xFF1B73B3) // Warna teks untuk S2
                    "S3" -> Color(0xFFC77738) // Warna teks untuk S3
                    else -> Color.Black // Warna teks default jika derajat tidak dikenali
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = containerColor,
                    )
                ) {
                    Text(
                        text = degree,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
//                Spacer(modifier = Modifier.width(2.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x80D9D9D9),
                )
            ) {
                beasiswa.city.let {
                    if (it != null) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            //                        color = Color(0xCC17181A)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // CARD DEADLINE
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80D9D9D9),
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Deadline", fontSize = 12.sp)
                beasiswa.deadline?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // CARD TENTANG
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Tentang", fontWeight = FontWeight.Bold)
                Text(
                    text = "LOKASI NEGARA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\u2022", // Unicode untuk simbol bullet
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )
                    Text(text = beasiswa.country, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "INSTITUSI ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\u2022", // Unicode untuk simbol bullet
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )
                    Text(text = beasiswa.institution, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "JUMLAH BEASISWA ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\u2022", // Unicode untuk simbol bullet
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )
                    Text(text = beasiswa.opportunities, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "DURASI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\u2022", // Unicode untuk simbol bullet
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )
                    Text(text = beasiswa.duration, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // CARD BENEFIT
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.Blue),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Manfaat", fontWeight = FontWeight.Bold, color = Color.Blue)
                beasiswa.benefitsHtml.forEach { document ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = document, fontSize = 14.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // CARD PERSYARATAN
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Persyaratan", fontWeight = FontWeight.Bold)
                Text(
                    text = "KEWARGANEGARAAN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\u2022", // Unicode untuk simbol bullet
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )
                    Text(text = beasiswa.eligibleNationals, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "SERTIFIKAT BAHASA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (beasiswa.languageRequirements.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = beasiswa.languageRequirements, fontSize = 14.sp)
                    }
                } else {
                    Text(text = "-")
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "DOKUMEN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (beasiswa.documentsHtml.isNotEmpty()) {
                    beasiswa.documentsHtml.forEach { document ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = document, fontSize = 14.sp)
                        }
                    }
                } else {
                    Text(text = "-")
                }
            }
        }
    }
}
