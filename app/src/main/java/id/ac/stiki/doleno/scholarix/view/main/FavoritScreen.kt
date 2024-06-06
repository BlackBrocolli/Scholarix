package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import id.ac.stiki.doleno.scholarix.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.google.UserData
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun FavoritScreen(viewModel: MainViewModel, userData: UserData, navController: NavController) {

    // TODO: Tampilkan favorite yang beasiswa Indonesia
    val scholarships by viewModel.scholarships.observeAsState(initial = emptyList())
    val indonesiaScholarships by viewModel.indonesiaScholarships.observeAsState(initial = emptyList())
    val favorites by viewModel.favorites.observeAsState(emptyList())
    val userEmail = userData.email

    LaunchedEffect(userEmail) {
        if (userEmail != null) {
            viewModel.loadUserFavorites(userEmail)
        }
    }

    val favoriteBeasiswaList = scholarships.filter { beasiswa ->
        favorites.contains(beasiswa.name)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (favoriteBeasiswaList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(favoriteBeasiswaList) { beasiswa ->
                    FavoriteBeasiswaItem(beasiswa = beasiswa, navController = navController)
                }
            }
        } else {
            Text(
                text = "Belum ada beasiswa yang ditambahkan ke daftar favorit.",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FavoriteBeasiswaItem(beasiswa: Beasiswa, navController: NavController) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                navController.navigate("${Screen.DetailBeasiswaScreen.route}/${beasiswa.id}/beasiswaLuarNegeri")
            },
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
                        if (beasiswa.city != null || beasiswa.country != "") {
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
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                    //                        color = Color(0xCC17181A)
                                )
                            }
                        }
                    }

                    // ICON FAVORITE
//                    IconButton(modifier = Modifier
////                                .background(
////                                    MaterialTheme.colorScheme.primary,
//                        .background(
//                            color = Color(0xFF00AFEA),
//                            RoundedCornerShape(percent = 15)
//                        )
//                        .size(32.dp),
//                        onClick = { /*TODO*/ }
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.baseline_favorite_border_24),
//                            contentDescription = null,
//                            tint = Color.White
//                        )
//                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // TIPE PENDANAAN
                val beasiswaText = if (beasiswa.fundingStatus != null) {
                    "Beasiswa ${beasiswa.fundingStatus}"
                } else {
                    "Beasiswa ${beasiswa.amount}"
                }
                Text(
                    text = beasiswaText,
                    fontSize = 12.sp,
                    maxLines = 1, // Batasi jumlah baris menjadi 1
                    overflow = TextOverflow.Ellipsis // Tambahkan elipsis jika teks terlalu panjang
                )
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