package id.ac.stiki.doleno.scholarix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.DummyBeasiswa
import id.ac.stiki.doleno.scholarix.model.Lokasi
import id.ac.stiki.doleno.scholarix.ui.theme.ScholarixTheme
import id.ac.stiki.doleno.scholarix.view.main.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScholarixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Navigation()
//                    TesTampilanLazyRow()
                    ProfileScreen()
                }
            }
        }
    }
}

// Tes tampilan beasiswa menggunakan lazyRow
@Composable
fun TesTampilanLazyRow() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyRow {
            items(DummyBeasiswa.beasiswaList) { beasiswa ->
                BeasiswaItem(beasiswa = beasiswa)
            }
        }
    }
}

@Composable
fun BeasiswaItem(beasiswa: Beasiswa) {
    OutlinedCard(
        modifier = Modifier
            .padding(4.dp)
            .widthIn(max = 260.dp)
            .height(175.dp),
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
                Row {
                    // Menampilkan setiap derajat dalam kartu terpisah
                    beasiswa.degree.forEach { degree ->
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
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
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
                        Text(
                            text = beasiswa.lokasi.kota,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
//                        color = Color(0xCC17181A)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Beasiswa ${beasiswa.pendanaan}", fontSize = 12.sp)
                Text(
                    text = beasiswa.nama,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .heightIn(32.dp) // Set tinggi minimum
                )
            }
//            Spacer(modifier = Modifier.heightIn(16.dp))
            // Kartu Deadline
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    Text(
                        text = beasiswa.deadline,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BeasiswaItemPreview() {
//    val beasiswa = Beasiswa(
//        nama = "Beasiswa ABC",
//        pendanaan = "Fully Funded",
//        deadline = "May 12, 2024",
//        degree = listOf("S1", "S2", "S3"),
//        lokasi = Lokasi("Jakarta", "Indonesia")
//    )
//    BeasiswaItem(beasiswa = beasiswa)
//}