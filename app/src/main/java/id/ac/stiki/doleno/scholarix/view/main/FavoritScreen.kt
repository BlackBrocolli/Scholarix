package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.stiki.doleno.scholarix.model.DummyBeasiswa
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import id.ac.stiki.doleno.scholarix.R
import androidx.compose.ui.res.painterResource

@Composable
fun FavoritScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(DummyBeasiswa.beasiswaList) { beasiswa ->
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(bottom = 16.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // BARIS DEGREE & LOKASI
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
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            ),
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
                                        text = beasiswa.city ?: beasiswa.country,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        //                        color = Color(0xCC17181A)
                                    )
                                }


                            }

                            // ICON FAVORITE
                            IconButton(modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(percent = 15)
                                )
                                .size(32.dp),
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))
                        // TIPE PENDANAAN
                        Text(text = "Beasiswa ${beasiswa.fundingStatus}", fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        // NAMA BEASISWA
                        Text(
                            text = beasiswa.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        )
                    }

                    // DEADLINE CARD
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x80D9D9D9),
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 6.dp, horizontal = 12.dp)
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
                }
            }
        }
    }
}