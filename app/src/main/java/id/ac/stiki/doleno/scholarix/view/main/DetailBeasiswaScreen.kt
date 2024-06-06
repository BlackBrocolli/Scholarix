package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.getValue
import id.ac.stiki.doleno.scholarix.model.BeasiswaIndonesia
import id.ac.stiki.doleno.scholarix.model.google.UserData

@Composable
fun DetailBeasiswaScreen(
    id: String,
    type: String,
    viewModel: MainViewModel,
    navController: NavController,
    userData: UserData
) {

    val scholarship = viewModel.scholarship.observeAsState(initial = Beasiswa())
    val indonesiaScholarship =
        viewModel.indonesiaScholarship.observeAsState(initial = BeasiswaIndonesia())
    val isLoadingScholarship = viewModel.isLoadingScholarship.observeAsState(initial = false)
    val isErrorScholarship = viewModel.isErrorScholarship.observeAsState(initial = false)
    val favorites by viewModel.favorites.observeAsState(initial = emptyList())

    val userEmail = userData.email

    LaunchedEffect(Unit) {
        viewModel.fetchScholarshipById(id, type)
        userEmail?.let {
            viewModel.loadUserFavorites(it)
        }
    }

    val isFavorite = when (type) {
        "beasiswaLuarNegeri" -> scholarship.value.name in favorites
        "beasiswaIndonesia" -> indonesiaScholarship.value.name in favorites
        else -> false // Jika type tidak cocok, defaultkan ke false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 1.5.dp,
                title = { },
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back Icon"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            userEmail?.let { email ->
                                val name = when (type) {
                                    "beasiswaLuarNegeri" -> scholarship.value.name
                                    "beasiswaIndonesia" -> indonesiaScholarship.value.name
                                    else -> "" // Menangani kasus ketika type tidak cocok dengan yang diharapkan
                                }

                                if (name.isNotEmpty()) {
                                    viewModel.toggleFavorite(email, name)
                                }
                            }
                        }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite Icon",
                            tint = if (isFavorite) Color.Red else Color.Black
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val url = when (type) {
                        "beasiswaLuarNegeri" -> scholarship.value.link
                        "beasiswaIndonesia" -> indonesiaScholarship.value.link
                        else -> "" // Menangani kasus ketika type tidak cocok dengan yang diharapkan
                    }
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    navController.context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Kunjungi Website")
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoadingScholarship.value -> {
                    // Display a loading animation or indicator
                    CircularProgressIndicator()
                }

                isErrorScholarship.value -> {
                    // Show an error message and possibly a retry button
                    Text("Failed to load scholarship details. Tap to retry.",
                        modifier = Modifier
                            .clickable { viewModel.fetchScholarshipById(id, type) }
                            .padding(16.dp)
                    )
                }

                else -> {
                    // Adjust UI based on collectionType
                    when (type) {
                        "beasiswaLuarNegeri" -> {
                            DetailBeasiswaContent(scholarship.value)
                        }

                        "beasiswaIndonesia" -> {
                            DetailBeasiswaIndonesiaContent(indonesiaScholarship.value)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailBeasiswaIndonesiaContent(beasiswa: BeasiswaIndonesia) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = beasiswa.name,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun DetailBeasiswaContent(beasiswa: Beasiswa) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = beasiswa.name,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
        val beasiswaText = if (beasiswa.fundingStatus != null) {
            "Beasiswa ${beasiswa.fundingStatus}"
        } else {
            "Beasiswa ${beasiswa.amount}"
        }
        Text(
            text = beasiswaText,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            // Menampilkan setiap derajat dalam kartu terpisah
            if (beasiswa.degrees.isNotEmpty()) {
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
            }
//                Spacer(modifier = Modifier.width(2.dp))
            beasiswa.city?.let { city ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x80D9D9D9),
                    )
                ) {
                    Text(
                        text = city,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        // color = Color(0xCC17181A)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // CARD DEADLINE
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80D9D9D9),
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Deadline", fontSize = 12.sp)
                beasiswa.deadline?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // CARD TENTANG
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Tentang", fontWeight = FontWeight.Bold)

                if (beasiswa.country.isNotEmpty()) {
                    Text(
                        text = "LOKASI NEGARA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = beasiswa.country, fontSize = 14.sp)
                    }
                }

                if (beasiswa.institution.isNotEmpty()) {
                    if (beasiswa.country.isNotEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                    Text(
                        text = "INSTITUSI ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = beasiswa.institution, fontSize = 14.sp)
                    }
                }

                if (beasiswa.opportunities.isNotEmpty()) {
                    if (beasiswa.country.isNotEmpty() || beasiswa.institution.isNotEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                    Text(
                        text = "JUMLAH BEASISWA ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = beasiswa.opportunities, fontSize = 14.sp)
                    }
                }

                if (beasiswa.duration.isNotEmpty()) {
                    if (beasiswa.country.isNotEmpty() || beasiswa.institution.isNotEmpty() || beasiswa.opportunities.isNotEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                    Text(
                        text = "DURASI",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = beasiswa.duration, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // CARD BENEFIT
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.Blue),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            if (beasiswa.benefitsHtml.isNotEmpty()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Manfaat", fontWeight = FontWeight.Bold, color = Color.Blue)
                    // Regex pattern untuk menghilangkan semua tag HTML kecuali <hr>, <p>, <ul>, <li>, </li>, <strong>
                    val cleanHtmlPattern = Regex("<(?!hr|p|ul|li|/li|/strong).*?>")

                    // Fungsi untuk membersihkan teks dari tag HTML dan mengkonversi tag <ul> dan <li> menjadi teks daftar
                    fun cleanAndFormatText(html: String): String {
                        return html.replace(cleanHtmlPattern, "")
                            .replace("<ul>", "\n") // Ganti <ul> dengan newline
                            .replace("</ul>", "")
                            .replace("<li>", "") // Hapus <li>
                            .replace("</li>", "")
                            .replace("</strong>", "")
                            .replace("&nbsp;", " ")
                    }

                    beasiswa.benefitsHtml.forEach { benefit ->
                        val cleanText = when {
                            benefit.startsWith("<hr>") -> {
                                // Skip <hr> tag
                                null
                            }

                            benefit.startsWith("<p>") -> {
                                // Display text inside <p> tag
                                cleanAndFormatText(benefit.removePrefix("<p>"))
                            }

                            benefit.startsWith("<ul>") -> {
                                // Extract list items from <ul> tag
                                val listItems = benefit
                                    .removePrefix("<ul>").removeSuffix("</ul>")
                                    .split("<li>").filter { it.isNotEmpty() }
                                    .map { cleanAndFormatText(it) }
                                listItems.joinToString("\u2022 ", prefix = "\u2022 ")
                            }

                            else -> {
                                // Default case, display text
                                cleanAndFormatText(benefit)
                            }
                        }

                        // Display the clean text
                        cleanText?.let {
                            Text(text = it, fontSize = 14.sp)
                        }
                    }
                }
            } else if (beasiswa.description.isNotEmpty()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Deskripsi", fontWeight = FontWeight.Bold, color = Color.Blue)
                    Text(text = beasiswa.description, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // CARD PERSYARATAN
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Persyaratan", fontWeight = FontWeight.Bold)
                Text(
                    text = "KEWARGANEGARAAN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\u2022", // Unicode untuk simbol bullet
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )
                    Text(text = beasiswa.eligibleNationals, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "SERTIFIKAT BAHASA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (beasiswa.languageRequirements.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "\u2022", // Unicode untuk simbol bullet
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(text = beasiswa.languageRequirements, fontSize = 14.sp)
                    }
                } else {
                    Text(text = "-")
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "DOKUMEN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (beasiswa.documentsHtml.isNotEmpty()) {
                    beasiswa.documentsHtml.forEach { document ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "\u2022", // Unicode untuk simbol bullet
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                            )
                            Text(text = document, fontSize = 14.sp)
                        }
                    }
                } else {
                    Text(text = "-")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // CARD OTHER CRITERIA
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            if (beasiswa.otherCriteria.isNotEmpty()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Kriteria Lain", fontWeight = FontWeight.Bold)
                    Text(text = beasiswa.otherCriteria, fontSize = 14.sp)
                }
            }
        }
    }
}
