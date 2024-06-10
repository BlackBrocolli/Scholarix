package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import id.ac.stiki.doleno.scholarix.view.auth.MyTopAppBar
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel

@Composable
fun PreferensiUser(navController: NavController, viewModel: MainViewModel) {

    var searchText by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            MyTopAppBar(title = "Preferensi Pengguna") {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
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
                SelectableCard(
                    text = "S1",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    viewModel = viewModel
                )
                SelectableCard(
                    text = "S2",
                    modifier = Modifier
                        .weight(1f),
                    viewModel = viewModel
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                SelectableCard(
                    text = "S3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    viewModel = viewModel
                )
                SelectableCard(
                    text = "Lainnya",
                    modifier = Modifier
                        .weight(1f),
                    viewModel = viewModel
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
                SelectableCard(
                    text = "Fully Funded",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    viewModel = viewModel
                )
                SelectableCard(
                    text = "Partial Funded",
                    modifier = Modifier
                        .weight(1f),
                    viewModel = viewModel
                )
            }
            Text(
                text = "Negara",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
//                        viewModel.setSearchText(it)
//                        viewModel.searchScholarshipsByName(searchText)
                        searchText = it
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
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
//                                viewModel.resetSearching()
                                searchText = ""
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
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                val countries = listOf("Malaysia", "Indonesia", "Canada", "Bhutan")
                countries.forEach { country ->
                    InputChipNegara(text = country) {}
                }
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