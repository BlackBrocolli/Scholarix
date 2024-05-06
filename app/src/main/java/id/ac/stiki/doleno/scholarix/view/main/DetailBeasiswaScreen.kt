package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun DetailBeasiswaScreen(id: Long, viewModel: MainViewModel, navController: NavController) {
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
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            val beasiswa = viewModel.getABeasiswaById(id)
            if (beasiswa != null) {
                Text(
                    text = beasiswa.nama,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                beasiswa.pendanaan?.let { Text(text = it, fontSize = 14.sp) }
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
                        beasiswa.lokasi.kota?.let {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = beasiswa.lokasi.negara, fontSize = 14.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "INSTITUSI ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = beasiswa.institusi, fontSize = 14.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "JUMLAH BEASISWA ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = beasiswa.jumlah, fontSize = 14.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "DURASI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = beasiswa.durasi, fontSize = 14.sp)
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
                        beasiswa.benefits.forEach { document ->
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
                            text = "UMUR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = beasiswa.umur, fontSize = 14.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "SERTIFIKAT BAHASA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = beasiswa.sertifikatBahasa, fontSize = 14.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "DOKUMEN",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        beasiswa.documents.forEach { document ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
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
            }
        }
    }
}