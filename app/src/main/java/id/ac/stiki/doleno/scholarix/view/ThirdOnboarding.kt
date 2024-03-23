package id.ac.stiki.doleno.scholarix.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.ui.theme.poppinsFontFamily

@Composable
fun ThirdOnboarding(navController: NavController) {

    Scaffold(
        topBar = {
            OnboardingTopBar(
                progress = 0.66f,
                onBackNavClicked = { navController.navigateUp() },
                onSkipButtonClicked = { navController.navigate(Screen.FourthOnboarding.route) }
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
                    text = "Tipe Pendanaan",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                // == PILIHAN LEVEL PENDIDIKAN ==
                var selectedRows by remember { mutableIntStateOf(-1) }
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
                    navController.navigate(Screen.FourthOnboarding.route)
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