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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun KalenderBeasiswaScreen(viewModel: MainViewModel, navController: NavController) {

    // Observing the LiveData for changes
    val scholarships = viewModel.scholarships.observeAsState(initial = emptyList())
    val filteredScholarships = viewModel.filteredScholarships.observeAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val isError = viewModel.isError.observeAsState(initial = false)
    val totalScholarshipsCount = viewModel.totalScholarshipsCount.observeAsState(initial = 0)
    val totalFilteredScholarshipsCount =
        viewModel.totalFilteredScholarshipsCount.observeAsState(initial = 0)
    val isSearching = viewModel.isSearching.observeAsState(initial = false)
    val searchText by viewModel.searchText.observeAsState("")

    LaunchedEffect(key1 = true) {
        if (scholarships.value.isEmpty()) {
            viewModel.fetchScholarshipDetails()
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var borderColor by remember {
        mutableStateOf(Color.Gray)
    }
    val inputUrutkanBerdasar by viewModel.inputUrutkanBerdasar

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 1.5.dp,
                backgroundColor = Color.White,
                modifier = Modifier.height(216.dp)
            ) {
                Column(verticalArrangement = Arrangement.Top) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Arrow Back Icon"
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
                                //TODO: lakukan operasi pencarian beasiswa
                                viewModel.setSearchText(it)
                                viewModel.searchScholarshipsByName(searchText)
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
                                    contentDescription = "Search Icon"
                                )
                            },
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.resetSearching()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Icon"
                                        )
                                    }
                                }
                            }
                        )
                    }
                    // BUTTON FILTER DAN SELECT BOX
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
                                    viewModel.setInputUrutkanBerdasar(it)
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
                                    .heightIn(max = 200.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Terpopuler") },
                                    onClick = {
                                        viewModel.setInputUrutkanBerdasar("Terpopuler")
                                        dropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Open Terdekat") },
                                    onClick = {
                                        viewModel.setInputUrutkanBerdasar("Open Terdekat")
                                        dropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Deadline Terdekat") },
                                    onClick = {
                                        viewModel.setInputUrutkanBerdasar("Deadline Terdekat")
                                        dropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Deadline Terjauh") },
                                    onClick = {
                                        viewModel.setInputUrutkanBerdasar("Deadline Terjauh")
                                        dropdownExpanded = false
                                    }
                                )
                            }

                        }
                    }
                    if (isSearching.value) {
                        Text(
                            text = "Menampilkan ${totalFilteredScholarshipsCount.value} Beasiswa",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "Menampilkan ${totalScholarshipsCount.value} Beasiswa",
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
            DrawerFilterOptions()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp)
        ) {
            when {
                isLoading.value -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                isError.value -> {
                    // Show an error message and possibly a retry button
                    androidx.compose.material3.Text("Failed to load scholarships. Tap to retry.",
                        modifier = Modifier
                            .clickable { viewModel.fetchScholarshipDetails() }
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (isSearching.value) {
                            items(filteredScholarships.value) { beasiswa ->
                                BeasiswaItem(
                                    beasiswa = beasiswa,
                                    navController = navController
                                )
                            }
                        } else {
                            items(scholarships.value) { beasiswa ->
                                BeasiswaItem(
                                    beasiswa = beasiswa,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerFilterOptions() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(text = "Halo, saya drawer")
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
                onClick = { /*TODO*/ }
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
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Terapkan", color = Color.White)
            }
        }
    }
}

@Composable
fun BeasiswaItem(beasiswa: Beasiswa, navController: NavController) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { navController.navigate("${Screen.DetailBeasiswaScreen.route}/${beasiswa.id}") },
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
                                //                        color = Color(0xCC17181A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // TIPE PENDANAAN
                androidx.compose.material3.Text(
                    text = "Beasiswa ${beasiswa.fundingStatus}",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                // NAMA BEASISWA
                androidx.compose.material3.Text(
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
                    androidx.compose.material3.Text(
                        text = "Deadline",
                        fontSize = 12.sp
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