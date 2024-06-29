package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun KalenderBeasiswaScreen(viewModel: MainViewModel, navController: NavController, type: String) {

    // Observing the LiveData for changes
    val scholarships = when (type) {
        "rekomendasi" -> viewModel.recommendedScholarships.observeAsState(initial = emptyList()).value
        "luarnegeri" -> viewModel.scholarships.observeAsState(initial = emptyList()).value
        "indonesia" -> viewModel.indonesiaScholarships.observeAsState(initial = emptyList()).value
        else -> emptyList() // Menyediakan default kosong jika tipe tidak sesuai
    }
    val totalScholarshipsCount = when (type) {
        "luarnegeri" -> viewModel.totalScholarshipsCount.observeAsState(initial = 0).value
        "rekomendasi" -> viewModel.totalRecommendedScholarshipsCount.observeAsState(initial = 0).value
        "indonesia" -> viewModel.totalIndonesiaScholarshipsCount.observeAsState(initial = 0).value
        else -> 0 // Menyediakan default jika tipe tidak sesuai
    }
    val searchedScholarships = viewModel.searchedScholarships.observeAsState(initial = emptyList())
    val totalSearchedScholarshipsCount =
        viewModel.totalSearchedScholarshipsCount.observeAsState(initial = 0)
    val isSearching = viewModel.isSearching.observeAsState(initial = false)
    val searchText by viewModel.searchText.observeAsState("")
    val isFiltering = viewModel.isFiltering.observeAsState(initial = false)
    val filteredScholarships = viewModel.filteredScholarships.observeAsState(initial = emptyList())
    val totalFilteredScholarshipsCount =
        viewModel.totalFilteredScholarshipsCount.observeAsState(initial = 0)

    // State for pagination
    val itemsPerPage = 10
    val currentPage = viewModel.currentPage.observeAsState(initial = 0)

    LaunchedEffect(isFiltering) {
        if (isFiltering.value) {
            viewModel.performFiltering(type)
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(Color.Gray) }
    val inputUrutkanBerdasar by viewModel.inputUrutkanBerdasar

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 1.5.dp,
                backgroundColor = Color.White,
                modifier = if (type != "indonesia") {
                    Modifier.height(216.dp)
                } else {
                    Modifier.height(144.dp)
                }
            ) {
                Column(verticalArrangement = Arrangement.Top) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Arrow Back Icon",
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                        Text(
                            text = "Kalender Beasiswa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                    // SEARCH BAR
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
                    ) {
                        // Search Bar
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                viewModel.setSearchText(it)
                                viewModel.searchScholarshipsByName(searchText, type)
                            },
                            placeholder = { Text("Cari beasiswa") },
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.Gray
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(
                                    0xFF8F79E8
                                ),
                                cursorColor = MaterialTheme.colors.onSurface,
                                focusedTextColor = MaterialTheme.colors.onSurface,
                                unfocusedTextColor = MaterialTheme.colors.onSurface
                            ),
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.resetSearching()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Icon",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            }
                        )
                    }
                    // BUTTON FILTER DAN SELECT BOX
                    if (type != "indonesia") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Button(
                                onClick = {
                                    // TODO: open drawer
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(
                                        0xFF8F79E8
                                    )
                                ),
                                modifier = Modifier
                                    .weight(0.5f)
                                    .height(48.dp)
                            ) {
                                Text(text = "Filter", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            // DROPDOWN URUTKAN BERDASAR
                            // TODO: JIKA value dari inputUrutkanBerdasar terlalu panjang, maka ubah jadi ...
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clickable {
                                        dropdownExpanded = true
                                        borderColor = Color(0xFF8F79E8)
                                    },
                            ) {
                                OutlinedTextField(
                                    enabled = false,
                                    value = inputUrutkanBerdasar,
                                    onValueChange = {
                                        viewModel.updateSortingPreference(it, type = type)
                                    },
                                    label = {
                                        androidx.compose.material3.Text(
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
                                            Text(
                                                "Nama A-Z",
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateSortingPreference(
                                                "Nama A-Z",
                                                type = type
                                            )
                                            dropdownExpanded = false
                                        },
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Nama Z-A",
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateSortingPreference(
                                                "Nama Z-A",
                                                type = type
                                            )
                                            dropdownExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Terpopuler",
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateSortingPreference(
                                                "Terpopuler",
                                                type = type
                                            )
                                            dropdownExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Deadline Terdekat",
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateSortingPreference(
                                                "Deadline Terdekat",
                                                type = type
                                            )
                                            dropdownExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Deadline Terjauh",
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateSortingPreference(
                                                "Deadline Terjauh",
                                                type = type
                                            )
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if (type == "indonesia") {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    if (isFiltering.value) {
                        Text(
                            text = "Menampilkan ${totalFilteredScholarshipsCount.value} Beasiswa",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (isSearching.value) {
                        Text(
                            text = "Menampilkan ${totalSearchedScholarshipsCount.value} Beasiswa",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "Menampilkan $totalScholarshipsCount Beasiswa",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerFilterOptions(
                viewModel = viewModel,
                scope = scope,
                scaffoldState = scaffoldState,
                type = type
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            val scholarshipsToShow = when {
                isFiltering.value -> filteredScholarships.value
                isSearching.value -> searchedScholarships.value
                else -> scholarships
            }

            // Calculate the start and end indices for the current page
            val startIndex = currentPage.value * itemsPerPage
            val endIndex = minOf(startIndex + itemsPerPage, scholarshipsToShow.size)

            // Display the scholarships for the current page
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(scholarshipsToShow.subList(startIndex, endIndex)) { index, beasiswa ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (type == "indonesia" && index > 0) {
                        Divider(
                            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                            color = Color.Gray
                        )
                    }
                    if (type == "indonesia") {
                        val beasiswaIndonesia = beasiswa as BeasiswaIndonesia
                        BeasiswaIndonesiaItem(
                            beasiswa = beasiswaIndonesia,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
                        val beasiswaLuar = beasiswa as Beasiswa
                        BeasiswaItem(
                            beasiswa = beasiswaLuar,
                            navController = navController,
                        )
                    }
                }

                // Pagination controls
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (currentPage.value > 0) viewModel.setCurrentPage(-1) },
                            enabled = currentPage.value > 0
                        ) {
                            Text("Previous")
                        }
                        Text("Page ${currentPage.value + 1}")
                        Button(
                            onClick = {
                                if (startIndex + itemsPerPage < scholarshipsToShow.size) viewModel.setCurrentPage(
                                    1
                                )
                            },
                            enabled = startIndex + itemsPerPage < scholarshipsToShow.size
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BeasiswaIndonesiaItem(beasiswa: BeasiswaIndonesia, navController: NavController) {
    Text(
        text = beasiswa.name,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 18.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .heightIn(min = 32.dp)
            .clickable { navController.navigate("${Screen.DetailBeasiswaScreen.route}/${beasiswa.id}/beasiswaIndonesia") }
    )
}

@Composable
fun BeasiswaItem(beasiswa: Beasiswa, navController: NavController) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { navController.navigate("${Screen.DetailBeasiswaScreen.route}/${beasiswa.id}/beasiswaLuarNegeri") },
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
                                androidx.compose.material3.Text(
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
                                androidx.compose.material3.Text(
                                    text = beasiswa.city ?: beasiswa.country,
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onSurface
                                    //                        color = Color(0xCC17181A)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                val beasiswaText = if (beasiswa.fundingStatus != null) {
                    "Beasiswa ${beasiswa.fundingStatus}"
                } else {
                    "Beasiswa ${beasiswa.amount}"
                }
                androidx.compose.material3.Text(
                    text = beasiswaText,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1, // Batasi jumlah baris menjadi 1
                    overflow = TextOverflow.Ellipsis // Tambahkan elipsis jika teks terlalu panjang
                )
                Spacer(modifier = Modifier.height(4.dp))
                // NAMA BEASISWA
                androidx.compose.material3.Text(
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
                    androidx.compose.material3.Text(
                        text = "Deadline",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                    beasiswa.deadline?.let {
                        androidx.compose.material3.Text(
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

@Composable
fun DrawerFilterOptions(
    viewModel: MainViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    type: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Filter", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Jenjang", fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                SelectableCard(
                    text = "S1",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    viewModel = viewModel
                )
                SelectableCard(
                    text = "S2",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    viewModel = viewModel
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SelectableCard(
                    text = "S3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    viewModel = viewModel
                )
                SelectableCard(
                    text = "Lainnya",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    viewModel = viewModel
                )
            }
            Divider(
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = Color.LightGray
            )
            Text(
                text = "Tipe Pendanaan",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                SelectableCard(
                    text = "Fully Funded",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    viewModel = viewModel
                )
                SelectableCard(
                    text = "Partial Funded",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    viewModel = viewModel
                )
            }
            Divider(
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = Color.LightGray
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .padding(end = 4.dp),
                border = BorderStroke(1.dp, Color(0xFF8F79E8)),
                onClick = {
                    viewModel.resetCardSelections()
                }
            ) {
                Text(
                    text = "Atur Ulang",
                    color = Color(0xFF8F79E8)
                )
            }
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .padding(start = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(
                        0xFF8F79E8
                    )
                ),
                onClick = {
                    // TODO: Lakukan filtering
                    viewModel.checkAndSetFiltering(type = type)

                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            ) {
                Text(text = "Terapkan", color = Color.White)
            }
        }
    }
}

@Composable
fun SelectableCard(
    modifier: Modifier = Modifier,
    text: String,
    viewModel: MainViewModel,
) {
    val selectedCards by viewModel.selectedCards.observeAsState(initial = emptyMap())
    val isSelected = selectedCards[text] ?: false

    val backgroundColor = if (isSelected) Color.White else Color(0xFFE0E0E0)
    val borderColor = if (isSelected) Color(0xFF8F79E8) else Color.Transparent
    val textColor = if (isSelected) Color(0xFF8F79E8) else Color.Black

    Card(
        modifier = modifier
            .clickable {
                when (text) {
                    "S1", "S2", "S3", "Lainnya" -> {
                        viewModel.toggleDegreeSelection(text)
                    }

                    "Fully Funded", "Partial Funded" -> {
                        viewModel.toggleFundingStatusSelection(text)
                    }

                    else -> {
                        viewModel.toggleCardSelection(text)
                    }
                }
            },
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color(0xFF8F79E8),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(16.dp)
                    )
                }
                Text(
                    text = text,
                    color = textColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}