package id.ac.stiki.doleno.scholarix.view.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.R
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.ui.theme.poppinsFontFamily

@Composable
fun OnBoardingScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "Selamat datang di Scholarix",
                fontSize = 22.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colors.onSurface
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(Screen.SecondOnboarding.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.LightGray,
                    containerColor = Color(0xFF8F79E8),
                )
            ) {
                Text(
                    text = "Lanjutkan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
            TextButton(
                onClick = {
                    navController.navigate(Screen.MainView.route) {
                        popUpTo(Screen.OnboardingScreen.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
//            colors = ButtonDefaults.buttonColors(Color(0xFF4A5391))
            ) {
                Text(
                    text = "Tambahkan preferensi nanti",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily,
                    color = Color(0xFF8F79E8)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun OnboardingPreview() {
//    ScholarixTheme {
//        OnBoardingScreen()
//    }
//}