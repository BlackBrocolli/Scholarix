package id.ac.stiki.doleno.scholarix.view.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import id.ac.stiki.doleno.scholarix.model.Country
import id.ac.stiki.doleno.scholarix.model.google.UserData
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.ui.theme.poppinsFontFamily
import id.ac.stiki.doleno.scholarix.viewmodel.CountryViewModel
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun FourthOnboarding(navController: NavController, viewModel: MainViewModel, userData: UserData) {
    // fetch countries
    val countryViewModel: CountryViewModel = viewModel()
    val viewState by countryViewModel.countriesState
    val userEmail = userData.email
    val isLoading by viewModel.isLoadingSaveUserPreference.observeAsState(initial = false)

//    val context = LocalContext.current
    val countries = viewState.list.sortedBy { it.name }

    // State untuk melacak indeks item negara yang dipilih
    val selectedItems = remember { mutableStateOf(setOf<Int>()) }
    var selectedCountries by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            OnboardingTopBar(
                progress = 1f,
                onBackNavClicked = { navController.navigateUp() },
                onSkipButtonClicked = {
                    navController.navigate(Screen.MainView.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Preferensi Negara",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                // == PILIHAN PREFERENSI NEGARA ==
                when {
                    viewState.loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    viewState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Tidak ada jaringan internet")
                        }
                    }

                    else -> {
                        LazyColumn {
                            itemsIndexed(countries) { index, country ->
                                CountryItem(
                                    country = country,
                                    isSelected = selectedItems.value.contains(index),
                                    onClick = {
                                        // Menambah atau menghapus indeks item yang dipilih
                                        selectedItems.value =
                                            if (selectedItems.value.contains(index)) {
                                                selectedItems.value - index
                                            } else {
                                                selectedItems.value + index
                                            }
                                        selectedCountries =
                                            if (selectedCountries.contains(country.name)) {
                                                selectedCountries - country.name
                                            } else {
                                                selectedCountries + country.name
                                            }
                                    }
                                )
                            }
                        }
                    }
                }
                // == AKHIR PILIHAN PREFERENSI NEGARA ==
            }
            Button(
                onClick = {
                    viewModel.savePreferensiNegara(selectedCountries)
                    if (userEmail != null) {
                        viewModel.saveUserPreferences(userEmail)
                    }
                    navController.navigate(Screen.MainView.route) {
                        popUpTo(Screen.OnboardingScreen.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Selesai",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@Composable
fun CountryItem(country: Country, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                border = BorderStroke(
                    1.dp,
                    color = if (isSelected) Color(0xFF405E90) else Color.Gray.copy(
                        alpha = 0.5f
                    )
                ),
                shape = RoundedCornerShape(20)
            )
            .clickable {
                onClick()
            }
            .background(
                shape = RoundedCornerShape(20),
                color = if (isSelected) Color(0xFF405E90) else Color.Transparent
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = rememberAsyncImagePainter(model = country.flag),
                contentDescription = "Country Flag",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp)), // Mengatur ukuran gambar bendera
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            // nama negara
            val maxLength = 22 // Panjang maksimal yang diinginkan
            val text = if (country.name.length > maxLength) {
                "${country.name.substring(0, maxLength)}..."
            } else {
                country.name
            }
            Text(text = text, color = if (isSelected) Color.White else Color.Black)
        }
        if (isSelected) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Icon Check",
                    tint = Color.White
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}