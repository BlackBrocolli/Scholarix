package id.ac.stiki.doleno.scholarix.view.main

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.Button
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun KalenderBeasiswaScreen(viewModel: MainViewModel, navController: NavController) {
    // TODO: INI HARUSNYA DIAMBIL DARI MainViewModel
//    var inputUrutkanBerdasar by remember { mutableStateOf("Deadline Terdekat") }
    var inputUrutkanBerdasar by viewModel.inputUrutkanBerdasar

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 1.5.dp,
                backgroundColor = Color.White,
                modifier = Modifier.height(200.dp)
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                // TODO: Menampilkan menu dari bawah/samping berisi filter
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8F79E8)),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(text = "Filter", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        // TODO: KETIKA DITEKAN MAKA MUNCUL ITEM SEPERTI DROPDOWNMENU ITEM
                        // TODO: JIKA value dari inputUrutkanBerdasar terlalu panjang, maka ubah jadi ...
                        OutlinedTextField(
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
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            readOnly = true
                        )
                    }
                    // TODO: Jumlah beasiswa yang tampil berubah sesuai berapa banyak beasiswa yang tampil
                    Text(
                        text = "Menampilkan 1002 Beasiswa",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
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

        }
    }
}