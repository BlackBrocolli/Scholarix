package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.R
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.view.auth.MyTopAppBar

@Composable
fun KebijakanPrivasi(navController: NavController) {
    Scaffold(
        topBar = {
            MyTopAppBar(title = "Kebijakan Privasi") {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Kebijakan Privasi",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Row {
                Text(text = "Lihat juga: ", fontSize = 14.sp)
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Ketentuan Penggunaan")
                        }
                    },
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.KetentuanPenggunaan.route)
                    }
                )
            }
            Text(
                text = "Informasi Pengumpulan Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_1),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = "Cara Pengumpulan Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_2),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = "Tujuan Pengumpulan Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_3),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = "Penggunaan Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_4),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = "Pemberian Data kepada Pihak Ketiga",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_5),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = "Perlindungan Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_6),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = "Perubahan Kebijakan Privasi",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_7),
                fontSize = 14.sp, lineHeight = 18.sp
            )
            Text(
                text = stringResource(id = R.string.kebijakan_privasi_8),
                fontSize = 14.sp, lineHeight = 18.sp
            )
        }
    }
}