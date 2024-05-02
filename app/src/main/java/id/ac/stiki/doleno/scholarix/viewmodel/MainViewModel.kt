package id.ac.stiki.doleno.scholarix.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.DummyBeasiswa
import id.ac.stiki.doleno.scholarix.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class MainViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Home)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    // Tambahkan properti inputUrutkanBerdasar
    private val _inputUrutkanBerdasar: MutableState<String> = mutableStateOf("Deadline Terdekat")
    val inputUrutkanBerdasar: MutableState<String>
        get() = _inputUrutkanBerdasar

    fun setInputUrutkanBerdasar(value: String) {
        _inputUrutkanBerdasar.value = value
    }

    fun getABeasiswaById(id: Long): Beasiswa? {
        return DummyBeasiswa.beasiswaList.find { it.id == id }
    }

    // === WEB SCRAPING USING JSOUP ===
    // Metode untuk mengambil detail beasiswa di background thread
    fun fetchScholarshipDetails() {
        viewModelScope.launch {
            val baseUrl = "https://scholarshipsdb.org/"
            try {
                // Mengambil dokumen HTML dari halaman utama di IO Dispatcher
                val doc = withContext(Dispatchers.IO) {
                    Jsoup.connect(baseUrl).get()
                }

                // Ekstrak semua link dari elemen yang berisi link ke detail beasiswa
                val scholarships = doc.select("h2 a[href]")

                // Iterasi melalui setiap link dan akses halaman detail
                scholarships.forEach { element ->
                    val detailUrl = element.attr("abs:href")
                    Log.d("Link dari web", detailUrl)
//                    fetchDetailPage(detailUrl)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchDetailPage(url: String) {
        viewModelScope.launch {
            try {
                // Menggunakan Dispatchers.IO untuk operasi I/O network
                val detailDoc = withContext(Dispatchers.IO) {
                    Jsoup.connect(url).get()
                }

                // Ekstraksi data seperti judul, deadline, dan deskripsi
                val title =
                    detailDoc.select("h1").text()  // Pastikan selektor sesuai dengan struktur HTML
                val deadline =
                    detailDoc.select("time[datetime]").last()?.text()  // Ambil elemen time terakhir
                val description = detailDoc.select("div.description")
                    .text()  // Asumsi ada div dengan class 'description'

                // Logging atau update UI di sini jika diperlukan
                Log.d(
                    "Scraping Result",
                    "Title: $title, Deadline: $deadline, Description: $description"
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}