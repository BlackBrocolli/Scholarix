package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.BeasiswaIndonesia
import id.ac.stiki.doleno.scholarix.model.google.UserData
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.view.auth.MyTopAppBar
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun FavoritList(
    title: String,
    navController: NavController,
    viewModel: MainViewModel,
    userData: UserData
) {

    val scholarships by viewModel.scholarships.observeAsState(initial = emptyList())
    val indonesiaScholarships by viewModel.indonesiaScholarships.observeAsState(initial = emptyList())
    val favoriteScholarships by viewModel.favoriteScholarships.observeAsState(initial = emptyList())
    val favorites by viewModel.favorites.observeAsState(emptyList())
    val userEmail = userData.email

    var dropdownExpanded by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(Color.Gray) }

    val inputUrutkanBerdasarFavorit by viewModel.inputUrutkanBerdasarFavorit

    LaunchedEffect(userEmail) {
        if (userEmail != null) {
            viewModel.loadUserFavorites(userEmail)
        }
        viewModel.resetSortingBeasiswa()
    }

    val favoriteBeasiswaList = scholarships.filter { beasiswa ->
        favorites.contains(beasiswa.name)
    }

    val favoriteBeasiswaIndonesaList = indonesiaScholarships.filter { beasiswa ->
        favorites.contains(beasiswa.name)
    }

    Scaffold(
        topBar = {
            MyTopAppBar(title = title) {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (favoriteBeasiswaList.isNotEmpty() || favoriteBeasiswaIndonesaList.isNotEmpty()) {

                    viewModel.setFavoriteScholarships(favoriteBeasiswaList)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (title == "Beasiswa Luar Negeri") {
                            // == Sorting ==
                            item {
                                Box(
                                    modifier = Modifier
                                        .height(56.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            dropdownExpanded = true
                                            borderColor = Color(0xFF8F79E8)
                                        }
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = false,
                                        value = inputUrutkanBerdasarFavorit,
                                        onValueChange = {
                                            viewModel.updateSortingPreferenceFavorit(
                                                it,
                                                type = "favorit"
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = "Urutkan Berdasarkan",
                                                color = Color(0xFF8F79E8)
                                            )
                                        },
                                        textStyle = TextStyle(fontSize = 14.sp),
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                            )
                                        },
                                        readOnly = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.Black,  // Warna teks ketika aktif
                                            disabledTextColor = Color.Black, // Warna teks tetap hitam ketika dinonaktifkan
                                            disabledBorderColor = borderColor, // Warna border tetap cerah saat dinonaktifkan
                                            disabledLabelColor = Color(0xFF8F79E8),
                                        )
                                    )
                                    DropdownMenu(
                                        expanded = dropdownExpanded,
                                        onDismissRequest = { dropdownExpanded = false },
                                        modifier = Modifier
                                            .verticalScroll(rememberScrollState())
                                            .heightIn(max = 250.dp)
                                            .background(Color.White),
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                androidx.compose.material.Text(
                                                    "Nama A-Z",
                                                    color = MaterialTheme.colors.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateSortingPreferenceFavorit(
                                                    "Nama A-Z",
                                                    type = "favorit"
                                                )
                                                dropdownExpanded = false
                                            },
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                androidx.compose.material.Text(
                                                    "Nama Z-A",
                                                    color = MaterialTheme.colors.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateSortingPreferenceFavorit(
                                                    "Nama Z-A",
                                                    type = "favorit"
                                                )
                                                dropdownExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                androidx.compose.material.Text(
                                                    "Terpopuler",
                                                    color = MaterialTheme.colors.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateSortingPreferenceFavorit(
                                                    "Terpopuler",
                                                    type = "favorit"
                                                )
                                                dropdownExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                androidx.compose.material.Text(
                                                    "Deadline Terdekat",
                                                    color = MaterialTheme.colors.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateSortingPreferenceFavorit(
                                                    "Deadline Terdekat",
                                                    type = "favorit"
                                                )
                                                dropdownExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                androidx.compose.material.Text(
                                                    "Deadline Terjauh",
                                                    color = MaterialTheme.colors.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.updateSortingPreferenceFavorit(
                                                    "Deadline Terjauh",
                                                    type = "favorit"
                                                )
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        if (title == "Beasiswa Luar Negeri") {
                            if (favoriteScholarships.isNotEmpty()) {
                                items(favoriteScholarships) { beasiswa ->
                                    FavoriteBeasiswaItem(
                                        beasiswa = beasiswa,
                                        navController = navController
                                    )
                                }
                            } else {
                                item {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Text(
                                            text = "Belum ada beasiswa luar negeri yang ditambahkan ke daftar favorit.",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(16.dp),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                    }
                                }
                            }
                        } else if (title == "Beasiswa Indonesia") {
                            if (favoriteBeasiswaIndonesaList.isNotEmpty()) {
                                items(favoriteBeasiswaIndonesaList) { beasiswa ->
                                    FavoriteBeasiswaIndonesiaItem(
                                        beasiswa = beasiswa,
                                        navController = navController
                                    )
                                }
                            } else {
                                item {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Text(
                                            text = "Belum ada beasiswa Indonesia yang ditambahkan ke daftar favorit.",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(16.dp),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Belum ada beasiswa yang ditambahkan ke daftar favorit.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteBeasiswaIndonesiaItem(beasiswa: BeasiswaIndonesia, navController: NavController) {
    Text(
        text = beasiswa.name,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 18.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .heightIn(min = 32.dp)
            .clickable { navController.navigate("${Screen.DetailBeasiswaScreen.route}/${beasiswa.id}/beasiswaIndonesia") }
    )
    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
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
                .padding(vertical = 12.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // BARIS DEGREE & LOKASI
                    Row {
                        Spacer(modifier = Modifier.width(12.dp))
                        // Menampilkan setiap derajat dalam kartu terpisah
                        beasiswa.degrees.forEach { degree ->
                            val containerColor = when (degree) {
                                "S1" -> Color(0xFFD9FAE7)
                                "S2" -> Color(0x401B73B3)
                                "S3" -> Color(0x40C77738)
                                else -> Color(0xFFEDE7F6)
                            }
                            val textColor = when (degree) {
                                "S1" -> Color(0xFF21764C)
                                "S2" -> Color(0xFF1B73B3)
                                "S3" -> Color(0xFFC77738)
                                else -> Color(0xFF4A148C)
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
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.onSurface
                                    //                        color = Color(0xCC17181A)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // TIPE PENDANAAN
                val beasiswaText = if (beasiswa.fundingStatus != null) {
                    "Beasiswa ${beasiswa.fundingStatus}"
                } else {
                    "Beasiswa ${beasiswa.amount}"
                }
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = beasiswaText,
                    fontSize = 12.sp,
                    maxLines = 1, // Batasi jumlah baris menjadi 1
                    overflow = TextOverflow.Ellipsis, // Tambahkan elipsis jika teks terlalu panjang
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                // NAMA BEASISWA
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = beasiswa.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }

            // DEADLINE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 12.dp),
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
                    Text(
                        text = "Deadline",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface
                    )
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