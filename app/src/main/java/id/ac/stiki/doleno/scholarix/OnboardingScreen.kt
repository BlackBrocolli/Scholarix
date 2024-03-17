package id.ac.stiki.doleno.scholarix

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.stiki.doleno.scholarix.ui.theme.ScholarixTheme
import id.ac.stiki.doleno.scholarix.ui.theme.poppinsFontFamily

var screenIndex by mutableStateOf(0)

@Composable
fun OnBoardingScreen() {
    when (screenIndex) {
        0 -> FirstOnboarding()
        1 -> SecondOnboarding()
        2 -> ThirdOnboarding()
        3 -> FourthOnboarding()
    }
}

@Composable
fun FirstOnboarding() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "Selamat datang di Scholarix",
                fontSize = 22.sp,
                fontWeight = FontWeight(600)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Mari kita mulai dengan menyesuaikan pencarian beasiswa sesuai dengan kebutuhan Anda",
                modifier = Modifier.width(280.dp),
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                style = TextStyle(
                    lineHeight = 20.sp
                )
            )
            Image(
                painter = painterResource(id = R.drawable.onboarding),
                contentDescription = "Image for onboarding screen",
                modifier = Modifier.fillMaxSize()
            )
        }
        Button(
            onClick = {
                screenIndex++
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text(
                text = "Lanjutkan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = {
                Toast.makeText(context, "Nanti saja!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xFF4A5391))
        ) {
            Text(
                text = "Tambahkan preferensi nanti",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        }
    }
}

@Composable
fun SecondOnboarding() {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0.33f) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { screenIndex-- }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Icon arrow back"
                    )
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.width(160.dp)
                )
                TextButton(onClick = {
                    screenIndex++
                }) {
                    Text(text = "Skip")
                }
            }
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
                    text = "Level Pendidikan",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                // == PILIHAN LEVEL PENDIDIKAN ==
                var selectedRows by remember { mutableStateOf(List(4) { false }) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (selectedRows[0]) Color(0xFF405E90) else Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shape = RoundedCornerShape(20)
                        )
                        .height(64.dp)
                        .clickable {
                            selectedRows = selectedRows
                                .toMutableList()
                                .also {
                                    it[0] = !it[0]
                                }
                        }
                        .background(
                            shape = RoundedCornerShape(20),
                            color = if (selectedRows[0]) Color(0xFF405E90) else Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Sarjana (S1)",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedRows[0]) Color.White else Color.Black
                        )
                    }
                    if (selectedRows[0]) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (selectedRows[1]) Color(0xFF405E90) else Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shape = RoundedCornerShape(20)
                        )
                        .height(64.dp)
                        .clickable {
                            selectedRows = selectedRows
                                .toMutableList()
                                .also {
                                    it[1] = !it[1]
                                }
                        }
                        .background(
                            shape = RoundedCornerShape(20),
                            color = if (selectedRows[1]) Color(0xFF405E90) else Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Magister (S2)",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedRows[1]) Color.White else Color.Black
                        )
                    }
                    if (selectedRows[1]) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (selectedRows[2]) Color(0xFF405E90) else Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shape = RoundedCornerShape(20)
                        )
                        .height(64.dp)
                        .clickable {
                            selectedRows = selectedRows
                                .toMutableList()
                                .also {
                                    it[2] = !it[2]
                                }
                        }
                        .background(
                            shape = RoundedCornerShape(20),
                            color = if (selectedRows[2]) Color(0xFF405E90) else Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Doktor (S3)",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedRows[2]) Color.White else Color.Black
                        )
                    }
                    if (selectedRows[2]) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (selectedRows[3]) Color(0xFF405E90) else Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shape = RoundedCornerShape(20)
                        )
                        .height(64.dp)
                        .clickable {
                            selectedRows = selectedRows
                                .toMutableList()
                                .also {
                                    it[3] = !it[3]
                                }
                        }
                        .background(
                            shape = RoundedCornerShape(20),
                            color = if (selectedRows[3]) Color(0xFF405E90) else Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Lainnya",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedRows[3]) Color.White else Color.Black
                        )
                    }
                    if (selectedRows[3]) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Icon Check",
                                tint = Color.White
                            )
                        }
                    }
                }
                // == AKHIR PILIHAN LEVEL PENDIDIKAN ==
            }
            Button(
                onClick = {
                    screenIndex++
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(
                    text = "Selanjutnya",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}

@Composable
fun ThirdOnboarding() {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0.66f) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { screenIndex-- }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Icon arrow back"
                    )
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.width(160.dp)
                )
                TextButton(onClick = {
                    screenIndex++
                }) {
                    Text(text = "Skip")
                }
            }
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
                    text = "Tipe Pendanaan",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                // == PILIHAN LEVEL PENDIDIKAN ==
                var selectedRows by remember { mutableStateOf(-1) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (selectedRows == 1) Color(0xFF405E90) else Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shape = RoundedCornerShape(20)
                        )
                        .height(64.dp)
                        .clickable {
                            selectedRows = 1
                        }
                        .background(
                            shape = RoundedCornerShape(20),
                            color = if (selectedRows == 1) Color(0xFF405E90) else Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Didanai Sepenuhnya",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedRows == 1) Color.White else Color.Black
                        )
                    }
                    if (selectedRows == 1) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (selectedRows == 2) Color(0xFF405E90) else Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shape = RoundedCornerShape(20)
                        )
                        .height(64.dp)
                        .clickable {
                            selectedRows = 2
                        }
                        .background(
                            shape = RoundedCornerShape(20),
                            color = if (selectedRows == 2) Color(0xFF405E90) else Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Didanai Sebagian",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedRows == 2) Color.White else Color.Black
                        )
                    }
                    if (selectedRows == 2) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Icon Check",
                                tint = Color.White
                            )
                        }
                    }
                }
                // == AKHIR PILIHAN LEVEL PENDIDIKAN ==
            }
            Button(
                onClick = {
                    screenIndex++
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(
                    text = "Selanjutnya",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}

@Composable
fun FourthOnboarding() {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(1f) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    screenIndex--
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Icon arrow back"
                    )
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.width(160.dp)
                )
                TextButton(onClick = {
                    Toast.makeText(
                        context,
                        "Pindah ke halaman utama!",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(text = "Skip")
                }
            }
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
                LazyColumn {

                }
                // == AKHIR PILIHAN PREFERENSI NEGARA ==
            }
            Button(
                onClick = {
                    Toast.makeText(context, "Pindah ke halaman utama!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
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

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    ScholarixTheme {
        OnBoardingScreen()
    }
}