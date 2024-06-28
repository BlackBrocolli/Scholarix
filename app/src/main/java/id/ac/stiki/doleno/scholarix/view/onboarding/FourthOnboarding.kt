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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    // Fetch countries
    val countryViewModel: CountryViewModel = viewModel()
    val viewState by countryViewModel.countriesState
    val userEmail = userData.email
    val isLoading by viewModel.isLoadingSaveUserPreference.observeAsState(initial = false)
    val searchText by countryViewModel.searchText.observeAsState("")
    val isSearching by countryViewModel.isSearching.observeAsState(false)
    val searchedCountries by countryViewModel.searchedCountries.observeAsState(emptyList())

    // State untuk melacak nama negara yang dipilih
    val selectedCountries by viewModel.selectedCountries.observeAsState(setOf())

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
                .background(Color.White)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Preferensi Negara",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(32.dp))

                // == PILIHAN PREFERENSI NEGARA ==
                when {
                    viewState.loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF8F79E8))
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
                            item {
                                // SEARCH BAR
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                ) {
                                    // Search Bar
                                    OutlinedTextField(
                                        value = searchText,
                                        onValueChange = {
                                            countryViewModel.setSearchText(it)
                                        },
                                        placeholder = { Text("Cari negara") },
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
                                            focusedBorderColor = Color(0xFF8F79E8),
                                            cursorColor = MaterialTheme.colors.onSurface,
                                            focusedTextColor = MaterialTheme.colors.onSurface,
                                            unfocusedTextColor = MaterialTheme.colors.onSurface
                                        ),
                                        trailingIcon = {
                                            if (searchText.isNotEmpty()) {
                                                IconButton(onClick = {
                                                    countryViewModel.resetSearching()
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
                            }
                            itemsIndexed(if (isSearching) searchedCountries else viewState.list) { _, country ->
                                CountryItem(
                                    country = country,
                                    isSelected = selectedCountries.contains(country.name),
                                    onClick = {
                                        // Menambah atau menghapus negara yang dipilih
                                        val newSelectedCountries = if (selectedCountries.contains(country.name)) {
                                            selectedCountries - country.name
                                        } else {
                                            selectedCountries + country.name
                                        }
                                        viewModel.savePreferensiNegara(newSelectedCountries)
                                    }
                                )
                            }
                        }
                    }
                }
                // == AKHIR PILIHAN PREFERENSI NEGARA ==
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
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
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.LightGray,
                        containerColor = Color(0xFF8F79E8),
                    )
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
                            fontFamily = poppinsFontFamily,
                            color = Color.White
                        )
                    }
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
                    color = if (isSelected) Color(0xFF8F79E8) else Color.Gray.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(20)
            )
            .clickable {
                onClick()
            }
            .background(
                shape = RoundedCornerShape(20),
                color = if (isSelected) Color(0xFF8F79E8) else Color.Transparent
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
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Nama negara
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