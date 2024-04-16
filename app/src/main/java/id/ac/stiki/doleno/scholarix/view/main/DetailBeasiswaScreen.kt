package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp),
        ) {
            val beasiswa = viewModel.getABeasiswaById(id)
            if (beasiswa != null) {
                Text(
                    text = beasiswa.nama,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(text = beasiswa.pendanaan, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    // Menampilkan setiap derajat dalam kartu terpisah
                    beasiswa.degree.forEach { degree ->
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
                        Text(
                            text = beasiswa.lokasi.kota,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
//                        color = Color(0xCC17181A)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
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
                        Text(
                            text = beasiswa.deadline,
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}