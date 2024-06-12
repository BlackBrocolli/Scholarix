package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import id.ac.stiki.doleno.scholarix.view.auth.MyTopAppBar
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun PreferensiUser(navController: NavController, viewModel: MainViewModel, email: String) {

    val userPreferences by viewModel.userPreferences.observeAsState()
    val countries by viewModel.countries.observeAsState(emptyList())
    var searchText by remember { mutableStateOf("") }
    val filteredCountries by viewModel.filteredCountries.observeAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        if (countries.isEmpty()) {
            viewModel.fetchCountries()
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(title = "Preferensi Pengguna") {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Negara",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // TODO: membuat fitur agar bisa menambahkan negara preferensi
                // TODO: ketika mencari negara, munculkan daftar negara yang sesuai dari db
                // TODO: ketika dipilih, tambahkan chip baru
                // TODO: Tambahkan ke variabel sementara mirip _userCountriesPreference
                // TODO: nanti ketika tombol simpan diklik, baru simpan ke db
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        // Search Bar
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { newValue ->
                                searchText = newValue
                            },
                            placeholder = { Text("Cari negara") },
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search // Menetapkan aksi IME menjadi "Search"
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            },
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.filterCountries(searchText)
                                        expanded = true
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Search Icon"
                                        )
                                    }
                                }
                            },
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    // Tindakan yang ingin dilakukan saat tombol "Search" pada keyboard ditekan
                                    viewModel.filterCountries(searchText)
                                    expanded = true
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ),
                        )
                    }
                    DropdownMenu(
                        expanded = expanded && filteredCountries.isNotEmpty(),
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .padding(horizontal = 16.dp) // Limit the height of the dropdown menu
                    ) {
                        filteredCountries.forEach { country ->
                            DropdownMenuItem(
                                onClick = {
                                    searchText = country.name
                                    expanded = false
                                    // Keep focus on the text field to keep the keyboard open
                                    focusRequester.requestFocus()
                                }
                            ) {
                                Text(text = country.name)
                            }
                        }
                    }
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp
                ) {
                    userPreferences?.countries?.forEach { country ->
                        InputChipNegara(
                            text = country,
                            onDismiss = { viewModel.togglePreference("country", country) }
                        )
                    }
                }
                Text(
                    text = "Jenjang",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    UserPreferenceSelectableCard(
                        text = "S1",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        viewModel = viewModel,
                        selected = userPreferences?.degrees?.contains("S1") ?: false,
                        preferenceType = "degree"
                    )
                    UserPreferenceSelectableCard(
                        text = "S2",
                        modifier = Modifier
                            .weight(1f),
                        viewModel = viewModel,
                        selected = userPreferences?.degrees?.contains("S2") ?: false,
                        preferenceType = "degree"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    UserPreferenceSelectableCard(
                        text = "S3",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        viewModel = viewModel,
                        selected = userPreferences?.degrees?.contains("S3") ?: false,
                        preferenceType = "degree"
                    )
                    UserPreferenceSelectableCard(
                        text = "Lainnya",
                        modifier = Modifier
                            .weight(1f),
                        viewModel = viewModel,
                        selected = userPreferences?.degrees?.contains("Lainnya") ?: false,
                        preferenceType = "degree"
                    )
                }
                Text(
                    text = "Tipe Pendanaan",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    UserPreferenceSelectableCard(
                        text = "Fully Funded",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        viewModel = viewModel,
                        selected = userPreferences?.fundingStatus?.contains("Fully Funded")
                            ?: false,
                        preferenceType = "funding"
                    )
                    UserPreferenceSelectableCard(
                        text = "Partial Funded",
                        modifier = Modifier
                            .weight(1f),
                        viewModel = viewModel,
                        selected = userPreferences?.fundingStatus?.contains("Partial Funded")
                            ?: false,
                        preferenceType = "funding"
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),

                    onClick = {
                        viewModel.updateUserPreferences(email, context)
                        navController.navigateUp()
                    },
                ) {
                    Text(text = "Simpan")
                }
            }
        }
    }
}

@Composable
fun UserPreferenceSelectableCard(
    modifier: Modifier = Modifier,
    text: String,
    viewModel: MainViewModel,
    selected: Boolean,
    preferenceType: String
) {
    var isSelected by remember { mutableStateOf(selected) }

    val backgroundColor = if (isSelected) Color.White else Color(0xFFE0E0E0)
    val borderColor = if (isSelected) Color(0xFF8F79E8) else Color.Transparent
    val textColor = if (isSelected) Color(0xFF8F79E8) else Color.Black

    Card(
        modifier = modifier
            .clickable {
                isSelected = !isSelected
                viewModel.togglePreference(preferenceType, text)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputChipNegara(
    text: String,
    onDismiss: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    val customColor = Color(0xFF8F79E8)

    InputChip(
        modifier = Modifier
            .border(1.dp, customColor, shape = MaterialTheme.shapes.small),
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = {
            Text(
                text,
                fontSize = 14.sp,
                color = customColor
            )
        },
        selected = enabled,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
                modifier = Modifier.size(16.dp),
                tint = customColor
            )
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = Color.Transparent,
            selectedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            selectedLabelColor = customColor,
            selectedTrailingIconColor = customColor,
        ),
    )
}