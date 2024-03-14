package id.ac.stiki.doleno.scholarix

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.stiki.doleno.scholarix.ui.theme.ScholarixTheme
import id.ac.stiki.doleno.scholarix.ui.theme.openSansFontFamily

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
                    OnBoardingScreen()
                }
            }
        }
    }
}

@Composable
fun OnBoardingScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Selamat datang di Scholarix",
                fontSize = 22.sp,
                fontWeight = FontWeight(600)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Mari kita mulai dengan menyesuaikan pencarian beasiswa sesuai dengan kebutuhan Anda",
                modifier = Modifier.width(300.dp),
                textAlign = TextAlign.Center,
                fontFamily = openSansFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Gray,
                style = TextStyle(
                    lineHeight = 20.sp
                )
            )
//            Image(painter = painterResource(id = R.drawable.un), contentDescription = )
        }
        Button(
            onClick = {
                Toast.makeText(context, "Ayo mulai!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xFF4A5391))
        ) {
            Text(text = "Lanjutkan", fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScholarixTheme {
        OnBoardingScreen()
    }
}